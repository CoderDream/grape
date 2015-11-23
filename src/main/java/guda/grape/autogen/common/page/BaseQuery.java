package guda.grape.autogen.common.page;

public class BaseQuery {
	
    private int pageSize = 10;

    private int startRow = 0;

    private int pageNo = 1;

    private int totalCount;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        if (pageSize <= 1) {
            pageSize = 10;
        }
        startRow = (pageNo - 1) * pageSize;
        this.pageSize = pageSize;
    }

    public int getStartRow() {
        return startRow;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        if (pageNo <= 1) {
            pageNo = 1;
        }
        startRow = (pageNo - 1) * pageSize;
        this.pageNo = pageNo;
    }


    public int getMaxPage() {
        if (totalCount <= 1) {
            return 1;
        }
        if (pageSize <= 1) {
            return totalCount;
        }
        return (totalCount + pageSize - 1) / pageSize;
    }

   

    public int getPageCount() {
        return getMaxPage();
    }

    public boolean hasNextPage() {
        int currentPage = getCurrentPage();
        return currentPage < getMaxPage();
    }

    public boolean hasPrevPage() {
        int currentPage = getCurrentPage();
        return currentPage > 1;
    }

    public int getNextPage() {
        int currentPage = getCurrentPage();
        if (hasNextPage()) {
            return (currentPage + 1);
        }
        return currentPage;
    }

    public int getPrevPage() {
        int currentPage = getCurrentPage();
        if (hasPrevPage()) {
            return currentPage - 1;
        }
        return currentPage;
    }

    public boolean turnNext() {
        if (hasNextPage()) {
            pageNo = getNextPage();
            return true;
        }
        return false;
    }

    public boolean turnPrev() {
        if (hasPrevPage()) {
            pageNo = getPrevPage();
            return true;
        }
        return false;
    }

    public boolean turn(int page) {
        int maxPage = getMaxPage();
        if (page > maxPage) {
            pageNo = maxPage;
            return false;
        }
        if (page < 1) {
            pageNo = 1;
            return false;
        }
        pageNo = page;
        return true;
    }

    public int getCurrentPage() {
        int maxPage = getMaxPage();
        if (pageNo > maxPage) {
            return maxPage;
        }
        if (maxPage < 1) {
            return 1;
        }
        return pageNo;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        int page = (totalCount + pageSize - 1) / pageSize + 1;
        if (pageNo > page) {
            pageNo = page;
        }
    }

}
