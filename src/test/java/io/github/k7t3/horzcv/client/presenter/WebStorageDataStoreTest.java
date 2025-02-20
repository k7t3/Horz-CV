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

import io.github.k7t3.horzcv.client.logic.WebStorage;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class WebStorageDataStoreTest {

    private WebStorageDataStore store;

    @Before
    public void setUp() throws Exception {
        store = new WebStorageDataStore("namespace", new InMemoryStorage());
    }

    @Test
    public void testInitialize() {
        store.initialize();

        assertEquals(0, store.size());

        store.store("test.key", "test.value");
        assertEquals(1, store.size());

        store.flush();
        assertEquals(1, store.size());

        store.initialize();
        assertEquals(1, store.size());
    }

    @Test
    public void getGetNamespace() {
        assertEquals("namespace", store.getNamespace());
    }

    @Test
    public void testGetLimit() {
        assertEquals(WebStorageDataStore.DEFAULT_LIMIT, store.getLimit());
    }

    @Test
    public void testSize() {
        assertEquals(0, store.size());

        store.store("test.key", "test.value");
        assertEquals(1, store.size());

        store.remove("test.key");
        assertEquals(0, store.size());
    }

    @Test
    public void testLoad() {
        String key = "test.key";
        String value = "test.value";
        store.store(key, value);

        assertEquals(value, store.load(key));
    }

    @Test
    public void testClear() {
        // 異なる名前空間のデータは消さないことをテスト
        var storage = new InMemoryStorage();

        var store1 = new WebStorageDataStore("hello", storage);
        var store2 = new WebStorageDataStore("world", storage);

        // 同じキーで登録
        var key = "common.key";
        store1.store(key, "hello.value");
        store2.store(key, "world.value");

        assertEquals(1, store1.size());
        assertEquals(1, store2.size());

        // store"1"をクリア
        store1.clear();

        assertEquals(0, store1.size());
        assertEquals(1, store2.size());
    }

    private static class InMemoryStorage implements WebStorage {

        private final Map<String, String> map = new HashMap<>();

        @Override
        public int getLength() {
            return map.size();
        }

        @Override
        public String getItem(String key) {
            return map.get(key);
        }

        @Override
        public void setItem(String key, String value) {
            map.put(key, value);
        }

        @Override
        public void removeItem(String key) {
            map.remove(key);
        }

        @Override
        public void clear() {
            map.clear();
        }
    }

}