package org.libraries.oauth.web;

import org.evan.libraries.utils.Excludor;
import org.evan.libraries.utils.StringUtil;
import org.libraries.oauth.model.LoginAccount;
import org.libraries.oauth.model.LoginAccountSetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author Evan.Shen
 * @since 2020-07-13
 */
public class LoginAccountFilter implements Filter {
    private final Logger LOGGER = LoggerFactory.getLogger(LoginAccountFilter.class);

    private Set<String> excludesCache = new ConcurrentSkipListSet<>();

    private UrlPathHelper urlPathHelper;

    private Set<String> excludes = new HashSet<>();
    private String defaultToken;
    private LoginSession loginAccountSession;

    @Override
    public void init(FilterConfig filterConfig) {
        if (CollectionUtils.isEmpty(excludes)) {
            excludes.addAll(new Excludor().getExcludes());
        }

        if (urlPathHelper == null) {
            urlPathHelper = new UrlPathHelper();
        }

        LOGGER.info(">>>> LoginAccountFilter inited, excludes [{}]", excludes);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String requestPath = urlPathHelper.getPathWithinApplication(request);

        String token = request.getHeader("token");
        if (StringUtil.isNotBlank(token)) {
            if (!StringUtil.equals(token, defaultToken)) {
                LoginAccount loginAccount = loginAccountSession.get(request);
                if (loginAccount != null) {
                    LoginAccountSetter.put(loginAccount);
                }
            } else {
                LoginAccountSetter.remove();
            }
        }

        chain.doFilter(servletRequest, response);

        LoginAccountSetter.remove();
    }

    @Override
    public void destroy() {
    }

    /***/
    public void setExcludes(Set<String> excludes) {
        this.excludes.clear();
        this.excludes.addAll(excludes);
    }

    /***/
    public void setLoginAccountSession(LoginSession loginAccountSession) {
        this.loginAccountSession = loginAccountSession;
    }

    /***/
    public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
        this.urlPathHelper = urlPathHelper;
    }

    /***/
    public void setDefaultToken(String defaultToken) {
        this.defaultToken = defaultToken;
    }
}
