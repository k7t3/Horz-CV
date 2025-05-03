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

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LiveStreamingTest {

    @Test
    public void toTokenWithName() {
        LiveStreaming liveStreaming = new LiveStreaming();
        liveStreaming.setService(StreamingService.YOUTUBE);
        liveStreaming.setId("12345");
        liveStreaming.setName("Test Stream");

        String token = liveStreaming.toToken();
        assertEquals("y,12345,VGVzdCBTdHJlYW0=", token);
    }

    @Test
    public void toTokenWithoutName() {
        LiveStreaming liveStreaming = new LiveStreaming();
        liveStreaming.setService(StreamingService.TWITCH);
        liveStreaming.setId("67890");

        String token = liveStreaming.toToken();
        assertEquals("t,67890", token);
    }

    @Test
    public void fromTokenWithName() {
        String token = "y,12345,VGVzdCBTdHJlYW0=";
        LiveStreaming liveStreaming = LiveStreaming.fromToken(token);

        assertEquals(StreamingService.YOUTUBE, liveStreaming.getService());
        assertEquals("12345", liveStreaming.getId());
        assertEquals("Test Stream", liveStreaming.getName());
    }

    @Test
    public void fromTokenWithoutName() {
        String token = "t,67890";
        LiveStreaming liveStreaming = LiveStreaming.fromToken(token);

        assertEquals(StreamingService.TWITCH, liveStreaming.getService());
        assertEquals("67890", liveStreaming.getId());
        assertNull(liveStreaming.getName());
    }

    @Test
    public void fromTokenListWithMultipleTokens() {
        String tokenList = "y,12345,VGVzdCBTdHJlYW0=;t,67890";
        List<LiveStreaming> streamingList = LiveStreaming.fromTokens(tokenList);

        assertEquals(2, streamingList.size());

        LiveStreaming youtube = streamingList.get(0);
        assertEquals(StreamingService.YOUTUBE, youtube.getService());
        assertEquals("12345", youtube.getId());
        assertEquals("Test Stream", youtube.getName());

        LiveStreaming twitch = streamingList.get(1);
        assertEquals(StreamingService.TWITCH, twitch.getService());
        assertEquals("67890", twitch.getId());
        assertNull(twitch.getName());
    }

    @Test
    public void fromTokenListWithSingleToken() {
        String tokenList = "y,12345,VGVzdCBTdHJlYW0=";
        List<LiveStreaming> streamingList = LiveStreaming.fromTokens(tokenList);

        assertEquals(1, streamingList.size());

        LiveStreaming youtube = streamingList.get(0);
        assertEquals(StreamingService.YOUTUBE, youtube.getService());
        assertEquals("12345", youtube.getId());
        assertEquals("Test Stream", youtube.getName());
    }

    @Test
    public void fromTokenListWithEmptyString() {
        String tokenList = "";
        List<LiveStreaming> streamingList = LiveStreaming.fromTokens(tokenList);

        assertTrue(streamingList.isEmpty());
    }

    @Test
    public void toTokenAndFromTokenList() {
        List<LiveStreaming> originalList = new ArrayList<>();

        LiveStreaming youtube = new LiveStreaming();
        youtube.setService(StreamingService.YOUTUBE);
        youtube.setId("12345");
        youtube.setName("YouTube Stream");
        originalList.add(youtube);

        LiveStreaming twitch = new LiveStreaming();
        twitch.setService(StreamingService.TWITCH);
        twitch.setId("67890");
        twitch.setName("Twitch Stream");
        originalList.add(twitch);

        String token = LiveStreaming.toTokens(originalList);
        List<LiveStreaming> parsedList = LiveStreaming.fromTokens(token);

        assertEquals(originalList.size(), parsedList.size());

        for (int i = 0; i < originalList.size(); i++) {
            LiveStreaming original = originalList.get(i);
            LiveStreaming parsed = parsedList.get(i);

            assertEquals(original.getService(), parsed.getService());
            assertEquals(original.getId(), parsed.getId());
            assertEquals(original.getName(), parsed.getName());
        }
    }
  
}