# Neon Lane — Google Play Console 입력 자료

패키지명: `com.jackeychankey.neonlane` · versionCode 1 / versionName 1.0.0 · targetSdk 36 / minSdk 24

## 1. 스토어 등록정보 (한국어, 기본)
- **앱 이름 (≤30)**: 네온 레인 - 4색 리듬 러너
- **간단한 설명 (≤80)**: 빨주노초 4개 레인을 달리며 비트에 맞춰 점프! 일일 랭킹 & 명예의 전당
- **자세한 설명 (≤4000)**:
```
네온이 빛나는 사이버 도시에서 빨강·주황·노랑·초록 4개의 레인을 달리는 리듬 러너입니다.

■ 이렇게 즐겨요
- 좌/우 버튼으로 레인을 이동하고, 점프 버튼으로 장애물(음표 블록)을 넘어요.
- 한 번에 이어 그리는 빛의 궤적으로 음악이 완성돼요.
- 비트에 맞춰 PERFECT / GOOD 판정을 노려 콤보를 쌓으세요.
- 시간이 지날수록 BPM이 점점 빨라져요.

■ 돌발 이벤트
- 이벤트 1·2: 화면이 가로로 회전! 버튼 방향도 함께 바뀌어요.
- 이벤트 3: 화면이 상하 반전돼요.
- 방향이 바뀌어도 침착하게 비트를 따라가세요.

■ 랭킹
- 일일 랭킹: 매일 새로운 맵, 모두가 같은 조건으로 도전
- 명예의 전당: 최근 일일 챔피언들의 기록
- 닉네임만 정하면 바로 시작 (회원가입·로그인 없음)

■ 특징
- 결제 없음 (게임오버 후 광고가 표시됩니다)
- 가벼운 용량, 세로 화면 한 손 플레이

team. Jackeychankey
```
- **최근 변경사항(릴리스 노트)**: `첫 출시! 네온 레인에서 4색 레인을 달려 보세요.`

## 2. English (선택, en-US)
- **Title**: Neon Lane - 4 Color Rhythm Runner
- **Short**: Run 4 neon lanes, jump to the beat, climb the daily leaderboard!
- **Full**: Run through four glowing lanes (red, orange, yellow, green) in sync with the music. Move left/right, jump over note blocks, and chain PERFECT hits. Surprise events rotate or flip the screen — and your controls with it. Compete on the daily ranking and the Hall of Fame. Contains ads (after game over). No in-app purchases, no sign-up. — team. Jackeychankey

## 3. 그래픽 자산 (이 폴더)
- 앱 아이콘 512×512: `icon-512.png`
- 그래픽 이미지 1024×500: `feature-graphic-1024x500.png`
- 휴대전화 스크린샷 1080×1920: `screenshots/` (최소 2장, 권장 4~8장)

## 4. 정책 / 앱 콘텐츠
- **개인정보처리방침 URL**: https://jackeychan0511.github.io/games/neon-lane/privacy.html (Pages에 머지·배포된 후 유효)
- **앱 액세스**: 로그인 불필요 (제한 없음)
- **광고**: **예, 광고 포함** (AdMob 전면광고 + 결과화면 배너) · **인앱 결제**: 없음
- **대상 연령**: 13세 이상 (13세 미만 아님 — 광고 포함 앱이므로 아동 대상으로 선택하지 말 것)
- **콘텐츠 등급(IARC)**: 폭력·성적 콘텐츠·도박·약물 없음. 사용자 생성 콘텐츠 = 닉네임이 랭킹에 공개(채팅/메시징 없음). 위치 공유 없음. 예상 등급: 전체이용가 계열(UGC 문항 응답에 따라 달라질 수 있음).
- **카테고리**: 게임 > 음악 (또는 아케이드) · 태그: 리듬, 아케이드
- **뉴스 앱 / 코로나 / 정부 앱 / 금융 기능 / 건강 앱**: 모두 해당 없음

## 5. 데이터 보안 (Data safety)
- 데이터 수집: **예** / 데이터 공유(제3자): **예 — 광고 SDK(Google AdMob)와 광고 ID·대략적 위치 등 공유** (Firebase는 서비스 제공자로 처리). Console의 광고 SDK 안내에서 AdMob 항목을 그대로 따르세요.
- 전송 중 암호화: **예** (HTTPS) · 삭제 요청 방법 제공: **예** (privacy.html 참고)
- 수집 항목:
  | 범주 | 항목 | 목적 | 필수 여부 |
  |---|---|---|---|
  | 개인 정보 | 이름(닉네임) | 앱 기능(랭킹) | 선택 아님(랭킹 이용 시) |
  | 기기 또는 기타 ID | 익명 Firebase 사용자 ID | 앱 기능, 부정 방지 | 필수 |
  | 앱 활동 | 기타 작업(점수·거리·콤보) | 앱 기능(랭킹) | 필수 |
  | 기기 또는 기타 ID | 광고 ID(AdMob) | 광고 또는 마케팅, 분석 | 필수 |
  | 앱 정보 및 성능 | 비정상 종료 로그·진단(AdMob SDK) | 분석 | 필수 |
  | 위치 | 대략적인 위치(AdMob, IP 기반) | 광고 또는 마케팅 | 필수 |
- 연락처, 사진, 정밀 위치, 결제정보: 수집 안 함
- **광고 ID 선언**: Play Console > 앱 콘텐츠 > 광고 ID → '예' (매니페스트에 AD_ID 권한 포함, 목적: 광고 또는 마케팅)

## 5-1. AdMob
- AdMob에서 '네온 레인'(Android, `com.jackeychankey.neonlane`) 앱을 새로 만들고 전면·배너 단위 ID를 발급 (다른 앱 ID 재사용 금지).
- 앱 ID → `_android/neon-lane-app/android/ads.properties`, 단위 ID → `games/neon-lane/ads-config.js`.
- 노출: 3번째 게임오버부터 3판마다 전면광고(최소 90초 간격), 결과화면 배너. 플레이 중 광고 없음.
- 앱이 스토어에 게시되면 AdMob > 앱 > 스토어 연결(app-ads.txt 는 선택).

## 6. 앱 서명 / 업로드
- Play App Signing 사용(권장). 업로드 키 SHA-256: `BD:E2:16:D9:36:9B:01:55:7C:74:05:1E:C5:75:31:A4:6A:DC:DC:A5:D8:85:9B:BC:E3:E3:1A:47:58:FC:CF:3B`
- 업로드 키스토어(`neonlane-upload.jks`)와 비밀번호는 저장소에 올리지 말고 별도 백업. 분실 시 Play 지원을 통해 업로드 키 재설정 필요.
- 광고 포함 빌드는 Capacitor 프로젝트(`_android/neon-lane-app/`, README-빌드.md)로 만든다. 이전 SDK 없는 수동 빌드(`_android/neon-lane/`)는 광고 미포함 버전이라 더 이상 사용하지 않는다. 업로드마다 `android/app/build.gradle`의 versionCode를 올릴 것.

## 7. 출시 전 체크
- 개인 개발자 계정(신규): 비공개 테스트 12명 이상 × 14일 후 프로덕션 신청 가능.
- Firestore 규칙/인덱스 배포: `firebase deploy --only firestore:rules,firestore:indexes --project orbit-hop-jackey`, 익명 인증 사용 설정 확인.
- Firebase API 키에 HTTP 리퍼러 제한이 있으면 앱 WebView 출처(`appassets.androidplatform.net`)를 허용 목록에 추가.
- 실기기 테스트: `neonlane-test.apk` 설치 후 홈 → 게임 → 랭킹 → 뒤로가기 → 공유 확인.
