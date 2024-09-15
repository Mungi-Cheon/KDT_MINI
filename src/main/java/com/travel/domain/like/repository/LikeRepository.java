package com.travel.domain.like.repository;

import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.like.entity.Like;
import com.travel.domain.member.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByMember(Member member);
    Optional<Like> findByMemberAndAccommodation(Member member, Accommodation accommodation);
    int countByAccommodation(Accommodation accommodation);
}
