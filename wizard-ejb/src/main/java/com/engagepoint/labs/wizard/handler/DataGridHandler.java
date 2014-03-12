package com.engagepoint.labs.wizard.handler;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named("dataGridHandler")
@SessionScoped
public class DataGridHandler implements Serializable {

    private static final long serialVersionUID = -1066387551691144085L;

    private ArrayList<String> dataList;

    @PostConstruct
    public void init() {
	dataList = new ArrayList<String>();

	dataList.add("sadad");
	dataList.add("sadwdwdad");
	dataList.add("saddad");
	dataList.add("sadsdad");
	dataList.add("ssdadad");
	dataList.add("sadsddad");
	dataList.add("sadssdad");
    }

    public ArrayList<String> getDataList() {
	return dataList;
    }

    public void setDataList(ArrayList<String> dataList) {
	this.dataList = dataList;
    }
}
