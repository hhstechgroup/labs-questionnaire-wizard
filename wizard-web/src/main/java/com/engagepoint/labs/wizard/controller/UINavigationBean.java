package com.engagepoint.labs.wizard.controller;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.component.menuitem.MenuItem;

import com.engagepoint.labs.wizard.bean.WizardPage;
import com.engagepoint.labs.wizard.model.NavigationData;

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

    public void clearCurrentTopicsData() {
	
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

    /**
     * A BIG method (magic here! :D). Method is used for pushing elements
     * (menuitems) to breadcrumb. Each element (menuitem) must have
     * action="#{someBean.someMethod(something)}" attribute. In our case
     * 'something' is ID of each page. Please see docs of FacesContext and
     * ELContext
     */
    private void initBreadcrumb() {

	facesCtx = FacesContext.getCurrentInstance();
	elCtx = facesCtx.getELContext();
	expFact = facesCtx.getApplication().getExpressionFactory();

	//Iterating over all pages from model
	for (int i = 0; i < getPageCount(); i++) {

	    MenuItem item = new MenuItem();
	    MethodExpression expr;

	    WizardPage page;
	    page = navigationData.getWizardForm().getWizardPageList().get(i);

	    item.setValue("Page " + page.getPageNumber().toString());

	    expr = expFact.createMethodExpression(elCtx, "#{uiNavigationBean.changeCurrentPage(\"" + page.getId()
		    + "\")}", void.class, new Class[] { String.class });

	    item.setActionExpression(expr);

	    navigationData.getBreadcrumb_model().addMenuItem(item);
	}

    }

    /**
     * This method is used to insert values to our left menu. Values are
     * extracted from currentTopicIDs list. If we know topic's ID, we can select
     * topic's title
     */
    private void initMenu() {

	navigationData.getCurrentTopicIDs().clear();

	for (int i = 0; i < getTopicCount(navigationData.getCurrentPageID()); i++) {

	    String topicID = navigationData.getWizardForm().getWizardPageById(navigationData.getCurrentPageID())
		    .getTopicList().get(i).getId();

	    navigationData.getCurrentTopicIDs().add(topicID);

	    String topic_title = navigationData.getWizardForm().getWizardTopicById(topicID).getGroupTitle();

	    navigationData.getCurrentTopicTitles().add(topic_title);
	}

	createQuestions();
    }

    /**
     * Method that can insert many different questions to UI form. Stub here.
     */
    private void createQuestions() {

	navigationData.getMainContentForm().getChildren().clear();

	navigationData.setCurrentOutputText(new HtmlOutputText());

	navigationData.getCurrentOutputText().setValue(
		"Page " + navigationData.getCurrentPageTitle() + " - " + navigationData.getCurrentTopicTitle());

	navigationData.getMainContentForm().getChildren().add(navigationData.getCurrentOutputText());
	getNavigationData().setNeedRefresh(true);
    }

    private int getPageCount() {
	
	return navigationData.getWizardForm().getWizardPageList().size();
    }

    private int getTopicCount(String pageID) {
	
	return navigationData.getWizardForm().getWizardPageById(pageID).getTopicList().size();
    }


    public void changeCurrentPage(String currentPage) {
	
	clearCurrentTopicsData();
	
	navigationData.setCurrentPageID(currentPage);

	navigationData.setCurrentTopicID(navigationData.getWizardForm()
		.getWizardPageById(navigationData.getCurrentPageID()).getTopicList().get(0).getId());

	initMenu();
    }

    // The same
    public void changeCurrentTopic(String currentTopicID) {
	
	navigationData.setCurrentTopicID(currentTopicID);
	
	createQuestions();
    }

    public NavigationData getNavigationData() {
	
	return navigationData;
    }

    public void setNavigationData(NavigationData navigationData) {
	
	this.navigationData = navigationData;
    }

}
