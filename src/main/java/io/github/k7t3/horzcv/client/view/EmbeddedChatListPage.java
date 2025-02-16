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
