package com.engagepoint.labs.wizard.controller;

import com.engagepoint.labs.wizard.bean.WizardPage;
import com.engagepoint.labs.wizard.model.NavigationData;
import org.primefaces.component.menuitem.MenuItem;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named("uiNavigationBean")
@RequestScoped
public class UINavigationBean implements Serializable {

    private static final long serialVersionUID = 7470581070941487130L;
    /**
     * This is model class to hold data about XML files and Navigation data objects
     */
    @Inject
    private NavigationData navigationData;
    /**
     * contains all of the per-request state information related to the processing
     * of a single JavaServer Faces request, and the rendering of the corresponding response.
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
     * Method clears current topic IDs and current topic titles
     * It is needed when user sets next page on breadcrumb
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
     * At first this method configure navigationData (set pageId to first, set topicId to first etc.)
     * then calls init breadcrumb and init menu
     * set needRefresh to false
     * and redirect user to page wizard-index
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
     * Method used for dynamic initialization breadcrumb component for template form
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
                    "#{uiNavigationBean.changeCurrentPage(\"" + wizardPage.getId() + "\")}",
                    void.class, new Class[]{String.class});
            // set elExpression on item action attribute
            item.setActionExpression(elExpression);
            navigationData.getBreadcrumb_model().addMenuItem(item);
        }

    }

    /**
     * This method is used to insert values to our left menu. Values are
     * extracted from currentTopicIDs list. If we know topic's ID, we can select
     * topic's title.
     * Method will be called every time when user change page on breadcrumb
     */
    private void initMenu() {
        //clearing current topics id's. It needed for navigation.
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
        //todo THIS METHOD MUST NOT BE CALLED HERE
        createQuestions();
    }

    /**
     * Create questions, method must be called for every navigation case
     */
    private void createQuestions() {
//        navigationData.getMainContentForm().getChildren().clear();
//        navigationData.setCurrentOutputText(new HtmlOutputText());
//        navigationData.getCurrentOutputText().setValue(
//                "Page " + navigationData.getCurrentPageTitle() + " - "
//                        + navigationData.getCurrentTopicTitle());
//        navigationData.getMainContentForm().getChildren()
//                .add(navigationData.getCurrentOutputText());
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
     * @param currentPageID
     */
    public void changeCurrentPage(String currentPageID) {
        clearCurrentTopicsData();
        navigationData.setCurrentPageID(currentPageID);
        //set current topic to first on new page
        navigationData.setCurrentTopicID(navigationData.getWizardForm()
                .getWizardPageById(navigationData.getCurrentPageID()).getTopicList()
                .get(0).getId());
        // create new menu for page
        initMenu();
    }

    /**
     * change topic by id
     * @param currentTopicID
     */
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

    /**
     * Method used as action attribute for NEXT button
     */
    public void nextButtonClick() {

        // in if condition we try to change current topic id
        if (navigationData.setCurrentTopicIDtoNext()) {
            // if topic id was changed successfully
            changeCurrentTopic(navigationData.getCurrentTopicID());
            // if topic id was last id on page we go to "else-if" and try to change page id
        } else if (navigationData.setCurrentPageIDtoNext()) {
            // if page id was changed successfully
            changeCurrentPage(navigationData.getCurrentPageID());
        } else {
            // if current topic was last on last page we will be here
            //todo submit, validation, and confirmation calls actions here

        }

    }


}
