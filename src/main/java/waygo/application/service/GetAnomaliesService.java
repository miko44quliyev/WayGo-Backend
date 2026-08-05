package waygo.application.service;

import waygo.application.port.in.GetAnomaliesUseCase;
import waygo.application.port.out.TrafficAnomalyRepository;
import waygo.domain.model.TrafficAnomaly;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class GetAnomaliesService implements GetAnomaliesUseCase {

    private final TrafficAnomalyRepository trafficAnomalyRepository;

    public GetAnomaliesService(TrafficAnomalyRepository trafficAnomalyRepository) {
        this.trafficAnomalyRepository = trafficAnomalyRepository;
    }

    @Override
    public List<TrafficAnomaly> handle() {
        return trafficAnomalyRepository.findActive().stream()
                .sorted(Comparator.comparing(TrafficAnomaly::detectedAt).reversed())
                .toList();
    }
}
