package com.antonov.is1.beans;

import com.antonov.is1.entities.Ring;
import com.antonov.is1.services.RingService;
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
@Getter @Setter
public class RingListBean implements Serializable {

    @Inject
    private RingService ringService;

    private List<Ring> rings;
    private final int pageSize = 20;
    private int currentPage = 1;
    private long totalCount;

    @PostConstruct
    public void init() {
        loadData();
    }

    public void loadData() {
        rings = ringService.getAllRings(currentPage, pageSize);
        totalCount = ringService.getAllRings().size();
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

    public int getTotalPages() {
        if (totalCount == 0) return 1;
        return (int) Math.ceil((double) totalCount / pageSize);
    }

}