package org.evan.libraries.model.result;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Restful接口输出格式封装
 *
 * @author shenwei
 * @version 17/3/11 下午11:57
 * @since 1.0
 */
public class RestResponse<T> extends OperateResult<T> implements Serializable {
    private static final long serialVersionUID = 2941991011401190488L;

    private Page page;

    public RestResponse() {
        super();
    }

    private RestResponse(String code, String msg, T data) {
        super(code, msg, data);
    }

    public static <T> RestResponse<T> create() {
        return new RestResponse();
    }

    public static <T> RestResponse<T> create(String code, String msg, Serializable data) {
        return new RestResponse(code, msg, data);
    }

    public static <T> RestResponse<T> create(String code, String msg) {
        return new RestResponse(code, msg, null);
    }

    public static <T> RestResponse<T> create(T data) {
        return new RestResponse(null, null, data);
    }

    public static <T> RestResponse<T> create(PageResult pageResult) {
        RestResponse<T> response = new RestResponse();
        response.setPageResult(pageResult);

        if (pageResult == null || pageResult.getData() == null) {
            response.setData((T) new ArrayList());
        } else {
            response.setData((T) pageResult.getData());
        }

        return response;
    }

    public static <T> RestResponse<T> create(OperateResult<T> operateResult) {
        RestResponse response = new RestResponse();

        response.setCode(operateResult.getCode());
        response.setData(operateResult.getData());
        response.setMsg(operateResult.getMsg());

        return response;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public void setPageResult(PageResult pageResult) {
        if (page == null) {
            page = new Page();
        }
        page.setPageNo(pageResult.getPageNo());
        page.setPageCount(pageResult.getPageCount());
        page.setPageSize(pageResult.getPageSize());
        page.setRecordCount(pageResult.getRecordCount());
        this.setPage(page);
        this.setData((T) pageResult.getData());
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
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

    @Override
    public String toString() {
        return "Page [getPageSize()=" + getPageSize() + ", getPageNo()=" + getPageNo() + ", getRecordCount()="
                + getRecordCount() + ", getPageCount()=" + getPageCount() + "]";
    }
}
