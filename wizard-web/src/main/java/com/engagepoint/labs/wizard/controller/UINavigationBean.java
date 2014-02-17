package com.engagepoint.labs.wizard.controller;

import com.engagepoint.labs.wizard.bean.WizardForm;
import java.io.Serializable;
import java.util.List;
import com.engagepoint.labs.wizard.bean.WizardPage;
import com.engagepoint.labs.wizard.bean.WizardTopic;
import com.engagepoint.labs.wizard.model.NavigationData;
import com.engagepoint.labs.wizard.ui.UIComponentGenerator;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.panel.Panel;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.panel.Panel;

import com.engagepoint.labs.wizard.bean.WizardPage;
import com.engagepoint.labs.wizard.model.NavigationData;
import com.engagepoint.labs.wizard.style.WizardComponentStyles;

@Named("uiNavigationBean")
@RequestScoped
public class UINavigationBean implements Serializable {

    private static final long serialVersionUID = 7470581070941487130L;
    /**
     * This is model class to hold data about XML files and Navigation data
     * objects
     */
    @Inject
    private NavigationData navigationData;
    /**
     * contains all of the per-request state information related to the
     * processing of a single JavaServer Faces request, and the rendering of the
     * corresponding response.
     */
    private FacesContext facesContext;
    /**
     * Contains all info about EL evaluation
     */
    private ELContext elContext;
    /**
     * Used to create EL expressions
     */
    private ExpressionFactory expressionFactory;

    /**
     * Method clears current topic IDs and current topic titles It is needed
     * when user sets next page on breadcrumb
     */
    public void clearCurrentTopicsData() {
	navigationData.getCurrentTopicIDs().clear();
	navigationData.getCurrentTopicTitles().clear();
    }

    /**
     * Method used to create wizard forms, then one of forms must be selected
     * and start() method will called to navigate on wizard-index page
     */
    @PostConstruct
    public void init() {
	if (navigationData.isOnSelectXMLPage()) {
	    navigationData.setOnSelectXMLPage(false);
	    navigationData.startSelectXMLScreen();
	}
    }

    /**
     * At first this method configure navigationData (set pageId to first, set
     * topicId to first etc.) then calls init breadcrumb and init menu set
     * needRefresh to false and redirect user to page wizard-index
     * 
     *
     * @return wizard index page name
     */
    public String start() {
	navigationData.startWizard();
	initBreadcrumb();
	initMenu();
	// Refresh Processed in UINavigationPhaseListener.
	// Now we set refresh flag to false, because of we need to be redirected
	// to bootstrapindex page and see our wizard
	navigationData.setNeedRefresh(false);
	return "wizard-index";
    }

    /**
     * Method used for dynamic initialization breadcrumb component for template
     * form
     */
    private void initBreadcrumb() {
	// get facesContext for current response
	facesContext = FacesContext.getCurrentInstance();
	// get elContext (super container for EL expressions)
	elContext = facesContext.getELContext();
	// get expression factory for creating EL expressions in following cycle
	expressionFactory = facesContext.getApplication().getExpressionFactory();
	// Iterating over all pages in model
	for (int i = 0; i < getPageCount(); i++) {
	    // creating menu item
	    MenuItem item = new MenuItem();
	    MethodExpression elExpression;
	    WizardPage wizardPage;
	    // get page from navigationData by index
	    wizardPage = navigationData.getWizardForm().getWizardPageList().get(i);
	    // set titles for breadcrumb items
	    item.setValue("Page " + wizardPage.getPageNumber().toString());
	    // creating EL expressions for all items in breadcrumb
	    elExpression = expressionFactory.createMethodExpression(elContext,
		    "#{uiNavigationBean.changeCurrentPage(\"" + wizardPage.getId()
			    + "\")}", void.class, new Class[] { String.class });
	    // set elExpression on item action attribute
	    item.setActionExpression(elExpression);
	    navigationData.getBreadcrumb_model().addMenuItem(item);
	}

    }

    /**
     * This method is used to insert values to our left menu. Values are
     * extracted from currentTopicIDs list. If we know topic's ID, we can select
     * topic's title. Method will be called every time when user change page on
     * breadcrumb
     */
    private void initMenu() {
	// clearing current topics id's. It needed for navigation.

	navigationData.getCurrentTopicIDs().clear();
	// now we start create new topics for page
	for (int i = 0; i < getTopicCount(navigationData.getCurrentPageID()); i++) {
	    // retrieve topicID from navigation data
	    String topicID = navigationData.getWizardForm()
		    .getWizardPageById(navigationData.getCurrentPageID()).getTopicList()
		    .get(i).getId();
	    // add topicID to topics ID's list
	    navigationData.getCurrentTopicIDs().add(topicID);
	    // get topics title by id
	    String topic_title = navigationData.getWizardForm()
		    .getWizardTopicById(topicID).getGroupTitle();
	    // add title to topic titles
	    navigationData.getCurrentTopicTitles().add(topic_title);
	}
	// after initialization menu questions creator method called
	createQuestions();
    }

