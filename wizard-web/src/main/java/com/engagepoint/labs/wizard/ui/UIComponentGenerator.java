package com.engagepoint.labs.wizard.ui;

import com.engagepoint.labs.wizard.controller.UINavigationBean;
import com.engagepoint.labs.wizard.questions.*;
import com.engagepoint.labs.wizard.ui.ajax.CustomAjaxBehaviorListener;
import com.engagepoint.labs.wizard.ui.converters.ComponentValueConverter;
import com.engagepoint.labs.wizard.ui.validators.ComponentValidator;
import com.engagepoint.labs.wizard.values.Value;
import com.engagepoint.labs.wizard.values.objects.Range;
import org.primefaces.component.behavior.ajax.AjaxBehavior;
import org.primefaces.component.button.Button;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.datagrid.DataGrid;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.message.Message;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.panel.Panel;
import org.primefaces.component.selectmanycheckbox.SelectManyCheckbox;
import org.primefaces.component.slider.Slider;
import super_binding.QType;

import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.*;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by igor.guzenko on 2/11/14.
 */
public class UIComponentGenerator {
    private static int MAXIMUM_SIZE_FILE_ANSWER = 1024 * 1024 * 100;
    private UIPanel panel;
    private final int ONE_SELECT_ITEM_HEIGHT = 20;
    private boolean isParent;
    private int pageNumber;
    private int topicNumber;
    private UINavigationBean navigationBean;

    public UIComponentGenerator() {
    }

   // public List<UIComponent> getPanelList(Map<WizardQuestion, Boolean> wizardQuestionMap, int pageNumber, int topicNumber) {
    public List<UIComponent> getPanelList(Map<WizardQuestion, Boolean> wizardQuestionMap,
                                    int pageNumber, int topicNumber, UINavigationBean navigationBean) {
        this.navigationBean = navigationBean;
        this.pageNumber = pageNumber;
        this.topicNumber = topicNumber;
        List<UIComponent> panelList = new ArrayList<>();
        for (Map.Entry<WizardQuestion, Boolean> entry : wizardQuestionMap.entrySet()) {
            isParent = entry.getValue();
            panelList.add(analyzeQuestion(entry.getKey()));
        }
        return panelList;
    }

    private UIComponent analyzeQuestion(WizardQuestion question) {
        panel = new Panel();
        panel.setId("panel_" + question.getId());
        panel.getChildren().add(getLabel(question));
        panel.getChildren().add(getValidationMessage(question));
        UIComponent component = null;
        Value answer = question.getAnswer();
        Value defaultAnswer = question.getDefaultAnswer();
        switch (question.getQuestionType()) {
            case TEXT:
                component = getInputText(question, answer, defaultAnswer);
                break;
            case PARAGRAPHTEXT:
                component = getInputTextArea(question, answer, defaultAnswer);
                break;
            case MULTIPLECHOICE:
                component = getSelectOneListBox(question, answer, defaultAnswer);
                break;
            case CHECKBOX:
                component = getSelectManyCheckbox(question, answer, defaultAnswer);
                break;
            case CHOOSEFROMLIST:
                component = getSelectOneMenu(question, answer, defaultAnswer);
                break;
            case DATE:
                component = getDate(question, answer, defaultAnswer);
                break;
            case TIME:
                // to do
                component = getTime(question, answer, defaultAnswer);
                break;
            case RANGE:
                return getHtmlPanelGroup(question, answer, defaultAnswer);
            case FILEUPLOAD:
                panel.getChildren().add(getLitleLabel(question));
                panel.getChildren().add(getHTMLbr());
                component = getFileUpload(question);
                panel.getChildren().add(getButton(question));
                break;
            case GRID:
                // to do
                component = new DataGrid();
                break;
        }
        component.setId(question.getId());
        panel.getChildren().add(component);
        return panel;
    }

    private HtmlPanelGroup getHtmlPanelGroup(WizardQuestion question, Value answer, Value defaultAnswer) {
        HtmlPanelGroup panelGroup = new HtmlPanelGroup();
        panelGroup.setStyle("padding: 20px; background-color: #EDEDED; border: 1px solid #DDD; border-radius: 3px;");
        panelGroup.setStyleClass("ui-panel-column");
        panelGroup.setLayout("block");
        panelGroup.getChildren().add(getLabel(question));
        panelGroup.getChildren().add(getHTMLbr());
        panelGroup.getChildren().add(getOutputTextForSlider(question));
        panelGroup.getChildren().add(getSliderOutputLabelBegin(question));
        panelGroup.getChildren().add(getInputHiddenEnd(question));
        panelGroup.getChildren().add(getSliderOutputLabelEnd(question));
        panelGroup.getChildren().add(getInputHiddenBegin(question));
        panelGroup.getChildren().add(getSlider(question));
        panelGroup.getChildren().add(getHTMLbr());
        panelGroup.getChildren().add(getCommandButtonForSlider());
        return panelGroup;
    }

