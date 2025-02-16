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
