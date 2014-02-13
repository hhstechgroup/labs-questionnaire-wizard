package com.engagepoint.labs.wizard.model;

import com.engagepoint.labs.wizard.bean.WizardDocument;
import com.engagepoint.labs.wizard.bean.WizardForm;
import com.engagepoint.labs.wizard.bean.WizardPage;
import com.engagepoint.labs.wizard.xml.controllers.XmlController;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlOutputText;
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

@Named("navigationData")
@SessionScoped
/**
 *
 * @author vyacheslav.mysak
 * Bean which helps UINavigationBean in storing navigation data - e.g. current page, current topic, current question list, etc.
 *
 */
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
    private List<String> XMLpathList;
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

    // UI elements
    private MenuModel breadcrumb_model;
    // Binding on form in maincontent.xhtml
    private HtmlForm mainContentForm;
    private HtmlOutputText currentOutputText;

    /**
     * Method parses our XML's. Created because out first page must know the
     * list of available templates. Then when you click on start button, method
     *
     * @see startWizard() must be called.
     */
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

    /**
     * Method uses parsed XML for creating actual wizardForm for user. Sets
     * currentPage and currentTopic to '1' - first in the list. Method creates
     * new empty breadCrumbModel for our breadcrumb and empty list of our
     * current topicID's for menu, because menu uses forEach that iterates over
     * this topicID's
     */
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

    public boolean setCurrentTopicToNext() {
        for (int index = 0; index < currentTopicIDs.size(); index++) {
            if (currentTopicID.equals(currentTopicIDs.get(index))) {
                if (index == currentTopicIDs.size() - 1) {
                    return false;
                } else {
                    currentTopicID = currentTopicIDs.get(index + 1);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean setCurrentPageToNext() {
        // get pageList from model
        List<WizardPage> pageList = wizardForm.getWizardPageList();
        // start searching current page
        for (int index = 0; index < pageList.size(); index++) {
            if (currentPageID.equals(pageList.get(index).getId())) {
                if (index == pageList.size() - 1) {
                    return false;// if finded page is last
                } else {
                    currentPageID = pageList.get(index + 1).getId();// change pageId to next id
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
     * @param needRefresh flag
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