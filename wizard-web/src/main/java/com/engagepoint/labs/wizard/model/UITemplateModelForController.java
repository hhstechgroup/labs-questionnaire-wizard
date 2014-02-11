package com.engagepoint.labs.wizard.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.xml.bind.JAXBException;

import org.primefaces.model.MenuModel;
import org.xml.sax.SAXException;

import com.engagepoint.labs.wizard.bean.WizardDocument;
import com.engagepoint.labs.wizard.bean.WizardForm;
import com.engagepoint.labs.wizard.model.data.Page;
import com.engagepoint.labs.wizard.ui.UIBasicQuestion;
import com.engagepoint.labs.wizard.xml.controllers.XmlController;

@Named("uiTemplateModelForController")
@SessionScoped
public class UITemplateModelForController implements Serializable {

    private static final long serialVersionUID = -3879860102027220266L;

    private ArrayList<Page> document;

    // NavData

    private int currentWizardFormID;

    private String selectedFormTemplate;
    private String currentFormName;
    private String currentFormID;

    private int currPage;
    private int currTopic;
    
 // BreadCrumb
    private MenuModel breadcrumb_model;

    // CurrentUIComponents
    private ArrayList<UIBasicQuestion> currentUIquestions;
    private ArrayList<String> currentTopicIDs;
    private ArrayList<String> currentTopicTitles;

    private WizardDocument wizardDocument;
    private XmlController xmlController;

    // only for our default xml files!
    private List<String> XMLpathList;
    private Map<String, String> MapOfWizardForms;

    private boolean needRefresh;

    @PostConstruct
    public void init() {

	setMapOfWizardForms(new LinkedHashMap<String, String>());
	// MapOfWizardForms = new LinkedHashMap<>();
	setXMLpathList(new ArrayList<String>());
	// XMLpathList = new ArrayList<>();
	getXMLpathList().add("/XMLforWizard.xml");
	// XMLpathList.add("/XMLforWizard.xml");
	getXMLpathList().add("/XMLforWizard2.xml");
	// XMLpathList.add("/XMLforWizard2.xml");
	setXmlController(new XmlController());
	// xmlController = new XmlController();
	try {
	    setWizardDocument(getXmlController().readAllDeafultXmlFiles(getXMLpathList()));
	} catch (SAXException | JAXBException ex) {
	    Logger.getLogger(UITemplateModelForController.class.getName()).log(Level.SEVERE, null, ex);
	}

	for (WizardForm wForm : getWizardDocument().getFormList()) {
	    getMapOfWizardForms().put(wForm.getFormName(), wForm.getId());
	}

	needRefresh = false;

	setCurrPage(1);
	setCurrTopic(1);

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

    public int getID(String topic_id) {
	Pattern p = Pattern.compile("(\\d+)(?!.*\\d)");
	Matcher m = p.matcher(topic_id);
	int result = 0;
	if (m.find()) {
	    result = Integer.parseInt(m.group(1));
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

    public int getCurrentWizardFormID() {
	return currentWizardFormID;
    }

    public void setCurrentWizardFormID(int currentWizardFormID2) {
	this.currentWizardFormID = currentWizardFormID2;
    }

    public String getCurrentFormName() {
	return currentFormName;
    }

    public void setCurrentFormName(String currentFormName) {
	this.currentFormName = currentFormName;
    }

    public String getCurrentFormID() {
	return currentFormID;
    }

    public void setCurrentFormID(String currentFormID) {
	this.currentFormID = currentFormID;
    }

    public String getSelectedFormTemplate() {
	return selectedFormTemplate;
    }

    public void setSelectedFormTemplate(String selectedFormTemplate) {
	this.selectedFormTemplate = selectedFormTemplate;
    }

    public WizardDocument getWizardDocument() {
	return wizardDocument;
    }

    public void setWizardDocument(WizardDocument wizardDocument) {
	this.wizardDocument = wizardDocument;
    }

    public List<String> getXMLpathList() {
	return XMLpathList;
    }

    public void setXMLpathList(List<String> xMLpathList) {
	XMLpathList = xMLpathList;
    }

    public Map<String, String> getMapOfWizardForms() {
	return MapOfWizardForms;
    }

    public void setMapOfWizardForms(Map<String, String> mapOfWizardForms) {
	MapOfWizardForms = mapOfWizardForms;
    }

    public XmlController getXmlController() {
	return xmlController;
    }

    public void setXmlController(XmlController xmlController) {
	this.xmlController = xmlController;
    }

    public MenuModel getBreadcrumb_model() {
	return breadcrumb_model;
    }

    public void setBreadcrumb_model(MenuModel breadcrumb_model) {
	this.breadcrumb_model = breadcrumb_model;
    }
}