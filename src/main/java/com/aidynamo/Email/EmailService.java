package com.aidynamo.Email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


@Service
public class EmailService {
    @Value("${sendgrid.api.key}")
    private String key;
    @Value("${spring.mail.username}")
    private String senderEmail;
    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendSimpleEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }


    @Async
    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        helper.setFrom(senderEmail);

        javaMailSender.send(mimeMessage);
    }
    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    public String sendEmailsFromCsv(MultipartFile file, String subject) {
        List<String> emailAddresses = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader("emails").withFirstRecordAsHeader())) {

            for (CSVRecord record : csvParser) {
                emailAddresses.add(record.get("emails"));
            }

        } catch (IOException e) {
            return "Error reading CSV file: " + e.getMessage();
        }

        return sendEmails(emailAddresses, subject);
    }

    //    public String sendEmails(List<String> recipients, String subject, String body) {
//        Email from = new Email("no-reply@openwhitepapers.com");
//        Content content = new Content("text/html", body);
//        Mail mail = new Mail();
//        mail.setFrom(from);
//        mail.setSubject(subject);
//        mail.addContent(content);
//
//        for (String recipientEmail : recipients) {
//            Email to = new Email(recipientEmail);
//            Personalization personalization = new Personalization();
//            personalization.addTo(to);
//            mail.addPersonalization(personalization);
//        }
//
//        SendGrid sg = new SendGrid(sendGridApiKey);
//        Request request = new Request();
//
//        try {
//            request.setMethod(Method.POST);
//            request.setEndpoint("mail/send");
//            request.setBody(mail.build());
//            Response response = sg.api(request);
//
//            return "Status Code: " + response.getStatusCode() +
//                    ", Body: " + response.getBody() +
//                    ", Headers: " + response.getHeaders();
//        } catch (IOException ex) {
//            return "Error: " + ex.getMessage();
//        }
//    }
    public String sendEmails(List<String> recipients, String subject) {
        Email from = new Email("no-reply@openwhitepapers.com");

        String body = """
<!DOCTYPE html>
<html>
<head>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #121212;
            color: #e0e0e0;
        }
        .email-container {
            max-width: 600px;
            margin: 20px auto;
            background-color: #1f1f1f;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
        }
        .header {
            background-color: #212121;
            color: #76ff03;
            padding: 20px;
            text-align: center;
        }
        .header h1 {
            margin: 0;
            font-size: 28px;
        }
        .content {
            padding: 20px;
            color: #e0e0e0;
        }
        .content h2 {
            margin-top: 0;
            color: #76ff03;
        }
        .content p {
            line-height: 1.6;
        }
        .content a {
            color: #76ff03;
            text-decoration: none;
        }
        .footer {
            background-color: #1f1f1f;
            padding: 10px;
            text-align: center;
            font-size: 12px;
            color: #777777;
        }
        .footer a {
            color: #76ff03;
            text-decoration: none;
        }
    </style>
</head>
<body>
    <div class="email-container">
        <div class="header">
            <h1>Aidynamo is Coming Soon!</h1>
        </div>
        <div class="content">
            <h2>Hello,</h2>
            <p>
                We're thrilled to announce that <strong>Aidynamo</strong> is launching soon! Get ready to experience the future of AI-driven solutions, bringing cutting-edge technology to your fingertips.
            </p>
            <p>
                Our website, <a href="https://idynamo.ai" target="_blank">idynamo.ai</a>, will be live soon with more information, updates, and exclusive access to our services. Stay tuned!
            </p>
            <p>
                If you have any questions or need more information, don't hesitate to reach out. We're here to help.
            </p>
            <p>
                Best regards,<br>
                The Aidynamo Team
            </p>
        </div>
        <div class="footer">
            <p>
                You're receiving this email because you signed up for updates from Aidynamo. If you'd prefer not to receive updates, <a href="#">unsubscribe here</a>.
            </p>
        </div>
    </div>
</body>
</html>
""";


        Content content = new Content("text/html", body);

        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setSubject(subject);
        mail.addContent(content);

        for (String recipientEmail : recipients) {
            Email to = new Email(recipientEmail);
            Personalization personalization = new Personalization();
            personalization.addTo(to);
            mail.addPersonalization(personalization);
        }

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            return "Status Code: " + response.getStatusCode() +
                    ", Body: " + response.getBody() +
                    ", Headers: " + response.getHeaders();
        } catch (IOException ex) {
            return "Error: " + ex.getMessage();
        }
    }
}
