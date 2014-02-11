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
import javax.faces.component.html.HtmlForm;
import javax.inject.Named;
import javax.xml.bind.JAXBException;

import org.primefaces.model.MenuModel;
import org.xml.sax.SAXException;

import com.engagepoint.labs.wizard.bean.WizardDocument;
import com.engagepoint.labs.wizard.bean.WizardForm;
import com.engagepoint.labs.wizard.ui.UIBasicQuestion;
import com.engagepoint.labs.wizard.xml.controllers.XmlController;

@Named("navigationData")
@SessionScoped
public class NavigationData implements Serializable {

    private static final long serialVersionUID = -3879860102027220266L;

    private boolean needRefresh;

    // NavData
    private String selectedFormTemplate;
    private String currentFormName;
    private int currentFormID;
    private int currPage;
    private int currTopic;

    // UI elements
    private MenuModel breadcrumb_model;
    private HtmlForm content;

    // CurrentUIComponents
    private ArrayList<UIBasicQuestion> currentUIquestions;
    private ArrayList<String> currentTopicIDs;
    private ArrayList<String> currentTopicTitles;

    // Wizard XML items
    private WizardDocument wizardDocument;
    private XmlController xmlController;

    // only for our default xml files!
    private List<String> XMLpathList;
    private Map<String, String> MapOfWizardForms;

    // Initial construction, only for bootstrapwelcome.xhtml
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
	    Logger.getLogger(NavigationData.class.getName()).log(Level.SEVERE, null, ex);
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
    }

    // This method takes topicId (e.g Topic1, Topic999), extracts number and
    // return actual topic title from model with correct index
    public String getTitleFromID(String topicID) {
	Pattern p = Pattern.compile("(\\d+)(?!.*\\d)");
	Matcher m = p.matcher(topicID);
	String result = "1";
	if (m.find()) {
	    result = m.group(1);
	    int index = Integer.parseInt(result);
	    result = currentTopicTitles.get(index - 1);
	}
	return result;
    }

    // This method takes topicId (e.g Topic1, Topic999), extracts number and
    // returns topic number
    public int getID(String topicID) {
	Pattern p = Pattern.compile("(\\d+)(?!.*\\d)");
	Matcher m = p.matcher(topicID);
	int result = 0;
	if (m.find()) {
	    result = Integer.parseInt(m.group(1));
	}
	return result;
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

    public String getCurrentFormName() {
	return currentFormName;
    }

    public void setCurrentFormName(String currentFormName) {
	this.currentFormName = currentFormName;
    }

    public int getCurrentFormID() {
	return currentFormID;
    }

    public void setCurrentFormID(int currentFormID) {
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

    public HtmlForm getContent() {
	return content;
    }

    public void setContent(HtmlForm content) {
	this.content = content;
    }
}