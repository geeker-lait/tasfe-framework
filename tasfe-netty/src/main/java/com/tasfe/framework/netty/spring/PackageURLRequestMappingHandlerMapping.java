package com.tasfe.framework.netty.spring;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * RequestMapping处理器, 将Action类的包名转换成url的版本号.
 * 例：
 * packageBase: com.tasfe.democenter
 * replacement: v:|_:.|, 即v替换成空字符, 下划线替换成圆点
 * url root: /democenter/
 * com.tasfe.democenter.api.controller.OrderController 类的RequestMapping值为:order, 某成员方法的RequestMapping值为: /getOrder, 则 转换后的完整url为: /order/getOrder
 * com.tasfe.democenter.api.controller.v2_1_7.OrderController 类的RequestMapping值为order, 某成员方法的RequestMapping值为: /getOrder, 则 转换后的完整url为: /2.1.7/order/getOrder
 * 这个时候的docBase参考tomcat的server.xml的docBase指定为：com.tasfe.democenter.api.controller
 * 这个时候的contextPath参考tomcat的server.xml的指定为path：/demo(项目名)
 */
public class PackageURLRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
    /**
     * 扫描的controller包路径
     */
    private String docBase;

    /**
     * 路径项目一般是项目路径
     */
    private String contextPath;
    /*
	 * format: $old1:$new1|$old2:$new2
	 */

    private String replacement = "v:|_:.|";

    /**
     * 包名前缀, 即包名截取该值后开始转换成url的path
     *
     * @param docBase
     */
    public void setDocBase(String docBase) {
        this.docBase = docBase;
        this.docBase = (null == docBase) ? null : docBase.trim();
    }

    public void setReplacement(String replacement) {
        this.replacement = replacement;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    @Override
    protected HandlerMethod createHandlerMethod(Object handler, Method method) {
        return super.createHandlerMethod(handler, method);
    }

    protected RequestMappingInfo createRequestMappingInfo(String pattern) {
        String[] patterns = (null == pattern) ? null
                : this.resolveEmbeddedValuesInPatterns(new String[]{pattern});
        return new RequestMappingInfo(new PatternsRequestCondition(patterns,
                this.getUrlPathHelper(), this.getPathMatcher(),
                this.useSuffixPatternMatch(), this.useTrailingSlashMatch(),
                this.getFileExtensions()), null, null, null, null, null,
                null);
    }

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        RequestMappingInfo info = super.getMappingForMethod(method, handlerType);
        if (null != info) {
            if (contextPath == null) {
                contextPath = "/";
            }
            StringBuilder prefix = new StringBuilder(contextPath);
            Package pack = handlerType.getPackage();
            String fullName = pack.getName();
            String pbase = this.docBase;

            if (StringUtils.containsIgnoreCase(handlerType.toString(), "login")) {
                String[] names = fullName.split("\\.");
                String name = names[names.length - 1];
                name = this.replace(name);
                prefix.append('/');
                prefix.append(name);
            } else if (null != pbase && !pbase.isEmpty() && 0 == fullName.indexOf(pbase)) {
                pbase = pbase.endsWith(".") ? pbase : (pbase += '.');
                fullName = fullName.substring(this.docBase.length());
                String[] names = fullName.split("\\.");
                for (String name : names) {
                    name = this.replace(name);
                    prefix.append('/');
                    prefix.append(name);
                }
            }
            String prefixStr = prefix.toString().replace("//", "/");
            if (prefix.length() > 1) {
                info = createRequestMappingInfo(prefixStr).combine(info);
            }
        }
        return info;
    }

    private String replace(String str) {
        if (!str.isEmpty() && !replacement.isEmpty()) {
            String[] segs = replacement.split("\\|");
            for (String seg : segs) {
                String[] pairs = seg.split(":");
                String oldVal = pairs[0];
                String newVal = "";
                if (pairs.length > 1) {
                    newVal = pairs[1];
                }
                str = str.replace(oldVal, newVal);
            }
        }
        return str;
    }
}
