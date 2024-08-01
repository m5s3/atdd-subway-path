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
import javax.persistence.Embedded;
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

    @Embedded
    private Sections sections = new Sections();

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
        this.sections.addSection(new Section(upStationId, downStationId, distance, this));
    }

    public void deleteSection(Long stationId) {
        this.sections.deleteSection(stationId);
    }

    public int getDistance() {
        return this.sections.calculateDistance();
    }

    public Sections getSections() {
        return sections;
    }
}
