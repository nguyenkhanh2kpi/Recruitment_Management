package com.java08.quanlituyendung.controller;
import com.java08.quanlituyendung.entity.Banner.BannerEntity;
import com.java08.quanlituyendung.service.impl.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/banners")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @GetMapping
    public List<BannerEntity> getAllBanners() {
        return bannerService.getAllBanners();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BannerEntity> getBannerById(@PathVariable Long id) {
        Optional<BannerEntity> banner = bannerService.getBannerById(id);
        return banner.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BannerEntity> createBanner(@RequestBody BannerEntity banner) {
        BannerEntity createdBanner = bannerService.createOrUpdateBanner(banner);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBanner);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BannerEntity> updateBanner(@PathVariable Long id, @RequestBody BannerEntity banner) {
        banner.setId(id);
        BannerEntity updatedBanner = bannerService.createOrUpdateBanner(banner);
        return ResponseEntity.ok(updatedBanner);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBanner(@PathVariable Long id) {
        bannerService.deleteBanner(id);
        return ResponseEntity.noContent().build();
    }
}
