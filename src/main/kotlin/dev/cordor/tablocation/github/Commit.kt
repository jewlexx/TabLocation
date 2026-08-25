package dev.cordor.tablocation.github

import com.fasterxml.jackson.annotation.JsonProperty

class Commit {
    @get:JsonProperty("sha")
    @set:JsonProperty("sha")
    var sHA: String? = null

    @get:JsonProperty("url")
    @set:JsonProperty("url")
    var uRL: String? = null
}
