package subway.Line.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
        if (Objects.isNull(name)) {
            return;
        }
        this.name = name;
    }

    public void updateColor(String color) {
        if (Objects.isNull(color)) {
            return;
        }
        this.color = color;
    }

    public void addSection(Long upStationId, Long downStationId, int distance) {
        this.sections.add(new Section(upStationId, downStationId, distance, this));
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
