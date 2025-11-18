package com.antonov.is1.beans;

import com.antonov.is1.entities.BookCreature;
import com.antonov.is1.entities.Ring;
import com.antonov.is1.services.BookCreatureService;
import com.antonov.is1.services.MagicCityService;
import com.antonov.is1.services.RingService;
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
import java.util.Set;
import java.util.stream.Collectors;

@Named
@ViewScoped
@Getter @Setter
public class SpecialOperationsBean implements Serializable {

    @Inject
    private BookCreatureService bookCreatureService;

    @Inject
    private RingService ringService;

    @Inject
    private MagicCityService magicCityService;

    // Результаты операций
    private BookCreature creatureWithMaxCreationDate;
    private List<Ring> uniqueRings;
    private Long attackLevelToDelete;
    private String operationResult;

    @PostConstruct
    public void init() {
        // Инициализация при необходимости
    }

    // ===== РАБОЧИЕ ОПЕРАЦИИ =====

    /**
     * Вернуть один (любой) объект, значение поля creationDate которого является максимальным
     */
    public void findCreatureWithMaxCreationDate() {
        try {
            List<BookCreature> allCreatures = bookCreatureService.getAllBookCreatures();
            if (!allCreatures.isEmpty()) {
                creatureWithMaxCreationDate = allCreatures.stream()
                        .max((c1, c2) -> c1.getCreationDate().compareTo(c2.getCreationDate()))
                        .orElse(null);

                if (creatureWithMaxCreationDate != null) {
                    addMessage("Успех",
                            "Найдено существо с максимальной датой создания: " +
                                    creatureWithMaxCreationDate.getName() +
                                    " (ID: " + creatureWithMaxCreationDate.getId() + ")");
                }
            } else {
                addMessage("Информация", "В базе нет существ");
            }
        } catch (Exception e) {
            addMessage("Ошибка", "Не удалось выполнить операцию: " + e.getMessage());
        }
    }

    /**
     * Вернуть массив уникальных значений поля ring по всем объектам
     */
    public void findUniqueRings() {
        try {
            List<BookCreature> allCreatures = bookCreatureService.getAllBookCreatures();
            Set<Ring> rings = allCreatures.stream()
                    .filter(creature -> creature.getRing() != null)
                    .map(BookCreature::getRing)
                    .collect(Collectors.toSet());

            uniqueRings = rings.stream().collect(Collectors.toList());

            if (!uniqueRings.isEmpty()) {
                addMessage("Успех", "Найдено уникальных колец: " + uniqueRings.size());
            } else {
                addMessage("Информация", "Нет существ с кольцами");
            }
        } catch (Exception e) {
            addMessage("Ошибка", "Не удалось выполнить операцию: " + e.getMessage());
        }
    }

    // ===== ЗАГЛУШКИ =====

    /**
     * Удалить все объекты, значение поля attackLevel которого эквивалентно заданному
     */
    public void deleteByAttackLevel() {
        // Заглушка - просто показываем сообщение
        if (attackLevelToDelete == null) {
            addMessage("Ошибка", "Введите значение attackLevel");
            return;
        }

        try {
            // Здесь будет реальная логика удаления
            // Пока просто заглушка
            addMessage("Информация",
                    "Операция в разработке. Должны быть удалены существа с attackLevel = " +
                            attackLevelToDelete);

            // Сброс значения
            attackLevelToDelete = null;

        } catch (Exception e) {
            addMessage("Ошибка", "Не удалось выполнить операцию: " + e.getMessage());
        }
    }

    /**
     * Забрать все кольца у хоббитов
     */
    public void takeRingsFromHobbits() {
        try {
            // Заглушка - просто показываем сообщение
            addMessage("Информация",
                    "Операция в разработке. Будут отобраны кольца у всех хоббитов");

        } catch (Exception e) {
            addMessage("Ошибка", "Не удалось выполнить операцию: " + e.getMessage());
        }
    }

    /**
     * Уничтожить города эльфов
     */
    public void destroyElfCities() {
        try {
            // Заглушка - просто показываем сообщение
            addMessage("Информация",
                    "Операция в разработке. Будут уничтожены города, где правителями являются эльфы");

        } catch (Exception e) {
            addMessage("Ошибка", "Не удалось выполнить операцию: " + e.getMessage());
        }
    }

    // ===== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ =====

    private void addMessage(String summary, String detail) {
        operationResult = detail;
        FacesContext.getCurrentInstance().addMessage(
                null, new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail)
        );
    }

}