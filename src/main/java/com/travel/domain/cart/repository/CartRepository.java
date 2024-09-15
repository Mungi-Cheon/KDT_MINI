package com.travel.domain.cart.repository;

import com.travel.domain.cart.entity.Cart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c "
        + "LEFT JOIN FETCH c.member m "
        + "LEFT JOIN FETCH c.accommodation a "
        + "LEFT JOIN FETCH a.images "
        + "LEFT JOIN FETCH a.options "
        + "LEFT JOIN FETCH c.product p "
        + "LEFT JOIN FETCH p.productImage "
        + "LEFT JOIN FETCH p.productOption "
        + "WHERE m.id = :memberId")
    List<Cart> findByMemberIdWithFetchJoin(@Param("memberId") Long memberId);

    @Query("SELECT c FROM Cart c "
        + "LEFT JOIN FETCH c.member m "
        + "LEFT JOIN FETCH c.accommodation a "
        + "LEFT JOIN FETCH a.images "
        + "LEFT JOIN FETCH a.options "
        + "LEFT JOIN FETCH c.product p "
        + "LEFT JOIN FETCH p.productImage "
        + "LEFT JOIN FETCH p.productOption "
        + "WHERE m.id = :memberId AND p.id = :productId")
    Optional<Cart> findByMemberIdAndProductId(@Param("memberId") Long memberId, @Param("productId") Long productId);
}
