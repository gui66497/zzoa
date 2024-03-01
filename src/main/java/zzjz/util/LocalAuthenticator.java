package zzjz.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * 验证地址类.
 * @author pengpeng
 * @version 2018-01-02
 */
public class LocalAuthenticator extends Authenticator {
    private String username;

    private String password;

    public LocalAuthenticator(String username1, String password1) {
        this.username = username1;
        this.password = password1;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }
}
