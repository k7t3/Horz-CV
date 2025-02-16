package io.github.k7t3.horzcv.client.presenter.twitch;

import com.google.gwt.regexp.shared.RegExp;
import io.github.k7t3.horzcv.client.presenter.LiveStreamValidator;

public class TwitchChannelDetector extends LiveStreamValidator {

    /** Twitchのチャンネルにマッチする正規表現*/
    private static final RegExp TWITCH_CHANNEL_REGEX = RegExp.compile("(?<=www.twitch.tv/)[^/]+$");

    public TwitchChannelDetector() {
        super("https://www.twitch.tv/.+", "Invalid Twitch Channel URL");
    }

    @Override
    public String parseId(String url) {
        var matcher = TWITCH_CHANNEL_REGEX.exec(url);
        if (0 < matcher.getGroupCount()) {
            return matcher.getGroup(0);
        }
        throw new IllegalArgumentException("Invalid Twitch Channel URL");
    }

    @Override
    public String construct(String id) {
        return "https://www.twitch.tv/" + id;
    }

    @Override
    public String placeholder() {
        return "https://www.twitch.tv/...";
    }
}
