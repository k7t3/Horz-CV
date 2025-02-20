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

import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialPanel;
import io.github.k7t3.horzcv.client.presenter.EmbeddedChatPresenter;

public class EmbeddedChatListPage extends AbstractPage implements EmbeddedChatPresenter.Display {

    public EmbeddedChatListPage() {
        init();
    }

    private void init() {
        setId("chatList");
    }

    @Override
    public MaterialPanel getContainer() {
        return this;
    }

    @Override
    public Widget getRoot() {
        return this;
    }

}
