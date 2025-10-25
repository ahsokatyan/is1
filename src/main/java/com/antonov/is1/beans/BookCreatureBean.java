package com.antonov.is1.beans;

import com.antonov.is1.entities.*;
import com.antonov.is1.services.*;
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
@Getter @Setter
public class BookCreatureBean implements Serializable {

    @Inject
    private BookCreatureService bookCreatureService;

    @Inject
    private CoordinatesService coordinatesService;

    @Inject
    private MagicCityService magicCityService;

    @Inject
    private RingService ringService;

    // Данные для отображения
    private List<BookCreature> creatures;
    private List<MagicCity> availableCities;
    private List<Ring> availableRings;

    // Выбранные объекты для операций
    private BookCreature selectedCreature;
    private BookCreature newCreature;

    // Пагинация
    private int currentPage = 1;
    private int pageSize = 15;
    private long totalCount;

    @PostConstruct
    public void init() {
        loadData();
        initNewCreature();
    }

    public void loadData() {
        creatures = bookCreatureService.getAllBookCreatures(currentPage, pageSize);
        totalCount = bookCreatureService.getBookCreaturesCount();
        availableCities = magicCityService.getAllMagicCities();
        availableRings = ringService.getFreeRings();
    }

    private void initNewCreature() {
        newCreature = new BookCreature();
        newCreature.setCoordinates(new Coordinates());
    }

    // ===== CRUD OPERATIONS =====

    public void createCreature() {
        try {
            bookCreatureService.createBookCreature(newCreature);
            addMessage("Успех", "Существо создано успешно!");
            loadData();
            initNewCreature();
        } catch (Exception e) {
            addMessage("Ошибка", "Не удалось создать существо: " + e.getMessage());
        }
    }


    public void updateCreature() {
        try {
            bookCreatureService.updateBookCreature(selectedCreature);
            addMessage("Успех", "Существо обновлено успешно!");
            loadData();
            selectedCreature = null;
        } catch (Exception e) {
            addMessage("Ошибка", "Не удалось обновить существо: " + e.getMessage());
        }
    }

    public void deleteCreature() {
        if (selectedCreature != null) {
            try {
                bookCreatureService.deleteBookCreature(selectedCreature.getId());
                addMessage("Успех", "Существо удалено успешно!");
                loadData();
                selectedCreature = null;
            } catch (Exception e) {
                addMessage("Ошибка", "Не удалось удалить существо: " + e.getMessage());
            }
        }
    }

    // ===== PAGINATION =====

    public void nextPage() {
        if (hasNextPage()) {
            currentPage++;
            loadData();
        }
    }

    public void previousPage() {
        if (hasPreviousPage()) {
            currentPage--;
            loadData();
        }
    }

    public void firstPage() {
        currentPage = 1;
        loadData();
    }

    public void lastPage() {
        currentPage = (int) Math.ceil((double) totalCount / pageSize);
        loadData();
    }

    public boolean hasNextPage() {
        return (long) currentPage * pageSize < totalCount;
    }

    public boolean hasPreviousPage() {
        return currentPage > 1;
    }

    // ===== UTILITY METHODS =====

    private void addMessage(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(
                null, new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail)
        );
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        firstPage(); // Сброс на первую страницу при изменении размера
    }
    public long getTotalPages() {
        return (long) Math.ceil((double) totalCount / pageSize);
    }



    public void debugConsole(String value){
        System.out.println("Converting to object: " + value);
//        System.out.println("Converting to string: " + value);
    }
}