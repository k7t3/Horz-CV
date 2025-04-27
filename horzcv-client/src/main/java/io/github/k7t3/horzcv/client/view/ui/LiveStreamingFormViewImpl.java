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

import elemental2.dom.HTMLElement;
import io.github.k7t3.horzcv.client.view.LiveStreamingFormView;
import org.dominokit.domino.ui.animations.Animation;
import org.dominokit.domino.ui.animations.Transition;
import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.cards.Card;
import org.dominokit.domino.ui.forms.TextBox;
import org.dominokit.domino.ui.grid.Row;
import org.dominokit.domino.ui.grid.flex.FlexAlign;
import org.dominokit.domino.ui.grid.flex.FlexItem;
import org.dominokit.domino.ui.grid.flex.FlexLayout;
import org.dominokit.domino.ui.icons.lib.Icons;

import java.util.ArrayList;
import java.util.List;

import static org.dominokit.domino.ui.style.GenericCss.dui_small;
import static org.dominokit.domino.ui.style.SpacingCss.*;

public class LiveStreamingFormViewImpl implements LiveStreamingFormView {

    private final TextBox uriTextBox;
    private final TextBox nameTextBox;

    private final HTMLElement root;

    private final List<LiveStreamingFormUIHandlers> handlers = new ArrayList<>();

    public LiveStreamingFormViewImpl() {
        root = Card.create()
                .withBody((parent, self) -> self.addCss(dui_p_x_4, dui_p_y_2))
                .appendChild(
                        FlexLayout.create()
                                .addCss(dui_gap_4)
                                .setAlignItems(FlexAlign.CENTER)
                                .appendChild(FlexItem.of(
                                        Row.create()
                                                .addCss(dui_m_0)
                                                .span12(uriTextBox = TextBox.create().addCss(dui_m_0).setLabelFloatLeft(true).setLabel("URI").setRequired(true))
                                                .span12(nameTextBox = TextBox.create().addCss(dui_m_0).setLabelFloatLeft(true).setLabel("Name"))
                                ).setFlexGrow(1))
                                .appendChild(FlexItem.of(
                                        FlexItem.of(
                                                Button.create(Icons.close())
                                                        .setTabIndex(-1) // フォーカスを当てない
                                                        .circle()
                                                        .addCss(dui_small, dui_m_1)
                                                        .addClickListener(evt -> {
                                                            remove();
                                                            evt.preventDefault();
                                                        })
                                        )
                                ).setFlexGrow(0))
                )
                .element();

        uriTextBox.addChangeListener((s, v1) -> {
            // URIが変更されたらイベントハンドラに通知
            handlers.forEach(handler -> handler.onURIChanged(this, v1));
        });
    }

    private void remove() {
        Animation.create(root)
                .duration(400)
                .transition(Transition.FADE_OUT_RIGHT)
                .callback(element -> {
                    root.remove();
                    // イベントハンドラに通知
                    handlers.forEach(handler -> handler.onRemoved(this));
                })
                .animate();
    }

    @Override
    public void setURI(String uri) {
        uriTextBox.setValue(uri);
    }

    @Override
    public String getURI() {
        return uriTextBox.getValue();
    }

    @Override
    public void setInvalid(boolean invalid) {
        if (invalid) {
            uriTextBox.invalidate("Unexpected URI");
        } else {
            uriTextBox.validate();
        }
    }

    @Override
    public void setName(String name) {
        nameTextBox.setValue(name);
    }

    @Override
    public String getName() {
        return nameTextBox.getValue();
    }

    @Override
    public HTMLElement element() {
        return root;
    }

    @Override
    public void setUiHandlers(LiveStreamingFormUIHandlers handler) {
        handlers.add(handler);
    }
}
