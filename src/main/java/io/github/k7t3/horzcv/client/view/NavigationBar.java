package io.github.k7t3.horzcv.client.view;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.History;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconPosition;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialNavBar;
import gwt.material.design.client.ui.MaterialNavBrand;
import gwt.material.design.client.ui.MaterialNavSection;

public class NavigationBar extends MaterialNavBar {

    public NavigationBar() {
        init();
    }

    private void init() {
        var brand = new MaterialNavBrand("Horz CV");
        brand.setFloat(Style.Float.LEFT);
        brand.setMarginLeft(45);
        brand.setCursor(Style.Cursor.POINTER);

        // ブランドをクリックするとホームに戻る
        brand.addClickHandler(e -> History.newItem("", true));

        var refresh = new MaterialLink(IconType.AUTORENEW);
        refresh.setIconPosition(IconPosition.LEFT);
        refresh.setText("Refresh");

        var icon = new MaterialIcon();
        icon.setCustomIconType("fa-github");
        icon.setIconColor(Color.BLACK);

        var hamburger = new MaterialLink(IconType.MORE_VERT);

        var right = new MaterialNavSection();
        right.setFloat(Style.Float.RIGHT);
        right.add(icon);
        right.add(refresh);
        right.add(hamburger);

        add(brand);
        add(right);
    }

}
