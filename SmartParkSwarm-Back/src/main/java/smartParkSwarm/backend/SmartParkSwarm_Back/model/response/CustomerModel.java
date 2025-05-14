package smartParkSwarm.backend.SmartParkSwarm_Back.model.response;

public record CustomerModel(Long userId, String username, String firstName, String lastName, String email,
                            String phoneNumber, String membership, boolean active, String uuid) {
}
