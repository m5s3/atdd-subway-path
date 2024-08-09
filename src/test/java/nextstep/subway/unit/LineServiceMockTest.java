package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.line.domain.Line;
import subway.line.domain.Sections;
import subway.line.infrastructure.LineRepository;
import subway.Station.application.StationService;
import subway.Station.domain.Station;
import subway.Station.infrastructure.StationRepository;

@ExtendWith(MockitoExtension.class)
public class LineServiceMockTest {
    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationService stationService;
    @Mock
    private StationRepository stationRepository;

    @Test
    void addSection() {
        // given
        // lineRepository, stationService stub 설정을 통해 초기값 셋팅
        Long 신사역_id = 1L;
        Long 강남역_id = 2L;
        Long 신분당선_id = 1L;

        Line 신분당선 = new Line(신분당선_id, "신분당선", "bg-red-600");
        Station 신사역 = new Station(신사역_id, "신사역");
        Station 강남역 = new Station(강남역_id, "강남역");

        Mockito.lenient().when(lineRepository.findById(신분당선_id)).thenReturn(Optional.of(신분당선));
        Mockito.lenient().when(stationService.findById(신사역_id)).thenReturn(신사역);
        Mockito.lenient().when(stationService.findById(강남역_id)).thenReturn(강남역);

        신분당선.addSection(신사역, 강남역, 7);

        // when
        // lineService.addSection 호출
        Long 논현역_id = 3L;
        Station 논현역 = new Station(논현역_id, "논현역");
        신분당선.addSection(신사역, 논현역, 7);

        // then
        // lineService.findLineById 메서드를 통해 검증
        Sections sections = 신분당선.getSections();
        assertThat(sections.getSections().size()).isEqualTo(2);
    }
}
