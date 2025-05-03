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
import io.github.k7t3.horzcv.client.presenter.OOPSPresenter;
import io.github.k7t3.horzcv.client.view.OOPSView;
import org.dominokit.domino.api.client.annotations.UiView;
import org.dominokit.domino.ui.elements.ParagraphElement;
import org.dominokit.domino.ui.grid.flex.FlexAlign;
import org.dominokit.domino.ui.grid.flex.FlexDirection;
import org.dominokit.domino.ui.grid.flex.FlexLayout;
import org.dominokit.domino.view.BaseElementView;

@UiView(presentable = OOPSPresenter.class)
public class OOPSViewImpl extends BaseElementView<HTMLElement> implements OOPSView {

    private final HTMLElement element;
    private final ParagraphElement paragraph;

    private OOPSUiHandlers handler;

    public OOPSViewImpl() {
        element = FlexLayout.create()
                .setDirection(FlexDirection.TOP_TO_BOTTOM)
                .setAlignItems(FlexAlign.CENTER)
                .appendChild(h(2).textContent("OOPS!").addCss(dui_font_bold))
                .appendChild(paragraph = p().addCss(dui_fg_error))
                .element();
    }

    @Override
    protected HTMLElement init() {
        return element;
    }

    @Override
    public void setMessage(String message) {
        paragraph.textContent(message);
    }

    @Override
    public void setUiHandlers(OOPSUiHandlers handler) {
        this.handler = handler;
    }
}
