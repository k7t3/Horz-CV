/*
 * Copyright 2025 k7t3
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.k7t3.horzcv.client.presenter;

import com.google.gwt.storage.client.Storage;
import io.github.k7t3.horzcv.client.model.LiveStreaming;
import io.github.k7t3.horzcv.client.model.LiveStreamingDetector;
import io.github.k7t3.horzcv.client.presenter.twitch.TwitchChannelDetector;
import io.github.k7t3.horzcv.client.presenter.youtube.YoutubeLiveDetector;
import io.github.k7t3.horzcv.client.view.HomeView;
import io.github.k7t3.horzcv.client.view.LayoutEvent;
import io.github.k7t3.horzcv.client.view.LiveStreamingFormView;
import io.github.k7t3.horzcv.client.view.Routes;
import io.github.k7t3.horzcv.client.view.Slots;
import io.github.k7t3.horzcv.client.view.Tokens;
import io.github.k7t3.horzcv.client.view.ui.LiveStreamingFormViewImpl;
import io.github.k7t3.horzcv.shared.service.StreamerInfoServiceAsync;
import org.dominokit.domino.api.client.annotations.presenter.*;
import org.dominokit.domino.api.client.mvp.presenter.ViewablePresenter;
import org.dominokit.domino.history.StateToken;
import org.dominokit.domino.history.TokenParameter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@AutoRoute(token = Routes.HOME)
@AutoReveal
@Slot(Slots.CONTENT)
@PresenterProxy(name = Presenters.HOME)
@DependsOn(@EventsGroup(LayoutEvent.class))
public class HomePresenter extends ViewablePresenter<HomeView>
        implements HomeView.HomeUIHandlers,
        LiveStreamingFormView.LiveStreamingFormUIHandlers,
        WindowTitleProvider {

    private static final Logger LOGGER = Logger.getLogger(HomePresenter.class.getName());

    private static final int MAX_LIVE_STREAMING_FORMS = 8;

    // FIXME: 一時的にバックエンドサービスを使用しないように変更。
    private final StreamerInfoServiceAsync streamerInfoService = (url, callback) -> {
        // no-op
    };

    private final List<LiveStreamingDetector> detectors;

    private final Map<LiveStreamingFormView, LiveStreamingPresenter> forms = new HashMap<>();

    public HomePresenter() {
        detectors = List.of(
                new TwitchChannelDetector(),
                new YoutubeLiveDetector()
        );
    }

    /**
     * サービスとIDに基づいてURIを構成するメソッド。
     * 対応するURI検出の実装が見つからない時はnullを返す。
     */
    private LiveStreaming constructURI(LiveStreaming s) {
        var uri = detectors.stream()
                .filter(d -> d.getService() == s.getService())
                .map(d -> d.construct(s.getId()))
                .findFirst()
                .orElse(null);
        if (uri == null) {
            return null;
        }
        s.setUri(uri);
        return s;
    }

    /**
     * ビューの表示とストリームセッションデータに基づくライブストリーミングフォームの初期化を行います。
     * <p>
     * このメソッドは{@code @OnReveal}でアノテートされており、プレゼンターの表示サイクル中に実行されます。
     * 処理内容は以下の通りです:
     * <p>
     * 1. サポートされている場合、セッションストレージからライブストリーミングトークンを取得
     * 2. トークンから{@link LiveStreaming}オブジェクトのリストを生成し、
     * {@code constructURI}メソッドを通じてストリームのURIを構築
     * 3. 最大4つの{@code LiveStreamingFormView}インスタンスを作成し、
     * それぞれを{@code LiveStreamingPresenter}に関連付け
     * 4. フォームをこのプレゼンターのUIハンドラーとしてバインドし、ビューに追加
     * 5. 利用可能な場合、ストリームデータをフォームに割り当て
     * 6. ビューのアニメーションを開始
     * <p>
     * セッションストレージが未対応、またはトークンが存在しない場合、空のフォームが初期化されます。
     */
    @OnReveal
    public void onRevealed() {

        List<LiveStreaming> streams;
        var session = Storage.getSessionStorageIfSupported();
        if (session != null) {
            var token = session.getItem(Storages.TOKEN);
            streams = LiveStreaming.fromTokens(token)
                    .stream()
                    .map(this::constructURI)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } else {
            streams = List.of();
        }

        // 表示するフォームの数を決定
        // 8件以下のストリームを全て表示し、4件未満の場合は最低4件のフォームを作成
        int formsToCreate = Math.max(4, Math.min(streams.size(), MAX_LIVE_STREAMING_FORMS));
        LOGGER.info("onRevealed: " + streams + ", formsToCreate: " + formsToCreate);

        for (var i = 0; i < formsToCreate; i++) {
            var view = new LiveStreamingFormViewImpl();
            var form = new LiveStreamingPresenter(streamerInfoService, detectors, view);
            view.setUiHandlers(this);

            forms.put(view, form);
            this.view.addLiveStreamingForm(view);

            if (i < streams.size()) {
                var stream = streams.get(i);
                form.setStream(stream);
            }
        }

        view.startAnimation();
    }

    private void updateAddButton() {
        var disable = MAX_LIVE_STREAMING_FORMS <= forms.size();
        view.setDisableAddButton(disable);
    }

    @Override
    public void onAddedLiveStreamingForm(LiveStreamingFormView view) {
        var form = new LiveStreamingPresenter(streamerInfoService, detectors, view);
        forms.put(view, form);
        view.setUiHandlers(this);
        updateAddButton();
    }

    @Override
    public void onSubmitted() {
        LOGGER.info("HomePresenter onSubmitted");
        var items = forms.values()
                .stream()
                .filter(LiveStreamingPresenter::isValid)
                .map(LiveStreamingPresenter::getAsLiveStreaming)
                .collect(Collectors.toList());

        var token = LiveStreaming.toTokens(items);
        var names = title(items);

        var state = StateToken.of(Routes.CHAT + "#:" + Tokens.PARMS).title(names);
        var parameter = TokenParameter.of(Tokens.PARMS, token);
        history().fireState(state, parameter);
    }

    @Override
    public void onURIChanged(LiveStreamingFormView view, String uri) {
        // no-op
    }

    @Override
    public void onRemoved(LiveStreamingFormView view) {
        forms.remove(view);
        updateAddButton();
    }
}
