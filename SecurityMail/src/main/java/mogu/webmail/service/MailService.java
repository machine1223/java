package mogu.webmail.service;

import mogu.webmail.service.Mail;
import mogu.webmail.service.User;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

public interface MailService
{
	/**
	 * 列出用户收件箱的邮件
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Mail[] listMails(User user) throws Exception;
	
	/**
	 * 发送一封邮件
	 * 
	 * @param message
	 * @throws Exception
	 */
	public void sendMail(User user, Mail mail) throws Exception;
	
	/**
	 * 解读一封邮件
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public Mail viewMail(User user, Message message) throws Exception;
	
	/**解读一封邮件
	 * @param msgNum
	 * @return
	 * @throws Exception
	 */
	public Mail viewMail(User user, int msgNum) throws Exception;
	
	/**
	 * 加密邮件，并返回加密后的Message
	 * 
	 * @param userCertificate
	 *            用户证书
	 * @param mail
	 *            邮件对象
	 * @return
	 * @throws Exception
	 */
	public Message createEncryptMail(X509Certificate userCertificate, Session session, MimeMessage message) throws Exception;
	
	/**
	 * 创建签名邮件
	 * 
	 * @param userPrivatekey
	 *            用户私钥
	 * @param userCertificate
	 *            用户证书
	 * @param mail
	 *            邮件对象
	 * @return
	 * @throws Exception
	 */
	public Message createSignedMail(PrivateKey userPrivatekey, X509Certificate userCertificate, Session session, MimeMessage message) throws Exception;
	
}
