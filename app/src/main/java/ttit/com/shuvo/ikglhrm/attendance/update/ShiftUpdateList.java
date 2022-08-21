package ttit.com.shuvo.ikglhrm.attendance.update;

public class ShiftUpdateList {

    private String shift_id;
    private String shift_name;

    public ShiftUpdateList(String shift_id, String shift_name) {
        this.shift_id = shift_id;
        this.shift_name = shift_name;
    }

    public String getShift_id() {
        return shift_id;
    }

    public void setShift_id(String shift_id) {
        this.shift_id = shift_id;
    }

    public String getShift_name() {
        return shift_name;
    }

    public void setShift_name(String shift_name) {
        this.shift_name = shift_name;
    }
}
