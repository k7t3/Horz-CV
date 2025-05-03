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

package io.github.k7t3.horzcv.client.presenter.theme;

import elemental2.dom.DomGlobal;
import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.ui.themes.DominoThemeAccent;
import org.dominokit.domino.ui.themes.DominoThemeDark;
import org.dominokit.domino.ui.themes.DominoThemeLight;
import org.dominokit.domino.ui.themes.DominoThemeManager;

/**
 * カラースキーマを管理するクラス
 */
public class ThemeManager {

    private static final String DARK_MODE_QUERY = "(prefers-color-scheme: dark)";

    public static final ThemeManager INSTANCE = new ThemeManager();

    private ColorScheme colorScheme = ColorScheme.LIGHT;

    private boolean isInitialized = false;

    private ThemeManager() {
    }

    public void initialize() {
        if (isInitialized) {
            return;
        }

        isInitialized = true;

        // アクセントカラー
        DominoThemeManager.INSTANCE.apply(DominoThemeAccent.DEEP_PURPLE);

        // システムのカラースキームに合わせてテーマを変更できるようにする
        var query = DomGlobal.window.matchMedia(DARK_MODE_QUERY);

        // 初期状態がダークモードの場合、ダークテーマを適用
        if (query.matches) {
            applyDarkTheme();
        }

        // ダークモードの変更を監視
        query.addListener(e -> {
            if (e.matches) {
                applyDarkTheme();
            } else {
                applyLightTheme();
            }
        });
    }

    public ColorScheme getColorScheme() {
        return colorScheme;
    }

    private void fireEvent() {
        var event = new ColorSchemeEvent(colorScheme);
        ClientApp.make().fireEvent(ColorSchemeEvent.class, event);
    }

    private void applyDarkTheme() {
        DominoThemeManager.INSTANCE.apply(DominoThemeDark.INSTANCE);
        colorScheme = ColorScheme.DARK;
        fireEvent();
    }

    private void applyLightTheme() {
        DominoThemeManager.INSTANCE.apply(DominoThemeLight.INSTANCE);
        colorScheme = ColorScheme.LIGHT;
        fireEvent();
    }

}
