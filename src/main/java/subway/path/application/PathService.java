package subway.path.application;

import static subway.global.exception.ExceptionCode.NOT_FOUND_STATION;

import java.util.List;
import org.springframework.stereotype.Service;
import subway.Line.domain.Section;
import subway.Line.infrastructure.SectionRepository;
import subway.Station.domain.Station;
import subway.Station.infrastructure.StationRepository;
import subway.global.exception.CustomException;
import subway.path.domain.PathBuilder;
import subway.path.domain.PathFinder;

@Service
public class PathService {

    private final PathBuilder pathBuilder;
    private final PathFinder pathFinder;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public PathService(PathBuilder pathBuilder, PathFinder pathFinder, StationRepository stationRepository, SectionRepository sectionRepository) {
        this.pathBuilder = pathBuilder;
        this.pathFinder = pathFinder;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public void getPath(Long source, Long target) {
        Station sourceStation = this.stationRepository.findById(source)
                .orElseThrow(() -> new CustomException(NOT_FOUND_STATION));
        Station targetStation = this.stationRepository.findById(target)
                .orElseThrow(() -> new CustomException(NOT_FOUND_STATION));
        createPaths();
        pathFinder.getPath(pathBuilder.build(), sourceStation, targetStation);
    }

    private void createPaths() {
        List<Section> sections = sectionRepository.findAll();
        sections.forEach(pathBuilder::addSection);
    }
}
