package com.jewelexx.tablocation.github;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Tags {
    private String name;
    private String zipballURL;
    private String tarballURL;
    private Commit commit;
    private String nodeID;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String value) {
        this.name = value;
    }

    @JsonProperty("zipball_url")
    public String getZipballURL() {
        return zipballURL;
    }

    @JsonProperty("zipball_url")
    public void setZipballURL(String value) {
        this.zipballURL = value;
    }

    @JsonProperty("tarball_url")
    public String getTarballURL() {
        return tarballURL;
    }

    @JsonProperty("tarball_url")
    public void setTarballURL(String value) {
        this.tarballURL = value;
    }

    @JsonProperty("commit")
    public Commit getCommit() {
        return commit;
    }

    @JsonProperty("commit")
    public void setCommit(Commit value) {
        this.commit = value;
    }

    @JsonProperty("node_id")
    public String getNodeID() {
        return nodeID;
    }

    @JsonProperty("node_id")
    public void setNodeID(String value) {
        this.nodeID = value;
    }
}
