package org.evan.libraries.web.session;

import org.evan.libraries.web.utils.Excludor;
import org.evan.libraries.web.utils.WebContextUtils;
import org.evan.libraries.utils.PathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.PathMatcher;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * <p>
 * create at 2014年4月18日 下午5:25:10
 *
 * @author shen.wei
 * @version %I%, %G%
 */
public abstract class AbstractLoginContextFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractLoginContextFilter.class);
    protected Excludor excludor;
    protected PathMatcher pathMatcher;
    protected UrlPathHelper urlPathHelper;
    protected WebContextUtils webContextUtils;
    private Set<String> matchesCache = new ConcurrentSkipListSet<>();

    @Override
    public void init(FilterConfig filterConfig) {
        if (urlPathHelper == null) {
            urlPathHelper = new UrlPathHelper();
        }
        if (excludor == null) {
            excludor = new Excludor();
        }

        LOGGER.info(">>>> {} inited, excludes [{}]", this.getClass().getSimpleName(), excludor.toString());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String requestPath = urlPathHelper.getPathWithinApplication(request);
        //String requestPath = urlPathHelper.getPathWithinServletMapping(request);

        boolean isNeedAuth = false;
        if (matchesCache.contains(requestPath)) {
            isNeedAuth = true;
        } else {
            if (!PathUtil.matches(requestPath, excludor.getExcludes())) {
                isNeedAuth = true;
                matchesCache.add(requestPath);
            }
        }

        if (isNeedAuth) {
            process(request);
        }

        chain.doFilter(request, response);
    }

    public abstract void process(HttpServletRequest request);


    public void destroy() {
    }

    public void setExcludor(Excludor excludor) {
        this.excludor = excludor;
    }

    public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
        this.urlPathHelper = urlPathHelper;
    }

    public void setWebContextUtils(WebContextUtils webContextUtils) {
        this.webContextUtils = webContextUtils;
    }
}
