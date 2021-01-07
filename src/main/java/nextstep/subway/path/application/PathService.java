package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathCalculateRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PathService {

	private final LineRepository lines;
	private final StationRepository stations;

	public PathService(LineRepository lines, StationRepository stations) {
		this.lines = lines;
		this.stations = stations;
	}

	public PathResponse calculatePath(PathCalculateRequest pathCalculateRequest) {
		Station source = stations.findById(pathCalculateRequest.getSourceStationId())
				.orElseThrow(() -> new PathCalculateException("존재하지 않는 출발역입니다."));
		Station target = stations.findById(pathCalculateRequest.getTargetStationId())
				.orElseThrow(() -> new PathCalculateException("존재하지 않는 도착역입니다."));

		List<Line> allLines = lines.findAll();
		Path path = new Path(allLines);
		return PathResponse.of(path.calculate(source, target));
	}
}