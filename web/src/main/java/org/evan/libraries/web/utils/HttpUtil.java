package org.evan.libraries.web.utils;


import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang3.StringUtils;
import org.evan.libraries.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * http请求和响应的工具类，主要处理字符编码和其它一些信息
 *
 * @author tao.wangt
 * @version $Id: HttpUtils.java,v 0.1 2009-12-28 涓嬪崍12:56:41 tao.wangt Exp $
 */
public class HttpUtil {
    // -- Content Type 定义 --//
    public static final String TEXT_TYPE = "text/plain";
    public static final String JSON_TYPE = "application/json";
    public static final String XML_TYPE = "text/xml";
    public static final String HTML_TYPE = "text/html";
    public static final String JS_TYPE = "text/javascript";
    public static final String EXCEL_TYPE = "application/vnd.ms-excel";
    // -- Header 定义 --//
    public static final String AUTHENTICATION_HEADER = "Authorization";
    // -- 常用数值定义 --//
    public static final long ONE_YEAR_SECONDS = 60 * 60 * 24 * 365;

    private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);
    private static final String CONTENT_ATTRIBUTE = "javax.servlet.content";


    /**
     * 输出HttpServletRequest的信息，包括header部分
     *
     * @param request <p>
     *                author: ShenWei<br>
     *                create at 2014年9月24日 上午11:28:40
     */
    public static void printRequest(HttpServletRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("request.getContentType():" + request.getContentType());
            log.debug("request.getContentLength():" + request.getContentLength());
            log.debug("request.getHttpServletMapping():" + request.getHttpServletMapping());
            log.debug("request.getLocalAddr():" + request.getLocalAddr());
            log.debug("request.getLocalName():" + request.getLocalName());
            log.debug("request.getLocalPort():" + request.getLocalPort());
            log.debug("request.getPathInfo():" + request.getPathInfo());
            log.debug("request.getPathTranslated():" + request.getPathTranslated());
            log.debug("request.getProtocol():" + request.getProtocol());
            log.debug("request.getQueryString():" + request.getQueryString());
            log.debug("request.getRemoteAddr():" + request.getRemoteAddr());
            log.debug("request.getRemoteHost():" + request.getRemoteHost());
            log.debug("request.getRemotePort():" + request.getRemotePort());
            log.debug("request.getRemoteUser():" + request.getRemoteUser());
            log.debug("request.getRequestURI():" + request.getRequestURI());
            log.debug("request.getServletPath():" + request.getServletPath());
            log.debug("request.getScheme():" + request.getScheme());
            log.debug("request.getServerName():" + request.getServerName());
            log.debug("request.getServerPort():" + request.getServerPort());
            log.debug("request.getUserPrincipal():" + request.getUserPrincipal());


            Enumeration<String> names = request.getHeaderNames();
            for (Enumeration<String> e = names; e.hasMoreElements(); ) {
                String thisName = e.nextElement().toString();
                String thisValue = request.getHeader(thisName);
                log.debug("request.getHeader(" + thisName + "):" + thisValue);
            }
        }

    }

    /**
     * 获得应用根地址
     *
     * @param request
     */
    public static String getAppURL(HttpServletRequest request) {
        StringBuilder url = new StringBuilder();
        int port = request.getServerPort();
        if (port < 0) {
            port = 80; // Work around java.net.URL bug
        }
        String scheme = request.getScheme();
        url.append(scheme);
        url.append("://");
        url.append(request.getServerName());
        if ((scheme.equals("http") && (port != 80)) || (scheme.equals("https") && (port != 443))) {
            url.append(':');
            url.append(port);
        }
        url.append(request.getContextPath());
        //url.append(request.getServletPath());

        if (url.lastIndexOf("/") == url.length() - 1) {
            url.deleteCharAt(url.length() - 1);
        }

        return url.toString();
    }

    /**
     * 获取请求参数,返回map
     *
     * @param request
     */
    public static Map<String, String> getRequestParameters(HttpServletRequest request) {
        Map<String, String> paraMap = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            if (null != values && values.length > 0) {
                StringBuilder valueSb = new StringBuilder();
                for (int i = 0; i < values.length; i++) {
                    valueSb.append(values[i]).append(",");
                }
                valueSb.deleteCharAt(valueSb.length() - 1);
                if (valueSb.length() > 0 && StringUtils.isNotBlank(name)) {
                    paraMap.put(name, valueSb.toString());
                }
            }

        }
        return paraMap;
    }

    /**
     * 获取URL和参数
     *
     * @param request <br>
     *                create by shen.wei as
     *                2010-11-4
     */
    public static String getRequestURLAndQuery(HttpServletRequest request) {
        StringBuilder url = new StringBuilder();

        url.append(getAppURL(request));
        url.append(request.getRequestURI());

        if (request.getQueryString() != null) {
            url.append("?" + request.getQueryString());
        }

        return url.toString();
    }

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr()的原因是有可能用户使用了代理软件方式避免真实IP地址
     *
     * @param request 客户端Ip
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP"); //nginx代理一般会加上此请求头

        if (StringUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) { //这是一个 Squid 开发的字段，只有在通过了HTTP代理或者负载均衡服务器时才会添加该项。　　　　
            ip = request.getHeader("x-forwarded-for");
            if (StringUtil.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
                // 多次反向代理后会有多个ip值，第一个ip才是真实ip
                if (ip.indexOf(",") != -1) {
                    ip = ip.split(",")[0];
                }
            }
        }
        if (StringUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) { //apache代理方式部署时
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {//apache代理方式部署时
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) { //apache代理方式部署时
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) { //有些代理服务器会加上此请求头
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    /**
     * 根据IP获取MAC地址
     *
     * @return
     */
    public static String getMacAddr(String remoteAddr) {
        String macAddr = null;
        try {
            Process process = Runtime.getRuntime().exec("nbtstat -a " + remoteAddr);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            Pattern pattern = Pattern.compile("([A-F0-9]{2}-){5}[A-F0-9]{2}");
            Matcher matcher;
            for (String strLine = br.readLine(); strLine != null;
                 strLine = br.readLine()) {
                matcher = pattern.matcher(strLine);
                if (matcher.find()) {
                    macAddr = matcher.group();
                    break;
                }
            }
        } catch (IOException e) {
            log.error(" >>>> 读取mac地址出错,ip [" + remoteAddr + "],ex:" + e.getMessage());
        }
        return macAddr;
    }

    /**
     * 获取系统版本信息
     *
     * @param request
     * @return
     */
    public static String getSystemInfo(HttpServletRequest request) {
        String systenInfo = null;
        String header = request.getHeader("user-agent");
        if (header == null || header.equals("")) {
            return "";
        }

        //得到用户的操作系统
        if (header.indexOf("NT 6.0") > 0) {
            systenInfo = "Windows Vista/Server 2008";
        } else if (header.indexOf("NT 5.2") > 0) {
            systenInfo = "Windows Server 2003";
        } else if (header.indexOf("NT 6.0") > 0) {
            systenInfo = "Windows Vista";
        } else if (header.indexOf("NT 6.1") > 0) {
            systenInfo = "Windows 7";
        } else if (header.indexOf("NT 6.2") > 0) {
            systenInfo = "Windows Slate";
        } else if (header.indexOf("NT 6.3") > 0) {
            systenInfo = "Windows 9";
        } else if (header.indexOf("Mac") > 0) {
            systenInfo = "Mac";
        } else if (header.indexOf("Unix") > 0) {
            systenInfo = "UNIX";
        } else if (header.indexOf("Linux") > 0) {
            systenInfo = "Linux";
        } else if (header.indexOf("SunOS") > 0) {
            systenInfo = "SunOS";
        }
        return systenInfo;
    }

    public static UserAgent getUserAgent(String userAgentStr) {
        return UserAgent.parseUserAgentString(userAgentStr);
    }

    /**
     * 获取设备类型
     *
     * @param userAgentStr
     * @return
     */
    public static String getDeviceType(String userAgentStr) {
        if (StringUtils.isEmpty(userAgentStr)) {
            return null;
        }

        UserAgent userAgent = getUserAgent(userAgentStr);

        //获取操作系统对象
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();

        //获取设备类型
        return operatingSystem.getDeviceType().toString();

    }

    /**
     * 获取厂商
     *
     * @param userAgentStr
     * @return
     */
    public static String getDeviceMfrs(String userAgentStr) {
        if (StringUtils.isEmpty(userAgentStr)) {
            return null;
        }

        UserAgent userAgent = getUserAgent(userAgentStr);

        //获取操作系统对象
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();

        //获取厂商
        return operatingSystem.getManufacturer().toString();

    }

    /**
     * 操作系统名称
     *
     * @param userAgentStr
     * @return
     */
    public static String getDeviceName(String userAgentStr) {
        if (StringUtils.isEmpty(userAgentStr)) {
            return null;
        }

        UserAgent userAgent = getUserAgent(userAgentStr);

        //获取操作系统对象
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();

        //操作系统名称
        return operatingSystem.getName();
    }

    /**
     * 获取设备类型
     *
     * @param userAgentStr
     * @return
     */
    public static String getDeviceModel(String userAgentStr) {
        if (StringUtils.isEmpty(userAgentStr)) {
            return null;
        }

        UserAgent userAgent = getUserAgent(userAgentStr);

        //获取操作系统对象
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        //操作系统名称
        String osName = operatingSystem.getName();
        //获取设备类型
        String deviceType = operatingSystem.getDeviceType().toString();

        //获取设备型号
        if ("MOBILE".equals(deviceType)) {
            String split[] = userAgentStr.split("Build/");

            if (split != null || split.length > 0) {
                String splitType[] = split[0].split(";");
                if (splitType != null && splitType.length > 0) {
                    return splitType[splitType.length - 1];
                }

            }

        } else if ("TABLET".equals(deviceType)) {
            String split[] = userAgentStr.split("Build/");

            if (split != null || split.length > 0) {
                String splitType[] = split[0].split(";");
                if (splitType != null && splitType.length > 0) {
                    return splitType[splitType.length - 1];
                }

            }
        }

        return osName;
    }

    /**
     * 获取返回内容
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static String getRequestContent(HttpServletRequest request) throws IOException {
        String returnV = null;
        Object o = request.getAttribute(CONTENT_ATTRIBUTE);
        if (o == null) {
            StringBuffer sb = new StringBuffer();
            PushbackInputStream pbis = new PushbackInputStream(request.getInputStream());
            InputStreamReader isr = new InputStreamReader(pbis);
            BufferedReader br = new BufferedReader(isr);
            String s = null;
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
            returnV = sb.toString();
            request.setAttribute(CONTENT_ATTRIBUTE, returnV);
        } else {
            returnV = o.toString();
        }
        return returnV;
    }

    /**
     * 判断网络是否可用
     *
     * @param url
     */
    public static boolean isCanConnection(String url) {
        try {
            URL urlInfo = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlInfo.openConnection();
            int state = conn.getResponseCode();
            if (state == 200) {
                return true;
            }
        } catch (Exception e) {
            log.error("the url:" + url + " is can't connection", e);
        }
        return false;
    }

    /**
     * 设置客户端缓存过期时间 的Header.
     */
    public static void setExpiresHeader(HttpServletResponse response, long expiresSeconds) {
        // Http 1.0 header
        response.setDateHeader("Expires", System.currentTimeMillis() + expiresSeconds * 1000);
        // Http 1.1 header
        response.setHeader("Cache-Control", "private, max-age=" + expiresSeconds);
    }

    /**
     * 设置禁止客户端缓存的Header.
     */
    public static void setDisableCacheHeader(HttpServletResponse response) {
        // Http 1.0 header
        response.setDateHeader("Expires", 1L);
        response.addHeader("Pragma", "no-cache");
        // Http 1.1 header
        response.setHeader("Cache-Control", "no-cache, no-store, max-age=0");
    }

    /**
     * 设置LastModified Header.
     */
    public static void setLastModifiedHeader(HttpServletResponse response, long lastModifiedDate) {
        response.setDateHeader("Last-Modified", lastModifiedDate);
    }

    /**
     * 根据浏览器If-Modified-Since Header, 计算文件是否已被修改.
     * <p>
     * 如果无修改, checkIfModify返回false ,设置304 not modify status.
     *
     * @param lastModified 内容的最后修改时间.
     */
    public static boolean checkIfModifiedSince(HttpServletRequest request, HttpServletResponse response,
                                               long lastModified) {
        long ifModifiedSince = request.getDateHeader("If-Modified-Since");
        if ((ifModifiedSince != -1) && (lastModified < ifModifiedSince + 1000)) {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return false;
        }
        return true;
    }

    /**
     * 根据浏览器 If-None-Match Header, 计算Etag是否已无效.
     * <p>
     * 如果Etag有效, checkIfNoneMatch返回false, 设置304 not modify status.
     *
     * @param etag 内容的ETag.
     */
    public static boolean checkIfNoneMatchEtag(HttpServletRequest request, HttpServletResponse response, String etag) {
        String headerValue = request.getHeader("If-None-Match");
        if (headerValue != null) {
            boolean conditionSatisfied = false;
            if (!"*".equals(headerValue)) {
                StringTokenizer commaTokenizer = new StringTokenizer(headerValue, ",");

                while (!conditionSatisfied && commaTokenizer.hasMoreTokens()) {
                    String currentToken = commaTokenizer.nextToken();
                    if (currentToken.trim().equals(etag)) {
                        conditionSatisfied = true;
                    }
                }
            } else {
                conditionSatisfied = true;
            }

            if (conditionSatisfied) {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                response.setHeader("ETag", etag);
                return false;
            }
        }
        return true;
    }

    /**
     * 设置让浏览器弹出下载对话框的Header.
     *
     * @param fileName 下载后的文件名.
     */
    public static void setFileDownloadHeader(HttpServletResponse response, String fileName) {
        try {
            // 中文文件名支持
            String encodedfileName = new String(fileName.getBytes(), "ISO8859-1");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + "\"");
        } catch (UnsupportedEncodingException e) {
        }
    }

    /**
     * 取得带相同前缀的Request Parameters.
     * <p>
     * 返回的结果的Parameter名已去除前缀.
     */
    public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
        Assert.notNull(request, "Request must not be null");
        Enumeration paramNames = request.getParameterNames();
        Map<String, Object> params = new TreeMap<String, Object>();
        if (prefix == null) {
            prefix = "";
        }
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            if ("".equals(prefix) || paramName.startsWith(prefix)) {
                String unprefixed = paramName.substring(prefix.length());
                String[] values = request.getParameterValues(paramName);
                if (values == null || values.length == 0) {
                    // Do nothing, no values found at all.
                } else if (values.length > 1) {
                    params.put(unprefixed, values);
                } else {
                    params.put(unprefixed, values[0]);
                }
            }
        }
        return params;
    }

    /**
     * 是否ajax
     * <p>
     * author: shen.wei<br>
     * version: 2012-5-9 下午4:03:41 <br>
     *
     * @param request
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String xRequestedWith = request.getHeader("X-Requested-With");
        return StringUtils.equalsIgnoreCase("XMLHttpRequest", xRequestedWith);
    }

    /**
     * 是否表单提交
     * <p>
     * author: shen.wei<br>
     * version: 2012-6-5 上午9:36:58 <br>
     *
     * @param request
     */
    public static boolean isFormPost(HttpServletRequest request) {
        String contentType = request.getHeader("Content-Type");
        // return StringUtils.equalsIgnoreCase(contentType,
        // "application/x-www-form-urlencoded");
        return StringUtils.containsIgnoreCase(contentType, "application/x-www-form-urlencoded");
    }

    /**
     * 直接输出纯Json.
     */
    public static void printJson(String text, HttpServletResponse response, String webencoding) {
        render(text, "application/json;charset=" + webencoding, response);
    }

    /**
     * 直接输出纯XML.
     */
    public static void printXML(String text, HttpServletResponse response, String webencoding) {
        render(text, "application/xml;charset=" + webencoding, response);
    }

    /**
     * 直接输出纯HTML.
     */
    public static void printHtml(String text, HttpServletResponse response, String webencoding) {
        render(text, "text/html;charset=" + webencoding, response);
    }

    /**
     * 直接输出纯字符串.
     */
    public static void printText(String text, HttpServletResponse response, String webencoding) {
        render(text, "text/plain;charset=" + webencoding, response);
    }

    /**
     * 直接输出.
     *
     * @param contentType 内容的类型 <br>
     *                    输出json: text/x-json;charset= <br>
     *                    输出纯字符串: text/plain;charset= <br>
     *                    输出html: text/html;charset= <br>
     *                    输出xml: text/xml;charset=
     */
    public static void render(String text, String contentType, HttpServletResponse response) {
        PrintWriter out = null;
        response.setContentType(contentType);

        try {
            out = response.getWriter();
            out.write(text);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            if (out != null) {
                out.print("");
                out.close();
            }
        }
    }

    //    public static Map<String, String> getRequestURLDecodeParameters(HttpServletRequest request, String encoding) {
