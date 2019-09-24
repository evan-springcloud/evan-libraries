package org.evan.libraries.exception;


import org.evan.libraries.model.result.OperateResultConstants;

/**
 * 没有权限异常
 * <p/>
 *
 * @author ShenWei
 * @version 2010-12-12 下午05:14:55
 * @since 1.0
 */
public class PermissionDeniedException extends ServiceException {

    private static final long serialVersionUID = -19517187915506429L;

    public PermissionDeniedException() {
        super(OperateResultConstants.NO_ACCESS);
    }

    public PermissionDeniedException(String message) {
        super(OperateResultConstants.NO_ACCESS.getCode(), message);
    }
}
