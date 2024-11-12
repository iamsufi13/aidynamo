package com.aidynamo.config;

import com.aidynamo.CampaignManager.CampaignManager;
import com.aidynamo.CampaignManager.CampaignManagerRepository;
import com.aidynamo.Dan.Dan;
import com.aidynamo.Dan.DanRepository;
import com.aidynamo.User.User;
import com.aidynamo.User.UserRepository;
import com.aidynamo.admin.Admin;
import com.aidynamo.admin.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final AdminRepository adminRepository;
    private final CampaignManagerRepository campaignManagerRepository;
    private final UserRepository userRepository;
    private final DanRepository danRepository;

    @Autowired
    public CustomUserDetailsService(@Lazy AdminRepository adminRepository, @Lazy CampaignManagerRepository campaignManagerRepository,@Lazy UserRepository userRepository
    ,@Lazy DanRepository danRepository) {
        this.adminRepository = adminRepository;
        this.campaignManagerRepository=campaignManagerRepository;
        this.userRepository=userRepository;
        this.danRepository=danRepository;
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByEmail(username);
        if (admin!=null){
            return admin;
        }
        CampaignManager campaignManager = campaignManagerRepository.findByEmail(username);
        if (campaignManager!=null){
            return campaignManager;
        }
        User user = userRepository.findByEmail(username);
        if (user!=null){
            return user;
        }
        Dan dan = danRepository.findByEmail(username);
        if (dan!=null){
            return dan;
        }
        throw new UsernameNotFoundException("User not found with email " + username);
    }
}
