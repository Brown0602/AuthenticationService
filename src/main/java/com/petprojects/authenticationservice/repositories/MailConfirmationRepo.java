package com.petprojects.authenticationservice.repositories;

import com.petprojects.authenticationservice.services.CodeGenerationService;
import com.petprojects.authenticationservice.services.MailConfirmationService;
import com.petprojects.authenticationservice.services.SaveCodeConfirmationService;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import java.io.IOException;
import java.util.Properties;

@Repository
public class MailConfirmationRepo implements MailConfirmationService {

    SaveCodeConfirmationService saveCodeConfirmationService;
    CodeGenerationService codeGenerationService;

    @Autowired
    public MailConfirmationRepo(SaveCodeConfirmationService saveCodeConfirmationService, CodeGenerationService codeGenerationService){
        this.saveCodeConfirmationService = saveCodeConfirmationService;
        this.codeGenerationService = codeGenerationService;
    }

    @Value("${mail.password}")
    private String mailPassword;
    @Value("${mail.myEmail}")
    private String myEmail;

    @Override
    public String mailConfirmation(String email) throws IOException, MessagingException {

        Properties properties = new Properties();
        properties.load(MailConfirmationRepo.class.getClassLoader().getResourceAsStream("application.properties"));

        Session mailSession = Session.getDefaultInstance(properties);
        MimeMessage message = new MimeMessage(mailSession);
        message.setFrom(new InternetAddress(myEmail));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        message.setSubject("Подтверждение изменения пароля");
        String code_confirmation = codeGenerationService.codeGeneration();
        saveCodeConfirmationService.saveCodeConfirmation(code_confirmation, String.valueOf(email.hashCode()));
        message.setText("Здравствуйте!\nДля подтверждения изменения пароля введите в поле формы указанный код: " + code_confirmation);
        Transport transport = mailSession.getTransport();
        transport.connect(myEmail, mailPassword);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
        return "Введите код подтверждения";
    }
}
