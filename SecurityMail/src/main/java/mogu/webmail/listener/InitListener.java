package mogu.webmail.listener;

import mogu.webmail.utils.CertificateUtils;
import mogu.webmail.utils.Constants;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.security.Security;


public class InitListener implements ServletContextListener
{
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0)
	{
	}
	@Override
	public void contextInitialized(ServletContextEvent arg0)
	{
		try
		{
			Security.addProvider(new BouncyCastleProvider());
			Constants.BASE_PATH=arg0.getServletContext().getRealPath("/");
			//初始化根证书，如果不存在则先创建一个。
			if(!new File(Constants.CA_CERTIFICATE_PATH).exists())
			{
				Constants.generateBasePath();
				CertificateUtils.generateCACertificate();
			}
			System.out.println(Constants.TRUSTSTORE_PATH);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
