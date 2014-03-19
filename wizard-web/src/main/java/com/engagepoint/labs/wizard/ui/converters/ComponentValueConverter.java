package com.engagepoint.labs.wizard.ui.converters;

import com.engagepoint.labs.wizard.questions.DateQuestion;
import com.engagepoint.labs.wizard.questions.TimeQuestion;
import com.engagepoint.labs.wizard.questions.WizardQuestion;
import org.apache.log4j.Logger;
import org.primefaces.component.calendar.Calendar;
import super_binding.QType;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by artem on 02.03.14.
 */
public class ComponentValueConverter implements Converter {
    private static final Logger LOGGER = Logger.getLogger(ComponentValueConverter.class);
    private WizardQuestion question;

    public ComponentValueConverter(WizardQuestion question) {
        this.question = question;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        QType questionType = question.getQuestionType();
        Date date = null;
        if (questionType.equals(QType.DATE)) {
            date = convertDate(component, value);
        } else if (questionType.equals(QType.TIME)) {
            date = convertTime(value);
        }
        return date;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        QType questionType = question.getQuestionType();
        SimpleDateFormat format;
        String dateOrTime = "";
        if (questionType.equals(QType.DATE)) {
            format = new SimpleDateFormat(DateQuestion.DATE_FORMAT);
            dateOrTime = format.format(value);
        } else if (questionType.equals(QType.TIME)) {
            format = new SimpleDateFormat(TimeQuestion.TIME_FORMAT);
            dateOrTime = format.format(value);
        }
        return dateOrTime;
    }

    private Date convertDate(UIComponent component, String value) {
        SimpleDateFormat format = new SimpleDateFormat(DateQuestion.DATE_FORMAT);
        Date date;
        try {
            date = format.parse(value);
            return date;
        } catch (ParseException e) {
            LOGGER.warn("Can't parse date", e);
            question.setValid(false);
            ((Calendar) component).setConversionFailed(true);
            throw new ConverterException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Converter Error",
                    "Wrong date format. Please enter something like " + DateQuestion.DATE_FORMAT));
        }
    }

    private Date convertTime(String value) {
        SimpleDateFormat format = new SimpleDateFormat(TimeQuestion.TIME_FORMAT);
        Date date;
        try {
            date = format.parse(value);
            return date;
        } catch (ParseException e) {
            LOGGER.warn("Can't parse date", e);
            question.setValid(false);
            throw new ConverterException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Converter Error",
                    "Wrong time format. Please enter something like " + TimeQuestion.TIME_FORMAT));
        }
    }
}
