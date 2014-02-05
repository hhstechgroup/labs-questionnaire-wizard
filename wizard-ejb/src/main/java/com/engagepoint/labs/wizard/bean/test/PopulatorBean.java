package com.engagepoint.labs.wizard.bean.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.el.ValueExpression;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.html.HtmlColumn;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.primefaces.component.commandbutton.CommandButton;

@Named("populatorBean")
@SessionScoped
public class PopulatorBean implements Serializable {

    // Init
    // --------------------------------------------------------------------------------------

    private HtmlPanelGroup dataTableGroup; // Placeholder.
    private CommandButton prButton;
    private List<Item> dataList = new ArrayList<Item>();
    private boolean visibleFlag = true;
    private String type;
    private int amount;
    private boolean fakeFlag;

    // Actions
    // -----------------------------------------------------------------------------------

    @PostConstruct
    public void init() {
	dataList.add(new Item("12", 123121, "SSdfs"));
	dataList.add(new Item("1122", 1223, "SSaa"));
	dataList.add(new Item("1332", 1223, "SsaS"));
	dataList.add(new Item("11232", 1216763, "S12S"));
    }

    private void populateDataTable() {

	// Create <h:dataTable value="#{myBean.dataList}" var="dataItem">.
	HtmlDataTable dataTable = new HtmlDataTable();
	dataTable.setValueExpression("value",
		createValueExpression("#{populatorBean.dataList}", List.class));
	dataTable.setVar("dataItem");

	// Create <h:column> for 'ID' column.
	HtmlColumn idColumn = new HtmlColumn();
	dataTable.getChildren().add(idColumn);

	// Create <h:outputText value="ID"> for <f:facet name="header"> of 'ID'
	// column.
	HtmlOutputText idHeader = new HtmlOutputText();
	idHeader.setValue("ID");
	idColumn.setHeader(idHeader);

	// Create <h:outputText value="#{dataItem.id}"> for the body of 'ID'
	// column.
	HtmlOutputText idOutput = new HtmlOutputText();
	idOutput.setValueExpression("value",
		createValueExpression("#{dataItem.id}", Long.class));
	idColumn.getChildren().add(idOutput);

	// Create <h:column> for 'Name' column.
	HtmlColumn nameColumn = new HtmlColumn();
	dataTable.getChildren().add(nameColumn);

	// Create <h:outputText value="Name"> for <f:facet name="header"> of
	// 'Name' column.
	HtmlOutputText nameHeader = new HtmlOutputText();
	nameHeader.setValue("Name");
	nameColumn.setHeader(nameHeader);

	// Create <h:outputText value="#{dataItem.name}"> for the body of 'Name'
	// column.
	HtmlOutputText nameOutput = new HtmlOutputText();
	nameOutput.setValueExpression("value",
		createValueExpression("#{dataItem.name}", String.class));
	nameColumn.getChildren().add(nameOutput);

	// Create <h:column> for 'Value' column.
	HtmlColumn valueColumn = new HtmlColumn();
	dataTable.getChildren().add(valueColumn);

	// Create <h:outputText value="Value"> for <f:facet name="header"> of
	// 'Value' column.
	HtmlOutputText valueHeader = new HtmlOutputText();
	valueHeader.setValue("Value");
	valueColumn.setHeader(valueHeader);

	// Create <h:outputText value="#{dataItem.value}"> for the body of
	// 'Value' column.
	HtmlOutputText valueOutput = new HtmlOutputText();
	valueOutput.setValueExpression("value",
		createValueExpression("#{dataItem.value}", String.class));
	valueColumn.getChildren().add(valueOutput);

	// Finally add the datatable to <h:panelGroup
	// binding="#{myBean.dataTableGroup}">.
	dataTableGroup = new HtmlPanelGroup();
	dataTableGroup.getChildren().add(dataTable);
    }

    // Getters
    // -----------------------------------------------------------------------------------

    public HtmlPanelGroup getDataTableGroup() {
	// This will be called once in the first RESTORE VIEW phase.
	if (dataTableGroup == null) {
	    populateDataTable(); // Populate datatable.
	}
	return dataTableGroup;
    }

    public void command() {
	System.out.println("command");
	visibleFlag = !visibleFlag;
    }

    // Setters
    // -----------------------------------------------------------------------------------

    public void setDataTableGroup(HtmlPanelGroup dataTableGroup) {
	this.dataTableGroup = dataTableGroup;
    }

    // Helpers
    // -----------------------------------------------------------------------------------

    private ValueExpression createValueExpression(String valueExpression,
	    Class<?> valueType) {
	FacesContext facesContext = FacesContext.getCurrentInstance();
	return facesContext
		.getApplication()
		.getExpressionFactory()
		.createValueExpression(facesContext.getELContext(), valueExpression,
			valueType);
    }

    public List<Item> getDataList() {
	return dataList;
    }

    public void setDataList(List<Item> dataList) {
	this.dataList = dataList;
    }

    public String getValueChanged() {
	return "OK";
    }

    public void setValueChanged(String val) {
	System.out.println("Changed!");
	visibleFlag = !visibleFlag;
    }

    public boolean isVisibleFlag() {
	return visibleFlag;
    }

    public void setVisibleFlag(boolean visibleFlag) {
	this.visibleFlag = visibleFlag;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public int getAmount() {
	return amount;
    }

    public void setAmount(int amount) {
	this.amount = amount;
	dataList.add(new Item(new Integer(amount + 1).toString(), amount, "blah" + amount));
    }

    public CommandButton getPrButton() {
	// TODO: make a list of object below inserted element to update
	if (prButton == null) {
	    prButton = new CommandButton();
	    prButton.setValue(new String("Button from Bean"));
	}
	return prButton;
    }

    public void setPrButton(CommandButton prButton) {
	this.prButton = prButton;
    }

    public boolean isFakeFlag() {
	return fakeFlag;
    }

    public void setFakeFlag(boolean fakeFlag) {
	this.fakeFlag = fakeFlag;
    }

}
