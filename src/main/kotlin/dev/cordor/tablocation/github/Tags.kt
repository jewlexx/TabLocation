package dev.cordor.tablocation.github

import com.fasterxml.jackson.annotation.JsonProperty

class Tags {
    @get:JsonProperty("name")
    @set:JsonProperty("name")
    var name: String? = null

    @get:JsonProperty("zipball_url")
    @set:JsonProperty("zipball_url")
    var zipballURL: String? = null

    @get:JsonProperty("tarball_url")
    @set:JsonProperty("tarball_url")
    var tarballURL: String? = null

    @get:JsonProperty("commit")
    @set:JsonProperty("commit")
    var commit: Commit? = null

    @get:JsonProperty("node_id")
    @set:JsonProperty("node_id")
    var nodeID: String? = null
}
