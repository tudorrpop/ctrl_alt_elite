package smartParkSwarm.backend.SmartParkSwarm_Back.model.response;

public class ChatResponse {
    String key;
    String value;

    public ChatResponse(String value, String key) {
        this.value = value;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
