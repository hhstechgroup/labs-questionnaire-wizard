package com.engagepoint.labs.wizard.model.data;

import java.util.ArrayList;


public class Page {
    private String name = "Page";
    private ArrayList<Group> groups;

    private int count = 3 + (int) (Math.random() * ((10 - 3) + 1));

    public Page(int id) {
	setName(getName() + id + 1);
	setGroups(new ArrayList<Group>());

	for (int i = 0; i < count; i++) {
	    getGroups().add(new Group(id, i));
	}
    }

    public ArrayList<Group> getGroups() {
	return groups;
    }

    public void setGroups(ArrayList<Group> groups) {
	this.groups = groups;
	
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }
}