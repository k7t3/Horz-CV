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

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import io.github.k7t3.horzcv.client.model.EmbeddedChatFrameBuilder;

public class YoutubeLiveChatFrameBuilder implements EmbeddedChatFrameBuilder {

    /**
     * Youtube LIVEのチャットを埋め込むためのiframeを生成するテンプレート。
     */
    public interface YoutubeEmbeddedChatUrl extends SafeHtmlTemplates {
        @Template("<iframe src=\"https://www.youtube.com/live_chat?v={0}&embed_domain={1}&dark_theme=1\" class=\"chatFrame youtube\"></iframe>")
        SafeHtml template(String videoId, String host);
    }

    private static final YoutubeEmbeddedChatUrl YOUTUBE_EMBEDDED_CHAT_TEMPLATE = GWT.create(YoutubeEmbeddedChatUrl.class);

    private final String host;

    public YoutubeLiveChatFrameBuilder(String host) {
        this.host = host;
    }

    @Override
    public String build(String id) {
        return YOUTUBE_EMBEDDED_CHAT_TEMPLATE.template(id, host).asString();
    }
}
