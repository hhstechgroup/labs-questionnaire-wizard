package com.engagepoint.labs.wizard.model;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.primefaces.component.menuitem.MenuItem;

import com.engagepoint.labs.wizard.model.data.Page;
import com.engagepoint.labs.wizard.ui.UIBasicQuestion;

@Named("uiTemplateModelForController")
@SessionScoped
public class UITemplateModelForController implements Serializable {

    private static final long serialVersionUID = -3879860102027220266L;

    private ArrayList<Page> document;

    // NavData
    private int currPage;
    private int currTopic;

    // CurrentUIComponents
    private ArrayList<UIBasicQuestion> currentUIquestions;
    private ArrayList<MenuItem> currentMenuItems;
    
    private boolean needRefresh;

    @PostConstruct
    public void init() {
	needRefresh=false;
	
	setCurrPage(0);
	setCurrTopic(0);

	setCurrentUIquestions(new ArrayList<UIBasicQuestion>());
	setCurrentMenuItems(new ArrayList<MenuItem>());

	setDocument(new ArrayList<Page>());
	int count = 3 + (int) (Math.random() * ((10 - 3) + 1));
	for (int i = 0; i < count; i++) {
	    getDocument().add(new Page(i));
	}
    }

    public ArrayList<Page> getDocument() {
	return document;
    }

    public void setDocument(ArrayList<Page> document) {
	this.document = document;
    }

    public int getCurrPage() {
	return currPage;
    }

    public void setCurrPage(int currPage) {
	this.currPage = currPage;
    }

    public int getCurrTopic() {
	return currTopic;
    }

    public void setCurrTopic(int currTopic) {
	this.currTopic = currTopic;
    }

    public ArrayList<UIBasicQuestion> getCurrentUIquestions() {
	return currentUIquestions;
    }

    public void setCurrentUIquestions(ArrayList<UIBasicQuestion> currentUIquestions) {
	this.currentUIquestions = currentUIquestions;
    }

    public ArrayList<MenuItem> getCurrentMenuItems() {
	return currentMenuItems;
    }

    public void setCurrentMenuItems(ArrayList<MenuItem> currentMenuItems) {
	this.currentMenuItems = currentMenuItems;
    }

    public boolean isNeedRefresh() {
	return needRefresh;
    }

    public void setNeedRefresh(boolean needRefresh) {
	this.needRefresh = needRefresh;
    }
}