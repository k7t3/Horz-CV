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

import io.github.k7t3.horzcv.client.model.LiveStreaming;

import java.util.List;
import java.util.stream.Collectors;

public interface WindowTitleProvider {

    default String title(List<LiveStreaming> items) {
        var joined = items.stream()
                .map(LiveStreaming::getName)
                .filter(n -> n != null && !n.trim().isEmpty())
                .map(n -> ellipsis(n.trim(), 5))
                .collect(Collectors.joining(","));
        joined = ellipsis(joined, 20);
        return joined;
    }

    private String ellipsis(String name, int maxLength) {
        if (name.length() <= maxLength) {
            return name;
        }
        return name.substring(0, maxLength - 1) + "..";
    }

}
