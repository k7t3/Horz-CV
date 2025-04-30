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

import io.github.k7t3.horzcv.client.model.LiveStreaming;
import io.github.k7t3.horzcv.client.model.LiveStreamingDetector;
import io.github.k7t3.horzcv.client.view.LiveStreamingFormView;
import io.github.k7t3.horzcv.shared.service.StreamerInfoServiceAsync;

import java.util.List;
import java.util.logging.Logger;

public class LiveStreamingPresenter implements LiveStreamingFormView.LiveStreamingFormUIHandlers {

    private static final Logger LOGGER = Logger.getLogger(LiveStreamingPresenter.class.getName());

    private final StreamerInfoServiceAsync streamerInfoService;

    private final List<LiveStreamingDetector> detectors;

    private final LiveStreamingFormView view;

    private boolean valid = false;

    public LiveStreamingPresenter(
            StreamerInfoServiceAsync streamerInfoService,
            List<LiveStreamingDetector> detectors,
            LiveStreamingFormView view
    ) {
        this.streamerInfoService = streamerInfoService;
        this.detectors = detectors;
        this.view = view;
        view.setUiHandlers(this);
    }

    public boolean isValid() {
        return valid;
    }

    public void setStream(LiveStreaming stream) {
        view.setName(stream.getName());
        view.setURI(stream.getUri());
    }

    /**
     * 入力された内容からストリームを取得する
     * @throws IllegalStateException ストリームのURLが無効な場合
     */
    public LiveStreaming getAsLiveStreaming() {
        var uri = view.getURI();

        var detector = detectors.stream()
                .filter(d -> d.isValidURL(uri))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Invalid URL: " + uri));

        var id = detector.parseId(uri);
        var service = detector.getService();
        var name = view.getName();

        var s = new LiveStreaming();
        s.setId(id);
        s.setUri(uri);
        s.setName(name);
        s.setService(service);
        return s;
    }

    @Override
    public void onURIChanged(LiveStreamingFormView view, String uri) {
        var invalid = detectors.stream().noneMatch(d -> d.isValidURL(uri));
        view.setInvalid(invalid);
        valid = !invalid;

        // URIに基づくユーザー名が取得できたときはそれを割り当てる
        streamerInfoService.getStreamerInfo(uri, BasicAsyncCallback.of(response -> {
            if (response.isIdentified() && response.getInfoArray().length > 0) {
                var info = response.getInfoArray()[0];
                view.setName(info.getName());
            }
        }));
    }

    @Override
    public void onRemoved(LiveStreamingFormView view) {
        // no-op
    }
}
