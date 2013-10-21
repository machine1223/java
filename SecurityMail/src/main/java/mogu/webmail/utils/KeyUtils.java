package mogu.webmail.utils;

import mogu.webmail.service.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * 用于生成密钥对
 * 
 * @author Administrator
 */
public class KeyUtils
{
	/**
	 * 返回一个生成的密钥对（KeyPair）对象
	 * 
	 * @return
	 * @throws Exception
	 */
	public static KeyPair generateKeyPair() throws Exception
	{
		KeyPairGenerator keygen = KeyPairGenerator.getInstance(Constants.KEYPAIR_ALGORITHM);
		// 随即种子
		SecureRandom secrand = new SecureRandom();
		// 初始化随机产生器
		secrand.setSeed("mogu".getBytes());
		keygen.initialize(Constants.KEY_SIZE, secrand);
		return keygen.genKeyPair();
	}
	
	/**
	 * 此方法是将用户的私钥.keyStore文件中。 生成用户的密钥对，并对私钥写入文件，同时返回该密钥对
	 * 
	 * @throws Exception
	 */
	public static void saveUserKeyToKeystore(User user, PrivateKey pk, Certificate certificate) throws Exception
	{
		KeyStore ks = KeyStore.getInstance(Constants.KEYSTORE_TYPE_JKS);
		File keystoreFile = new File(Constants.KEYSTORE_PATH);
		if(!keystoreFile.exists())
		{
			keystoreFile.createNewFile();
			ks.load(null, Constants.KEYSTORE_PASSWORD.toCharArray());
		}
		else
		{
			ks.load(new FileInputStream(keystoreFile), Constants.KEYSTORE_PASSWORD.toCharArray());
		}
		ks.setCertificateEntry(user.getUserEmail(), (X509Certificate) certificate);
		X509Certificate caCertificate = CertificateUtils.getCertificate(Constants.CA_CERTIFICATE_PATH);
		// 往.keystore中添加用户私钥,并设置用户密码，别名为用户名称
		ks.setKeyEntry(user.getUserEmail(), pk, user.getUserPassword().toCharArray(), new X509Certificate[] { (X509Certificate) certificate, caCertificate });
		ks.store(new FileOutputStream(keystoreFile), Constants.KEYSTORE_PASSWORD.toCharArray());
		System.out.println("用户'" + user.getUserEmail() + "'的私钥已添加到密钥库中，位置:" + Constants.KEYSTORE_PATH);
	}
	
	/**
	 * 从密钥库中读取用户的密钥
	 * 
	 * @param userName
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKeyFromKeystore(String userName, String password) throws Exception
	{
		KeyStore ks = KeyStore.getInstance(Constants.KEYSTORE_TYPE_JKS);
		File keystoreFile = new File(Constants.KEYSTORE_PATH);
		if(!keystoreFile.exists())
		{
			throw new Exception("密钥库不存在！");
		}
		ks.load(new FileInputStream(keystoreFile), Constants.KEYSTORE_PASSWORD.toCharArray());
		PrivateKey privateKey;
		try
		{
			privateKey = (PrivateKey) ks.getKey(userName, password.toCharArray());
		}
		catch (UnrecoverableKeyException uke)
		{
			throw new Exception("用户‘" + userName + "’的私钥密码不正确！");
		}
		return privateKey;
	}
	
	/**
	 * 读取CA的私钥
	 * 
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey getCAPrivateKey() throws Exception
	{
		File privateKeyFile = new File(Constants.CA_PRIVATEKEY_PATH);
		if(!privateKeyFile.exists())
			throw new Exception("CA的私钥不存在，请先出示CA的证书！");
		FileInputStream fis = new FileInputStream(privateKeyFile);
		byte[] der = new byte[1024];
		fis.read(der);
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(der);
		KeyFactory kfac = KeyFactory.getInstance(Constants.KEYPAIR_ALGORITHM);
		PrivateKey privateKey = kfac.generatePrivate(spec);
		return privateKey;
	}
}
