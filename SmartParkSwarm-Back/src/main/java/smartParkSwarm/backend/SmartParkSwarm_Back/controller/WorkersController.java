package smartParkSwarm.backend.SmartParkSwarm_Back.controller;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.request.StoreRequest;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.response.*;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.worker.ParkingLot;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.worker.ParkingSpot;
import smartParkSwarm.backend.SmartParkSwarm_Back.service.UserService;
import smartParkSwarm.backend.SmartParkSwarm_Back.service.WorkerService;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@RestController
public class WorkersController {

    @Autowired
    private UserService userService;

    @Autowired
    private WorkerService workerService;


    @Autowired
    private SseController sseController;

    @Autowired
    public WorkersController(UserService userService, WorkerService workerService, SseController sseController) {
        this.userService = userService;
        this.workerService = workerService;
        this.sseController = sseController;
    }

    @PostMapping("/worker/markParked")
    public ResponseEntity<Boolean> markParked(@RequestParam String uuid) {
        boolean markParked = userService.markParked(uuid);
        return ResponseEntity.ok(markParked);
    }

    @PostMapping("/worker/unmarkParked")
    public ResponseEntity<Boolean> unmarkParked(@RequestParam String uuid) {
        boolean unmarkParked = userService.unmarkParked(uuid);
        return ResponseEntity.ok(unmarkParked);
    }

    @PutMapping("/worker/update")
    public ResponseEntity<List<ParkingSpotStatus>> updateParkingLot(@RequestParam Integer id) {
        Long idL = id.longValue();
        List<ParkingSpot> list = workerService.fetchAllParkingSpots(idL);
        List<ParkingSpotStatus> test = list.stream().map(spot ->
                new ParkingSpotStatus(
                        spot.getSpot_number(),
                        spot.is_occupied())).toList();

        sseController.broadcast(test);
        return ResponseEntity.ok(test);
    }

    @PostMapping("/worker/initialize")
    public ResponseEntity<StoreOverviewModel> initializeWorker(@RequestBody StoreRequest storeRequest) throws InterruptedException {
        StoreOverviewModel storeOverviewModel = workerService.setupWorker(storeRequest);
        return ResponseEntity.ok(storeOverviewModel);
    }

    @GetMapping(value = "/worker/metrics/threedays", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SpotInfo>> getFreeSpotsJsonThreeDays(@RequestParam String id) throws Exception {
        return ResponseEntity.ok(workerService.returnFreeSpotsJsonThreeDays(Long.parseLong(id)));
    }

    @GetMapping(value = "/worker/metrics/today", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SpotInfo>> getFreeSpotsJsonToday(@RequestParam String id) throws Exception {
        return ResponseEntity.ok(workerService.returnFreeSpotsJsonToday(Long.parseLong(id)));
    }

    @GetMapping(value = "/worker/metrics/occupancyweekday", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WeekdayOcuppancy>> getFreeSpotsJsonOccupancyWeekday(@RequestParam Long id) throws Exception {
        return ResponseEntity.ok(workerService.returnFreeSpotsJsonOccupancyWeekday(id));
    }

    @GetMapping(value = "/worker/metrics/occupancymonth", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MonthOcuppancy>> getFreeSpotsJsonOccupancyMonth(@RequestParam Long id) throws Exception {
        return ResponseEntity.ok(workerService.returnFreeSpotsJsonOccupancyMonth(id));
    }

    @GetMapping(value = "/worker/metrics/occupancymonthday", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MonthDayOcuppancy>> getFreeSpotsJsonOccupancyMonthDay(@RequestParam Long id) throws Exception {
        return ResponseEntity.ok(workerService.returnFreeSpotsJsonOccupancyMonthDay(id));
    }

    @GetMapping(value = "/worker/statistics/overall", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MonthStatisticsOcuppancy>> getMonthlyStoreStatistics() throws Exception {
        return ResponseEntity.ok(workerService.returnMonthlyStatistics());
    }

    @GetMapping(value = "/worker/chatprompt", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChatResponse> getChatPrompt() throws Exception {
        String value = workerService.getAnswer();
        return ResponseEntity.ok(
                new ChatResponse("context", value)
        );
    }


}
