package mogu.webmail.utils;

import mogu.webmail.service.User;

import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import java.security.Security;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: pairwinter
 * Date: 13-10-13
 * Time: 下午1:18
 * To change this template use File | Settings | File Templates.
 */
public class MailUtils {


    private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    private static final int SMTP_PORT = 465; // smtp 服务器端口
    private static final int SMTP_SEND_PORT = 465; // smtp 服务器端口
    private static final int POP3_PORT = 995; // pop3 服务器端口

    static {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
    }

    public static Properties buildProperty(User user) throws Exception{
        String email = user.getUserEmail();
        String host = "smtp." + email.split("@")[1];
        Properties prop = new Properties();
        prop.setProperty("mail.smtp.host", host);
        prop.setProperty("mail.smtp.starttls.enable","true");
        prop.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        prop.setProperty("mail.smtp.socketFactory.fallback", "false");
        prop.setProperty("mail.smtp.port", String.valueOf(SMTP_SEND_PORT));
        prop.setProperty("mail.smtp.socketFactory.port", String.valueOf(SMTP_SEND_PORT));
        prop.put("mail.smtp.auth", "true");
        return prop;
    }

    public static Store buildStore(User user) throws Exception{
        String email = user.getUserEmail();
        String host = "smtp." + email.split("@")[1];
        Properties prop = new Properties();
        prop.setProperty("mail.smtp.host", host);
        prop.setProperty("mail.smtp.starttls.enable","true");
        prop.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        prop.setProperty("mail.smtp.socketFactory.fallback", "false");
        prop.setProperty("mail.smtp.port", String.valueOf(SMTP_PORT));
        prop.setProperty("mail.smtp.socketFactory.port", String.valueOf(SMTP_PORT));
        prop.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(prop);
        Store store;
        store = session.getStore("imaps");
        store.connect(host, email, user.getUserPassword());
        return store;
    }

    public static Store buildPop3Store(User user) throws Exception{
        String email = user.getUserEmail();
        String host = "pop." + email.split("@")[1];
        Properties prop = new Properties();
        prop.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
        prop.setProperty("mail.pop3.socketFactory.fallback", "false");
        prop.setProperty("mail.pop3.port", String.valueOf(POP3_PORT));
        prop.setProperty("mail.pop3.socketFactory.port", String.valueOf(POP3_PORT));

        //以下步骤跟一般的JavaMail操作相同
        Session session = Session.getDefaultInstance(prop,null);

        //请将红色部分对应替换成你的邮箱帐号和密码
        URLName urln = new URLName("pop3",host,995,null,email, user.getUserPassword());
        Store store = session.getStore(urln);
        store.connect();
        return store;
    }


}