//        Map<String, String> paraMap = new HashMap<String, String>();
//        Map requestParams = request.getParameterMap();
//        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
//            String name = (String) iter.next();
//            String[] values = (String[]) requestParams.get(name);
//
//            if (null != values && values.length > 0) {
//                StringBuilder valueSb = new StringBuilder();
//                for (int i = 0; i < values.length; i++) {
//                    valueSb.append(values[i]).append(",");
//                }
//                valueSb.deleteCharAt(valueSb.length() - 1);
//                if (valueSb.length() > 0 && StringUtils.isNotBlank(name)) {
//                    String valueStr = valueSb.toString();
//                    valueStr = UrlEncodeUtil.encode(valueStr, encoding);
//                    paraMap.put(name, valueStr);
//                }
//            }
//        }
//        return paraMap;
//    }

//    /**
//     * 获取http get方式经编码后的请求串
//     *
//
//     */
    // public static String getEncodedQueryString(Map<String, String[]>
    // queryMap, String encoding) {
    // StringBuilder sb = new StringBuilder();
    // if (null != queryMap && 0 != queryMap.size() &&
    // StringUtils.isNotBlank(encoding)) {
    // for (String key : queryMap.keySet()) {
    // String[] values = queryMap.get(key);
    // if (values != null && values.length > 0) {
    // String val = values[0];
    // if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(val)) {
    // sb.append(sb.length() > 0 ? "&" : "");
    // key = Encodes.urlEncode(key, encoding);
    // val = Encodes.urlEncode(val, encoding);
    // sb.append(key + "=" + val);
    // }
    // }
    // }
    // }
    // return sb.toString();
    // }
