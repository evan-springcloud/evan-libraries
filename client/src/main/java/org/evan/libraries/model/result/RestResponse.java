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
    private static final long serialVersionUID = 294199101140210488L;

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

    public static <T> RestResponse<T> create(OperateResult<T> operateResult) {
        RestResponse response = new RestResponse();

        response.setCode(operateResult.getCode());
        response.setData(operateResult.getData());
        response.setMsg(operateResult.getMsg());

        return response;
    }
}
