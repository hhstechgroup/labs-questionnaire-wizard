package com.engagepoint.labs.wizard.controller;

import com.engagepoint.labs.wizard.bean.WizardForm;
import com.engagepoint.labs.wizard.bean.WizardPage;
import com.engagepoint.labs.wizard.bean.WizardTopic;
import com.engagepoint.labs.wizard.client.ClientConstantStrings;
import com.engagepoint.labs.wizard.handler.DataGridHandler;
import com.engagepoint.labs.wizard.model.NavigationData;
import com.engagepoint.labs.wizard.questions.RuleExecutor;
import com.engagepoint.labs.wizard.questions.WizardQuestion;
import com.engagepoint.labs.wizard.style.WizardComponentStyles;
import com.engagepoint.labs.wizard.ui.UIComponentGenerator;
import com.engagepoint.labs.wizard.ui.validators.QuestionAnswerValidator;
import com.engagepoint.labs.wizard.upload.ArchiverZip;
import com.engagepoint.labs.wizard.upload.FileDownloadController;

import org.apache.log4j.Logger;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;

import super_binding.QType;
import super_binding.QuestionRule;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Named("uiNavigationBean")
@SessionScoped
public class UINavigationBean implements Serializable {

    //
    private static final long serialVersionUID = 7470581070941487130L;
    private static final Logger LOGGER = Logger.getLogger(UINavigationBean.class);

    private String xmlPath;
    private boolean needToStopUserOnCurrentTopic = false;
    private QType currentQuestionType;
    /**
     * This is model class to hold data about XML files and Navigation data
     * objects
     */
    @Inject
    private NavigationData navigationData;
    @Inject
    private FileDownloadController fileDownloadController;
    @Inject
    private DataGridHandler gridHandler;
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

