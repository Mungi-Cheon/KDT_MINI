package com.travel.domain.accommodation.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.travel.domain.accommodation.dto.response.AccommodationResponse;
import com.travel.domain.accommodation.service.AccommodationService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class AccommodationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccommodationService accommodationService;

    private AccommodationResponse accommodationResponse;

    @Test
    @WithMockUser
    @DisplayName("숙소 조회 api 테스트")
    void getAvailableAccommodations_Success() throws Exception {
        accommodationResponse = AccommodationResponse.builder()
            .id(1L)
            .name("Test Hotel")
            .category("호텔")
            .build();

        List<AccommodationResponse> mockResponse = List.of(accommodationResponse);

        Mockito.when(accommodationService.getAvailableAccommodations(
                anyString(), any(LocalDate.class),
                any(LocalDate.class), anyInt(), anyLong()))
            .thenReturn(mockResponse);

        mockMvc.perform(get("/api/accommodations")
                .param("categoryId", "1")
                .param("checkInDate", "2024-08-25")
                .param("checkOutDate", "2024-08-26")
                .param("personNumber", "2")
                .param("lastAccommodationId", "0")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].name", is("Test Hotel")))
            .andExpect(jsonPath("$[0].category", is("호텔")));
    }
}
