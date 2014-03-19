/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engagepoint.labs.wizard.bean;

import com.engagepoint.labs.wizard.ruleExecutors.PageRuleExecutor;
import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import super_binding.PageRule;

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
    private boolean ignored;
    public PageRuleExecutor ruleExecutor;

    public WizardPage() {
    }

    public List<PageRule> getPageRuleList() {
        return pageRuleList;
    }

    public void setPageRuleList(List<PageRule> pageRuleList) {
        this.pageRuleList = pageRuleList;
    }

    public PageRuleExecutor getRuleExecutor() {
        return ruleExecutor;
    }

    public void setRuleExecutor(PageRuleExecutor ruleExecutor) {
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

    public boolean executeAllRules() {
        boolean change = false;
        if (pageRuleList != null) {
            for (PageRule rule : pageRuleList) {
                ruleExecutor.setPage(this);
                JexlEngine jexlEngine = new JexlEngine();
                Expression expression = jexlEngine.createExpression(String.format(rule.getMethod(), rule.getParentId()));
                JexlContext context = new MapContext();
                context.set("this", this);
                boolean result = (boolean) expression.evaluate(context);
                if (result) {
                    change = true;
                }
            }
        }
        return change;
    }

    public void resetPage() {
        for (WizardTopic topic : topicList) {
            topic.resetTopic();
        }
    }
}
