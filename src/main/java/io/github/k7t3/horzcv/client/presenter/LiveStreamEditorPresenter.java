package io.github.k7t3.horzcv.client.presenter;

import com.google.gwt.user.client.ui.FormPanel;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
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
        MaterialTextBox getDescriptionField();
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

    private final Display display;

    private final List<Entry> items = new ArrayList<>();

    public LiveStreamEditorPresenter(LiveStreamingManager manager) {
        this.manager = manager;
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

        // ビューに反映
        var container = display.getStreamsContainer();
        container.clear();
        manager.getEntries().forEach(this::addLiveStreamingEntry);

        // 最低4つの入力欄を確保
        for (var i = manager.getEntries().size(); i < 4; i++) {
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
            var desc = item.getDescriptionField().getText();

            model.setUrl(url);
            model.setService(foundService);
            model.setDescription(desc);
        }
    }

    private void validURL(InputLiveStreamView item, String url) {
        var foundService = manager.findSuitableService(url);

        // ビューに反映
        item.updateService(foundService);
    }

    private void addLiveStreamingEntry(LiveStreamingEntry entry) {
        var item = new InputLiveStreamView();
        items.add(item);
        display.getStreamsContainer().add(item);
        updateAddButtonStyle();

        // モデルの値をUIに反映
        item.getUrlField().setText(entry.getUrl());
        item.getUrlField().setLabel(entry.getService().getText());

        // URL欄の値が変更されたらサービスを推測
        item.getUrlField().addValueChangeHandler(e -> {
            var url = e.getValue();
            validURL(item, url);
        });
        item.getUrlField().addPasteHandler(e -> {
            // ペースト
            var pasteUrl = e.getValue();
            validURL(item, pasteUrl);
        });

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
