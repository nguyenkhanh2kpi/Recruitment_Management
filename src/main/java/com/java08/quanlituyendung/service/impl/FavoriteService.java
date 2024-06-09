package com.java08.quanlituyendung.service.impl;

import com.java08.quanlituyendung.auth.UserAccountRetriever;
import com.java08.quanlituyendung.dto.Favorite.FavoriteResponseDto;
import com.java08.quanlituyendung.entity.Favorite.FavoriteEntity;
import com.java08.quanlituyendung.entity.JobPostingEntity;
import com.java08.quanlituyendung.entity.UserAccountEntity;
import com.java08.quanlituyendung.repository.FavoriteRepository;
import com.java08.quanlituyendung.repository.JobPostingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final JobPostingRepository jobPostingRepository;
    private final UserAccountRetriever userAccountRetriever;

//    public FavoriteResponseDto addFavorite(Authentication authentication, Long jobPostingId) {
//        UserAccountEntity user = userAccountRetriever.getUserAccountEntityFromAuthentication(authentication);
//        JobPostingEntity jobPosting = jobPostingRepository.findById(jobPostingId)
//                .orElseThrow(() -> new RuntimeException("Job Posting not found"));
//        var favorite = FavoriteEntity.builder()
//                .user(user)
//                .jobPosting(jobPosting)
//                .build();
//        favoriteRepository.save(favorite);
//        return toDto(favorite);
//    }

    public List<FavoriteResponseDto> addFavorite(Authentication authentication, Long jobPostingId) {
        UserAccountEntity user = userAccountRetriever.getUserAccountEntityFromAuthentication(authentication);
        JobPostingEntity jobPosting = jobPostingRepository.findById(jobPostingId)
                .orElseThrow(() -> new RuntimeException("Job Posting not found"));
        FavoriteEntity existingFavorite = favoriteRepository.findByUserAndJobPosting(user, jobPosting);

        if (existingFavorite != null) {
            favoriteRepository.delete(existingFavorite);
        } else {
            var favorite = FavoriteEntity.builder()
                    .user(user)
                    .jobPosting(jobPosting)
                    .build();
            favoriteRepository.save(favorite);
        }

        return favoriteRepository.findByUser(user)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }



    @Transactional
    public void deleteFavorite(Authentication authentication, Long jobPostingId) {
        UserAccountEntity user = userAccountRetriever.getUserAccountEntityFromAuthentication(authentication);
        JobPostingEntity jobPosting = jobPostingRepository.findById(jobPostingId)
                .orElseThrow(() -> new RuntimeException("Job Posting not found"));
        favoriteRepository.deleteByUserAndJobPosting(user, jobPosting);
    }

    public List<FavoriteResponseDto> getUserFavorites(Authentication authentication) {
        UserAccountEntity user = userAccountRetriever.getUserAccountEntityFromAuthentication(authentication);
        return favoriteRepository.findByUser(user)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private FavoriteResponseDto toDto(FavoriteEntity favorite) {
        JobPostingEntity job = favorite.getJobPosting();
        return FavoriteResponseDto.builder()
                .id(favorite.getId())
                .jobId(job.getId())
                .jobName(job.getName())
                .jobPosition(job.getPosition())
                .build();
    }
}