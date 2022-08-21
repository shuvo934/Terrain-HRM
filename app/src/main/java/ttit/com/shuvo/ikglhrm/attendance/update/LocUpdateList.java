package ttit.com.shuvo.ikglhrm.attendance.update;

public class LocUpdateList {
    private String locID;
    private String location;

    public LocUpdateList(String locID, String location) {
        this.locID = locID;
        this.location = location;
    }

    public String getLocID() {
        return locID;
    }

    public void setLocID(String locID) {
        this.locID = locID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}


