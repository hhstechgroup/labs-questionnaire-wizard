package com.engagepoint.labs.wizard.producers;

import com.engagepoint.labs.wizard.questions.Rule;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import java.io.Serializable;

/**
 * Created by artem.pylypenko on 3/6/14.
 */
@SessionScoped
public class RuleFactory implements Serializable {
    private static final long serialVersionUID = -44416514616012281L;

    @Produces
    public Rule getGreetingCard() {
        return new Rule();
    }
}
