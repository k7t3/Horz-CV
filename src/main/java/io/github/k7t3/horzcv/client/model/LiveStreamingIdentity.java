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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * ストリーミングサービスとそのIDのレコード。
 * @param service ストリーミングサービス
 * @param id ID
 */
public record LiveStreamingIdentity(StreamingService service, String id) {

    public LiveStreamingIdentity {
        if (service == null) throw new IllegalArgumentException("service is null");
        if (id == null || id.isEmpty()) throw new IllegalArgumentException("id is null or empty");
    }

    public String toToken() {
        return service.getType() + "=" + id;
    }

    private static LiveStreamingIdentity fromToken(String type, String id) {
        try {
            return new LiveStreamingIdentity(StreamingService.find(type), id);
        } catch (Exception ignored) {
            return null;
        }
    }

    public static String toToken(List<LiveStreamingIdentity> streams) {
        return streams.stream()
                .map(LiveStreamingIdentity::toToken)
                .collect(Collectors.joining("+"));
    }

    public static List<LiveStreamingIdentity> fromToken(String token) {
        return Arrays.stream(token.split("\\+"))
                .map(v -> v.split("="))
                .filter(kv -> kv.length == 2)
                .map(kv -> fromToken(kv[0], kv[1]))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
