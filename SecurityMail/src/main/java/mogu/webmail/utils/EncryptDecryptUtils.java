package mogu.webmail.utils;

import mogu.webmail.utils.CertificateUtils;
import mogu.webmail.utils.Constants;
import mogu.webmail.utils.KeyUtils;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.X509Certificate;

import javax.crypto.Cipher;

/**
 * 加密解密工具类
 * 
 * @author Administrator
 */
public class EncryptDecryptUtils
{
	public static byte[] encryptByPrivateKey(String userName, String password, String text) throws Exception
	{
		PrivateKey pk = KeyUtils.getPrivateKeyFromKeystore(userName, password);
		// 对数据加密
		Cipher cipher = Cipher.getInstance(pk.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, pk);
		return cipher.doFinal(text.getBytes());
		
	}
	
	public static byte[] decryptByPrivateKey(String userName, String password, byte[] date) throws Exception
	{
		PrivateKey pk = KeyUtils.getPrivateKeyFromKeystore(userName, password);
		// 对数据加密
		Cipher cipher = Cipher.getInstance(pk.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, pk);
		return cipher.doFinal(date);
	}
	
	public static byte[] encryptByPublicKey(String userName, String text) throws Exception
	{
		PublicKey pk = CertificateUtils.getCertificate(Constants.generateUserCertificatePath(userName)).getPublicKey();
		// 对数据加密
		Cipher cipher = Cipher.getInstance(pk.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, pk);
		return cipher.doFinal(text.getBytes());
		
	}
	
	public static byte[] decryptByPublicKey(String userName, byte[] date) throws Exception
	{
		PublicKey pk = CertificateUtils.getCertificate(Constants.generateUserCertificatePath(userName)).getPublicKey();
		// 对数据加密
		Cipher cipher = Cipher.getInstance(pk.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, pk);
		return cipher.doFinal(date);
	}
	
	/**
	 * 签名
	 * 
	 * @param sign
	 *            待签名的加密字节
	 * @param userName
	 *            签名用户
	 * @param password
	 *            用户密码
	 * @return 签名后的字节数组
	 * @throws Exception
	 */
	public static byte[] sign(byte[] sign, String userName, String password) throws Exception
	{
		// 获得证书
		X509Certificate x509Certificate = (X509Certificate) CertificateUtils.getCertificate(Constants.generateUserCertificatePath(userName));
		PrivateKey pk = KeyUtils.getPrivateKeyFromKeystore(userName, password);
		// 构建签名
		Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
		signature.initSign(pk);
		signature.update(sign);
		return signature.sign();
	}
	
	/**
	 * 验证签名
	 * 
	 * @param data
	 *            未签名的字节数组
	 * @param sign
	 *            待验证的签名字节
	 * @param userName
	 *            签名用户
	 * @return
	 * @throws Exception
	 */
	public static boolean verify(byte[] data, byte[] sign, String userName) throws Exception
	{
		X509Certificate x509Certificate = (X509Certificate) CertificateUtils.getCertificate(Constants.generateUserCertificatePath(userName));
		// 获得公钥
		PublicKey publicKey = x509Certificate.getPublicKey();
		// 构建签名
		Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
		signature.initVerify(publicKey);
		signature.update(data);
		return signature.verify(sign);
		
	}
	
}
