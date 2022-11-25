package pt.ua.hackaton.smartmove.data.requests;

public class ReportRequest {

    private String timestamp;

    public ReportRequest(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

}
