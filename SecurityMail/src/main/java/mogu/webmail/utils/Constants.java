package mogu.webmail.utils;

import java.io.File;

public class Constants
{
	public static final String	host				= "192.168.6.39";
	public static final String	userName			= "d1@d.com";
	public static final String	password			= "123";
	
	public static String		BASE_PATH			= "/home/pairwinter/soft/server/ssl/key/";
	
	/**
	 * 证书的根路径
	 */
	public static String		CERTIFICATE_PATH	= "";
	/**
	 * CA证书的存放路径
	 */
	public static String		CA_CERTIFICATE_PATH	= "";
	/**
	 * CA的私钥存放路径
	 */
	public static String		CA_PRIVATEKEY_PATH	= "";
	/**
	 * .keystore的存放路径
	 */
	public static String		KEYSTORE_PATH		= "";
	/**
	 * .truststore的存放路径
	 */
	public static String		TRUSTSTORE_PATH		= "";
	/**
	 *.keystore的密码
	 */
	public static String		KEYSTORE_PASSWORD	= "mogu";
	/**
	 *.truststore的密码
	 */
	public static String		TRUSTSTORE_PASSWORD	= "mogu";
	/**
	 * .keystore的类型(jks)
	 */
	public static String		KEYSTORE_TYPE_JKS	= "JKS";
	/**
	 * .keystore的类型(PKCS#12 既 pfx格式的文件)
	 */
	public static String		KEYSTORE_TYPE_PFX	= "PKCS#12";
	/**
	 * 密码对的生成算法
	 */
	public static String		KEYPAIR_ALGORITHM	= "RSA";
	/**
	 * 给证书签名时（这里及用CA的私钥为其他证书进行签署）的算法
	 */
	public static String		SIGNATURE_ALGORITHM	= "SHA1withRSA";
	
	/**
	 * 密钥的位数
	 */
	public static int			KEY_SIZE			= 1024;
	
	public static void generateBasePath()
	{
		CERTIFICATE_PATH = BASE_PATH + "mogu" + File.separator + "certificate";
		CA_CERTIFICATE_PATH = CERTIFICATE_PATH + "" + File.separator + "ca" + File.separator + "ca-der.crt";
		CA_PRIVATEKEY_PATH = CERTIFICATE_PATH + "" + File.separator + "ca" + File.separator + "ca-der.key";
		KEYSTORE_PATH = CERTIFICATE_PATH + File.separator + "mogu.keystore";
		TRUSTSTORE_PATH = CERTIFICATE_PATH + File.separator + "mogu.truststore";
		
		File file = new File(CERTIFICATE_PATH + File.separator + "ca");
		if(!file.exists())
			file.mkdirs();
		File file2 = new File(CERTIFICATE_PATH + File.separator + "user");
		if(!file2.exists())
			file2.mkdirs();
	}
	
	public static String generateUserCertificatePath(String userName)
	{
		return CERTIFICATE_PATH + File.separator + "user" + File.separator + userName + "-der.crt";
	}
	
	public static String generateUserPrivateKeyPath(String userName)
	{
		return CERTIFICATE_PATH + File.separator + "user" + File.separator + userName + "-der.key";
	}
	
	public static String generateUserPfxPath(String userName)
	{
		return CERTIFICATE_PATH + File.separator + "user" + File.separator + userName + ".pfx";
	}
	
}
