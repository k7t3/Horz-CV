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

import io.github.k7t3.horzcv.client.view.LayoutEvent;
import io.github.k7t3.horzcv.client.view.Pages;
import io.github.k7t3.horzcv.client.view.ShellView;
import io.github.k7t3.horzcv.client.view.Slots;
import org.dominokit.domino.api.client.annotations.presenter.*;
import org.dominokit.domino.api.client.mvp.presenter.ViewablePresenter;
import org.dominokit.domino.api.shared.extension.PredefinedSlots;

import java.util.logging.Logger;

@PresenterProxy(name = Presenters.SHELL)
@AutoRoute(routeOnce = true)
@AutoReveal
@RegisterSlots(Slots.CONTENT)
@Slot(PredefinedSlots.BODY_SLOT)
@Singleton
@OnStateChanged(LayoutEvent.class)
public class ShellPresenter extends ViewablePresenter<ShellView> implements ShellView.ShellUiHandlers {

    private static final Logger LOGGER = Logger.getLogger(ShellPresenter.class.getName());

    @Override
    protected void postConstruct() {
        LOGGER.info("ShellPresenter postConstruct");

        ThemeManager.INSTANCE.initialize();
    }

    @Override
    public void onTitleClicked() {
        publishState(Pages.HOME);
    }

    @OnReveal
    public void onReveal() {
        if (history().currentToken().paths().isEmpty()) {
            LOGGER.info("No token, redirecting to home");
            publishState(Pages.HOME);
        }
    }

}
