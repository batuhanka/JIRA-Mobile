package com.project.ozyegin.vesteljiramobile.model;

/**
 * Created by Batuhan on 19.9.2015.
 */
public class IssueModel {

    private String issueId;
    private String issueKey;
    private String priority;
    
    public IssueModel(String issueId, String issueKey, String priority){
        this.issueId    = issueId;
        this.issueKey   = issueKey;
        this.priority   = priority;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public String getIssueKey() {
        return issueKey;
    }

    public void setIssueKey(String issueKey) {
        this.issueKey = issueKey;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
