package smartParkSwarm.backend.SmartParkSwarm_Back.model.response;

public record UserOverviewModel(Long userId, String username, String role, String token, String uuid) {
}
