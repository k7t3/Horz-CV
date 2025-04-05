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
import com.google.gwt.user.client.Window;
import elemental2.dom.DomGlobal;
import io.github.k7t3.horzcv.client.model.EmbeddedChatFrame;
import io.github.k7t3.horzcv.client.model.EmbeddedChatFrameBuilder;
import io.github.k7t3.horzcv.client.model.LiveStreaming;
import io.github.k7t3.horzcv.client.model.LiveStreamingDetector;
import io.github.k7t3.horzcv.client.model.StreamingService;
import io.github.k7t3.horzcv.client.presenter.twitch.TwitchChannelChatFrameBuilder;
import io.github.k7t3.horzcv.client.presenter.twitch.TwitchChannelDetector;
import io.github.k7t3.horzcv.client.presenter.youtube.YoutubeLiveChatFrameBuilder;
import io.github.k7t3.horzcv.client.presenter.youtube.YoutubeLiveDetector;
import io.github.k7t3.horzcv.client.view.ChatListView;
import io.github.k7t3.horzcv.client.view.LayoutEvent;
import io.github.k7t3.horzcv.client.view.Pages;
import io.github.k7t3.horzcv.client.view.Slots;
import org.dominokit.domino.api.client.annotations.presenter.*;
import org.dominokit.domino.api.client.mvp.presenter.ViewablePresenter;
import org.dominokit.domino.history.DominoHistory;
import org.dominokit.domino.history.StateToken;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@SuppressWarnings("SimplifyStreamApiCallChains")
@AutoRoute(token = Pages.CHAT)
@AutoReveal
@Slot(Slots.CONTENT)
@PresenterProxy(name = Presenters.CHAT)
@DependsOn(@EventsGroup(LayoutEvent.class))
public class ChatListPresenter
        extends ViewablePresenter<ChatListView>
        implements ChatListView.ChatListUiHandler {

    private static final Logger LOGGER = Logger.getLogger(ChatListPresenter.class.getName());

    @RoutingState
    protected DominoHistory.State state;

    private final LinkedList<EmbeddedChatFrame> chatFrames = new LinkedList<>();

    /**
     * 配信サービスに対応する埋め込みのチャットフレームビルダーのマップ
     */
    private final Map<StreamingService, EmbeddedChatFrameBuilder> frameBuilders = Map.of(
            StreamingService.TWITCH, new TwitchChannelChatFrameBuilder(Window.Location.getHostName()),
            StreamingService.YOUTUBE, new YoutubeLiveChatFrameBuilder(Window.Location.getHostName())
    );

    private final Map<StreamingService, LiveStreamingDetector> detectors = Map.of(
            StreamingService.TWITCH, new TwitchChannelDetector(),
            StreamingService.YOUTUBE, new YoutubeLiveDetector()
    );

    /**
     * コンストラクタ
     */
    public ChatListPresenter() {
    }

    @Override
    public void onTokenChanged(DominoHistory.State state) {
        LOGGER.info("onTokenChanged: " + state.token().fragment());
        updateStreamsFromToken(state.token().fragment());
        updateView();
    }

    @OnRouting
    public void onRouting() {
        String fragment = state.token().fragment();
        LOGGER.info("onRouting: " + fragment);

        saveSession(fragment);

        var frames = LiveStreaming.fromTokens(fragment)
                .stream()
                .map(EmbeddedChatFrame::new)
                .collect(Collectors.toList());

        chatFrames.addAll(frames);
    }

    @OnBeforeReveal
    public void setUpStreams() {
        LOGGER.info("setUpStreams: " + chatFrames);
        updateView();
    }

    @OnReveal
    public void onRevealed() {
        // 表示するストリームがない場合はホームにリダイレクトする
        if (chatFrames.isEmpty()) {
            LOGGER.info("No fragments, redirecting to home");
            redirectToHome();
        }
    }

    @ListenTo(event = ColorSchemeEvent.class)
    public void onEventReceived(ColorSchemeEvent event) {
        var isDarkMode = event.colorScheme() == ColorScheme.DARK;
        updateEmbeddedChatFrames(isDarkMode);
        view.setChatList(Collections.unmodifiableList(chatFrames));
    }

    @Override
    public void onListUpdated(List<EmbeddedChatFrame> chatFrames) {
        var history = history();
        var current = history.currentToken();

        if (chatFrames.isEmpty()) {
            redirectToHome();
            return;
        }

        var chatList = chatFrames.stream()
                .map(EmbeddedChatFrame::getStream)
                .collect(Collectors.toList());

        String fragments = LiveStreaming.toTokens(chatList);
        saveSession(fragments);

        // 履歴の更新
        current.replaceAllFragments(fragments);

        // 履歴に新しいトークンをセット
        history.pushState(StateToken.of(current));

        // ストリームリストの更新
        this.chatFrames.clear();
        this.chatFrames.addAll(chatFrames);
    }

    @Override
    public void onLinkOpening(EmbeddedChatFrame chatFrame) {
        var stream = chatFrame.getStream();
        DomGlobal.window.open(stream.getUri(), "_blank");
    }

    /**
     * トークンからストリームリストを更新する
     */
    private void updateStreamsFromToken(String token) {
        saveSession(token);

        var frames = LiveStreaming.fromTokens(token)
                .stream()
                .map(EmbeddedChatFrame::new)
                .collect(Collectors.toList());

        chatFrames.clear();
        chatFrames.addAll(frames);
    }

    /**
     * ビューを現在のストリームリストで更新する
     */
    private void updateView() {
        var isDarkMode = ThemeManager.INSTANCE.getColorScheme() == ColorScheme.DARK;
        updateEmbeddedChatFrames(isDarkMode);
        view.setChatList(Collections.unmodifiableList(chatFrames));
    }

    /**
     * ストリームフレームを準備する
     */
    private void updateEmbeddedChatFrames(boolean isDarkMode) {
        for (var frame : chatFrames) {
            var stream = frame.getStream();
            var service = stream.getService();
            var builder = frameBuilders.get(service);
            var detector = detectors.get(service);

            if (builder == null || detector == null) {
                continue;
            }

            String uri = detector.construct(stream.getId());
            stream.setUri(uri);

            String frameHtml = builder.build(stream.getId(), isDarkMode);
            frame.setIframe(frameHtml);
        }
    }

    private void saveSession(String token) {
        Storage storage = Storage.getSessionStorageIfSupported();
        if (storage != null) {
            storage.setItem(Storages.TOKEN, token);
        }
    }

    private void redirectToHome() {
        history().fireState(StateToken.of(Pages.HOME));
    }
}
