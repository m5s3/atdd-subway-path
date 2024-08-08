package subway.path.domain;

import static subway.global.exception.ExceptionCode.INVALID_CONNECT_PATH;
import static subway.global.exception.ExceptionCode.INVALID_DUPLICATE_PATH;

import java.util.List;
import java.util.Objects;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.springframework.stereotype.Component;
import subway.Station.domain.Station;
import subway.global.exception.CustomException;

@Component
public class PathFinder {

    public List<String> getPath(ShortestPathAlgorithm path, Station source, Station target) {
        validatePath(path, source, target);
        return path.getPath(source.getName(), target.getName()).getVertexList();
    }

    public double getPathWeight(ShortestPathAlgorithm path, Station source, Station target) {
        validatePath(path, source, target);
        return path.getPath(source.getName(), target.getName()).getWeight();
    }

    private void validatePath(ShortestPathAlgorithm path, Station source, Station target) {
        if (source.equals(target)) {
            throw new CustomException(INVALID_DUPLICATE_PATH);
        }

        GraphPath pathPath = path.getPath(source.getName(), target.getName());
        if (Objects.isNull(pathPath)) {
            throw new CustomException(INVALID_CONNECT_PATH);
        }
    }
}
