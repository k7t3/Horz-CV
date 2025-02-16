package io.github.k7t3.horzcv.client.logic;

import io.github.k7t3.horzcv.client.model.EmbeddedChatFrame;
import io.github.k7t3.horzcv.client.model.EmbeddedChatFrameBuilder;
import io.github.k7t3.horzcv.client.model.LiveStream;
import io.github.k7t3.horzcv.client.model.StreamingService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * チャット埋め込みフレームのリストを管理するクラス。
 */
public class EmbeddedChatList {

    private final List<EmbeddedChatFrame> frames = new ArrayList<>();

    private final Map<StreamingService, EmbeddedChatFrameBuilder> builders;

    public EmbeddedChatList() {
        builders = new HashMap<>();
    }

    public void registerBuilder(StreamingService service, EmbeddedChatFrameBuilder builder) {
        builders.put(service, builder);
    }

    private EmbeddedChatFrame tryBuild(LiveStream stream) {
        var builder = builders.get(stream.service());
        if (builder == null) {
            return null;
        }
        var frame = builder.build(stream);
        return new EmbeddedChatFrame(stream, frame);
    }

    public void setAll(List<LiveStream> streams) {
        this.frames.clear();
        streams.stream()
                .map(this::tryBuild)
                .filter(Objects::nonNull)
                .forEach(frames::add);
    }

    public List<EmbeddedChatFrame> getFrames() {
        return frames;
    }

}
