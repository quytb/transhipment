package com.havaz.transport.api.form;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PageImplForm extends PageImpl {
    private final long total;

    public PageImplForm(List content, Pageable pageable, long total) {
        super(content,pageable,total);
        this.total = (Long)pageable.toOptional().filter((it) -> {
            return !content.isEmpty();
        }).filter((it) -> {
            return it.getOffset() + (long)it.getPageSize() > total;
        }).map((it) -> {
            return it.getOffset() + (long)content.size();
        }).orElse(total);
    }

}
