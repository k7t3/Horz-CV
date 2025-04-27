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
