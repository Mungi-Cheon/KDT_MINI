package com.travel.domain.review.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.member.entity.Member;
import com.travel.domain.reservations.entity.Reservation;
import com.travel.domain.review.dto.request.ReviewRequest;
import com.travel.domain.review.dto.response.ReviewResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(precision = 2, scale = 1)
    private BigDecimal grade;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="accommodation_id")
    @JsonBackReference
    private Accommodation accommodation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonBackReference
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reservation_id")
    @JsonBackReference
    private Reservation reservation;

    public static Review from(ReviewRequest request, Accommodation accommodation,
        Member member, Reservation reservation) {
     return Review.builder()
         .title(request.getTitle())
         .comment(request.getComment())
         .grade(request.getGrade())
         .createdAt(LocalDateTime.now())
         .accommodation(accommodation)
         .member(member)
         .reservation(reservation)
         .build();
    }

    public void update(ReviewRequest request){
        this.title = request.getTitle();
        this.comment  = request.getComment();
        this.grade = request.getGrade();
        this.updatedAt = LocalDateTime.now();
    }
}
