package smartParkSwarm.backend.SmartParkSwarm_Back.service;

import jakarta.persistence.EntityExistsException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.entity.Store;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.request.StoreRequest;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.worker.ParkingLot;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.worker.ParkingSpot;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.worker.VehicleEntry;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class WorkerService {

    private WebClient webClient;

    private StoreService storeService;

    public WorkerService(WebClient.Builder webClientBuilder, StoreService storeService) {
        this.webClient = webClientBuilder.build();
        this.storeService = storeService;
    }

    public ParkingLot setupWorker(StoreRequest storeRequest) {


        storeService.saveStore(storeRequest);

        Map<String, Object> requestBody = Map.of(
                "name", storeRequest.getStoreName(),
                "location", storeRequest.getStoreAddress(),
                "capacity", storeRequest.getCapacity()
        );
        return createData(storeRequest.getIpAddress(), "/api/setup", requestBody, ParkingLot.class).block();
    }

    public ParkingLot fetchWorkerInformation(String ipaddress) {
        return retrieveData(ipaddress, "/api/ParkingLot", ParkingLot.class).block();
    }

    public List<ParkingSpot> fetchAllParkingSpots(String ipaddress) {
        return retrieveData(ipaddress, "/api/ParkingSpot", new ParameterizedTypeReference<List<ParkingSpot>>() {}).block();
    }

    public ParkingSpot fetchParkingSpot(String ipaddress, String parkingSpot) {
        return retrieveData(ipaddress, "/api/ParkingSpot/" + parkingSpot, ParkingSpot.class).block();
    }

    public List<VehicleEntry> fetchAllVehicleEntries(String ipaddress) {
        return retrieveData(ipaddress, "/api/VehicleEntry", new ParameterizedTypeReference<List<VehicleEntry>>() {}).block();
    }

    public VehicleEntry fetchVehicleEntry(String ipaddress, String vehicleEntry) {
        return retrieveData(ipaddress, "/api/VehicleEntry/" + vehicleEntry, VehicleEntry.class).block();
    }

    private <T> Mono<T> retrieveData(String ipaddress, String endpoint, Class<T> clazz){
        return webClient.get()
                .uri( "http://" + ipaddress + endpoint)
                .retrieve()
                .bodyToMono(clazz);
    }

    private <T, R> Mono<T> createData(String ipaddress, String endpoint, R requestBody, Class<T> clazz) {
        return webClient.post()
                .uri("http://" + ipaddress + endpoint)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(clazz);
    }


    private <T> Mono<T> retrieveData(String ipaddress, String endpoint, ParameterizedTypeReference<T> typeRef) {
        return webClient.get()
                .uri("http://" + ipaddress + endpoint)
                .retrieve()
                .bodyToMono(typeRef);
    }
}
