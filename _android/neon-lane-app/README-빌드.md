# 네온 레인 Android 빌드 (Capacitor + AdMob)

웹 원본: `games/neon-lane/` → `scripts/sync-web.mjs` 가 `www/` 로 복사 → Capacitor 가 `android/` 에 반영.
Orbit Hop 과 같은 구조입니다 (Node 22+, JDK 21, Android Studio / SDK 36).

## 1. 최초 1회
```
cd _android/neon-lane-app
npm ci
```
업로드 키스토어 준비: `android/keystore/neonlane-upload.jks` 에 두고(`.gitignore` 처리됨),
`android/keystore.properties.example` 을 `android/keystore.properties` 로 복사해 비밀번호 입력.
(키스토어/비밀번호는 절대 커밋 금지. 이전에 전달한 `neonlane-upload.jks` 그대로 사용 가능)

## 2. AdMob 설정
1. AdMob 에서 '네온 레인'(Android, com.jackeychankey.neonlane) 앱 + 전면광고 + 배너 단위를 생성
2. 앱 ID(`ca-app-pub-…~…`) → `android/ads.properties` 의 `ADMOB_APP_ID`
3. 단위 ID → `games/neon-lane/ads-config.js` 의 `interstitial`, `banner` (`test:true` 는 그대로 — release 빌드가 자동으로 false 로 바꿈)

ID 를 채우기 전에는 Google 테스트 광고만 나옵니다 (실수로 실제 광고를 눌러 계정이 제재되는 것 방지).

## 3. 빌드
- 디버그 APK (테스트 광고): `npm run android:debug` → `android/app/build/outputs/apk/debug/`
- 스토어용 AAB: `npm run android:release` → `android/app/build/outputs/bundle/release/app-release.aab`
  (단위 ID 가 비어 있으면 중단됩니다)
- Android Studio: `npm run android:open`

## 4. 주의
- 업로드마다 `android/app/build.gradle` 의 `versionCode` 를 올리세요.
- 테스트 기기에서는 테스트 광고만 클릭하세요.
