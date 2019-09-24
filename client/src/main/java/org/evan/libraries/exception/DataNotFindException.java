package org.evan.libraries.exception;


import org.evan.libraries.model.result.OperateResultConstants;

/**
 * @author shenWei
 * @since 1.0
 */
public class DataNotFindException extends ServiceException {
    private static final long serialVersionUID = -951778798106425369L;

    public DataNotFindException() {
        super(OperateResultConstants.DATA_NOT_FIND);
    }

    public DataNotFindException(String message) {
        super(OperateResultConstants.DATA_NOT_FIND.getCode(), message);
    }
}
