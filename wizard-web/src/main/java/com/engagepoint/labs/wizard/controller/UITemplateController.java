package com.engagepoint.labs.wizard.controller;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;

import com.engagepoint.labs.wizard.model.UITemplateModelForController;
import com.engagepoint.labs.wizard.ui.UIBasicQuestion;
import com.engagepoint.labs.wizard.ui.UITextQuestion;

@Named("uiTemplateController")
@RequestScoped
public class UITemplateController implements Serializable {

    @Inject
    private UITemplateModelForController templateModel;

    private static final long serialVersionUID = 7470581070941487130L;


    // BreadCrumb
    private MenuModel breadcrumb_model;

    // Questions
    private HtmlForm content;

    // FacesData
    private FacesContext facesCtx;
    private ELContext elCtx;
    private ExpressionFactory expFact;

    @PostConstruct
    public void init() {

	// breadcrumb = new BreadCrumb();
	setBreadcrumb_model(new DefaultMenuModel());

	content = new HtmlForm();

	// Insert data to menus
	populateBreadcrumb();
	populateMenu();
    }

    private void populateBreadcrumb() {

	facesCtx = FacesContext.getCurrentInstance();
	elCtx = facesCtx.getELContext();
	expFact = facesCtx.getApplication().getExpressionFactory();

	for (int i = 0; i < getPageCount(); i++) {
	    MenuItem item = new MenuItem();
	    MethodExpression expr;

	    item.setValue("Page" + (i + 1));

	    expr = expFact.createMethodExpression(elCtx,
		    "#{uiTemplateController.changeCurrentPage(" + i + ")}", void.class,
		    new Class[] { int.class });

	    item.setActionExpression(expr);
	    getBreadcrumb_model().addMenuItem(item);
	}

	// breadcrumb.setModel(getBreadcrumb_model());
    }

    private void populateMenu() {

	facesCtx = FacesContext.getCurrentInstance();
	elCtx = facesCtx.getELContext();
	expFact = facesCtx.getApplication().getExpressionFactory();

	templateModel.setCurrentMenuElements(new ArrayList<String>());
	templateModel.getCurrentMenuElements().clear();

	for (int i = 0; i < getTopicCount(getTemplateModel().getCurrentPage()); i++) {
	    String str = "Topic" + (i + 1);
	    templateModel.getCurrentMenuElements().add(str);
	}

	createQuestions();
    }

    private void createQuestions() {

	getTemplateModel().getCurrentUIquestions().clear();

	UIBasicQuestion q1 = new UITextQuestion((getTemplateModel().getCurrentPage() + 1)
		+ " - " + (getTemplateModel().getCurrTopic() + 1));

	getTemplateModel().getCurrentUIquestions().add(q1);

	// TODO: get model data here and convert to UIComponents

	populateUIquestions();
    }

    private void populateUIquestions() {

	facesCtx = FacesContext.getCurrentInstance();
	elCtx = facesCtx.getELContext();
	expFact = facesCtx.getApplication().getExpressionFactory();

	content.getChildren().clear();
	for (int i = 0; i < getTemplateModel().getCurrentUIquestions().size(); i++) {
	    getTemplateModel().getCurrentUIquestions().get(i).postInit();
	    content.getChildren().add(
		    getTemplateModel().getCurrentUIquestions().get(i).getUiComponent());
	    HtmlOutputText linebreak = new HtmlOutputText();
	    linebreak.setValue("<br/>");
	    linebreak.setEscape(false);
	    content.getChildren().add(linebreak);
	}

	getTemplateModel().setNeedRefresh(true);

    }

    private int getPageCount() {
	return getTemplateModel().getDocument().size();
    }

    private int getTopicCount(int p_id) {
	return getTemplateModel().getDocument().get(p_id).getTopics().size();
    }

    public MenuModel getBreadcrumb_model() {
	return breadcrumb_model;
    }

    public void setBreadcrumb_model(MenuModel breadcrumb_model) {
	this.breadcrumb_model = breadcrumb_model;
    }

    public void navigate(String p_id) {
	System.out.println(p_id);
    }

    public void changeCurrentPage(int currPage) {
	System.out.println("Curr page set to: " + currPage);
	System.out.println("Curr group set to: " + getTemplateModel().getCurrTopic());
	getTemplateModel().setCurrPage(currPage);
	getTemplateModel().setCurrTopic(0);
	populateMenu();
    }

    public void changeCurrentTopic(String currTopic) {
	System.out.println("Curr page set to: " + getTemplateModel().getCurrentPage());
	System.out.println("Curr group set to: " + currTopic);
	getTemplateModel().setCurrTopic(1);
	createQuestions();
    }

    public HtmlForm getcontent() {
	return content;
    }

    public void setcontent(HtmlForm content) {
	this.content = content;
    }

    public UITemplateModelForController getTemplateModel() {
	return templateModel;
    }

    public void setTemplateModel(UITemplateModelForController templateModel) {
	this.templateModel = templateModel;
    }

}
