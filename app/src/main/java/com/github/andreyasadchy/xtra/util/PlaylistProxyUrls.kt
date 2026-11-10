package com.github.andreyasadchy.xtra.util

import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

/** ReYohoho's public playlist protocol. Account credentials are deliberately never appended. */
object PlaylistProxyUrls {
    val reyohohoHosts = listOf("proxy4.rte.net.ru", "proxy7.rte.net.ru", "proxy5.rte.net.ru", "proxy6.rte.net.ru")
    val defaults = reyohohoHosts.map { "https://$it/https://usher.ttvnw.net/api/channel/hls/\$channel.m3u8" }

    fun isReyohoho(raw: String): Boolean = normalized(raw)?.let { it.isHttps && it.host in reyohohoHosts } == true

    private fun normalized(raw: String) = (if (raw.contains("://")) raw else "https://$raw").toHttpUrlOrNull()

    fun build(raw: String?, channel: String, addParams: Boolean, codecs: String?): String? {
        if (raw.isNullOrBlank() || !channel.matches(Regex("[A-Za-z0-9_]+"))) return null
        val url = normalized(raw.replace("\$channel", channel)) ?: return null
        return url.newBuilder().apply {
            if (addParams) {
                val defaults = linkedMapOf("allow_source" to "true", "allow_audio_only" to "true", "fast_bread" to "true", "include_unavailable" to "true")
                if (!codecs.isNullOrBlank()) defaults["supported_codecs"] = codecs
                defaults.forEach { (name, value) -> if (url.queryParameter(name) == null) addQueryParameter(name, value) }
            }
        }.build().toString()
    }

    /** Drop all playlist parameters: health probes must not carry auth or playback tokens. */
    fun healthUrl(raw: String): String? = normalized(raw)?.newBuilder()?.apply {
        encodedPath(if (isReyohoho(raw)) "/russia-only-channels" else "/ping")
        query(null)
        fragment(null)
    }?.build()?.toString()
}
