package io.github.k7t3.horzcv.client.presenter;

import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import io.github.k7t3.horzcv.client.logic.LiveStreamEditor;
import io.github.k7t3.horzcv.client.model.InputLiveStream;
import io.github.k7t3.horzcv.client.model.LiveStream;
import io.github.k7t3.horzcv.client.model.StreamingService;
import io.github.k7t3.horzcv.client.view.InputLiveStreamView;
import io.github.k7t3.horzcv.client.view.LiveStreamEditorPage;
import io.github.k7t3.horzcv.client.view.Page;
import io.github.k7t3.horzcv.client.view.View;

import java.util.List;

public class LiveStreamEditorPresenter {

    private static final int INPUT_LIMIT = 6;

    /**
     * ストリーミングサービスの選択とURL入力を行う画面。
     */
    public interface InputDisplay extends View {
        void updateService(StreamingService service);
        MaterialTextBox getUrlField();
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

    private final LiveStreamEditor editor;

    private final Display display;

    public LiveStreamEditorPresenter(LiveStreamEditor editor) {
        this.editor = editor;
        display = new LiveStreamEditorPage();
        init();
    }

    private void init() {
        display.getAddButton().addClickHandler(e -> {
            var stream = new InputLiveStream();
            editor.add(stream);
            addInputLiveStream(stream);
            updateAddButtonStyle();
        });
    }

    public Display getDisplay() {
        return display;
    }

    public void setInputItems(List<LiveStream> streams) {
        // エディタに生の情報を設定
        editor.setAll(streams);

        // ビューに反映
        var container = display.getStreamsContainer();
        container.clear();
        editor.getInputItems().forEach(this::addInputLiveStream);

        // 最低4つの入力欄を確保
        for (var i = editor.getInputItems().size(); i < 4; i++) {
            var stream = new InputLiveStream();
            editor.add(stream);
            addInputLiveStream(stream);
        }
    }

    private void updateURL(InputLiveStreamView item, InputLiveStream model, String url) {
        var foundService = editor.findSuitableService(url);

        // モデルに反映する
        model.setService(foundService);
        model.setUrl(url);

        // ビューに反映
        if (foundService == null) {
            item.getUrlField().setErrorText("Invalid URL");
        } else {
            item.getUrlField().clearErrorText();
        }
        item.updateService(foundService);
    }

    private void addInputLiveStream(InputLiveStream stream) {
        var item = new InputLiveStreamView();
        display.getStreamsContainer().add(item);
        updateAddButtonStyle();

        // モデルの値をUIに反映
        item.getUrlField().setText(stream.getUrl());
        item.getUrlField().setLabel(stream.getService().getText());

        // URL欄の値が変更されたらサービスを推測
        item.getUrlField().addValueChangeHandler(e -> {
            var pasteUrl = e.getValue();
            updateURL(item, stream, pasteUrl);
        });
        item.getUrlField().addPasteHandler(e -> { // ペースト
            var pasteUrl = e.getValue();
            updateURL(item, stream, pasteUrl);
        });

        // 削除ボタンの処理を設定
        item.getRemoveButton().addClickHandler(e -> {
            // モデル・UIから削除
            item.removeFromParent();
            editor.removed(stream);
            updateAddButtonStyle();
        });
    }

    private void updateAddButtonStyle() {
        var addButton = display.getAddButton();
        addButton.setEnabled(editor.getInputItems().size() < INPUT_LIMIT);
    }

}
