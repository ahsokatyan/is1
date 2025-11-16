package com.antonov.is1.beans;

import com.antonov.is1.entities.MagicCity;
import com.antonov.is1.services.MagicCityService;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
@Getter
@Setter
public class MagicCityListBean implements Serializable {

    @Inject
    private MagicCityService magicCityService;

    private List<MagicCity> cities;
    private final int pageSize = 20;
    private int currentPage = 1;
    private long totalCount;

    @PostConstruct
    public void init() {
        loadData();
    }

    public void loadData() {
        cities = magicCityService.getAllMagicCities(currentPage, pageSize);
        totalCount = magicCityService.getAllMagicCities().size();
    }

    // Пагинация
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

}