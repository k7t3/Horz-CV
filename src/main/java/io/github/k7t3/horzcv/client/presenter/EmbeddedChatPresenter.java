package io.github.k7t3.horzcv.client.presenter;

import com.google.gwt.user.client.ui.InlineHTML;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import io.github.k7t3.horzcv.client.logic.EmbeddedChatList;
import io.github.k7t3.horzcv.client.model.EmbeddedChatFrame;
import io.github.k7t3.horzcv.client.model.LiveStreamingEntry;
import io.github.k7t3.horzcv.client.view.EmbeddedChatFrameView;
import io.github.k7t3.horzcv.client.view.EmbeddedChatListPage;
import io.github.k7t3.horzcv.client.view.Page;
import io.github.k7t3.horzcv.client.view.View;

import java.util.ArrayList;
import java.util.List;

public class EmbeddedChatPresenter {

    public interface EmbeddedChatDisplay extends View {
        InlineHTML getEmbeddedChatFrame();
        MaterialButton getDropDownButton();
        MaterialLink getOpenStreamingLink();
        MaterialLink getCloseLink();
        MaterialButton getMoveLeftButton();
        MaterialButton getMoveRightButton();
    }

    public interface Display extends Page {
        MaterialPanel getContainer();
    }

    private final EmbeddedChatList chatList;

    private final Display display;

    private final List<EmbeddedChatFrameView> items = new ArrayList<>();

    public EmbeddedChatPresenter(EmbeddedChatList chatList) {
        this.chatList = chatList;
        display = new EmbeddedChatListPage();
    }

    public Display getDisplay() {
        return display;
    }

    public void setLiveStreams(List<LiveStreamingEntry> streams) {
        chatList.setAll(streams);

        var frames = chatList.getFrames();

        var container = display.getContainer();
        container.clear();

        for (var frame : frames) {
            addChatFrame(frame);
        }

        updateButtonStateAll();
    }

    private void addChatFrame(EmbeddedChatFrame frame) {
        var item = new EmbeddedChatFrameView(frame);
        items.add(item);

        var view = item.getRoot();

        var container = display.getContainer();
        container.add(view);

        // 削除ボタンのクリックイベントを設定
        item.getCloseLink().addClickHandler(e -> {
            item.removeFromParent();
            items.remove(item);
            chatList.getFrames().remove(frame);
            updateButtonStateAll();
        });

        // ストリーミングを開くボタンのリンクを設定
        item.getOpenStreamingLink().setHref(frame.entry().getUrl());

        // 左右移動ボタンのクリックイベントを設定
        item.getMoveLeftButton().addClickHandler(e -> {
            var index = items.indexOf(item);
            if (0 < index) {
                var previous = items.get(index - 1);
                var previousFrame = chatList.getFrames().get(index - 1);
                chatList.getFrames().set(index - 1, frame);
                chatList.getFrames().set(index, previousFrame);
                items.set(index - 1, item);
                items.set(index, previous);
                container.remove(view);
                container.insert(item.getRoot(), index - 1);
                updateButtonState(item, index - 1);
                updateButtonState(previous, index);
            }
        });
        item.getMoveRightButton().addClickHandler(e -> {
            var index = items.indexOf(item);
            if (index < items.size() - 1) {
                var next = items.get(index + 1);
                var nextFrame = chatList.getFrames().get(index + 1);
                chatList.getFrames().set(index, nextFrame);
                chatList.getFrames().set(index + 1, frame);
                items.set(index + 1, item);
                items.set(index, next);
                container.remove(view);
                container.insert(item.getRoot(), index + 1);
                updateButtonState(item, index + 1);
                updateButtonState(next, index);
            }
        });
    }

    private void updateButtonStateAll() {
        for (var i = 0; i < items.size(); i++) {
            updateButtonState(items.get(i), i);
        }
    }

    private void updateButtonState(EmbeddedChatFrameView view, int index) {
        var leftButton = view.getMoveLeftButton();
        var rightButton = view.getMoveRightButton();

        leftButton.setEnabled(index > 0);
        rightButton.setEnabled(index < items.size() - 1);
    }

}
