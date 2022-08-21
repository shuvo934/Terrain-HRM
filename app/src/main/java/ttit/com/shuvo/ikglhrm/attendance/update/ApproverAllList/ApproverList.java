package ttit.com.shuvo.ikglhrm.attendance.update.ApproverAllList;

public class ApproverList {

    private String approver_emp_id;
    private String approver_emp_name;

    public ApproverList(String approver_emp_id, String approver_emp_name) {
        this.approver_emp_id = approver_emp_id;
        this.approver_emp_name = approver_emp_name;
    }

    public String getApprover_emp_id() {
        return approver_emp_id;
    }

    public void setApprover_emp_id(String approver_emp_id) {
        this.approver_emp_id = approver_emp_id;
    }

    public String getApprover_emp_name() {
        return approver_emp_name;
    }

    public void setApprover_emp_name(String approver_emp_name) {
        this.approver_emp_name = approver_emp_name;
    }
}
