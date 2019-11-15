package org.evan.libraries.model.result;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Restful分页接口输出格式封装
 *
 * @author shenwei
 * @version 19/11/15
 * @since 1.1
 */
public class RestPageResponse<T> extends RestListResponse<T> implements Serializable {
    private static final long serialVersionUID = 2941991011401190488L;

    private Page page;

    public RestPageResponse() {
        super();
    }

    public static <T> RestPageResponse<T> create() {
        return new RestPageResponse();
    }

    public static <T> RestPageResponse<T> create(PageResult pageResult) {
        RestPageResponse<T> response = new RestPageResponse();
        response.setPageResult(pageResult);

        if (pageResult == null || pageResult.getData() == null) {
            response.setData(new ArrayList());
        } else {
            response.setData(pageResult.getData());
        }

        return response;
    }

    public Page getPage() {
        return page;
    }

    public void setPageResult(PageResult pageResult) {
        if (page == null) {
            page = new Page();
        }
        page.setPageNo(pageResult.getPageNo());
        page.setPageCount(pageResult.getPageCount());
        page.setPageSize(pageResult.getPageSize());
        page.setRecordCount(pageResult.getRecordCount());
        this.setData(pageResult.getData());
    }

    @Override
    public String toString() {
        return "RestResponse{" +
                "page=" + page +
                "} " + super.toString();
    }
}

class Page {
    private Integer pageSize;
    private Integer pageNo;
    private Long recordCount;
    private Integer pageCount;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public Long getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(long recordCount) {
        this.recordCount = recordCount;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    /**
     * 根据pageNo和pageSize计算当前页第一条记录在总结果集中的位置,序号从1开始.
     */
    public int getFirst() {
        return (pageNo - 1) * pageSize + 1;
    }

    /**
     * 是否还有下一页.
     */
    public boolean isHasNext() {
        return pageNo < pageCount;
    }

    /**
     * 取得下页的页号, 序号从1开始. 当前页为尾页时仍返回尾页序号.
     */
    public int getNextPage() {
        if (isHasNext()) {
            return pageNo + 1;
        } else {
            return pageNo;
        }
    }

    /**
     * 是否还有上一页.
     */
    public boolean isHasPre() {
        return pageNo > 1;
    }

    /**
     * 取得上页的页号, 序号从1开始. 当前页为首页时返回首页序号.
     */
    public int getPrePage() {
        if (isHasPre()) {
            return pageNo - 1;
        } else {
            return pageNo;
        }
    }
}
