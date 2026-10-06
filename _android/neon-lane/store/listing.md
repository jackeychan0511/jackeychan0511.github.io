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
- 광고 없음, 결제 없음
- 가벼운 용량, 세로 화면 한 손 플레이

team. Jackeychankey
```
- **최근 변경사항(릴리스 노트)**: `첫 출시! 네온 레인에서 4색 레인을 달려 보세요.`

## 2. English (선택, en-US)
- **Title**: Neon Lane - 4 Color Rhythm Runner
- **Short**: Run 4 neon lanes, jump to the beat, climb the daily leaderboard!
- **Full**: Run through four glowing lanes (red, orange, yellow, green) in sync with the music. Move left/right, jump over note blocks, and chain PERFECT hits. Surprise events rotate or flip the screen — and your controls with it. Compete on the daily ranking and the Hall of Fame. No ads, no in-app purchases, no sign-up. — team. Jackeychankey

## 3. 그래픽 자산 (이 폴더)
- 앱 아이콘 512×512: `icon-512.png`
- 그래픽 이미지 1024×500: `feature-graphic-1024x500.png`
- 휴대전화 스크린샷 1080×1920: `screenshots/` (최소 2장, 권장 4~8장)

## 4. 정책 / 앱 콘텐츠
- **개인정보처리방침 URL**: https://jackeychan0511.github.io/games/neon-lane/privacy.html (Pages에 머지·배포된 후 유효)
- **앱 액세스**: 로그인 불필요 (제한 없음)
- **광고**: 없음 · **인앱 결제**: 없음
- **대상 연령**: 13세 이상 (13세 미만 아님)
- **콘텐츠 등급(IARC)**: 폭력·성적 콘텐츠·도박·약물 없음. 사용자 생성 콘텐츠 = 닉네임이 랭킹에 공개(채팅/메시징 없음). 위치 공유 없음. 예상 등급: 전체이용가 계열(UGC 문항 응답에 따라 달라질 수 있음).
- **카테고리**: 게임 > 음악 (또는 아케이드) · 태그: 리듬, 아케이드
- **뉴스 앱 / 코로나 / 정부 앱 / 금융 기능 / 건강 앱**: 모두 해당 없음

## 5. 데이터 보안 (Data safety)
- 데이터 수집: **예** / 데이터 공유(제3자): **아니요** (Firebase는 서비스 제공자로 처리)
- 전송 중 암호화: **예** (HTTPS) · 삭제 요청 방법 제공: **예** (privacy.html 참고)
- 수집 항목:
  | 범주 | 항목 | 목적 | 필수 여부 |
  |---|---|---|---|
  | 개인 정보 | 이름(닉네임) | 앱 기능(랭킹) | 선택 아님(랭킹 이용 시) |
  | 기기 또는 기타 ID | 익명 Firebase 사용자 ID | 앱 기능, 부정 방지 | 필수 |
  | 앱 활동 | 기타 작업(점수·거리·콤보) | 앱 기능(랭킹) | 필수 |
- 위치, 연락처, 사진, 결제정보, 광고 ID: 수집 안 함

## 6. 앱 서명 / 업로드
- Play App Signing 사용(권장). 업로드 키 SHA-256: `BD:E2:16:D9:36:9B:01:55:7C:74:05:1E:C5:75:31:A4:6A:DC:DC:A5:D8:85:9B:BC:E3:E3:1A:47:58:FC:CF:3B`
- 업로드 키스토어(`neonlane-upload.jks`)와 비밀번호는 저장소에 올리지 말고 별도 백업. 분실 시 Play 지원을 통해 업로드 키 재설정 필요.
- 다음 업로드부터는 `VERSION_CODE`를 올려 `build-aab.sh` 로 재빌드.

## 7. 출시 전 체크
- 개인 개발자 계정(신규): 비공개 테스트 12명 이상 × 14일 후 프로덕션 신청 가능.
- Firestore 규칙/인덱스 배포: `firebase deploy --only firestore:rules,firestore:indexes --project orbit-hop-jackey`, 익명 인증 사용 설정 확인.
- Firebase API 키에 HTTP 리퍼러 제한이 있으면 앱 WebView 출처(`appassets.androidplatform.net`)를 허용 목록에 추가.
- 실기기 테스트: `neonlane-test.apk` 설치 후 홈 → 게임 → 랭킹 → 뒤로가기 → 공유 확인.
