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

/**
 * 特定のストリーミングサービスのURLを検出するためのインターフェース。
 */
public interface LiveStreamingDetector {

    /**
     * 指定されたURLがこのサービスのものであるかどうかを判定するメソッド。
     * @param url URL
     * @return このサービスのURLであればtrue
     */
    boolean isValidURL(String url);

    /**
     * 指定されたURLからIDを抽出するメソッド。
     * @param url URL
     * @return ID
     */
    String parseId(String url);

    /**
     * 指定されたIDからURLを構築するメソッド。
     * @param id ID
     * @return URL
     */
    String construct(String id);

    StreamingService getService();

}
