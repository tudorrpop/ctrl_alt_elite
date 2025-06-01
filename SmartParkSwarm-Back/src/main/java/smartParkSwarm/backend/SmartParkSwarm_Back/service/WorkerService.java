package smartParkSwarm.backend.SmartParkSwarm_Back.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.entity.Store;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.request.StoreRequest;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.response.*;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.worker.ParkingLot;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.worker.ParkingSpot;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.worker.VehicleEntry;
import smartParkSwarm.backend.SmartParkSwarm_Back.repository.StoreRepository;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class WorkerService {

    private WebClient webClient;

    private StoreService storeService;

    private StoreRepository storeRepository;

    public WorkerService(WebClient.Builder webClientBuilder, StoreService storeService, StoreRepository storeRepository) {
        this.webClient = webClientBuilder.build();
        this.storeRepository = storeRepository;
        this.storeService = storeService;
    }

    public StoreOverviewModel setupWorker(StoreRequest storeRequest) throws InterruptedException {

        //lambda from Azure call
        Integer port =  ThreadLocalRandom.current().nextInt(8000, 9000);
        Map<String, Object> requestBodyAzureFunction = Map.of(
                "name", storeRequest.getStoreName().toLowerCase(),
                "targetPort", port,
                "exposedPort",port,
                "containerName", storeRequest.getStoreName().toLowerCase() + "container"
        );
        //String storeFQDN = callAzureFunction("createworkers.azurewebsites.net", "/api/containerapp_creator", requestBodyAzureFunction, String.class).block() + ":8000";
        // String fullFQDNandPort = storeFQDN.substring(0, storeFQDN.length() - 5)+ ":"+ port;
        //String fullFQDNandPort = storeRequest.getStoreName().toLowerCase() + ":" + port;

        //this is used to create a worker locally. to to be set back to azure function later
         String  fullFQDNandPort = "127.0.0.1:8000";

        Thread.sleep(45000);

        Store store = storeService.saveStore(storeRequest, fullFQDNandPort);

        Map<String, Object> requestBody = Map.of(
                "name", storeRequest.getStoreName(),
                "location", storeRequest.getStoreAddress(),
                "capacity", store.getCapacity(),
                "self_id", store.getId());
        createData(fullFQDNandPort, "/api/setup", requestBody, ParkingLot.class).block();
        return new StoreOverviewModel(
            store.getId(),
                store.getStoreName(),
                store.getStoreAddress()
        );
    }


    public ParkingLot fetchWorkerInformation(String ipaddress) {
        return retrieveData(ipaddress, "/api/ParkingLot", ParkingLot.class).block();
    }

    public List<ParkingSpot> fetchAllParkingSpots(Long id) {

        Optional<Store> store = storeRepository.findById(id);
        return store.map(value -> retrieveData(value.getIpAddress(), "/api/ParkingSpot", new ParameterizedTypeReference<List<ParkingSpot>>() {}).block()).orElse(Collections.emptyList());
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

    private <T, R> Mono<T> callAzureFunction(String ipaddress, String endpoint, R requestBody, Class<T> clazz) {
        return webClient.post()
                .uri("https://" + ipaddress + endpoint)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("x-functions-key", "VALUE")
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


    public List<SpotInfo> returnFreeSpotsJsonThreeDays(Long id) throws IOException {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Store not found"));

        String ipAddress = store.getIpAddress();
        String endpoint = "/api/metrics/free-spots-3-days";
        String url = ipAddress + endpoint;

        String jsonString = retrieveData(url);

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(jsonString, new TypeReference<List<SpotInfo>>(){});
    }

    public String retrieveData(String url) {
        return webClient
                .get()
                .uri("http://"+url)
                .retrieve()
                .bodyToMono(String.class)
                .block(); // Blocking call; safe in non-reactive apps
    }

    public List<SpotInfo> returnFreeSpotsJsonToday(Long id) throws JsonProcessingException {

        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Store not found"));

        String ipAddress = store.getIpAddress();
        String endpoint = "/api/metrics/free-spots-today";
        String url = ipAddress + endpoint;

        // Call the Python service

        String jsonString = retrieveData(url);

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(jsonString, new TypeReference<List<SpotInfo>>(){});
    }

    public List<WeekdayOcuppancy> returnFreeSpotsJsonOccupancyWeekday(Long id) throws JsonProcessingException {

        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Store not found"));

        String ipAddress = store.getIpAddress();
        String endpoint = "/api/metrics/occupancy-by-weekday";
        String url = ipAddress + endpoint;

        String jsonString = retrieveData(url);

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(jsonString, new TypeReference<List<WeekdayOcuppancy>>(){});
    }

    public List<MonthOcuppancy> returnFreeSpotsJsonOccupancyMonth(Long id) throws JsonProcessingException {

        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Store not found"));

        String ipAddress = store.getIpAddress();
        String endpoint = "/api/metrics/occupancy-by-month";
        String url = ipAddress + endpoint;

        String jsonString = retrieveData(url);

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(jsonString, new TypeReference<List<MonthOcuppancy>>(){});

    }

    public List<MonthDayOcuppancy> returnFreeSpotsJsonOccupancyMonthDay(Long id) throws JsonProcessingException {

        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Store not found"));

        String ipAddress = store.getIpAddress();
        String endpoint = "/api/metrics/occupancy-by-month-day";
        String url = ipAddress + endpoint;

        // Call the Python service

        String jsonString = retrieveData(url);

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(jsonString, new TypeReference<List<MonthDayOcuppancy>>(){});
    }

    public List<MonthStatisticsOcuppancy> returnMonthlyStatistics() {
        List<MonthStatisticsOcuppancy> result = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Store> stores = storeRepository.findAll();
        for(Store store : stores) {
            String ipAddress = store.getIpAddress();
            String endpoint = "/api/metrics/occupancy-by-month";
            String url = ipAddress + endpoint;

            try {
                String json = retrieveData(url); // returns JSON string

                // Deserialize JSON array
                List<MonthStatisticsOcuppancy> monthlyData = objectMapper.readValue(
                        json,
                        new TypeReference<List<MonthStatisticsOcuppancy>>() {}
                );

                for (MonthStatisticsOcuppancy entry : monthlyData) {
                    result.add(new MonthStatisticsOcuppancy(
                            store.getStoreName(),
                            entry.getMonth_name(),
                            entry.getOccupancy_percent()
                    ));
                }

            } catch (Exception e) {
                System.err.println("Error fetching stats from " + store.getStoreName() + ": " + e.getMessage());
            }
        }
        return result;
    }

    public String getAnswer() throws Exception {


        List<Store> stores = storeRepository.findAll();
        StringBuilder csvBuilder = new StringBuilder();

        String[] endpoints = {
                "/api/metrics/free-spots-today",
                "/api/metrics/free-spots-3-days",
                "/api/metrics/occupancy-by-weekday",
                "/api/metrics/occupancy-by-month",
                "/api/metrics/occupancy-by-month-day"
        };

        String[] labels = {
                "Free Spots Today",
                "Free Spots Last 3 Days",
                "Occupancy by Weekday",
                "Occupancy by Month",
                "Occupancy by Month Day"
        };

        for (Store store : stores) {
            String ip = store.getIpAddress();

            for (int i = 0; i < endpoints.length; i++) {
                String endpoint = endpoints[i];
                String label = labels[i];
                String url = ip + endpoint;

                try {
                    String csv = retrieveData(url); // This calls WebClient and fetches the CSV
                    csvBuilder.append("Store: ").append(store.getStoreName()).append("\n");
                    csvBuilder.append("Metric: ").append(label).append("\n");
                    csvBuilder.append(csv).append("\n\n");
                } catch (Exception e) {
                    System.err.println("Failed to fetch " + label + " for store " + store.getStoreName() + ": " + e.getMessage());
                }
            }
        }
        return csvBuilder.toString();

    }
}
