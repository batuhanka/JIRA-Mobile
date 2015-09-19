package com.project.ozyegin.vesteljiramobile.model;

/**
 * Created by Batuhan on 19.9.2015.
 */
public class IssueModel {

    private String issueId;
    private String issueKey;
    
    public IssueModel(String issueId, String issueKey){
        this.issueId    = issueId;
        this.issueKey   = issueKey;
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
}
