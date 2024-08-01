package subway.Line.presentation.dto;

import java.util.List;

public class SectionsResponse {

    private List<SectionResponse> sections;

    public SectionsResponse(List<SectionResponse> SectionResponses) {
        this.sections = SectionResponses;
    }

    public List<SectionResponse> getSections() {
        return sections;
    }
}
