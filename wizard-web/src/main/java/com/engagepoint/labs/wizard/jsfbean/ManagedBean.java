
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

    private String xmlInfo;
    private WizardDocument wizardDocument;
    XmlController xmlController;
    @Inject
    private WizardForm wizardForm;
    private String selectedFormTemplate;
    // only for our default xml files!
    private List<String> XMLpathList;
    private Map<String, String> MapOfXmls;

    {
        MapOfXmls = new LinkedHashMap<>();
        XMLpathList = new ArrayList<>();
        XMLpathList.add("/XMLforWizard.xml");
        XMLpathList.add("/XMLforWizard2.xml");
        xmlController = new XmlController();
        try {
            wizardDocument = xmlController.readXML(XMLpathList);
        } catch (SAXException | JAXBException ex) {
            Logger.getLogger(ManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (WizardForm wForm : wizardDocument.getFormList()) {
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
            System.out.println("size" + wizardDocument.getFormList().size());
            System.out.println("Name" + wForm.getFormName());
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
            MapOfXmls.put(wForm.getFormName(), wForm.getId());
        }
    }

    /**
     * Creates a new instance of ManagedBean
     */
    public ManagedBean() {
    }

    public Map<String, String> getXmlsValues() {
        return MapOfXmls;
    }

    public String getSelectedFormTemplate() {
        return selectedFormTemplate;
    }

    public void setSelectedFormTemplate(String selectedFormTemplate) {
        this.selectedFormTemplate = selectedFormTemplate;
    }

    public String getXmlInfo() {
        return xmlInfo;
    }

    public void setXmlInfo(String xmlInfo) {
        this.xmlInfo = xmlInfo;
    }

    public void parse() {
//        try {
//            xmlController.readXML(selectedFormTemplate);
//        } catch (JAXBException | SAXException ex) {
//            Logger.getLogger(ManagedBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
        // Code below for testing only 
        xmlController.getSelectedTemplate(selectedFormTemplate, wizardDocument, wizardForm);
        System.out.println("**********************");
        System.out.println("***********************");
        System.out.println("SELECTED TEML: " + selectedFormTemplate);
        System.out.println("It's a call from Managed BEan");
        System.out.println("ID : " + wizardForm.getId());
        System.out.println("Wizard Form - " + wizardForm.getFormName());
        System.out.println("Pages: " + wizardForm.getPageList());
    }
}
