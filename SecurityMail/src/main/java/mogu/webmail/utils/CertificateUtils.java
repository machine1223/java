package mogu.webmail.utils;

import mogu.webmail.service.User;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.x509.X509V3CertificateGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

/**
 * 用来生成证书，并将相应的公钥、私钥、证书 进行保存
 * 
 * @author Administrator
 */
@SuppressWarnings("deprecation" )
public class CertificateUtils
{
	/**
	 * 生成CA证书，此证书的签发者主体和待认证证书的主体是一样的，自己给自己签发为CA证书。
	 * 
	 * @param caCertificate
	 *            证书的信息
	 * @throws Exception
	 */
	public static void generateCACertificate() throws Exception
	{
		// CA证书主体
		MoguCertificate caCertificate = new MoguCertificate();
		caCertificate.setName("CA-MOGU");
		caCertificate.setCompany("company-MOGU");
		caCertificate.setOrganization("organization-MOGU");
		caCertificate.setCity("BJ");
		caCertificate.setProvince("BJ");
		caCertificate.setCountry("CN");
		caCertificate.setPassword("123456");
		
		// CA的密钥对
		KeyPair kp = KeyUtils.generateKeyPair();
		
		// 将私钥保存至ca-der.key文件中
		File caPrivateKeyfile = new File(Constants.CA_PRIVATEKEY_PATH);
		if(caPrivateKeyfile.exists())
			return;
		caPrivateKeyfile.createNewFile();
		FileOutputStream out = new FileOutputStream(caPrivateKeyfile);
		out.write(kp.getPrivate().getEncoded());
		
		// 生成证书
		X509V3CertificateGenerator v3CertGen = new X509V3CertificateGenerator();
		v3CertGen.setSerialNumber(BigInteger.valueOf(1));
		// CN:姓氏、名字 OU:组织单位名称 O:组织名称 L:城市、区域 C:国家代码
		String X509Principal = "CN=" + caCertificate.getName() + ",OU=" + caCertificate.getCompany() + ",O=" + caCertificate.getOrganization() + ",L=" + caCertificate.getCity() + ",C=" + caCertificate.getCountry();
		// 签发者的主体名称（CA的主体名称）
		v3CertGen.setIssuerDN(new X509Principal(X509Principal));
		// 待认证证书的主体
		v3CertGen.setSubjectDN(new X509Principal(X509Principal));
		Date start = caCertificate.getStart();
		Date end = caCertificate.getEnd();
		if(null == start)
			start = new Date(System.currentTimeMillis());
		if(null == end)
			end = new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365));
		v3CertGen.setNotBefore(start);
		v3CertGen.setNotAfter(end);
		v3CertGen.setPublicKey(kp.getPublic());
		v3CertGen.setSignatureAlgorithm(Constants.SIGNATURE_ALGORITHM);// 签名算法
		// 生成证书
		X509Certificate cert = v3CertGen.generateX509Certificate(kp.getPrivate());
		String certFile = Constants.CA_CERTIFICATE_PATH;
		File cerfile = new File(certFile);
		// 如果证书已经存在则返回
		if(cerfile.exists())
			return;
		else
			cerfile.createNewFile();
		FileOutputStream out2 = new FileOutputStream(cerfile);
		out2.write(cert.getEncoded());
		saveCertificateToTruststore("CA-MOGU", cert);
	}
	
	/**
	 * @param caCertificatePath
	 *            CA证书路径
	 * @param certificate
	 *            用户申请证书的信息
	 * @param pk
	 *            密钥对中私钥
	 * @return
	 * @throws Exception
	 */
	public static X509Certificate generateX509Certificate(String caCertificatePath, MoguCertificate certificate, PublicKey pk) throws Exception
	{
		// 生成用户证书
		X509V3CertificateGenerator v3CertGen = new X509V3CertificateGenerator();
		v3CertGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
		String X509Principal = "CN=" + certificate.getName() + ",OU=" + certificate.getCompany() + ",O=" + certificate.getOrganization() + ",L=" + certificate.getCity() + ",C=" + certificate.getCountry();
		// 签发者的主体名称（CA的主体名称）
		X509Certificate caCertificate = getCertificate(caCertificatePath);
		v3CertGen.setIssuerDN(caCertificate.getIssuerX500Principal());
		// 设置有效期
		Date start = certificate.getStart();
		Date end = certificate.getEnd();
		if(null == start)
			start = new Date(System.currentTimeMillis());
		if(null == end)
			end = new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365));
		v3CertGen.setNotBefore(start);
		v3CertGen.setNotAfter(end);
		// 设置证书主体
		v3CertGen.setSubjectDN(new X509Principal(X509Principal));
		v3CertGen.setPublicKey(pk);
		// 签名算法
		v3CertGen.setSignatureAlgorithm("SHA1withRSA");
		// 生成证书
		X509Certificate cert = v3CertGen.generateX509Certificate(KeyUtils.getCAPrivateKey());
		File certFile = new File(Constants.generateUserCertificatePath(certificate.getName()));
		if(certFile.exists())
			throw new Exception("用户‘" + certificate.getName() + "’的证书已经存在！");
		certFile.createNewFile();
		FileOutputStream out = new FileOutputStream(certFile);
		out.write(cert.getEncoded());
		System.out.println("用户'" + certificate.getName() + "'的证书创建成功，位置:" + Constants.generateUserCertificatePath(certificate.getName()));
		return cert;
	}
	
	/**
	 * 生成PFX文件，PFX其实也是一个keystore
	 * 
	 * @param userName
	 * @param password
	 * @throws Exception
	 */
	public static void generatePFX(User user, X509Certificate userCertificate, PrivateKey userPrivateKey) throws Exception
	{
		KeyStore pfxStore = KeyStore.getInstance("PKCS12");
		File pfxFile = new File(Constants.generateUserPfxPath(user.getUserEmail()));
		if(pfxFile.exists())
			throw new Exception("用户‘" + user.getUserEmail() + "’的PFX文件已经存在！");
		pfxFile.createNewFile();
		pfxStore.load(null, Constants.KEYSTORE_PASSWORD.toCharArray());
		
		// 取得CA证书，供下面构建证书链
		X509Certificate caCertificate = CertificateUtils.getCertificate(Constants.CA_CERTIFICATE_PATH);
		// 往pfx中添加用户私钥及证书链
		// 特别说明，这个地方的第二参数和pix的密码一样，而不是用户私钥的密码。
		// 如果是用户私钥的密码，由于和pfx文件的密码不一样，那么IE将不能够对保存其中的私钥进行读取。
		// 此时IE会在导入pfx文件时报错（需要另外一个程序在IE中进行私钥的读取，这个程序应该要求用户输入正确的私钥）
		// 当然了，如果私钥的密码和pfx密钥库的密码一样的话，IE就能够读取私钥的内容，并使用私钥进行加解密。
		pfxStore.setKeyEntry(user.getUserEmail(), userPrivateKey, Constants.KEYSTORE_PASSWORD.toCharArray(), new X509Certificate[] { userCertificate, caCertificate });
		
		FileOutputStream pfxout = new FileOutputStream(pfxFile);
		pfxStore.store(pfxout, Constants.KEYSTORE_PASSWORD.toCharArray());
		System.out.println("用户'" + user.getUserEmail() + "'的pfx文件创建成功，位置:" + Constants.generateUserPfxPath(user.getUserEmail()));
		System.out.println();
		pfxout.close();
	}
	
	/**
	 * 此方法是将证书存入.truststore文件中。
	 * 
	 * @throws Exception
	 */
	public static void saveCertificateToTruststore(String certificateAlias, X509Certificate caCertificate) throws Exception
	{
		KeyStore ks = KeyStore.getInstance(Constants.KEYSTORE_TYPE_JKS);
		File keystoreFile = new File(Constants.TRUSTSTORE_PATH);
		if(!keystoreFile.exists())
		{
			keystoreFile.createNewFile();
			ks.load(null, Constants.TRUSTSTORE_PASSWORD.toCharArray());
		}
		else
		{
			ks.load(new FileInputStream(keystoreFile), Constants.KEYSTORE_PASSWORD.toCharArray());
		}
		ks.setCertificateEntry(certificateAlias, caCertificate);
		ks.store(new FileOutputStream(keystoreFile), Constants.KEYSTORE_PASSWORD.toCharArray());
		System.out.println("证书'" + certificateAlias + "'已存入信任库，位置:" + Constants.TRUSTSTORE_PATH);
	}
	
	/**
	 * 通过证书路径读取证书
	 * 
	 * @param certFilePath
	 * @return
	 * @throws Exception
	 */
	public static X509Certificate getCertificate(String certFilePath) throws Exception
	{
		CertificateFactory cf = CertificateFactory.getInstance("X509");
		X509Certificate x509Cert = (X509Certificate) cf.generateCertificate(new FileInputStream(certFilePath));
		return x509Cert;
	}
	
	/**
	 * 验证证书时候过期
	 * 
	 * @param certificatePath
	 * @param date
	 *            日期为空的话则为当前日期
	 * @return
	 */
	public static boolean verifyCertificate(String certificatePath, Date date)
	{
		boolean status = true;
		try
		{
			// 取得证书
			X509Certificate certificate = getCertificate(certificatePath);
			// 验证证书是否过期或无效
			if(null == date)
				date = new Date();
			certificate.checkValidity(date);
		}
		catch (Exception e)
		{
			status = false;
		}
		return status;
	}
	
	/**
	 * 查看一个用户证书的信息
	 * 
	 * @param userName
	 */
	public static void viewX509CertificateInfo(String userName)
	{
		try
		{
			String certFile = Constants.generateUserCertificatePath(userName);
			CertificateFactory cf = CertificateFactory.getInstance("X509");
			X509Certificate x509Cert = (X509Certificate) cf.generateCertificate(new FileInputStream(certFile));
			System.out.println("IssuerDN: " + x509Cert.getIssuerDN());
			System.out.println("Signature   alg: " + x509Cert.getSigAlgName());
			System.out.println("Version: " + x509Cert.getVersion());
			System.out.println("Serial   Number: " + x509Cert.getSerialNumber());
			System.out.println("Subject   DN: " + x509Cert.getSubjectDN());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
