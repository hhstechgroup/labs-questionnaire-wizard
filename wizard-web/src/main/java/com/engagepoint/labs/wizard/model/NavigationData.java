package com.engagepoint.labs.wizard.model;

import com.engagepoint.labs.wizard.bean.WizardDocument;
import com.engagepoint.labs.wizard.bean.WizardForm;
import com.engagepoint.labs.wizard.bean.WizardPage;
import com.engagepoint.labs.wizard.ui.WizardLimits;
import com.engagepoint.labs.wizard.xml.controllers.XmlController;
import org.primefaces.component.button.Button;
import org.primefaces.component.dialog.Dialog;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.panel.Panel;
import org.primefaces.component.panelgrid.PanelGrid;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlForm;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBException;
import java.io.File;
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
    private boolean finishButtonRendered;
    private boolean isFirstPage = true;
    private boolean isFirstTopic = true;
    private boolean previousButtonRendered;

    private HtmlForm sliderForm;
    private String mainContentFormStyle;

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
        xmlController = new XmlController();
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
        mainContentForm.getChildren().add(getDialog());
        wizardDocument.findWizardFormByID(selectedFormTemplate, wizardForm,
                wizardDocument.getFormList());
        currentPageID = wizardForm.getWizardPageList().get(0).getId();
        currentTopicID = wizardForm.getWizardPageById(currentPageID)
                .getTopicList().get(0).getId();
        currentTopicIDs = new ArrayList<String>();
        currentTopicTitles = new ArrayList<String>();
        breadcrumbModel = new DefaultMenuModel();
        menuModel = new DefaultMenuModel();
        WizardLimits.pageLimit = wizardForm.getWizardPageById(currentPageID).getPageNumber();
        WizardLimits.topicLimit = wizardForm.getWizardTopicById(currentTopicID)
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
                    if (newCurrentTopicNumber > WizardLimits.topicLimit) {
                        WizardLimits.topicLimit = newCurrentTopicNumber;
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
                    currentPageID = pageList.get(index + 1).getId();
                    Integer newCurrentPageNumber = wizardForm
                            .getWizardPageById(currentPageID).getPageNumber();
                    if (newCurrentPageNumber > WizardLimits.pageLimit) {
                        WizardLimits.pageLimit = newCurrentPageNumber;
                    }
                    Integer newCurrentTopicNumber = (wizardForm.getWizardPageById(currentPageID)
                            .getTopicList().get(0)).getTopicNumber();
                    if (newCurrentTopicNumber > WizardLimits.topicLimit) {
                        WizardLimits.topicLimit = newCurrentTopicNumber;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public boolean setCurrentPageIDtoPrev() {
        List<WizardPage> pageList = wizardForm.getWizardPageList();
        // start searching current page
        for (int index = 0; index < pageList.size(); index++) {
            if (currentPageID.equals(pageList.get(index).getId())) {
                if (index == 0) {
                    return false;// if finded page is first
                } else {
                    currentPageID = pageList.get(index - 1).getId();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean setCurrentTopicIDtoPrev() {
        for (int index = 0; index < currentTopicIDs.size(); index++) {
            if (currentTopicID.equals(currentTopicIDs.get(index))) {
                if (index == 0) {
                    return false; // first topic case
                } else {
                    currentTopicID = currentTopicIDs.get(index - 1);
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

            UIComponent component = mainContentForm.findComponent("maincontentid");
            if(component!=null){
                component.getAttributes().put("styleClass",this.getMainContentFormStyle());
            }


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

    public boolean isFinishButtonRendered() {
        boolean nowLastPage = isOnLastPage();
        boolean nowLastTopic = isOnLastTopic();
        if (nowLastPage && nowLastTopic) {
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
                if (pageIndex == 0) {
                    setFirstPage(true);
                } else {
                    setFirstPage(false);
                }
                if (pageIndex == pagesOnTemplate.size() - 1)
                    return true;
            }
        }
        return false;
    }

    private boolean isOnLastTopic() {
        for (int topicIntId = 0; topicIntId < currentTopicIDs.size(); topicIntId++) {
            if (currentTopicID.equals(currentTopicIDs.get(topicIntId))) {
                if (topicIntId == 0) {
                    setFirstTopic(true);
                } else {
                    setFirstTopic(false);
                }
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

    public File getExportFile() {
        File exFile = xmlController.getExportFileFromWizardForm(this.wizardForm);
        return exFile;
    }

    public boolean isFirstPage() {
        return isFirstPage;
    }

    public void setFirstPage(boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
    }

    private Dialog getDialog() {
        OutputLabel message = new OutputLabel();
        message.setValue("Some required questions have no answers !");
        OutputLabel header = new OutputLabel();
        header.setValue("Validation Error");
        Dialog dialog = new Dialog();
        dialog.setHeader("Validation Error");
        dialog.setId("dialog");
        dialog.setWidgetVar("dialog");
        dialog.setModal(true);
        dialog.setResizable(false);
        dialog.getChildren().add(message);
        dialog.setHideEffect("clip");
        dialog.setDynamic(true);
        return dialog;
    }

    private Dialog getDialogForDependentQuestion() {
        OutputLabel message = new OutputLabel();
        message.setValue("Parent Question was redacted !");
        OutputLabel header = new OutputLabel();
        header.setValue("Parent Question was redacted");
        Dialog dialog = new Dialog();
        dialog.setHeader("Parent Question was redacted");
        dialog.setId("dialog");
        dialog.setWidgetVar("dialogDependentQuestion");
        dialog.setModal(true);
        dialog.setResizable(false);
        dialog.getChildren().add(message);
        dialog.setHideEffect("clip");
        dialog.setDynamic(true);
        return dialog;
    }

    public boolean isPreviousButtonRendered() {
        return previousButtonRendered;
    }

    public void setPreviousButtonRendered(boolean previousButtonRendered) {
        this.previousButtonRendered = previousButtonRendered;
    }

    public boolean isFirstTopic() {
        return isFirstTopic;
    }

    public void setFirstTopic(boolean isFirstTopic) {
        this.isFirstTopic = isFirstTopic;
    }

    public String getMainContentFormStyle() {
        if(wizardForm.getWizardTopicById(currentTopicID).getWizardQuestionList().size()>5){
            setMainContentFormStyle("maincontentid-scroll");}
        else {
            setMainContentFormStyle("maincontentid-non-scroll");
        }
        return mainContentFormStyle;
    }

    private void setMainContentFormStyle(String mainContentFormStyle) {
        this.mainContentFormStyle = mainContentFormStyle;
    }
}