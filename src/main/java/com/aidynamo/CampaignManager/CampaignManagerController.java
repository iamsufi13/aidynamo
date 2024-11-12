package com.aidynamo.CampaignManager;

import com.aidynamo.Jwt.JwtResponse;
import com.aidynamo.Security.JwtHelper;
import com.aidynamo.User.User;
import com.aidynamo.User.UserRepository;
import com.aidynamo.User.UserService;
import com.aidynamo.utils.ApiResponse;
import com.aidynamo.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campaign-manager")
public class CampaignManagerController {
    @Autowired
    CampaignManagerService campaignManagerService;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtHelper helper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @GetMapping
    public ResponseEntity<ApiResponse<List<CampaignManager>>> getAllCampaignManager(){
        List<CampaignManager> list = campaignManagerService.getAllCampaignManager();
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(list,"SUCCESS", true));
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> loginCampaignManager(@RequestParam String email,@RequestParam String password)
    {
        CampaignManager campaignManager = campaignManagerService.campaignManagerRepository.findByEmail(email);
        String storedPassword =campaignManager.getPassword();
        if (!passwordEncoder.matches(password,storedPassword)){
            return ResponseEntity.ok().body(ResponseUtils.createResponse1(null,"Invalid Credentials",true));
        }
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setUsername(email);
        String token = this.helper.generateToken(campaignManager);
        jwtResponse.setJwtToken(token);
        ApiResponse<JwtResponse> response = ResponseUtils.createResponse1(jwtResponse, "Login Successful", true);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(jwtResponse,"SUCCESS",true));
    }

    @PostMapping("/add-user")
    public ResponseEntity<ApiResponse<User>> addUser(@RequestParam String name,@RequestParam String email, @RequestParam String password, @AuthenticationPrincipal CampaignManager campaignManager){
        User user = new User();
        user.setEmail(email);
        String hashcode = passwordEncoder.encode(password);
        user.setPassword(hashcode);
        user.setCampaignManager(campaignManager);
        user.setName(name);
        userService.addUser(user);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(user,"SUCCESS",true));
    }
    @PutMapping("/update-password")
    public ResponseEntity<ApiResponse<CampaignManager>> updateCampaignManagerPassword(@RequestParam String password, @AuthenticationPrincipal CampaignManager campaignManager){
        CampaignManager campaignManager1 = campaignManagerService.campaignManagerRepository.findById(campaignManager.getId()).orElse(null);
        String hashCode = passwordEncoder.encode(password);
        campaignManager1.setPassword(hashCode);
        campaignManagerService.addCampaignManager(campaignManager1);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(campaignManager1,"SUCCESS",true));
    }
    @PutMapping("/transfer-user")
    public ResponseEntity<ApiResponse<User>> transferUser(@RequestParam long userId,@RequestParam long managerId,@AuthenticationPrincipal CampaignManager campaignManager){
        User user = userRepository.findById(userId).orElse(null);

        CampaignManager campaignManager1 = campaignManagerService.campaignManagerRepository.findById(managerId).orElse(null);
        assert user != null;
        user.setCampaignManager(campaignManager1);
        userService.addUser(user);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(user,"SUCCESS",true));
    }
}
