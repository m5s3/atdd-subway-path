package nextstep.subway.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.Line.application.LineService;
import subway.Line.domain.Line;
import subway.Line.infrastructure.LineRepository;
import subway.Station.domain.Station;
import subway.Station.infrastructure.StationRepository;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
public class LineServiceTest {
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private LineService lineService;

    private Station 신사역;
    private Station 강남역;
    private Line 신분당선;


    @BeforeEach
    void setUp() {
//        강남역 = new Station("강남역");
//        신논현역 = new Station("신논현역");
//        신분당선 = new Line("신분당선", "bg-red-600");
    }

    @Test
    void addSection() {
        // given
        // stationRepository와 lineRepository를 활용하여 초기값 셋팅

        // when
        // lineService.addSection 호출

        // then
        // line.getSections 메서드를 통해 검증
    }
}
