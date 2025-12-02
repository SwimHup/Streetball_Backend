package com.example.streetball_backend;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI streetballOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("로컬 개발 서버");

        Contact contact = new Contact();
        contact.setName("Street Basketball Team");
        contact.setEmail("support@streetball.com");

        Info info = new Info()
                .title("Street Basketball API")
                .version("1.0.0")
                .description("길거리 농구 매칭 및 관전 시스템 API 문서입니다.\n\n" +
                        "## 주요 기능\n" +
                        "- 👤 사용자 관리 (위치 정보 포함)\n" +
                        "- 🏀 게임 생성 및 관리\n" +
                        "- 📍 근처 게임 검색 (Haversine 공식 사용)\n" +
                        "- 👥 참여자 자동 등록\n\n" +
                        "## 핵심 기능\n" +
                        "1. **사용자 위치 업데이트**: 실시간 위치 기반 서비스\n" +
                        "2. **게임 생성**: 생성자 자동 참여\n" +
                        "3. **근처 게임 검색**: 반경 기반 검색")
                .contact(contact);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer));
    }
}

