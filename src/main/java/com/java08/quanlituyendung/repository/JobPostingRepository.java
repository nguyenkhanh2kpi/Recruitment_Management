package com.java08.quanlituyendung.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java08.quanlituyendung.entity.JobPostingEntity;

public interface JobPostingRepository extends JpaRepository<JobPostingEntity, Long>{

    JobPostingEntity findOneById(Long id);
}
