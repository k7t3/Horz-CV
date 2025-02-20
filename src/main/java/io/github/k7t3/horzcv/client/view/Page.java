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

package io.github.k7t3.horzcv.client.view;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.Objects;
import java.util.logging.Logger;

public interface Page extends View {

    default void show() {

        var root = RootPanel.get("content");

        // 現在最上位にあるウィジェットを取得
        Widget widget;
        if (0 < root.getWidgetCount()) {
            widget = root.getWidget(root.getWidgetCount() - 1);
        } else {
            widget = null;
        }

        var me = getRoot();

        // 現在のウィジェットが同じであれば何もしない
        if (Objects.equals(widget, me)) {
            return;
        }

        var logger = Logger.getLogger(getClass().getName());
        logger.info("Showing " + getClass().getName());

        // 現在のウィジェットを削除
        if (widget != null) {
            root.remove(widget);
        }

        root.add(me);

        // フェードインアニメーションを実行
//        var fadeIn = new MaterialAnimation(me);
//        fadeIn.setTransition(Transition.FADEIN);
//        fadeIn.animate();

    }

}
