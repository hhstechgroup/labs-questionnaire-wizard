package com.engagepoint.labs.wizard.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBException;

import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;
import org.xml.sax.SAXException;

import com.engagepoint.labs.wizard.bean.WizardDocument;
import com.engagepoint.labs.wizard.bean.WizardForm;
import com.engagepoint.labs.wizard.jsfbean.ManagedBean;
import com.engagepoint.labs.wizard.model.UITemplateModelForController;
import com.engagepoint.labs.wizard.ui.UIBasicQuestion;
import com.engagepoint.labs.wizard.ui.UITextQuestion;
import com.engagepoint.labs.wizard.xml.controllers.XmlController;

@Named("uiTemplateController")
@RequestScoped
public class UITemplateController implements Serializable {

    @Inject
    private UITemplateModelForController templateModel;

    private static final long serialVersionUID = 7470581070941487130L;

    // BreadCrumb
    private MenuModel breadcrumb_model;

    // Questions
    private HtmlForm content;

    // FacesData
    private FacesContext facesCtx;
    private ELContext elCtx;
    private ExpressionFactory expFact;

    @Inject
    private WizardForm wizardForm;
    
    @PostConstruct
    
    public void init(){

	templateModel.setMapOfWizardForms(new LinkedHashMap<String, String>());
	// MapOfWizardForms = new LinkedHashMap<>();
	templateModel.setXMLpathList(new ArrayList<String>());
	// XMLpathList = new ArrayList<>();
	templateModel.getXMLpathList().add("/XMLforWizard.xml");
	// XMLpathList.add("/XMLforWizard.xml");
	templateModel.getXMLpathList().add("/XMLforWizard2.xml");
	// XMLpathList.add("/XMLforWizard2.xml");
	templateModel.setXmlController(new XmlController());
	// xmlController = new XmlController();
	try {
	    templateModel.setWizardDocument(templateModel.getXmlController()
		    .readAllDeafultXmlFiles(templateModel.getXMLpathList()));
	    // wizardDocument =
	    // xmlController.readAllDeafultXmlFiles(XMLpathList);
	} catch (SAXException | JAXBException ex) {
	    Logger.getLogger(ManagedBean.class.getName()).log(Level.SEVERE, null, ex);
	}

	for (WizardForm wForm : templateModel.getWizardDocument().getFormList()) {
	    templateModel.getMapOfWizardForms().put(wForm.getFormName(), wForm.getId());
	}
	// for (WizardForm wForm : wizardDocument.getFormList()) {
	// MapOfWizardForms.put(wForm.getFormName(), wForm.getId());
	// }

	setBreadcrumb_model(new DefaultMenuModel());

	content = new HtmlForm();

	// Insert data to menus

    }

    public String start() {
	
	init();

	int currentWizardFormID = templateModel.getWizardDocument().getWizardFormByID(
		templateModel.getSelectedFormTemplate(), wizardForm,
		templateModel.getWizardDocument().getFormList());

	templateModel.setCurrentWizardFormID(currentWizardFormID);

	// int currentWizardFormID =
	// wizardDocument.getWizardFormByID(selectedFormTemplate,
	// wizardForm, wizardDocument.getFormList());
	//
	// wizardForm = wizardDocument.getFormList().get(
	// templateModel.getCurrentWizardFormID());
	//
	// templateModel.setCurrentWizardFormID(currentWizardFormID);
	initBreadcrumb();
	initMenu();
	templateModel.setNeedRefresh(false);
	return "bootstrapindex";
    }

