package com.aidynamo.SendGrid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/sendgrid")
public class SendGridWebHookController {

    @Autowired
    SendGridEventService sendGridEventService;

//        @PostMapping
//        public ResponseEntity<String> handleSendGridEvent(@RequestBody List<SendGridEvent> payload) {
//            System.out.println("Received SendGrid Event: " + payload);
//            sendGridEventService.saveEvents(payload);
//            return ResponseEntity.ok("Received");
//        }
private static final String SENDGRID_WEBHOOK_SECRET = "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEO94Nmd+4idrDUEJfGgNKXv1sadB6vmrtfUNtQHAFrouhQMWnHmwSogy8mPNEdB+zvMkpvdcUvr01PvIMz2C+Qg==";

    @PostMapping
    public ResponseEntity<String> handleSendGridEvent(
            @RequestHeader(value = "X-Signature", required = false) String signature,
            @RequestHeader(value = "X-Timestamp", required = false) String timestamp,
            @RequestBody List<SendGridEvent> events) {

        System.out.println("Received Webhook Request:");
        System.out.println("X-Signature: " + signature);
        System.out.println("X-Timestamp: " + timestamp);
        System.out.println("Payload: " + events);

        String payload = new String(events.toString());
        String computedSignature = computeSignature(timestamp, payload);

        System.out.println("Computed Signature: " + computedSignature);

        if (!computedSignature.equals(signature)) {
            System.out.println("Signature verification failed!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid signature");
        }

        System.out.println("Signature verified, saving events...");
        for (SendGridEvent event : events) {
            sendGridEventService.saveEvent(event);
        }

        return ResponseEntity.ok("Received and Saved");
    }


    /**
     * Computes the expected signature from the timestamp and payload using HMAC-SHA256
     *
     * @param timestamp the timestamp from SendGrid
     * @param payload   the body of the webhook request
     * @return the computed signature
     */
    private String computeSignature(String timestamp, String payload) {
        try {
            String data = timestamp + payload;
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(SENDGRID_WEBHOOK_SECRET.getBytes(), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hash = mac.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error computing signature", e);
        }
    }
    @GetMapping("/reports")
    public ResponseEntity<List<SendGridEvent>> getAllEvents(){
        return ResponseEntity.ok().body(sendGridEventService.getAllEvents());
    }
    }


