#!/usr/bin/env bash
# 네온 레인 AAB 빌드 (Android SDK/Gradle 없이: aapt2 + javac + dx + bundletool)
# 필요: JDK 11+, aapt2(build-tools), dalvik-exchange(dx) 또는 d8, bundletool.jar, 업로드 키스토어
#   ANDROID_JAR = 자바 컴파일용 android.jar(API 23 이상, Java 8 바이트코드)
#   RES_JAR     = 리소스 링크용 최신 프레임워크(API 36 android.jar 또는 robolectric android-all-16); 없으면 ANDROID_JAR 사용
# 사용: KEYSTORE=... KS_ALIAS=... KS_PASS=... BUNDLETOOL=... ANDROID_JAR=... RES_JAR=... ./build-aab.sh [출력폴더]
set -euo pipefail
HERE="$(cd "$(dirname "$0")" && pwd)"; REPO="$(cd "$HERE/../.." && pwd)"
OUT="${1:-$HERE/out}"; B="$OUT/build"
: "${ANDROID_JAR:?}" "${BUNDLETOOL:?}" "${KEYSTORE:?}" "${KS_ALIAS:?}" "${KS_PASS:?}"
VERSION_CODE="${VERSION_CODE:-1}"; VERSION_NAME="${VERSION_NAME:-1.0.0}"; MIN_SDK=24; TARGET_SDK=36
rm -rf "$B"; mkdir -p "$B/classes" "$B/assets/www" "$B/module/manifest" "$B/module/dex"

# 1) 웹 게임 → 앱 assets (런타임에 필요한 파일만)
for f in index.html firebase-config.js team-logo.png icon-192.png icon-512.png apple-touch-icon.png; do cp "$REPO/games/neon-lane/$f" "$B/assets/www/$f"; done

# 2) 자바 컴파일 → dex
javac --release 8 -Xlint:-options -cp "$ANDROID_JAR" -d "$B/classes" $(find "$HERE/src" -name '*.java')
if command -v d8 >/dev/null; then d8 --release --min-api $MIN_SDK --output "$B/dexout" $(find "$B/classes" -name '*.class'); mkdir -p "$B/dexout"; cp "$B/dexout/classes.dex" "$B/module/dex/classes.dex"
else dalvik-exchange --dex --output="$B/module/dex/classes.dex" "$B/classes"; fi

# 3) 리소스 컴파일/링크(proto 형식) → base.apk
aapt2 compile --dir "$HERE/res" -o "$B/res.zip"
aapt2 link --proto-format -o "$B/base.apk" -I "${RES_JAR:-$ANDROID_JAR}" --manifest "$HERE/AndroidManifest.xml" \
  --min-sdk-version $MIN_SDK --target-sdk-version $TARGET_SDK --version-code "$VERSION_CODE" --version-name "$VERSION_NAME" \
  -A "$B/assets" -R "$B/res.zip" --auto-add-overlay

# 4) 모듈 조립 (manifest/ dex/ res/ assets/ resources.pb) → bundletool
( cd "$B/module" && unzip -q -o ../base.apk && mv AndroidManifest.xml manifest/AndroidManifest.xml && zip -q -r ../base.zip . -x '.*' )
rm -f "$OUT/neonlane.aab"
java -jar "$BUNDLETOOL" build-bundle --modules="$B/base.zip" --output="$OUT/neonlane.aab"

# 5) 업로드 키로 서명 (Play App Signing 사용 시 이 키가 '업로드 키')
jarsigner -keystore "$KEYSTORE" -storepass "$KS_PASS" -sigalg SHA256withRSA -digestalg SHA-256 "$OUT/neonlane.aab" "$KS_ALIAS" >/dev/null
echo "OK: $OUT/neonlane.aab (versionCode=$VERSION_CODE, versionName=$VERSION_NAME, targetSdk=$TARGET_SDK)"
