package org.evan.libraries.model.result;


import java.io.Serializable;
import java.util.List;

import static org.evan.libraries.model.result.OperateResult.DEFAULT_CODE;

/**
 * Restful列表（非分页）接口输出格式封装
 *
 * @author shenwei
 * @version 19/11/15
 * @since 1.1
 */
public class RestListResponse<T> implements Serializable {
    private static final long serialVersionUID = 2941991018140190488L;

    private String code = DEFAULT_CODE;
    private String msg = DEFAULT_CODE;
    private List<T> data;

    public RestListResponse() {
    }

    private RestListResponse(String code, String msg, List<T> data) {
        if (code != null && !"".equals(code.trim())) {
            this.code = code;
        }
        if (msg != null && !"".equals(msg.trim())) {
            this.msg = msg;
        }
        this.data = data;
    }

    public static <T> RestListResponse<T> create(String code, String msg, List<T> data) {
        return new RestListResponse(code, msg, data);
    }

    public static <T> RestListResponse<T> create(List<T> data) {
        return new RestListResponse(null, null, data);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RestListResponse{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}


