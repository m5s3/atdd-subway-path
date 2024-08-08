package subway.path.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.path.application.PathService;

@RequestMapping("/paths")
@RestController
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<Void> paths(@RequestParam Long source, @RequestParam Long target) {
        pathService.createPaths(pathService.findAllSections());
        pathService.getPath(source, target);
        return ResponseEntity.noContent().build();
    }
}
