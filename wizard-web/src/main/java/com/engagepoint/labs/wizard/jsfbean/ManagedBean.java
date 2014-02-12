/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.engagepoint.labs.wizard.jsfbean;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author artem.pylypenko
 */
@Named(value = "managedBean")
@RequestScoped
public class ManagedBean {

    /**
     * Creates a new instance of ManagedBean
     */
    public ManagedBean() {
    }
    
}
