package com.aidynamo.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/email")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @PostMapping("/send-from-csv")
    public ResponseEntity<String> sendEmailsFromCsv(@RequestParam("file") MultipartFile file,
                                                    @RequestParam String subject) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("CSV file is missing!");
        }

        String response = emailService.sendEmailsFromCsv(file, subject);
        System.out.println("All Mail Sent SuccessFully");
        return ResponseEntity.ok(response);
    }
}
