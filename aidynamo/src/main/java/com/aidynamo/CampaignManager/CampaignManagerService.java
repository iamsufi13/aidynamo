package com.aidynamo.CampaignManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampaignManagerService {
    @Autowired
    CampaignManagerRepository campaignManagerRepository;
    public List<CampaignManager> getAllCampaignManager() {
        return campaignManagerRepository.findAll();
    }
}
