package org.vaadin.example.views.masterdetail;

import java.util.List;

public class PageResult<T> {
    private final List<T> pageItems;
    private final long totalCount;

    public PageResult(List<T> items, long totalCount) {
        this.pageItems = items;
        this.totalCount = totalCount;
    }
}
