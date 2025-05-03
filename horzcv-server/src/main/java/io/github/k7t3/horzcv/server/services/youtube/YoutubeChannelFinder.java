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
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.ChannelSnippet;
import io.github.k7t3.horzcv.shared.model.StreamerInfo;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * YouTubeチャンネル情報を取得するための機能を提供するインターフェース
 */
public interface YoutubeChannelFinder {
    
    /**
     * 指定されたチャンネルIDに基づいてYouTubeチャンネル情報を取得し、StreamerInfo形式に変換します
     *
     * @param client    YouTube APIクライアント
     * @param channelId 取得対象のYouTubeチャンネルID
     * @param url       元の動画URL
     * @return チャンネル情報を含むStreamerInfoのOptional。チャンネルが見つからない場合は空のOptional
     * @throws IOException API呼び出し中にエラーが発生した場合
     */
    default Optional<StreamerInfo> fetchChannelInfo(YouTube client, String channelId, String url) throws IOException {
        var response = fetchChannelResponse(client, channelId);
        return extractChannelSnippet(response)
                .map(snippet -> createStreamerInfo(snippet, url));
    }
    
    /**
     * YouTube APIからチャンネル情報を取得します
     */
    private ChannelListResponse fetchChannelResponse(YouTube client, String channelId) throws IOException {
        return client.channels()
                .list(List.of("snippet"))
                .setId(List.of(channelId))
                .execute();
    }
    
    /**
     * チャンネルレスポンスからスニペット情報を抽出します
     */
    private Optional<ChannelSnippet> extractChannelSnippet(ChannelListResponse response) {
        return response.getItems()
                .stream()
                .map(Channel::getSnippet)
                .findFirst();
    }
    
    /**
     * チャンネルスニペットからStreamerInfoオブジェクトを作成します
     */
    private StreamerInfo createStreamerInfo(ChannelSnippet snippet, String url) {
        return new StreamerInfo(
                snippet.getTitle(),
                snippet.getThumbnails().getDefault().getUrl(),
                url
        );
    }
}