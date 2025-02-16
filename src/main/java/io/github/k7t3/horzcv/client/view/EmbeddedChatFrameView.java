package io.github.k7t3.horzcv.client.view;

import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialPanel;
import io.github.k7t3.horzcv.client.model.EmbeddedChatFrame;
import io.github.k7t3.horzcv.client.presenter.EmbeddedChatPresenter;

public class EmbeddedChatFrameView extends MaterialPanel implements EmbeddedChatPresenter.EmbeddedChatDisplay {

    private final EmbeddedChatFrame frame;

    private final MaterialButton minimizeButton = new MaterialButton("Minimize");

    private final InlineHTML embeddedChatFrame = new InlineHTML();

    public EmbeddedChatFrameView(EmbeddedChatFrame frame) {
        setClass("chatContainer");
        this.frame = frame;
        init();
    }

    private void init() {
        embeddedChatFrame.setHTML(frame.frame());
        add(embeddedChatFrame);
        add(minimizeButton);
    }

    @Override
    public InlineHTML getEmbeddedChatFrame() {
        return embeddedChatFrame;
    }

    @Override
    public MaterialButton getMinimizeButton() {
        return minimizeButton;
    }

    @Override
    public Widget getRoot() {
        return this;
    }
}
