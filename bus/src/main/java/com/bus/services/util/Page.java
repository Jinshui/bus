package com.bus.services.util;

import java.io.Serializable;
import java.util.*;
import javax.xml.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.data.domain.*;

/**
 * Inspired by Spring Data's Page, but with added ThisTech flavor.
 * @author <a href="mailto:jeffb@thistech.com">Jeff Bailey</a>
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Page<T extends Serializable> implements Serializable {

    /** The current page index **/
    @XmlAttribute
    private int pageIndex;
    /** The page size **/
    @XmlAttribute
    private int pageSize;
    /** Total number of elements across all pages **/
    @XmlAttribute
    private long totalElements;
    /** Total number of pages **/
    @XmlAttribute
    private int totalPages;
    /** If true, this is the first page **/
    @XmlAttribute
    private boolean isFirstPage;
    /** If true, this is the last page **/
    @XmlAttribute
    private boolean isLastPage;
    /** If true, there are pages after this page **/
    @XmlAttribute
    private boolean hasNextPage;
    /** If true, there are pages before this page **/
    @XmlAttribute
    private boolean hasPreviousPage;
    /** The contents **/
    @JsonProperty("contents")
    @JsonTypeInfo(use= JsonTypeInfo.Id.CLASS, include= JsonTypeInfo.As.PROPERTY, property="clazz")
    private List<T> contents;
    /** The orders used to sort the contents **/
    @JsonProperty("orders")
    private List<Order> orders;

    public Page() {}

    public Page(List<T> content, Pageable pageable, long totalElements) {
        this.pageIndex = pageable.getPageNumber();
        this.pageSize = pageable.getPageSize();
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / (double) pageSize);
        this.isFirstPage = pageIndex == 0;
        this.isLastPage = (this.pageIndex + 1) * pageSize >= totalElements;
        this.hasNextPage = !isLastPage;
        this.hasPreviousPage = !isFirstPage;
        this.contents = new ArrayList<T>(content);
        if (pageable.getSort() != null) {
            orders = new ArrayList<>();
            for (Sort.Order order : pageable.getSort()) {
                orders.add(new Order(order));
            }
        }
    }

    public Page(List<T> content) {
        this.pageIndex = 0;
        this.pageSize = content.size();
        this.totalElements = content.size();
        this.totalPages = 1;
        this.isFirstPage = true;
        this.isLastPage = true;
        this.hasNextPage = false;
        this.hasPreviousPage = false;
        this.contents = new ArrayList<T>(content);
    }


    public List<T> getContents() {
        if (contents == null) { contents = new ArrayList<>(); }
        return contents;
    }
    public Page setContents(List<T> value) { this.contents = value; return this; }

    public int getPageIndex() { return pageIndex; }
    public Page setPageIndex(int value) { this.pageIndex = value; return this; }

    public int getPageSize() { return pageSize; }
    public Page setPageSize(int value) { this.pageSize = value; return this; }

    public long getTotalElements() { return totalElements; }
    public Page setTotalElements(long value) { this.totalElements = value; return this; }

    public int getTotalPages() { return totalPages; }
    public Page setTotalPages(int value) { this.totalPages = value; return this; }

    public List<Order> getOrders() {
        if (orders == null) { orders = new ArrayList<>(); }
        return orders;
    }
    public Page setOrders(List<Order> value) { this.orders = value; return this; }

    public boolean getIsFirstPage() { return isFirstPage; }
    public Page setIsFirstPage(boolean value) { this.isFirstPage = value; return this; }

    public boolean getIsLastPage() { return isLastPage; }
    public Page setIsLastPage(boolean value) { this.isLastPage = value; return this; }

    public boolean getHasNextPage() { return hasNextPage; }
    public Page setHasNextPage(boolean value) { this.hasNextPage = value; return this; }

    public boolean getHasPreviousPage() { return hasPreviousPage; }
    public Page setHasPreviousPage(boolean value) { this.hasPreviousPage = value; return this; }

    @Override
    public String toString() {
        String contentType = "UNKNOWN";
        if (contents.size() > 0) {
            contentType = contents.get(0).getClass().getSimpleName();
        }
        return String.format("Page %s of %d of %s", pageIndex, totalPages, contentType);
    }
}