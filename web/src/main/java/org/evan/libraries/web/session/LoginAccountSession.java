package org.evan.libraries.web.session;

import org.evan.libraries.model.AbstractLoginAccount;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
public interface LoginAccountSession {
	AbstractLoginAccount get(HttpServletRequest request);
}
