package com.engagepoint.labs.wizard.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
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
import com.engagepoint.labs.wizard.bean.WizardPage;
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

    public void clear() {
	navigationData.setCurrentTopicIDs(new ArrayList<String>());
	navigationData.setCurrentTopicTitles(new ArrayList<String>());
    }

    @PostConstruct
    public void init() {

	if (navigationData.isOnSelectXMLPage()) {

	    navigationData.setOnSelectXMLPage(false);
	    navigationData.startSelectXMLScreen();
	}
    }

    public String start() {

	navigationData.startWizard();

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

	    WizardPage page;
	    page = navigationData.getWizardForm().getWizardPageList().get(i);

	    item.setValue("Page " + page.getPageNumber().toString());

	    // Generating
	    // action="#{uiNavigationBean.changeCurrentPage(pageID)}" for
	    // each menuItem
	    expr = expFact.createMethodExpression(elCtx, "#{uiNavigationBean.changeCurrentPage(\"" + page.getId()
		    + "\")}", void.class, new Class[] { String.class });

	    item.setActionExpression(expr);

	    navigationData.getBreadcrumb_model().addMenuItem(item);
	}

    }

    private void initMenu() {

	navigationData.getCurrentTopicIDs().clear();

	for (int i = 0; i < getTopicCount(navigationData.getCurrentPageID()); i++) {

	    // topicID will be represented as uib:menuItem id="#{topicID}" (see
	    // leftMenu.xhtml).
	    // Each of topicID's are put to curretTopicIDs list.
	    // Each Topic title extracted from pagelist.topic list by arraylist
	    // index

	    String topicID = navigationData.getWizardForm().getWizardPageById(navigationData.getCurrentPageID())
		    .getTopicList().get(i).getId();

	    navigationData.getCurrentTopicIDs().add(topicID);

	    String topic_title = navigationData.getWizardForm().getWizardPageById(navigationData.getCurrentPageID())
		    .getTopicList().get(i).getGroupTitle();

	    navigationData.getCurrentTopicTitles().add(topic_title);
	}

	createQuestions();
    }

    private void createQuestions() {

	getNavigationData().getCurrentUIquestions().clear();

	// UIBasicQuestion q1 = new
	// UITextQuestion((getNavigationData().getCurrentPageID()) + " - "
	// + (getNavigationData().getCurrTopic() + 1));
	//
	// getNavigationData().getCurrentUIquestions().add(q1);

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
	return navigationData.getWizardForm().getWizardPageList().size();
    }

    private int getTopicCount(String pageID) {
	System.out.println("Get topic counr for PID: " + pageID);
	System.out.println("Topics: " + navigationData.getWizardForm().getWizardPageById(pageID).getTopicList().size());
	return navigationData.getWizardForm().getWizardPageById(pageID).getTopicList().size();
    }

    // This method called by action="" attribute from page. Page numbers must be
    // in range from 1 to n
    public void changeCurrentPage(String currentPage) {
	System.out.println("P: Curr page set to: " + currentPage);
	System.out.println("P: Curr group set to: " + navigationData.getCurrentTopicID());
	clear();
	getNavigationData().setCurrentPageID(currentPage);
	navigationData.setCurrentTopicID(navigationData.getWizardForm()
		.getWizardPageById(navigationData.getCurrentPageID()).getTopicList().get(0).getId());
	initMenu();
    }

    // The same
    public void changeCurrentTopic(String currTopicID) {
	String currentPageID = navigationData.getCurrentPageID();
	System.out.println("T: Curr page set to: " + currentPageID);
	System.out.println("T: Curr group set to: " + currTopicID);
	clear();
	getNavigationData().setCurrentPageID(currentPageID);
	navigationData.setCurrentTopicID(navigationData.getWizardForm()
		.getWizardPageById(navigationData.getCurrentPageID()).getTopicList().get(0).getId());
	createQuestions();
    }

    public NavigationData getNavigationData() {
	return navigationData;
    }

    public void setNavigationData(NavigationData navigationData) {
	this.navigationData = navigationData;
    }

}
