package com.engagepoint.labs.wizard.model;

import com.engagepoint.labs.wizard.bean.WizardDocument;
import com.engagepoint.labs.wizard.bean.WizardForm;
import com.engagepoint.labs.wizard.bean.WizardPage;
import com.engagepoint.labs.wizard.xml.controllers.XmlController;
import org.primefaces.component.button.Button;
import org.primefaces.component.panel.Panel;
import org.primefaces.component.panelgrid.PanelGrid;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.html.HtmlForm;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author vyacheslav.mysak Bean which helps UINavigationBean in storing
 *         navigation data - e.g. current page, current topic, current question
 *         list, etc.
 */
@Named("navigationData")
@SessionScoped
public class NavigationData implements Serializable {

    private static final long serialVersionUID = -3879860102027220266L;
    private boolean needRefresh;
    private boolean onSelectXMLPage;
    @Inject
    private WizardForm wizardForm;
    // Wizard XML items
    private WizardDocument wizardDocument;
    private XmlController xmlController;
    // only for our default xml files!
    private Map<String, String> MapOfWizardForms;
    // NavData
    private String selectedFormTemplate;

    private ArrayList<String> currentTopicIDs;
    private ArrayList<String> currentTopicTitles;

    private String currentFormName;
    private String currentPageID;
    private String currentTopicID;
    private String currentPageTitle;
    private String currentTopicTitle;
    private MenuModel breadcrumbModel;
    private MenuModel menuModel;
    // Binding on form in maincontent.xhtml
    private HtmlForm mainContentForm;
    private List<Panel> panelList;
    private List<Button> buttonsList;
    private PanelGrid panelGrid;
    private int pageLimit;
    private int topicLimit;
    private boolean finishButtonRendered;

    private HtmlForm sliderForm;

    /**
     * Method parses our XML's. Created because out first page must know the
     * list of available templates. Then when you click on start button, method
     * 
     * @see
     */
    @PostConstruct
    public void startSelectXMLScreen() {
	onSelectXMLPage = true;
	MapOfWizardForms = new LinkedHashMap<String, String>();
	xmlController = new XmlController();
	try {
	    wizardDocument = xmlController.readAllDeafultXmlFiles();
	} catch (SAXException | JAXBException ex) {
	    Logger.getLogger(NavigationData.class.getName()).log(Level.SEVERE,
		    null, ex);
	}
	for (WizardForm wForm : wizardDocument.getFormList()) {
	    MapOfWizardForms.put(wForm.getFormName(), wForm.getId());
	}
    }

    public void refreshXMLScreen(String path) {
	onSelectXMLPage = true;
	MapOfWizardForms = new LinkedHashMap<String, String>();
	xmlController.getXmlPathList().add(path);
	try {
	    wizardDocument = xmlController.readAllDeafultXmlFiles();
	} catch (SAXException | JAXBException ex) {
	    Logger.getLogger(NavigationData.class.getName()).log(Level.SEVERE,
		    null, ex);
	}
	for (WizardForm wForm : wizardDocument.getFormList()) {
	    MapOfWizardForms.put(wForm.getFormName(), wForm.getId());
	}
    }

    /**
     * Method uses parsed XML for creating actual wizardForm for user. Sets
     * currentPage and currentTopic to '1' - first in the list. Method creates
     * new empty breadCrumbModel for our breadcrumb and empty list of our
     * current topicID's for menu, because menu uses forEach that iterates over
     * this topicID's
     */
    public void startWizard() {
	mainContentForm = new HtmlForm();
	panelGrid = new PanelGrid();
	panelGrid.setColumns(1);
	mainContentForm.getChildren().add(panelGrid);
	wizardDocument.getWizardFormByID(selectedFormTemplate, wizardForm,
		wizardDocument.getFormList());
	needRefresh = false;
	currentPageID = wizardForm.getWizardPageList().get(0).getId();
	currentTopicID = wizardForm.getWizardPageById(currentPageID)
		.getTopicList().get(0).getId();
	currentTopicIDs = new ArrayList<String>();
	currentTopicTitles = new ArrayList<String>();
	breadcrumbModel = new DefaultMenuModel();
	menuModel = new DefaultMenuModel();
	pageLimit = wizardForm.getWizardPageById(currentPageID).getPageNumber();
	topicLimit = wizardForm.getWizardTopicById(currentTopicID)
		.getTopicNumber();
    }

    public boolean setCurrentTopicIDtoNext() {
	for (int index = 0; index < currentTopicIDs.size(); index++) {
	    if (currentTopicID.equals(currentTopicIDs.get(index))) {
		if (index == currentTopicIDs.size() - 1) {
		    return false;
		} else {
		    currentTopicID = currentTopicIDs.get(index + 1);
		    Integer newCurrentTopicNumber = wizardForm
			    .getWizardTopicById(currentTopicID)
			    .getTopicNumber();
		    if (newCurrentTopicNumber > topicLimit) {
			topicLimit = newCurrentTopicNumber;
		    }
		    return true;
		}
	    }
	}
	return false;
    }

    public boolean setCurrentPageIDtoNext() {
	// get pageList from model
	List<WizardPage> pageList = wizardForm.getWizardPageList();
	// start searching current page
	for (int index = 0; index < pageList.size(); index++) {
	    if (currentPageID.equals(pageList.get(index).getId())) {
		if (index == pageList.size() - 1) {
		    return false;// if finded page is last
		} else {
		    currentPageID = pageList.get(index + 1).getId();// change
		    // pageId to
		    // next id
		    Integer newCurrentPageNumber = wizardForm
			    .getWizardPageById(currentPageID).getPageNumber();
		    if (newCurrentPageNumber > pageLimit) {
			pageLimit = newCurrentPageNumber;
		    }
		    return true;
		}
	    }
	}
	return false;
    }

