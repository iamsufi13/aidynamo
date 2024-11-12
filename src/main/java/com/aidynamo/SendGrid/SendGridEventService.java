package com.aidynamo.SendGrid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SendGridEventService {
    @Autowired
    private SendGridEventRepository eventRepository;

    public void saveEvents(List<SendGridEvent> events) {
        eventRepository.saveAll(events);
    }
    public List<SendGridEvent> getAllEvents() {
        return eventRepository.findAll();
    }

    public void saveEvent(SendGridEvent event) {
        eventRepository.save(event);
    }
}
