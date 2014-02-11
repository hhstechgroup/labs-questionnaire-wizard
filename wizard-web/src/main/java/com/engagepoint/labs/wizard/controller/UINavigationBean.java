package com.engagepoint.labs.wizard.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBException;

import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.model.DefaultMenuModel;
import org.xml.sax.SAXException;

import com.engagepoint.labs.wizard.bean.WizardForm;
import com.engagepoint.labs.wizard.model.NavigationData;
import com.engagepoint.labs.wizard.ui.UIBasicQuestion;
import com.engagepoint.labs.wizard.ui.UITextQuestion;
import com.engagepoint.labs.wizard.xml.controllers.XmlController;

@Named("uiNavigationBean")
@RequestScoped
public class UINavigationBean implements Serializable {

    private static final long serialVersionUID = 7470581070941487130L;

    @Inject
    private NavigationData navigationData;

    // FacesData
    private FacesContext facesCtx;
    private ELContext elCtx;
    private ExpressionFactory expFact;

    @Inject
    private WizardForm wizardForm;

    public void init() {

	navigationData.setMapOfWizardForms(new LinkedHashMap<String, String>());
	// MapOfWizardForms = new LinkedHashMap<>();

	navigationData.setXMLpathList(new ArrayList<String>());
	// XMLpathList = new ArrayList<>();

	navigationData.getXMLpathList().add("/XMLforWizard.xml");
	// XMLpathList.add("/XMLforWizard.xml");

	navigationData.getXMLpathList().add("/XMLforWizard2.xml");
	// XMLpathList.add("/XMLforWizard2.xml");

	navigationData.setXmlController(new XmlController());
	// xmlController = new XmlController();
	
	navigationData.getMapOfWizardForms().clear();

	try {
	    navigationData.setWizardDocument(navigationData.getXmlController()
		    .readAllDeafultXmlFiles(navigationData.getXMLpathList()));
	    // wizardDocument =
	    // xmlController.readAllDeafultXmlFiles(XMLpathList);

	} catch (SAXException | JAXBException ex) {
	    Logger.getLogger(UINavigationBean.class.getName())
		    .log(Level.SEVERE, null, ex);
	}

	for (WizardForm wForm : navigationData.getWizardDocument().getFormList()) {
	    navigationData.getMapOfWizardForms().put(wForm.getFormName(), wForm.getId());
	}
	// for (WizardForm wForm : wizardDocument.getFormList()) {
	// MapOfWizardForms.put(wForm.getFormName(), wForm.getId());
	// }

	navigationData.setCurrentTopicIDs(new ArrayList<String>());
	navigationData.setCurrentTopicTitles(new ArrayList<String>());
	navigationData.setCurrPage(1);
	navigationData.setCurrTopic(1);
	
	navigationData.setBreadcrumb_model(new DefaultMenuModel());

    }

    public String start() {

	// Call this to parse XML files
	init();

	// ID of wizard form which was selected by user. This id represents
	// index in arrayList of wizardDocument.formList.
	int currentWizardFormID = navigationData.getWizardDocument().getWizardFormByID(
		navigationData.getSelectedFormTemplate(), wizardForm,
		navigationData.getWizardDocument().getFormList());

	navigationData.setCurrentFormID(currentWizardFormID);

	initBreadcrumb();
	initMenu();

	// Refresh Processed in UINavigationPhaseListener.
	// Now we set refresh flag to false, because of we need to be redirected
	// to bootstrapindex page and see our wizard
	navigationData.setNeedRefresh(false);

	return "bootstrapindex";
    }

