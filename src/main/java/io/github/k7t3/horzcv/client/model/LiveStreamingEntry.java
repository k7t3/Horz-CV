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

package io.github.k7t3.horzcv.client.model;

import java.util.Objects;

/**
 * ユーザーが入力するストリーミングに関する情報モデルクラス。
 */
public class LiveStreamingEntry {

    private StreamingService service = StreamingService.YOUTUBE;

    private String url = "";

    // optional
    private String displayName = "";

    private String id = null;

    public StreamingService getService() {
        return service;
    }

    public void setService(StreamingService service) {
        this.service = service;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        id = null; // URLが変更されたらIDを無効化
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void invalidate() {
        id = null;
    }

    public boolean isValid() {
        return id != null;
    }

    public LiveStreamingIdentity asIdentity() {
        if (!isValid()) {
            throw new IllegalStateException("ID is not set");
        }
        return new LiveStreamingIdentity(service, id);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LiveStreamingEntry entry)) return false;
        return service == entry.service && Objects.equals(url, entry.url) && Objects.equals(displayName, entry.displayName) && Objects.equals(id, entry.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(service, url, displayName, id);
    }

    @Override
    public String toString() {
        return "LiveStreamingEntry{" +
                "service=" + service +
                ", url='" + url + '\'' +
                ", description='" + displayName + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