//    public static String getEncodedQueryString(Map<String, String> queryMap, String encoding) {
//        StringBuilder sb = new StringBuilder();
//        if (null != queryMap && 0 != queryMap.size() && StringUtils.isNotBlank(encoding)) {
//            for (String key : queryMap.keySet()) {
//                String val = queryMap.get(key);
//                if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(val)) {
//                    sb.append(sb.length() > 0 ? "&" : "");
//                    key = UrlEncodeUtil.encode(key, encoding);
//                    val = UrlEncodeUtil.encode(val, encoding);
//                    sb.append(key + "=" + val);
//                }
//            }
//        }
//        return sb.toString();
//    }
//
//    public static String getEncodedQueryString(Serializable arg0, final String encoding) {
//        final StringBuilder sb = new StringBuilder();
//        BeanUtils.eachProperties(arg0, new BeanUtils.EachPropertiesHandler() {
//            @Override
//            public void handler(String property, Object value) {
//                sb.append(sb.length() > 0 ? "&" : "");
//                String val = null;
//                if (value != null) {
//                    val = UrlEncodeUtil.encode(value.toString(), encoding);
//                }
//                if (StringUtils.isNotBlank(val)) {
//                    sb.append(property + "=" + val);
//                }
//            }
//        });
//
//        return sb.toString();
//    }

    // public static String getEncodedUrl(String queryUrl, Map<String, String[]>
    // queryMap, String encoding) {
    // String queryString = getEncodedQueryString(queryMap, encoding);
    // return merginUrl(queryUrl, queryString);
    // }


//    public static String getEncodedUrl(String queryUrl, Serializable arg0, String encoding) {
//        String queryString = getEncodedQueryString(arg0, encoding);
//        return merginUrl(queryUrl, queryString);
//    }

//    private static String merginUrl(String queryUrl, String queryString) {
//        int queryIndex = queryUrl.indexOf("?");
//        if (-1 != queryIndex) {
//            if (StringUtils.isBlank(queryString)) {
//                queryUrl = queryUrl.substring(0, queryIndex);
//            } else {
//                queryUrl = queryUrl + queryString;
//            }
//        } else {
//            if (StringUtils.isNotBlank(queryString)) {
//                queryUrl = queryUrl + "?" + queryString;
//            }
//        }
//        return queryUrl;
//    }
}
