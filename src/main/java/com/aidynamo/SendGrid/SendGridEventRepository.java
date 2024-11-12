package com.aidynamo.SendGrid;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SendGridEventRepository extends JpaRepository<SendGridEvent,Long> {
}
