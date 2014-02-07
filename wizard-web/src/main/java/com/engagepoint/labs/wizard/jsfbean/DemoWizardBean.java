package com.engagepoint.labs.wizard.jsfbean;

import com.engagepoint.component.menu.UIMenuItem;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.model.MenuModel;

import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * Created by igor.guzenko on 2/7/14.
 *
 **/
@javax.faces.bean.ManagedBean(name = "wizardBean")
@SessionScoped
public class DemoWizardBean {
    @Inject
    private MenuModel breadCrumbModel;
    @Inject
    private com.engagepoint.component.menu.model.MenuModel topicsModel;


    private ArrayList<MockPage> pagesList;
    private FacesContext facesContext;



    @PostConstruct
    public void init(){
        MenuItem startMenuItem = new MenuItem();
        startMenuItem.setValue("StartPage");
        startMenuItem.setUrl("/bootstrapindex.xhtml");
        startMenuItem.setId("startPage");
        startMenuItem.setOnclick("p1");
        breadCrumbModel.addMenuItem(startMenuItem);
        fillPagesList();
        addBreadcrumpItems();
    }

    public com.engagepoint.component.menu.model.MenuModel getTopicsModel() {
        addTopicsItems();
        return topicsModel;
    }

    public MenuModel getBreadCrumbModel() {

        return breadCrumbModel;
    }

    private void addBreadcrumpItems(){
        MenuItem menuItem;
        for(MockPage page:pagesList){
            menuItem = new MenuItem();
            menuItem.setValue(page.getPageName());
            menuItem.setUrl("/"+page.getPageName());
            menuItem.setId(page.getPageName());
            breadCrumbModel.addMenuItem(menuItem);
        }
    }

    private void fillPagesList(){
        pagesList = new ArrayList<>(5);
        pagesList.add(new MockPage("Page 1"));
        pagesList.add(new MockPage("Page 2"));
        pagesList.add(new MockPage("Page 3"));
        pagesList.add(new MockPage("Page 4"));
        pagesList.add(new MockPage("Page 5"));
    }

    private void addTopicsItems(){
             facesContext = FacesContext.getCurrentInstance();
             String uri = ((HttpServletRequest) facesContext.getExternalContext().getRequest()).getRequestURI();
             MockPage currentPage = null;
            if(getPageByUri(uri)!=null)
             currentPage =getPageByUri(uri);
             for(String topic:currentPage.getTopicPages()){
                 UIMenuItem item = new UIMenuItem();
                 item.setId(topic);
                 item.setValue("Item1");
                 item.setUrl("#");
                 item.setTitle(topic);
                 topicsModel.addMenuItem(item);
             }
    }

    private MockPage getPageByUri(String uri){
        String[] splitted = uri.split("/");
        String pageName = splitted[splitted.length-1];
        for(MockPage page:pagesList){
            if(page.getPageName().equals(pageName)){
                return page;
            }
        }
        return null;
    }



}
