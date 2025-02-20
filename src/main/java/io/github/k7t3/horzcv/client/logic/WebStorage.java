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

/**
 * HTML5で策定されるストレージの機能インターフェース
 */
public interface WebStorage {

    /**
     * ストレージのサイズを取得するメソッド
     * @return ストレージのサイズ
     */
    int getLength();

    /**
     * キーに対応する値を取得するメソッド
     * @param key キー
     * @return 値
     */
    String getItem(String key);

    /**
     * キーに対応する値を設定するメソッド
     * @param key キー
     * @param value 値
     */
    void setItem(String key, String value);

    /**
     * キーに対応する値を削除するメソッド
     * @param key キー
     */
    void removeItem(String key);

    /**
     * ストレージをクリアするメソッド
     */
    void clear();

}
