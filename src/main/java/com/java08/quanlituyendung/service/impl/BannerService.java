package com.java08.quanlituyendung.service.impl;
import com.java08.quanlituyendung.entity.Banner.BannerEntity;
import com.java08.quanlituyendung.repository.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BannerService {

    @Autowired
    private BannerRepository bannerRepository;

    public List<BannerEntity> getAllBanners() {
        return bannerRepository.findAll();
    }

    public Optional<BannerEntity> getBannerById(Long id) {
        return bannerRepository.findById(id);
    }

    public BannerEntity createOrUpdateBanner(BannerEntity banner) {
        return bannerRepository.save(banner);
    }

    public void deleteBanner(Long id) {
        bannerRepository.deleteById(id);
    }
}
