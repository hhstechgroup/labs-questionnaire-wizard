package com.engagepoint.labs.wizard.controller;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.primefaces.component.breadcrumb.BreadCrumb;
import org.primefaces.component.menu.Menu;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;

@Named("uiTemplateController")
@SessionScoped
public class UITemplateController implements Serializable {

    private static final long serialVersionUID = 7470581070941487130L;

    private BreadCrumb breadcrumb;
    private MenuModel breadcrumb_model;

    private Menu menu;
    private MenuModel menu_model;

    private ArrayList<Page> document;

    @PostConstruct
    public void init() {
	breadcrumb = new BreadCrumb();
	setBreadcrumb_model(new DefaultMenuModel());

	menu = new Menu();
	menu_model = new DefaultMenuModel();

	document = new ArrayList<Page>();
	int count = 3 + (int) (Math.random() * ((10 - 3) + 1));
	for (int i = 0; i < count; i++) {
	    document.add(new Page(i));
	}

	populateBreadcrumb();
    }

    private void populateBreadcrumb() {
	FacesContext facesCtx = FacesContext.getCurrentInstance();
	ELContext elCtx = facesCtx.getELContext();
	ExpressionFactory expFact = facesCtx.getApplication().getExpressionFactory();
	for (int i = 0; i < getPageCount(); i++) {
	    MenuItem item = new MenuItem();
	    item.setValue(document.get(i).getName());
	    item.setActionExpression(expFact.createMethodExpression(elCtx,
		    "#{uiTemplateController.populateMenu(value)}", void.class,
		    new Class[] { String.class }));
	    getBreadcrumb_model().addMenuItem(item);
	}
	breadcrumb.setModel(getBreadcrumb_model());
    }

    public void populateMenu(String p_id) {
	System.out.println(p_id);
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
