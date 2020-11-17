package org.evan.libraries.model.query;

/**
 * 适用于查询条件比较少rest controller method
 *
 * @author Evan.Shen
 * @since 1.5.1
 *
 */
public class SimplePageQueryDTO {

    private int pageNo = 1;

    private int pageSize = 10;

    /**
     *
     */
    public int getPageNo() {
        return pageNo;
    }

    /***/
    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    /**
     *
     */
    public int getPageSize() {
        return pageSize;
    }

    /***/
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