    private HtmlOutputText getOutputTextForSlider(WizardQuestion question) {
        int begin = 0;
        int end = 0;
        if (question.getAnswer() != null) {
            Range rangeValue = (Range) question.getAnswer().getValue();
            begin = rangeValue.getStart();
            end = rangeValue.getEnd();
        } else {
            begin = ((RangeQuestion) question).getStartRange();
            end = ((RangeQuestion) question).getEndRange();
        }
        HtmlOutputText outputText = new HtmlOutputText();
        outputText.setId("displayRange" + question.getId());
        String rangeText = "Between " + begin + " and " + end;
        outputText.setValue(rangeText);
        return outputText;
    }

    private HtmlInputHidden getInputHiddenEnd(WizardQuestion question) {
        int end = 0;
        if (question.getAnswer() != null) {
            Range rangeValue = (Range) question.getAnswer().getValue();
            end = rangeValue.getEnd();
        } else {
            end = ((RangeQuestion) question).getEndRange();
            System.err.println("end = " + end);
        }
        HtmlInputHidden inputHidden = new HtmlInputHidden();
        inputHidden.setId("txt7" + question.getId());
        inputHidden.setValue(end);
        inputHidden.addValidator(new ComponentValidator(question));
        return inputHidden;
    }

    private HtmlInputHidden getInputHiddenBegin(WizardQuestion question) {
        int begin = 0;
        if (question.getAnswer() != null) {
            Range rangeValue = (Range) question.getAnswer().getValue();
            begin = rangeValue.getStart();
        } else {
            begin = ((RangeQuestion) question).getStartRange();
            System.err.println("begin = " + begin);
        }
        HtmlInputHidden inputHidden = new HtmlInputHidden();
        inputHidden.setId("txt6" + question.getId());
        inputHidden.setValue(begin);
        inputHidden.addValidator(new ComponentValidator(question));
        return inputHidden;
    }

    private OutputLabel getSliderOutputLabelBegin(WizardQuestion question) {
        OutputLabel label = new OutputLabel();
        label.setValue("slider_input");
        label.setFor("txt7" + question.getId());
        label.setStyleClass("for-slider-horizontal");
        label.setStyle("display: none");
        return label;
    }

    private OutputLabel getSliderOutputLabelEnd(WizardQuestion question) {
        OutputLabel label = new OutputLabel();
        label.setValue("slider_input");
        label.setFor("txt6" + question.getId());
        label.setStyleClass("for-slider-horizontal");
        label.setStyle("display: none");
        return label;
    }

    private Slider getSlider(WizardQuestion question) {
        Slider slider = new Slider();
        slider.setFor("sliderinputtext");
        slider.setAnimate(false);
        slider.addClientBehavior("valueChange", getAjaxBehavior(question));
        slider.setStyle("width: 400px;");
        slider.setDisplay("displayRange" + question.getId());
        slider.setStyleClass("ui-slider-with-range");
        slider.setDisplayTemplate("Between {min} and {max}");
        slider.setRange(true);
        String textFor = "txt6" + question.getId() + " , " + "txt7" + question.getId();
        slider.setFor(textFor);
        return slider;
    }

    private CommandButton getCommandButtonForSlider() {
        CommandButton commandButton = new CommandButton();
        commandButton.setValue("Submit");
        return commandButton;
    }


    private HtmlSelectOneListbox getSelectOneListBox(WizardQuestion question, Value answer, Value defaultAnswer) {
        HtmlSelectOneListbox selectOneListBox = new HtmlSelectOneListbox();

        List<String> optionsList = ((MultipleChoiseQuestion) question)
                .getOptionsList();
        int height = ONE_SELECT_ITEM_HEIGHT * optionsList.size();

        // Creating Listener for Validation and AJAX ClientBehavior
        selectOneListBox.setStyle("height:" + height + "px");
        selectOneListBox.getChildren().add(getSelectItems(optionsList));
        selectOneListBox.addValidator(getComponentValidator(question));
        selectOneListBox.addClientBehavior("valueChange", getAjaxBehavior(question));

        // Showing Answer or Default Answer
        if (defaultAnswer != null && answer == null) {
            selectOneListBox.setValue(defaultAnswer.getValue());
        } else if (answer != null) {
            selectOneListBox.setValue(answer.getValue());
        }
        return selectOneListBox;
    }

