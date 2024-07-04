package com.java08.quanlituyendung.controller;

import com.java08.quanlituyendung.entity.FeedBack.FeedBackEntity;
import com.java08.quanlituyendung.service.impl.FeedBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/feedback")
public class FeedBackController {

    @Autowired
    private FeedBackService service;

    @GetMapping
    public List<FeedBackEntity> getAllFeedback() {
        return service.getAllFeedback();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeedBackEntity> getFeedbackById(@PathVariable Long id) {
        Optional<FeedBackEntity> feedback = service.getFeedbackById(id);
        return feedback.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public FeedBackEntity createFeedback(@RequestBody FeedBackEntity feedback) {
        return service.saveFeedback(feedback);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeedBackEntity> updateFeedback(@PathVariable Long id, @RequestBody FeedBackEntity feedbackDetails) {
        Optional<FeedBackEntity> feedback = service.getFeedbackById(id);
        if (feedback.isPresent()) {
            FeedBackEntity entity = feedback.get();
            entity.setName(feedbackDetails.getName());
            entity.setEmail(feedbackDetails.getEmail());
            entity.setMessage(feedbackDetails.getMessage());
            entity.setView(feedbackDetails.getView());
            entity.setIsPin(feedbackDetails.getIsPin());
            return ResponseEntity.ok(service.saveFeedback(entity));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        service.deleteFeedback(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/view")
    public ResponseEntity<FeedBackEntity> markAsViewed(@PathVariable Long id) {
        FeedBackEntity feedback = service.markAsViewed(id);
        if (feedback != null) {
            return ResponseEntity.ok(feedback);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/pin")
    public ResponseEntity<FeedBackEntity> pinFeedback(@PathVariable Long id) {
        FeedBackEntity feedback = service.pinFeedback(id);
        if (feedback != null) {
            return ResponseEntity.ok(feedback);
        }
        return ResponseEntity.notFound().build();
    }
}