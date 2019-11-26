package org.libraries.oauth.web;

import org.evan.libraries.exception.ServiceException;
import org.evan.libraries.model.result.OperateResultConstants;

/**
 * 缺少token异常
 *
 * @author Evan.Shen
 * @since 2019-09-30
 */
public class NoTokenException extends ServiceException {
    private static final long serialVersionUID = 3583566093089790852L;

    private static final String CODE = OperateResultConstants.NO_TOKEN.getCode();

    public NoTokenException(String msg) {
        super(CODE, msg);
    }

    public NoTokenException() {
        super(CODE, OperateResultConstants.NO_TOKEN.getMsg());
    }
}
