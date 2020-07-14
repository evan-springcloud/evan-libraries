package org.libraries.oauth.web;

import org.evan.libraries.utils.PathUtil;
import org.evan.libraries.utils.StringUtil;
import org.libraries.oauth.model.LoginAccount;
import org.libraries.oauth.model.LoginAccountSetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 鉴权控制拦截器,用途 :<br>
 * 1、判断token <br>
 * 2、判断是否登录<br>
 * 3、判断权限<br>
 * <p>
 * create at 2016年5月2日 下午9:13:25
 *
 * @author shen.wei
 * @version %I%, %G%
 */
public abstract class AbstractAuthInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAuthInterceptor.class);

    private Set<Method> notRequiredLoginMethodCache = Collections.synchronizedSet(new HashSet<Method>());

    private Set<Method> noAuthCache = Collections.synchronizedSet(new HashSet<Method>());

    private Map<Method, Set<String>> methodFunctionCache = new ConcurrentHashMap<>();

    private Set<String> notRequiredLoginPath;

    private UrlPathHelper urlPathHelper;

    private String defaultToken;

    private String defaultTokenSecret;

    private AbstractLoginAccountSession LoginAccountSession;

    public void init() {
        if (urlPathHelper == null) {
            urlPathHelper = new UrlPathHelper();
        }

        LOGGER.info(">>>> AuthInterceptor Inited, AuthInterceptor class[{}], {}", this.getClass(), notRequiredLoginPath);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestPath = urlPathHelper.getPathWithinApplication(request);

        if (!HandlerMethod.class.isInstance(handler)) { //只要不是HandlerMethod，都认为是404
            //LOGGER.warn(">>>> URI[{}],{} is not execute instance of HandlerMethod,", requestPath, handler);
            //return false;
            HttpHeaders httpHeaders = new HttpHeaders();
            throw new NoHandlerFoundException(request.getMethod(), requestPath, httpHeaders);
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        validate(request, method);

        return true;
    }

    /**
     * 默认验证逻辑，子类可重写
     *
     * @param request
     * @param method
     */
    protected void validate(HttpServletRequest request, Method method) {
        String requestPath = urlPathHelper.getPathWithinApplication(request);

        String token = request.getHeader("token");

        if (StringUtil.isBlank(token)) {
            LOGGER.warn(">>>> No token in request [{}], method [{}]", requestPath, method);
            throw new NoTokenException();
        } else {
            String random = request.getHeader("random");
            String sign = request.getHeader("sign");

            String tokenSecret = null;

            if (StringUtil.equals(token, defaultToken)) {
                tokenSecret = defaultTokenSecret;
            } else {
                LoginAccount loginAccount = LoginAccountSession.get(request);

                if (loginAccount != null) {
                    tokenSecret = loginAccount.getTokenSecret();
                    LoginAccountSetter.put(loginAccount);
                } else if (PathUtil.matches(requestPath, notRequiredLoginPath)) { //当前请求不是不需要登录的地址
                    doNotDefaultTokenAndNoLogin(method, requestPath);
                } else if (notRequiredLoginMethodCache.contains(method)) {
                    doNotDefaultTokenAndNoLogin(method, requestPath);
                } else if (AnnotationUtils.getAnnotation(method, IgnoreLoginAuth.class) != null) {
                    doNotDefaultTokenAndNoLogin(method, requestPath);
                    notRequiredLoginMethodCache.add(method);
                } else {
                    LOGGER.warn(">>>> No login in request [{}], method [{}]", requestPath, method);
                    throw new NoLoginException();
                }
            }
        }
        //TODO 验证签名
    }

    /**
     * 处理当前请求不需要登录，但传入了非默认TOKEN，并且会话不存在的情况
     * @param method
     * @param requestPath
     */
    private void doNotDefaultTokenAndNoLogin(Method method, String requestPath) {
        //TODO 当前请求不需要登录，但传入了非默认TOKEN，并且会话不存在的情况
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>>> 当前请求无需登录，但传入了非默认TOKEN, [{}], method [{}]", requestPath, method);
        }
    }

    /**
     * @param requestPath
     * @param method
     */
    protected void validateLoginUser(String requestPath, Method method) {

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    }

    private void validatePermissions(HttpServletRequest request, String requestPath, Method handlerMethod) {
    }

    /**
     * 获取允许的Functions
     *
     * @param handlerMethod
     * @return
     */
    private Set<String> getAllowFunctions(Method handlerMethod) {
        Set<String> functions = null;

        if (noAuthCache.contains(handlerMethod)) { // 该method在不需要权限控制的methed缓存中，则不需判断
            return functions;
        }

        functions = this.methodFunctionCache.get(handlerMethod);
        if (functions == null) {
            functions = getDefindFunctions(handlerMethod);
            // 没有配置@UserAuthority
            if (functions.size() == 0) {
                // 该方法或类没有配置UserAuthority,将handlerMethod加入noControlCaches缓存
                noAuthCache.add(handlerMethod);
            } else {
                // 配置了@UserAuthority，将配置的的权限加入缓存
                this.methodFunctionCache.put(handlerMethod, functions);
            }
        }

        return functions;
    }

    /**
     * 获取方法或类上定义的允许的功能Id
     * <p>
     * author: Evan.Shen<br>
     * version: 2013-2-28 上午10:47:57 <br>
     *
     * @param handlerMethod
     * @return
     */
    private Set<String> getDefindFunctions(Method handlerMethod) {
        LoginAccountAuthority auth1 = null, auth2 = null;
        Set<String> defindFunctions = new HashSet<String>();

        // 取method上的@UserAuthority
        auth1 = AnnotationUtils.getAnnotation(handlerMethod, LoginAccountAuthority.class);
        addFunToSet(auth1, defindFunctions);

        // 取method上没有配置@UserAuthority
        if (defindFunctions.isEmpty()) {
            // 取class上的@UserAuthority
            auth2 = AnnotationUtils.getAnnotation(handlerMethod.getDeclaringClass(), LoginAccountAuthority.class);
            addFunToSet(auth2, defindFunctions);
        }

        return defindFunctions;
    }

    /**
     * <p>
     * author: Evan.Shen<br>
     * version: 2013-2-28 上午11:05:24 <br>
     *
     * @param auth
     * @param defindFunctions
     */
    private void addFunToSet(LoginAccountAuthority auth, Set<String> defindFunctions) {
        if (auth != null && auth.value() != null && auth.value().length > 0) {
            for (String s : auth.value()) {
                defindFunctions.add(s);
            }
        }
    }

    public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
        this.urlPathHelper = urlPathHelper;
    }

    /***/
    public void setDefaultToken(String defaultToken) {
        this.defaultToken = defaultToken;
    }

    /***/
    public void setDefaultTokenSecret(String defaultTokenSecret) {
        this.defaultTokenSecret = defaultTokenSecret;
    }

    /***/
    public void setLoginAccountSession(AbstractLoginAccountSession loginAccountSession) {
        LoginAccountSession = loginAccountSession;
    }

    /***/
    public void setNotRequiredLoginPath(Set<String> notRequiredLoginPath) {
        this.notRequiredLoginPath = notRequiredLoginPath;
    }
}
