package com.java08.quanlituyendung.repository;

import com.java08.quanlituyendung.entity.Favorite.FavoriteEntity;
import com.java08.quanlituyendung.entity.JobPostingEntity;
import com.java08.quanlituyendung.entity.UserAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {
    List<FavoriteEntity> findByUser(UserAccountEntity user);
    void deleteByUserAndJobPosting(UserAccountEntity user, JobPostingEntity jobPosting);
    FavoriteEntity findByUserAndJobPosting(UserAccountEntity user, JobPostingEntity jobPosting);

}