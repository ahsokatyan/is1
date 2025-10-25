package com.antonov.is1.beans;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class NavigationBean implements Serializable {

    public String goToCreatures() {
        return "creatures?faces-redirect=true";
    }

    public String goToCities() {
        return "cities?faces-redirect=true";
    }

    public String goToHome() {
        return "index?faces-redirect=true";
    }

    public String goToCreateCreature() {
        return "create-creature?faces-redirect=true";
    }

    public String goToEditCreature() {
        return "edit-creature?faces-redirect=true";
    }
}