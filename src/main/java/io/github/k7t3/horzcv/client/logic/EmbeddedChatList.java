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

package io.github.k7t3.horzcv.client.logic;

import io.github.k7t3.horzcv.client.model.EmbeddedChatFrame;
import io.github.k7t3.horzcv.client.model.EmbeddedChatFrameBuilder;
import io.github.k7t3.horzcv.client.model.LiveStreamingEntry;
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

    private EmbeddedChatFrame tryBuild(LiveStreamingEntry stream) {
        var builder = builders.get(stream.getService());
        if (builder == null) {
            return null;
        }
        var frame = builder.build(stream.getId());
        return new EmbeddedChatFrame(stream, frame);
    }

    public void setAll(List<LiveStreamingEntry> streams) {
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
