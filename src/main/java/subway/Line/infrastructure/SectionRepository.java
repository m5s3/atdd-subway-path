package subway.Line.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.Line.domain.Section;

public interface SectionRepository extends JpaRepository<Section, Long> {

}
