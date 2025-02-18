package io.github.k7t3.horzcv.client.presenter.youtube;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class YoutubeLiveDetectorTest {

    private YoutubeLiveDetector detector;

    @Before
    public void setUp() throws Exception {
        detector = new YoutubeLiveDetector();
    }

    @Test
    public void testParseId() {
        String video = "https://www.youtube.com/watch?v=watchmyvideo";
        String live = "https://www.youtube.com/live/watchmystreaming";

        assertTrue(detector.isValidURL(video));
        assertTrue(detector.isValidURL(live));

        assertEquals("watchmyvideo", detector.parseId(video));
        assertEquals("watchmystreaming", detector.parseId(live));
    }

    @Test
    public void testConstruct() {
        String videoId = "watchmyvideo";
        String liveId = "watchmystreaming";
        String expectedVideo = "https://www.youtube.com/watch?v=watchmyvideo";
        String expectedLive = "https://www.youtube.com/watch?v=watchmystreaming";

        assertEquals(expectedVideo, detector.construct(videoId));
        assertEquals(expectedLive, detector.construct(liveId));
    }
}