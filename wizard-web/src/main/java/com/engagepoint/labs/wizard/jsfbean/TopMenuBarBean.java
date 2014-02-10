package com.engagepoint.labs.wizard.jsfbean;

import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

@ManagedBean(name = "topMenuBar")
@RequestScoped
public class TopMenuBarBean {

    private MenuModel model;
    private UIViewRoot viewRoot;

    @PostConstruct
    public void initModel() {
        model = new DefaultMenuModel();
        viewRoot = FacesContext.getCurrentInstance().getViewRoot();
        String viewId = viewRoot.getViewId();

        MenuItem menuItem = new MenuItem();
        menuItem.setId("bootstrapItem");
        if (viewId.startsWith("/bootstrap")) {
            menuItem.setStyleClass("active");
        }
        menuItem.setValue("Bootstrap");
        menuItem.setUrl("/bootstrap/button/index.xhtml");
        model.addMenuItem(menuItem);

        menuItem = new MenuItem();
        menuItem.setId("demoItem");
        if (viewId.startsWith("/demo")) {
            menuItem.setStyleClass("active");
        }
        menuItem.setValue("Demo");
        menuItem.setUrl("/templates/default.xhtml");
        model.addMenuItem(menuItem);

        menuItem = new MenuItem();
        menuItem.setId("toolsItem");
        if (viewId.startsWith("/tools")) {
            menuItem.setStyleClass("active");
        }
        menuItem.setValue("Tools");
        menuItem.setUrl("/tools/grid/index.xhtml");
        model.addMenuItem(menuItem);
    }

    public MenuModel getModel() {
        return model;
    }

}
