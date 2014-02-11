
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engagepoint.labs.wizard.jsfbean;

import com.engagepoint.labs.wizard.bean.WizardDocument;
import com.engagepoint.labs.wizard.bean.WizardForm;
import com.engagepoint.labs.wizard.xml.controllers.XmlController;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import org.xml.sax.SAXException;

/**
 *
 * @author artem.pylypenko
 */
@Named(value = "managedBean")
@RequestScoped
public class ManagedBean {

    private WizardDocument wizardDocument;
    XmlController xmlController;
    @Inject
    private WizardForm wizardForm;
    private String selectedFormTemplate;
    // only for our default xml files!
    private List<String> XMLpathList;
    private Map<String, String> MapOfWizardForms;

    {
        MapOfWizardForms = new LinkedHashMap<>();
        XMLpathList = new ArrayList<>();
        XMLpathList.add("/XMLforWizard.xml");
        XMLpathList.add("/XMLforWizard2.xml"); 
        xmlController = new XmlController();     
        try {
            wizardDocument = xmlController.readAllDeafultXmlFiles(XMLpathList);
        } 
        catch (SAXException | JAXBException ex) {
            Logger.getLogger(ManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for (WizardForm wForm : wizardDocument.getFormList()) {
            MapOfWizardForms.put(wForm.getFormName(), wForm.getId());
        }
    }

    /**
     * Creates a new instance of ManagedBean
     */
    public ManagedBean() {
    }

    public Map<String, String> getXmlsValues() {
        return MapOfWizardForms;
    }

    public String getSelectedFormTemplate() {
        return selectedFormTemplate;
    }

    public void setSelectedFormTemplate(String selectedFormTemplate) {
        this.selectedFormTemplate = selectedFormTemplate;
    }

    public String start() {
        wizardDocument.getWizardFormByID(selectedFormTemplate, wizardForm, wizardDocument.getFormList());
        return null;
    }
}
