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

package io.github.k7t3.horzcv.server.services.twitch;

import com.github.twitch4j.TwitchClient;
import io.github.k7t3.horzcv.shared.model.StreamerInfo;
import io.github.k7t3.horzcv.shared.model.StreamerInfoResponse;
import io.github.k7t3.horzcv.server.services.StreamerFinder;
import jakarta.inject.Inject;

import java.util.regex.Pattern;

public class TwitchStreamerIdentityFinder implements StreamerFinder {

    // URLからログイン名を取得するパターン
    private static final Pattern LOGIN_PATTERN = Pattern.compile("(?<=https?://(www\\.)?twitch.tv/)[^/]+$");

    @Inject
    private TwitchClient client;

    @Override
    public StreamerInfoResponse find(String urlOrKeyword) {
        var matcher = LOGIN_PATTERN.matcher(urlOrKeyword);

        if (matcher.find()) {
            var login = matcher.group();
            var info = findStreamerInfo(login);
            if (info == null) {
                return StreamerInfoResponse.empty();
            }
            return StreamerInfoResponse.of(info);
        }

        return StreamerInfoResponse.empty();
    }

    private StreamerInfo findStreamerInfo(String login) {
        var helix = client.getHelix();
        var command = helix.searchChannels(null, login, 10, null, false);
        var response = command.execute();
        var result = response.getResults();
        if (result.isEmpty()) {
            return null;
        }

        return result.stream()
                .filter(c -> c.getBroadcasterLogin().equalsIgnoreCase(login))
                .map(c -> new StreamerInfo(c.getDisplayName(), c.getThumbnailUrl(), "https://www.twitch.tv/" + c.getBroadcasterLogin()))
                .findFirst()
                .orElse(null);
    }
}
