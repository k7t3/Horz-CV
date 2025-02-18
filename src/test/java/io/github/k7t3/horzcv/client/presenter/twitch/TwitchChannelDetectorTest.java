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