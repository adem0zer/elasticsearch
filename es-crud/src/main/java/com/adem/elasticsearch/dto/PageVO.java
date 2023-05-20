package com.adem.elasticsearch.dto;

import lombok.Data;

@Data
public class PageVO {
    private int page;
    private int pageSize;

    public void setPage(int page) {
        if (page < 1) {
            this.page = 1;
        }
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize < 1 ? 10 : Math.min(pageSize, 1000);
    }
}
