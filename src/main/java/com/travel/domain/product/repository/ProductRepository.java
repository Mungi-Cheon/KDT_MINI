package com.travel.domain.product.repository;

import com.travel.domain.product.entity.Product;
import com.travel.global.exception.ProductException;
import com.travel.global.exception.type.ErrorType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByAccommodationId(Long accommodationId);

    @Query("SELECT p FROM Product p "
            + "LEFT JOIN FETCH p.productOption "
            + "LEFT JOIN FETCH p.productImage "
            + "WHERE p.id = :productId "
            + "AND p.accommodation.id = :accommodationId")
    Optional<Product> findByAccommodationIdAndProductIdJoinImagesAndOption(
            @Param("accommodationId") Long accommodationId, @Param("productId") Long productId);

    Optional<Product> findByIdAndAccommodationId(Long productId, Long accommodationId);

    @Query("SELECT p FROM Product p " +
            "LEFT JOIN FETCH p.productOption " +
            "LEFT JOIN FETCH p.productImage " +
            "WHERE p.accommodation.id = :accommodationId")
    List<Product> findAllByAccommodationIdWithFetchJoin
            (@Param("accommodationId") Long accommodationId);

    default Product getByAccommodationIdAndProductIdJoinImagesAndOption(
            Long accommodationId,
            Long productId) { //단일 책임의 원칙, repo 에서 생길 수 있는 에러는 repo 서 처리 (단순 단일 객체)
        return findByAccommodationIdAndProductIdJoinImagesAndOption(accommodationId, productId)
                .orElseThrow(() -> new ProductException(ErrorType.NOT_FOUND));
    }
}