package org.libraries.oauth.web;


import org.evan.libraries.exception.ServiceException;
import org.evan.libraries.model.result.OperateResultConstants;

/**
 * Created on 2017/9/5.
 *
 * @author evan.shen
 */
public class RemotingAddrExcetion extends ServiceException {
    private static final long serialVersionUID = -951777198106425369L;

    public RemotingAddrExcetion() {
        super(OperateResultConstants.REMOTING_ADDR_WRONG);
    }

    public RemotingAddrExcetion(String message) {
        super(OperateResultConstants.REMOTING_ADDR_WRONG.getCode(), message);
    }
}
