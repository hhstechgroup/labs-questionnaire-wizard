/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engagepoint.labs.wizard.bean;

import com.engagepoint.labs.wizard.questions.WizardQuestion;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author artem.pylypenko
 */
@Named
@SessionScoped
public class WizardForm implements Serializable {

    private String id;
    private String formName;
    private List<WizardPage> wizardPageList;
    private int pageLimit;
    private int topicLimit;

    public WizardForm() {
    }

    public int getTopicLimit() {
        return topicLimit;
    }

    public void setTopicLimit(int topicLimit) {
        this.topicLimit = topicLimit;
    }

    public int getPageLimit() {

        return pageLimit;
    }

    public void setPageLimit(int pageLimit) {
        this.pageLimit = pageLimit;
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

    public List<WizardTopic> getAllWizardTopics() {
        List<WizardTopic> allTopicsInForm = new ArrayList<>();
        for (WizardPage wizardPage : wizardPageList) {
            List<WizardTopic> topicList = wizardPage.getTopicList();
            allTopicsInForm.addAll(topicList);
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
        List<WizardTopic> allTopics = getAllWizardTopics();
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
        List<WizardTopic> allWizardTopics = getAllWizardTopics();
        List<WizardQuestion> allWizardQuestions = new ArrayList<>();
        for (WizardTopic wizardTopic : allWizardTopics) {
            List<WizardQuestion> wizardQuestionList = wizardTopic.getWizardQuestionList();
            if (null != wizardQuestionList) {
                for (WizardQuestion wizardQuestion : wizardQuestionList) {
                    allWizardQuestions.add(wizardQuestion);
                }
            }
        }
        return allWizardQuestions;
    }

    /**
     * Get WizardQuestion by id
     *
     * @param questionId WizardQuestion id
     * @return WizardQuestion object if null if there is no WizardQuestion with
     * such id
     */
    public WizardQuestion getWizardQuestionById(String questionId) {
        List<WizardQuestion> allWizardQuestions = getAllWizardQuestions();
        for (WizardQuestion wizardQuestion : allWizardQuestions) {
            if (questionId.equals(wizardQuestion.getId())) {
                return wizardQuestion;
            }
        }
        return null;
    }

    public WizardTopic findWizardTopicByQuestionId(String questionId) {
        for (WizardTopic wizardTopic : getAllWizardTopics()) {
            for (WizardQuestion question : wizardTopic.getWizardQuestionList()) {
                if (question.getId().equals(questionId)) {
                    return wizardTopic;
                }
            }
        }
        return null;
    }

}
