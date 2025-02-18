package io.github.k7t3.horzcv.client.view;

import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.Display;
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

        add.setWidth("100%");
        submit.setWidth("100%");

        var addColumn = new MaterialColumn(12, 6, 2);
        var submitColumn = new MaterialColumn(12, 6, 2);
        submitColumn.setOffset("l10");

        addColumn.add(add);
        submitColumn.add(submit);

        var container = new MaterialRow();
        container.setContainerEnabled(true);
        container.add(table);
        container.add(addColumn);
        container.add(submitColumn);

        var vertSpacer = new MaterialPanel();
        vertSpacer.setFlexGrow(1);

        var footer = new MaterialFooter();
        footer.addStyleName("footer-copyright");
        footer.setType(FooterType.FIXED);
        var copyRight = new MaterialFooterCopyright();
        copyRight.add(new MaterialLabel("© 2025 Copyright k7t3"));
        footer.add(copyRight);

        add(container);
        add(vertSpacer);
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
