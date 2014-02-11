
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engagepoint.labs.wizard.jsfbean;

import com.engagepoint.component.menu.UIMenuItem;
import com.engagepoint.component.menu.model.DefaultMenuModel;
import com.engagepoint.component.menu.model.MenuModel;
import com.engagepoint.labs.wizard.bean.WizardDocument;
import com.engagepoint.labs.wizard.bean.WizardForm;
import com.engagepoint.labs.wizard.bean.WizardPage;
import com.engagepoint.labs.wizard.bean.WizardTopic;
import com.engagepoint.labs.wizard.xml.controllers.XmlController;
import org.primefaces.component.menuitem.MenuItem;
import org.xml.sax.SAXException;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.faces.context.FacesContext.*;

/**
 * @author artem.pylypenko
 */
@Named(value = "managedBean")
@RequestScoped
public class ManagedBean {
    private MenuModel menuModel;
    private String topicTitle;
    private MenuModel breadcrumbmodel;
    private org.primefaces.model.MenuModel pageModel;


    private WizardDocument wizardDocument;
    XmlController xmlController;
    @Inject
    private WizardForm wizardForm;
    private String selectedFormTemplate;
    // only for our default xml files!
    private List<String> XMLpathList;
    private Map<String, String> MapOfWizardForms;

    {
        MapOfWizardForms = new LinkedHashMap<>();
        XMLpathList = new ArrayList<>();
        XMLpathList.add("/XMLforWizard.xml");
        XMLpathList.add("/XMLforWizard2.xml");
        xmlController = new XmlController();
        try {
            wizardDocument = xmlController.readAllDeafultXmlFiles(XMLpathList);
        } catch (SAXException | JAXBException ex) {
            Logger.getLogger(ManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (WizardForm wForm : wizardDocument.getFormList()) {
            MapOfWizardForms.put(wForm.getFormName(), wForm.getId());
        }
    }

    /**
     * Creates a new instance of ManagedBean
     */
    public ManagedBean() {
    }

    public Map<String, String> getXmlsValues() {
        return MapOfWizardForms;
    }

    public String getSelectedFormTemplate() {
        return selectedFormTemplate;
    }

    public void setSelectedFormTemplate(String selectedFormTemplate) {
        this.selectedFormTemplate = selectedFormTemplate;
    }

    public String start() {
        wizardDocument.getWizardFormByID(selectedFormTemplate, wizardForm, wizardDocument.getFormList());
        initFirstPage(wizardForm);
        initBreadcrumb(wizardForm);
        return "bootstrapindex";
    }

    public MenuModel getMenuModel() {
        return menuModel;
    }

    public void setMenuModel(MenuModel menuModel) {
        this.menuModel = menuModel;

    }

    public org.primefaces.model.MenuModel getPageModel() {
        return pageModel;
    }

    public void setPageModel(MenuModel pageModel) {
        this.pageModel = (org.primefaces.model.MenuModel) pageModel;
    }

    public MenuModel getBreadcrumbmodel() {
        return breadcrumbmodel;
    }

    public void setBreadcrumbmodel(MenuModel breadcrumbmodel) {
        this.breadcrumbmodel = breadcrumbmodel;
    }

    public void initMenu(WizardForm wizardForm) {
        List<WizardPage> wizardPageList = wizardForm.getWizardPageList();
        WizardPage pageOne = wizardForm.getWizardPageList().get(0);
        menuModel = new DefaultMenuModel();
        for (WizardTopic wizardTopic : pageOne.getTopicList()) {
            topicTitle = wizardTopic.getGroupTitle();
            UIMenuItem uiMenuItem = new UIMenuItem();
            uiMenuItem.setTitle(topicTitle);
            menuModel.addMenuItem(uiMenuItem);
        }
    }


    public void initBreadcrumb(WizardForm wizardForm) {
        pageModel = new org.primefaces.model.DefaultMenuModel();
        List<WizardPage> wizardPageList = wizardForm.getWizardPageList();
        for (WizardPage wizardPage : wizardPageList) {
            int pageNumber = wizardPage.getPageNumber();
            MenuItem menuItem = new MenuItem();
            menuItem.setValue("Page " + pageNumber);
            menuItem.setUrl("#");
            menuItem.setOnclick("alert('sdfsdf');");
            menuItem.getOnclick();
           pageModel.addMenuItem(menuItem);

        }
    }

    public void initFirstPage(WizardForm wizardForm) {
        List<WizardPage> wizardPageList = wizardForm.getWizardPageList();
        WizardPage pageOne = wizardForm.getWizardPageList().get(0);
        menuModel = new DefaultMenuModel();
        for (WizardTopic wizardTopic : pageOne.getTopicList()) {
            topicTitle = wizardTopic.getGroupTitle();
            UIMenuItem uiMenuItem = new UIMenuItem();
            uiMenuItem.setTitle(topicTitle);
            menuModel.addMenuItem(uiMenuItem);
        }
    }

    public String arg(){
        String params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("productId");
        System.out.print("-------------------------------------"+params+"------------------------------------");
        return params;
    }
}
