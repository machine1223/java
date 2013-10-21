package mogu.webmail.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthenticateFilter implements Filter
{
	
	@Override
	public void destroy()
	{
		
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession();
		if(req.getRequestURI().indexOf("toLogin.action") != -1 
				|| req.getRequestURI().indexOf("login.action") != -1
				|| req.getRequestURI().indexOf("toRegister.action") != -1
				|| req.getRequestURI().indexOf("register.action") != -1
				|| req.getRequestURI().indexOf("logout.action") != -1)
		{
			chain.doFilter(request, response);
		}
		else
		{
			if(session == null || session.getAttribute("user") == null)
			{
				res.sendRedirect(req.getContextPath() + "/toLogin.action");
				return;
			}
			chain.doFilter(request, response);
		}
	}
	
	@Override
	public void init(FilterConfig config) throws ServletException
	{
	}
}
