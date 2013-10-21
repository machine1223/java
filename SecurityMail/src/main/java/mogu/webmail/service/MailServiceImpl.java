package mogu.webmail.service;

import mogu.webmail.service.Mail;
import mogu.webmail.service.MailService;
import mogu.webmail.service.MoguAuth;
import mogu.webmail.service.User;
import mogu.webmail.utils.CertificateUtils;
import mogu.webmail.utils.Constants;
import mogu.webmail.utils.KeyUtils;
import mogu.webmail.utils.MailUtils;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.cms.AttributeTable;
import org.bouncycastle.asn1.cms.IssuerAndSerialNumber;
import org.bouncycastle.asn1.smime.SMIMECapabilitiesAttribute;
import org.bouncycastle.asn1.smime.SMIMECapability;
import org.bouncycastle.asn1.smime.SMIMECapabilityVector;
import org.bouncycastle.asn1.smime.SMIMEEncryptionKeyPreferenceAttribute;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.cms.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.mail.smime.*;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.internet.MimeMessage.RecipientType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertStore;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509Certificate;
import java.util.*;

public class MailServiceImpl implements MailService
{
	static
	{
		Security.addProvider(new BouncyCastleProvider());
		MailcapCommandMap mailcap = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
		mailcap.addMailcap("application/pkcs7-signature;; x-java-content-handler=org.bouncycastle.mail.smime.handlers.pkcs7_signature");
		mailcap.addMailcap("application/pkcs7-mime;; x-java-content-handler=org.bouncycastle.mail.smime.handlers.pkcs7_mime");
		mailcap.addMailcap("application/x-pkcs7-signature;; x-java-content-handler=org.bouncycastle.mail.smime.handlers.x_pkcs7_signature");
		mailcap.addMailcap("application/x-pkcs7-mime;; x-java-content-handler=org.bouncycastle.mail.smime.handlers.x_pkcs7_mime");
		mailcap.addMailcap("multipart/signed;; x-java-content-handler=org.bouncycastle.mail.smime.handlers.multipart_signed");
		CommandMap.setDefaultCommandMap(mailcap);
	}
	
	@Override
	public Message createEncryptMail(X509Certificate receiptCertificate, Session session, MimeMessage message) throws Exception
	{
		SMIMEEnvelopedGenerator gen = new SMIMEEnvelopedGenerator();
		gen.addKeyTransRecipient((X509Certificate) receiptCertificate);
		//   
		MimeBodyPart mp = gen.generate(message, SMIMEEnvelopedGenerator.DES_EDE3_CBC, "BC");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		mp.writeTo(out);
		
		MimeMessage encryptedMessage = new MimeMessage(session, new ByteArrayInputStream(out.toByteArray()));
		
		// 将原始邮件头信息存入新对象中
		Enumeration headers = message.getAllHeaderLines();
		while (headers.hasMoreElements())
		{
			String headline = (String) headers.nextElement();
			// 保留原始邮件的content-
			if(!headline.toLowerCase().startsWith("content-"))
				encryptedMessage.addHeaderLine(headline);
		}
		
		return encryptedMessage;
	}
	
	@Override
	public Message createSignedMail(PrivateKey userPrivatekey, X509Certificate userCertificate, Session session, MimeMessage message) throws Exception
	{
		SMIMECapabilityVector capabilities = new SMIMECapabilityVector();
		capabilities.addCapability(SMIMECapability.dES_EDE3_CBC);
		capabilities.addCapability(SMIMECapability.rC2_CBC, 128);
		capabilities.addCapability(SMIMECapability.dES_CBC);
		ASN1EncodableVector attributes = new ASN1EncodableVector();
		attributes.add(new SMIMECapabilitiesAttribute(capabilities));
		
		attributes.add(new SMIMEEncryptionKeyPreferenceAttribute(new IssuerAndSerialNumber(new X509Name((userCertificate).getIssuerDN().getName()), userCertificate.getSerialNumber())));
		// 创建一个签名容器
		SMIMESignedGenerator signer = new SMIMESignedGenerator();
        try{
		// 添加签名需要的元素（私钥和算法）
		signer.addSigner(userPrivatekey, userCertificate, "DSA".equals(userPrivatekey.getAlgorithm()) ? SMIMESignedGenerator.DIGEST_SHA1 : SMIMESignedGenerator.DIGEST_MD5, new AttributeTable(attributes), null);
        }catch (Exception e){
            e.printStackTrace();
        }
        ArrayList<X509Certificate> certList = new ArrayList<X509Certificate>();
		certList.add(userCertificate);
		CertStore certs = CertStore.getInstance("Collection", new CollectionCertStoreParameters(certList), "BC");
		signer.addCertificatesAndCRLs(certs);
		// 对信息进行签名
		MimeMultipart mm = signer.generate(message, "BC");
		MimeMessage signedMessage = new MimeMessage(session);
		// 将原始邮件头，添加到新邮件中
		Enumeration headers = message.getAllHeaderLines();
		while (headers.hasMoreElements())
		{
			signedMessage.addHeaderLine((String) headers.nextElement());
		}
		// 在新邮件中设置签名过的邮件
		signedMessage.setContent(mm);
		signedMessage.saveChanges();
		
		return signedMessage;
	}
	
