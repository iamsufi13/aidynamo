package com.aidynamo.User;

import com.aidynamo.CampaignManager.CampaignManager;
import com.aidynamo.Jwt.JwtResponse;
import com.aidynamo.Security.JwtHelper;
import com.aidynamo.utils.ApiResponse;
import com.aidynamo.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService ;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtHelper helper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @GetMapping("/getbyemail")
    public ResponseEntity<ApiResponse<User>> getByEmail(@RequestParam String email){
        User user = userRepository.findByEmail(email);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(user,"SUCCESS",true));}

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@RequestParam String email,@RequestParam String password)
    {
        User user = userService.userRepositorty.findByEmail(email);
        if (!passwordEncoder.matches(password,user.getPassword())){
            return ResponseEntity.ok().body(ResponseUtils.createResponse1(null,"Invalid Credentials",true));
        }
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setUsername(email);
        String token = this.helper.generateToken(user);
        jwtResponse.setJwtToken(token);
        ApiResponse<JwtResponse> response = ResponseUtils.createResponse1(jwtResponse, "Login Successful", true);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(jwtResponse,"SUCCESS",true));
    }
}
