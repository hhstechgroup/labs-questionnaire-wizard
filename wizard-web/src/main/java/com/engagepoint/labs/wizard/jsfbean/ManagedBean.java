
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engagepoint.labs.wizard.jsfbean;

import com.engagepoint.labs.wizard.xml.controllers.XmlContrloller;
import java.util.LinkedHashMap;
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
    @Inject
    XmlContrloller xmlContrloller;
    private String selectedXmlFile;
    private Map<String, String> MapOfXmls;

    {
        MapOfXmls = new LinkedHashMap<>();
        MapOfXmls.put("1 Template", "/XMLforWizard.xml");
        MapOfXmls.put("2 Template", "/XMLforWizard2.xml");
    }

    /**
     * Creates a new instance of ManagedBean
     */
    public ManagedBean() {
    }

    public Map<String, String> getXmlsValues() {
        return MapOfXmls;
    }

    public String getSelectedXmlFile() {
        return selectedXmlFile;
    }

    public void setSelectedXmlFile(String selectedXmlFile) {
        this.selectedXmlFile = selectedXmlFile;
    }

    public String getXmlInfo() {
        return xmlInfo;
    }

    public void setXmlInfo(String xmlInfo) {
        this.xmlInfo = xmlInfo;
    }

    public void parse() {
        try {
            xmlInfo = xmlContrloller.readXML(selectedXmlFile);
        } catch (JAXBException | SAXException ex) {
            Logger.getLogger(ManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
