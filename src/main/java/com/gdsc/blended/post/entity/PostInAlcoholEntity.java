package com.gdsc.blended.post.entity;

import com.gdsc.blended.alcohol.entity.AlcoholEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "tb_post_in_alcohol")
public class PostInAlcoholEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_in_alcohol_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity postEntity;

    @ManyToOne
    @JoinColumn(name = "alcohol_id")
    private AlcoholEntity alcoholEntity;



}
