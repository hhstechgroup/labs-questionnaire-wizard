package com.engagepoint.labs.wizard.jsfbean;

import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by igor.guzenko on 2/10/14.
 */
@Named("dwBean")
@javax.enterprise.context.SessionScoped
public class DWBean implements Serializable{
    private ArrayList<String> pageList;
    private ArrayList<String> topicList;


    public boolean isNeedUpdate() {
        return needUpdate;
    }

    public void setNeedUpdate(boolean needUpdate) {
        this.needUpdate = needUpdate;
    }

    private  boolean needUpdate;

    public DWBean() {
        pageList = new ArrayList<>(3);
        topicList = new ArrayList<>();
        pageList.add("Page1");
        pageList.add("Page2");
        pageList.add("Page3");
        needUpdate=false;

    }

    public ArrayList<String> getPageList() {
        return pageList;
    }

    public ArrayList<String> getTopicList() {
        return topicList;
    }

    public void createTopics(String pageName){

        topicList.clear();
        if(pageName.equals("Home"))
            topicList.add("Upload");
        if(pageName.equals("Page1")){
            topicList.add(pageName+"_Topic1");
            topicList.add(pageName+"_Topic2");
            topicList.add(pageName+"_Topic3");
        }
        if(pageName.equals("Page2")){
            topicList.add(pageName+"_Topic1");
            topicList.add(pageName+"_Topic2");
            topicList.add(pageName+"_Topic3");
        }
        if(pageName.equals("Page3")){
            topicList.add(pageName+"_Topic1");
            topicList.add(pageName+"_Topic2");
            topicList.add(pageName+"_Topic3");
        }
        setNeedUpdate(true);

    }
}
