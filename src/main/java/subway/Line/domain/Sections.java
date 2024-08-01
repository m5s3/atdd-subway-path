package subway.Line.domain;

import static subway.global.exception.ExceptionCode.INVALID_NO_EXIST_SECTION;
import static subway.global.exception.ExceptionCode.INVALID_SECTION_MIN;
import static subway.global.exception.ExceptionCode.NOT_FOUND_STATION;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import subway.global.exception.BadRequestException;

@Embeddable
public class Sections {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "line", orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {}

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public void addSection(Section section) {
        this.sections.add(section);
    }

    public void deleteSection(Long stationId) {
        Section deleteSection = sections.stream().filter(section -> section.isDownStationId(stationId))
                .findAny()
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_STATION));

        if (this.sections.size() < 2) {
            throw new BadRequestException(INVALID_SECTION_MIN);
        }

        deleteSection.remove();
        this.sections.remove(deleteSection);
    }

    public Long getUpStationId() {
        if (this.sections.isEmpty()) {
            throw new BadRequestException(INVALID_NO_EXIST_SECTION);
        }
        return this.sections.get(0).getUpStationId();
    }

    public Long getDownStationId() {
        if (this.sections.isEmpty()) {
            throw new BadRequestException(INVALID_NO_EXIST_SECTION);
        }
        return this.sections.get(this.sections.size() - 1).getDownStationId();
    }

    public int calculateDistance() {
        return this.sections.stream().mapToInt(Section::getDistance).sum();
    }

    public boolean isEmpty() {
        return this.sections.isEmpty();
    }

    public List<Section> getSections() {
        return sections;
    }
}
