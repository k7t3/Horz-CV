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
import io.github.k7t3.horzcv.client.presenter.ShellPresenter;
import io.github.k7t3.horzcv.client.view.ShellView;
import org.dominokit.domino.api.client.annotations.UiView;
import org.dominokit.domino.api.client.mvp.slots.IsSlot;
import org.dominokit.domino.ui.layout.AppLayout;
import org.dominokit.domino.view.BaseElementView;
import org.dominokit.domino.view.slots.SingleElementSlot;

import java.util.logging.Logger;

@UiView(presentable = ShellPresenter.class)
public class ShellViewImpl extends BaseElementView<HTMLElement> implements ShellView {

    private static final Logger LOGGER = Logger.getLogger(ShellViewImpl.class.getName());

    private final AppLayout layout;

    private ShellUiHandlers handler;

    public ShellViewImpl() {
        LOGGER.info("ShellViewImpl constructor");
        layout = new AppLayout("Horz CV");
        layout.withNavBar((appLayout, nav) -> {

            nav.getTitleTextElement().id(Ids.TITLE_ID);
            nav.getTitleElement().addClickListener(evt -> {
                if (handler != null) {
                    handler.onTitleClicked();
                }
            });
        });
        layout.getContent().setPadding("0.3em 0.8em");
    }

    @Override
    protected HTMLElement init() {
        return layout.element();
    }

    @Override
    public IsSlot<?> getContentSlot() {
        return SingleElementSlot.of(layout.getContent());
    }

    @Override
    public void setUiHandlers(ShellUiHandlers handler) {
        this.handler = handler;
    }
}
