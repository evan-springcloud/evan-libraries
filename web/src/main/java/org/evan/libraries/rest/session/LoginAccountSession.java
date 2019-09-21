package org.evan.libraries.rest.session;

import org.evan.libraries.model.CurrentLoginAccount;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
public interface LoginAccountSession {
	CurrentLoginAccount get(HttpServletRequest request);
}