    private InputText getInputText(WizardQuestion question, Value answer, Value defaultAnswer) {
        InputText inputText = new InputText();

        // Creating Listener for Validation and AJAX ClientBehavior
        inputText.addValidator(getComponentValidator(question));
        inputText.addClientBehavior("valueChange", getAjaxBehavior(question));
        // Showing Answer or Default Answer
        if (defaultAnswer != null && answer == null) {
            inputText.setValue(defaultAnswer.getValue().toString());
        } else if (answer != null) {
            inputText.setValue(answer.getValue().toString());
        }
        return inputText;
    }

    private InputTextarea getInputTextArea(WizardQuestion question, Value answer, Value defaultAnswer) {
        InputTextarea inputTextarea = new InputTextarea();

        // Creating Listener for Validation and AJAX ClientBehavior
        inputTextarea.addValidator(getComponentValidator(question));
        inputTextarea.addClientBehavior("valueChange", getAjaxBehavior(question));

        // Showing Answer or Default Answer
        if (defaultAnswer != null && answer == null) {
            inputTextarea.setValue(defaultAnswer.getValue().toString());
        } else if (answer != null) {
            inputTextarea.setValue(answer.getValue().toString());
        }
        return inputTextarea;
    }

    private HtmlSelectOneMenu getSelectOneMenu(WizardQuestion question, Value answer, Value defaultAnswer) {
        HtmlSelectOneMenu selectOneMenu = new HtmlSelectOneMenu();
        List<String> optionsList = ((DropDownQuestion) question)
                .getOptionsList();
        UISelectItems defaultItem = new UISelectItems();
        if (defaultAnswer == null && answer == null) {
            defaultItem.setValue(new SelectItem("", "Set answer please"));
            defaultItem.setId("defaultItem");
            selectOneMenu.getChildren().add(defaultItem);
        }

        // Creating Listener for Validation and AJAX ClientBehavior
        selectOneMenu.getChildren().add(getSelectItems(optionsList));
        selectOneMenu.addValidator(getComponentValidator(question));
        selectOneMenu.addClientBehavior("valueChange", getAjaxBehavior(question));

        if (defaultAnswer != null && answer == null) {
            selectOneMenu.setValue(defaultAnswer.getValue());
        } else if (answer != null) {
            selectOneMenu.setValue(answer.getValue());
        }
        return selectOneMenu;
    }

    private SelectManyCheckbox getSelectManyCheckbox(WizardQuestion question, Value answer, Value defaultAnswer) {
        SelectManyCheckbox checkbox = new SelectManyCheckbox();

        List<String> optionsList = ((CheckBoxesQuestion) question)
                .getOptionsList();

        // Creating Listener for Validation and AJAX ClientBehavior
        checkbox.getChildren().add(getSelectItems(optionsList));
        checkbox.setLayout("pageDirection");
        checkbox.addValidator(getComponentValidator(question));
        checkbox.addClientBehavior("valueChange", getAjaxBehavior(question));
        // Showing Answer or Default Answer
        if (defaultAnswer != null && answer == null) {
            checkbox.setValue(defaultAnswer.getValue());
        } else if (answer != null) {
            checkbox.setValue(answer.getValue());
        }
        return checkbox;
    }

    private Calendar getDate(WizardQuestion question, Value answer, Value defaultAnswer) {
        Calendar dateCalendar = new Calendar();

        // Adding all attributes to UIComponent
        dateCalendar.setPattern(DateQuestion.DATE_FORMAT);
        dateCalendar.setStyle("padding:1px");
        dateCalendar.setNavigator(true);
        dateCalendar.setShowOn("both");
        dateCalendar.addClientBehavior("valueChange", getAjaxBehavior(question));
        dateCalendar.addClientBehavior("dateSelect", getAjaxBehavior(question));
        dateCalendar.addValidator(getComponentValidator(question));
        dateCalendar.setConverter(new ComponentValueConverter(question));

        // Showing Answer or Default Answer
        if (defaultAnswer != null && answer == null) {
            dateCalendar.setValue(defaultAnswer.getValue());
        } else if (answer != null) {
            dateCalendar.setValue(answer.getValue());
        }
        return dateCalendar;
    }

    private Calendar getTime(final WizardQuestion question, Value answer, Value defaultAnswer) {
        Calendar timeCalendar = new Calendar();

        // Adding all attributes to UIComponent
        timeCalendar.setTimeOnly(true);
        timeCalendar.setPattern(TimeQuestion.TIME_FORMAT);
        timeCalendar.setStyle("padding:1px");
        timeCalendar.setShowOn("both");
        timeCalendar.addClientBehavior("valueChange", getAjaxBehavior(question));
//        timeCalendar.addClientBehavior("dateSelect", getAjaxBehavior(question));
        timeCalendar.addValidator(getComponentValidator(question));
        timeCalendar.setConverter(new ComponentValueConverter(question));

        // Showing Answer or Default Answer
        if (defaultAnswer != null && answer == null) {
            timeCalendar.setValue(defaultAnswer.getValue());
        } else if (answer != null) {
            timeCalendar.setValue(answer.getValue());
        }
        return timeCalendar;
    }

