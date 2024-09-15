package com.travel.domain.accommodation.repository;

import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.global.exception.AccommodationException;
import com.travel.global.exception.type.ErrorType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {

    @Query(value = "SELECT a FROM Accommodation a " +
        "LEFT JOIN FETCH a.images " +
        "LEFT JOIN FETCH a.options " +
        "LEFT JOIN FETCH a.products p " +
        "LEFT JOIN FETCH p.productImage " +
        "LEFT JOIN FETCH p.productOption " +
        "WHERE (:lastAccommodationId IS NULL OR a.id > :lastAccommodationId) " +
        "ORDER BY a.id ASC")
    List<Accommodation> findAccommodations(
        @Param("lastAccommodationId") Long lastAccommodationId);

    @Query(value = "SELECT a FROM Accommodation a " +
        "LEFT JOIN FETCH a.images " +
        "LEFT JOIN FETCH a.options " +
        "LEFT JOIN FETCH a.products p " +
        "LEFT JOIN FETCH p.productImage " +
        "LEFT JOIN FETCH p.productOption " +
        "WHERE (a.category = :category) " +
        "AND (:lastAccommodationId IS NULL OR a.id > :lastAccommodationId) " +
        "ORDER BY a.id ASC")
    List<Accommodation> findAccommodationsByCategory(
        @Param("category") String category,
        @Param("lastAccommodationId") Long lastAccommodationId);

    @Query("SELECT a FROM Accommodation a " +
        "LEFT JOIN FETCH a.images " +
        "LEFT JOIN FETCH a.options " +
        "WHERE a.id = :id")
    Optional<Accommodation> findByIdJoinImagesAndOptions(@Param("id") Long id);

    default Accommodation getByIdJoinImagesAndOptions(Long id) {
        return findByIdJoinImagesAndOptions(id)
            .orElseThrow(() -> new AccommodationException(ErrorType.NOT_FOUND));
    }

    @Query("SELECT a FROM Accommodation a"
        + " LEFT JOIN FETCH a.products"
        + " WHERE a.id = :accommodationId")
    default Accommodation findAccommodationById(Long accommodationId) {
        return findById(accommodationId)
            .orElseThrow(() -> new AccommodationException(ErrorType.NOT_FOUND));
    }
}