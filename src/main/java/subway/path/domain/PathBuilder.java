package subway.path.domain;

import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.Line.domain.Section;

@Component
public class PathBuilder {

    private final WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private ShortestPathAlgorithm dijkstraShortestPath;

    public ShortestPathAlgorithm build() {
        dijkstraShortestPath = new DijkstraShortestPath(graph);
        return dijkstraShortestPath;
    }

    public void addSection(Section section) {
        String upStationName = section.getUpStationName();
        String downStationName = section.getDownStationName();
        if (!graph.containsVertex(upStationName)) {
            System.out.println("upStationName = " + upStationName);
            graph.addVertex(upStationName);
        }

        if (!graph.containsVertex(downStationName)) {
            System.out.println("downStationName = " + downStationName);
            graph.addVertex(downStationName);
        }

        graph.setEdgeWeight(graph.addEdge(upStationName, downStationName), section.getDistance());
        System.out.println("graph.getEdge(upStationName, downStationName) = " + graph.getEdge(upStationName, downStationName));
    }

    public WeightedMultigraph<String, DefaultWeightedEdge> getGraph() {
        return graph;
    }
}
