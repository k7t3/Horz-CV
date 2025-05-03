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

package io.github.k7t3.horzcv.client.view.ui;

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.api.client.mvp.view.HasUiHandlers;
import org.dominokit.domino.api.client.mvp.view.UiHandlers;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.animations.Animation;
import org.dominokit.domino.ui.animations.Transition;
import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.elements.DivElement;
import org.dominokit.domino.ui.forms.TextBox;
import org.dominokit.domino.ui.grid.Column;
import org.dominokit.domino.ui.grid.Row;
import org.dominokit.domino.ui.grid.flex.FlexAlign;
import org.dominokit.domino.ui.grid.flex.FlexDirection;
import org.dominokit.domino.ui.grid.flex.FlexItem;
import org.dominokit.domino.ui.grid.flex.FlexJustifyContent;
import org.dominokit.domino.ui.grid.flex.FlexLayout;
import org.dominokit.domino.ui.icons.MdiIcon;
import org.dominokit.domino.ui.icons.lib.Icons;
import org.dominokit.domino.ui.menu.AbstractMenuItem;
import org.dominokit.domino.ui.menu.Menu;
import org.dominokit.domino.ui.menu.MenuItem;
import org.dominokit.domino.ui.utils.PrefixAddOn;
import org.dominokit.domino.ui.utils.ScreenMedia;

import java.util.logging.Logger;

import static org.dominokit.domino.ui.style.DisplayCss.dui_flex;
import static org.dominokit.domino.ui.style.SpacingCss.*;
import static org.dominokit.domino.ui.utils.Domino.div;

public class ChatView implements IsElement<HTMLDivElement>, HasUiHandlers<ChatView.ChatUiHandler> {

    private static final Logger LOGGER = Logger.getLogger(ChatView.class.getName());
    public static final String MENU_LEFT = "left";
    public static final String MENU_RIGHT = "right";
    public static final String MENU_OPEN = "open";
    public static final String MENU_CLOSE = "close";

    final FlexItem<HTMLDivElement> flexItem;
    final DivElement iframe;
    final Button left;
    final Button right;
    final MdiIcon menu;
    final TextBox name;

    final AbstractMenuItem<Integer> leftMenuItem;
    final AbstractMenuItem<Integer> rightMenuItem;

    private ChatUiHandler handler;

    public ChatView() {

        var menu = Menu.<Integer>create()
                .appendChild(
                        leftMenuItem = MenuItem.<Integer>create("Left")
                                .style("list-style: none;")
                                .setKey(MENU_LEFT)
                                .appendChild(PrefixAddOn.of(Icons.arrow_left_bold().addCss(dui_font_size_5)))
                                .hideOn(ScreenMedia.MEDIUM_AND_UP)
                )
                .appendChild(
                        rightMenuItem = MenuItem.<Integer>create("Right")
                                .style("list-style: none;")
                                .setKey(MENU_RIGHT)
                                .appendChild(PrefixAddOn.of(Icons.arrow_right_bold().addCss(dui_font_size_5)))
                                .hideOn(ScreenMedia.MEDIUM_AND_UP)
                )
                .appendChild(
                        MenuItem.<Integer>create("Open")
                                .style("list-style: none;")
                                .setKey(MENU_OPEN)
                                .appendChild(PrefixAddOn.of(Icons.open_in_new().addCss(dui_font_size_5)))
                )
                .appendChild(
                        MenuItem.<Integer>create("Close")
                                .style("list-style: none;")
                                .setKey(MENU_CLOSE)
                                .appendChild(PrefixAddOn.of(Icons.close().addCss(dui_font_size_5)))
                );
        menu.addSelectionListener((source, selection) -> source.ifPresent(item -> {
            switch (item.getKey()) {
                case MENU_OPEN -> {
                    if (handler != null) {
                        handler.onOpenLinkClicked(this);
                        menu.close();
                    }
                }
                case MENU_CLOSE -> close();
                case MENU_LEFT -> {
                    if (handler != null) {
                        handler.onLeftButtonClicked(this);
                        menu.close();
                    }
                }
                case MENU_RIGHT -> {
                    if (handler != null) {
                        handler.onRightButtonClicked(this);
                        menu.close();
                    }
                }
            }
        }));

        var actions = Row.of12Columns()
                .setGap("0.1rem")
                .setMargin("0 0 0 0")
                .appendChild(
                        Column.span1(
                                left = createIconButton(Icons.arrow_left_bold())
                        ).hideOn(ScreenMedia.SMALL_AND_DOWN)
                )
                .appendChild(
                        Column.colspan(Column.Span._9, Column.Span._11).appendChild(
                                name = TextBox.create()
                                        .setPlaceholder("Name")
                                        .setFontSize("0.75em")
                                        .addCss(dui_m_0)
                        )
                )
                .appendChild(
                        Column.colspan(Column.Span._1).appendChild(
                                this.menu = Icons.menu()
                                        .clickable()
                                        .setDropMenu(menu)
                        ).addCss(dui_flex, dui_items_center, dui_justify_center)
                )
                .appendChild(
                        Column.span1(
                                right = createIconButton(Icons.arrow_right_bold())
                        ).addCss(dui_flex, dui_items_end).hideOn(ScreenMedia.SMALL_AND_DOWN)
                );

        var layout = FlexLayout.create()
                .setDirection(FlexDirection.TOP_TO_BOTTOM)
                .setJustifyContent(FlexJustifyContent.CENTER)
                .setAlignItems(FlexAlign.STRETCH)
                .setGap("10px")
                .setHeight("100%");
        layout.appendChild(FlexItem.of(iframe = div().setHeight("100%")).setFlexGrow(1));
        layout.appendChild(FlexItem.of(actions));

        flexItem = FlexItem.of(layout);
    }

    public void initialize() {
        left.addClickListener(e -> {
            if (handler != null)
                handler.onLeftButtonClicked(this);
            e.preventDefault();
        });
        right.addClickListener(e -> {
            if (handler != null)
                handler.onRightButtonClicked(this);
            e.preventDefault();
        });
        name.addChangeListener((oldValue, newValue) -> {
            if (handler != null)
                handler.onNameChanged(this, newValue);
        });
    }

    private void close() {
        if (handler != null) {
            handler.onRemoveClicked(this);
        }
    }

    public void startAnimation() {
        Animation.create(this)
                .transition(Transition.FADE_IN)
                .duration(1000)
                .animate();
    }

    public void setDisableLeft(boolean disable) {
        left.setDisabled(disable);
        leftMenuItem.setDisabled(disable);
    }

    public void setDisableRight(boolean disable) {
        right.setDisabled(disable);
        rightMenuItem.setDisabled(disable);
    }

    private Button createIconButton(MdiIcon icon) {
        return Button.create(icon).addCss(dui_m_1);
    }

    @Override
    public HTMLDivElement element() {
        return flexItem.element();
    }

    @Override
    public void setUiHandlers(ChatUiHandler handler) {
        this.handler = handler;
    }

    public interface ChatUiHandler extends UiHandlers {
        void onLeftButtonClicked(ChatView view);
        void onRightButtonClicked(ChatView view);
        void onOpenLinkClicked(ChatView view);
        void onRemoveClicked(ChatView view);
        void onNameChanged(ChatView view, String name);
    }

}
