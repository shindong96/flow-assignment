package com.flow.assignment;

import static org.hamcrest.Matchers.equalTo;
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
public class AccessRuleAcceptanceTest {

    private static final String URI = "/access-rules";
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
        requestBody.put("content", "IP 주소입니다.");

        // when
        ValidatableResponse response = post(URI, requestBody);

        // then
        response.statusCode(HttpStatus.CREATED.value())
                .header("Location", notNullValue());
    }

    @DisplayName("page와 size를 이용해 규칙 정보 전체 조회를 하고 200을 반환한다.")
    @Test
    void findAll() {
        // given
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("ipAddress", "11.111.111.111");
        requestBody.put("startTime", "2024/12/24 15:23");
        requestBody.put("endTime", "2024/12/24 15:25");
        requestBody.put("content", "IP 주소입니다.");
        post(URI, requestBody);

        // when
        ValidatableResponse response = get(URI + "?page=1&size=2", new Header("Time-Zone", "America/New_York"));

        // then
        response.statusCode(200)
                .body("hasNext", equalTo(false))
                .body("accessRules.size()", equalTo(1));
    }

    @DisplayName("접근 제한 규칙의 id를 통해 정보를 삭제하고 204를 반환한다.")
    @Test
    void delete() {
        // given
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("ipAddress", "11.111.111.111");
        requestBody.put("startTime", "2024/12/24 15:23");
        requestBody.put("endTime", "2024/12/24 15:25");
        requestBody.put("content", "IP 주소입니다.");
        final String accessRuleId = post(URI, requestBody)
                .extract()
                .header("Location")
                .split(URI + "/")[1];

        // when
        ValidatableResponse response = delete(URI + "/" + accessRuleId);

        // then
        response.statusCode(204);
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

    ValidatableResponse get(final String uri, final Header timeZone) {
        return RestAssured.given().log().all()
                .header(timeZone)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(uri)
                .then().log().all();
    }

    private ValidatableResponse delete(final String uri) {
        return RestAssured.given().log().all()
                .when().delete(uri)
                .then().log().all();
    }
}