    /**
     * Create questions, method must be called for every navigation case
     */
    public void createQuestions() {
        UIComponentGenerator generator = new UIComponentGenerator();
        WizardForm wizardForm = navigationData.getWizardForm();
        WizardTopic wizardTopic = wizardForm.getWizardTopicById(navigationData.getCurrentTopicID());
        List<Panel> panelList = generator.getPanelList(wizardTopic.getWizardQuestionList());
        navigationData.setPanelList(panelList);
        navigationData.getPanelGrid().getChildren().clear();
        for (Panel panel : navigationData.getPanelList()) {
            navigationData.getPanelGrid().getChildren().add(panel);
        }
        getNavigationData().setNeedRefresh(true);
    }

    private int getPageCount() {

	return navigationData.getWizardForm().getWizardPageList().size();
    }

    private int getTopicCount(String pageID) {

	return navigationData.getWizardForm().getWizardPageById(pageID).getTopicList()
		.size();
    }

    /**
     * Method called every time for changing current page.
     * 
     * @param currentPageID
     */
    public void changeCurrentPage(String currentPageID) {
	clearCurrentTopicsData();
	// Refreshing current style to BootStrap defaults
	changeStyleOfCurrentPageButton(WizardComponentStyles.STYLE_PAGE_BUTTON_DEFAULT);
	navigationData.setCurrentPageIDAndTitle(currentPageID);
	// After changing current page to a new one, mark it with a new style
	changeStyleOfCurrentPageButton(WizardComponentStyles.STYLE_PAGE_BUTTON_SELECTED);
	// set current topic to first on new page
	navigationData.setCurrentTopicIDAndTitle(navigationData.getWizardForm()
		.getWizardPageById(navigationData.getCurrentPageID()).getTopicList()
		.get(0).getId());
	// create new menu for page
	initMenu();
    }

    /**
     * change topic by id
     * 
     * @param currentTopicID
     */
    public void changeCurrentTopic(String currentTopicID) {
	navigationData.setCurrentTopicIDAndTitle(currentTopicID);
	createQuestions();
    }

    public NavigationData getNavigationData() {
	return navigationData;
    }

    public void setNavigationData(NavigationData navigationData) {
	this.navigationData = navigationData;
    }

    /**
     * Method used as action attribute for NEXT button
     */
    public void nextButtonClick() {
	System.out.println("Inside method");
	// in if condition we try to change current topic id

	if (navigationData.setCurrentTopicIDtoNext()) {
	    // if topic id was changed successfully
	    changeCurrentTopic(navigationData.getCurrentTopicID());
	    // if topic id was last id on page we go to "else-if" and try to
	    // change page id
	} else if (navigationData.setCurrentPageIDtoNext()) {
	    // if page id was changed successfully
	    changeCurrentPage(navigationData.getCurrentPageID());
	} else {
	    // if current topic was last on last page we will be here
	    // todo submit, validation, and confirmation calls actions here

	}

    }

    /**
     * Method changes CSS Style of current selected PageButton from breadcrumb
     * 
     * @param styleClass
     *            style class from CSS file
     */
    private void changeStyleOfCurrentPageButton(String styleClass) {
	List<WizardPage> pageList = navigationData.getWizardForm().getWizardPageList();
	int currentPageButtonIndex = 0;

	// here we can find position number of page button from breadcrumb
	// (numbers are from 0 to n). Page buttons on breadcrumb have same
	// order, as in wizard page list from our XML
	for (int i = 0; i < pageList.size(); i++) {
	    WizardPage page = pageList.get(i);
	    if (page.getId().equals(navigationData.getCurrentPageID())) {
		currentPageButtonIndex = i;
		break;
	    }
	}

	MenuItem currentPageButtonMenuItem;
	MenuItem pageOneButtonMenuItem;

	// Get our menuItem from breadcrumb using our index we've found
	currentPageButtonMenuItem = (MenuItem) navigationData.getBreadcrumb_model()
		.getContents().get(currentPageButtonIndex);
	pageOneButtonMenuItem = (MenuItem) navigationData.getBreadcrumb_model()
		.getContents().get(0);
	// Setting style to current page button menuItem
	if (currentPageButtonIndex == 0) {
	    // On page one we can't set style class, styles only set to id
	    // #brd-j_id1
	    // NOTE: brd is an ID of a parent object - breadcrumb,
	    // those it added automatically, so in next step we print only j_id1
	    // If we have changed page one's ID before, return it to default
	    pageOneButtonMenuItem.setId("j_id1");
	} else {
	    // Setting our class style
	    currentPageButtonMenuItem.setStyleClass(styleClass);
	    // and changing our page one generated id to another one
	    pageOneButtonMenuItem.setId(pageOneButtonMenuItem.getId() + "a");
	}
    }

}