    public String getTopicTitleFromID(String topicID) {
	String title;
	title = wizardForm.getWizardTopicById(topicID).getGroupTitle();
	return title;
    }

    public String getCurrentPageID() {
	return currentPageID;
    }

    public void setCurrentPageIDAndTitle(String currentPageID) {
	this.currentPageID = currentPageID;
	this.currentPageTitle = wizardForm.getWizardPageById(currentPageID)
		.getPageNumber().toString();
    }

    public void setCurrentPageID(String currentPageID) {
	this.currentPageID = currentPageID;
    }

    public String getCurrentTopicID() {
	return currentTopicID;
    }

    public void setCurrentTopicID(String currentTopicID) {
	this.currentTopicID = currentTopicID;
    }

    public void setCurrentTopicIDAndTitle(String currentTopicID) {
	this.currentTopicID = currentTopicID;
	this.currentTopicTitle = getTopicTitleFromID(currentTopicID);
    }

    /**
     * Get flag, that determines need of refreshing current page in
     * NavigationPhaseListener. Made because of new content must be shown on UI
     * properly and old UI content must be deleted, for example, after choosing
     * new page or topic
     * 
     * @return flag
     * @author vyacheslav.mysak
     */
    public boolean isNeedRefresh() {
	return needRefresh;
    }

    /**
     * Set flag, that determines need of refreshing current page in
     * NavigationPhaseListener. Made because of new content must be shown on UI
     * properly and old UI content must be deleted, for example, after choosing
     * new page or topic
     * 
     * @param needRefresh
     *            flag
     * @author vyacheslav.mysak
     */
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

    public MenuModel getBreadcrumbModel() {
	return breadcrumbModel;
    }

    public void setBreadcrumbModel(MenuModel breadcrumbModel) {
	this.breadcrumbModel = breadcrumbModel;
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

    /**
     * Get flag, that determines our position in our application. Necessary for
     * correct XML processing - first we are parsing XML, and show templates on
     * our start page. Then this flag sets to false - in next steps we don't
     * need to parse XML. If a new XML appears, this flag must be set again to
     * true
     * 
     * @return flag
     * @author vyacheslav.mysak
     */
    public boolean isOnSelectXMLPage() {
	return onSelectXMLPage;
    }

    /**
     * Set flag, that determines our position in our application. Necessary for
     * correct XML processing - first we are parsing XML, and show templates on
     * our start page. Then this flag sets to false - in next steps we don't
     * need to parse XML. If a new XML appears, this flag must be set again to
     * true
     * 
     * @return flag
     * @author vyacheslav.mysak
     */
    public void setOnSelectXMLPage(boolean onSelectXMLPage) {
	this.onSelectXMLPage = onSelectXMLPage;
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

    public List<Panel> getPanelList() {
	return panelList;
    }

    public void setPanelList(List<Panel> panelList) {
	this.panelList = panelList;
    }

    public PanelGrid getPanelGrid() {
	return panelGrid;
    }

    public void setPanelGrid(PanelGrid panelGrid) {
	this.panelGrid = panelGrid;
    }

    public List<Button> getButtonsList() {
	if (buttonsList == null) {
	    buttonsList = new ArrayList<Button>();
	}
	return buttonsList;
    }

    public void setButtonsList(List<Button> buttonsList) {
	this.buttonsList = buttonsList;
    }

    public MenuModel getMenuModel() {
	return menuModel;
    }

    public void setMenuModel(MenuModel menuModel) {
	this.menuModel = menuModel;
    }

    public int getTopicLimit() {
	return topicLimit;
    }

    public void setTopicLimit(int topicLimit) {
	this.topicLimit = topicLimit;
    }

    public int getPageLimit() {

	return pageLimit;
    }

    public void setPageLimit(int pageLimit) {
	this.pageLimit = pageLimit;
    }

    public boolean isFinishButtonRendered() {
	if (isOnLastPage() && isOnLastTopic()) {
	    setFinishButtonRendered(true);
	} else {
	    setFinishButtonRendered(false);
	}
	return finishButtonRendered;
    }

    public void setFinishButtonRendered(boolean finishButtonRendered) {
	this.finishButtonRendered = finishButtonRendered;
    }

    private boolean isOnLastPage() {
	List<WizardPage> pagesOnTemplate = this.wizardForm.getWizardPageList();
	WizardPage simpleWizardPage;
	for (int pageIndex = 0; pageIndex < pagesOnTemplate.size(); pageIndex++) {
	    simpleWizardPage = (WizardPage) pagesOnTemplate.get(pageIndex);
	    if (simpleWizardPage.getId().equals(currentPageID)) {
		if (pageIndex == pagesOnTemplate.size() - 1)
		    return true;
	    }
	}
	return false;
    }

    private boolean isOnLastTopic() {
	for (int topicIntId = 0; topicIntId < currentTopicIDs.size(); topicIntId++) {
	    if (currentTopicID.equals(currentTopicIDs.get(topicIntId))) {
		if (topicIntId == currentTopicIDs.size() - 1)
		    return true;
	    }
	}
	return false;
    }

    public HtmlForm getSliderForm() {
	if (sliderForm == null) {
	    sliderForm = new HtmlForm();
	}
	return sliderForm;
    }

    public void setSliderForm(HtmlForm sliderForm) {
	this.sliderForm = sliderForm;
    }

}