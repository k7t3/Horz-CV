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
