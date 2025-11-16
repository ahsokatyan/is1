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
import java.util.Optional;


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


    private Long selectedCityId;
    private Long selectedRingId;

    // Пагинация
    private int currentPage = 1;
    private int pageSize = 20;
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

//    public void lastPage() {
//        currentPage = (int) Math.ceil((double) totalCount / pageSize);
//        loadData();
//    }

    public void lastPage() {
        currentPage = Math.toIntExact(getTotalPages());
        loadData();
    }

    public boolean hasNextPage() {
        return currentPage < getTotalPages();
    }

    public boolean hasPreviousPage() {
        return currentPage > 1;
    }

    public long getTotalPages() {
        if (totalCount == 0) return 1;
        return (long) Math.ceil((double) totalCount / pageSize);
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


    public String createCreatureInSeparatePage() {
        try {
            // Валидация обязательных полей
            if (newCreature.getName() == null || newCreature.getName().trim().isEmpty()) {
                addMessage("Ошибка", "Имя обязательно");
                return null;
            }

            if (newCreature.getAge() == null || newCreature.getAge() <= 0) {
                addMessage("Ошибка", "Возраст должен быть больше 0");
                return null;
            }

            if (newCreature.getCoordinates() == null) {
                addMessage("Ошибка", "Координаты обязательны");
                return null;
            }

            // Устанавливаем связи по ID

            if (selectedCityId != null) {
                Optional<MagicCity> city = magicCityService.getMagicCityById(selectedCityId);
                city.ifPresent(newCreature::setCreatureLocation);
            }

            if (selectedRingId != null) {
                Optional<Ring> ring = ringService.getRingById(selectedRingId);
                ring.ifPresent(newCreature::setRing);
            }

            // Сохраняем
            BookCreature savedCreature = bookCreatureService.createBookCreature(newCreature);

            // Сбрасываем форму
            initNewCreature();
            selectedCityId = null;
            selectedRingId = null;

            // Редирект на страницу просмотра созданного существа
            return "view-creature?id=" + savedCreature.getId() + "&faces-redirect=true";

        } catch (Exception e) {
            addMessage("Ошибка", "Не удалось создать существо: " + e.getMessage());
            return null;
        }
    }



}