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
import elemental2.dom.HTMLElement;
import io.github.k7t3.horzcv.client.presenter.HomePresenter;
import io.github.k7t3.horzcv.client.view.Constants;
import io.github.k7t3.horzcv.client.view.HomeView;
import io.github.k7t3.horzcv.client.view.LiveStreamingFormView;
import org.dominokit.domino.api.client.annotations.UiView;
import org.dominokit.domino.ui.animations.Animation;
import org.dominokit.domino.ui.animations.Transition;
import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.elements.FooterElement;
import org.dominokit.domino.ui.grid.GridLayout;
import org.dominokit.domino.ui.grid.flex.FlexAlign;
import org.dominokit.domino.ui.grid.flex.FlexDirection;
import org.dominokit.domino.ui.grid.flex.FlexItem;
import org.dominokit.domino.ui.grid.flex.FlexJustifyContent;
import org.dominokit.domino.ui.grid.flex.FlexLayout;
import org.dominokit.domino.view.BaseElementView;

import java.util.logging.Logger;

@UiView(presentable = HomePresenter.class)
public class HomeViewImpl extends BaseElementView<HTMLElement> implements HomeView {

    private static final Logger LOGGER = Logger.getLogger(HomeViewImpl.class.getName());

    private final HTMLElement root;
    private final FlexLayout flex;
    private final FlexItem<HTMLDivElement> last;
    private final Button submit;
    private final Button add;

    private HomeUIHandlers handler;

    public HomeViewImpl() {
        LOGGER.info("HomeViewImpl constructor");

        var actions = FlexLayout.create()
                .setDirection(FlexDirection.LEFT_TO_RIGHT)
                .setJustifyContent(FlexJustifyContent.END)
                .appendChild(FlexItem.of(
                        submit = Button.create("Submit").addCss(dui_primary, dui_m_1, dui_w_28)
                ))
                .appendChild(FlexItem.of(
                        add = Button.create("Add").addCss(dui_m_1, dui_w_28)
                ));
        last = FlexItem.of(actions);

        flex = FlexLayout.create()
                //.addCss(dui_gap_4)
                //.addCss()
                .setDirection(FlexDirection.TOP_TO_BOTTOM)
                .setAlignItems(FlexAlign.CENTER)
                .appendChild(last);

        submit.addClickListener(evt -> {
            evt.preventDefault();
            if (handler != null) {
                handler.onSubmitted();
            }
        });
        add.addClickListener(evt -> {
            evt.preventDefault();
            if (handler != null) {
                var view = new LiveStreamingFormViewImpl();
                addLiveStreamingForm(view);
                handler.onAddedLiveStreamingForm(view);
            }
        });

        var layout = GridLayout.create()
                .withContent((parent, self) -> {
                    self.appendChild(flex);
                })
                .withFooter((parent, self) -> {
                    var flex = FlexLayout.create()
                            .setJustifyContent(FlexJustifyContent.END)
                            .setAlignItems(FlexAlign.CENTER)
                            .appendChild(Constants.COPYRIGHT);
                    self.appendChild(
                            FooterElement.of(flex.element())
                    );
                });

        root = layout.element();
    }

    @Override
    public void addLiveStreamingForm(LiveStreamingFormView form) {
        LOGGER.info("addLiveStreamingForm");
        // dui_p_x_4
        var item = FlexItem.of(form).addCss(dui_w_3_4p);
        //flex.appendChild(item);
        flex.appendChildBefore(item, last);
    }

    @Override
    public void setDisableAddButton(boolean disable) {
        LOGGER.info("setDisableAddButton: " + disable);
        add.setDisabled(disable);
    }

    @Override
    public void startAnimation() {
        Animation.create(root)
                .transition(Transition.FADE_IN)
                .duration(750)
                .animate();
    }

    @Override
    protected HTMLElement init() {
        return root;
    }

    @Override
    public void setUiHandlers(HomeUIHandlers handler) {
        this.handler = handler;
    }

}
