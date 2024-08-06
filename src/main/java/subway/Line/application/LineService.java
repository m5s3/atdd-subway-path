package subway.Line.application;

import static subway.global.exception.ExceptionCode.NOT_FOUND_LINE;
import static subway.global.exception.ExceptionCode.NOT_FOUND_STATION;

import java.util.List;
import java.util.Optional;
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
import subway.global.exception.CustomException;

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
        Station upStation = this.stationRepository.findById(lineRequest.getUpStationId())
                .orElseThrow(() -> new CustomException(NOT_FOUND_STATION));
        Station downStation = this.stationRepository.findById(lineRequest.getDownStationId())
                .orElseThrow(() -> new CustomException(NOT_FOUND_STATION));

        line.addSection(upStation, downStation, lineRequest.getDistance());

        return createLineResponse(line);
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse.Builder(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .distance(line.getDistance())
                .sections(createSectionsResponse(line.getSections()))
                .build();
    }

    private Line toEntity(LineRequest lineRequest) {
        return new Line(
                lineRequest.getName(),
                lineRequest.getColor());
    }

    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLine(Long lineId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("해당 지하철 노선은 존재하지 않습니다. id=" + lineId));
        return createLineResponse(line);
    }

    private SectionsResponse createSectionsResponse(Sections sections) {
        return new SectionsResponse(sections.getSections().stream()
                .map(section -> new SectionResponse.Builder()
                        .id(section.getId())
                        .upStationId(section.getUpStationId())
                        .downStationId(section.getDownStationId())
                        .distance(section.getDistance())
                        .build())
                .collect(Collectors.toList()));
    }

    @Transactional
    public void updateLine(Long lineId, LineRequest lineRequest) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_LINE));

        line.updateName(lineRequest.getName());
        line.updateColor(lineRequest.getColor());
    }

    @Transactional
    public void deleteLine(Long lineId) {
        lineRepository.deleteById(lineId);
    }

    @Transactional
    public void addSection(Long lineId, SectionRequest sectionRequest) {
        Line line = this.lineRepository.findById(lineId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_LINE));

        Station upStation = this.stationRepository.findById(sectionRequest.getUpStationId())
                .orElseThrow(() -> new CustomException(NOT_FOUND_STATION));
        Station downStation = this.stationRepository.findById(sectionRequest.getDownStationId())
                .orElseThrow(() -> new CustomException(NOT_FOUND_STATION));

        line.addSection(upStation, downStation, sectionRequest.getDistance());
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = this.lineRepository.findById(lineId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_LINE));

        Station station = this.stationRepository.findById(stationId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_STATION));

        line.deleteSection(station);
    }
}
