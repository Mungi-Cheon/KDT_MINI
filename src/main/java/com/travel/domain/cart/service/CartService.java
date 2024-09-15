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
import com.travel.domain.product.entity.ProductInfoPerNight;
import com.travel.domain.product.repository.ProductInfoPerNightRepository;
import com.travel.domain.product.repository.ProductRepository;
import com.travel.global.exception.AccommodationException;
import com.travel.global.exception.CartException;
import com.travel.global.exception.MemberException;
import com.travel.global.exception.ProductException;
import com.travel.global.exception.ReservationsException;
import com.travel.global.exception.type.ErrorType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    private final MemberRepository memberRepository;

    private final AccommodationRepository accommodationRepository;

    private final ProductRepository productRepository;

    private final ProductInfoPerNightRepository productInfoPerNightRepository;

    @Transactional
    public CartResponse addToCart(Long memberId, CartRequest cartRequest) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberException(ErrorType.INVALID_EMAIL_AND_PASSWORD));
        Accommodation accommodation = accommodationRepository.
            findByIdJoinImagesAndOptions(cartRequest.getAccommodationId())
            .orElseThrow(() -> new AccommodationException(ErrorType.NOT_FOUND));
        Product product = productRepository.findByAccommodationIdAndProductIdJoinImagesAndOption(
                cartRequest.getAccommodationId(),cartRequest.getProductId())
            .orElseThrow(() -> new ProductException(ErrorType.NOT_FOUND));

        List<ProductInfoPerNight> productInfoList = findProductInfoPerNightList(
            cartRequest.getProductId(),
            cartRequest.getCheckInDate(), cartRequest.getCheckOutDate());
        AvailableToCart(productInfoList);

        Optional<Cart> existingCart = cartRepository.findByMemberIdAndProductId(memberId,
            cartRequest.getProductId());
        if (existingCart.isPresent()) {
            throw new CartException(ErrorType.ALREADY_IN_CART);
        }

        Cart cart = Cart.builder()
            .member(member)
            .accommodation(accommodation)
            .product(product)
            .checkInDate(cartRequest.getCheckInDate())
            .checkOutDate(cartRequest.getCheckOutDate())
            .personNumber(cartRequest.getPersonNumber())
            .build();

        cartRepository.save(cart);

        return CartResponse.from(cart);
    }

    @Transactional(readOnly = true)
    public List<CartResponse> getCartByMemberId(Long memberId) {
        List<Cart> carts = cartRepository.findByMemberIdWithFetchJoin(memberId);

        return carts.stream()
            .map(CartResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public void removeFromCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new CartException(ErrorType.NOT_FOUND));

        cartRepository.delete(cart);
    }

    private List<ProductInfoPerNight> findProductInfoPerNightList(long productId,
        LocalDate checkInDate, LocalDate checkOutDate) {
        return productInfoPerNightRepository
            .findByProductIdAndDateRange(productId, checkInDate, checkOutDate);
    }

    private void AvailableToCart(
        List<ProductInfoPerNight> productInfoPerNightsList) {
        for (ProductInfoPerNight pi : productInfoPerNightsList) {
            if (pi.getCount() <= 0) {
                throw new ReservationsException(ErrorType.INCLUDES_FULLY_BOOKED_PRODUCT);
            }
        }
    }
}
