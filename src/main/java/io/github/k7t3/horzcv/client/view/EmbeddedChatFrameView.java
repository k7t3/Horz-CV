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

import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.*;
import gwt.material.design.client.ui.*;
import io.github.k7t3.horzcv.client.model.EmbeddedChatFrame;
import io.github.k7t3.horzcv.client.presenter.EmbeddedChatPresenter;

import java.util.List;

public class EmbeddedChatFrameView extends MaterialPanel implements EmbeddedChatPresenter.EmbeddedChatDisplay {

    private final EmbeddedChatFrame frame;

    private final MaterialButton dropDownButton = new MaterialButton("", IconType.ARROW_DROP_DOWN);

    private final MaterialLink openStreamingLink = new MaterialLink(IconType.OPEN_IN_NEW);

    private final MaterialLink closeLink = new MaterialLink(IconType.CLOSE);

    private final MaterialButton moveLeftButton = new MaterialButton("", IconType.ARROW_BACK);

    private final MaterialButton moveRightButton = new MaterialButton("", IconType.ARROW_FORWARD);

    private final MaterialTextBox displayNameField = new MaterialTextBox();

    private final InlineHTML embeddedChatFrame = new InlineHTML();

    public EmbeddedChatFrameView(EmbeddedChatFrame frame) {
        setClass("chatContainer");
        this.frame = frame;
        init();
    }

    private void init() {
        embeddedChatFrame.setHTML(frame.frame());
        add(embeddedChatFrame);

        displayNameField.setText(frame.entry().getDisplayName());
        displayNameField.setPlaceholder("Display Name");
        displayNameField.setInitialClasses("chatDisplayName");

        moveLeftButton.setType(ButtonType.RAISED);

        moveRightButton.setType(ButtonType.RAISED);
        moveRightButton.setIconPosition(IconPosition.RIGHT);

        dropDownButton.setType(ButtonType.RAISED);
        dropDownButton.setTooltip("Open the menu");

        var buttons = new MaterialPanel();
        buttons.setDisplay(Display.FLEX);
        buttons.setFlexDirection(FlexDirection.ROW);
        buttons.setAlignItems("center");
        buttons.setMargin(4);
        buttons.setGap("4px");

        var row = new MaterialRow();
        row.setDisplay(Display.FLEX);
        row.setFlexDirection(FlexDirection.ROW);
        row.setAlignItems("center");
        row.setMargin(4);
        row.setGap("4px");

        var leftColumn = new MaterialColumn(2, 2, 2);
        var labelColumn = new MaterialColumn(6, 6, 6);
        var menuColumn = new MaterialColumn(2, 2, 2);
        var rightColumn = new MaterialColumn(2, 2, 2);
        leftColumn.add(moveLeftButton);
        labelColumn.add(displayNameField);
        menuColumn.add(dropDownButton);
        rightColumn.add(moveRightButton);

        // ボタンを右詰めにする
        rightColumn.setDisplay(Display.FLEX);
        rightColumn.setFlexJustifyContent(FlexJustifyContent.END);

        row.add(leftColumn);
        row.add(labelColumn);
        row.add(menuColumn);
        row.add(rightColumn);
        add(row);

        openStreamingLink.setText("Open Streaming");
        openStreamingLink.setTooltip("Open the streaming page in a new tab");
        openStreamingLink.setIconType(IconType.OPEN_IN_NEW);
        openStreamingLink.setTarget("_blank");
        openStreamingLink.getElement().setAttribute("rel", "noopener");
        openStreamingLink.setIconPosition(IconPosition.RIGHT);
        openStreamingLink.setSeparator(true);

        closeLink.setText("Close");
        closeLink.setIconPosition(IconPosition.RIGHT);
        closeLink.setSeparator(true);

        var id = "dp-" + frame.entry().getId();
        dropDownButton.setActivates(id);

        var dropDown = new MaterialDropDown<MaterialLink>();
        dropDown.setConstrainWidth(false);
        dropDown.setItems(List.of(openStreamingLink, closeLink), o -> o);
        dropDown.setActivator(id);
        add(dropDown);
    }

    @Override
    public InlineHTML getEmbeddedChatFrame() {
        return embeddedChatFrame;
    }

    @Override
    public MaterialButton getDropDownButton() {
        return dropDownButton;
    }

    @Override
    public MaterialLink getOpenStreamingLink() {
        return openStreamingLink;
    }

    @Override
    public MaterialLink getCloseLink() {
        return closeLink;
    }

    @Override
    public MaterialButton getMoveLeftButton() {
        return moveLeftButton;
    }

    @Override
    public MaterialButton getMoveRightButton() {
        return moveRightButton;
    }

    @Override
    public MaterialTextBox getDisplayNameField() {
        return displayNameField;
    }

    @Override
    public Widget getRoot() {
        return this;
    }
}
