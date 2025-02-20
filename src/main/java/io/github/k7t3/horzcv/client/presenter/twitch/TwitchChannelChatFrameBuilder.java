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

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import io.github.k7t3.horzcv.client.model.EmbeddedChatFrameBuilder;

public class TwitchChannelChatFrameBuilder implements EmbeddedChatFrameBuilder {

    /**
     * Twitchのチャットを埋め込むためのiframeを生成するテンプレート。
     * <p>
     *     ダークモードにしたいときはクエリパラメータに<code>darkpopout</code>を追加する。
     * </p>
     */
    public interface TwitchEmbeddedChatUrl extends SafeHtmlTemplates {
        @Template("<iframe src=\"https://www.twitch.tv/embed/{0}/chat?parent={1}&darkpopout\" class=\"chatFrame twitch\"></iframe>")
        SafeHtml template(String channel, String host);
    }

    private static final TwitchEmbeddedChatUrl TWITCH_EMBEDDED_CHAT_TEMPLATE = GWT.create(TwitchEmbeddedChatUrl.class);

    private final String host;

    public TwitchChannelChatFrameBuilder(String host) {
        this.host = host;
    }

    @Override
    public String build(String id) {
        return TWITCH_EMBEDDED_CHAT_TEMPLATE.template(id, host).asString();
    }
}
