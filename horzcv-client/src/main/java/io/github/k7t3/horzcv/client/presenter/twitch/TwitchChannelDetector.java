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

package io.github.k7t3.horzcv.client.presenter.twitch;

import com.google.gwt.regexp.shared.RegExp;
import io.github.k7t3.horzcv.client.model.LiveStreamingDetector;
import io.github.k7t3.horzcv.client.model.StreamingService;

public class TwitchChannelDetector implements LiveStreamingDetector {

    /** Twitchのチャンネルにマッチする正規表現*/
    private final RegExp regex = RegExp.compile("https?://(?:www\\.)twitch\\.tv/([^/]+)");

    public TwitchChannelDetector() {
    }

    @Override
    public boolean isValidURL(String url) {
        return regex.test(url);
    }

    @Override
    public String parseId(String url) {
        var matcher = regex.exec(url);
        if (matcher != null && 0 < matcher.getGroupCount()) {
            return matcher.getGroup(1);
        }
        throw new IllegalArgumentException("Invalid Twitch Channel URL");
    }

    @Override
    public String construct(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid Twitch Channel ID");
        }
        return "https://www.twitch.tv/" + id;
    }

    @Override
    public StreamingService getService() {
        return StreamingService.TWITCH;
    }
}
