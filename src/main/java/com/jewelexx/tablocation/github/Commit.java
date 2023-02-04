package com.jewelexx.tablocation.github;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Commit {
    private String sha;
    private String url;

    @JsonProperty("sha")
    public String getSHA() {
        return sha;
    }

    @JsonProperty("sha")
    public void setSHA(String value) {
        this.sha = value;
    }

    @JsonProperty("url")
    public String getURL() {
        return url;
    }

    @JsonProperty("url")
    public void setURL(String value) {
        this.url = value;
    }
}
