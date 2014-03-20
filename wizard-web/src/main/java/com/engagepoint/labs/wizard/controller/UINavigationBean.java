package com.engagepoint.labs.wizard.controller;

import com.engagepoint.labs.wizard.bean.WizardForm;
import com.engagepoint.labs.wizard.bean.WizardPage;
import com.engagepoint.labs.wizard.bean.WizardTopic;
import com.engagepoint.labs.wizard.client.ClientConstantStrings;
import com.engagepoint.labs.wizard.handler.DataGridHandler;
import com.engagepoint.labs.wizard.handler.TimeHandler;
import com.engagepoint.labs.wizard.model.NavigationData;
import com.engagepoint.labs.wizard.rulexecutors.PageRuleExecutor;
import com.engagepoint.labs.wizard.rulexecutors.QuestionRuleExecutor;
import com.engagepoint.labs.wizard.questions.WizardQuestion;
import com.engagepoint.labs.wizard.rulexecutors.TopicRuleExecutor;
import com.engagepoint.labs.wizard.style.WizardComponentStyles;
import com.engagepoint.labs.wizard.ui.UIComponentGenerator;
import com.engagepoint.labs.wizard.ui.validators.QuestionAnswerValidator;
import com.engagepoint.labs.wizard.upload.ArchiverZip;
import com.engagepoint.labs.wizard.upload.FileDownloadController;

import org.apache.log4j.Logger;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;

import super_binding.GroupRule;
import super_binding.PageRule;
import super_binding.QType;
import super_binding.QuestionRule;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
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
import java.util.concurrent.CopyOnWriteArrayList;

@Named("uiNavigationBean")
@SessionScoped
public class UINavigationBean implements Serializable {

    //
    private static final long serialVersionUID = 7470581070941487130L;
    private static final Logger LOGGER = Logger
	    .getLogger(UINavigationBean.class);
    private List<File> filesForArchive;
    private List<String> xmlPathList = new CopyOnWriteArrayList<>();
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
    @Inject
    private TimeHandler timeHandler;
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
    private List<MenuItem> pageMenuItemList;
    private List<MenuItem> topicMenuItemList;

    public QType getCurrentQuestionType() {
	return currentQuestionType;
    }

    public void setCurrentQuestionType(QType currentQuestionType) {
	this.currentQuestionType = currentQuestionType;
    }

    public List<MenuItem> getPageMenuItemList() {
	return pageMenuItemList;
    }

    public void setPageMenuItemList(List<MenuItem> pageMenuItemList) {
	this.pageMenuItemList = pageMenuItemList;
    }

    public List<MenuItem> getTopicMenuItemList() {
	return topicMenuItemList;
    }

    public void setTopicMenuItemList(List<MenuItem> topicMenuItemList) {
	this.topicMenuItemList = topicMenuItemList;
    }

    /**
     * Method clears current topic IDs and current topic titles It is needed
     * when user sets next page on breadcrumb
     */
    public void clearCurrentTopicsData() {
	navigationData.getAllTopicsIdOnCurrentPage().clear();
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
	xmlPathList.add(path);
	try {
	    Thread.sleep(200);
	} catch (InterruptedException e) {
	    LOGGER.warn("Thread interrupted", e);
	}
	navigationData.refreshXMLScreen(xmlPathList);
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
	pageMenuItemList = new ArrayList<>();
	topicMenuItemList = new ArrayList<>();
	initBreadcrumb();
	setRulesInAllQuestionsTopicAndPages();
	return "wizard-index?faces-redirect=true";
    }

