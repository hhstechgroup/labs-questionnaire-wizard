package com.engagepoint.labs.wizard.model;

import com.engagepoint.labs.wizard.bean.WizardDocument;
import com.engagepoint.labs.wizard.bean.WizardForm;
import com.engagepoint.labs.wizard.bean.WizardPage;
import com.engagepoint.labs.wizard.xml.controllers.XmlController;
import org.apache.log4j.Logger;
import org.primefaces.component.button.Button;
import org.primefaces.component.dialog.Dialog;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.panelgrid.PanelGrid;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGroup;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vyacheslav.mysak Bean which helps UINavigationBean in storing
 *         navigation data - e.g. current page, current topic, current question
 *         list, etc.
 */
@Named("navigationData")
@SessionScoped
public class NavigationData implements Serializable {

    public static final String DIALOG = "dialog";
    public static final String DIALOG_DEPEND_QUEST = "dialogDependentQuestion";
    private static final Logger LOGGER = Logger.getLogger(NavigationData.class.getName());
    private static final long serialVersionUID = -3879860102027220266L;
    private boolean onSelectXMLPage;
    @Inject
    private WizardForm wizardForm;
    // Wizard XML items
    private WizardDocument wizardDocument;
    private XmlController xmlController;
    // only for our default xml files!
    private Map<String, String> wizardFormMap;
    // NavData
    private String selectedFormTemplate;

    private List<String> allPagesIdOnCurrentForm;
    private List<String> allTopicsIdOnCurrentPage;

    private String currentFormName;
    private String currentPageID;
    private String currentTopicID;
    private String currentPageTitle;
    private String currentTopicTitle;
    private MenuModel breadcrumbModel;
    private MenuModel menuModel;
    // Binding on form in maincontent.xhtml
    private HtmlForm mainContentForm;
    private List<UIComponent> panelList;
    private List<Button> buttonsList;
    private PanelGrid panelGrid;
    private boolean finishButtonRendered;
    private boolean isFirstPage = true;
    private boolean isFirstTopic = true;
    private boolean previousButtonRendered;
    private HtmlForm sliderForm;
    private String mainContentFormStyle;
    private HtmlPanelGroup scrollablePanelGroup;
    private boolean renderBreadCrumb;


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

    public List<String> getAllPagesIdOnCurrentForm() {
        return allPagesIdOnCurrentForm;
    }

    public void setAllPagesIdOnCurrentForm(List<String> allPagesIdOnCurrentForm) {
        this.allPagesIdOnCurrentForm = allPagesIdOnCurrentForm;
    }

    public List<String> getAllTopicsIdOnCurrentPage() {
        return allTopicsIdOnCurrentPage;
    }