    private void initBreadcrumb() {

	facesCtx = FacesContext.getCurrentInstance();
	elCtx = facesCtx.getELContext();
	expFact = facesCtx.getApplication().getExpressionFactory();

	System.out.println("Init breadcrumb");
	for (int i = 0; i < getPageCount(); i++) {
	    MenuItem item = new MenuItem();
	    MethodExpression expr;

	    item.setValue(wizardForm.getWizardPageList().get(i).getId());

	    int pageNumber = i + 1;
	    expr = expFact.createMethodExpression(elCtx,
		    "#{uiTemplateController.changeCurrentPage(" + pageNumber + ")}",
		    void.class, new Class[] { int.class });

	    item.setActionExpression(expr);
	    getBreadcrumb_model().addMenuItem(item);
	}

	// breadcrumb.setModel(getBreadcrumb_model());
    }

    private void initMenu() {

	templateModel.getCurrentTopicIDs().clear();

	for (int i = 0; i < getTopicCount(getTemplateModel().getCurrentPage()); i++) {
	    String topic_id = "Topic" + (i + 1);
	    templateModel.getCurrentTopicIDs().add(topic_id);

	    String topic_title = wizardForm.getWizardPageList()
		    .get(templateModel.getCurrentPage() - 1).getTopicList().get(i)
		    .getGroupTitle();
	    templateModel.getCurrentTopicTitles().add(topic_title);
	}

	createQuestions();
    }

    private void createQuestions() {

	getTemplateModel().getCurrentUIquestions().clear();

	UIBasicQuestion q1 = new UITextQuestion((getTemplateModel().getCurrentPage())
		+ " - " + (getTemplateModel().getCurrTopic() + 1));

	getTemplateModel().getCurrentUIquestions().add(q1);

	// TODO: get model data here and convert to UIComponents

	createUIquestions();
    }

    private void createUIquestions() {

	facesCtx = FacesContext.getCurrentInstance();
	elCtx = facesCtx.getELContext();
	expFact = facesCtx.getApplication().getExpressionFactory();

	content.getChildren().clear();
	for (int i = 0; i < getTemplateModel().getCurrentUIquestions().size(); i++) {
	    getTemplateModel().getCurrentUIquestions().get(i).postInit();
	    content.getChildren().add(
		    getTemplateModel().getCurrentUIquestions().get(i).getUiComponent());
	    HtmlOutputText linebreak = new HtmlOutputText();
	    linebreak.setValue("<br/>");
	    linebreak.setEscape(false);
	    content.getChildren().add(linebreak);
	}

	getTemplateModel().setNeedRefresh(true);
    }

    private int getPageCount() {
	return wizardForm.getWizardPageList().size();
    }

    private int getTopicCount(int p_id) {
	return wizardForm.getWizardPageList().get(p_id - 1).getTopicList().size();
    }

    public MenuModel getBreadcrumb_model() {
	return breadcrumb_model;
    }

    public void setBreadcrumb_model(MenuModel breadcrumb_model) {
	this.breadcrumb_model = breadcrumb_model;
    }

    public void navigate(String p_id) {
	System.out.println(p_id);
    }

    public void changeCurrentPage(int currPage) {
	System.out.println("Curr page set to: " + currPage);
	System.out.println("Curr group set to: " + getTemplateModel().getCurrTopic());
	getTemplateModel().setCurrPage(currPage);
	getTemplateModel().setCurrTopic(1);
	initMenu();
    }

    public void changeCurrentTopic(String currTopic) {
	System.out.println("Curr page set to: " + getTemplateModel().getCurrentPage());
	System.out.println("Curr group set to: " + currTopic);
	getTemplateModel().setCurrTopic(templateModel.getID(currTopic));
	createQuestions();
    }

    public HtmlForm getcontent() {
	return content;
    }

    public void setcontent(HtmlForm content) {
	this.content = content;
    }

    public UITemplateModelForController getTemplateModel() {
	return templateModel;
    }

    public void setTemplateModel(UITemplateModelForController templateModel) {
	this.templateModel = templateModel;
    }
    
    public Map<String, String> getXmlsValues() {
	return templateModel.getMapOfWizardForms();
    }

//    public Map<String, String> getXmlsValues() {
//	return MapOfWizardForms;
//    }

}
