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
import io.github.k7t3.horzcv.client.view.Routes;
import io.github.k7t3.horzcv.client.view.HomeView;
import io.github.k7t3.horzcv.client.view.LayoutEvent;
import io.github.k7t3.horzcv.client.view.LiveStreamingFormView;
import io.github.k7t3.horzcv.client.view.Pages;
import io.github.k7t3.horzcv.client.view.Slots;
import io.github.k7t3.horzcv.client.view.Tokens;
import io.github.k7t3.horzcv.client.view.ui.LiveStreamingFormViewImpl;
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

@AutoRoute(token = Pages.HOME)
@AutoReveal
@Slot(Slots.CONTENT)
@PresenterProxy(name = Presenters.HOME)
@DependsOn(@EventsGroup(LayoutEvent.class))
public class HomePresenter extends ViewablePresenter<HomeView> implements HomeView.HomeUIHandlers, LiveStreamingFormView.LiveStreamingFormUIHandlers {

    private static final Logger LOGGER = Logger.getLogger(HomePresenter.class.getName());

    private static final int MAX_LIVE_STREAMING_FORMS = 8;

    private final List<LiveStreamingDetector> detectors;

    private final Map<LiveStreamingFormView, LiveStreamingPresenter> forms = new HashMap<>();

    public HomePresenter() {
        detectors = List.of(
                new TwitchChannelDetector(),
                new YoutubeLiveDetector()
        );
    }

    @OnActivated
    public void activated() {
        LOGGER.fine("HomePresenter activated");
    }

    @Override
    protected void onDeactivated() {
        LOGGER.fine("HomePresenter deactivated");
    }

    @OnRouting
    public void onRouting() {
        LOGGER.fine("HomePresenter onRouting");
    }

    @OnReveal
    public void onRevealed() {
        LOGGER.fine("HomePresenter onRevealed");

        List<LiveStreaming> streams;
        var session = Storage.getSessionStorageIfSupported();
        if (session != null) {
            var token = session.getItem(Storages.TOKEN);
            streams = LiveStreaming.fromTokens(token)
                    .stream()
                    .map(s -> {
                        var uri = detectors.stream()
                                .filter(d -> d.getService() == s.getService())
                                .map(d -> d.construct(s.getId()))
                                .findFirst()
                                .orElse(null);
                        if (uri != null) {
                            s.setUri(uri);
                            return s;
                        } else {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } else {
            streams = List.of();
        }

        for (var i = 0; i < 4; i++) {
            var view = new LiveStreamingFormViewImpl();
            var form = new LiveStreamingPresenter(detectors, view);
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
        var form = new LiveStreamingPresenter(detectors, view);
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
        LOGGER.info("HomePresenter onSubmitted: " + token);

        history().fireState(StateToken.of(Routes.CHAT + "#:" + Tokens.PARMS), TokenParameter.of(Tokens.PARMS, token));
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
