package com.aidynamo.admin;

import com.aidynamo.CampaignManager.CampaignManager;
import com.aidynamo.CampaignManager.CampaignManagerService;
import com.aidynamo.Email.EmailService;
import com.aidynamo.Jwt.JwtResponse;
import com.aidynamo.Security.JwtHelper;
import com.aidynamo.utils.ApiResponse;
import com.aidynamo.utils.ResponseUtils;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final EmailService emailService;
    private final CampaignManagerService campaignManagerService;
    private final JwtHelper helper;

    public AdminController(AdminService adminService, PasswordEncoder passwordEncoder, OtpService otpService, EmailService emailService, CampaignManagerService campaignManagerService, JwtHelper helper) {
        this.adminService = adminService;
        this.passwordEncoder = passwordEncoder;
        this.otpService = otpService;
        this.emailService = emailService;
        this.campaignManagerService = campaignManagerService;
        this.helper = helper;
    }
//To Enable the 2fa Login for admin
//    @PostMapping("/login")
//    public ResponseEntity<ApiResponse<Admin>> loginAdmin(@RequestParam String email, @RequestParam String password) throws MessagingException {
//        Admin admin = adminService.adminRepository.findByEmail(email);
//        if (admin != null && passwordEncoder.matches(password, admin.getPassword())) {
//            String otp = String.valueOf((int) (Math.random() * 900000) + 100000);
//
//            otpService.saveOtp(email, otp);
//
//            String htmlMessage = "<div style=\"font-family: Arial, sans-serif; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px; background-color: #f9f9f9;\">" +
//                    "<h2 style=\"text-align: center; color: #28a745; font-size: 24px;\">OTP Verification</h2>" +
//                    "<p style=\"font-size: 16px; line-height: 1.5; text-align: center;\">" +
//                    "Your OTP for verification is <strong>" + otp + "</strong>. Please enter this OTP to complete your login." +
//                    "</p>" +
//                    "<p style=\"font-size: 14px; line-height: 1.5; text-align: center;\">" +
//                    "If you did not request this OTP, please ignore this email." +
//                    "</p>" +
//                    "<hr style=\"border: 0; border-top: 1px solid #e0e0e0; margin: 40px 0;\">" +
//                    "<footer style=\"text-align: center; font-size: 12px; color: #999;\">" +
//                    "<p>&copy; 2024 InfiniteB2B. All rights reserved.</p>" +
//                    "<p><a href=\"#\" style=\"color: #999; text-decoration: none;\">Unsubscribe</a> | <a href=\"#\" style=\"color: #999; text-decoration: none;\">Contact Us</a></p>" +
//                    "</footer>" +
//                    "</div>";
//
//            emailService.sendHtmlEmail(email, "OTP For Verification", htmlMessage);
//
//            return ResponseEntity.ok().body(ResponseUtils.createResponse1(admin, "Verify using OTP sent to your email", true));
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseUtils.createResponse1(null, "LOGIN FAILED", false));
//        }
//    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> loginAdmin(@RequestParam String email, @RequestParam String password) throws MessagingException {
        Admin admin = adminService.adminRepository.findByEmail(email);
        if (admin != null && passwordEncoder.matches(password, admin.getPassword())){
            String token = this.helper.generateToken(admin);
            JwtResponse jwtResponse = new JwtResponse();
            jwtResponse.setUsername(email);
            jwtResponse.setJwtToken(token);
            ApiResponse<JwtResponse> response = ResponseUtils.createResponse1(jwtResponse, "Login Successful", true);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(jwtResponse,"SUCCESS",true));
        }
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(null,"Not Verfied Admin",true));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Admin>> registerAdmin(@RequestParam String name,
                                                            @RequestParam String email,
                                                            @RequestParam String password){
        Admin admin = new Admin();
        admin.setEmail(email);
        admin.setName(name);
        String hashPassword = passwordEncoder.encode(password);
        admin.setPassword(hashPassword);
        adminService.adminRepository.save(admin);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(admin,"SUCCESS",true));
    }
    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<JwtResponse>> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        boolean isOtpValid = otpService.validateOtp(email, otp);

        if (isOtpValid) {
            Admin admin = adminService.adminRepository.findByEmail(email);
            String token = this.helper.generateToken(admin);

            JwtResponse jwtResponse = new JwtResponse();
            jwtResponse.setUsername(email);
            jwtResponse.setJwtToken(token);
            ApiResponse<JwtResponse> response = ResponseUtils.createResponse1(jwtResponse, "Login Successful", true);

            return ResponseEntity.ok().body(ResponseUtils.createResponse1(jwtResponse, "OTP Verified Successfully", true));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseUtils.createResponse1(null, "OTP Verification Failed", false));
        }

    }
    @PostMapping("/add-campaign-manager")
    public ResponseEntity<ApiResponse<CampaignManager>> addCampaignManager(@RequestParam String email,@RequestParam String password,@RequestParam String name){
        CampaignManager campaignManager = new CampaignManager();
        campaignManager.setName(name);
        String hashCode = passwordEncoder.encode(password);
        campaignManager.setPassword(hashCode);
        campaignManager.setEmail(email);
        campaignManagerService.addCampaignManager(campaignManager);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(campaignManager,"SUCCESS",true));
    }
    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<Admin>> updatePassword(@RequestParam String password, @AuthenticationPrincipal Admin admin){
        Admin admin1 = adminService.adminRepository.findById(admin.getId()).orElse(null);
        String newPassword = passwordEncoder.encode(password);
        admin1.setPassword(newPassword);

        adminService.adminRepository.save(admin1);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(admin1,"SUCCESS",true));
    }
}
