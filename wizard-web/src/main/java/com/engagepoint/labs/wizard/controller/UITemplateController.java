package com.engagepoint.labs.wizard.controller;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.component.breadcrumb.BreadCrumb;
import org.primefaces.component.menu.Menu;
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
    UITemplateModelForController templateModel;

    private static final long serialVersionUID = 7470581070941487130L;

    private final String FORM_MENU = "form_menu";
    private final String FORM_CONTENT = "form_content";

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

    @PostConstruct
    public void init() {

	breadcrumb = new BreadCrumb();
	setBreadcrumb_model(new DefaultMenuModel());

	menu = new Menu();
	setMenu_model(new DefaultMenuModel());

	content = new HtmlForm();

	// facesCtx = FacesContext.getCurrentInstance();
	// elCtx = facesCtx.getELContext();
	// expFact = facesCtx.getApplication().getExpressionFactory();

	// facesCtx.getPartialViewContext().getRenderIds().add(FORM_MENU);
	// facesCtx.getPartialViewContext().getRenderIds().add(FORM_CONTENT);

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

	    item.setValue("Page " + (i + 1));

	    expr = expFact.createMethodExpression(elCtx, "#{uiTemplateController.chCurrPage(" + i + ")}", void.class,
		    new Class[] { int.class });

	    item.setActionExpression(expr);
	    getBreadcrumb_model().addMenuItem(item);
	}

	breadcrumb.setModel(getBreadcrumb_model());
    }

    private void populateMenu() {

	facesCtx = FacesContext.getCurrentInstance();
	elCtx = facesCtx.getELContext();
	expFact = facesCtx.getApplication().getExpressionFactory();

	templateModel.getCurrentMenuItems().clear();
	menu.getChildren().clear();
	menu_model = new DefaultMenuModel();

	for (int i = 0; i < getTopicCount(templateModel.getCurrPage()); i++) {
	    MenuItem item = new MenuItem();
	    MethodExpression expr;

	    item.setValue("Topic " + (i + 1));

	    expr = expFact.createMethodExpression(elCtx, "#{uiTemplateController.chCurrTopic(" + i + ")}", void.class,
		    new Class[] { int.class });

	    item.setActionExpression(expr);
	    item.setId("topic_"+i);
	    menu_model.addMenuItem(item);
	    templateModel.getCurrentMenuItems().add(item);
	}

	menu.setModel(menu_model);

	createQuestions();
    }

    private void createQuestions() {

	templateModel.getCurrentUIquestions().clear();

	UIBasicQuestion q1 = new UITextQuestion((templateModel.getCurrPage() + 1) + " - "
		+ (templateModel.getCurrTopic() + 1));

	templateModel.getCurrentUIquestions().add(q1);

	// TODO: get model data here and convert to UIComponents

	populateUIquestions();
    }

    private void populateUIquestions() {

	facesCtx = FacesContext.getCurrentInstance();
	elCtx = facesCtx.getELContext();
	expFact = facesCtx.getApplication().getExpressionFactory();

	content.getChildren().clear();
	for (int i = 0; i < templateModel.getCurrentUIquestions().size(); i++) {
	    templateModel.getCurrentUIquestions().get(i).postInit();
	    content.getChildren().add(templateModel.getCurrentUIquestions().get(i).getUiComponent());
	    HtmlOutputText linebreak = new HtmlOutputText();
	    linebreak.setValue("<br/>");
	    linebreak.setEscape(false);
	    content.getChildren().add(linebreak);
	}

//	FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add(FORM_MENU);
//	FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add(FORM_CONTENT);
	
	templateModel.setNeedRefresh(true);

    }

    public BreadCrumb getBreadcrumb() {
	return breadcrumb;
    }

    public void setBreadcrumb(BreadCrumb breadcrumb) {
	this.breadcrumb = breadcrumb;
    }

    private int getPageCount() {
	return templateModel.getDocument().size();
    }

    private int getTopicCount(int p_id) {
	return templateModel.getDocument().get(p_id).getTopics().size();
    }

    private int qetQuestionsCount(int p_id, int g_id) {
	return templateModel.getDocument().get(p_id).getTopics().get(g_id).getQuestions().size();
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
	System.out.println("Curr group set to: " + templateModel.getCurrTopic());
	templateModel.setCurrPage(currPage);
	templateModel.setCurrTopic(0);
	populateMenu();
    }

    public void chCurrTopic(int currTopic) {
	System.out.println("Curr page set to: " + templateModel.getCurrPage());
	System.out.println("Curr group set to: " + currTopic);
	templateModel.setCurrTopic(currTopic);
	createQuestions();
    }

    public HtmlForm getcontent() {
	return content;
    }

    public void setcontent(HtmlForm content) {
	this.content = content;
    }

}
