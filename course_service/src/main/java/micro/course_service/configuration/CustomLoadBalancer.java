package micro.course_service.configuration;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomLoadBalancer implements ReactorServiceInstanceLoadBalancer {


    private int lastInstance;

    private final ServiceInstanceListSupplier serviceInstanceListSupplier;

    public CustomLoadBalancer(ServiceInstanceListSupplier serviceInstanceListSupplier) {
        this.serviceInstanceListSupplier = serviceInstanceListSupplier;
        this.lastInstance = 0;
    }


    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        return serviceInstanceListSupplier.get().next().map(this::selectInstance);
    }


    private Response<ServiceInstance> selectInstance(List<ServiceInstance> instances) {
        if (instances.isEmpty()) return new EmptyResponse();

        // Build weighted list
        List<ServiceInstance> weightedList = new ArrayList<>();
        for (ServiceInstance instance : instances) {
            int weight = parseWeight(instance);
            for (int i = 0; i < weight; i++) {
                weightedList.add(instance);
            }
        }
        lastInstance = lastInstance == weightedList.size() - 1 ? 0 : lastInstance + 1;
        System.out.println(weightedList.get(lastInstance).getPort());
        return new DefaultResponse(weightedList.get(lastInstance));
    }


    private int parseWeight(ServiceInstance instance) {
        Map<String, String> metadata = instance.getMetadata();
        String weightStr = metadata.getOrDefault("weight", "1"); // Default weight=1
        try {
            return Math.max(1, Integer.parseInt(weightStr));
        } catch (NumberFormatException e) {
            return 1;
        }
    }
}

