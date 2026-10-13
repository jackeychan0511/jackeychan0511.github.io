# Blob Drop — 안드로이드 앱 (AAB)

웹 게임(`games/blob-drop/`)을 WebView로 감싼 앱입니다. 게임 코드는 **저장소 한 곳(`games/blob-drop/`)에서만** 관리하고,
빌드할 때 `index.html`, `matter.min.js`, `team-jackeychankey.png` 가 앱 `assets/www/` 로 자동 복사됩니다.

- 패키지: `com.jackeychankey.blobdrop` / 최소 Android 7.0(API 24) / 타깃 Android 16(API 36)
- 권한: `VIBRATE`(충돌 햅틱)만 사용. 인터넷 권한 없음(완전 오프라인)

## 빌드 (Play 업로드용 AAB)
```bash
export ANDROID_HOME=<안드로이드 SDK 경로>      # platforms;android-36, build-tools;36.0.0 필요
cd apps/blobdrop-android
./gradlew bundleRelease                       # → app/build/outputs/bundle/release/app-release.aab
./gradlew assembleRelease                     # → 설치용 APK(테스트용)
```
새 버전을 낼 때마다 `app/build.gradle.kts` 의 `versionCode` 를 1씩 올리세요(Play 필수).

## 서명 키 (중요 — 저장소에 없음)
`keystore.properties` 와 `blobdrop-upload.jks` 는 `.gitignore` 로 제외됩니다. 두 파일을 **반드시 따로 안전하게 백업**하세요.
키를 잃어버리면 Play Console 에서 업로드 키 재설정을 요청해야 합니다.

`keystore.properties` 형식:
```
storeFile=blobdrop-upload.jks
storePassword=...
keyAlias=blobdrop-upload
keyPassword=...        # PKCS12 키스토어라 storePassword 와 같아야 함
```
파일이 없으면 서명되지 않은 번들이 만들어집니다.
