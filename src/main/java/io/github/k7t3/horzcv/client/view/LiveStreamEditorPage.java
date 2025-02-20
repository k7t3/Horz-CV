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
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.FlexAlignItems;
import gwt.material.design.client.constants.FlexDirection;
import gwt.material.design.client.constants.FooterType;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.*;
import io.github.k7t3.horzcv.client.presenter.LiveStreamEditorPresenter;

public class LiveStreamEditorPage extends AbstractPage implements LiveStreamEditorPresenter.Display {

    private final MaterialPanel table = new MaterialPanel();

    private final MaterialButton add = new MaterialButton("Add", IconType.ADD_CIRCLE_OUTLINE);

    private final MaterialButton submit = new MaterialButton("Submit");

    public LiveStreamEditorPage() {
        init();
    }

    private void init() {
        //setContainerEnabled(true);
        setHeight("100%");
        setDisplay(Display.FLEX);
        setFlexDirection(FlexDirection.COLUMN);

        var horzSpacer = new MaterialPanel();
        horzSpacer.setFlexGrow(1);

        //add.setWidth("100%");
        submit.setWidth("100%");

        var addColumn = new MaterialColumn(12, 6, 4);
        var submitColumn = new MaterialColumn(12, 6, 4);
        submitColumn.setOffset("l4");

        addColumn.add(add);
        submitColumn.add(submit);

        var supportedForPanel = new MaterialColumn();
        supportedForPanel.setGrid("s12");
        supportedForPanel.setDisplay(Display.FLEX);
        supportedForPanel.setFlexDirection(FlexDirection.ROW);
        supportedForPanel.setFlexAlignItems(FlexAlignItems.CENTER);
        supportedForPanel.setColumnGap("8px");
        supportedForPanel.setPadding(10);
        var onlySupportedFor = new MaterialLabel("Only supported for");
        onlySupportedFor.setFontSize("0.8em");

        var youtubeIcon = new MaterialIcon();
        // 2xサイズのGitHubアイコン
        youtubeIcon.setCustomIconType("fa-brands fa-youtube");
        var youtubeLink = new MaterialLink(ButtonType.LINK, "", youtubeIcon);
        youtubeLink.setHref(Constants.YOUTUBE);
        youtubeLink.setTarget("_blank");
        youtubeLink.getElement().setAttribute("rel", "noopener");
        youtubeLink.setFlexGrow(0);
        youtubeLink.setTooltip("YouTube");

        var twitchIcon = new MaterialIcon();
        // 2xサイズのGitHubアイコン
        twitchIcon.setCustomIconType("fa-brands fa-twitch");
        var twitchLink = new MaterialLink(ButtonType.LINK, "", twitchIcon);
        twitchLink.setHref(Constants.TWITCH);
        twitchLink.setTarget("_blank");
        twitchLink.getElement().setAttribute("rel", "noopener");
        twitchLink.setFlexGrow(0);
        twitchLink.setTooltip("Twitch");

        supportedForPanel.add(onlySupportedFor);
        supportedForPanel.add(youtubeLink);
        supportedForPanel.add(twitchLink);

        var container = new MaterialRow();
        container.setMarginTop(20); // 20pxのマージンを追加(bottomと同じ)
        container.setContainerEnabled(true);
        container.add(table);
        container.add(addColumn);
        container.add(submitColumn);
        container.add(supportedForPanel);

        var footer = new MaterialFooter();
        footer.addStyleName("footer-copyright");
        footer.setType(FooterType.FIXED);
        var copyRight = new MaterialFooterCopyright();
        var copyRightLabel = new MaterialLabel(Constants.COPYRIGHT);
        copyRightLabel.setInitialClasses("copyright");
        copyRight.add(copyRightLabel);
        footer.add(copyRight);

        add(container);
        //add(vertSpacer);
        add(footer);
    }

    @Override
    public Widget getRoot() {
        return this;
    }

    @Override
    public MaterialPanel getStreamsContainer() {
        return table;
    }

    @Override
    public MaterialButton getAddButton() {
        return add;
    }

    @Override
    public MaterialButton getSubmitButton() {
        return submit;
    }
}
