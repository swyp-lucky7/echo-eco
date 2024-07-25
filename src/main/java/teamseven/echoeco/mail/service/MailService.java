package teamseven.echoeco.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    public void sendEmailWithAttachment(String to, String subject, String text) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);

        // 파일 첨부
//        helper.addAttachment(attachmentPath.getFileName().toString(), attachmentPath.toFile());

        mailSender.send(message);
    }
}
