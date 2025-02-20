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

package io.github.k7t3.horzcv.client.presenter;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.InlineHTML;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import io.github.k7t3.horzcv.client.logic.DataStore;
import io.github.k7t3.horzcv.client.logic.EmbeddedChatList;
import io.github.k7t3.horzcv.client.model.EmbeddedChatFrame;
import io.github.k7t3.horzcv.client.model.LiveStreamingEntry;
import io.github.k7t3.horzcv.client.model.LiveStreamingIdentity;
import io.github.k7t3.horzcv.client.view.EmbeddedChatFrameView;
import io.github.k7t3.horzcv.client.view.EmbeddedChatListPage;
import io.github.k7t3.horzcv.client.view.Page;
import io.github.k7t3.horzcv.client.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmbeddedChatPresenter {

    public interface EmbeddedChatDisplay extends View {
        InlineHTML getEmbeddedChatFrame();
        MaterialButton getDropDownButton();
        MaterialLink getOpenStreamingLink();
        MaterialLink getCloseLink();
        MaterialButton getMoveLeftButton();
        MaterialButton getMoveRightButton();
        MaterialTextBox getDisplayNameField();
    }

    public interface Display extends Page {
        MaterialPanel getContainer();
    }

    private final EmbeddedChatList chatList;

    private final Display display;

    private final List<EmbeddedChatFrameView> items = new ArrayList<>();

    private final DataStore displayNameStore;

    public EmbeddedChatPresenter(EmbeddedChatList chatList, DataStore displayNameStore) {
        this.chatList = chatList;
        this.displayNameStore = displayNameStore;
        display = new EmbeddedChatListPage();
    }

    public Display getDisplay() {
        return display;
    }

    public void setLiveStreams(List<LiveStreamingEntry> streams) {
        items.clear();
        chatList.setAll(streams);

        var frames = chatList.getFrames();

        var container = display.getContainer();
        container.clear();

        for (var frame : frames) {
            addChatFrame(frame);
        }

        updateButtonStateAll();
    }

    private void pushToken() {
        var streams = chatList.getFrames()
                .stream()
                .map(EmbeddedChatFrame::entry)
                .map(LiveStreamingEntry::asIdentity)
                .collect(Collectors.toList());
        var token = LiveStreamingIdentity.toToken(streams);
        History.replaceItem(token, false);
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
            pushToken();
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
                container.insert(view, index - 1);
                updateButtonState(item, index - 1);
                updateButtonState(previous, index);
                pushToken();
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
                container.insert(view, index + 1);
                updateButtonState(item, index + 1);
                updateButtonState(next, index);
                pushToken();
            }
        });

        // 表示名の変更イベントを設定
        item.getDisplayNameField().addValueChangeHandler(e -> {
            var displayName = e.getValue();
            frame.entry().setDisplayName(displayName);

            // 表示名をデータストアに保存
            var identity = frame.entry().asIdentity();
            if (displayName != null) {
                displayNameStore.store(identity.toToken(), displayName);
            } else {
                displayNameStore.remove(identity.toToken());
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
