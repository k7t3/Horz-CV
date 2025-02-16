package io.github.k7t3.horzcv.client.view;

import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.ButtonType;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.FieldType;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import io.github.k7t3.horzcv.client.model.StreamingService;
import io.github.k7t3.horzcv.client.presenter.LiveStreamEditorPresenter;

/**
 * ストリーミングサービスの選択とURL入力を行うUI。
 */
public class InputLiveStreamView extends MaterialPanel implements LiveStreamEditorPresenter.InputDisplay {

    private final MaterialTextBox url = new MaterialTextBox();

    private final MaterialButton removeButton = new MaterialButton("", IconType.CLEAR, ButtonType.FLOATING);

    public InputLiveStreamView() {
        init();
    }

    private void init() {
        setClass("inputLiveStream");

        // エディタの設定
        url.setFieldType(FieldType.ALIGNED_LABEL);
        url.setLabel("Service");
        url.setValidateOnBlur(true);
        url.setAllowBlank(true);
        url.setFlexGrow(1);

        // 削除ボタンの設定
        removeButton.setTabIndex(-1); // タブでフォーカスされないようにする
        //removeButton.setType(ButtonType.FLOATING);
        removeButton.setType(ButtonType.FLAT);
        removeButton.setBackgroundColor(Color.TRANSPARENT);
        removeButton.setIconColor(Color.RED);
        removeButton.setCircle(true);

        add(url);
        add(removeButton);
    }

    @Override
    public void updateService(StreamingService service) {
        if (service == null) {
            url.setLabel("Service");
        } else {
            url.setLabel(service.getText());
        }
    }

    @Override
    public Widget getRoot() {
        return this;
    }

    @Override
    public MaterialTextBox getUrlField() {
        return url;
    }

    @Override
    public MaterialButton getRemoveButton() {
        return removeButton;
    }
}
