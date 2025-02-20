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

import io.github.k7t3.horzcv.client.logic.DataStore;
import io.github.k7t3.horzcv.client.logic.WebStorage;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class WebStorageDataStore implements DataStore {

    public static final int DEFAULT_LIMIT = 32;

    private final WebStorage storage;
    private final Catalog catalog = new Catalog();

    private final String namespace;
    private final int limit;

    public WebStorageDataStore(String namespace, WebStorage storage) {
        this(namespace, DEFAULT_LIMIT, storage);
    }

    public WebStorageDataStore(String namespace, int limit, WebStorage storage) {
        this.storage = storage;
        this.namespace = namespace;
        this.limit = limit;
    }

    @Override
    public void initialize() {
        catalog.load();
    }

    @Override
    public String getNamespace() {
        return namespace;
    }

    @Override
    public int getLimit() {
        return limit;
    }

    @Override
    public int size() {
        return catalog.size();
    }

    /**
     * 名前空間を接頭辞に付与したキー
     */
    private String getTrueKey(String key) {
        return getNamespace() + "." + key;
    }

    @Override
    public void store(String key, String value) {
        catalog.add(key);

        key = getTrueKey(key);
        storage.setItem(key, value);
    }

    @Override
    public String load(String key) {
        key = getTrueKey(key);
        return storage.getItem(key);
    }

    @Override
    public void remove(String key) {
        catalog.remove(key);

        key = getTrueKey(key);
        storage.removeItem(key);
    }

    @Override
    public void clear() {
        catalog.clear();
    }

    /**
     * バッファリングされたデータを永続化する。
     */
    public void flush() {
        catalog.flush();
    }

    /**
     * このデータストアが管理するデータのカタログ
     * <p>カタログの内容はバッファリングされており、{@link #flush()}で永続化できる。</p>
     */
    @SuppressWarnings("SimplifyStreamApiCallChains")
    private class Catalog {
        private final TreeSet<Header> catalog = new TreeSet<>();

        public void add(String key) {
            add(Header.of(key));
        }

        private void add(Header header) {
            // 超過分を削除
            if (getLimit() <= catalog.size()) {
                var over = catalog.pollFirst();
                if (over != null) {
                    var trueKey = getTrueKey(over.plainKey);
                    storage.removeItem(trueKey);
                }
            }
            catalog.add(header);
        }

        public void remove(String key) {
            catalog.remove(Header.removal(key));
        }

        public int size() {
            return catalog.size();
        }

        private String getCatalogKey() {
            return getNamespace() + ".catalog";
        }

        public void flush() {
            var key = getCatalogKey();
            var value = catalog.stream()
                    .map(header -> header.plainKey + "=" + header.timestamp.getTime())
                    .collect(Collectors.joining(";"));
            storage.setItem(key, value);
        }

        public void load() {
            // キューをクリア
            catalog.clear();

            var key = getCatalogKey();
            var value = storage.getItem(key);

            if (value != null) {
                Arrays.stream(value.split(";"))
                        .map(s -> s.split("="))
                        .filter(array -> array.length == 2)
                        .map(this::toHeader)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
                        .forEach(this::add);
            }
        }

        private Header toHeader(String[] array) {
            try {
                return new Header(array[0], new Date(Long.parseLong(array[1])));
            } catch (NumberFormatException e) {
                return null;
            }
        }

        public void clear() {
            for (var header : catalog) {
                // 実データを削除
                var trueKey = getTrueKey(header.plainKey);
                storage.removeItem(trueKey);
            }
            catalog.clear();

            // カタログデータを削除
            var key = getCatalogKey();
            storage.removeItem(key);
        }
    }

    private record Header(String plainKey, Date timestamp) implements Comparable<Header> {

        public static Header of(String key) {
            return new Header(key, new Date());
        }

        public static Header removal(String key) {
            return new Header(key, null);
        }

        @Override
        public int compareTo(Header o) {
            return plainKey.compareTo(o.plainKey);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Header header)) return false;
            return Objects.equals(plainKey, header.plainKey);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(plainKey);
        }
    }

}
