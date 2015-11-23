/**
 * tiaotiaogift.com Inc.
 * Copyright (c) 2002-2012 All Rights Reserved.
 */
package guda.grape.autogen.common.page;

import java.util.Collection;
import java.util.Collections;

/**
 * 
 * @author gang
 * @version $Id: Page.java, v 0.1 2012-12-15 上午11:18:43 gang Exp $
 */
public class PageResult {

    public static final int DEFAULT_PAGE_SIZE = 20;

    private int             pageId            = 1;

    private int             total             = 0;

    private int             pageCount         = 0;

    private int             pageSize          = DEFAULT_PAGE_SIZE;

    private Collection<?>   result            = Collections.emptyList();

    public PageResult(int pageId, int total, Collection<?> result) {
        if (total > 0) {
            this.pageId = pageId;
            this.total = total;
            this.result = result;
            countTotalpage();
        }
    }

    private void countTotalpage() {
        this.pageCount = 1;
        if ((this.total % this.pageSize) > 0) {
            this.pageCount = this.total / this.pageSize + 1;
        } else {
            this.pageCount = this.total / this.pageSize;
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public Collection<?> getResult() {
        return result;
    }

    public void setResult(Collection<?> result) {
        this.result = result;
    }

}
