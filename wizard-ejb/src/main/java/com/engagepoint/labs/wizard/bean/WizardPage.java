/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engagepoint.labs.wizard.bean;

import com.engagepoint.labs.wizard.questions.RuleExecutor;
import super_binding.PageRule;
import super_binding.QuestionRule;

import java.util.List;

/**
 * @author artem.pylypenko
 */
public class WizardPage {

    private String id;
    private Integer pageNumber;
    private String pageName;
    private List<WizardTopic> topicList;
    private List<PageRule> pageRuleList;
    public boolean ignored;
    public RuleExecutor ruleExecutor;

    public WizardPage() {
    }

    public List<PageRule> getPageRuleList() {
        return pageRuleList;
    }

    public void setPageRuleList(List<PageRule> pageRuleList) {
        this.pageRuleList = pageRuleList;
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

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public List<WizardTopic> getTopicList() {
        return topicList;
    }

    public void setTopicList(List<WizardTopic> topicList) {
        this.topicList = topicList;
    }

}
