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
import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.ThumbnailDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class YoutubeChannelFinderTest {

    @Mock
    private YouTube mockYouTube;

    @Mock
    private YouTube.Channels mockChannels;

    @Mock
    private YouTube.Channels.List mockChannelList;

    @InjectMocks
    private YoutubeChannelFinderImpl youtubeChannelFinder;

    @Test
    public void fetchChannelInfoReturnsStreamerInfoForValidChannelId() throws IOException {
        var channelId = "validChannelId";
        var url = "https://www.youtube.com/watch?v=example";
        var snippet = new ChannelSnippet();
        snippet.setTitle("Channel Title");
        var thumbnails = new ThumbnailDetails();
        var thumbnail = new Thumbnail();
        thumbnail.setUrl("https://example.com/thumbnail.jpg");
        thumbnails.setDefault(thumbnail);
        snippet.setThumbnails(thumbnails);

        var channel = new Channel();
        channel.setSnippet(snippet);

        var response = new ChannelListResponse();
        response.setItems(List.of(channel));

        when(mockYouTube.channels()).thenReturn(mockChannels);
        when(mockChannels.list(List.of("snippet"))).thenReturn(mockChannelList);
        when(mockChannelList.setId(List.of(channelId))).thenReturn(mockChannelList);
        when(mockChannelList.execute()).thenReturn(response);

        var result = youtubeChannelFinder.fetchChannelInfo(mockYouTube, channelId, url);

        assertTrue(result.isPresent());
        assertEquals("Channel Title", result.get().getName());
        assertEquals("https://example.com/thumbnail.jpg", result.get().getThumbnailURL());
        assertEquals(url, result.get().getStreamURL());
    }

    @Test
    public void fetchChannelInfoReturnsEmptyOptionalForNonExistentChannelId() throws IOException {
        var channelId = "nonExistentChannelId";
        var url = "https://www.youtube.com/watch?v=example";

        var response = new ChannelListResponse();
        response.setItems(List.of());

        when(mockYouTube.channels()).thenReturn(mockChannels);
        when(mockChannels.list(List.of("snippet"))).thenReturn(mockChannelList);
        when(mockChannelList.setId(List.of(channelId))).thenReturn(mockChannelList);
        when(mockChannelList.execute()).thenReturn(response);

        var result = youtubeChannelFinder.fetchChannelInfo(mockYouTube, channelId, url);

        assertTrue(result.isEmpty());
    }

    @Test
    public void fetchChannelInfoThrowsIOExceptionForApiError() throws IOException {
        var channelId = "validChannelId";
        var url = "https://www.youtube.com/watch?v=example";

        when(mockYouTube.channels()).thenReturn(mockChannels);
        when(mockChannels.list(List.of("snippet"))).thenReturn(mockChannelList);
        when(mockChannelList.setId(List.of(channelId))).thenReturn(mockChannelList);
        when(mockChannelList.execute()).thenThrow(new IOException("API error"));

        assertThrows(IOException.class, () -> youtubeChannelFinder.fetchChannelInfo(mockYouTube, channelId, url));
    }

    // YoutubeChannelFinderImplのモックインスタンス化用の空実装クラス
    private static class YoutubeChannelFinderImpl implements YoutubeChannelFinder {
    }
}