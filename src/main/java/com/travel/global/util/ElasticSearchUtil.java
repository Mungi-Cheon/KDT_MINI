//package com.travel.global.util;
//
//
//import co.elastic.clients.elasticsearch.ElasticsearchClient;
//import co.elastic.clients.elasticsearch.core.IndexRequest;
//import co.elastic.clients.elasticsearch.core.IndexResponse;
//import com.travel.domain.accommodation.entity.Accommodation;
//import com.travel.domain.accommodation.entity.AccommodationSearch;
//import java.io.IOException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ElasticSearchUtil {
//
//    @Autowired
//    private ElasticsearchClient elasticsearchClient;
//
//    public void indexAccommodation(Accommodation accommodation) {
//        AccommodationSearch accommodationSearch = new AccommodationSearch();
//        accommodationSearch.setId(accommodation.getId());
//        accommodationSearch.setName(accommodation.getName());
//
//        try {
//            IndexRequest<AccommodationSearch> request = IndexRequest.of(i -> i
//                .index("accommodation")
//                .id(String.valueOf(accommodationSearch.getId()))
//                .document(accommodationSearch));
//            IndexResponse response = elasticsearchClient.index(request);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void bulkIndexAccommodation(Iterable<Accommodation> accommodations) {
//        accommodations.forEach(this::indexAccommodation);
//    }
//}
//
