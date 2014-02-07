package com.engagepoint.labs.wizard.controller;

import java.io.Serializable;
import java.sql.Array;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.primefaces.component.breadcrumb.BreadCrumb;
import org.primefaces.component.menu.Menu;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;

import com.engagepoint.labs.wizard.ui.UIBasicQuestion;
import com.engagepoint.labs.wizard.ui.UIMultipleChoiceQuestion;
import com.engagepoint.labs.wizard.ui.UITextAreaQuestion;
import com.engagepoint.labs.wizard.ui.UITextQuestion;
import com.engagepoint.labs.wizard.ui.UITimeQuestion;

@Named("uiTemplateController")
@SessionScoped
public class UITemplateController implements Serializable {

    private static final long serialVersionUID = 7470581070941487130L;

    // NavData
    private int currPage;
    private int currTopic;
    
    private final String FORM_MENU = "form_menu";
    private final String FORM_CONTENT = "form_content";

    // CurrentUIComponents
    private ArrayList<UIBasicQuestion> currentUIquestions;
    private ArrayList<MenuItem> currentMenuItems;

    // BreadCrumb
    private BreadCrumb breadcrumb;
    private MenuModel breadcrumb_model;

    // Menu
    private Menu menu;
    private MenuModel menu_model;

    // Questions
    private HtmlForm content;

    // FacesData
    private FacesContext facesCtx;
    private ELContext elCtx;
    private ExpressionFactory expFact;

    // TODO: Test
    private ArrayList<Page> document;
    private ArrayList<String> defaultStrings;
    private ArrayList<Integer> defaultInts;

    @PostConstruct
    public void init() {

	currPage = 0;
	currTopic = 0;

	breadcrumb = new BreadCrumb();
	setBreadcrumb_model(new DefaultMenuModel());

	menu = new Menu();
	setMenu_model(new DefaultMenuModel());

	content = new HtmlForm();

	currentUIquestions = new ArrayList<UIBasicQuestion>();
	currentMenuItems = new ArrayList<MenuItem>();

	// TODO: Test
	document = new ArrayList<Page>();
	int count = 3 + (int) (Math.random() * ((10 - 3) + 1));
	for (int i = 0; i < count; i++) {
	    document.add(new Page(i));
	}

	defaultStrings = new ArrayList<String>();
	defaultInts = new ArrayList<Integer>();
	for (int i = 0; i < 4; i++) {
	    defaultInts.add(new Integer(i));
	    defaultStrings.add(new String("Option" + i));
	}

	facesCtx = FacesContext.getCurrentInstance();
	elCtx = facesCtx.getELContext();
	expFact = facesCtx.getApplication().getExpressionFactory();

	facesCtx.getPartialViewContext().getRenderIds().add(FORM_MENU);
	facesCtx.getPartialViewContext().getRenderIds().add(FORM_CONTENT);

	// Insert data to menus
	populateBreadcrumb();
	populateMenu();
    }

    private void populateBreadcrumb() {

	for (int i = 0; i < getPageCount(); i++) {
	    MenuItem item = new MenuItem();
	    MethodExpression expr;

	    item.setValue("Page " + (i + 1));

	    expr = expFact.createMethodExpression(elCtx,
		    "#{uiTemplateController.chCurrPage(" + i + ")}", void.class,
		    new Class[] { int.class });

	    item.setActionExpression(expr);
	    getBreadcrumb_model().addMenuItem(item);
	}

	breadcrumb.setModel(getBreadcrumb_model());
    }

    private void populateMenu() {

	currentMenuItems.clear();
	menu.getChildren().clear();
	menu_model = new DefaultMenuModel();

	for (int i = 0; i < getGroupCount(currPage); i++) {
	    MenuItem item = new MenuItem();
	    MethodExpression expr;

	    item.setValue("Group " + (i + 1));

	    expr = expFact.createMethodExpression(elCtx,
		    "#{uiTemplateController.chCurrTopic(" + i + ")}", void.class,
		    new Class[] { int.class });

	    item.setActionExpression(expr);
	    menu_model.addMenuItem(item);
	    currentMenuItems.add(item);
	}

	menu.setModel(menu_model);

	createQuestions();
    }

