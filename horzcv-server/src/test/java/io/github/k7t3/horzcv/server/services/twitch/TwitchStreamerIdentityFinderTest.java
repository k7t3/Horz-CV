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
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.domain.ChannelSearchList;
import com.github.twitch4j.helix.domain.ChannelSearchResult;
import com.netflix.hystrix.HystrixCommand;
import io.github.k7t3.horzcv.shared.model.StreamerInfo;
import io.github.k7t3.horzcv.shared.model.StreamerInfoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TwitchStreamerIdentityFinderTest {

    @Mock
    private TwitchClient client;

    @Mock
    private TwitchHelix helix;

    @InjectMocks
    private TwitchStreamerIdentityFinder finder;

    @BeforeEach
    void setUp() {
        //when(client.getHelix()).thenReturn(helix);
    }

    @Test
    void testFind_validTwitchUrl_returnsStreamerInfo() {
        // 準備
        String url = "https://www.twitch.tv/testuser";
        ChannelSearchResult channel = mock(ChannelSearchResult.class);
        when(channel.getBroadcasterLogin()).thenReturn("testuser");
        when(channel.getDisplayName()).thenReturn("TestUser");
        when(channel.getThumbnailUrl()).thenReturn("https://thumbnail.url");

        var command = mock(HystrixCommand.class);
        var response = mock(ChannelSearchList.class);

        when(client.getHelix()).thenReturn(helix);
        when(helix.searchChannels(isNull(), eq("testuser"), eq(10), isNull(), eq(false))).thenReturn(command);
        when(command.execute()).thenReturn(response);
        when(response.getResults()).thenReturn(List.of(channel));

        // 実行
        StreamerInfoResponse result = finder.find(url);

        // 検証
        assertTrue(result.isIdentified());
        assertEquals(1, result.getInfoArray().length);

        StreamerInfo info = result.getInfoArray()[0];
        assertEquals("TestUser", info.getName());
        assertEquals("https://thumbnail.url", info.getThumbnailURL());
        assertEquals("https://www.twitch.tv/testuser", info.getStreamURL());
    }

    @Test
    void testFind_invalidTwitchUrl_returnsEmptyResponse() {
        // 無効なURLのテスト
        StreamerInfoResponse result = finder.find("https://www.example.com/testuser");

        assertEquals(0, result.getInfoArray().length);
        assertFalse(result.isIdentified());
    }

    @Test
    void testFind_noMatchingChannel_returnsEmptyResponse() {
        // 準備
        String url = "https://www.twitch.tv/nonexistentuser";

        var command = mock(HystrixCommand.class);
        var response = mock(ChannelSearchList.class);

        when(client.getHelix()).thenReturn(helix);
        when(helix.searchChannels(isNull(), eq("nonexistentuser"), eq(10), isNull(), eq(false))).thenReturn(command);
        when(command.execute()).thenReturn(response);
        when(response.getResults()).thenReturn(Collections.emptyList());

        // 実行
        StreamerInfoResponse result = finder.find(url);

        // 検証
        assertEquals(0, result.getInfoArray().length);
        assertFalse(result.isIdentified());
    }

    @Test
    void testFind_withWwwInUrl_returnsStreamerInfo() {
        // 準備
        String url = "https://www.twitch.tv/testuser";
        ChannelSearchResult channel = mock(ChannelSearchResult.class);
        when(channel.getBroadcasterLogin()).thenReturn("testuser");
        when(channel.getDisplayName()).thenReturn("TestUser");
        when(channel.getThumbnailUrl()).thenReturn("https://thumbnail.url");

        var command = mock(HystrixCommand.class);
        var response = mock(ChannelSearchList.class);

        when(client.getHelix()).thenReturn(helix);
        when(helix.searchChannels(isNull(), eq("testuser"), eq(10), isNull(), eq(false))).thenReturn(command);
        when(command.execute()).thenReturn(response);
        when(response.getResults()).thenReturn(List.of(channel));

        // 実行
        StreamerInfoResponse result = finder.find(url);

        // 検証
        assertTrue(result.isIdentified());
        assertEquals(1, result.getInfoArray().length);
    }

    @Test
    void testFind_withoutWwwInUrl_returnsStreamerInfo() {
        // 準備
        String url = "https://twitch.tv/testuser";
        ChannelSearchResult channel = mock(ChannelSearchResult.class);
        when(channel.getBroadcasterLogin()).thenReturn("testuser");
        when(channel.getDisplayName()).thenReturn("TestUser");
        when(channel.getThumbnailUrl()).thenReturn("https://thumbnail.url");

        var command = mock(HystrixCommand.class);
        var response = mock(ChannelSearchList.class);

        when(client.getHelix()).thenReturn(helix);
        when(helix.searchChannels(isNull(), eq("testuser"), eq(10), isNull(), eq(false))).thenReturn(command);
        when(command.execute()).thenReturn(response);
        when(response.getResults()).thenReturn(List.of(channel));

        // 実行
        StreamerInfoResponse result = finder.find(url);

        // 検証
        assertTrue(result.isIdentified());
        assertEquals(1, result.getInfoArray().length);
    }

    @Test
    void testFind_caseInsensitiveMatching_returnsStreamerInfo() {
        // 準備
        String url = "https://www.twitch.tv/TestUser";
        ChannelSearchResult channel = mock(ChannelSearchResult.class);
        when(channel.getBroadcasterLogin()).thenReturn("testuser");
        when(channel.getDisplayName()).thenReturn("TestUser");
        when(channel.getThumbnailUrl()).thenReturn("https://thumbnail.url");

        var command = mock(HystrixCommand.class);
        var response = mock(ChannelSearchList.class);

        when(client.getHelix()).thenReturn(helix);
        when(helix.searchChannels(isNull(), eq("TestUser"), eq(10), isNull(), eq(false))).thenReturn(command);
        when(command.execute()).thenReturn(response);
        when(response.getResults()).thenReturn(List.of(channel));

        // 実行
        StreamerInfoResponse result = finder.find(url);

        // 検証
        assertTrue(result.isIdentified());
        assertEquals(1, result.getInfoArray().length);
    }
}
