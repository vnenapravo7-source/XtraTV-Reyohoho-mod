"""Verify that an APK contains exactly the requested native architecture."""
import argparse
import hashlib
from pathlib import Path
import zipfile

SUPPORTED = ("armeabi-v7a", "arm64-v8a", "x86", "x86_64")


def verify(apk: Path, abi: str) -> str:
    with zipfile.ZipFile(apk) as archive:
        names = archive.namelist()
        actual = {name.split("/")[1] for name in names if name.startswith("lib/") and name.endswith(".so")}
        if actual != {abi}:
            raise ValueError(f"Expected only {abi}, found {sorted(actual)}")
        if any("translate_jni" in name or "language_id_l2c_jni" in name for name in names):
            raise ValueError("Removed ML Kit native libraries are present")
        if "AndroidManifest.xml" not in names:
            raise ValueError("Missing APK manifest")
    return hashlib.sha256(apk.read_bytes()).hexdigest()


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("apk", type=Path)
    parser.add_argument("--abi", required=True, choices=SUPPORTED)
    args = parser.parse_args()
    digest = verify(args.apk, args.abi)
    print(f"{digest}  {args.apk.name}")
