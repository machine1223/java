package com.pw.spring.handler;

import com.pw.common.CookieUtil;
import com.pw.common.I18NMessageUtil;
import com.pw.common.json.JackSonUtils;
import com.pw.common.json.JsonResult;
import com.pw.exception.BaseException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PWHandlerExceptionResolver extends SimpleMappingExceptionResolver implements HandlerExceptionResolver {
	@Override
    public ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        try {
            ex.printStackTrace();
            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                response.setStatus(this.determineStatusCode(request,this.determineViewName(ex,request)));
                ModelAndView mav = new ModelAndView();
                if(ex instanceof BaseException){
                    BaseException e = (BaseException)ex;
                    String code = "error.business."+e.getCode();
                    String errorInfo = I18NMessageUtil.getMessage(request, code, "Business error code: " + e.getCode(), e.getMessageArgs());
                    JsonResult result = new JsonResult(false,errorInfo);
                    result.setResultCode("10002");
                    response.getWriter().write(JackSonUtils.toJson(result));
                }else{
                    String errorInfo = I18NMessageUtil.getMessage(request,"error.500.title",ex.getMessage());
                    JsonResult result = new JsonResult(false,errorInfo);
                    result.setResultCode("500");
                    response.getWriter().write(JackSonUtils.toJson(result));
                }
                return mav;
            }

            return super.doResolveException(request,response,handler,ex);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return null;
    }

    @Override
    protected String determineViewName(Exception ex, HttpServletRequest request) {
        String viewName = super.determineViewName(ex, request);
        Cookie siteType = CookieUtil.getCookie(request, "eb.user.site.type");
        if(siteType!=null && "MOBILE_WEB".equals(siteType.getValue())){
            return "m_"+viewName;
        }else{
            return viewName;
        }
    }

}
