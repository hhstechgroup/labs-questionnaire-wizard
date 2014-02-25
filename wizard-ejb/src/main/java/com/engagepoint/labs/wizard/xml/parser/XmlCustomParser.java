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
//                SYSTEM.OUT.PRINTLN("\NEVENT");
//                SYSTEM.OUT.PRINTLN("SEVERITY:  " + EVENT.GETSEVERITY());
//                SYSTEM.OUT.PRINTLN("MESSAGE:  " + EVENT.GETMESSAGE());
//                SYSTEM.OUT.PRINTLN("LINKED EXCEPTION:  "
//                        + EVENT.GETLINKEDEXCEPTION());
//                SYSTEM.OUT.PRINTLN("LOCATOR");
//                SYSTEM.OUT.PRINTLN("    LINE NUMBER:  "
//                        + EVENT.GETLOCATOR().GETLINENUMBER());
//                SYSTEM.OUT.PRINTLN("    COLUMN NUMBER:  "
//                        + EVENT.GETLOCATOR().GETCOLUMNNUMBER());
//                SYSTEM.OUT.PRINTLN("    OFFSET:  "
//                        + EVENT.GETLOCATOR().GETOFFSET());
//                SYSTEM.OUT.PRINTLN("    OBJECT:  "
//                        + EVENT.GETLOCATOR().GETOBJECT());
//                SYSTEM.OUT
//                        .PRINTLN("    NODE:  " + EVENT.GETLOCATOR().GETNODE());
//                SYSTEM.OUT.PRINTLN("    URL:  " + EVENT.GETLOCATOR().GETURL());
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
