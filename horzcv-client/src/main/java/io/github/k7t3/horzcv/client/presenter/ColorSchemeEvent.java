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

import org.dominokit.domino.api.shared.extension.DominoEvent;

import java.util.Objects;

@SuppressWarnings("ClassCanBeRecord")
public final class ColorSchemeEvent implements DominoEvent {
    
    private final ColorScheme colorScheme;

    public ColorSchemeEvent(ColorScheme colorScheme) {
        this.colorScheme = colorScheme;
    }

    public ColorScheme getColorScheme() {
        return colorScheme;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ColorSchemeEvent) obj;
        return Objects.equals(this.colorScheme, that.colorScheme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(colorScheme);
    }

    @Override
    public String toString() {
        return "ColorSchemeEvent[" +
                "colorScheme=" + colorScheme + ']';
    }

}
