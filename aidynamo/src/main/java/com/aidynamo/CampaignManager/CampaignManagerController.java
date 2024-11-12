package com.aidynamo.CampaignManager;

import com.aidynamo.utils.ApiResponse;
import com.aidynamo.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/campaign-manager")
public class CampaignManagerController {
    @Autowired
    CampaignManagerService campaignManagerService;

    @Autowired
    PasswordEncoder passwordEncoder;
    @GetMapping
    public ResponseEntity<ApiResponse<List<CampaignManager>>> getAllCampaignManager(){
        List<CampaignManager> list = campaignManagerService.getAllCampaignManager();
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(list,"SUCCESS", true));
    }
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CampaignManager>> addCampaignManager(@RequestParam String email,@RequestParam String password)
    {
        CampaignManager campaignManager = new CampaignManager();
        campaignManager.setEmail(email);
        String hashPassword = passwordEncoder.encode(password);
        campaignManager.setPassword(hashPassword);
        campaignManagerService.campaignManagerRepository.save(campaignManager);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(campaignManager,"SUCCESS",true));
    }
}
