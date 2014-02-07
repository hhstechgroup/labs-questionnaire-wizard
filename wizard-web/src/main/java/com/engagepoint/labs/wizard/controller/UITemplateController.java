package com.engagepoint.labs.wizard.controller;

import java.io.Serializable;
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

    private HtmlForm dynaform;

    private int currPage;
    private int currTopic;
    private ArrayList<UIBasicQuestion> currentUIBasicComponents;

    private BreadCrumb breadcrumb;
    private MenuModel breadcrumb_model;

    private Menu menu;
    private MenuModel menu_model;

    private ArrayList<Page> document;

    private FacesContext facesCtx;
    private ELContext elCtx;
    private ExpressionFactory expFact;

    // TODO: Test
    private ArrayList<String> defaultStrings;
    private ArrayList<Integer> defaultInts;

    @PostConstruct
    public void init() {

	breadcrumb = new BreadCrumb();
	setBreadcrumb_model(new DefaultMenuModel());

	menu = new Menu();
	setMenu_model(new DefaultMenuModel());

	document = new ArrayList<Page>();
	int count = 3 + (int) (Math.random() * ((10 - 3) + 1));
	for (int i = 0; i < count; i++) {
	    document.add(new Page(i));
	}

	// TODO: check this
	facesCtx = FacesContext.getCurrentInstance();
	elCtx = facesCtx.getELContext();
	expFact = facesCtx.getApplication().getExpressionFactory();

	currentUIBasicComponents = new ArrayList<UIBasicQuestion>();

	// TODO: Test

	defaultStrings = new ArrayList<String>();
	defaultInts = new ArrayList<Integer>();
	for (int i = 0; i < 4; i++) {
	    defaultInts.add(new Integer(i));
	    defaultStrings.add(new String("Option" + i));
	}

	populateBreadcrumb();
	populateMenu();
    }

    private void populateBreadcrumb() {

	for (int i = 0; i < getPageCount(); i++) {
	    MenuItem item = new MenuItem();
	    MethodExpression expr;

	    item.setValue("Page " + i);

	    expr = expFact.createMethodExpression(elCtx,
		    "#{uiTemplateController.chCurrPage(" + i + ")}", void.class,
		    new Class[] { int.class });

	    item.setActionExpression(expr);
	    getBreadcrumb_model().addMenuItem(item);
	}

	breadcrumb.setModel(getBreadcrumb_model());
    }

    private void populateMenu() {

	for (int i = 0; i < getGroupCount(currPage); i++) {
	    MenuItem item = new MenuItem();
	    MethodExpression expr;

	    item.setValue("Group " + i);

	    expr = expFact.createMethodExpression(elCtx,
		    "#{uiTemplateController.chCurrGroup(" + i + ")}", void.class,
		    new Class[] { int.class });

	    item.setActionExpression(expr);
	    getMenu_model().addMenuItem(item);
	}

	menu.setModel(getMenu_model());
    }

    private void reloadMenu() {
	System.out.println("Hi!");
	populateCurrentUIComponents();
	// FacesContext facesCtx = FacesContext.getCurrentInstance();
	// ELContext elCtx = facesCtx.getELContext();
	// ExpressionFactory expFact =
	// facesCtx.getApplication().getExpressionFactory();
	// for (int i = 0; i < getPageCount(); i++) {
	// MenuItem item = new MenuItem();
	// item.setValue(document.get(i).getName());
	// item.setActionExpression(expFact.createMethodExpression(elCtx,
	// "#{uiTemplateController.navigate(value)}", null, new Class[1]));
	// getBreadcrumb_model().addMenuItem(item);
	// }
	// breadcrumb.setModel(getBreadcrumb_model());
    }

    private void populateCurrentUIComponents() {

	UIBasicQuestion q1 = new UITextQuestion(currPage + " - " + currTopic);
	UIBasicQuestion q2 = new UITextAreaQuestion(currPage + " - " + currTopic);
	UIBasicQuestion q3 = new UITimeQuestion(currPage + " - " + currTopic);
	UIBasicQuestion q4 = new UIMultipleChoiceQuestion((currPage + " - " + currTopic),
		defaultStrings, defaultInts);

	currentUIBasicComponents.add(q1);
	currentUIBasicComponents.add(q2);
	currentUIBasicComponents.add(q3);
	currentUIBasicComponents.add(q4);

	// TODO: get model data here and convert to UIComponents
	// wakes up within button from left menu with help of currentPointer

	reloadDynaForm();
    }

    private void reloadDynaForm() {
	dynaform.getChildren().clear();
	for (int i = 0; i < currentUIBasicComponents.size(); i++) {
	    currentUIBasicComponents.get(i).postInit();
	    dynaform.getChildren().add(currentUIBasicComponents.get(i).getUiComponent());
	    HtmlOutputText linebreak = new HtmlOutputText();
	    linebreak.setValue("<br/>");
	    linebreak.setEscape(false);
	    dynaform.getChildren().add(linebreak);
	}
	FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("dynaform");
	RequestContext.getCurrentInstance().update("dynaform");
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
	this.currPage = currPage;
	this.currTopic = 1;
	reloadMenu();
	System.out.println("Curr page set to: " + currPage);
	System.out.println("Curr group set to: " + currTopic);
    }

    public void chCurrGroup(int currGroup) {
	this.currTopic = currGroup;
	reloadMenu();
	System.out.println("Curr page set to: " + currPage);
	System.out.println("Curr group set to: " + currGroup);
    }

    public HtmlForm getDynaform() {
	if (dynaform == null) {
	    dynaform = new HtmlForm();
	}
	return dynaform;
    }

    public void setDynaform(HtmlForm dynaform) {
	this.dynaform = dynaform;
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
	setName(getName() + id);
	setGroups(new ArrayList<Group>());

	for (int i = 0; i < count; i++) {
	    getGroups().add(new Group(id, i + 1));
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
	setName(getName() + id);
	setQuestions(new ArrayList<Question>());

	for (int i = 0; i < count; i++) {
	    getQuestions().add(new Question(p_id, id, i + 1));
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
	setName(getName() + p_id + gr_id + id);
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }
}
