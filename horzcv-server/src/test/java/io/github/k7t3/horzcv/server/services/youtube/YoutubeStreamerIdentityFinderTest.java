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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import io.github.k7t3.horzcv.shared.model.StreamerInfoResponse;
import io.github.k7t3.horzcv.shared.model.StreamerInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class YoutubeStreamerIdentityFinderTest {

    @Mock
    private YouTube youtubeClient;

    @Mock
    private YouTube.Videos videos;

    @Mock
    private YouTube.Videos.List videosList;

    @Mock
    private YouTube.Channels channels;

    @Mock
    private YouTube.Channels.List channelsList;

    @InjectMocks
    private YoutubeStreamerIdentityFinder finder;

    private final String VALID_VIDEO_ID = "abcd1234";
    private final String VALID_CHANNEL_ID = "UC1234567890";
    private final String CHANNEL_TITLE = "テストチャンネル";
    private final String CHANNEL_THUMBNAIL_URL = "https://example.com/thumbnail.jpg";

    @Test
    void extractVideoId_validYoutubeUrl_returnsVideoId() {
        // 通常の動画URL
        Optional<String> result1 = finder.extractVideoId("https://www.youtube.com/watch?v=" + VALID_VIDEO_ID);
        assertTrue(result1.isPresent());
        assertEquals(VALID_VIDEO_ID, result1.get());

        // ライブURL
        Optional<String> result2 = finder.extractVideoId("https://youtube.com/live/" + VALID_VIDEO_ID);
        assertTrue(result2.isPresent());
        assertEquals(VALID_VIDEO_ID, result2.get());
    }

    @Test
    void extractVideoId_invalidUrl_returnsEmpty() {
        Optional<String> result = finder.extractVideoId("https://example.com/video");
        assertTrue(result.isEmpty());
    }

    @Test
    void findChannelIdByVideoId_validVideoId_returnsChannelId() throws IOException {
        // 必要なモックのセットアップ
        when(youtubeClient.videos()).thenReturn(videos);
        when(videos.list(anyList())).thenReturn(videosList);
        when(videosList.setId(anyList())).thenReturn(videosList);

        // モックの応答を設定
        VideoListResponse videoResponse = new VideoListResponse();
        VideoSnippet snippet = new VideoSnippet();
        snippet.setChannelId(VALID_CHANNEL_ID);
        Video video = new Video();
        video.setSnippet(snippet);
        videoResponse.setItems(List.of(video));

        when(videosList.execute()).thenReturn(videoResponse);

        // テスト実行
        Optional<String> result = finder.findChannelIdByVideoId(VALID_VIDEO_ID);
        
        // 検証
        assertTrue(result.isPresent());
        assertEquals(VALID_CHANNEL_ID, result.get());
        verify(videosList).setId(List.of(VALID_VIDEO_ID));
    }

    @Test
    void findChannelIdByVideoId_videoNotFound_returnsEmpty() throws IOException {
        // 必要なモックのセットアップ
        when(youtubeClient.videos()).thenReturn(videos);
        when(videos.list(anyList())).thenReturn(videosList);
        when(videosList.setId(anyList())).thenReturn(videosList);

        // 空のレスポンスを設定
        VideoListResponse emptyResponse = new VideoListResponse();
        emptyResponse.setItems(List.of());
        when(videosList.execute()).thenReturn(emptyResponse);

        // テスト実行
        Optional<String> result = finder.findChannelIdByVideoId(VALID_VIDEO_ID);
        
        // 検証
        assertTrue(result.isEmpty());
    }

    @Test
    void find_validUrl_returnsStreamerInfo() throws IOException {
        // ビデオAPI用のモックセットアップ
        when(youtubeClient.videos()).thenReturn(videos);
        when(videos.list(anyList())).thenReturn(videosList);
        when(videosList.setId(anyList())).thenReturn(videosList);

        // ビデオAPIのモックレスポンス
        VideoListResponse videoResponse = new VideoListResponse();
        VideoSnippet videoSnippet = new VideoSnippet();
        videoSnippet.setChannelId(VALID_CHANNEL_ID);
        Video video = new Video();
        video.setSnippet(videoSnippet);
        videoResponse.setItems(List.of(video));
        when(videosList.execute()).thenReturn(videoResponse);

        // チャンネルAPI用のモックセットアップ
        when(youtubeClient.channels()).thenReturn(channels);
        when(channels.list(anyList())).thenReturn(channelsList);
        when(channelsList.setId(anyList())).thenReturn(channelsList);

        // チャンネルAPIのモックレスポンス
        ChannelListResponse channelResponse = new ChannelListResponse();
        ChannelSnippet channelSnippet = new ChannelSnippet();
        channelSnippet.setTitle(CHANNEL_TITLE);
        
        ThumbnailDetails thumbnailDetails = new ThumbnailDetails();
        Thumbnail thumbnail = new Thumbnail();
        thumbnail.setUrl(CHANNEL_THUMBNAIL_URL);
        thumbnailDetails.setDefault(thumbnail);
        channelSnippet.setThumbnails(thumbnailDetails);
        
        Channel channel = new Channel();
        channel.setId(VALID_CHANNEL_ID);
        channel.setSnippet(channelSnippet);
        channelResponse.setItems(List.of(channel));
        
        when(channelsList.execute()).thenReturn(channelResponse);

        // テスト実行
        String validUrl = "https://www.youtube.com/watch?v=" + VALID_VIDEO_ID;
        StreamerInfoResponse response = finder.find(validUrl);
        
        // 検証
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertTrue(response.isIdentified());
        
        // StreamerInfo配列の検証
        StreamerInfo[] infoArray = response.getInfoArray();
        assertEquals(1, infoArray.length);
        
        StreamerInfo info = infoArray[0];
        assertEquals(CHANNEL_TITLE, info.getName());
        assertEquals(CHANNEL_THUMBNAIL_URL, info.getThumbnailURL());
        assertEquals(validUrl, info.getStreamURL());
    }

    @Test
    void find_invalidUrl_returnsEmptyResponse() {
        // テスト実行
        StreamerInfoResponse response = finder.find("https://example.com/not-youtube");
        
        // 検証
        assertTrue(response.isEmpty());
        
        // YouTube APIは呼ばれないはず
        verifyNoInteractions(videos);
    }
}
