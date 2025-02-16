package io.github.k7t3.horzcv.client.presenter.twitch;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import io.github.k7t3.horzcv.client.model.EmbeddedChatFrameBuilder;
import io.github.k7t3.horzcv.client.model.LiveStream;

public class TwitchChannelChatFrameBuilder implements EmbeddedChatFrameBuilder {

    /**
     * Twitchのチャットを埋め込むためのiframeを生成するテンプレート。
     * <p>
     *     ダークモードにしたいときはクエリパラメータに<code>darkpopout</code>を追加する。
     * </p>
     */
    public interface TwitchEmbeddedChatUrl extends SafeHtmlTemplates {
        @Template("<iframe src=\"https://www.twitch.tv/embed/{0}/chat?parent={1}\" class=\"chatFrame twitch\"></iframe>")
        SafeHtml template(String channel, String host);
    }

    private static final TwitchEmbeddedChatUrl TWITCH_EMBEDDED_CHAT_TEMPLATE = GWT.create(TwitchEmbeddedChatUrl.class);

    private final String host;

    public TwitchChannelChatFrameBuilder(String host) {
        this.host = host;
    }

    @Override
    public String build(LiveStream stream) {
        return TWITCH_EMBEDDED_CHAT_TEMPLATE.template(stream.id(), host).asString();
    }
}