    private UISelectItems getSelectItems(List<String> optionsList) {
        SelectItem item;
        UISelectItems selectItems = new UISelectItems();
        List<SelectItem> itemsList = new ArrayList<>();
        for (int i = 0; i < optionsList.size(); i++) {
            item = new SelectItem(optionsList.get(i));
            itemsList.add(item);
        }
        selectItems.setValue(itemsList);
        return selectItems;
    }

    private Button getButtonTooltip(WizardQuestion question) {
        Button tooltip = new Button();
        tooltip.setId("tooltip_" + question.getId());
        tooltip.setTitle(question.getHelpText());
        tooltip.setIcon("ui-icon-help");
        tooltip.setStyleClass("custom");
        tooltip.setStyle("position: absolute; left: auto; right: 15px; bottom: auto; padding: 1px");
        tooltip.setDisabled(true);
        return tooltip;
    }

    private OutputLabel getLabel(WizardQuestion question) {
        OutputLabel label = new OutputLabel();
        label.setId("labelIdFor-" + question.getId());
        label.setValue(question.getTitle());
        if (question.isRequired()) {
            HtmlOutputText outputText = new HtmlOutputText();
            outputText.setValue(" *");
            outputText.setStyle("color:red");
            label.getChildren().add(outputText);
        }
        if (isParent) {
            HtmlOutputText outputText = new HtmlOutputText();
            outputText.setValue(" *");
            outputText.setStyle("color:#00CC00");
            label.getChildren().add(outputText);
        }
        label.getChildren().add(getButtonTooltip(question));
        return label;
    }

    private Message getValidationMessage(WizardQuestion question) {
        Message message = new Message();
        message.setFor("maincontentid-" + question.getId());
        return message;
    }

    private AjaxBehavior getAjaxBehavior(WizardQuestion question) {
        AjaxBehavior ajaxBehavior = new AjaxBehavior();
        ajaxBehavior.addAjaxBehaviorListener(new CustomAjaxBehaviorListener(question));
//        if (question.getQuestionType() == QType.RANGE) {
//            ajaxBehavior.setUpdate("maincontentid-j_id1");
//        } else {
//            ajaxBehavior.setUpdate("@(maincontentid-j_id1 :not(.noupdate))");
//        }
        ajaxBehavior.setUpdate("maincontentid-j_id1");
        ajaxBehavior.setAsync(true);
        return ajaxBehavior;
    }

    private HtmlInputFile getFileUpload(WizardQuestion question) {
        HtmlInputFile fileUpload = new HtmlInputFile();
        fileUpload.setValue("#{fileUploadController.file}");
        fileUpload.setSize(MAXIMUM_SIZE_FILE_ANSWER);

        fileUpload.setStyle("position: absolute; left: auto; right: 100px; display: inline-block;");
        fileUpload.addValidator(getComponentValidator(question));
        return fileUpload;
    }

    public CommandButton getButton(WizardQuestion question) {
        CommandButton commandButton = new CommandButton();
        commandButton.setValue("Upload");
        commandButton.setAjax(false);
        //   commandButton.setActionExpression(createMethodExpression(String.format("#{fileUploadController.getAnswerInputStream('" + question.getId() + "')}"), null, String.class));
        return commandButton;
    }

    private OutputLabel getLitleLabel(WizardQuestion question) {
        OutputLabel label = new OutputLabel();
        label.setId("little_" + question.getId());
        if (question.getAnswer() != null) {
            label.setValue("Your file  uploaded");
        }
        return label;
    }

    private HtmlOutputText getHTMLbr() {
        HtmlOutputText lineBreak = new HtmlOutputText();
        lineBreak.setValue("<br/>");
        lineBreak.setEscape(false);
        return lineBreak;
    }

    public static MethodExpression createMethodExpression(String expression, Class<?> returnType, Class<?>... parameterTypes) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getExpressionFactory().createMethodExpression(
                facesContext.getELContext(), expression, returnType, parameterTypes);
    }

    private Validator getComponentValidator(WizardQuestion question) {
        if (isParent) {
            return new ComponentValidator(question, pageNumber, topicNumber, isParent, navigationBean);
        } else {
            return new ComponentValidator(question);
        }
    }
}
