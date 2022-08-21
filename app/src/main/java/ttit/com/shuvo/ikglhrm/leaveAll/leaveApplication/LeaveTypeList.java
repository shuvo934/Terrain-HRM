package ttit.com.shuvo.ikglhrm.leaveAll.leaveApplication;

public class LeaveTypeList {

    private String id;
    private String typeName;

    public LeaveTypeList(String id, String typeName) {
        this.id = id;
        this.typeName = typeName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
