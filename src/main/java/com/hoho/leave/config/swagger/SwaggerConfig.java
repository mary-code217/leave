package com.hoho.leave.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger/OpenAPI 문서화 설정 클래스.
 * <p>
 * API 문서의 기본 정보와 서버 설정을 정의한다.
 * </p>
 */
@Configuration
public class SwaggerConfig {

    /**
     * OpenAPI 문서 설정을 빈으로 등록한다.
     * <p>
     * API 제목, 설명, 버전 및 서버 정보를 설정한다.
     * </p>
     *
     * @return OpenAPI 설정 객체
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("휴가 전자결재 시스템 API")
                        .description("API documentation for HoHo Leave Management System")
                        .version("1.0.0"))
                .servers(List.of(new Server()
                        .url("http://localhost:8080")
                        .description("개발용 서버")
                ));
    }
}
