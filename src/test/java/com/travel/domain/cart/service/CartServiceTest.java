package com.travel.domain.cart.service;

import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.accommodation.repository.AccommodationRepository;
import com.travel.domain.cart.dto.request.CartRequest;
import com.travel.domain.cart.dto.response.CartResponse;
import com.travel.domain.cart.entity.Cart;
import com.travel.domain.cart.repository.CartRepository;
import com.travel.domain.member.entity.Member;
import com.travel.domain.member.repository.MemberRepository;
import com.travel.domain.product.entity.Product;
import com.travel.domain.product.entity.ProductImage;
import com.travel.domain.product.entity.ProductInfoPerNight;
import com.travel.domain.product.repository.ProductInfoPerNightRepository;
import com.travel.domain.product.repository.ProductRepository;
import com.travel.global.exception.AccommodationException;
import com.travel.global.exception.CartException;
import com.travel.global.exception.MemberException;
import com.travel.global.exception.ProductException;
import com.travel.global.exception.ReservationsException;
import com.travel.global.exception.type.ErrorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private AccommodationRepository accommodationRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductInfoPerNightRepository productInfoPerNightRepository;

    @InjectMocks
    private CartService cartService;

    private Member member;

    private Accommodation accommodation;

    private Product productValid;

    private Product productInValid;

    private ProductInfoPerNight productInfoPerNightValid;

    private ProductInfoPerNight productInfoPerNightInValid;

    private ProductImage productImage;

    private Cart cart;

    private CartRequest cartRequest;

    @BeforeEach
    void setUp() {
        member = Member.builder()
            .id(1L).
            name("Test User").
            build();

        accommodation = Accommodation.builder()
            .id(1L)
            .name("Test Hotel")
            .build();

        productInfoPerNightValid = ProductInfoPerNight.builder()
            .id(1L)
            .count(1)
            .price(100000)
            .build();

        productInfoPerNightInValid = ProductInfoPerNight.builder()
            .id(2L)
            .count(0)
            .build();

        productImage = ProductImage.builder()
            .id(1L)
            .imageUrl1("url")
            .build();

        productValid = Product.builder()
            .id(1L)
            .name("Deluxe Room")
            .productImage(productImage)
            .productInfoPerNightsList(List.of(productInfoPerNightValid))
            .build();

        productInValid = Product.builder()
            .id(2L)
            .name("Deluxe Room")
            .productInfoPerNightsList(List.of(productInfoPerNightInValid))
            .build();

        cart = Cart.builder()
            .id(1L)
            .member(member)
            .accommodation(accommodation)
            .product(productValid)
            .checkInDate(LocalDate.of(2024, 7, 1))
            .checkOutDate(LocalDate.of(2024, 7, 5))
            .personNumber(2)
            .build();

        cartRequest = new CartRequest(
            1L,
            1L,
            LocalDate.of(2024, 7, 1),
            LocalDate.of(2024, 7, 5),
            2);
    }

    @Test
    @DisplayName("장바구니 추가 성공")
    void addToCart_success() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(accommodationRepository.findByIdJoinImagesAndOptions(anyLong())).thenReturn(
            Optional.of(accommodation));
        when(productRepository.findByAccommodationIdAndProductIdJoinImagesAndOption(
                anyLong(), anyLong())).thenReturn(
            Optional.of(productValid));
        when(productInfoPerNightRepository.findByProductIdAndDateRange(anyLong(),
            any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(List.of(productInfoPerNightValid));
        when(cartRepository.findByMemberIdAndProductId(anyLong(), anyLong())).thenReturn(
            Optional.empty());

        CartResponse response = cartService.addToCart(1L, cartRequest);

        verify(cartRepository, times(1)).save(any(Cart.class));
        assertNotNull(response);
        assertEquals("Test Hotel", response.getAccommodationName());
        assertEquals("Deluxe Room", response.getProductName());
    }

    @Test
    @DisplayName("장바구니 추가 실패 - 이미 존재하는 상품")
    void addToCart_alreadyInCart() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(accommodationRepository.findByIdJoinImagesAndOptions(anyLong())).thenReturn(Optional.of(accommodation));
        when(productRepository.findByAccommodationIdAndProductIdJoinImagesAndOption(
                anyLong(), anyLong())).thenReturn(Optional.of(productValid));
        when(productInfoPerNightRepository.findByProductIdAndDateRange(anyLong(), any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(List.of(productInfoPerNightValid));
        when(cartRepository.findByMemberIdAndProductId(anyLong(), anyLong())).thenReturn(Optional.of(cart));

        CartException exception = assertThrows(CartException.class, () -> cartService.addToCart(1L, cartRequest));

        assertEquals(ErrorType.ALREADY_IN_CART.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("장바구니 추가 실패 - 회원 없음")
    void addToCart_memberNotFound() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

        MemberException exception = assertThrows(MemberException.class,
            () -> cartService.addToCart(1L, cartRequest));

        assertEquals(ErrorType.INVALID_EMAIL_AND_PASSWORD.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("장바구니 추가 실패 - 숙소 없음")
    void addToCart_accommodationNotFound() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(accommodationRepository.findByIdJoinImagesAndOptions(anyLong())).thenReturn(
            Optional.empty());

        AccommodationException exception = assertThrows(AccommodationException.class,
            () -> cartService.addToCart(1L, cartRequest));

        assertEquals(ErrorType.NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("장바구니 추가 실패 - 상품 없음")
    void addToCart_productNotFound() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(accommodationRepository.findByIdJoinImagesAndOptions(anyLong())).thenReturn(
            Optional.of(accommodation));
        when(productRepository.findByAccommodationIdAndProductIdJoinImagesAndOption(
                anyLong(), anyLong())).thenReturn(Optional.empty());

        ProductException exception = assertThrows(ProductException.class,
            () -> cartService.addToCart(1L, cartRequest));

        assertEquals(ErrorType.NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("장바구니 추가 실패 - 예약 가능하지 않음")
    void addToCart_unavailableProduct() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(accommodationRepository.findByIdJoinImagesAndOptions(anyLong())).thenReturn(
            Optional.of(accommodation));
        when(productRepository.findByAccommodationIdAndProductIdJoinImagesAndOption(
                anyLong(),anyLong())).thenReturn(
            Optional.of(productInValid));
        when(productInfoPerNightRepository.findByProductIdAndDateRange(anyLong(),
            any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(List.of(productInfoPerNightInValid));

        ReservationsException exception = assertThrows(ReservationsException.class,
            () -> cartService.addToCart(1L, cartRequest));

        assertEquals(ErrorType.INCLUDES_FULLY_BOOKED_PRODUCT.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("장바구니 조회 성공")
    void getCartByMemberId_success() {
        when(cartRepository.findByMemberIdWithFetchJoin(anyLong())).thenReturn(List.of(cart));

        List<CartResponse> response = cartService.getCartByMemberId(1L);

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
        assertEquals("Test Hotel", response.get(0).getAccommodationName());
    }

    @Test
    @DisplayName("장바구니 삭제 성공")
    void removeFromCart_success() {
        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));

        cartService.removeFromCart(1L);

        verify(cartRepository, times(1)).delete(any(Cart.class));
    }

    @Test
    @DisplayName("장바구니 삭제 실패 - 카트 항목 없음")
    void removeFromCart_notFound() {
        when(cartRepository.findById(anyLong())).thenReturn(Optional.empty());

        CartException exception = assertThrows(CartException.class,
            () -> cartService.removeFromCart(1L));

        assertEquals(ErrorType.NOT_FOUND.getMessage(), exception.getMessage());
    }
}
