package teamseven.echoeco.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    public void sendEmailWithAttachment(String to, String subject, String text, File file) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        String htmlContent = "<html><body>" + text + "<br><img src='cid:image'></body></html>";
        helper.setText(htmlContent, true);
        helper.addInline("image", file);

        // 파일 첨부
//        helper.addAttachment(attachmentPath.getFileName().toString(), attachmentPath.toFile());

        mailSender.send(message);
    }
}