    private void initBreadcrumb() {

	facesCtx = FacesContext.getCurrentInstance();
	elCtx = facesCtx.getELContext();
	expFact = facesCtx.getApplication().getExpressionFactory();

	for (int i = 0; i < getPageCount(); i++) {

	    MenuItem item = new MenuItem();
	    MethodExpression expr;

	    item.setValue("Page "+wizardForm.getWizardPageList().get(i).getPageNumber().toString());

	    int pageNumber = i + 1;

	    // Generating
	    // action="#{uiNavigationBean.changeCurrentPage(pageNumber)}" for
	    // each menuItem
	    expr = expFact.createMethodExpression(elCtx,
		    "#{uiNavigationBean.changeCurrentPage(" + pageNumber + ")}",
		    void.class, new Class[] { int.class });

	    item.setActionExpression(expr);

	    navigationData.getBreadcrumb_model().addMenuItem(item);
	}

    }

    private void initMenu() {

	navigationData.getCurrentTopicIDs().clear();

	for (int i = 0; i < getTopicCount(getNavigationData().getCurrentPage()); i++) {

	    //topicID will be represented as uib:menuItem id="#{topicID}" (see leftMenu.xhtml).
	    //Each of topicID's are put to curretTopicIDs list.
	    //Each Topic title extracted from pagelist.topic list by arraylist index
	    
	    String topicID = "Topic" + (i + 1);
	    navigationData.getCurrentTopicIDs().add(topicID);

	    String topic_title = wizardForm.getWizardPageList()
		    .get(navigationData.getCurrentPage() - 1).getTopicList().get(i)
		    .getGroupTitle();

	    navigationData.getCurrentTopicTitles().add(topic_title);
	}

	createQuestions();
    }

    private void createQuestions() {

	getNavigationData().getCurrentUIquestions().clear();

	UIBasicQuestion q1 = new UITextQuestion((getNavigationData().getCurrentPage())
		+ " - " + (getNavigationData().getCurrTopic() + 1));

	getNavigationData().getCurrentUIquestions().add(q1);

	// TODO: get model data here and convert to UIComponents

	createUIquestions();
    }

    private void createUIquestions() {

	// facesCtx = FacesContext.getCurrentInstance();
	// elCtx = facesCtx.getELContext();
	// expFact = facesCtx.getApplication().getExpressionFactory();
	//
	// content.getChildren().clear();
	// for (int i = 0; i <
	// getNavigationData().getCurrentUIquestions().size(); i++) {
	// getNavigationData().getCurrentUIquestions().get(i).postInit();
	// content.getChildren().add(
	// getNavigationData().getCurrentUIquestions().get(i).getUiComponent());
	// HtmlOutputText linebreak = new HtmlOutputText();
	// linebreak.setValue("<br/>");
	// linebreak.setEscape(false);
	// content.getChildren().add(linebreak);
	// }
	getNavigationData().setNeedRefresh(true);
    }

    private int getPageCount() {
	return wizardForm.getWizardPageList().size();
    }

    private int getTopicCount(int p_id) {
	System.out.println("Get topic counr for PID: "+p_id);
	System.out.println("Topics: "+wizardForm.getWizardPageList().get(p_id - 1).getTopicList().size());
	return wizardForm.getWizardPageList().get(p_id - 1).getTopicList().size();
    }

    //This method called by action="" attribute from page. Page numbers must be in range from 1 to n
    public void changeCurrentPage(int currPage) {
	System.out.println("P: Curr page set to: " + currPage);
	System.out.println("P: Curr group set to: " + getNavigationData().getCurrTopic());
	getNavigationData().setCurrPage(currPage);
	getNavigationData().setCurrTopic(1);
	initMenu();
    }

    //The same
    public void changeCurrentTopic(String currTopic) {
	System.out
		.println("T: Curr page set to: " + getNavigationData().getCurrentPage());
	System.out.println("T: Curr group set to: " + currTopic);
	getNavigationData().setCurrTopic(navigationData.getID(currTopic));
	createQuestions();
    }

    public NavigationData getNavigationData() {
	return navigationData;
    }

    public void setNavigationData(NavigationData navigationData) {
	this.navigationData = navigationData;
    }

    public Map<String, String> getXmlsValues() {
	return navigationData.getMapOfWizardForms();
    }

}
