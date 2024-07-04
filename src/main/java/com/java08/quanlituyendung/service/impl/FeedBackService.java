package com.java08.quanlituyendung.service.impl;

import com.java08.quanlituyendung.entity.FeedBack.FeedBackEntity;
import com.java08.quanlituyendung.repository.FeedBackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FeedBackService {

    @Autowired
    private FeedBackRepository repository;

    public List<FeedBackEntity> getAllFeedback() {
        return repository.findAll();
    }

    public Optional<FeedBackEntity> getFeedbackById(Long id) {
        return repository.findById(id);
    }

    public FeedBackEntity saveFeedback(FeedBackEntity feedback) {
        return repository.save(feedback);
    }

    public void deleteFeedback(Long id) {
        repository.deleteById(id);
    }

    public FeedBackEntity markAsViewed(Long id) {
        Optional<FeedBackEntity> feedback = repository.findById(id);
        if (feedback.isPresent()) {
            FeedBackEntity entity = feedback.get();
            entity.setView(true);
            return repository.save(entity);
        }
        return null;
    }

    public FeedBackEntity pinFeedback(Long id) {
        Optional<FeedBackEntity> feedback = repository.findById(id);
        if (feedback.isPresent()) {
            FeedBackEntity entity = feedback.get();
            entity.setIsPin(!entity.getIsPin());
            return repository.save(entity);
        }
        return null;
    }
}