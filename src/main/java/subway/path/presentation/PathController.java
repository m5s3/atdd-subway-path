package subway.path.presentation;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.Station.domain.Station;
import subway.Station.presentation.dto.StationResponse;
import subway.path.application.PathService;
import subway.path.presentation.dto.PathResponse;

@RequestMapping("/paths")
@RestController
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> paths(@RequestParam Long source, @RequestParam Long target) {
        List<Station> stations = pathService.getPath(source, target);
        double pathWeight = pathService.getPathWeight(source, target);
        return ResponseEntity.ok().body(PathResponse.of(stations.stream().map(StationResponse::fromEntity).collect(Collectors.toList()),
                (int) pathWeight));
    }
}
