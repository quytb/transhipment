package com.havaz.transport.dao.query;

import org.springframework.data.domain.Pageable;

public class SelectOptions {
    private long count;
    private int offset;
    private int limit;
    private boolean countFlag;

    private SelectOptions() {
        countFlag = false;
        limit = Integer.MAX_VALUE;
        offset = 0;
    }

    public static SelectOptions get() {
        return new SelectOptions();
    }

    public static SelectOptions get(Pageable pageable) {
        SelectOptions options = get();
        if (pageable == null) {
            return options;
        }
        return options.pageable(pageable);
    }

    private SelectOptions pageable(Pageable pageable) {
        this.offset((int) pageable.getOffset());
        this.limit(pageable.getPageSize());
        return this;
    }

    public SelectOptions count() {
        countFlag = true;
        return this;
    }

    public SelectOptions offset(int offset) {
        this.offset = Math.max(offset, 0);
        return this;
    }

    public SelectOptions limit(int limit) {
        if (limit < 0) {
            this.limit = Integer.MAX_VALUE;
        } else {
            this.limit = limit;
        }
        return this;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public boolean isCount() {
        return countFlag;
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }
}