    public void clearDataFromSession() {
	if (!xmlPathList.isEmpty()) {
	    for (String xmlFile : xmlPathList) {
		refresh(xmlFile);
	    }
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
	expressionFactory = facesContext.getApplication()
		.getExpressionFactory();
	// Iterating over all pages in model
	for (int i = 0; i < getPageCount(); i++) {
	    // creating menu item
	    MenuItem item = new MenuItem();
	    MethodExpression elExpression;
	    WizardPage wizardPage;
	    // get page from navigationData by index
	    wizardPage = getWizardForm().getWizardPageList().get(i);
	    // set titles for breadcrumb items
	    item.setValue(wizardPage.getPageName());
	    item.setId(wizardPage.getId());
	    // creating EL expressions for all items in breadcrumb
	    elExpression = expressionFactory.createMethodExpression(
		    elContext,
		    "#{uiNavigationBean.changeCurrentPage(\""
			    + wizardPage.getId() + "\")}", void.class,
		    new Class[] { String.class });
	    // set elExpression on item action attribute
	    item.setActionExpression(elExpression);
	    pageMenuItemList.add(item);
	    navigationData.getBreadcrumbModel().addMenuItem(item);
	}
	fillAllPagesIdOnForm();
	setMenuItemInPagesOnCurrentForm();
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

	navigationData.getAllTopicsIdOnCurrentPage().clear();
	navigationData.getMenuModel().getContents().clear();
	topicMenuItemList.clear();

	// now we start create new topics for page
	facesContext = FacesContext.getCurrentInstance();
	// get elContext (super container for EL expressions)
	elContext = facesContext.getELContext();
	// get expression factory for creating EL expressions in following cycle
	expressionFactory = facesContext.getApplication()
		.getExpressionFactory();
	// Iterating over all pages in model
	for (int i = 0; i < getTopicCount(navigationData.getCurrentPageID()); i++) {
	    // retrieve topicID from navigation data
	    String topicID = getWizardForm()
		    .getWizardPageById(navigationData.getCurrentPageID())
		    .getTopicList().get(i).getId();
	    // get topics title by id
	    String topicTitle = getWizardForm().getWizardTopicById(topicID)
		    .getGroupTitle();
	    // add title to topic titles
	    // creating menu item
	    MenuItem item = new MenuItem();
	    MethodExpression elExpression;
	    // set titles for our menu items
	    item.setValue(topicTitle);
	    item.setId(topicID);
	    // creating EL expressions for all items in menu
	    elExpression = expressionFactory.createMethodExpression(elContext,
		    "#{uiNavigationBean.changeCurrentTopic(\"" + topicID
			    + "\")}", void.class, new Class[] { String.class });
	    // set elExpression on item action attribute
	    item.setActionExpression(elExpression);
	    topicMenuItemList.add(item);
	    navigationData.getMenuModel().addMenuItem(item);
	}
	fillAllTopicsIdOnPage();
	setMenuItemInTopicsOnCurrentPage();
	changeStyleOfCurrentTopicButton(WizardComponentStyles.STYLE_TOPIC_BUTTON_SELECTED);
	// after initialization menu questions creator method called
	createQuestions();
    }

    /**
     * Create questions, method must be called for every navigation case
     */
    public void createQuestions() {
	UIComponentGenerator generator = new UIComponentGenerator();
	WizardForm wizardForm = getWizardForm();
	WizardPage wizardPage = wizardForm.getWizardPageById(navigationData
		.getCurrentPageID());
	WizardTopic wizardTopic = wizardForm.getWizardTopicById(navigationData
		.getCurrentTopicID());
	Map<WizardQuestion, Boolean> questionsMap = new LinkedHashMap<>();
	for (WizardQuestion question : wizardTopic.getWizardQuestionList()) {
	    questionsMap.put(question, isQuestionParent(question));
	}
	List<UIComponent> panelList = generator.getPanelList(questionsMap,
		wizardPage.getPageNumber(), wizardTopic.getTopicNumber(), this,
		gridHandler, timeHandler);
	navigationData.setPanelList(panelList);
	navigationData.getPanelGrid().getChildren().clear();
	for (UIComponent panel : navigationData.getPanelList()) {
	    navigationData.getPanelGrid().getChildren().add(panel);
	}
	HtmlForm form = navigationData.getMainContentForm();
	// UIComponent scrollableDiv = form.findComponent("scrollableDiv");
	// scrollableDiv.getAttributes().put("styleClass",
	// navigationData.getMainContentFormStyle());
	// RequestContext.getCurrentInstance().update("maincontentid-scrollableDiv");
	RequestContext.getCurrentInstance().update("maincontentid-j_id1");
	RequestContext.getCurrentInstance().update("leftmenuid-leftMenu");
	RequestContext.getCurrentInstance().update(
		"navigationButtonsForm-btnsDiv");
    }

    private int getPageCount() {
	return getWizardForm().getWizardPageList().size();
    }

    private int getTopicCount(String pageID) {
	return getWizardForm().getWizardPageById(pageID).getTopicList().size();
    }

    /**
     * Method called every time for changing current page.
     *
     * @param newCurrentPageID
     */
    public void changeCurrentPage(String newCurrentPageID) {
	commitAnswers(getQuestionListFromCurrentTopic());
	WizardForm wizardForm = getWizardForm();
	Integer newCurrentPageNumber = wizardForm.getWizardPageById(
		newCurrentPageID).getPageNumber();
	Integer currentTopicNumber = wizardForm.getWizardTopicById(
		navigationData.getCurrentTopicID()).getTopicNumber();
	if (newCurrentPageNumber > getWizardForm().getPageLimit()) {
	    return;
	} else if (currentTopicNumber < getWizardForm().getTopicLimit()
		&& !checkAllRequiredQuestions(getQuestionListFromCurrentTopic())) {
	    RequestContext.getCurrentInstance().execute(
		    ClientConstantStrings.DIALOGSHOW);
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
	navigationData.setCurrentTopicIDAndTitle(getWizardForm()
		.getWizardPageById(navigationData.getCurrentPageID())
		.getTopicList().get(0).getId());
	// create new menu for page
	initMenu();
	RequestContext.getCurrentInstance().update("dateStubb-breadcrumb");
    }

    /**
     * change topic by id
     *
     * @param newCurrentTopicID
     */
    public void changeCurrentTopic(String newCurrentTopicID) {
	commitAnswers(getQuestionListFromCurrentTopic());
	WizardForm wizardForm = getWizardForm();
	Integer newCurrentTopicNumber = wizardForm.getWizardTopicById(
		newCurrentTopicID).getTopicNumber();
	Integer currentTopicNumber = wizardForm.getWizardTopicById(
		navigationData.getCurrentTopicID()).getTopicNumber();
	if (newCurrentTopicNumber > getWizardForm().getTopicLimit()) {
	    return;
	} else if (currentTopicNumber < getWizardForm().getTopicLimit()
		&& !checkAllRequiredQuestions(getQuestionListFromCurrentTopic())) {
	    RequestContext.getCurrentInstance().execute(
		    ClientConstantStrings.DIALOGSHOW);
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
	    RequestContext.getCurrentInstance().execute(
		    ClientConstantStrings.DIALOGSHOW);
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
	}
    }

    public String finishButtonClick() {
	commitAnswers(getQuestionListFromCurrentTopic());
	if (needToStopUserOnCurrentTopic) {
	    needToStopUserOnCurrentTopic = false;
	    return "";
	}
	if (!checkAllRequiredQuestions(getQuestionListFromCurrentTopic())) {
	    RequestContext.getCurrentInstance().execute(
		    ClientConstantStrings.DIALOGSHOW);
	    return "";
	}
	return "wizard-confirmation?faces-redirect=true";
    }

    public void exportButtonClick() {
	filesForArchive = new ArrayList<>(7);
	filesForArchive.add(navigationData.getExportFile());
	List<WizardQuestion> allWizardQuestions = getWizardForm()
		.getAllWizardQuestions();
	for (WizardQuestion singleQuestion : allWizardQuestions) {
	    if (singleQuestion.getQuestionType().equals(QType.FILEUPLOAD)) {
		filesForArchive.add((File) singleQuestion.getAnswer()
			.getValue());
	    }
	}
	ArchiverZip.addFilesToZip(filesForArchive);
	try {
	    FileInputStream zipFileStream = new FileInputStream(
		    ArchiverZip.ZIP_FILE_NAME);
	    fileDownloadController.setFile(new DefaultStreamedContent(
		    zipFileStream, "application/zip", "answer.zip"));
	} catch (FileNotFoundException e) {
	    LOGGER.warn("ZIP FileNotFound", e);
	}
    }

    public void previousButtonClick() {
	Integer newCurrentTopicNumber = getWizardForm().getWizardTopicById(
		navigationData.getCurrentTopicID()).getTopicNumber();
	if (newCurrentTopicNumber != getWizardForm().getTopicLimit()) {
	    if (!checkAllRequiredQuestions(getQuestionListFromCurrentTopic())) {
		RequestContext.getCurrentInstance().execute(
			ClientConstantStrings.DIALOGSHOW);
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
	    navigationData
		    .setCurrentTopicID(navigationData
			    .getAllTopicsIdOnCurrentPage().get(
				    navigationData
					    .getAllTopicsIdOnCurrentPage()
					    .size() - 1));
	    changeCurrentTopic(navigationData.getCurrentTopicID());
	}
	executeAllRules();
    }

    /**
     * Method changes CSS Style of current selected PageButton from breadcrumb
     *
     * @param styleClass
     *            style class from CSS file
     */
    public void changeStyleOfCurrentPageButton(String styleClass) {
	List<WizardPage> pageList = getWizardForm().getWizardPageList();
	WizardPage wizardPage;
	MenuItem pageMenuItem;
	for (int pageIndex = 0; pageIndex < pageList.size(); pageIndex++) {
	    wizardPage = pageList.get(pageIndex);
	    pageMenuItem = (MenuItem) navigationData.getBreadcrumbModel()
		    .getContents().get(pageIndex);
	    if (wizardPage.getId().equals(navigationData.getCurrentPageID())) {
		pageMenuItem
			.setStyle("font-size: 16px !important; font-weight: bold !important;");
	    } else {
		if (pageIndex > (getWizardForm().getPageLimit() - 1)) {
		    pageMenuItem.setStyle("opacity: 0.2;");
		} else {
		    pageMenuItem
			    .setStyle("font-size: 16px; font-weight: normal;");
		}
	    }
	}
    }

    private void commitAnswers(List<WizardQuestion> wizardQuestionList) {
	for (WizardQuestion question : wizardQuestionList) {
	    if (question.getAnswer() == null
		    && question.getDefaultAnswer() != null) {
		question.setAnswer(question.getDefaultAnswer());
		if (question.getValid() == null) {
		    question.setValid(true);
		}
	    }
	}
    }

    private boolean checkAllRequiredQuestions(
	    List<WizardQuestion> wizardQuestionList) {
	for (WizardQuestion question : wizardQuestionList) {
	    if (!question.isIgnored() && question.isRequired()
		    && (null == question.getValid() || !question.getValid())) {
		return false;
	    }
	}
	return true;
    }

    private void validateAllRequiredQuestions(
	    List<WizardQuestion> wizardQuestionList) {
	QuestionAnswerValidator validator;
	for (WizardQuestion question : wizardQuestionList) {
	    validator = new QuestionAnswerValidator(question);
	    question.setValid(validator.validate(question.getAnswer()));
	}
    }

    private List<WizardQuestion> getQuestionListFromCurrentTopic() {
	WizardForm wizardForm = getWizardForm();
	String currentTopicID = navigationData.getCurrentTopicID();
	WizardTopic wizardTopic = wizardForm.getWizardTopicById(currentTopicID);
	return wizardTopic.getWizardQuestionList();
    }

    public void changeStyleOfCurrentTopicButton(String styleClass) {
	List<WizardTopic> topicList = getWizardForm().getWizardPageById(
		navigationData.getCurrentPageID()).getTopicList();
	WizardTopic topic;
	MenuItem item;
	for (int topicIndex = 0; topicIndex < topicList.size(); topicIndex++) {
	    topic = topicList.get(topicIndex);
	    item = (MenuItem) navigationData.getMenuModel().getContents()
		    .get(topicIndex);
	    if (topic.getId().equals(navigationData.getCurrentTopicID())) {
		item.setStyleClass(styleClass);
	    } else {
		if (topic.getTopicNumber() > getWizardForm().getTopicLimit()) {
		    item.setStyleClass(WizardComponentStyles.STYLE_MENU_ITEM_DISABLED);
		} else {
		    item.setStyleClass("");
		}
	    }
	}
    }

    public void executeAllRules() {
	boolean isEverChanged = false;
	isEverChanged = executeAllPagesRuleOnCurrentForm(isEverChanged);
	isEverChanged = executeAllTopicsRuleOnCurrentPage(isEverChanged);
	executeAllQuestionsRuleOnCurrentTopic(isEverChanged);
    }

    private boolean executeAllPagesRuleOnCurrentForm(boolean isEverChanged) {
	for (WizardPage page : getWizardForm().getWizardPageList()) {
	    stopUserOnCurrentTopicOrNot(page.executeAllRules());
	}
	fillAllPagesIdOnForm();
	updateBreadcrumbAfterRulesAreExecuted();
	return isEverChanged;
    }

    private boolean executeAllTopicsRuleOnCurrentPage(boolean isEverChanged) {
	String currentPageID = navigationData.getCurrentPageID();
	WizardPage page = getWizardForm().getWizardPageById(currentPageID);
	for (WizardTopic topic : page.getTopicList()) {
	    stopUserOnCurrentTopicOrNot(topic.executeAllRules());
	}
	fillAllTopicsIdOnPage();
	updateLeftMenuAfterRulesAreExecuted();
	return isEverChanged;
    }

    private boolean executeAllQuestionsRuleOnCurrentTopic(boolean isEverChanged) {
	String currentTopicID = navigationData.getCurrentTopicID();
	WizardTopic topic = getWizardForm().getWizardTopicById(currentTopicID);
	for (WizardQuestion question : topic.getWizardQuestionList()) {
	    stopUserOnCurrentTopicOrNot(question.executeAllRules());
	}
	return isEverChanged;
    }

    private void stopUserOnCurrentTopicOrNot(boolean isChanged) {
	if (isChanged) {
	    if (QType.TEXT == currentQuestionType
		    || QType.PARAGRAPHTEXT == currentQuestionType) {
		needToStopUserOnCurrentTopic = true;
	    }
	} else {
	    needToStopUserOnCurrentTopic = false;
	}
    }

    private void setMenuItemInPagesOnCurrentForm() {
	WizardForm wizardForm = getWizardForm();
	for (WizardPage page : wizardForm.getWizardPageList()) {
	    page.setRuleExecutor(new PageRuleExecutor(wizardForm));
	    page.ruleExecutor.setMenuItem(findMenuItemForWizardPage(page
		    .getId()));
	}
    }

    private void setMenuItemInTopicsOnCurrentPage() {
	WizardForm wizardForm = getWizardForm();
	WizardPage wizardPage = wizardForm.getWizardPageById(navigationData
		.getCurrentPageID());
	for (WizardTopic topic : wizardPage.getTopicList()) {
	    topic.setRuleExecutor(new TopicRuleExecutor(wizardForm));
	    topic.ruleExecutor.setMenuItem(findMenuItemForWizardTopic(topic
		    .getId()));
	}
    }

    private void setRulesInAllQuestionsTopicAndPages() {
	WizardForm wizardForm = getWizardForm();
	for (WizardQuestion question : wizardForm.getAllWizardQuestions()) {
	    question.setRuleExecutor(new QuestionRuleExecutor(wizardForm));
	}
    }

    private void fillAllPagesIdOnForm() {
	navigationData.getAllPagesIdOnCurrentForm().clear();
	for (int i = 0; i < getWizardForm().getWizardPageList().size(); i++) {
	    WizardPage page = getWizardForm().getWizardPageList().get(i);
	    if (!page.isIgnored()) {
		navigationData.getAllPagesIdOnCurrentForm().add(page.getId());
	    }
	}
    }

    private void fillAllTopicsIdOnPage() {
	navigationData.getAllTopicsIdOnCurrentPage().clear();
	for (int i = 0; i < getTopicCount(navigationData.getCurrentPageID()); i++) {
	    WizardTopic topic = getWizardForm()
		    .getWizardPageById(navigationData.getCurrentPageID())
		    .getTopicList().get(i);
	    if (!topic.isIgnored()) {
		navigationData.getAllTopicsIdOnCurrentPage().add(topic.getId());
	    }
	}
    }

    private void updateBreadcrumbAfterRulesAreExecuted() {
	for (MenuItem menuItem : pageMenuItemList) {
	    navigationData.getBreadcrumbModel().addMenuItem(menuItem);
	}
	RequestContext.getCurrentInstance().update("dateStubb-breadcrumb");
    }

    private void updateLeftMenuAfterRulesAreExecuted() {
	for (MenuItem menuItem : topicMenuItemList) {
	    navigationData.getMenuModel().addMenuItem(menuItem);
	}
	RequestContext.getCurrentInstance().update("leftmenuid-leftMenu");
    }

    private MenuItem findMenuItemForWizardPage(String pageID) {
	MenuItem menuItem = null;
	for (MenuItem item : pageMenuItemList) {
	    if (pageID.equals(item.getId())) {
		menuItem = item;
		break;
	    }
	}
	return menuItem;
    }

    private MenuItem findMenuItemForWizardTopic(String topicID) {
	MenuItem menuItem = null;
	for (MenuItem item : topicMenuItemList) {
	    if (topicID.equals(item.getId())) {
		menuItem = item;
		break;
	    }
	}
	return menuItem;
    }

    private boolean isQuestionParent(WizardQuestion currentQuestion) {
	boolean isParent = false;
	for (WizardPage wizardPage : getWizardForm().getWizardPageList()) {
	    if (isParent) {
		break;
	    }
	    for (WizardTopic wizardTopic : wizardPage.getTopicList()) {
		if (isParent) {
		    break;
		}
		isParent = isQuestionParentTopicNestedLoop(wizardTopic,
			wizardPage.getPageRuleList(), currentQuestion.getId());
	    }
	}
	return isParent;
    }

    private boolean isQuestionParentTopicNestedLoop(WizardTopic wizardTopic,
	    List<PageRule> pageRuleList, String currentQuestionId) {
	boolean isParent = false;
	for (WizardQuestion wizardQuestion : wizardTopic
		.getWizardQuestionList()) {
	    if (pageRuleList != null) {
		isParent = compareParentsIdAndCurrentQuestionIdForPageRules(
			pageRuleList, currentQuestionId);
	    }
	    if (wizardTopic.getGroupRuleList() != null) {
		isParent = compareParentsIdAndCurrentQuestionIdForTopicRules(
			wizardTopic.getGroupRuleList(), currentQuestionId);
	    }
	    if (wizardQuestion.getQuestionRuleList() != null) {
		isParent = compareParentsIdAndCurrentQuestionIdForQuestionRules(
			wizardQuestion.getQuestionRuleList(), currentQuestionId);
	    }
	    if (isParent) {
		break;
	    }
	}
	return isParent;
    }

    private boolean compareParentsIdAndCurrentQuestionIdForQuestionRules(
	    List<QuestionRule> ruleList, String questionId) {
	boolean match = false;
	for (QuestionRule rule : ruleList) {
	    if (rule.getParentId().equals(questionId)) {
		match = true;
	    }
	}
	return match;
    }

    private boolean compareParentsIdAndCurrentQuestionIdForTopicRules(
	    List<GroupRule> ruleList, String questionId) {
	boolean match = false;
	for (GroupRule rule : ruleList) {
	    if (rule.getParentId().equals(questionId)) {
		match = true;
	    }
	}
	return match;
    }

    private boolean compareParentsIdAndCurrentQuestionIdForPageRules(
	    List<PageRule> ruleList, String questionId) {
	boolean match = false;
	for (PageRule rule : ruleList) {
	    if (rule.getParentId().equals(questionId)) {
		match = true;
	    }
	}
	return match;
    }

    public void moveLimitIfNecessary(int pageNumber, int topicNumber) {
	boolean movePageLimit = getWizardForm().getPageLimit() > pageNumber;
	boolean moveTopicLimit = getWizardForm().getTopicLimit() > topicNumber;
	getWizardForm().setPageLimit(pageNumber);
	getWizardForm().setTopicLimit(topicNumber);
	if (moveTopicLimit) {
	    changeStyleOfCurrentTopicButton(WizardComponentStyles.STYLE_TOPIC_BUTTON_SELECTED);
	    RequestContext.getCurrentInstance().update("leftmenuid-leftMenu");
	    if (movePageLimit) {
		changeStyleOfCurrentPageButton(WizardComponentStyles.STYLE_PAGE_BUTTON_SELECTED);
		RequestContext.getCurrentInstance().update(
			"dateStubb-breadcrumb");
	    }
	    RequestContext.getCurrentInstance().execute(
		    "dialogDependentQuestion.show()");
	}
    }

    private WizardForm getWizardForm() {
	return navigationData.getWizardForm();
    }

    @PreDestroy
    public void clearFiles() {
	if (filesForArchive != null) {
	    filesForArchive.add(new File(ArchiverZip.ZIP_FILE_NAME));
	    for (File file : filesForArchive) {
		file.delete();
	    }
	}
	for (String path : xmlPathList) {
	    new File(path).delete();
	}
    }

    public TimeHandler getTimeHandler() {
	return timeHandler;
    }

    public void setTimeHandler(TimeHandler timeHandler) {
	this.timeHandler = timeHandler;
    }
}