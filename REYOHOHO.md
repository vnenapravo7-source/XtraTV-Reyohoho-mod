# Xtra TV ReYohoho (experimental)

Based on razeNFR/XtraATV commit 29e2091 (2026-11-08), AGPL-3.0.

This variant has its own application ID (`com.github.razenfr.xtratv.reyohoho`,
with `.debug` for the test APK) and can coexist with existing clients.

The four public ReYohoho servers are enabled in order. Other upstream playlist
providers are preserved but disabled by default. The existing TV proxy panel can
enable, disable and reorder them. Keep its mode set to **Always** to obtain source
quality outside ad breaks too. The existing ad blocker stays enabled by default.

Only the master playlist request goes to ReYohoho. Child playlists and video
segments use the URLs returned by Twitch. No Twitch account OAuth token is added
to ReYohoho requests. Signing in for chat does not enable sharing account tokens
with the playlist service. 1440p and account-specific playback are not promised.

Main playback and adblock proxy backups use the same URL builder. TV previews and
multi-stream backups try the same configured proxy order before direct access.
If all proxies fail, upstream direct fallback remains available; source quality
may then be region limited. A green proxy status means its service endpoint
responded, not that a particular channel or resolution is guaranteed.

Public backend behavior may change. Do not infer successful midroll suppression
from an ad-free startup. Test actual ad breaks and long viewing on the target TV.
The test APK uses the upstream development signing key and is not a production
release. Do not install upstream APK updates over this variant.

Build on Windows with JDK 21 and Android SDK 37:

    $env:JAVA_HOME='path/to/jdk21'
    $env:ANDROID_HOME='path/to/Android/sdk'
    $env:JAVA_TOOL_OPTIONS='-Djava.io.tmpdir=C:/jtmp -Djdk.net.unixdomain.tmpdir=C:/jtmp'
    ./gradlew.bat :app:testDebugUnitTest :app:assembleDebug '-Pkotlin.daemon.jvmargs=-Xmx4g' --max-workers=4

The short temporary directory must exist. Its override avoids the Windows JDK
Unix-domain socket path failure seen on the development machine.

Reference protocol (not copied userscript code):
https://github.com/reyohoho/twitch_quality_proxy/tree/universal

AGPL source: upstream repository + the accompanying patch, including new files.

## Lightweight TCL build (144)

`assembleOptimized` packages ARMv7 only for the current TCL. Other devices need a build for their ABI.
ML Kit translation and its settings have been removed. Stream previews, animated emotes, PiP and
multi-stream playback are disabled even if an older version saved them as enabled.
The main video player and its ad-block fallback remain available.

Each player view starts with chat hidden. ChatFragment is created only when the user opens chat;
closing it removes the fragment and disconnects chat sockets. Restored fragments do not reopen chat.
The chat position panel counts as manually opening chat.

Further candidates, not removed: offline/download services and local playback; DVR/VOD rewind;
voice search; advanced chat rewards/polls/7TV effects; alternate MediaPlayer backend. Removing unused
code primarily simplifies maintenance/size; idle features need profiling before claiming a speedup.

## Repository-ready build (150)

Download services, download screens/actions, local-file import and offline playback entry points are removed.
Bookmarks and saved filters remain; legacy Room tables remain to preserve existing databases.
SavedTabPolicy migrates old download-tab selections (five regression tests).
Stream previews and parallel-player cuts from 144 remain. Animated emotes are still disabled.
The upstream updater entry points are disabled; updates must come from this mod's own releases.

The focus spring is removed to avoid unnecessary work after a focus change.
No reliable improvement in frame latency has been established. Some early measurements did not
capture panel visibility and must not be used for before/after comparisons. A native RecyclerView choice-panel experiment was
measured and reverted because it did not help; it is not part of the final source.
Further profiling is needed before claiming the remaining focus latency is fixed.

Optimized and release builds accept -PtargetAbi=armeabi-v7a/arm64-v8a/x86/x86_64; one ABI per APK.
Production signing is separate from the public development key. GitHub workflows build test artifacts
and create a manually triggered signed draft release. No GitHub repository/release was published here.

Four UI optimizations are batched in 150 for one combined device verification: focus shadows removed
from home cards (white focus border retained), duplicate blurred avatar removed from missing-VOD
thumbnails, focused-title marquee removed, and TV RecyclerView item-change animations disabled.
Do not test/install each of these separately: the user requested 3-5 changes per device round.
Offline state filtering is centralized in PlayerRepository, including media-button/service restores,
with three regression tests (13 unit tests total).
