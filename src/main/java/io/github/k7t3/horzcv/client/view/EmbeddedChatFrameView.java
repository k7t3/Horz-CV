package io.github.k7t3.horzcv.client.view;

import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.base.DropdownItemRenderer;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.FlexDirection;
import gwt.material.design.client.constants.IconPosition;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import io.github.k7t3.horzcv.client.model.EmbeddedChatFrame;
import io.github.k7t3.horzcv.client.presenter.EmbeddedChatPresenter;

import java.util.List;

public class EmbeddedChatFrameView extends MaterialPanel implements EmbeddedChatPresenter.EmbeddedChatDisplay {

    private final EmbeddedChatFrame frame;

    private final MaterialButton dropDownButton = new MaterialButton("DropDown", IconType.ARROW_DROP_DOWN);

    private final MaterialLink openStreamingLink = new MaterialLink(IconType.OPEN_IN_NEW);

    private final MaterialLink closeLink = new MaterialLink(IconType.CLOSE);

    private final MaterialButton moveLeftButton = new MaterialButton("", IconType.ARROW_BACK);

    private final MaterialButton moveRightButton = new MaterialButton("", IconType.ARROW_FORWARD);

    private final InlineHTML embeddedChatFrame = new InlineHTML();

    public EmbeddedChatFrameView(EmbeddedChatFrame frame) {
        setClass("chatContainer");
        this.frame = frame;
        init();
    }

    private void init() {
        embeddedChatFrame.setHTML(frame.frame());
        add(embeddedChatFrame);

        var buttons = new MaterialPanel();
        buttons.setDisplay(Display.FLEX);
        buttons.setFlexDirection(FlexDirection.ROW);
        buttons.setAlignItems("center");
        buttons.setMargin(4);
        buttons.setGap("4px");

        var label = new MaterialLabel();
        label.setText(frame.entry().getDescription());
        label.setFlexGrow(1);

        buttons.add(moveLeftButton);
        buttons.add(label);
        buttons.add(dropDownButton);
        buttons.add(moveRightButton);

        add(buttons);

        openStreamingLink.setText("Open Streaming");
        openStreamingLink.setTooltip("Open the streaming service in a new tab");
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
        dropDownButton.setIconPosition(IconPosition.RIGHT);

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
    public Widget getRoot() {
        return this;
    }
}
