package com.java08.quanlituyendung.repository;

import com.java08.quanlituyendung.entity.FeedBack.FeedBackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedBackRepository extends JpaRepository<FeedBackEntity, Long> {
}