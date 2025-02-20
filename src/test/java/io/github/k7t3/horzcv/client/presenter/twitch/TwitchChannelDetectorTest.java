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

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TwitchChannelDetectorTest {

    private TwitchChannelDetector detector;

    @Before
    public void setUp() throws Exception {
        detector = new TwitchChannelDetector();
    }

    @Test
    public void parseId() {
        var url = "https://www.twitch.tv/watchmychannel";

        assertTrue(detector.isValidURL(url));

        assertEquals("watchmychannel", detector.parseId(url));
    }

    @Test
    public void constructTest() {
        var id = "watchmychannel";
        var expected = "https://www.twitch.tv/watchmychannel";

        assertEquals(expected, detector.construct(id));
    }
}