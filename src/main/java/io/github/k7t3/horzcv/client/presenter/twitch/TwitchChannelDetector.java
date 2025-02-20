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
import io.github.k7t3.horzcv.client.presenter.LiveStreamingValidator;

public class TwitchChannelDetector extends LiveStreamingValidator {

    /** Twitchのチャンネルにマッチする正規表現*/
    //private static final RegExp TWITCH_CHANNEL_REGEX = RegExp.compile("(?<=www.twitch.tv/)[^/]+$");
    private static final RegExp TWITCH_CHANNEL_REGEX = RegExp.compile("www\\.twitch\\.tv/([^/]+)");

    public TwitchChannelDetector() {
        super("https://www.twitch.tv/.+");
    }

    @Override
    public String parseId(String url) {
        var matcher = TWITCH_CHANNEL_REGEX.exec(url);
        if (0 < matcher.getGroupCount()) {
            return matcher.getGroup(1);
        }
        throw new IllegalArgumentException("Invalid Twitch Channel URL");
    }

    @Override
    public String construct(String id) {
        return "https://www.twitch.tv/" + id;
    }

}
