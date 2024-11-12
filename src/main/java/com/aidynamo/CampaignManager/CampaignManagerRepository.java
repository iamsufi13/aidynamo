package com.aidynamo.CampaignManager;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignManagerRepository extends JpaRepository<CampaignManager,Long> {
    CampaignManager findByEmail(String email);
}
