package com.travel.domain.like.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.travel.domain.accommodation.dto.response.AccommodationResponse;
import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.accommodation.entity.AccommodationImage;
import com.travel.domain.accommodation.repository.AccommodationRepository;
import com.travel.domain.like.dto.request.LikeRequest;
import com.travel.domain.like.dto.response.LikeResponse;
import com.travel.domain.like.entity.Like;
import com.travel.domain.like.repository.LikeRepository;
import com.travel.domain.member.entity.Member;
import com.travel.domain.member.repository.MemberRepository;
import com.travel.domain.product.entity.Product;
import com.travel.global.exception.AccommodationException;
import com.travel.global.exception.MemberException;
import com.travel.global.exception.type.ErrorType;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private AccommodationRepository accommodationRepository;

    @InjectMocks
    private LikeService likeService;

    private Member member;

    private Product product;

    private AccommodationImage accommodationImage;

    private Accommodation accommodation;

    private Like like;

    @BeforeEach
    public void setUp() {
        member = Member.builder()
            .id(1L)
            .build();

        product = Product.builder().build();

        accommodationImage = AccommodationImage.builder().build();

        accommodation = Accommodation.builder()
            .id(1L)
            .products(List.of(product))
            .images(accommodationImage)
            .build();

        like = Like.builder()
            .member(member)
            .accommodation(accommodation)
            .build();
    }

    @Test
    @DisplayName("좋아요 클릭 성공")
    void testClickLike_success() {
        // given
        Long memberId = 1L;
        LikeRequest request = new LikeRequest(1L);

        when(memberRepository.findById(any())).thenReturn(Optional.of(member));
        when(accommodationRepository.findById(any())).thenReturn(Optional.of(accommodation));
        when(likeRepository.findByMemberAndAccommodation(any(), any())).thenReturn(Optional.empty());
        when(likeRepository.countByAccommodation(any())).thenReturn(1);

        // when
        LikeResponse response = likeService.clickLike(memberId, request);

        // then
        verify(likeRepository, times(1)).save(any(Like.class));
        assertNotNull(response);
        assertEquals(true, response.getLiked());
        assertEquals(1, response.getLikeCount());
    }

    @Test
    @DisplayName("좋아요 취소 성공")
    void testClickLike_cancelSuccess() {
        // given
        Long memberId = 1L;
        LikeRequest request = new LikeRequest(1L);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(accommodationRepository.findById(request.getAccommodationId())).thenReturn(Optional.of(accommodation));
        when(likeRepository.findByMemberAndAccommodation(member, accommodation)).thenReturn(Optional.of(like));
        when(likeRepository.countByAccommodation(accommodation)).thenReturn(0);

        // when
        LikeResponse response = likeService.clickLike(memberId, request);

        // then
        verify(likeRepository, times(1)).delete(any(Like.class));
        assertNotNull(response);
        assertEquals(false, response.getLiked());
        assertEquals(0, response.getLikeCount());
    }

    @Test
    @DisplayName("좋아요 클릭 실패 - 사용자 없음")
    void testClickLike_memberNotFound() {
        // given
        Long memberId = 1L;
        LikeRequest request = new LikeRequest(1L);

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        // when
        MemberException exception = assertThrows(MemberException.class,
            () -> likeService.clickLike(memberId, request));

        // then
        assertEquals(ErrorType.INVALID_EMAIL_AND_PASSWORD.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("좋아요 클릭 실패 - 숙소 없음")
    void testClickLike_accommodationNotFound() {
        // given
        Long memberId = 1L;
        LikeRequest request = new LikeRequest(1L);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(accommodationRepository.findById(request.getAccommodationId())).thenReturn(Optional.empty());

        // when
        AccommodationException exception = assertThrows(AccommodationException.class,
            () -> likeService.clickLike(memberId, request));

        // then
        assertEquals(ErrorType.NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("좋아요 한 숙소 조회 성공")
    void testGetLikedAccommodations_success() {
        // given
        Long memberId = 1L;

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(likeRepository.findByMember(member)).thenReturn(List.of(like));

        // when
        List<AccommodationResponse> responseList = likeService.getLikedAccommodations(memberId);

        // then
        assertNotNull(responseList);
        assertFalse(responseList.isEmpty());
        assertEquals(1, responseList.size());
    }

    @Test
    @DisplayName("좋아요 한 숙소 조회 실패 - 사용자 없음")
    void testGetLikedAccommodations_memberNotFound() {
        // given
        Long memberId = 1L;

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        // when
        MemberException exception = assertThrows(MemberException.class,
            () -> likeService.getLikedAccommodations(memberId));

        // then
        assertEquals(ErrorType.INVALID_EMAIL_AND_PASSWORD.getMessage(), exception.getMessage());
    }
}