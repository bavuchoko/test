package egovframework.config;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class Gmail extends Authenticator {
    @Override
    protected PasswordAuthentication getPasswordAuthentication(){
        return new PasswordAuthentication(" G메일 아이디", "G메일 비밀번호");
        
    }
}
