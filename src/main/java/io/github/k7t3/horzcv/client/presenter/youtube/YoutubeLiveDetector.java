package io.github.k7t3.horzcv.client.presenter.youtube;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import io.github.k7t3.horzcv.client.presenter.LiveStreamValidator;

public class YoutubeLiveDetector extends LiveStreamValidator {

    /** Youtube LIVEのビデオIDにマッチする正規表現*/
    //private static final RegExp YOUTUBE_ID_REGEX = RegExp.compile("(?<=www.youtube.com/watch\\?v=)[^&]+");
    // テストケースのJavaScriptの実装次第では後方参照が使用できないようなので・・・
    private static final RegExp YOUTUBE_ID_REGEX = RegExp.compile("www\\.youtube\\.com/(?:watch\\?v=|live/)([^/&]+)");

    public YoutubeLiveDetector() {
        super("https://www.youtube.com/(watch\\?v=|live/)[^/&]+");
    }

    @Override
    public String parseId(String url) {
        MatchResult matcher = YOUTUBE_ID_REGEX.exec(url);
        if (0 < matcher.getGroupCount()) {
            return matcher.getGroup(1);
        }
        throw new IllegalArgumentException("Invalid YouTube Live URL");
    }

    @Override
    public String construct(String id) {
        return "https://www.youtube.com/watch?v=" + id;
    }

    @Override
    public String placeholder() {
        return "https://www.youtube.com/watch?v=...";
    }
}
