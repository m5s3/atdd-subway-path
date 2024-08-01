package subway.Line.presentation.dto;

import java.util.List;
import subway.Line.domain.Sections;
import subway.Station.presentation.dto.StationResponse;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private int distance;
    private SectionsResponse sections;

    public LineResponse(Long id, String name, String color, int distance, SectionsResponse sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.sections = sections;
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

    public int getDistance() {
        return distance;
    }

    public SectionsResponse getSections() {
        return sections;
    }
}
