package com.antonov.is1.beans;

import com.antonov.is1.entities.MagicCity;
import com.antonov.is1.services.MagicCityService;
import lombok.Getter;
import lombok.Setter;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
@Getter @Setter
public class DeleteMagicCityBean implements Serializable {

    @Inject
    private MagicCityService magicCityService;

    private Long cityId;
    private MagicCity city;

    public void loadCity() {
        if (cityId != null) {
            city = magicCityService.getMagicCityById(cityId).orElse(null);
        }
    }

    public String deleteCity() {
        try {
            magicCityService.deleteMagicCity(cityId);
            addMessage("Успех", "Город удален успешно!");
            return "cities?faces-redirect=true";
        } catch (Exception e) {
            addMessage("Ошибка", "Не удалось удалить город: " + e.getMessage());
            return null;
        }
    }

    private void addMessage(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(
                null, new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail)
        );
    }

}