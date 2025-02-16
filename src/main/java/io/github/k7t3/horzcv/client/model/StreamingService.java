package io.github.k7t3.horzcv.client.model;

import java.util.Arrays;
import java.util.Objects;

/**
 * サポートしているストリーミングサービスを表す列挙型。
 */
public enum StreamingService {

    /**
     * <a href="https://www.youtube.com/">Youtube</a>
     */
    YOUTUBE("y", "Youtube"),

    /**
     * <a href="https://www.twitch.tv/">Twitch</a>
     */
    TWITCH("t", "Twitch"),

    ;

    private final String type;

    private final String text;

    StreamingService(String type, String text) {
        this.type = type;
        this.text = text;
    }

    /**
     * サービスの種類を表す文字列を返すメソッド。
     * @return サービスの種類を表す文字列
     */
    public String getType() {
        return type;
    }

    /**
     * サービスの名前を返すメソッド。
     * @return サービスの名前
     */
    public String getText() {
        return text;
    }

    public static StreamingService find(String type) {
        return Arrays.stream(StreamingService.values())
                .filter(s -> Objects.equals(type, s.getType())).findFirst()
                .orElseThrow();
    }

}
