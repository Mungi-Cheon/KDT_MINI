package com.travel.domain.map.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.travel.domain.map.dto.response.DocumentResponse;
import com.travel.domain.map.dto.response.MapResponse;
import com.travel.global.exception.MapException;
import com.travel.global.exception.type.ErrorType;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
@AutoConfigureMockMvc
class KakaoMapServiceTest {

    @Autowired
    private KakaoMapService kakaoMapService;

    @MockBean
    private RestTemplate restTemplate;

    String address = "제주특별자치도 제주시 탑동로 66";

    @BeforeAll
    static void setUp() {
    }

    private DocumentResponse createDocumentResponse() {
        return DocumentResponse.builder()
            .latitude(33.5188061398631)
            .longitude(126.518138492511)
            .build();
    }

    private MapResponse createMapResponse(List<DocumentResponse> documentResponseList) {
        return MapResponse.builder()
            .documents(documentResponseList)
            .build();
    }

    @Test
    @DisplayName("주소로 위도, 경도 반환")
    void getAddress() {
        DocumentResponse documentResponse = createDocumentResponse();
        List<DocumentResponse> documentResponseList = List.of(documentResponse);
        MapResponse mapResponse = createMapResponse(documentResponseList);

        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.GET), any(HttpEntity.class),
            eq(MapResponse.class)
        )).thenReturn(ResponseEntity.ok(mapResponse));

        MapResponse actualMapResponse = kakaoMapService.getAddress(address);

        assertEquals(mapResponse, actualMapResponse);
        assertEquals(126.518138492511, actualMapResponse.getDocuments().get(0).getLongitude());
    }

    @Test
    @DisplayName("주소가 없을 때")
    void getAddress_ADDRESS_IS_EMPTY(){
        String emptyAddress = "";

        MapException exception = assertThrows(MapException.class, () -> {
            kakaoMapService.getAddress(emptyAddress);
        });
        assertEquals(ErrorType.ADDRESS_IS_EMPTY.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("주소에 대해 uri 생성")
    void builderUriByAddress() {
        URI expextedUri
            = URI.create("https://dapi.kakao.com/v2/local/search/address.json?query=%EC%A0%9C%EC%A3%BC%ED%8A%B9%EB%B3%84%EC%9E%90%EC%B9%98%EB%8F%84%20%EC%A0%9C%EC%A3%BC%EC%8B%9C%20%ED%83%91%EB%8F%99%EB%A1%9C%2066");

        URI actualUri = kakaoMapService.builderUriByAddress(address);

        assertEquals(expextedUri, actualUri);
    }
}