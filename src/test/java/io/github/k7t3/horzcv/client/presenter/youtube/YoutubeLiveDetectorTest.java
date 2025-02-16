package io.github.k7t3.horzcv.client.presenter.youtube;

import io.github.k7t3.horzcv.client.HorzCVTestBase;

public class YoutubeLiveDetectorTest extends HorzCVTestBase {

    private YoutubeLiveDetector detector;

    @Override
    protected void gwtSetUp() throws Exception {
        super.gwtSetUp();
        detector = new YoutubeLiveDetector();
    }

    public void testParseId() {
        String video = "https://www.youtube.com/watch?v=watchmyvideo";
        String live = "https://www.youtube.com/live/watchmystreaming";

        assertTrue(detector.isValidURL(video));
        assertTrue(detector.isValidURL(live));

        assertEquals("watchmyvideo", detector.parseId(video));
        assertEquals("watchmystreaming", detector.parseId(live));
    }
}