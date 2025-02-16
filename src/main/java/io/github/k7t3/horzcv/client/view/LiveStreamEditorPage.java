package io.github.k7t3.horzcv.client.view;

import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.FlexDirection;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialPanel;
import io.github.k7t3.horzcv.client.presenter.LiveStreamEditorPresenter;

public class LiveStreamEditorPage extends AbstractPage implements LiveStreamEditorPresenter.Display {

    private final MaterialPanel streamsContainer = new MaterialPanel();

    private final MaterialButton add = new MaterialButton("Add", IconType.ADD_CIRCLE_OUTLINE);

    private final MaterialButton submit = new MaterialButton("Submit");

    public LiveStreamEditorPage() {
        init();
    }

    private void init() {
        setContainerEnabled(true);

        setDisplay(Display.FLEX);
        setFlexDirection(FlexDirection.COLUMN);

        var spacer = new MaterialPanel();
        spacer.setFlexGrow(1);

        submit.setFlexGrow(1);

        var footer = new MaterialPanel();
        footer.setDisplay(Display.FLEX);
        footer.setFlexDirection(FlexDirection.ROW);
        footer.add(add);
        footer.add(spacer);
        footer.add(submit);

        add(streamsContainer);
        add(footer);
    }

    @Override
    public Widget getRoot() {
        return this;
    }

    @Override
    public MaterialPanel getStreamsContainer() {
        return streamsContainer;
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
