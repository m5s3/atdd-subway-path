package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import nextstep.subway.unit.fixture.SectionInMemoryRepository;
import nextstep.subway.unit.fixture.StationInMemoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.Station.domain.Station;
import subway.path.application.PathService;
import subway.path.domain.PathFinder;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
public class PathServiceMockTest extends SectionInMemoryRepository {
//    @Mock
//    private LineRepository lineRepository;
//    @Mock
//    private StationRepository stationRepository;
//    @Mock
//    private SectionRepository sectionRepository;

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
        pathService = new PathService(new PathFinder(), new StationInMemoryRepository(), new SectionInMemoryRepository());

        // when
        List<Station> result = pathService.getPath(교대역_id, 양재역_id);

        // then
        assertThat(result).containsExactly(교대역, 남부터미널역, 양재역);
    }

    @Test
    void getPathWeight() {
        // given
        pathService = new PathService(new PathFinder(), new StationInMemoryRepository(), new SectionInMemoryRepository());

        // when
        double weight = pathService.getPathWeight(교대역_id, 양재역_id);

        // then
        assertThat(weight).isEqualTo(5);
    }
}
