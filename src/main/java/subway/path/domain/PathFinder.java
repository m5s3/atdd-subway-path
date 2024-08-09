package subway.path.domain;

import static subway.global.exception.ExceptionCode.INVALID_CONNECT_PATH;
import static subway.global.exception.ExceptionCode.INVALID_DUPLICATE_PATH;

import java.util.List;
import java.util.Objects;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.Station.domain.Station;
import subway.global.exception.CustomException;
import subway.line.domain.Section;

@Component
public class PathFinder {

    private ShortestPathAlgorithm<String, DefaultWeightedEdge> path;

    public List<String> getPath(List<Section> sections, Station source, Station target) {
        if (Objects.isNull(path)) {
            sections.forEach(PathBuilder::addSection);
            path = PathBuilder.build();
        }
        validatePath(path, source, target);
        return path.getPath(source.getName(), target.getName()).getVertexList();
    }

    public double getPathWeight(List<Section> sections, Station source, Station target) {
        if (Objects.isNull(path)) {
            sections.forEach(PathBuilder::addSection);
            path = PathBuilder.build();
        }
        validatePath(path, source, target);
        return path.getPath(source.getName(), target.getName()).getWeight();
    }

    private void validatePath(ShortestPathAlgorithm<String, DefaultWeightedEdge> path, Station source, Station target) {
        if (source.equals(target)) {
            throw new CustomException(INVALID_DUPLICATE_PATH);
        }

        GraphPath<String, DefaultWeightedEdge> pathPath = path.getPath(source.getName(), target.getName());
        if (Objects.isNull(pathPath)) {
            throw new CustomException(INVALID_CONNECT_PATH);
        }
    }

    private static class PathBuilder {
        private static final WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        public static ShortestPathAlgorithm<String, DefaultWeightedEdge> build() {
            return new DijkstraShortestPath<>(graph);
        }

        public static void addSection(Section section) {
            String upStationName = section.getUpStationName();
            String downStationName = section.getDownStationName();
            if (!graph.containsVertex(upStationName)) {
                graph.addVertex(upStationName);
            }

            if (!graph.containsVertex(downStationName)) {
                graph.addVertex(downStationName);
            }

            graph.setEdgeWeight(graph.addEdge(upStationName, downStationName), section.getDistance());
        }
    }
}
