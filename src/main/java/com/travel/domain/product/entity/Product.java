package com.travel.domain.product.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.travel.domain.accommodation.entity.Accommodation;
import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String checkInTime;

    private String checkOutTime;

    private int standardNumber;

    private int maximumNumber;

    private String type;

    @OneToOne(mappedBy = "product", fetch = FetchType.LAZY)
    @JsonManagedReference
    private ProductOption productOption;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @JsonManagedReference
    @BatchSize(size = 100)
    private List<ProductInfoPerNight> productInfoPerNightsList;

    @OneToOne(mappedBy = "product", fetch = FetchType.LAZY)
    @JsonManagedReference
    private ProductImage productImage;
}