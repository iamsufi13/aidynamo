package com.aidynamo.Dan;

import com.aidynamo.Jwt.JwtResponse;
import com.aidynamo.Security.JwtHelper;
import com.aidynamo.utils.ApiResponse;
import com.aidynamo.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dan")
public class DanController {
    @Autowired
    DanRepository danRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtHelper helper;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> loginDan(@RequestParam String email,@RequestParam String password){
        Dan dan = danRepository.findByEmail(email);
        if (dan != null && passwordEncoder.matches(password, dan.getPassword())){
            String token = this.helper.generateToken(dan);
            JwtResponse jwtResponse = new JwtResponse();
            jwtResponse.setUsername(email);
            jwtResponse.setJwtToken(token);
            ApiResponse<JwtResponse> response = ResponseUtils.createResponse1(jwtResponse, "Login Successful", true);
            return ResponseEntity.ok().body(ResponseUtils.createResponse1(jwtResponse,"SUCCESS",true));
        }
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(null,"Not Verfied Admin",true));
    }
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Dan>> register(@RequestParam String email,@RequestParam String password){
        Dan dan = new Dan();
        dan.setEmail(email);
        String hashCode = passwordEncoder.encode(password);
        dan.setPassword(hashCode);
        danRepository.save(dan);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(dan,"SUCCESS",true));
    }
}