    public void setAllTopicsIdOnCurrentPage(List<String> allTopicsIdOnCurrentPage) {
        this.allTopicsIdOnCurrentPage = allTopicsIdOnCurrentPage;
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

    public Map<String, String> getWizardFormMap() {
        return wizardFormMap;
    }

    public void setWizardFormMap(Map<String, String> wizardFormMap) {
        this.wizardFormMap = wizardFormMap;
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
        return wizardFormMap;
    }

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

    public List<UIComponent> getPanelList() {
        return panelList;
    }

    public void setPanelList(List<UIComponent> panelList) {
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

    public boolean isOnSelectXMLPage() {
        return onSelectXMLPage;
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

    private void setMainContentFormStyle(String mainContentFormStyle) {
        this.mainContentFormStyle = mainContentFormStyle;
    }

    public boolean isRenderBreadCrumb() {
        return renderBreadCrumb;
    }

    public void setRenderBreadCrumb(boolean renderBreadCrumb) {
        this.renderBreadCrumb = renderBreadCrumb;
    }

    public void setFinishButtonRendered(boolean finishButtonRendered) {
        this.finishButtonRendered = finishButtonRendered;
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
        return  xmlController.getExportFileFromWizardForm(this.wizardForm);
    }

    public boolean isFirstPage() {
        return isFirstPage;
    }

    public void setFirstPage(boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
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


    /**
     * Method parses our XML's. Created because out first page must know the
     * list of available templates. Then when you click on start button, method
     *
     * @see
     */
    @PostConstruct
    public void startSelectXMLScreen() {
        onSelectXMLPage = true;
        wizardFormMap = new LinkedHashMap<String, String>();
        xmlController = new XmlController();
        try {
            wizardDocument = xmlController.readAllDeafultXmlFiles();
        } catch (Exception ex) {
            LOGGER.warn("SAX Exception", ex);
        }
        for (WizardForm wForm : wizardDocument.getFormList()) {
            wizardFormMap.put(wForm.getFormName(), wForm.getId());
        }
    }

    public void startWizard() {
        mainContentForm = new HtmlForm();
        panelGrid = new PanelGrid();
        panelGrid.setColumns(1);
        scrollablePanelGroup = new HtmlPanelGroup();
        scrollablePanelGroup.setLayout("block");
        scrollablePanelGroup.setId("scrollableDiv");
        scrollablePanelGroup.setStyle("height:87%");
        scrollablePanelGroup.getChildren().add(panelGrid);
        mainContentForm.getChildren().add(scrollablePanelGroup);
        mainContentForm.getChildren().add(getDialog());
        mainContentForm.getChildren().add(getDialogForDependentQuestion());
        wizardDocument.findWizardFormByID(selectedFormTemplate, wizardForm,
                wizardDocument.getFormList());
        currentPageID = wizardForm.getWizardPageList().get(0).getId();
        currentTopicID = wizardForm.getWizardPageById(currentPageID)
                .getTopicList().get(0).getId();
        allPagesIdOnCurrentForm = new ArrayList<>();
        allTopicsIdOnCurrentPage = new ArrayList<>();
        breadcrumbModel = new DefaultMenuModel();
        menuModel = new DefaultMenuModel();
        wizardForm.setPageLimit(wizardForm.getWizardPageById(currentPageID).getPageNumber());
        wizardForm.setTopicLimit(wizardForm.getWizardTopicById(currentTopicID)
                .getTopicNumber());
        renderBreadCrumb = true;
    }

    public void refreshXMLScreen(List<String> pathList) {
        onSelectXMLPage = true;
        wizardFormMap = new LinkedHashMap<String, String>();
        xmlController = new XmlController();
        xmlController.getXmlPathList().addAll(pathList);
        try {
            wizardDocument = xmlController.readAllDeafultXmlFiles();
        } catch (Exception ex) {
            LOGGER.warn("SAX Exception", ex);
        }
        for (WizardForm wForm : wizardDocument.getFormList()) {
            wizardFormMap.put(wForm.getFormName(), wForm.getId());
        }
    }

    /**
     * Method uses parsed XML for creating actual wizardForm for user. Sets
     * currentPage and currentTopic to '1' - first in the list. Method creates
     * new empty breadCrumbModel for our breadcrumb and empty list of our
     * current topicID's for menu, because menu uses forEach that iterates over
     * this topicID's
     */


    public boolean setCurrentTopicIDtoNext() {
        for (int index = 0; index < allTopicsIdOnCurrentPage.size(); index++) {
            if (currentTopicID.equals(allTopicsIdOnCurrentPage.get(index))) {
                if (index == allTopicsIdOnCurrentPage.size() - 1) {
                    return false;
                } else {
                    currentTopicID = allTopicsIdOnCurrentPage.get(index + 1);
                    Integer newCurrentTopicNumber = wizardForm
                            .getWizardTopicById(currentTopicID)
                            .getTopicNumber();
                    if (newCurrentTopicNumber > wizardForm.getTopicLimit()) {
                        wizardForm.setTopicLimit(newCurrentTopicNumber);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public boolean setCurrentPageIDtoNext() {
        for (int index = 0; index < allPagesIdOnCurrentForm.size(); index++) {
            if (currentPageID.equals(allPagesIdOnCurrentForm.get(index))) {
                if (index == allPagesIdOnCurrentForm.size() - 1) {
                    // if finded page is last
                    return false;
                } else {
                    currentPageID = allPagesIdOnCurrentForm.get(index + 1);
                    Integer newCurrentPageNumber = wizardForm
                            .getWizardPageById(currentPageID).getPageNumber();
                    if (newCurrentPageNumber > wizardForm.getPageLimit()) {
                        wizardForm.setPageLimit(newCurrentPageNumber);
                    }
                    Integer newCurrentTopicNumber = (wizardForm.getWizardPageById(currentPageID)
                            .getTopicList().get(0)).getTopicNumber();
                    if (newCurrentTopicNumber > wizardForm.getTopicLimit()) {
                        wizardForm.setTopicLimit(newCurrentTopicNumber);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public boolean setCurrentPageIDtoPrev() {
        for (int index = 0; index < allPagesIdOnCurrentForm.size(); index++) {
            if (currentPageID.equals(allPagesIdOnCurrentForm.get(index))) {
                if (index == 0) {
                    // if finded page is first
                    return false;
                } else {
                    currentPageID = allPagesIdOnCurrentForm.get(index - 1);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean setCurrentTopicIDtoPrev() {
        for (int index = 0; index < allTopicsIdOnCurrentPage.size(); index++) {
            if (currentTopicID.equals(allTopicsIdOnCurrentPage.get(index))) {
                if (index == 0) {
                    // first topic case
                    return false;
                } else {
                    currentTopicID = allTopicsIdOnCurrentPage.get(index - 1);
                    return true;
                }
            }
        }
        return false;
    }

    public void setCurrentPageIDAndTitle(String currentPageID) {
        this.currentPageID = currentPageID;
        this.currentPageTitle = wizardForm.getWizardPageById(currentPageID)
                .getPageNumber().toString();
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

    public String getMainContentFormStyle() {
        if (panelList != null) {
            if (panelList.size() > 4) {
                setMainContentFormStyle("maincontentid-scroll");
            } else {
                setMainContentFormStyle("maincontentid-non-scroll");
            }
        } else {
            setMainContentFormStyle("maincontentid-non-scroll");
        }
        return mainContentFormStyle;
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
                if (pageIndex == pagesOnTemplate.size() - 1) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isOnLastTopic() {
        for (int topicIntId = 0; topicIntId < allTopicsIdOnCurrentPage.size(); topicIntId++) {
            if (currentTopicID.equals(allTopicsIdOnCurrentPage.get(topicIntId))) {
                if (topicIntId == 0) {
                    setFirstTopic(true);
                } else {
                    setFirstTopic(false);
                }
                if (topicIntId == allTopicsIdOnCurrentPage.size() - 1) {
                    return true;
                }
            }
        }
        return false;
    }

    private Dialog getDialog() {
        OutputLabel message = new OutputLabel();
        message.setValue("Some required questions have no answers !");
        Dialog dialog = new Dialog();
        dialog.setHeader("Validation Error");
        dialog.setId(DIALOG);
        dialog.setWidgetVar(DIALOG);
        dialog.setModal(true);
        dialog.setResizable(false);
        dialog.getChildren().add(message);
        dialog.setHideEffect("clip");
        dialog.setDynamic(true);
        dialog.setDraggable(false);
        return dialog;
    }

    private Dialog getDialogForDependentQuestion() {
        HtmlOutputText firstPartOfMessage = new HtmlOutputText();
        firstPartOfMessage.setValue("Please keep in mind that other question, topic or page depend on answer for this one. <br/>" +
                "When you change answer for such question (that are marked with green star ");
        firstPartOfMessage.setEscape(false);
        HtmlOutputText star = new HtmlOutputText();
        star.setValue(" *");
        star.setStyle("color:#00CC00");
        HtmlOutputText secondPartOfMessage = new HtmlOutputText();
        secondPartOfMessage.setValue(" ),you will continue wizard from this point.");
        secondPartOfMessage.setEscape(false);
        Dialog dialog = new Dialog();
        dialog.setHeader("Other question, topic or page depend on this one!");
        dialog.setId(DIALOG_DEPEND_QUEST);
        dialog.setWidgetVar(DIALOG_DEPEND_QUEST);
        dialog.setModal(true);
        dialog.setResizable(false);
        dialog.getChildren().add(firstPartOfMessage);
        dialog.getChildren().add(star);
        dialog.getChildren().add(secondPartOfMessage);
        dialog.setHideEffect("clip");
        dialog.setDynamic(true);
        return dialog;
    }


}