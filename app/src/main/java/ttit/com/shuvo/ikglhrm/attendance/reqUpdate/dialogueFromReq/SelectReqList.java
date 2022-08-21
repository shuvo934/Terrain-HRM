package ttit.com.shuvo.ikglhrm.attendance.reqUpdate.dialogueFromReq;

public class SelectReqList {

    private String darm_id;
    private String darm_app_code;
    private String update_time;
    private String arrival;
    private String departure;
    private String darm_date;

    public SelectReqList(String darm_id, String darm_app_code, String update_time, String arrival, String departure, String darm_date) {
        this.darm_id = darm_id;
        this.darm_app_code = darm_app_code;
        this.update_time = update_time;
        this.arrival = arrival;
        this.departure = departure;
        this.darm_date = darm_date;
    }

    public String getDarm_id() {
        return darm_id;
    }

    public void setDarm_id(String darm_id) {
        this.darm_id = darm_id;
    }

    public String getDarm_app_code() {
        return darm_app_code;
    }

    public void setDarm_app_code(String darm_app_code) {
        this.darm_app_code = darm_app_code;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDarm_date() {
        return darm_date;
    }

    public void setDarm_date(String darm_date) {
        this.darm_date = darm_date;
    }
}
