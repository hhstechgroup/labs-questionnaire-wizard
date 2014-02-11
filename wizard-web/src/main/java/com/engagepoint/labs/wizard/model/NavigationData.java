package com.engagepoint.labs.wizard.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlOutputText;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBException;

import org.primefaces.model.DefaultMenuModel;
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
    private boolean onSelectXMLPage;

    @Inject
    private WizardForm wizardForm;
    // NavData
    private String selectedFormTemplate;
    private String currentFormName;
    private String currentPageID;
    private String currentTopicID;

    private String currentPageTitle;
    private String currentTopicTitle;

    // UI elements
    private MenuModel breadcrumb_model;

    // Binding on form in maincontent.xhtml
    private HtmlForm mainContentForm;

    private ArrayList<String> currentTopicIDs;
    private ArrayList<String> currentTopicTitles;
    private HtmlOutputText currentOutputText;

    // Wizard XML items
    private WizardDocument wizardDocument;
    private XmlController xmlController;

    // only for our default xml files!
    private List<String> XMLpathList;
    private Map<String, String> MapOfWizardForms;

    // Initial construction, only for bootstrapwelcome.xhtml

    @PostConstruct
    public void startSelectXMLScreen() {

	onSelectXMLPage = true;

	MapOfWizardForms = new LinkedHashMap<String, String>();

	XMLpathList = new ArrayList<String>();

	XMLpathList.add("/XMLforWizard.xml");
	XMLpathList.add("/XMLforWizard2.xml");

	xmlController = new XmlController();

	try {
	    wizardDocument = xmlController.readAllDeafultXmlFiles(XMLpathList);
	} catch (SAXException | JAXBException ex) {
	    Logger.getLogger(NavigationData.class.getName()).log(Level.SEVERE, null, ex);
	}

	for (WizardForm wForm : wizardDocument.getFormList()) {
	    MapOfWizardForms.put(wForm.getFormName(), wForm.getId());
	}
    }

    public void startWizard() {
	mainContentForm = new HtmlForm();

	wizardDocument.getWizardFormByID(selectedFormTemplate, wizardForm, wizardDocument.getFormList());

	needRefresh = false;

	setCurrentPageID(wizardForm.getWizardPageList().get(0).getId());
	setCurrentTopicID(wizardForm.getWizardPageById(currentPageID).getTopicList().get(0).getId());

	setCurrentTopicIDs(new ArrayList<String>());
	setCurrentTopicTitles(new ArrayList<String>());

	setBreadcrumb_model(new DefaultMenuModel());
    }

    public String getTopicTitleFromID(String topicID) {
	String title;

	title = wizardForm.getWizardTopicById(topicID).getGroupTitle();

	return title;
    }

    public String getCurrentPageID() {
	return currentPageID;
    }

    public void setCurrentPageID(String currentPageID) {
	this.currentPageID = currentPageID;
	this.currentPageTitle = wizardForm.getWizardPageById(currentPageID).getPageNumber().toString();
    }

    public String getCurrentTopicID() {
	return currentTopicID;
    }

    public void setCurrentTopicID(String currentTopicID) {
	this.currentTopicID = currentTopicID;
	this.currentTopicTitle = getTopicTitleFromID(currentTopicID);
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

    public HtmlForm getMainContentForm() {
	return mainContentForm;
    }

    public void setMainContentForm(HtmlForm content) {
	this.mainContentForm = content;
    }

    public WizardForm getWizardForm() {
	return wizardForm;
    }

    public void setWizardForm(WizardForm wizardForm) {
	this.wizardForm = wizardForm;
    }

    public Map<String, String> getXmlsValues() {
	return MapOfWizardForms;
    }

    public boolean isOnSelectXMLPage() {
	return onSelectXMLPage;
    }

    public void setOnSelectXMLPage(boolean onSelectXMLPage) {
	this.onSelectXMLPage = onSelectXMLPage;
    }

    public HtmlOutputText getCurrentOutputText() {
	return currentOutputText;
    }

    public void setCurrentOutputText(HtmlOutputText currentOutputText) {
	this.currentOutputText = currentOutputText;
    }

    public String getCurrentPageTitle() {
	return currentPageTitle;
    }

    public void setCurrentPageTitle(String currentPageTitle) {
	this.currentPageTitle = currentPageTitle;
    }

    public String getCurrentTopicTitle() {
	return currentTopicTitle;
    }

    public void setCurrentTopicTitle(String currentTopicTitle) {
	this.currentTopicTitle = currentTopicTitle;
    }
}