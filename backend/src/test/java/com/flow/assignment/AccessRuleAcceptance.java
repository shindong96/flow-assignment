package com.flow.assignment;

import static org.hamcrest.Matchers.notNullValue;

import com.flow.assignment.support.DatabaseCleanUp;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.ValidatableResponse;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(properties = "spring.session.store-type=none", webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccessRuleAcceptance {

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        databaseCleanUp.execute();
    }

    @DisplayName("IP 접근 제한 규칙 저징을 요창히먄 201을 응답한다.")
    @Test
    void create() {
        // given
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("ipAddress", "11.111.111.111");
        requestBody.put("startTime", "2024/12/24 15:23");
        requestBody.put("endTime", "2024/12/24 15:25");

        // when
        ValidatableResponse response = post("/access-rules", requestBody);

        // then
        response.statusCode(HttpStatus.CREATED.value())
                .header("Location", notNullValue());
    }

    private ValidatableResponse post(final String uri, final Object requestBody) {
        return RestAssured.given().log().all()
                .header(new Header("Time-Zone", "Asia/Seoul"))
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post(uri)
                .then().log().all();
    }
}
