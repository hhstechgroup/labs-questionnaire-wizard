/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engagepoint.labs.wizard.bean;

import com.engagepoint.labs.wizard.questions.RuleExecutor;
import com.engagepoint.labs.wizard.questions.WizardQuestion;
import super_binding.GroupRule;
import super_binding.QuestionRule;

import java.util.List;

/**
 * @author artem.pylypenko
 */
public class WizardTopic {

    private String id;
    private String groupTitle;
    private List<WizardQuestion> wizardQuestionList;
    private Integer topicNumber;
    private List<GroupRule> groupRuleList;
    public boolean ignored;
    public RuleExecutor ruleExecutor;

    public WizardTopic() {
    }

    public List<GroupRule> getGroupRuleList() {
        return groupRuleList;
    }

    public void setGroupRuleList(List<GroupRule> groupRuleList) {
        this.groupRuleList = groupRuleList;
    }

    public RuleExecutor getRuleExecutor() {
        return ruleExecutor;
    }

    public void setRuleExecutor(RuleExecutor ruleExecutor) {
        this.ruleExecutor = ruleExecutor;
    }

    public boolean isIgnored() {

        return ignored;
    }

    public void setIgnored(boolean ignored) {
        this.ignored = ignored;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getTopicNumber() {
        return topicNumber;
    }

    public void setTopicNumber(Integer topicNumber) {
        this.topicNumber = topicNumber;
    }

    public List<WizardQuestion> getWizardQuestionList() {
        return wizardQuestionList;
    }

    public void setWizardQuestionList(List<WizardQuestion> wizardQuestionList) {
        this.wizardQuestionList = wizardQuestionList;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public WizardQuestion findQuestionById(String questionId) {
        for (WizardQuestion question : wizardQuestionList) {
            if (question.getId().equals(questionId)) {
                return question;
            }
        }
        return null;
    }

}
