package subway.path.application;

import static subway.global.exception.ExceptionCode.NOT_FOUND_STATION;

import java.util.List;
import org.springframework.stereotype.Service;
import subway.line.domain.Section;
import subway.line.infrastructure.SectionRepository;
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

    public List<Station> getPath(Long source, Long target) {
        SourceAndTargetStation sourceAndTargetStation = fetchSourceAndTarget(source, target);
        List<String> stationNames = pathFinder.getPath(pathBuilder.build(), sourceAndTargetStation.getSource(), sourceAndTargetStation.getTarget());
        return stationRepository.findByNameIn(stationNames);
    }

    public double getPathWeight(Long source, Long target) {
        SourceAndTargetStation sourceAndTargetStation = fetchSourceAndTarget(source, target);
        return pathFinder.getPathWeight(pathBuilder.build(), sourceAndTargetStation.getSource(), sourceAndTargetStation.getTarget());
    }

    public void createPaths(List<Section> sections) {
        sections.forEach(pathBuilder::addSection);
    }

    public List<Section> findAllSections() {
        return sectionRepository.findAll();
    }

    private class SourceAndTargetStation {

        private final Station source;
        private final Station target;

        public SourceAndTargetStation(Long source, Long target) {
            this.source = findById(source);
            this.target = findById(target);
        }

        public Station getSource() {
            return source;
        }

        public Station getTarget() {
            return target;
        }

        private Station findById(Long id) {
            return stationRepository.findById(id)
                    .orElseThrow(() -> new CustomException(NOT_FOUND_STATION));
        }
    }

    private SourceAndTargetStation fetchSourceAndTarget(Long source, Long target) {
        return new SourceAndTargetStation(source, target);
    }
}
