package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.Line.application.LineService;
import subway.Line.domain.Line;
import subway.Line.domain.Section;
import subway.Line.domain.Sections;
import subway.Line.infrastructure.LineRepository;
import subway.Line.presentation.dto.SectionRequest;
import subway.Station.application.StationService;
import subway.Station.domain.Station;
import subway.Station.infrastructure.StationRepository;
import subway.Station.presentation.dto.StationRequest;
import subway.Station.presentation.dto.StationResponse;

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
        assertThat(sections.getUpStationId()).isEqualTo(신사역.getId());
        assertThat(sections.getDownStationId()).isEqualTo(논현역.getId());
    }
}
