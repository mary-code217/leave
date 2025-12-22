package com.hoho.leave.domain.leave.holiday.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoho.leave.domain.leave.holiday.service.shared.HolidayImport;
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

/**
 * 공휴일 API 서비스.
 * <p>
 * 공공데이터 포털의 공휴일 API를 호출하여 공휴일 정보를 가져온다.
 * </p>
 */
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

    /**
     * RestClient를 초기화한다.
     */
    @PostConstruct
    public void init() {
        this.client = RestClient.builder()
                .baseUrl(apiUrl)
                .build();
    }

    /**
     * 특정 년월의 공휴일 정보를 API로부터 가져온다.
     *
     * @param year 조회 년도
     * @param month 조회 월
     * @return 공휴일 목록
     */
    public List<HolidayImport> fetchRestHolidaysByMonth(int year, int month) {
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


            List<HolidayImport> result = new ArrayList<>();
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

    private void mapIfHoliday(JsonNode n, List<HolidayImport> acc) {
        String isHoliday = n.path("isHoliday").asText("");
        String dateKind = n.path("dateKind").asText("");
        if (!"Y".equalsIgnoreCase(isHoliday) || !"01".equals(dateKind)) return;

        String name = n.path("dateName").asText("");
        int loc = n.path("locdate").asInt(0);
        if (loc <= 0 || name.isBlank()) return;

        LocalDate date = LocalDate.parse(String.valueOf(loc), LOCDATE_FMT);
        acc.add(new HolidayImport(date, name));
    }
}
