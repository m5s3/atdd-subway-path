package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.line.domain.Line;
import subway.line.domain.Section;
import subway.line.infrastructure.LineRepository;
import subway.line.infrastructure.SectionRepository;
import subway.Station.domain.Station;
import subway.Station.infrastructure.StationRepository;
import subway.path.application.PathService;
import subway.path.domain.PathFinder;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
public class PathServiceMockTest {
    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationRepository stationRepository;
    @Mock
    private SectionRepository sectionRepository;

    private PathService pathService;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @Test
    void getPath() {
        // given
        Long 교대역_id = 1L;
        Long 강남역_id = 2L;
        Long 양재역_id = 3L;
        Long 남부터미널역_id = 4L;

        Station 교대역 = new Station(교대역_id, "교대역");
        Station 강남역 = new Station(강남역_id, "강남역");
        Station 양재역 = new Station(양재역_id, "양재역");
        Station 남부터미널역 = new Station(남부터미널역_id, "남부터미널역");

        Long 이호선_id = 1L;
        Long 신분당선_id = 2L;
        Long 삼호선_id = 3L;
        Line 이호선 = new Line(이호선_id, "이호선", "green");
        Line 신분당선 = new Line(신분당선_id, "신분당선", "red");
        Line 삼호선 = new Line(삼호선_id, "삼호선", "blue");

        Long 교대역_강남역_구간_id = 1L;
        Long 강남역_양재역_구간_id = 2L;
        Long 교대역_남부터미널_구간_id = 3L;
        Long 남부터미널_양재역_구간_id = 4L;
        Section 교대역_강남역_구간 = new Section(교대역_강남역_구간_id, 교대역, 강남역, 10, 이호선);
        Section 강남역_양재역_구간 = new Section(강남역_양재역_구간_id, 강남역, 양재역, 10, 신분당선);
        Section 교대역_남부터미널_구간 = new Section(교대역_남부터미널_구간_id, 교대역, 남부터미널역, 2, 삼호선);
        Section 남부터미널_양재역_구간 = new Section(남부터미널_양재역_구간_id, 남부터미널역, 양재역, 3, 삼호선);

        이호선.addSection(교대역_강남역_구간);
        신분당선.addSection(강남역_양재역_구간);
        삼호선.addSection(교대역_남부터미널_구간);
        삼호선.addSection(남부터미널_양재역_구간);

        Mockito.lenient().when(lineRepository.findById(이호선_id)).thenReturn(Optional.of(이호선));
        Mockito.lenient().when(lineRepository.findById(신분당선_id)).thenReturn(Optional.of(신분당선));
        Mockito.lenient().when(lineRepository.findById(삼호선_id)).thenReturn(Optional.of(삼호선));

        Mockito.lenient().when(stationRepository.findById(교대역_id)).thenReturn(Optional.of(교대역));
        Mockito.lenient().when(stationRepository.findById(강남역_id)).thenReturn(Optional.of(강남역));
        Mockito.lenient().when(stationRepository.findById(양재역_id)).thenReturn(Optional.of(양재역));
        Mockito.lenient().when(stationRepository.findById(남부터미널역_id)).thenReturn(Optional.of(남부터미널역));
        Mockito.lenient().when(stationRepository.findByNameIn(List.of("교대역", "남부터미널역", "양재역")))
                .thenReturn(List.of(교대역, 남부터미널역, 양재역));

        Mockito.lenient().when(sectionRepository.findAll()).thenReturn(List.of(교대역_강남역_구간, 강남역_양재역_구간, 교대역_남부터미널_구간, 남부터미널_양재역_구간));

        pathService = new PathService(new PathFinder(), stationRepository, sectionRepository);

        // when
        List<Section> sections = List.of(교대역_강남역_구간, 강남역_양재역_구간, 교대역_남부터미널_구간, 남부터미널_양재역_구간);
        List<Station> result = pathService.getPath(교대역_id, 양재역_id);

        // then
        assertThat(result).containsExactly(교대역, 남부터미널역, 양재역);
    }

    @Test
    void getPathWeight() {
        // given
        Long 교대역_id = 1L;
        Long 강남역_id = 2L;
        Long 양재역_id = 3L;
        Long 남부터미널역_id = 4L;

        Station 교대역 = new Station(교대역_id, "교대역");
        Station 강남역 = new Station(강남역_id, "강남역");
        Station 양재역 = new Station(양재역_id, "양재역");
        Station 남부터미널역 = new Station(남부터미널역_id, "남부터미널역");

        Long 이호선_id = 1L;
        Long 신분당선_id = 2L;
        Long 삼호선_id = 3L;
        Line 이호선 = new Line(이호선_id, "이호선", "green");
        Line 신분당선 = new Line(신분당선_id, "신분당선", "red");
        Line 삼호선 = new Line(삼호선_id, "삼호선", "blue");

        Long 교대역_강남역_구간_id = 1L;
        Long 강남역_양재역_구간_id = 2L;
        Long 교대역_남부터미널_구간_id = 3L;
        Long 남부터미널_양재역_구간_id = 4L;
        Section 교대역_강남역_구간 = new Section(교대역_강남역_구간_id, 교대역, 강남역, 10, 이호선);
        Section 강남역_양재역_구간 = new Section(강남역_양재역_구간_id, 강남역, 양재역, 10, 신분당선);
        Section 교대역_남부터미널_구간 = new Section(교대역_남부터미널_구간_id, 교대역, 남부터미널역, 2, 삼호선);
        Section 남부터미널_양재역_구간 = new Section(남부터미널_양재역_구간_id, 남부터미널역, 양재역, 3, 삼호선);

        이호선.addSection(교대역_강남역_구간);
        신분당선.addSection(강남역_양재역_구간);
        삼호선.addSection(교대역_남부터미널_구간);
        삼호선.addSection(남부터미널_양재역_구간);

        Mockito.lenient().when(lineRepository.findById(이호선_id)).thenReturn(Optional.of(이호선));
        Mockito.lenient().when(lineRepository.findById(신분당선_id)).thenReturn(Optional.of(신분당선));
        Mockito.lenient().when(lineRepository.findById(삼호선_id)).thenReturn(Optional.of(삼호선));

        Mockito.lenient().when(stationRepository.findById(교대역_id)).thenReturn(Optional.of(교대역));
        Mockito.lenient().when(stationRepository.findById(강남역_id)).thenReturn(Optional.of(강남역));
        Mockito.lenient().when(stationRepository.findById(양재역_id)).thenReturn(Optional.of(양재역));
        Mockito.lenient().when(stationRepository.findById(남부터미널역_id)).thenReturn(Optional.of(남부터미널역));
        Mockito.lenient().when(stationRepository.findByNameIn(List.of("교대역", "남부터미널역", "양재역")))
                .thenReturn(List.of(교대역, 남부터미널역, 양재역));

        Mockito.lenient().when(sectionRepository.findAll()).thenReturn(List.of(교대역_강남역_구간, 강남역_양재역_구간, 교대역_남부터미널_구간, 남부터미널_양재역_구간));

        pathService = new PathService(new PathFinder(), stationRepository, sectionRepository);

        // when
        List<Section> sections = List.of(교대역_강남역_구간, 강남역_양재역_구간, 교대역_남부터미널_구간, 남부터미널_양재역_구간);
        double weight = pathService.getPathWeight(교대역_id, 양재역_id);

        // then
        assertThat(weight).isEqualTo(5);
    }
}