    private void createQuestions() {

	currentUIquestions.clear();

	UIBasicQuestion q1 = new UITextQuestion((currPage + 1) + " - " + (currTopic + 1));

	currentUIquestions.add(q1);

	// TODO: get model data here and convert to UIComponents

	populateUIquestions();
    }

    private void populateUIquestions() {

	content.getChildren().clear();
	for (int i = 0; i < currentUIquestions.size(); i++) {
	    currentUIquestions.get(i).postInit();
	    content.getChildren().add(currentUIquestions.get(i).getUiComponent());
	    HtmlOutputText linebreak = new HtmlOutputText();
	    linebreak.setValue("<br/>");
	    linebreak.setEscape(false);
	    content.getChildren().add(linebreak);
	}

	RequestContext.getCurrentInstance().update(FORM_CONTENT);
	RequestContext.getCurrentInstance().update(FORM_MENU);
    }

    public BreadCrumb getBreadcrumb() {
	return breadcrumb;
    }

    public void setBreadcrumb(BreadCrumb breadcrumb) {
	this.breadcrumb = breadcrumb;
    }

    private int getPageCount() {
	return document.size();
    }

    private int getGroupCount(int p_id) {
	return document.get(p_id).getGroups().size();
    }

    private int qetQuestionsCount(int p_id, int g_id) {
	return document.get(p_id).getGroups().get(g_id).getQuestions().size();
    }

    public MenuModel getBreadcrumb_model() {
	return breadcrumb_model;
    }

    public void setBreadcrumb_model(MenuModel breadcrumb_model) {
	this.breadcrumb_model = breadcrumb_model;
    }

    public Menu getMenu() {
	return menu;
    }

    public void setMenu(Menu menu) {
	this.menu = menu;
    }

    public void navigate(String p_id) {
	System.out.println(p_id);
    }

    public MenuModel getMenu_model() {
	return menu_model;
    }

    public void setMenu_model(MenuModel menu_model) {
	this.menu_model = menu_model;
    }

    public void chCurrPage(int currPage) {
	System.out.println("Curr page set to: " + currPage);
	System.out.println("Curr group set to: " + currTopic);
	this.currPage = currPage;
	this.currTopic = 0;
	populateMenu();
    }

    public void chCurrTopic(int currTopic) {
	System.out.println("Curr page set to: " + currPage);
	System.out.println("Curr group set to: " + currTopic);
	this.currTopic = currTopic;
	createQuestions();
    }

    public HtmlForm getcontent() {
	return content;
    }

    public void setcontent(HtmlForm content) {
	this.content = content;
    }

}

// ///////////////////////////////////////////////
// ///////////////////////////////////////////////
// ///////////////////////////////////////////////

class Page {
    private String name = "Page";
    private ArrayList<Group> groups;

    private int count = 3 + (int) (Math.random() * ((10 - 3) + 1));

    Page(int id) {
	setName(getName() + id + 1);
	setGroups(new ArrayList<Group>());

	for (int i = 0; i < count; i++) {
	    getGroups().add(new Group(id, i));
	}
    }

    public ArrayList<Group> getGroups() {
	return groups;
    }

    public void setGroups(ArrayList<Group> groups) {
	this.groups = groups;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }
}

class Group {
    private String name = "Group";
    private ArrayList<Question> questions;

    private int count = 3 + (int) (Math.random() * ((10 - 3) + 1));

    Group(int p_id, int id) {
	setName(getName() + id + 1);
	setQuestions(new ArrayList<Question>());

	for (int i = 0; i < count; i++) {
	    getQuestions().add(new Question(p_id, id, i));
	}
    }

    public ArrayList<Question> getQuestions() {
	return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
	this.questions = questions;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }
}

class Question {
    private String name = "Question";

    Question(int p_id, int gr_id, int id) {
	setName(getName() + id + 1);
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }
}
