package com.engagepoint.labs.wizard.jsfbean;

import java.util.ArrayList;

/**
 * Created by igor.guzenko on 2/7/14.
 */
public class MockPage {
    private String pageName;

    public ArrayList<String> getTopicPages() {
        return topicPages;
    }

    private ArrayList<String> topicPages;

    public MockPage(String pageName) {
        this.pageName = pageName;
        fillTopics(pageName);

    }

    private void fillTopics(String pageName){
        topicPages = new ArrayList<>(5);
        String topicName;
        for(int i=0;i<5;i++){
            topicName=String.format("%s-> %s#%d",pageName,"Topic",i);
            topicPages.add(topicName);
        }

    }

    public String getPageName() {
        return pageName;
    }



}
