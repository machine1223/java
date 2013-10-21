package mogu.webmail.service;

import mogu.webmail.service.User;

import java.security.cert.X509Certificate;

public interface UserService
{
	/**
	 * 判断用户在此webmail中是否已经注册，本质是看看用户证书是否已经存在。
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	boolean isRegistered(User user) throws Exception;
	
	/**
	 * 在本webmail中注册账户，所谓注册就是生成用户的证书。
	 * 
	 * @param user
	 * @throws Exception
	 */
	void register(User user) throws Exception;
	
	/**
	 * 验证用户账户是否存在
	 * 
	 * @param user
	 * @throws Exception
	 */
	void testConnect(User user) throws Exception;
	
	/**
	 * 创建用户证书
	 * 
	 * @param user
	 * @throws Exception
	 */
	void createUserCertificate(User user) throws Exception;
	
	/**
	 * 生成用户的PFX文件
	 * 
	 * @param user
	 * @throws Exception
	 */
	void createUserPFX(User user) throws Exception;
	
	/**
	 * 得到用户的证书
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	X509Certificate getUserCertificate(User user) throws Exception;
}