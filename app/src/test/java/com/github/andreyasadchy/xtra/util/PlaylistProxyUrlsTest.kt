package com.github.andreyasadchy.xtra.util

import okhttp3.HttpUrl.Companion.toHttpUrl
import org.junit.Assert.*
import org.junit.Test

class PlaylistProxyUrlsTest {
    @Test fun reyohohoEmbedsUsherPathAndRequestsSourceWithoutAccountCredentials() {
        for (template in PlaylistProxyUrls.defaults) {
            val url = PlaylistProxyUrls.build(template, "lofigirl", true, "h264")!!.toHttpUrl()
            assertTrue(url.isHttps)
            assertEquals("/https://usher.ttvnw.net/api/channel/hls/lofigirl.m3u8", url.encodedPath)
            assertEquals("true", url.queryParameter("allow_source"))
            assertEquals("true", url.queryParameter("fast_bread"))
            assertEquals("h264", url.queryParameter("supported_codecs"))
            assertNull(url.queryParameter("auth"))
            assertNull(url.queryParameter("token"))
        }
    }
    @Test fun preservesExplicitParametersWithoutDuplicates() {
        val url = PlaylistProxyUrls.build("https://example.org/live/\$channel?allow_source=false", "test_1", true, null)!!.toHttpUrl()
        assertEquals(listOf("false"), url.queryParameterValues("allow_source"))
        assertEquals("/live/test_1", url.encodedPath)
    }
    @Test fun disabledParameterAugmentationRemainsSupported() {
        assertEquals("https://example.org/live/test", PlaylistProxyUrls.build("example.org/live/\$channel", "test", false, "h264"))
    }
    @Test fun healthChecksStripTokensAndSelectProviderSpecificEndpoint() {
        assertEquals("https://proxy4.rte.net.ru/russia-only-channels", PlaylistProxyUrls.healthUrl("https://proxy4.rte.net.ru/live/test?auth=secret#fragment"))
        assertEquals("https://example.org/ping", PlaylistProxyUrls.healthUrl("example.org/live/test?token=secret"))
        assertFalse(PlaylistProxyUrls.isReyohoho("https://proxy4.rte.net.ru.evil.example/"))
        assertFalse(PlaylistProxyUrls.isReyohoho("http://proxy4.rte.net.ru/"))
    }
    @Test fun invalidChannelCannotInjectPathsOrQueryParameters() {
        assertNull(PlaylistProxyUrls.build(PlaylistProxyUrls.defaults.first(), "test?auth=injected", true, null))
        assertNull(PlaylistProxyUrls.build(null, "test", true, null))
    }
}
