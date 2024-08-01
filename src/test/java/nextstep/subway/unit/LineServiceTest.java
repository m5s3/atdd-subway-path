package nextstep.subway.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.Line.application.LineService;
import subway.Line.domain.Line;
import subway.Line.domain.Section;
import subway.Line.infrastructure.LineRepository;
import subway.Line.presentation.dto.SectionRequest;
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

    private Line 신분당선;
    private Station 신사역;
    private Station 강남역;
    private Station 논현역;

    @BeforeEach
    void setUp() {
        신분당선 = new Line("신분당선", "bg-red-600");
        신사역 = new Station("신사역");
        강남역 = new Station("강남역");
        논현역 = new Station("논현역");
    }

    @Test
    @DisplayName("노선에 구간을 가운데 지점에 추가 할 수 있다.")
    void addSection_노선에_가운데_구간_추가() {
        // given
        // stationRepository와 lineRepository를 활용하여 초기값 셋팅
        stationRepository.save(강남역);
        stationRepository.save(신사역);
        lineRepository.save(신분당선);
        lineService.addSection(신분당선.getId(), new SectionRequest(신사역.getId(), 강남역.getId(), 10));

        // when
        // lineService.addSection 호출
        lineService.addSection(신분당선.getId(), new SectionRequest(강남역.getId(), 신사역.getId(), 7));

        // then
        // line.getSections 메서드를 통해 검증
    }
}
