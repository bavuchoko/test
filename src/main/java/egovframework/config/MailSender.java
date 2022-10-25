package egovframework.config;


import egovframework.account.dto.Account;
import egovframework.board.dto.BoardDto;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;

@Service

public class MailSender {

    @Async("mailExecutor")
    public void sendMail(BoardDto boardDto, String url, String sender, String[] mailTo ) throws IOException {

        //제목
        String subject="게시물이 등록 되었습니다 > " + boardDto.getTitle();
        String content ="<!DOCTYPE html>";
        content +="<html>";
        content +="<body>";
        content +="<h3>다음의 내용으로 등록되었습니다</h3>";
        content +="<h4>제목 : "+ boardDto.getTitle() +"</h4>";
        content +="<h4>본문 : </h4>";
        content +="<div style='border: 1px solid #DDD; padding: 5px;'><pre>";
        content += boardDto.getBody();
        content +="</pre></div>";
        content +="<a href='"+url+"/board/view.do?boardKey="+ boardDto.getBoardKey()+"'> ";
        content +="<p style='font-size: 14px; margin:10px;'> 이 페이지로 가기 </p>";
        content +="</a></body></html>";

        Properties p =new Properties();
        p.put("mail.smtp.user", sender);
        p.put("mail.smtp.host", "smtp.googlemail.com");
        p.put("mail.smtp.port", "465");
        p.put("mail.smtp.starttls.enable", "true");
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.debug", "true");
        p.put("mail.smtp.socketFactory.port", "465");
        p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        p.put("mail.smtp.socketFactory.fallback", "false");


        for (String mail : mailTo) {
            try{
                Authenticator auth =new Gmail();
                Session ses =Session.getInstance(p, auth);
                ses.setDebug(true);
                MimeMessage msg =new MimeMessage(ses);
                msg.setSubject(subject);
                Address fromAddr =new InternetAddress(sender);
                msg.setFrom(fromAddr);
                Address toAddr =new InternetAddress(mail);
                msg.addRecipient(Message.RecipientType.TO, toAddr);
                msg.setContent(content, "text/html;charset=UTF-8");
                Transport.send(msg);
            }catch(Exception e){e.printStackTrace();}
        }

    }
}