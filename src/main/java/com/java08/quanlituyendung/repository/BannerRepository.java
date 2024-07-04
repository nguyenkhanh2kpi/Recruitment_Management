package com.java08.quanlituyendung.repository;

import com.java08.quanlituyendung.entity.Banner.BannerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerRepository extends JpaRepository<BannerEntity, Long> {
}
