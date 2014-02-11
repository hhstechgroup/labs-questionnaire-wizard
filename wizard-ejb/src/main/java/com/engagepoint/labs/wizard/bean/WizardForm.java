/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engagepoint.labs.wizard.bean;

import com.engagepoint.labs.wizard.questions.WizardQuestion;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author artem.pylypenko
 */
@Named
@SessionScoped
public class WizardForm implements Serializable {

    private String id;
    private String formName;
    private List<WizardPage> wizardPageList;

    public WizardForm() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public List<WizardPage> getWizardPageList() {
        return wizardPageList;
    }

    public void setWizardPageList(List<WizardPage> wizardPageList) {
        this.wizardPageList = wizardPageList;
    }

    /**
     * Getting WizardPage by id
     *
     * @param pageId WizardPage id
     * @return WizardPage object or null if there is no WizardPage with such id
     */
    public WizardPage getWizardPageById(String pageId) {
        for (WizardPage wizardPage : wizardPageList) {
            if (pageId.equals(wizardPage.getId())) {
                return wizardPage;
            }
        }
        return null;
    }

    public List<WizardTopic> getAllWizarlTopics() {
        List<WizardTopic> allTopicsInForm = new ArrayList<>();
        for (WizardPage wizardPage : wizardPageList) {
            List<WizardTopic> topicList = wizardPage.getTopicList();
            for (WizardTopic wizardTopic : topicList) {
                allTopicsInForm.add(wizardTopic);
            }
        }
        return allTopicsInForm;
    }

    /**
     * Getting WizardTopic by id
     *
     * @param topicId WizardTopic id
     * @return WizardTopic object or null if there is no WizardTopic with such
     * id
     */
    public WizardTopic getWizardTopicById(String topicId) {
        List<WizardTopic> allTopics = getAllWizarlTopics();
        WizardTopic result = null;

        for (WizardTopic wizardTopic : allTopics) {
            if (topicId.equals(wizardTopic.getId())) {
                result = wizardTopic;
                break;
            }
        }

        return result;
    }

    public List<WizardQuestion> getAllWizardQuestions() {
        List<WizardTopic> allWizarlTopics = getAllWizarlTopics();
        List<WizardQuestion> allWizardQuestions = new ArrayList<>();
        for (WizardTopic wizardTopic : allWizarlTopics) {
            List<WizardQuestion> wizardQuestionList = wizardTopic.getWizardQuestionList();
            for (WizardQuestion wizardQuestion : wizardQuestionList) {
                allWizardQuestions.add(wizardQuestion);
            }
        }
        return allWizardQuestions;
    }

    /**
     * Get WizardQuestion by id
     *
     * @param QuestionId WizardQuestion id
     * @return WizardQuestion object if null if there is no WizardQuestion with
     * such id
     */
    public WizardQuestion getWizardQuestionById(String QuestionId) {
        List<WizardQuestion> allWizardQuestions = getAllWizardQuestions();
        for (WizardQuestion wizardQuestion : allWizardQuestions) {
            if (QuestionId.equals(wizardQuestion.getId())) {
                return wizardQuestion;
            }
        }
        return null;
    }

}