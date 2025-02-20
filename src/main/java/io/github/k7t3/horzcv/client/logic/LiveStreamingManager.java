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

package io.github.k7t3.horzcv.client.logic;


import io.github.k7t3.horzcv.client.model.LiveStreamingEntry;
import io.github.k7t3.horzcv.client.model.LiveStreamingIdentity;
import io.github.k7t3.horzcv.client.model.LiveStreamingDetector;
import io.github.k7t3.horzcv.client.model.StreamingService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * アプリケーションが扱うストリーミングを扱うクラス。
 */
public class LiveStreamingManager {

    private final List<LiveStreamingEntry> entries = new ArrayList<>();

    private final Map<StreamingService, LiveStreamingDetector> detectors;

    public LiveStreamingManager() {
        detectors = new HashMap<>();
    }

    public void registerDetector(StreamingService service, LiveStreamingDetector detector) {
        detectors.put(service, detector);
    }

    public List<LiveStreamingEntry> getEntries() {
        return entries;
    }

    /**
     * 保存されたストリーミング情報を復元するメソッド。
     * @param identities 保存されたストリーミング情報
     */
    public void restoreFromIdentities(List<LiveStreamingIdentity> identities) {
        this.entries.clear();

        for (var s : identities) {
            var detector = detectors.get(s.service());
            if (detector != null) {
                var url = detector.construct(s.id());
                var stream = new LiveStreamingEntry();
                stream.setService(s.service());
                stream.setUrl(url);
                stream.setId(s.id());
                this.entries.add(stream);
            }
        }
    }

    // 事前に入力内容が有効であるか検証しておくこと
    private LiveStreamingEntry activate(LiveStreamingEntry entry) {
        var detector = detectors.get(entry.getService());
        var id = detector.parseId(entry.getUrl());
        entry.setId(id);
        return entry;
    }

    /**
     * 有効なストリーミング情報を取得するメソッド。
     * @return 有効なストリーミング情報
     */
    public List<LiveStreamingEntry> getActiveEntries() {
        return entries.stream()
                .filter(this::validate)
                .map(this::activate)
                .collect(Collectors.toList());
    }

    /**
     * 入力されたURLに適したサービスを探すメソッド。
     * <p>
     *     適したものが見つからないときはnullが返される。
     * </p>
     * @param url 入力されたURL
     * @return 適したサービス。あるいはnull
     */
    public StreamingService findSuitableService(String url) {
        return detectors.entrySet()
                .stream()
                .filter(e -> e.getValue().isValidURL(url))
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public LiveStreamingDetector getDetector(StreamingService service) {
        return detectors.get(service);
    }

    /**
     * 入力された情報が正しい書式か検証するメソッド。
     * @param entry 入力された情報
     * @return 入力された内容が正しいときはtrue
     */
    public boolean validate(LiveStreamingEntry entry) {
        var detector = detectors.get(entry.getService());
        if (detector == null) {
            return false;
        }
        return detector.isValidURL(entry.getUrl());
    }

}
