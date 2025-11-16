package com.antonov.is1.beans;

import com.antonov.is1.entities.BookCreatureType;
import com.antonov.is1.entities.MagicCity;
import com.antonov.is1.services.MagicCityService;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
@Getter
@Setter
public class MagicCityBean implements Serializable {

    @Inject
    private MagicCityService magicCityService;

    // Данные для отображения
    private List<MagicCity> cities;
    private MagicCity selectedCity;
    private MagicCity newCity;

    // Пагинация
    private final int pageSize = 20;
    private int currentPage = 1;
    private long totalCount;

    public void loadData() {
        cities = magicCityService.getAllMagicCities();
        totalCount = magicCityService.getAllMagicCities().size();
    }

    private void initNewCity() {
        newCity = new MagicCity();
        newCity.setCapital(false);
    }

    // ===== CRUD OPERATIONS =====

    public void createCity() {
        try {
            magicCityService.createMagicCity(
                    newCity.getName(), newCity.getArea(), newCity.getPopulation(),
                    newCity.getEstablishmentDate(), newCity.getGovernor(),
                    newCity.getCapital(), newCity.getPopulationDensity()
            );
            addMessage("Успех", "Город создан успешно!");
            loadData();
            initNewCity();
        } catch (Exception e) {
            addMessage("Ошибка", "Не удалось создать город: " + e.getMessage());
        }
    }

    public void updateCity() {
        try {
            magicCityService.updateMagicCity(selectedCity);
            addMessage("Успех", "Город обновлен успешно!");
            loadData();
            selectedCity = null;
        } catch (Exception e) {
            addMessage("Ошибка", "Не удалось обновить город: " + e.getMessage());
        }
    }

    public void deleteCity() {
        if (selectedCity != null) {
            try {
                magicCityService.deleteMagicCity(selectedCity.getId());
                addMessage("Успех", "Город удален успешно!");
                loadData();
                selectedCity = null;
            } catch (Exception e) {
                addMessage("Ошибка", "Не удалось удалить город: " + e.getMessage());
            }
        }
    }

    // ===== UTILITY METHODS =====

    private void addMessage(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(
                null, new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail)
        );
    }

}