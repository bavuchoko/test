package egovframework.config;


import egovframework.account.dto.Account;
import egovframework.board.dto.BoardDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Properties;

@Service
public class MailSender {

    public static void sendMail(BoardDto boardDto, String url, String sender) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account)authentication.getPrincipal();

        String to =account.getUsername();  //이메일 주소
        // String host="192.168.1.41:7070";
        String from=sender;   /// 보내는 사람 메일 아이디
        //제목
        String subject="등록 되었습니다 > " + boardDto.getTitle();
        String content ="<!DOCTYPE html>";
        content +="<html>";
        content +="<h1>등록하신 글의 링크주소 입니다.</h1>";
        content +="<a href='"+url+"/board/view.do?boardKey="+ boardDto.getBoardKey()+"'> ";
        content +="<p>링크클릭</p>";
        content += "</a><div style='border-top: 1px solid #DDD; padding: 5px;'></div></div></body></html>";




        Properties p =new Properties();
        p.put("mail.smtp.user", from);
        p.put("mail.smtp.host", "smtp.googlemail.com");
        p.put("mail.smtp.port", "465");
        p.put("mail.smtp.starttls.enable", "true");
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.debug", "true");
        p.put("mail.smtp.socketFactory.port", "465");
        p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        p.put("mail.smtp.socketFactory.fallback", "false");

        try{
            Authenticator auth =new Gmail();
            Session ses =Session.getInstance(p, auth);
            ses.setDebug(true);
            MimeMessage msg =new MimeMessage(ses);
            msg.setSubject(subject);
            Address fromAddr =new InternetAddress(from);
            msg.setFrom(fromAddr);
            Address toAddr =new InternetAddress(to);
            msg.addRecipient(Message.RecipientType.TO, toAddr);
            msg.setContent(content, "text/html;charset=UTF-8");
            Transport.send(msg);
        }catch(Exception e){e.printStackTrace();}

        String aa = "";

    }


}