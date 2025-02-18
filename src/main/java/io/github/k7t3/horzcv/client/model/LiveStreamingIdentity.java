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

    private String toToken() {
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
