package com.aidynamo.SendGrid;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendGridEvent {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
    private String event;
    private String email;
    private Long timestamp;
    private String reason;
    private String smtpId;


}
