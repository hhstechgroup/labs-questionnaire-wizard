import com.engagepoint.labs.wizard.bean.WizardDataModelGenerator;
import com.engagepoint.labs.wizard.bean.WizardForm;
import com.engagepoint.labs.wizard.xml.parser.XmlCustomParser;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import super_binding.Question;
import super_binding.QuestionnaireForm;
import super_binding.QuestionnaireForms;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by igor.guzenko on 3/6/14.
 */
public class TestXmlCustomParser {

    private XmlCustomParser testingParser;

    @Before
    public void initParser(){
        testingParser = new XmlCustomParser();
    }

    /**
     * test that forms is parsing normal
     * @throws JAXBException
     * @throws SAXException
     */
    @Test
    public void testParseXML() throws JAXBException, SAXException {
        QuestionnaireForms parsedForms = testingParser.parseXML("/testparser.xml");
        QuestionnaireForm parsedForm = parsedForms.getQuestionnaireForm().get(0);
        assertNotNull(parsedForm);
    }

    /**
     * test correctness for form id
     * @throws JAXBException
     * @throws SAXException
     */
    @Test
     public void testParseXMLA() throws JAXBException, SAXException {
        QuestionnaireForms parsedForms = testingParser.parseXML("/testparser.xml");
        QuestionnaireForm parsedForm = parsedForms.getQuestionnaireForm().get(0);
        assertEquals("testController",parsedForm.getFormId());
    }

    /**
     * test correctness for form name
     * @throws JAXBException
     * @throws SAXException
     */
    @Test
    public void testParseXMLB() throws JAXBException, SAXException {
        QuestionnaireForms parsedForms = testingParser.parseXML("/testparser.xml");
        QuestionnaireForm parsedForm = parsedForms.getQuestionnaireForm().get(0);
        assertEquals("TestController",parsedForm.getFormName());
    }

    /**
     * test correctness for page id in form
     * @throws JAXBException
     * @throws SAXException
     */
    @Test
    public void testParseXMLC() throws JAXBException, SAXException {
        QuestionnaireForms parsedForms = testingParser.parseXML("/testparser.xml");
        QuestionnaireForm parsedForm = parsedForms.getQuestionnaireForm().get(0);
        assertEquals("formPage1",parsedForm.getPages().getPage().get(0).getPageId());
    }

    /**
     * test correctness for group id on page in form
     * @throws JAXBException
     * @throws SAXException
     */
    @Test
    public void testParseXMLD() throws JAXBException, SAXException {
        QuestionnaireForms parsedForms = testingParser.parseXML("/testparser.xml");
        QuestionnaireForm parsedForm = parsedForms.getQuestionnaireForm().get(0);
        assertEquals("page1Group1",parsedForm.getPages().getPage().get(0).getGroupsOfQuestions().getGroup().get(0).getGroupId());
    }
    /**
     * test correctness for group name on page in form
     * @throws JAXBException
     * @throws SAXException
     */
    @Test
    public void testParseXMLE() throws JAXBException, SAXException {
        QuestionnaireForms parsedForms = testingParser.parseXML("/testparser.xml");
        QuestionnaireForm parsedForm = parsedForms.getQuestionnaireForm().get(0);
        assertEquals("Topic 1 of First Page",parsedForm.getPages().getPage().get(0).getGroupsOfQuestions().getGroup().get(0).getGroupName());
    }

    /**
     * all test for question in template
     * @throws JAXBException
     * @throws SAXException
     */
    @Test
    public void testParseXMLF() throws JAXBException, SAXException {
        QuestionnaireForms parsedForms = testingParser.parseXML("/testparser.xml");
        QuestionnaireForm parsedForm = parsedForms.getQuestionnaireForm().get(0);
        Question testingQuestion = parsedForm.getPages().getPage().get(0).getGroupsOfQuestions().getGroup().get(0).getQuestions().getQuestion().get(0);
        assertEquals("pageOneGroupOne",testingQuestion.getQuestionId());
        assertFalse(testingQuestion.isAnswerRequired());
        assertEquals( "Question one",testingQuestion.getQuestionTitle());
        assertEquals("Type yes if it's topic one of first page?",testingQuestion.getHelpText());
        assertEquals("Default answer",testingQuestion.getDefaultAnswers().getDefaultAnswer().get(0));
    }

    @Test
    public void testParseWizardFormToXML() throws JAXBException, SAXException {
        QuestionnaireForms parsedForms = testingParser.parseXML("/testparser2");
        WizardDataModelGenerator wdmGenerator = new WizardDataModelGenerator();
        ArrayList<QuestionnaireForms> questionnaireFormses = new ArrayList<>();
        questionnaireFormses.add(parsedForms);
        WizardForm testingForm = wdmGenerator.getWizardDocument(questionnaireFormses).getFormList().get(0);
        File actualFile = testingParser.parseWizardFormToXml(testingForm);
        assertNotNull(actualFile);
        assertFalse(actualFile.getTotalSpace()==0);
    }
}
