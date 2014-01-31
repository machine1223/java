package com.pw.spring.extension;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;
import com.pw.common.json.JackSonUtils;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

public class MessageResourceExtesion extends ReloadableResourceBundleMessageSource implements ServletContextAware, InitializingBean {
    private ServletContext servletContext;
    private static List<Locale> supporedLocales = new ArrayList<Locale>();
    private String[] basefilenames = new String[0];

    private static final String defaultJsMessagePath = "/javascript/message/i18n_message";
    public static String jsFilePath = "resources/javascripts/everbridge/i18n/i18n_message";
    private static String jsMessagePath;

    public void setBasenames(String[] basenames) {
        if (basenames != null) {
            this.basefilenames = new String[basenames.length];
            for (int i = 0; i < basenames.length; i++) {
                String basename = basenames[i];
                Assert.hasText(basename, "Basename must not be empty");
                this.basefilenames[i] = basename.trim();
            }
        }
        else {
            this.basefilenames = new String[0];
        }
        super.setBasenames(basenames);
    }

    public static List<Locale> getSupporedLocales() {

        return supporedLocales;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public static String getJsFilePath() {
        return MessageResourceExtesion.jsFilePath;
    }

    public static void setJsFilePath(String jsFilePath) {
        MessageResourceExtesion.jsFilePath = jsFilePath;
    }

    public Properties getAllProperties(String filename){
        return super.getProperties(filename).getProperties();

    }
    public Properties getAllProperties(Locale locale){
        return super.getMergedProperties(locale).getProperties();

    }
    public String getMessage(String code, Locale locale){
        return super.resolveCodeWithoutArguments(code,locale);
    }

    public File getI18NJsFile(Locale locale){
        List<String> filenames = super.calculateFilenamesForLocale(jsFilePath, locale);
        for (String filename:filenames) {
            if(StringUtils.isNotBlank(filename)){
                String realPath = servletContext.getRealPath(filename+".js");
                File file = new File(realPath);
                return file;
            }
        }
        return null;

    }

    @Override
    public void afterPropertiesSet() throws Exception {
    	for(Locale locale:Locale.getAvailableLocales()){
    		for (int i = this.basefilenames.length - 1; i >= 0; i--) {
    			List filenames = calculateAllFilenames(this.basefilenames[i], locale);
    			for (int j = filenames.size() - 1; j >= 0; j--) {
    				String filename = (String) filenames.get(j);
    				if(filename.equals(this.basefilenames[i])){
    					continue;
    				}
    				PropertiesHolder propHolder = getProperties(filename);
    				if (propHolder.getProperties() != null) {
    					this.supporedLocales.add(locale);
    				}
    			}
    		}
    	}
        for(Locale supportedLocale:this.supporedLocales){
            File file = getI18NJsFile(supportedLocale);
            Properties properties = this.getAllProperties(supportedLocale);
            this.createJavascriptI18N(properties, file, supportedLocale);
        }

    }

    /**
     * create js file
     * @param file i18n javascript file
     */
    public static String createJavascriptI18N(Properties properties, File file, Locale locale){
        try {
            if(!file.exists()){
                file.createNewFile();
                /**
                 * create js
                 */
                StringBuilder sb = new StringBuilder("var i18n=(function(){\n");
                sb.append("var i={};\n");
                sb.append("var i=").append(JackSonUtils.toJson(properties)).append(";");
                sb.append("i.getCurrentLanguage=function(){ return \"").append(locale).append("\";};");
                sb.append("return i;})();");
                String jsStr = sb.toString();
                return jsStr;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
