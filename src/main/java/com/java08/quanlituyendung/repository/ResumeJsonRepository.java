package com.java08.quanlituyendung.repository;

import com.java08.quanlituyendung.entity.UserAccountEntity;
import com.java08.quanlituyendung.entity.sample.ResumeJsonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface ResumeJsonRepository extends JpaRepository<ResumeJsonEntity, Long> {
    Optional<ResumeJsonEntity> findByUserAccountEntity(UserAccountEntity userAccountEntity);
}