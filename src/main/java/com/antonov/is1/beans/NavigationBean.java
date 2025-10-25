package com.antonov.is1.beans;

import lombok.Getter;
import lombok.Setter;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
@Getter @Setter
public class NavigationBean implements Serializable {
    private Long searchId;

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


    public String searchCreature() {
        if (searchId != null) {
            return "view-creature?id=" + searchId + "&faces-redirect=true";
        }
        return null;
    }
}