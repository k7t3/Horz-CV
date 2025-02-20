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

package io.github.k7t3.horzcv.client.presenter;

import com.google.gwt.storage.client.Storage;
import io.github.k7t3.horzcv.client.logic.WebStorage;

/**
 * GWTのWebStorage実装クラス。
 * <p>
 *     JavaScriptの実行環境によってはWebStorageの実装がサポートされず、
 *     そのときはこのクラスのメソッドは以下の通りの動作となる
 *     <ul>
 *         <li>{@link #getLength()}は<code>-1</code>を返す</li>
 *         <li>{@link #getItem(String)}は<code>null</code>を返す</li>
 *         <li>{@link #setItem(String, String)}, {@link #removeItem(String)}, {@link #clear()}は何もしない</li>
 *     </ul>
 * </p>
 */
public class GWTWebStorage implements WebStorage {

    private final Storage storage;

    private final boolean supported;

    public GWTWebStorage(Storage storage) {
        this.storage = storage;
        this.supported = (storage != null);
    }

    @Override
    public int getLength() {
        if (supported) {
            return storage.getLength();
        }
        return -1;
    }

    @Override
    public String getItem(String key) {
        if (supported) {
            return storage.getItem(key);
        }
        return null;
    }

    @Override
    public void setItem(String key, String value) {
        if (supported) {
            storage.setItem(key, value);
        }
    }

    @Override
    public void removeItem(String key) {
        if (supported) {
            storage.removeItem(key);
        }
    }

    @Override
    public void clear() {
        if (supported) {
            storage.clear();
        }
    }

}
