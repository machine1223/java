package mogu.webmail.service;

import mogu.webmail.utils.*;

import javax.mail.Store;
import java.io.File;
import java.security.KeyPair;
import java.security.cert.X509Certificate;

public class UserServiceImpl implements UserService
{
    @Override
	public boolean isRegistered(User user) throws Exception
	{
		return (new File(Constants.generateUserCertificatePath(user.getUserEmail())).exists());
	}
	
	@Override
	public void register(User user) throws Exception
	{
		if(isRegistered(user))
			throw new Exception("该用户已经注册！");
		// 1)验证用户是否存在于互联网上。
		testConnect(user);
		// 2)创建用户证书。
		createUserCertificate(user);
	}
	
	@Override
	public void testConnect(User user) throws Exception
	{
		try
		{
            Store store = MailUtils.buildStore(user);
            store.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new Exception("用户Email或者密码错误，连接邮件服务器失败！");
		}
	}
	
	@Override
	public void createUserCertificate(User user) throws Exception
	{
		// 1)生成一个密钥对
		KeyPair keyPair = KeyUtils.generateKeyPair();
		// 2)生成用户的证书
		MoguCertificate certificateInfo = new MoguCertificate();
		certificateInfo.setName(user.getUserEmail());
		certificateInfo.setCompany(user.getCompany());
		certificateInfo.setOrganization(user.getOrganization());
		certificateInfo.setCity(user.getCity());
		certificateInfo.setProvince(user.getProvence());
		certificateInfo.setCountry(user.getCountry());
		X509Certificate userCertificate = CertificateUtils.generateX509Certificate(Constants.CA_CERTIFICATE_PATH, certificateInfo, keyPair.getPublic());
		// 3)往密钥库文件中存储用户的私钥
		KeyUtils.saveUserKeyToKeystore(user, keyPair.getPrivate(), userCertificate);
		// 4)生成浏览器使用的PFX文件
		CertificateUtils.generatePFX(user, userCertificate, keyPair.getPrivate());
	}
	
	@Override
	public void createUserPFX(User user) throws Exception
	{
		
	}
	
	@Override
	public X509Certificate getUserCertificate(User user) throws Exception
	{
		return CertificateUtils.getCertificate(Constants.generateUserCertificatePath(user.getUserEmail()));
	}
}
