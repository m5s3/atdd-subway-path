package subway.Line;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SuppressWarnings("NonAsciiCharacters")
@Sql(scripts = {"/truncate.sql"}, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class PathAcceptanceTest {

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 신촌역;
    private Long 이대역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

    private static String PATH = "/paths?source=%d&target=%d";

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");
        신촌역 = 지하철역_생성_요청("신촌역").jsonPath().getLong("id");
        이대역 = 지하철역_생성_요청("이대역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청(노선_생성("2호선", "green", 교대역, 강남역, 10)).jsonPath().getLong("id");
        신분당선 = 지하철_노선_생성_요청(노선_생성("신분당선", "red", 강남역, 양재역, 10)).jsonPath().getLong("id");
        삼호선 = 지하철_노선_생성_요청(노선_생성("3호선", "orange", 교대역, 남부터미널역, 2)).jsonPath().getLong("id");

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionParam(남부터미널역, 양재역, 3));
        지하철_노선에_지하철_구간_생성_요청(이호선, createSectionParam(신촌역, 이대역, 5));
    }

    @Test
    void 출발역과_도차역이_같은_경우_예외발생() {
        String url = String.format(PATH, 교대역, 교대역);
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .get(url)
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 출발역과_도착역이_연결이_되어_있지_않는_경우_예외발생() {
        String url = String.format(PATH, 교대역, 신촌역);
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .get(url)
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 존재하지_않는_출발역_조회_예외발생() {
        Long 존재하지_않는_출발역 = 9999L;
        String url = String.format(PATH, 존재하지_않는_출발역, 신촌역);
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .get(url)
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 존재하지_않는_도착역_조회_예외발생() {
        Long 존재하지_않는_도착역 = 9999L;
        String url = String.format(PATH, 신촌역, 존재하지_않는_도착역);
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .get(url)
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 지하철_노선에_지하철_구간_생성_요청(Long lineId, Map<String, Object> params) {
        return RestAssured.given().log().all().when()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .post("/lines/" + lineId + "/sections")
                .then().log().all().extract();
    }

    private HashMap<String, Object> createSectionParam(Long upStationId, Long downStationId, int distance) {
        var sectionParam = new HashMap<String, Object>();
        sectionParam.put("upStationId", upStationId);
        sectionParam.put("downStationId", downStationId);
        sectionParam.put("distance", distance);
        return sectionParam;
    }

    private static Map<String, Object> 노선_생성(String name, String color, Long upStationId, Long downStationId,
            int distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        return params;
    }

    private static Map<String, String> createStationParams(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return params;
    }

    private static ExtractableResponse<Response> 지하철역_생성_요청(String stationName) {
        return RestAssured.given().log().all()
                .body(createStationParams(stationName))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, Object> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }
}
