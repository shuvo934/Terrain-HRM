package ttit.com.shuvo.ikglhrm.leaveAll.leaveApprove.leaveAppDialogues;

public class ForwardHistoryList {

    private String forby;
    private String forComm;
    private String forTo;

    public ForwardHistoryList(String forby, String forComm, String forTo) {
        this.forby = forby;
        this.forComm = forComm;
        this.forTo = forTo;
    }

    public String getForby() {
        return forby;
    }

    public void setForby(String forby) {
        this.forby = forby;
    }

    public String getForComm() {
        return forComm;
    }

    public void setForComm(String forComm) {
        this.forComm = forComm;
    }

    public String getForTo() {
        return forTo;
    }

    public void setForTo(String forTo) {
        this.forTo = forTo;
    }
}
