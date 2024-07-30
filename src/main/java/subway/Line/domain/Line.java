package subway.Line.domain;

import static subway.global.exception.ExceptionCode.INVALID_DELETE_DOWNSTATION;
import static subway.global.exception.ExceptionCode.INVALID_DOWNSTATION_NOT_NEW_EQUAL_DOWNSTATION;
import static subway.global.exception.ExceptionCode.INVALID_DOWNSTATION_TO_BE_NEW_UPSTATION;
import static subway.global.exception.ExceptionCode.INVALID_SECTION_MIN;
import static subway.global.exception.ExceptionCode.NOT_FOUND_STATION;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import subway.global.exception.BadRequestException;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;
    @Column(length = 20, nullable = false)
    private String color;

    @OneToMany(
            mappedBy = "line",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Section> sections = new ArrayList<>();

    public Line() {}

    public Line(String name, String color) {
        this(null, name, color);
    }

    public Line(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateColor(String color) {
        this.color = color;
    }

    public void addSection(Long upStationId, Long downStationId, int distance) {
        this.sections.add(new Section(upStationId, downStationId, distance, this));
    }

    public void deleteSection(Long stationId) {
        Section deleteSection = sections.stream().filter(section -> section.isDownStationId(stationId)).findAny()
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_STATION));

        if (this.sections.size() < 2) {
            throw new BadRequestException(INVALID_SECTION_MIN);
        }

        deleteSection.remove();
        this.sections.remove(deleteSection);
    }

    public int getDistance() {
        return this.sections.stream().mapToInt(Section::getDistance).sum();
    }

    public Optional<Long> getUpStationId() {
        if (this.sections.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(this.sections.get(0).getUpStationId());
    }

    public Optional<Long> getDownStationId() {
        if (this.sections.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(this.sections.get(this.sections.size() - 1).getDownStationId());
    }

    public List<Long> getStationsIds() {
        Long upStationId = getUpStationId().orElseThrow(() -> new IllegalArgumentException("해당 지하철 역은 존재하지 않습니다."));
        Long downStationId = getDownStationId().orElseThrow(() -> new IllegalArgumentException("해당 지하철 역은 존재하지 않습니다."));
        return List.of(upStationId, downStationId);
    }
}
