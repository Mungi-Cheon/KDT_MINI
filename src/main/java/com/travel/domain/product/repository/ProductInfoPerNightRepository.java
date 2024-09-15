package com.travel.domain.product.repository;

import com.travel.domain.product.entity.ProductInfoPerNight;
import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductInfoPerNightRepository extends JpaRepository<ProductInfoPerNight, Long> {

    @Query("SELECT p FROM ProductInfoPerNight p " +
        "WHERE p.product.id = :productId " +
        "AND :checkInDate <= p.date " +
        "AND :checkOutDate > p.date ")
    List<ProductInfoPerNight> findByProductIdAndDateRange(
        @Param("productId") Long productId,
        @Param("checkInDate") LocalDate checkInDate,
        @Param("checkOutDate") LocalDate checkOutDate);

    @Query("SELECT CASE WHEN COUNT(pn) > 0 THEN TRUE ELSE FALSE END " +
        "FROM Product p " +
        "JOIN p.productInfoPerNightsList pn " +
        "WHERE p.id = :productId " +
        "AND pn.date = :date " +
        "AND pn.count > 0")
    boolean existsByProductIdAndDate(
        @Param("productId") Long productId,
        @Param("date") LocalDate date
    );

    @Query("SELECT MIN(p.count) FROM ProductInfoPerNight p " +
        "WHERE p.product.id = :productId " +
        "AND p.date BETWEEN :checkInDate AND :checkOutDate")
    Integer findMinCountByProductIdAndDateRange(
        @Param("productId") Long productId,
        @Param("checkInDate") LocalDate checkInDate,
        @Param("checkOutDate") LocalDate checkOutDate);

    @Query("SELECT p FROM ProductInfoPerNight p " +
        "WHERE p.product.accommodation.id = :accommodationId " +
        "AND p.date BETWEEN :startDate AND :endDate ")
    List<ProductInfoPerNight> findByAccommodationIdAndDateRange(
        @Param("accommodationId") Long accommodationId,
        @Param("startDate") LocalDate checkInDate,
        @Param("endDate") LocalDate checkOutDate);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM ProductInfoPerNight p " +
        "WHERE p.product.id = :productId " +
        "AND p.date BETWEEN :checkInDate AND :checkOutDate")
    List<ProductInfoPerNight> findByProductIdAndDateRangeWithPessimisticLock(
        @Param("productId") Long productId,
        @Param("checkInDate") LocalDate checkInDate,
        @Param("checkOutDate") LocalDate checkOutDate);
}