	@Override
	public Mail[] listMails(User user) throws Exception
	{
		Store store = MailUtils.buildStore(user);
		Folder inbox = store.getDefaultFolder().getFolder("INBOX");
		inbox.open(Folder.READ_WRITE);
		
		Message[] msgs = inbox.getMessages();
		FetchProfile profile = new FetchProfile();
		profile.add(FetchProfile.Item.ENVELOPE);
		inbox.fetch(msgs, profile);
		Mail[] mails = new Mail[] {};
		if(msgs.length > 0)
		{
			int length = msgs.length;
			if(length > 10)
				length = 10;
			mails = new Mail[length];
			for (int i = msgs.length - 1; i >= msgs.length - 10; i--)
			{
				Message message = msgs[i];
				Mail mail = new Mail();
				mail.setMsgNum(message.getMessageNumber());
				// 判断邮件类型为普通文本邮件
				if(message.isMimeType("text/*"))
				{
					mail.setTypt(Mail.TYPE_NORMAL);
					handle(message, mail);
				}
				else
				{
					// 签名邮件
					if(message.isMimeType("multipart/signed"))
					{
						mail.setTypt(Mail.TYPE_SIGNED);
						handle(message, mail);
					}
					// 加密邮件
					else if(message.isMimeType("application/pkcs7-mime"))
					{
						mail.setTypt(Mail.TYPE_ENCRYPT);
						handle(message, mail);
					}
					else
					{
						mail.setTypt(Mail.TYPE_NORMAL);
						handle(message, mail);
					}
				}
				mails[msgs.length - (i + 1)] = mail;
			}
		}
		inbox.close(true);
		store.close();
		return (mails);
	}
	
