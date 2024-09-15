package com.travel.domain.cart.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.accommodation.repository.AccommodationRepository;
import com.travel.domain.cart.entity.Cart;
import com.travel.domain.member.entity.Member;
import com.travel.domain.member.repository.MemberRepository;
import com.travel.domain.product.entity.Product;
import com.travel.domain.product.repository.ProductRepository;
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
class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    private Member member;
    private Accommodation accommodation;
    private Product product;
    private Cart cart;
    @Autowired
    private AccommodationRepository accommodationRepository;

    @BeforeEach
    void setUp() {
        member = Member.builder()
            .name("Test User")
            .email("test@example.com")
            .password("password")
            .build();

        accommodation = Accommodation.builder()
            .name("Test Hotel")
            .build();

        product = Product.builder()
            .name("Deluxe Room")
            .build();

        memberRepository.save(member);
        accommodationRepository.save(accommodation);
        productRepository.save(product);

        cart = Cart.builder()
            .member(member)
            .accommodation(accommodation)
            .product(product)
            .build();

        cartRepository.save(cart);
    }

    @Test
    @DisplayName("회원의 장바구니 항목 찾기")
    void findByMemberIdWithFetchJoin_ShouldReturnCart() {
        // When
        List<Cart> result = cartRepository.findByMemberIdWithFetchJoin(member.getId());

        // Then
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Test Hotel", result.get(0).getAccommodation().getName());
        assertEquals("Deluxe Room", result.get(0).getProduct().getName());
    }

    @Test
    @DisplayName("회원과 상품으로 장바구니 항목 찾기")
    void findByMemberIdAndProductId_ShouldReturnCart() {
        // When
        Optional<Cart> result = cartRepository.findByMemberIdAndProductId(member.getId(), product.getId());

        // Then
        assertTrue(result.isPresent());
        assertEquals(member.getId(), result.get().getMember().getId());
        assertEquals(product.getId(), result.get().getProduct().getId());
    }
}
