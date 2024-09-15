package com.travel.domain.like.service;

import com.travel.domain.accommodation.dto.response.AccommodationResponse;
import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.accommodation.repository.AccommodationRepository;
import com.travel.domain.like.dto.request.LikeRequest;
import com.travel.domain.like.dto.response.LikeResponse;
import com.travel.domain.like.entity.Like;
import com.travel.domain.like.repository.LikeRepository;
import com.travel.domain.member.entity.Member;
import com.travel.domain.member.repository.MemberRepository;
import com.travel.global.exception.AccommodationException;
import com.travel.global.exception.MemberException;
import com.travel.global.exception.type.ErrorType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final AccommodationRepository accommodationRepository;

    @Transactional
    public LikeResponse clickLike(Long userId, LikeRequest request) {
        Member member = memberRepository.findById(userId)
            .orElseThrow(() -> new MemberException(ErrorType.INVALID_EMAIL_AND_PASSWORD));
        Accommodation accommodation = accommodationRepository.findById(request.getAccommodationId())
            .orElseThrow(() -> new AccommodationException(ErrorType.NOT_FOUND));

        Optional<Like> existingLike = likeRepository.findByMemberAndAccommodation(member, accommodation);
        boolean liked;

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            liked = false;
        } else {
            Like newLike = Like.builder()
                .member(member)
                .accommodation(accommodation)
                .build();
            likeRepository.save(newLike);
            liked = true;
        }

        int likeCount = likeRepository.countByAccommodation(accommodation);
        return new LikeResponse(liked, likeCount);
    }

    @Transactional(readOnly = true)
    public List<AccommodationResponse> getLikedAccommodations(Long userId) {
        Member member = memberRepository.findById(userId)
            .orElseThrow(() -> new MemberException(ErrorType.INVALID_EMAIL_AND_PASSWORD));
        List<Accommodation> likedAccommodations = likeRepository.findByMember(member).stream()
            .map(Like::getAccommodation)
            .toList();
        return likedAccommodations.stream()
            .map(AccommodationResponse::createAccommodationResponse)
            .collect(Collectors.toList());
    }
}