package com.github.andreyasadchy.xtra.util

/** Keep bookmarks and filters when migrating settings from builds with downloads. */
object SavedTabPolicy {
    fun normalize(saved: String?): List<String> {
        val defaults = C.DEFAULT_SAVED_TABS.split(',')
        val allowed = defaults.map { it.substringBefore(':') }.toSet()
        val tabs = linkedMapOf<String, List<String>>()
        saved?.split(',')?.forEach { entry ->
            val fields = entry.split(':')
            if (fields.size == 3 && fields[0] in allowed &&
                fields[1] in setOf("0", "1") && fields[2] in setOf("0", "1")) {
                tabs.putIfAbsent(fields[0], fields)
            }
        }
        defaults.forEach { tabs.putIfAbsent(it.substringBefore(':'), it.split(':')) }
        val selected = tabs.values.firstOrNull { it[1] == "1" && it[2] == "1" }?.get(0)
            ?: tabs.values.firstOrNull { it[2] == "1" }?.get(0)
        return tabs.values.map { listOf(it[0], if (it[0] == selected) "1" else "0", it[2]).joinToString(":") }
    }
}
