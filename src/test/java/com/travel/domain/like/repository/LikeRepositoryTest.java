package com.travel.domain.like.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.accommodation.repository.AccommodationRepository;
import com.travel.domain.like.entity.Like;
import com.travel.domain.member.entity.Member;
import com.travel.domain.member.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LikeRepositoryTest {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AccommodationRepository accommodationRepository;

    private Member member;
    private Accommodation accommodation, accommodation2;
    private Like like, like2;

    @BeforeEach
    void setUp() {
        member = Member.builder().build();

        accommodation = Accommodation.builder()
            .category("호텔")
            .build();

        accommodation2 = Accommodation.builder()
            .category("리조트")
            .build();

        memberRepository.save(member);
        accommodationRepository.save(accommodation);
        accommodationRepository.save(accommodation2);

        like = Like.builder()
            .member(member)
            .accommodation(accommodation)
            .build();

        like2 = Like.builder()
            .member(member)
            .accommodation(accommodation2)
            .build();

        likeRepository.save(like);
        likeRepository.save(like2);
    }

    @Test
    @DisplayName("회원이 좋아요한 숙소 찾기")
    void findByMember_ShouldReturnLikes() {
        // When
        List<Like> result = likeRepository.findByMember(member);

        // Then
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("회원과 숙소로 좋아요 찾기")
    void findByMemberAndAccommodation_ShouldReturnLike() {
        // When
        Optional<Like> result = likeRepository.findByMemberAndAccommodation(member, accommodation);

        // Then
        assertTrue(result.isPresent());
        assertEquals(member.getId(), result.get().getMember().getId());
        assertEquals(accommodation.getId(), result.get().getAccommodation().getId());
    }

    @Test
    @DisplayName("숙소의 좋아요 개수 세기")
    void countByAccommodation_ShouldReturnCount() {
        // When
        int count = likeRepository.countByAccommodation(accommodation);

        // Then
        assertEquals(1, count);
    }
}
