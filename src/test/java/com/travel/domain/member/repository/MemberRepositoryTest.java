package com.travel.domain.member.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.travel.domain.member.entity.Member;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
            .email("test@example.com")
            .name("nickname")
            .password("password123")
            .build();
        memberRepository.save(member);
    }

    @Test
    @DisplayName("이메일로 사용자 존재 여부 확인")
    void existsByEmail() {
        boolean exists = memberRepository.existsByEmail("test@example.com");
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("이메일로 사용자 찾기")
    void findByEmail() {
        Optional<Member> foundMember = memberRepository.findByEmail("test@example.com");
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getEmail()).isEqualTo(member.getEmail());
    }
}
