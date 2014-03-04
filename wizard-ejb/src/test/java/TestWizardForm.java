import com.engagepoint.labs.wizard.bean.WizardForm;
import com.engagepoint.labs.wizard.bean.WizardPage;
import com.engagepoint.labs.wizard.bean.WizardTopic;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.*;

/**
 * Created by igor.guzenko on 3/4/14.
 */
public class TestWizardForm {
    private static WizardForm testingForm;
    private static ArrayList<WizardPage> testingList;
    private static ArrayList<WizardTopic> allTopicsList;

    @BeforeClass
    public static void initBeforeTestMethod() {
        testingForm = new WizardForm();
        testingList = new ArrayList<>(3);
        WizardPage page;
        ArrayList<WizardTopic> onePageTopicList;
        WizardTopic uniqueOnePageTopic;
//        onePageTopicList.add(new WizardTopic());
        allTopicsList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            page = new WizardPage();
            page.setId("page " + Integer.toString(i));
            onePageTopicList = new ArrayList<>();
            for (int j =0;j<3;j++){
            uniqueOnePageTopic = new WizardTopic();
            uniqueOnePageTopic.setId(page.getId()+" topic"+j);
            onePageTopicList.add(uniqueOnePageTopic);
            }
            page.setTopicList(onePageTopicList);
            testingList.add(i, page);
            allTopicsList.addAll(onePageTopicList);
        }
        testingForm.setWizardPageList(testingList);
    }

    /**
     * test for correct case
     */
    @Test
    public void testGetWizardPageById() {
        String expectedPageId = "page 1";
        String actualPageId = testingForm.getWizardPageById(expectedPageId).getId();
        assertNotNull(actualPageId);
        assertEquals(expectedPageId, actualPageId);
    }

    /**
     * Test for null result
     */
    @Test
    public void testGetWizardPageA() {
        assertNull(testingForm.getWizardPageById("incorrect id"));
    }

    /**
     * test for correctness
     */
    @Test
    public void testGetAllWizardTopics() {
        assertTrue(Arrays.equals(allTopicsList.toArray(), testingForm.getAllWizardTopics().toArray()));
    }

    /**
     * test for empty topicList in form
     */
    @Test
    public void testGetAllWizardTopicsA(){
        List pageList = testingForm.getWizardPageList();
        for(Object page:pageList){
            ((WizardPage)page).setTopicList(new ArrayList<WizardTopic>());
        }
        assertTrue(testingForm.getAllWizardTopics().isEmpty());
    }

    /**
     * test for topicsIds
     */
    @Test
    public void testGetWizardTopicById(){
        String expectedTopicId = "page 1 topic1";
        assertEquals(expectedTopicId,testingForm.getWizardTopicById(expectedTopicId).getId());
    }

    /**
     * test for doesn't exists id
     */
    @Test
    public void testGetWizardTopicByIdA(){
        assertNull(testingForm.getWizardTopicById("notExistingId"));
    }

    /**
     * test for finding for correct id
     */
    @Test
    public void testGetWizardTopicByIdB(){
        String correctId = "page 1 topic2";
        assertNotNull(testingForm.getWizardTopicById(correctId));
    }
}
