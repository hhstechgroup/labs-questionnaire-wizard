package com.engagepoint.labs.wizard.model.data;

import java.util.ArrayList;


public class Page {
    private String name = "Page";
    private ArrayList<Topic> topics;

    private int count = 3 + (int) (Math.random() * ((10 - 3) + 1));

    public Page(int id) {
	setName(getName() + id + 1);
	setTopics(new ArrayList<Topic>());

	for (int i = 0; i < count; i++) {
	    getTopics().add(new Topic(id, i));
	}
    }

    public ArrayList<Topic> getTopics() {
	return topics;
    }

    public void setTopics(ArrayList<Topic> topics) {
	this.topics = topics;
	
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }
}