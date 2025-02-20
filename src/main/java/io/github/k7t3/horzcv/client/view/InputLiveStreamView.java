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
import gwt.material.design.client.constants.ButtonType;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.FieldType;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import io.github.k7t3.horzcv.client.model.StreamingService;
import io.github.k7t3.horzcv.client.presenter.LiveStreamEditorPresenter;

/**
 * ストリーミングサービスの選択とURL入力を行うUI。
 */
public class InputLiveStreamView extends MaterialRow implements LiveStreamEditorPresenter.Entry {

    private final MaterialTextBox url = new MaterialTextBox();

    private final MaterialTextBox displayName = new MaterialTextBox();

    private final MaterialButton removeButton = new MaterialButton("", IconType.CLEAR, ButtonType.FLOATING);

    public InputLiveStreamView() {
        init();
    }

    private void init() {
        //setClass("inputLiveStream");

        // エディタの設定
        url.setLabel("Service");

        // 表示名を入力するテキストボックスの設定
        displayName.setPlaceholder("Display Name(Optional)");
        displayName.setTooltip("Display Name");

        // 削除ボタンの設定
        removeButton.setTabIndex(-1); // タブでフォーカスされないようにする
        //removeButton.setType(ButtonType.FLOATING);
        removeButton.setType(ButtonType.FLAT);
        removeButton.setBackgroundColor(Color.TRANSPARENT);
        removeButton.setIconColor(Color.RED);
        removeButton.setCircle(true);
        removeButton.setInitialClasses("input-field");

//        add(url);
//        add(removeButton);

        var urlColumn = new MaterialColumn(12, 12, 8);
        var descColumn = new MaterialColumn(11, 5, 3);
        var removeColumn = new MaterialColumn(1, 1, 1);

        descColumn.setOffset("m6");

        urlColumn.add(url);
        descColumn.add(displayName);
        removeColumn.add(removeButton);

        add(urlColumn);
        add(descColumn);
        add(removeColumn);
    }

    @Override
    public void updateService(StreamingService service) {
        if (service == null) {
            url.setLabel("Service");
            url.setErrorText("Invalid URL");
        } else {
            url.setLabel(service.getText());
            url.clearErrorText();
        }
    }

    @Override
    public Widget getRoot() {
        return this;
    }

    @Override
    public MaterialTextBox getUrlField() {
        return url;
    }

    @Override
    public MaterialTextBox getDisplayNameField() {
        return displayName;
    }

    @Override
    public MaterialButton getRemoveButton() {
        return removeButton;
    }
}
