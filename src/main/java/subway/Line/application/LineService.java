package subway.Line.application;

import static subway.global.exception.ExceptionCode.NOT_FOUND_LINE;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.Line.domain.Line;
import subway.Line.domain.Sections;
import subway.Line.presentation.dto.LineRequest;
import subway.Line.presentation.dto.LineResponse;
import subway.Line.infrastructure.LineRepository;
import subway.Line.presentation.dto.SectionRequest;
import subway.Line.presentation.dto.SectionResponse;
import subway.Line.presentation.dto.SectionsResponse;
import subway.Station.domain.Station;
import subway.Station.infrastructure.StationRepository;
import subway.global.exception.BadRequestException;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = lineRepository.save(toEntity(lineRequest));
        line.addSection(lineRequest.getUpStationId(), lineRequest.getDownStationId(), lineRequest.getDistance());
        return createLineResponse(line);
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getDistance(), createSectionsResponse(line.getSections()));
    }

    private Line toEntity(LineRequest lineRequest) {
        return new Line(
                lineRequest.getName(),
                lineRequest.getColor());
    }

    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(line -> new LineResponse(line.getId(), line.getName(), line.getColor(), line.getDistance(), createSectionsResponse(line.getSections())))
                .collect(Collectors.toList());
    }

    public LineResponse findLine(Long lineId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("해당 지하철 노선은 존재하지 않습니다. id=" + lineId));
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getDistance(), createSectionsResponse(line.getSections()));
    }

    private SectionsResponse createSectionsResponse(Sections sections) {
        return new SectionsResponse(sections.getSections().stream()
                .map(section -> new SectionResponse(section.getId(), section.getUpStationId(),
                        section.getDownStationId(), section.getDistance()))
                .collect(Collectors.toList()));
    }

    private Map<Long, Station> toMap(List<Station> stations) {
        return stations.stream()
                .collect(Collectors.toMap(Station::getId, station -> station));
    }

    @Transactional
    public void updateLine(Long lineId, LineRequest lineRequest) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_LINE));

        String name = lineRequest.getName();
        if (Objects.nonNull(name)) {
            line.updateName(name);
        }
        String color = lineRequest.getColor();
        if (Objects.nonNull(color)) {
            line.updateColor(color);
        }
    }

    @Transactional
    public void deleteLine(Long lineId) {
        lineRepository.deleteById(lineId);
    }

    @Transactional
    public void addSection(Long lineId, SectionRequest sectionRequest) {
        Line line = this.lineRepository.findById(lineId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_LINE));
        line.addSection(sectionRequest.getUpStationId(), sectionRequest.getDownStationId(), sectionRequest.getDistance());
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = this.lineRepository.findById(lineId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_LINE));

        line.deleteSection(stationId);
    }
}
