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

import com.google.gwt.regexp.shared.RegExp;
import io.github.k7t3.horzcv.client.model.LiveStreamingDetector;

public abstract class LiveStreamingValidator implements LiveStreamingDetector {

    private final RegExp pattern;

    public LiveStreamingValidator(String pattern) {
        this.pattern = RegExp.compile(pattern);
    }

    @Override
    public boolean isValidURL(String url) {
        return url == null || this.pattern.test(url);
    }

}