	@Override
	public void sendMail(User user, Mail mail) throws Exception
	{
		try{
            Properties props = MailUtils.buildProperty(user);

            MoguAuth sa = new MoguAuth();
            sa.setUserinfo(user.getUserEmail(), user.getUserPassword());

            Session session = Session.getInstance(props, sa);
            session.setDebug(true);

            MimeMessage body = new MimeMessage(session);
            body.setFrom(new InternetAddress(user.getUserEmail()));
            body.setRecipient(Message.RecipientType.TO, new InternetAddress(mail.getReceipt()));
            body.setSubject(mail.getSubject());
            body.setText(mail.getContent());
            body.saveChanges();
            if(mail.getSigned() == 0 && mail.getEncrypted() == 0)
            {
                Transport.send(body);
            }
            else if(mail.getSigned() == 0 && mail.getEncrypted() != 0)
            {
                String certPath = Constants.generateUserCertificatePath(mail.getReceipt());
                if(!new File(certPath).exists())
                    throw new Exception("收件人证书不存在，不能对邮件进行加密！");
                X509Certificate receiptCertificate = CertificateUtils.getCertificate(certPath);
                Message message = createEncryptMail(receiptCertificate, session, body);
                Transport.send(message);
            }
            else if(mail.getSigned() != 0 && mail.getEncrypted() == 0)
            {
                PrivateKey userPrivateKey = KeyUtils.getPrivateKeyFromKeystore(user.getUserEmail(), user.getUserPassword());
                X509Certificate userCertificate = CertificateUtils.getCertificate(Constants.generateUserCertificatePath(user.getUserEmail()));
                Message message = createSignedMail(userPrivateKey, userCertificate, session, body);
                Transport.send(message);
            }
            else
            {
                String certPath = Constants.generateUserCertificatePath(mail.getReceipt());
                if(!new File(certPath).exists())
                    throw new Exception("收件人证书不存在，不能对邮件进行加密！");
                PrivateKey userPrivateKey = KeyUtils.getPrivateKeyFromKeystore(user.getUserEmail(), user.getUserPassword());
                X509Certificate userCertificate = CertificateUtils.getCertificate(Constants.generateUserCertificatePath(user.getUserEmail()));
                X509Certificate receiptCertificate = CertificateUtils.getCertificate(Constants.generateUserCertificatePath(mail.getReceipt()));
                Message message = createSignedMail(userPrivateKey, userCertificate, session, body);
                message = createEncryptMail(receiptCertificate, session, (MimeMessage) message);
                Transport.send(message);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
	}
	
	@Override
	public Mail viewMail(User user, Message message) throws Exception
	{
		Mail mail = new Mail();
		// 判断邮件类型为普通文本邮件
		if(message.isMimeType("text/*"))
		{
			handle(message, mail);
			Object cont = message.getContent();
			if(cont instanceof String)
			{
				mail.setContent((String) cont);
			}
			else if(cont instanceof Multipart)
			{
				Multipart mp = (Multipart) cont;
				mail.setContent(handleMultipart(mp));
			}
		}
		else
		{
			// 签名邮件
			if(message.isMimeType("multipart/signed"))
			{
				handle(message, mail);
				message = (MimeMessage) message;
				SMIMESigned s = new SMIMESigned((MimeMultipart) message.getContent());
				mail.setVerifyResult(verify(s));
				MimeBodyPart content = s.getContent();
				Object cont = content.getContent();
				if(cont instanceof String)
				{
					mail.setContent((String) cont);
				}
				else if(cont instanceof Multipart)
				{
					Multipart mp = (Multipart) cont;
					mail.setContent(handleMultipart(mp));
				}
			}
			else if(message.isMimeType("application/pkcs7-mime"))// 加密邮件
			{
				handle(message, mail);
				MimeBodyPart res = null;
				res = decryptMail((MimeMessage) message, user);
				if(res.isMimeType("multipart/signed")) // 邮件解密后res为签名邮件
				{
					SMIMESigned s = new SMIMESigned((MimeMultipart) res.getContent());
					MimeBodyPart content = s.getContent();
					Object cont = content.getContent();
					if(cont instanceof String)
					{
						mail.setContent((String) cont);
					}
					else if(cont instanceof Multipart)
					{
						Multipart mp = (Multipart) cont;
						mail.setContent(handleMultipart(mp));
					}
					mail.setVerifyResult(verify(s));
				}
				else
				// 邮件解密后的res为非签名邮件
				{
					handle(message, mail);
					if(res.isMimeType("text/*")) // 邮件解密后的res为文本邮件
					{
						Object cont = res.getContent();
						if(cont instanceof String)
						{
							mail.setContent((String) cont);
						}
						else if(cont instanceof Multipart)
						{
							Multipart mp = (Multipart) cont;
							mail.setContent(handleMultipart(mp));
						}
					}
					else
					{
						mail.setContent(handleMultipart((Multipart) res.getContent()));
					}
				}
			}
			else
			{
				handle(message, mail);
				Object cont = message.getContent();
				if(cont instanceof String)
				{
					mail.setContent((String) cont);
				}
				else if(cont instanceof Multipart)
				{
					Multipart mp = (Multipart) cont;
					mail.setContent(handleMultipart(mp));
				}
			}
		}
		return mail;
	}
	
	@Override
	public Mail viewMail(User user, int msgNum) throws Exception
	{
        Store store = MailUtils.buildStore(user);
		Folder inbox = store.getDefaultFolder().getFolder("INBOX");
		inbox.open(Folder.READ_WRITE);
		Message message = inbox.getMessage(msgNum);
		return viewMail(user, message);
	}
	
	/**
	 * 解析邮件统一的信息
	 * 
	 * @param msg
	 * @param mail
	 * @throws Exception
	 */
	private void handle(Message msg, Mail mail) throws Exception
	{
		mail.setSubject(MimeUtility.decodeText(msg.getSubject()).trim());
		mail.setFrom(MimeUtility.decodeText(msg.getFrom()[0].toString()));
		mail.setReceipt(MimeUtility.decodeText(msg.getRecipients(RecipientType.TO)[0].toString()));
		mail.setDate(msg.getSentDate());
	}
	
	// 处理普通的Multipart邮件，包括了保存附件的功能
	public String handleMultipart(Multipart msg) throws Exception
	{
		String content = "";
		String disposition;
		BodyPart part;
		Multipart mp = (Multipart) msg;
		int mpCount = mp.getCount();// Miltipart的数量,用于除了多个part,比如多个附件
		for (int m = 0; m < mpCount; m++)
		{
			part = mp.getBodyPart(m);
			disposition = part.getDisposition();
			if((disposition != null) && ((disposition.equalsIgnoreCase(part.ATTACHMENT)) || (disposition.equalsIgnoreCase(part.INLINE))))//
			{
				content += "有附件<br/>";
			}
			else
			{
				Object cont = part.getContent();
				if(cont instanceof String)
				{
					content += (String) (part.getContent()) + "<br/>";
				}
				if(cont instanceof Multipart)
				{
					content += handleMultipart((Multipart) part.getContent());
				}
			}
			
		}
		return "<div style='border:1px solid #D6B99C;padding:5px'>" + content + "</div>";
	}
	
	/**
	 * 验证签名是否合法
	 * 
	 * @param s
	 * @return
	 * @throws Exception
	 */
	public String verify(SMIMESigned s) throws Exception
	{
		String checkSignedResult = new String("签名的验证结果：<br/>");
		boolean checkCertLian = true;
		CertStore certs = s.getCertificatesAndCRLs("Collection", "BC");
		SignerInformationStore signers = s.getSignerInfos();
		Collection c = signers.getSigners();
		Iterator it = c.iterator();
		while (it.hasNext())
		{
			SignerInformation signer = (SignerInformation) it.next();
			Collection certCollection = certs.getCertificates(signer.getSID());
			Iterator certIt = certCollection.iterator();
			X509Certificate cert = (X509Certificate) certIt.next();
			if(signer.verify(cert, "BC"))
			{
				checkSignedResult = checkSignedResult + new String("邮件内容合法！<br/>");
			}
			else
			{
				checkSignedResult = checkSignedResult + new String("邮件内容不合法！<br/>");
			}
			// 验证证书是否过期
			try
			{
				cert.checkValidity(new Date());
				checkSignedResult = checkSignedResult + "签名的证书未过期！<br/>";
			}
			catch (Exception ex)
			{
				checkSignedResult = checkSignedResult + "签名的证书已过期！<br/>";
			}
		}
		return "<div style='border:1px solid #D6B99C;padding:5px'>" + checkSignedResult + "</div>";
	}
	
	public MimeBodyPart decryptMail(MimeMessage encryptedMsg, User user) throws Exception
	{
		if(!encryptedMsg.isMimeType("application/pkcs7-mime"))
			throw new Exception("此邮件不是加密类型的邮件!");
		X509Certificate cert = CertificateUtils.getCertificate(Constants.generateUserCertificatePath(user.getUserEmail()));
		BigInteger bi = cert.getSerialNumber();
		MimeBodyPart mimeBodyPart = null; // 解密后的邮件正文
		
		SMIMEEnveloped m = new SMIMEEnveloped((MimeMessage) encryptedMsg);
		RecipientInformationStore recipients = m.getRecipientInfos();
		Collection c = recipients.getRecipients();
		Iterator it = c.iterator();
		while (it.hasNext())
		{
			RecipientInformation recipient = (RecipientInformation) it.next();
			RecipientId recId = recipient.getRID();
			if(bi.equals(recId.getSerialNumber()))
			{
				PrivateKey pk = KeyUtils.getPrivateKeyFromKeystore(user.getUserEmail(), user.getUserPassword());
				mimeBodyPart = SMIMEUtil.toMimeBodyPart(recipient.getContent(pk, "BC"));
				break;
			}
		}
		if(mimeBodyPart == null)
		{
			throw new NullPointerException("the mail not decypted success!");
		}
		
		return mimeBodyPart;
	}
}
