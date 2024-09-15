package com.travel.domain.member.repository;

import com.travel.domain.member.entity.Member;
import com.travel.global.exception.MemberException;
import com.travel.global.exception.type.ErrorType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    List<Member> findByDeletedAtIsNotNullAndDeletedAtBefore(LocalDateTime dateTime);

    @Query(value = "delete from Member where email = :email ")
    void deleteByEmail(String email);

    default Member getMember(Long memberId) {
        return findById(memberId).orElseThrow(
            () -> new MemberException(ErrorType.NOT_FOUND));
    }
}
