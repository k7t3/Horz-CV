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

package io.github.k7t3.horzcv.server.services.youtube;

import com.google.api.services.youtube.YouTube;
import io.github.k7t3.horzcv.server.services.StreamerFinder;
import io.github.k7t3.horzcv.shared.model.StreamerInfoResponse;
import jakarta.inject.Inject;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class YoutubeStreamerIdentityFinder implements StreamerFinder, YoutubeChannelFinder {

    private static final Pattern YOUTUBE_URL_PATTERN = Pattern.compile("https?://(?:www\\.)?youtube\\.com/(?:watch\\?v=|live/)([^/&?]+)");

    @Inject
    private YouTube youtubeClient;

    @Override
    public StreamerInfoResponse find(String urlOrKeyword) {
        return extractVideoId(urlOrKeyword)
                .flatMap(this::findChannelIdByVideoId)
                .map(channelId -> fetchStreamerInfo(channelId, urlOrKeyword))
                .orElse(StreamerInfoResponse.empty());
    }

    protected Optional<String> extractVideoId(String url) {
        var matcher = YOUTUBE_URL_PATTERN.matcher(url);
        if (!matcher.find()) {
            return Optional.empty();
        }
        return Optional.of(matcher.group(1));
    }

    protected Optional<String> findChannelIdByVideoId(String videoId) {
        try {
            var videoResponse = youtubeClient.videos()
                    .list(List.of("snippet"))
                    .setId(List.of(videoId))
                    .execute();

            return videoResponse.getItems()
                    .stream()
                    .map(video -> video.getSnippet().getChannelId())
                    .findFirst();
        } catch (IOException e) {
            throw new RuntimeException("動画情報の取得に失敗しました", e);
        }
    }

    private StreamerInfoResponse fetchStreamerInfo(String channelId, String originalUrl) {
        try {
            return fetchChannelInfo(youtubeClient, channelId, originalUrl)
                    .map(StreamerInfoResponse::of)
                    .orElse(StreamerInfoResponse.empty());
        } catch (IOException e) {
            throw new RuntimeException("チャンネル情報の取得に失敗しました", e);
        }
    }


}
