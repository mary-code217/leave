package com.hoho.leave.domain.leave.holiday.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoho.leave.domain.leave.holiday.dto.HolidayImportDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HolidayApiService {

    private static final DateTimeFormatter LOCDATE_FMT = DateTimeFormatter.BASIC_ISO_DATE;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${holiday.api.key}")
    private String apiKey;
    @Value("${holiday.api.base-url}")
    private String apiUrl;
    private RestClient client;

    @PostConstruct
    public void init() {
        this.client = RestClient.builder()
                .baseUrl(apiUrl)
                .build();
    }

    public List<HolidayImportDto> fetchRestHolidaysByMonth(int year, int month) {
        String monthStr = String.format("%02d", month);

        URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                .path("/getRestDeInfo")
                .queryParam("serviceKey", apiKey)
                .queryParam("_type", "json")
                .queryParam("solYear", year)
                .queryParam("solMonth", String.format("%02d", month))
                .queryParam("numOfRows", 100)
                .queryParam("pageNo", 1)
                .build(true)
                .toUri();

        ResponseEntity<String> res = client.get()
                .uri(uri)
                .retrieve()
                .toEntity(String.class);

        if (!res.getStatusCode().is2xxSuccessful() || res.getBody() == null) return Collections.emptyList();


        try {
            JsonNode root = objectMapper.readTree(res.getBody());
            JsonNode items = root.path("response").path("body").path("items").path("item");
            if (items.isMissingNode() || items.isNull()) return Collections.emptyList();


            List<HolidayImportDto> result = new ArrayList<>();
            if (items.isArray()) {
                for (JsonNode n : items) {
                    mapIfHoliday(n, result);
                }
            } else {
                mapIfHoliday(items, result);
            }
            return result;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private void mapIfHoliday(JsonNode n, List<HolidayImportDto> acc) {
        String isHoliday = n.path("isHoliday").asText("");
        String dateKind = n.path("dateKind").asText("");
        if (!"Y".equalsIgnoreCase(isHoliday) || !"01".equals(dateKind)) return;

        String name = n.path("dateName").asText("");
        int loc = n.path("locdate").asInt(0);
        if (loc <= 0 || name.isBlank()) return;

        LocalDate date = LocalDate.parse(String.valueOf(loc), LOCDATE_FMT);
        acc.add(new HolidayImportDto(date, name));
    }
}
