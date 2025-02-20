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
 * データストアのインターフェース。
 */
public interface DataStore {

    /**
     * データストアを初期化するメソッド。
     */
    void initialize();

    /**
     * データストアの名前空間を取得するメソッド。
     * @return 名前空間
     */
    String getNamespace();

    /**
     * データストアの保存上限を取得するメソッド。
     * @return 保存上限
     */
    int getLimit();

    /**
     * データストアの保存数を取得するメソッド。
     * @return 保存数
     */
    int size();

    /**
     * データストアにデータを保存するメソッド。
     * @param key キー
     * @param value 値
     */
    void store(String key, String value);

    /**
     * データストアからデータを読み込むメソッド。
     * @param key キー
     * @return 値
     */
    String load(String key);

    /**
     * データストアからデータを削除するメソッド。
     * @param key キー
     */
    void remove(String key);

    /**
     * データストアをクリアするメソッド。
     */
    void clear();

}
