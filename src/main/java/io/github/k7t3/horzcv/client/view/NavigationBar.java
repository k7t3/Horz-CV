package io.github.k7t3.horzcv.client.view;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.History;
import gwt.material.design.client.constants.ButtonType;
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
        setId("nav-bar");

        var brand = new MaterialNavBrand("Horz CV");
        brand.setCursor(Style.Cursor.POINTER);
        brand.setFlexGrow(0);

        // ブランドをクリックするとホームに戻る
        brand.addClickHandler(e -> History.newItem("", true));



        var icon = new MaterialIcon();
        // 2xサイズのGitHubアイコン
        icon.setCustomIconType("fa-brands fa-github fa-2x");

        var link = new MaterialLink(ButtonType.FLAT, "", icon);
        link.setHref("https://www.github.com/k7t3/");
        link.setTarget("_blank");
        link.getElement().setAttribute("rel", "noopener");
        link.setFlexGrow(0);

//        var right = new MaterialNavSection();
//        right.setFloat(Style.Float.RIGHT);
//        right.add(link);

        var spacer = new MaterialNavSection();
        spacer.setFlexGrow(1);

        add(brand);
        add(spacer);
        add(link);
    }

}