    public void refresh(String path) {
        xmlPath = path;
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            LOGGER.warn("Thread interrupted", e);
        }
        navigationData.refreshXMLScreen(path);

    }

    /**
     * At first this method configure navigationData (set pageId to first, set
     * topicId to first etc.) then calls init breadcrumb and init menu set
     * needRefresh to false and redirect user to page wizard-index
     *
     * @return wizard index page name
     */
    public String start() {
        clearDataFromSession();
        navigationData.startWizard();
        initBreadcrumb();
        initMenu();
        setRulesInAllQuestions();
        return "wizard-index?faces-redirect=true";
    }

    public void clearDataFromSession() {
        if (null != xmlPath) {
            refresh(xmlPath);
        } else {
            navigationData.startSelectXMLScreen();
        }

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
            item.setValue(wizardPage.getPageName());
            // creating EL expressions for all items in breadcrumb
            elExpression = expressionFactory.createMethodExpression(elContext,
                    "#{uiNavigationBean.changeCurrentPage(\"" + wizardPage.getId() + "\")}", void.class,
                    new Class[]{String.class});
            // set elExpression on item action attribute
            item.setActionExpression(elExpression);
            navigationData.getBreadcrumbModel().addMenuItem(item);
        }
        changeStyleOfCurrentPageButton(WizardComponentStyles.STYLE_PAGE_BUTTON_SELECTED);
        initMenu();
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
        navigationData.getCurrentTopicTitles().clear();
        navigationData.getMenuModel().getContents().clear();

        // now we start create new topics for page
        facesContext = FacesContext.getCurrentInstance();
        // get elContext (super container for EL expressions)
        elContext = facesContext.getELContext();
        // get expression factory for creating EL expressions in following cycle
        expressionFactory = facesContext.getApplication().getExpressionFactory();
        // Iterating over all pages in model
        for (int i = 0; i < getTopicCount(navigationData.getCurrentPageID()); i++) {
            // retrieve topicID from navigation data
            String topicID = navigationData.getWizardForm().getWizardPageById(navigationData.getCurrentPageID())
                    .getTopicList().get(i).getId();
            // add topicID to topics ID's list
            navigationData.getCurrentTopicIDs().add(topicID);
            // get topics title by id
            String topicTitle = navigationData.getWizardForm().getWizardTopicById(topicID).getGroupTitle();
            // add title to topic titles
            navigationData.getCurrentTopicTitles().add(topicTitle);
            // creating menu item
            MenuItem item = new MenuItem();
            MethodExpression elExpression;
            // set titles for our menu items
            item.setValue(topicTitle);
            // creating EL expressions for all items in menu
            elExpression = expressionFactory.createMethodExpression(elContext,
                    "#{uiNavigationBean.changeCurrentTopic(\"" + topicID + "\")}", void.class,
                    new Class[]{String.class});
            // set elExpression on item action attribute
            item.setActionExpression(elExpression);
            navigationData.getMenuModel().addMenuItem(item);
        }
        changeStyleOfCurrentTopicButton(WizardComponentStyles.STYLE_TOPIC_BUTTON_SELECTED);
        // after initialization menu questions creator method called
        createQuestions();
    }

    /**
     * Create questions, method must be called for every navigation case
     */
    public void createQuestions() {
        UIComponentGenerator generator = new UIComponentGenerator();
        WizardForm wizardForm = navigationData.getWizardForm();
        WizardPage wizardPage = wizardForm.getWizardPageById(navigationData.getCurrentPageID());
        WizardTopic wizardTopic = wizardForm.getWizardTopicById(navigationData.getCurrentTopicID());
        Map<WizardQuestion, Boolean> questionsMap = new LinkedHashMap<>();
        for (WizardQuestion question : wizardTopic.getWizardQuestionList()) {
            questionsMap.put(question, isQuestionAParent(question));
        }
        List<UIComponent> panelList = generator.getPanelList(questionsMap, wizardPage.getPageNumber(),
                wizardTopic.getTopicNumber(), this, gridHandler);
        navigationData.setPanelList(panelList);
        navigationData.getPanelGrid().getChildren().clear();
        for (UIComponent panel : navigationData.getPanelList()) {
            navigationData.getPanelGrid().getChildren().add(panel);
        }
        HtmlForm form = navigationData.getMainContentForm();
        UIComponent scrollableDiv = form.findComponent("scrollableDiv");
        scrollableDiv.getAttributes().put("styleClass", navigationData.getMainContentFormStyle());
        RequestContext.getCurrentInstance().update("maincontentid-scrollableDiv");
        RequestContext.getCurrentInstance().update("maincontentid-j_id1");
        RequestContext.getCurrentInstance().update("leftmenuid-leftMenu");
        RequestContext.getCurrentInstance().update("navigationButtonsForm-btnsDiv");
    }

    private int getPageCount() {
        return navigationData.getWizardForm().getWizardPageList().size();
    }

    private int getTopicCount(String pageID) {
        return navigationData.getWizardForm().getWizardPageById(pageID).getTopicList().size();
    }

    /**
     * Method called every time for changing current page.
     *
     * @param newCurrentPageID
     */
    public void changeCurrentPage(String newCurrentPageID) {
        commitAnswers(getQuestionListFromCurrentTopic());
        WizardForm wizardForm = navigationData.getWizardForm();
        Integer newCurrentPageNumber = wizardForm.getWizardPageById(newCurrentPageID).getPageNumber();
        Integer currentTopicNumber = wizardForm.getWizardTopicById(navigationData.getCurrentTopicID()).getTopicNumber();
        if (newCurrentPageNumber > navigationData.getWizardForm().getPageLimit()) {
            return;
        } else if (currentTopicNumber < navigationData.getWizardForm().getTopicLimit()
                && !checkAllRequiredQuestions(getQuestionListFromCurrentTopic())) {
            RequestContext.getCurrentInstance().execute(ClientConstantStrings.DIALOGSHOW);
            return;
        } else if (needToStopUserOnCurrentTopic) {
            needToStopUserOnCurrentTopic = false;
            return;
        } else {
            validateAllRequiredQuestions(getQuestionListFromCurrentTopic());
        }
        clearCurrentTopicsData();
        navigationData.setCurrentPageIDAndTitle(newCurrentPageID);
        // After changing current page to a new one, mark it with a new style
        changeStyleOfCurrentPageButton(WizardComponentStyles.STYLE_PAGE_BUTTON_SELECTED);
        // set current topic to first on new page
        navigationData.setCurrentTopicIDAndTitle(navigationData.getWizardForm()
                .getWizardPageById(navigationData.getCurrentPageID()).getTopicList().get(0).getId());
        // create new menu for page
        initMenu();
        RequestContext.getCurrentInstance().update("brd-breadcrumb");
    }

    /**
     * change topic by id
     *
     * @param newCurrentTopicID
     */
    public void changeCurrentTopic(String newCurrentTopicID) {
        commitAnswers(getQuestionListFromCurrentTopic());
        WizardForm wizardForm = navigationData.getWizardForm();
        Integer newCurrentTopicNumber = wizardForm.getWizardTopicById(newCurrentTopicID).getTopicNumber();
        Integer currentTopicNumber = wizardForm.getWizardTopicById(navigationData.getCurrentTopicID()).getTopicNumber();
        if (newCurrentTopicNumber > navigationData.getWizardForm().getTopicLimit()) {
            return;
        } else if (currentTopicNumber < navigationData.getWizardForm().getTopicLimit()
                && !checkAllRequiredQuestions(getQuestionListFromCurrentTopic())) {
            RequestContext.getCurrentInstance().execute(ClientConstantStrings.DIALOGSHOW);
            return;
        } else if (needToStopUserOnCurrentTopic) {
            needToStopUserOnCurrentTopic = false;
            return;
        } else {
            validateAllRequiredQuestions(getQuestionListFromCurrentTopic());
        }
        navigationData.setCurrentTopicIDAndTitle(newCurrentTopicID);
        changeStyleOfCurrentTopicButton(WizardComponentStyles.STYLE_TOPIC_BUTTON_SELECTED);
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
        commitAnswers(getQuestionListFromCurrentTopic());
        if (!checkAllRequiredQuestions(getQuestionListFromCurrentTopic())) {
            RequestContext.getCurrentInstance().execute(ClientConstantStrings.DIALOGSHOW);
            return;
        } else if (needToStopUserOnCurrentTopic) {
            needToStopUserOnCurrentTopic = false;
            return;
        }
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
            // submit, validation, and confirmation calls actions here
        }
    }

    public String finishButtonClick() {
        commitAnswers(getQuestionListFromCurrentTopic());
        if (needToStopUserOnCurrentTopic) {
            needToStopUserOnCurrentTopic = false;
            return "";
        }
        if (!checkAllRequiredQuestions(getQuestionListFromCurrentTopic())) {
            RequestContext.getCurrentInstance().execute(ClientConstantStrings.DIALOGSHOW);
            return "";
        }
        return "wizard-confirmation?faces-redirect=true";
    }

    public void exportButtonClick() {
        List<File> filesForArchive = new ArrayList<>(7);
        filesForArchive.add(navigationData.getExportFile());
        List<WizardQuestion> allWizardQuestions = navigationData.getWizardForm().getAllWizardQuestions();
        for (WizardQuestion singleQuestion : allWizardQuestions) {
            if (singleQuestion.getQuestionType().equals(QType.FILEUPLOAD)) {
                filesForArchive.add((File) singleQuestion.getAnswer().getValue());
            }
        }
        ArchiverZip.addFilesToZip(filesForArchive);
        try {
            FileInputStream zipFileStream = new FileInputStream(ArchiverZip.ZIP_FILE_NAME);
            fileDownloadController.setFile(new DefaultStreamedContent(zipFileStream, "application/zip", "answer.zip"));
        } catch (FileNotFoundException e) {
            LOGGER.warn("ZIP FileNotFound", e);
        }
        for (File file : filesForArchive) {
            file.delete();
        }
    }

    public void previousButtonClick() {
        Integer newCurrentTopicNumber = navigationData.getWizardForm()
                .getWizardTopicById(navigationData.getCurrentTopicID()).getTopicNumber();
        if (newCurrentTopicNumber != navigationData.getWizardForm().getTopicLimit()) {
            if (!checkAllRequiredQuestions(getQuestionListFromCurrentTopic())) {
                RequestContext.getCurrentInstance().execute(ClientConstantStrings.DIALOGSHOW);
                return;
            }
        } else if (needToStopUserOnCurrentTopic) {
            needToStopUserOnCurrentTopic = false;
            return;
        }
        if (navigationData.setCurrentTopicIDtoPrev()) {
            changeCurrentTopic(navigationData.getCurrentTopicID());
        } else if (navigationData.setCurrentPageIDtoPrev()) {
            changeCurrentPage(navigationData.getCurrentPageID());
            navigationData.setCurrentTopicID(navigationData.getCurrentTopicIDs().get(
                    navigationData.getCurrentTopicIDs().size() - 1));
            changeCurrentTopic(navigationData.getCurrentTopicID());
        }
    }

    /**
     * Method changes CSS Style of current selected PageButton from breadcrumb
     *
     * @param styleClass style class from CSS file
     */
    public void changeStyleOfCurrentPageButton(String styleClass) {
	List<WizardPage> pageList = navigationData.getWizardForm().getWizardPageList();
	WizardPage wizardPage;
	MenuItem pageMenuItem;
	MenuItem firstTopicMenuItem = (MenuItem) navigationData.getBreadcrumbModel().getContents().get(0);
	for (int pageIndex = 0; pageIndex < pageList.size(); pageIndex++) {
	    wizardPage = pageList.get(pageIndex);
	    pageMenuItem = (MenuItem) navigationData.getBreadcrumbModel().getContents().get(pageIndex);
	    if (wizardPage.getId().equals(navigationData.getCurrentPageID())) {
		if (pageIndex == 0) {
		    if (pageMenuItem.getId() != null) {
			pageMenuItem.setId("j_id1");
		    }
		} else {
		    firstTopicMenuItem.setId(pageMenuItem.getId() + "a");
		}
		LOGGER.info(pageMenuItem.getStyleClass());
		pageMenuItem.setStyleClass(styleClass);
	    } else {
		if (pageIndex > (navigationData.getWizardForm().getPageLimit() - 1)) {
		    pageMenuItem.setStyleClass(WizardComponentStyles.STYLE_MENU_ITEM_DISABLED);
		} else {
		    pageMenuItem.setStyleClass(WizardComponentStyles.STYLE_PAGE_ITEM_HOVER);
		}
	    }
	}
    }

    private void commitAnswers(List<WizardQuestion> wizardQuestionList) {
        for (WizardQuestion question : wizardQuestionList) {
            if (question.getAnswer() == null && question.getDefaultAnswer() != null) {
                question.setAnswer(question.getDefaultAnswer());
                if (question.getValid() == null) {
                    question.setValid(true);
                }
            }
        }
    }

    private boolean checkAllRequiredQuestions(List<WizardQuestion> wizardQuestionList) {
        for (WizardQuestion question : wizardQuestionList) {
            if (!question.isIgnored() && question.isRequired() && (null == question.getValid() || !question.getValid())) {
                return false;
            }
        }
        return true;
    }

    private void validateAllRequiredQuestions(List<WizardQuestion> wizardQuestionList) {
        QuestionAnswerValidator validator;
        for (WizardQuestion question : wizardQuestionList) {
            validator = new QuestionAnswerValidator(question);
            question.setValid(validator.validate(question.getAnswer()));
        }
    }

    private List<WizardQuestion> getQuestionListFromCurrentTopic() {
        WizardForm wizardForm = navigationData.getWizardForm();
        String currentTopicID = navigationData.getCurrentTopicID();
        WizardTopic wizardTopic = wizardForm.getWizardTopicById(currentTopicID);
        return wizardTopic.getWizardQuestionList();
    }

    public void changeStyleOfCurrentTopicButton(String styleClass) {
        List<WizardTopic> topicList = navigationData.getWizardForm()
                .getWizardPageById(navigationData.getCurrentPageID()).getTopicList();
        WizardTopic topic;
        MenuItem item;
        for (int topicIndex = 0; topicIndex < topicList.size(); topicIndex++) {
            topic = topicList.get(topicIndex);
            item = (MenuItem) navigationData.getMenuModel().getContents().get(topicIndex);
            if (topic.getId().equals(navigationData.getCurrentTopicID())) {
                item.setStyleClass(styleClass);
            } else {
                if (topic.getTopicNumber() > navigationData.getWizardForm().getTopicLimit()) {
                    item.setStyleClass(WizardComponentStyles.STYLE_MENU_ITEM_DISABLED);
                } else {
                    item.setStyleClass("");
                }
            }
        }
    }

    public void executeAllRulesOnCurrentTopic() {
        boolean isEverChanged = false;
        String currentTopicID = navigationData.getCurrentTopicID();
        WizardTopic wizardTopicById = navigationData.getWizardForm().getWizardTopicById(currentTopicID);
        for (int i = 0; i < wizardTopicById.getWizardQuestionList().size(); i++) {
            WizardQuestion question = wizardTopicById.getWizardQuestionList().get(i);
            boolean isChanged = question.executeAllRules();
            if (isChanged) {
                i = -1;
                isEverChanged = true;
                if (QType.TEXT == currentQuestionType || QType.PARAGRAPHTEXT == currentQuestionType) {
                    needToStopUserOnCurrentTopic = true;
                }
            } else if (!isEverChanged
                    && (QType.TEXT == currentQuestionType || QType.PARAGRAPHTEXT == currentQuestionType)) {
                needToStopUserOnCurrentTopic = false;
            }
        }
    }

    private void setRulesInAllQuestions() {
        for (WizardQuestion question : navigationData.getWizardForm().getAllWizardQuestions()) {
            question.setRuleExecutor(new RuleExecutor(navigationData.getWizardForm()));
        }
    }

    private boolean isQuestionAParent(WizardQuestion currentQuestion) {
        boolean isParent = false;
        isParent = loopIsQuestionAParent(currentQuestion);
        return isParent;
    }

    private boolean loopIsQuestionAParent(WizardQuestion currentQuestion) {
        boolean isParent = false;
        for (WizardPage wizardPage : navigationData.getWizardForm().getWizardPageList()) {
            if (wizardPage.getPageNumber() > navigationData.getWizardForm().getPageLimit() || isParent) {
                return false;
            }
            for (WizardTopic wizardTopic : wizardPage.getTopicList()) {
                if (wizardTopic.getTopicNumber() > navigationData.getWizardForm().getTopicLimit() || isParent) {
                    return false;
                }
                for (WizardQuestion wizardQuestion : wizardTopic.getWizardQuestionList()) {
                    if (wizardQuestion.getRules() != null) {
                        isParent = compareParentsIdAndCurrentQuestionId(wizardQuestion.getRules(),
                                currentQuestion.getId());
                    }
                    if (isParent) {
                        return false;
                    }
                }
            }
        }
        return isParent;
    }

    private boolean compareParentsIdAndCurrentQuestionId(List<QuestionRule> ruleList, String questionId) {
        boolean match = false;
        for (QuestionRule rule : ruleList) {
            if (rule.getParentId().equals(questionId)) {
                match = true;
            }
        }
        return match;
    }

    public QType getCurrentQuestionType() {
        return currentQuestionType;
    }

    public void setCurrentQuestionType(QType currentQuestionType) {
        this.currentQuestionType = currentQuestionType;
    }
}