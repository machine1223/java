package com.pw.common;

import com.pw.common.json.JackSonUtils;
import com.pw.common.logging.LogWrapper;
import org.slf4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Properties;

/**
 * I18NMessageUtil.<br/>
 *
 * @author carl
 * @since  Jun 18, 2012 
 */

public class I18NMessageUtil {

    private static Logger logger = LogWrapper.getLogger(I18NMessageUtil.class).getWrappedLogger();

    private static MessageSource messageSource;



    public static MessageSource getMessageSource() {

        return messageSource;
    }
    public static void setMessageSource(MessageSource messageSource) {
        I18NMessageUtil.messageSource = messageSource;
    }

    public static Locale getLocale(HttpServletRequest request){
        return RequestContextUtils.getLocaleResolver(request).resolveLocale(request);

    }
    public static String getMessage(Locale locale, String code, String defualtValue, Object[] args){
        try {
            return messageSource.getMessage(code, args, locale);
        } catch (NoSuchMessageException e) {
            if(logger.isDebugEnabled()){
                logger.error("I18N message is not found for code: "+ code);
            }
            return defualtValue;
        }
    }
    public static String getMessage(String code, String defaultValue, Object[] args){
        return getMessage(getLocale(), code,defaultValue, args);
    }
    public static String getMessage(HttpServletRequest request, String code, String defaultValue, Object[] args){
        return getMessage(getLocale(request), code,defaultValue, args);
    }
    public static String getMessage(Locale locale, String code, String defualtValue){
        return getMessage(locale, code, defualtValue, null);
    }
    public static String getMessage(HttpServletRequest request, String code, String defaultValue){
        return getMessage(getLocale(request), code,defaultValue);
    }
    public static String getMessage(String code, String defaultValue){
        return getMessage(getLocale(), code,defaultValue);
    }
    public static Locale getLocale(){
        return getLocale(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
    }

    /**
     * create js file
     */
    public static String toJavascriptI18N(Properties properties, Locale locale){
        /**
         * format js
         */
        StringBuilder sb = new StringBuilder("var i18n=(function(){");
//        properties.put("_current_language", locale);
        sb.append("var i=").append(JackSonUtils.toJson(properties)).append(";");
        sb.append("i.getCurrentLanguage=function(){ return \"").append(locale).append("\";};");
        sb.append("return i;})();");
        return sb.toString();
    }
}
