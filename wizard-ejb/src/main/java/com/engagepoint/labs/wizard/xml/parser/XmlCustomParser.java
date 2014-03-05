/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engagepoint.labs.wizard.xml.parser;

import com.engagepoint.labs.wizard.bean.WizardForm;
import com.engagepoint.labs.wizard.export.QuestionaireFormConverter;
import org.xml.sax.SAXException;
import super_binding.QuestionnaireForms;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;

/**
 * @author artem
 */

public class XmlCustomParser {
    private static final String EXPORT_FILE_NAME = "C:\\Users\\igor.guzenko\\IdeaProjects\\labs-questionnaire-wizard\\wizard-ejb\\src\\main\\resources\\exportFile.xml";
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
//                System.out.print("\NEVENT");
//                System.out.print("SEVERITY:  " + EVENT.GETSEVERITY());
//                System.out.print("MESSAGE:  " + EVENT.GETMESSAGE());
//                System.out.print("LINKED EXCEPTION:  "
//                        + EVENT.GETLINKEDEXCEPTION());
//                System.out.print("LOCATOR");
//                System.out.print("    LINE NUMBER:  "
//                        + EVENT.GETLOCATOR().GETLINENUMBER());
//                System.out.print("    COLUMN NUMBER:  "
//                        + EVENT.GETLOCATOR().GETCOLUMNNUMBER());
//                System.out.print("    OFFSET:  "
//                        + EVENT.GETLOCATOR().GETOFFSET());
//                System.out.print("    OBJECT:  "
//                        + EVENT.GETLOCATOR().GETOBJECT());
//                System.out.print("    NODE:  " + EVENT.GETLOCATOR().GETNODE());
//                System.out.print("    URL:  " + EVENT.GETLOCATOR().GETURL());
                return true;
            }
        });
        // Getting filled QuestionnaireForms using Unmarshaller depends
        //  on XML file from our Resources package (wizard-ejb/src/main/resources)
        if (XMLpath.startsWith("/")) {
            QuestionnaireForms forms = (QuestionnaireForms) unmarshaller
                    .unmarshal(new File(getClass().getResource(XMLpath).getFile()));
            return forms;
        } else {
            QuestionnaireForms forms = (QuestionnaireForms) unmarshaller
                    //new File(getClass().getResource(XMLpath).getFile())
                    .unmarshal(new File(XMLpath));
            return forms;
        }

    }

    public File parseWizardFormToXml(WizardForm form) {
        QuestionaireFormConverter converter = new QuestionaireFormConverter();
        QuestionnaireForms formsToMarshall = converter.convert(form);
        File exportFile = new File(EXPORT_FILE_NAME);
        try {
            Marshaller marshaller = JAXBContext.newInstance(QuestionnaireForms.class).createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(formsToMarshall, exportFile);
        } catch (JAXBException e) {
           return exportFile;
        }
        return exportFile;
    }


}
