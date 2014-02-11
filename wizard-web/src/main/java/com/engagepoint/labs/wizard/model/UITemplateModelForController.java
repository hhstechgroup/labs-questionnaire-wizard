package com.engagepoint.labs.wizard.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

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
    private ArrayList<String> currentTopicIDs;
    private ArrayList<String> currentTopicTitles;

    private boolean needRefresh;

    @PostConstruct
    public void init() {
	needRefresh = false;

	setCurrPage(0);
	setCurrTopic(0);

	setCurrentUIquestions(new ArrayList<UIBasicQuestion>());
	setCurrentTopicIDs(new ArrayList<String>());
	setCurrentTopicTitles(new ArrayList<String>());

	setDocument(new ArrayList<Page>());
	int count = 3 + (int) (Math.random() * ((10 - 3) + 1));
	for (int i = 0; i < count; i++) {

	    getDocument().add(new Page(i));
	}
    }

    public String getTitleFromID(String topic_id) {
	Pattern p = Pattern.compile("(\\d+)(?!.*\\d)");
	Matcher m = p.matcher(topic_id);
	String result = "1";
	if (m.find()) {
	    result = m.group(1);
	    int index = Integer.parseInt(result);
	    result = currentTopicTitles.get(index - 1);
	}
	return result;
    }

    public ArrayList<Page> getDocument() {
	return document;
    }

    public void setDocument(ArrayList<Page> document) {
	this.document = document;
    }

    public int getCurrentPage() {
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

    public boolean isNeedRefresh() {
	return needRefresh;
    }

    public void setNeedRefresh(boolean needRefresh) {
	this.needRefresh = needRefresh;
    }

    public ArrayList<String> getCurrentTopicIDs() {
	return currentTopicIDs;
    }

    public void setCurrentTopicIDs(ArrayList<String> currentTopicIDs) {
	this.currentTopicIDs = currentTopicIDs;
    }

    public ArrayList<String> getCurrentTopicTitles() {
	return currentTopicTitles;
    }

    public void setCurrentTopicTitles(ArrayList<String> currentTopicTitles) {
	this.currentTopicTitles = currentTopicTitles;
    }
}