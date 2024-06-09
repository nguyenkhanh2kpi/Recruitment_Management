package com.java08.quanlituyendung.entity.Favorite;
import com.java08.quanlituyendung.entity.BaseEntity;
import com.java08.quanlituyendung.entity.JobPostingEntity;
import com.java08.quanlituyendung.entity.UserAccountEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Favorite")
public class FavoriteEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccountEntity user;

    @ManyToOne
    @JoinColumn(name = "job_posting_id", nullable = false)
    private JobPostingEntity jobPosting;
}