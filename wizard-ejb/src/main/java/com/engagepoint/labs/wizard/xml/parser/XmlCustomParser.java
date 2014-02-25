/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engagepoint.labs.wizard.xml.parser;

import java.io.File;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;
import super_binding.QuestionnaireForms;

/**
 *
 * @author artem
 */

public class XmlCustomParser {

    public QuestionnaireForms parseXML(String XMLpath) throws SAXException,
            JAXBException {
        // Selecting XSD schema from our Resources package (wizard-ejb/src/main/resources)
        Schema schema = SchemaFactory.newInstance(
                XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(
                        new File(getClass().getClassLoader()
                                .getResource("/XSDforWizard.xsd").getFile()));
        // Creating Unmarshaller and select Class
        // that we wont to get after XML file parsing
        Unmarshaller unmarshaller = JAXBContext.newInstance(
                QuestionnaireForms.class).createUnmarshaller();
        // Setting XSD schema in our Unmarshaller
        unmarshaller.setSchema(schema);
        // Setting ValidationEventHandler interface in out Unmarshaller (it's optional)
        // All messages during validation will be written  in console
        unmarshaller.setEventHandler(new ValidationEventHandler() {
            @Override
            public boolean handleEvent(ValidationEvent event) {
                System.out.println("\nEVENT");
                System.out.println("SEVERITY:  " + event.getSeverity());
                System.out.println("MESSAGE:  " + event.getMessage());
                System.out.println("LINKED EXCEPTION:  "
                        + event.getLinkedException());
                System.out.println("LOCATOR");
                System.out.println("    LINE NUMBER:  "
                        + event.getLocator().getLineNumber());
                System.out.println("    COLUMN NUMBER:  "
                        + event.getLocator().getColumnNumber());
                System.out.println("    OFFSET:  "
                        + event.getLocator().getOffset());
                System.out.println("    OBJECT:  "
                        + event.getLocator().getObject());
                System.out
                        .println("    NODE:  " + event.getLocator().getNode());
                System.out.println("    URL:  " + event.getLocator().getURL());
                return true;
            }
        });
        // Getting filled QuestionnaireForms using Unmarshaller depends
        //  on XML file from our Resources package (wizard-ejb/src/main/resources)
        QuestionnaireForms forms = (QuestionnaireForms) unmarshaller
                .unmarshal(new File(getClass().getResource(XMLpath).getFile()));
        return forms;
    }
}
