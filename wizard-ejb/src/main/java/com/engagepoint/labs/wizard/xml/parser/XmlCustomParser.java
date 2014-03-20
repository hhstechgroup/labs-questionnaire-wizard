/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engagepoint.labs.wizard.xml.parser;

import com.engagepoint.labs.wizard.bean.WizardForm;
import com.engagepoint.labs.wizard.export.QuestionaireFormConverter;
import org.apache.log4j.Logger;
import super_binding.QuestionnaireForms;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author artem
 */

public class XmlCustomParser {

    private static final Logger LOGGER = Logger.getLogger(XmlCustomParser.class.getName());

    public QuestionnaireForms parseXML(String XMLpath) throws Exception {
        // Selecting XSD schema from our Resources package (wizard-ejb/src/main/resources)
        String schemaFileName = null;
        Class clazs = getClass();
        ClassLoader classLoader = clazs.getClassLoader();
        URL url = classLoader.getResource("/XSDforWizard.xsd");
        if (url != null) {
            schemaFileName = url.getFile();
        } else {
            url = classLoader.getResource("XSDforWizard.xsd");
            schemaFileName = url.getFile();
        }

        Schema schema = SchemaFactory.newInstance(
                XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(
                new File(schemaFileName));
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
        File exportFile=null;
        try {
            exportFile = File.createTempFile(getExportFileName(form),".xml");
            Marshaller marshaller = JAXBContext.newInstance(QuestionnaireForms.class).createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(formsToMarshall, exportFile);
        } catch (JAXBException e) {
            LOGGER.warn("JAXBException", e);
        } catch (IOException e) {
            LOGGER.warn("FILE IO EXCEPTION!!!");
        }finally {
            if(exportFile==null){
                exportFile = new File("FAILED.TXT");
            }
        }
        return exportFile;
    }

    private String getExportFileName(WizardForm form) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy_HH-mm-ss");
        Date date = new Date();
        return String.format("/%s_answers_%s", form.getFormName(), dateFormat.format(date));
    }


}
