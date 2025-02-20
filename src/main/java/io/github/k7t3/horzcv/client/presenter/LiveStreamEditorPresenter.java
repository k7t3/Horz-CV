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

import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import io.github.k7t3.horzcv.client.logic.DataStore;
import io.github.k7t3.horzcv.client.logic.LiveStreamingManager;
import io.github.k7t3.horzcv.client.model.LiveStreamingIdentity;
import io.github.k7t3.horzcv.client.model.LiveStreamingEntry;
import io.github.k7t3.horzcv.client.model.StreamingService;
import io.github.k7t3.horzcv.client.view.InputLiveStreamView;
import io.github.k7t3.horzcv.client.view.LiveStreamEditorPage;
import io.github.k7t3.horzcv.client.view.Page;
import io.github.k7t3.horzcv.client.view.View;

import java.util.ArrayList;
import java.util.List;

public class LiveStreamEditorPresenter {

    private static final int INPUT_LIMIT = 6;

    /**
     * ストリーミングサービスの選択とURL入力を行う画面。
     */
    public interface Entry extends View {
        void updateService(StreamingService service);
        MaterialTextBox getUrlField();
        MaterialTextBox getDisplayNameField();
        MaterialButton getRemoveButton();
    }

    /**
     * ストリームの編集画面。
     */
    public interface Display extends Page {
        MaterialPanel getStreamsContainer();
        MaterialButton getAddButton();
        MaterialButton getSubmitButton();
    }

    private final LiveStreamingManager manager;

    private final DataStore displayNameStore;

    private final Display display;

    private final List<Entry> items = new ArrayList<>();

    public LiveStreamEditorPresenter(LiveStreamingManager manager, DataStore displayNameStore) {
        this.manager = manager;
        this.displayNameStore = displayNameStore;
        display = new LiveStreamEditorPage();
        init();
    }

    private void init() {
        display.getAddButton().addClickHandler(e -> {
            var entry = new LiveStreamingEntry();
            manager.getEntries().add(entry);
            addLiveStreamingEntry(entry);
            updateAddButtonStyle();
        });
    }

    public Display getDisplay() {
        return display;
    }

    public void setInputItems(List<LiveStreamingIdentity> streams) {
        // エディタに生の情報を設定
        manager.restoreFromIdentities(streams);

        var entries = manager.getEntries();
        for (var entry : entries) {
            // データストアに表示名が保存されていた場合はそれを反映
            if (entry.isValid()) {
                var identity = entry.asIdentity();
                var displayName = displayNameStore.load(identity.toToken());
                if (displayName != null) {
                    entry.setDisplayName(displayName);
                }
            }
        }

        // 現在の内容をクリア
        var container = display.getStreamsContainer();
        container.clear();
        items.clear();

        // ビューに反映
        entries.forEach(this::addLiveStreamingEntry);

        // 最低4つの入力欄を確保
        var n = manager.getEntries().size();
        for (var i = n; i < 4; i++) {
            var entry = new LiveStreamingEntry();
            manager.getEntries().add(entry);
            addLiveStreamingEntry(entry);
        }
    }

    /**
     * ビューの入力値をモデルに反映する。
     */
    public void updateModels() {
        for (var i = 0; i < items.size(); i++) {
            var item = items.get(i);
            var model = manager.getEntries().get(i);

            var url = item.getUrlField().getText();
            var foundService = manager.findSuitableService(url);
            var displayName = item.getDisplayNameField().getText();

            model.setUrl(url);
            model.setService(foundService);
            model.setDisplayName(displayName);
        }
    }

    private void validURL(InputLiveStreamView item, String url) {
        var foundService = manager.findSuitableService(url);

        // ビューに反映
        item.updateService(foundService);

        var detector = manager.getDetector(foundService);
        if (detector != null && detector.isValidURL(url)) {
            var id = detector.parseId(url);
            var identity = new LiveStreamingIdentity(foundService, id);
            var displayName = displayNameStore.load(identity.toToken());
            if (displayName != null) {
                item.getDisplayNameField().setText(displayName);
            } else {
                item.getDisplayNameField().setText(id);
            }
        } else {
            item.getDisplayNameField().setText("");
        }
    }

    private void addLiveStreamingEntry(LiveStreamingEntry entry) {
        var item = new InputLiveStreamView();
        items.add(item);
        display.getStreamsContainer().add(item);
        updateAddButtonStyle();

        // モデルの値をUIに反映
        item.getUrlField().setText(entry.getUrl());
        item.getUrlField().setLabel(entry.getService().getText());
        item.getDisplayNameField().setText(entry.getDisplayName());

        var urlField = item.getUrlField();

        // URL欄の値が変更されたらサービスを推測
        urlField.addValueChangeHandler(e -> {
            var url = e.getValue();
            validURL(item, url);
        });
        urlField.addPasteHandler(e -> {
            // ペースト
            var pasteUrl = e.getValue();
            validURL(item, pasteUrl);
        });

        urlField.addFocusHandler(e -> urlField.select());

        // 削除ボタンの処理を設定
        item.getRemoveButton().addClickHandler(e -> {
            // モデル・UIから削除
            item.removeFromParent();
            manager.getEntries().remove(entry);
            items.remove(item);
            updateAddButtonStyle();
        });
    }

    private void updateAddButtonStyle() {
        var addButton = display.getAddButton();
        addButton.setEnabled(manager.getEntries().size() < INPUT_LIMIT);
    }

}
