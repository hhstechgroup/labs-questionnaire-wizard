/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engagepoint.labs.wizard.bean;

import java.util.List;

/**
 * @author artem.pylypenko
 */
public class WizardPage {

    private String id;
    private Integer pageNumber;
    private String pageName;
    private List<WizardTopic> topicList;

    public WizardPage() {
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
