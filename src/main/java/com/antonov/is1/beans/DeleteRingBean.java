package com.antonov.is1.beans;

import com.antonov.is1.entities.Ring;
import com.antonov.is1.services.RingService;
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
public class DeleteRingBean implements Serializable {

    @Inject
    private RingService ringService;

    private Long ringId;
    private Ring ring;

    public void loadRing() {
        if (ringId != null) {
            ring = ringService.getRingById(ringId).orElse(null);
        }
    }

    public String deleteRing() {
        try {
            ringService.deleteRing(ringId);
            addMessage("Успех", "Кольцо удалено успешно!");
            return "rings?faces-redirect=true";
        } catch (Exception e) {
            addMessage("Ошибка", "Не удалось удалить кольцо: " + e.getMessage());
            return null;
        }
    }

    private void addMessage(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(
                null, new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail)
        );
    }

}
