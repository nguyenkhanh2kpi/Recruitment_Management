package com.java08.quanlituyendung.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Conversation {
    @Id
    private Long id;

    @ManyToOne
    private UserAccountEntity user1;

    @ManyToOne
    private UserAccountEntity user2;

    @OneToMany
    private List<Message> messages;

    @Column
    private boolean isDelete;

}
