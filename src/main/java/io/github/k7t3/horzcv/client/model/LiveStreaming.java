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

package io.github.k7t3.horzcv.client.model;

import org.dominokit.jackson.utils.Base64Utils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LiveStreaming {

    private static final String DELIMITER = ",";
    private static final String TOKEN_DELIMITER = ";";

    private StreamingService service = null;
    private String uri = null;
    private String id = null;
    private String name = null;

    public LiveStreaming() {
    }

    public StreamingService getService() {
        return service;
    }

    public void setService(StreamingService service) {
        this.service = service;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toToken() {
        var builder = new StringBuilder();

        builder.append(service.getType()).append(DELIMITER).append(id);

        if (name != null && !name.isEmpty()) {
            var nameBytes = name.getBytes(StandardCharsets.UTF_8);
            builder.append(DELIMITER).append(Base64Utils.toBase64(nameBytes));
        }

        return builder.toString();
    }

    public static LiveStreaming fromToken(String token) {
        var parts = token.split(DELIMITER);
        if (parts.length < 2) {
            return null;
        }

        var serviceType = parts[0];
        var id = parts[1];

        var service = StreamingService.find(serviceType);
        if (service == null) {
            throw new IllegalArgumentException("Unknown service type: " + serviceType);
        }

        var liveStreaming = new LiveStreaming();
        liveStreaming.setService(service);
        liveStreaming.setId(id);

        if (2 < parts.length) {
            var nameBytes = Base64Utils.fromBase64(parts[2]);
            liveStreaming.setName(new String(nameBytes, StandardCharsets.UTF_8));
        }

        return liveStreaming;
    }

    public static String toTokens(List<LiveStreaming> list) {
        return list.stream()
                .map(LiveStreaming::toToken)
                .collect(Collectors.joining(TOKEN_DELIMITER));
    }

    public static List<LiveStreaming> fromTokens(String tokens) {
        return Stream.of(tokens.split(TOKEN_DELIMITER))
                .map(LiveStreaming::fromToken)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LiveStreaming that)) return false;
        return service == that.service
                && Objects.equals(uri, that.uri)
                && Objects.equals(id, that.id)
                && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(service, uri, id, name);
    }

    @Override
    public String toString() {
        return "LiveStreaming{" +
                "service=" + service +
                ", uri='" + uri + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
