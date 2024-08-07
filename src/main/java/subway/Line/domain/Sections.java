package subway.Line.domain;

import static subway.global.exception.ExceptionCode.INVALID_DUPLICATE_SECTION;
import static subway.global.exception.ExceptionCode.INVALID_NO_EXIST_SECTION;
import static subway.global.exception.ExceptionCode.INVALID_SECTION_MIN;
import static subway.global.exception.ExceptionCode.NOT_FOUND_STATION;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import subway.Station.domain.Station;
import subway.global.exception.CustomException;

@Embeddable
public class Sections {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "line", orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {}

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public void add(Section newSection) {
        validateSection(newSection);
        if (this.sections.isEmpty()) {
            this.sections.add(newSection);
            return;
        }

        Optional<Section> optionalAfterSection = this.sections.stream()
                .filter(section -> section.isUpStation(newSection.getUpStation()))
                .findAny();

        if (optionalAfterSection.isPresent()) {
            appendFirst(newSection, optionalAfterSection);
        }

        if(optionalAfterSection.isEmpty()) {
            appendCenter(newSection);
        }

        this.sections.add(newSection);
    }

    private void appendCenter(Section newSection) {
        Optional<Section> optionalBeforeSection = this.sections.stream()
                .filter(section -> section.isDownStation(newSection.getUpStation()))
                .findAny();

        if (optionalBeforeSection.isPresent()) {
            Section beforeSection = optionalBeforeSection.get();
            beforeSection.updateUpStation(newSection.getDownStation());
        }
    }

    private static void appendFirst(Section newSection, Optional<Section> optionalAfterSection) {
        Section afterSection = optionalAfterSection.get();
        afterSection.updateDownStation(newSection.getUpStation());
        afterSection.decreaseDistance(newSection);
    }

    private void validateSection(Section newSection) {
        this.sections.forEach(section -> {
            if(
                    section.isUpStation(newSection.getUpStation()) &&
                            section.isDownStation(newSection.getDownStation())
            ) {
               throw new CustomException(INVALID_DUPLICATE_SECTION);
            }
        });
    }

    public void deleteSection(Station station) {
        Section deleteSection = sections.stream().filter(section -> section.isDownStation(station))
                .findAny()
                .orElseThrow(() -> new CustomException(NOT_FOUND_STATION));

        if (this.sections.size() < 2) {
            throw new CustomException(INVALID_SECTION_MIN);
        }

        deleteSection.remove();
        this.sections.remove(deleteSection);
    }

    public Long getUpStationId() {
        if (this.sections.isEmpty()) {
            throw new CustomException(INVALID_NO_EXIST_SECTION);
        }
        return this.sections.get(0).getUpStationId();
    }

    public Long getDownStationId() {
        if (this.sections.isEmpty()) {
            throw new CustomException(INVALID_NO_EXIST_SECTION);
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

    public int size() {
        return sections.size();
    }
}
