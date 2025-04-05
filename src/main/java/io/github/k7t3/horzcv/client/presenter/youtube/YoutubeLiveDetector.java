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

package io.github.k7t3.horzcv.client.presenter.youtube;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import io.github.k7t3.horzcv.client.model.StreamingService;
import io.github.k7t3.horzcv.client.presenter.LiveStreamingValidator;

public class YoutubeLiveDetector extends LiveStreamingValidator {

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
    public StreamingService getService() {
        return StreamingService.YOUTUBE;
    }
}
