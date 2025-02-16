package io.github.k7t3.horzcv.client.presenter;

import com.google.gwt.user.client.ui.InlineHTML;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.animate.MaterialAnimation;
import gwt.material.design.client.ui.animate.Transition;
import io.github.k7t3.horzcv.client.logic.EmbeddedChatList;
import io.github.k7t3.horzcv.client.model.EmbeddedChatFrame;
import io.github.k7t3.horzcv.client.model.LiveStream;
import io.github.k7t3.horzcv.client.view.EmbeddedChatFrameView;
import io.github.k7t3.horzcv.client.view.EmbeddedChatListPage;
import io.github.k7t3.horzcv.client.view.Page;
import io.github.k7t3.horzcv.client.view.View;

import java.util.List;

public class EmbeddedChatPresenter {

    public interface EmbeddedChatDisplay extends View {
        InlineHTML getEmbeddedChatFrame();
        MaterialButton getMinimizeButton();
    }

    public interface Display extends Page {
        MaterialPanel getContainer();
    }

    private final EmbeddedChatList chatList;

    private final Display display;

    public EmbeddedChatPresenter(EmbeddedChatList chatList) {
        this.chatList = chatList;
        display = new EmbeddedChatListPage();
    }

    public Display getDisplay() {
        return display;
    }

    public void setLiveStreams(List<LiveStream> streams) {
        chatList.setAll(streams);

        var frames = chatList.getFrames();

        var container = display.getContainer();
        container.clear();

        for (var frame : frames) {
            addChatFrame(frame);
        }
    }

    private void addChatFrame(EmbeddedChatFrame frame) {
        var item = new EmbeddedChatFrameView(frame);
        var view = item.getRoot();

        item.getMinimizeButton().addClickHandler(e -> {
            var fadeOut = new MaterialAnimation(item.getRoot());
            fadeOut.setTransition(Transition.FADEOUTRIGHT);
            fadeOut.setCompleteCallback(view::removeFromParent);
            fadeOut.animate();
        });

        var container = display.getContainer();
        container.add(view);
    }

}
