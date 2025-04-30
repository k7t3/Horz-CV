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

package io.github.k7t3.horzcv.shared.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * 対象のストリーマーに関する情報
 */
public final class StreamerInfo implements Serializable {

    private String name;
    private String thumbnailURL;
    private String streamURL;

    public StreamerInfo() {
        name = "";
        thumbnailURL = "";
        streamURL = "";
    }

    /**
     * @param name         表示名
     * @param thumbnailURL サムネイルのURL
     * @param streamURL    配信のURL
     */
    public StreamerInfo(
            String name,
            String thumbnailURL,
            String streamURL
    ) {
        setName(name);
        setThumbnailURL(thumbnailURL);
        setStreamURL(streamURL);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public void setStreamURL(String streamURL) {
        this.streamURL = streamURL;
    }

    public String getName() {
        return name;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public String getStreamURL() {
        return streamURL;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (StreamerInfo) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.thumbnailURL, that.thumbnailURL) &&
                Objects.equals(this.streamURL, that.streamURL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, thumbnailURL, streamURL);
    }

    @Override
    public String toString() {
        return "StreamerInfo[" +
                "name=" + name + ", " +
                "thumbnailURL=" + thumbnailURL + ", " +
                "streamURL=" + streamURL + ']';
    }

}
