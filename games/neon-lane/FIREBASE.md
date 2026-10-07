# 네온 레인 · Firebase 랭킹 설정 (전용 Firebase 프로젝트)

Firestore + 익명 로그인. `firebase-config.js` 가 비어 있으면 이 기기 로컬 랭킹으로 동작합니다.

## 데이터 구조 (컬렉션 `neonlane_scores`)
- 문서 ID `<board>__<uid>` (플레이어당 보드별 1행), 필드 `{ board, name, score, dist, combo, day, ts }`
- board `dYYYYMMDD` — 일일 랭킹(오늘의 곡) / board `all` — 명예의 전당(역대 최고)
- 일일 챔피언 = 최근 14일 각 일일 보드의 1위. 정렬: 점수 내림차순, 같으면 먼저 달성한 사람
- 프리 플레이는 랭킹에 반영되지 않습니다

## 설정 순서 (콘솔에서)
1. **프로젝트 만들기**: Firebase 콘솔 → 프로젝트 추가 (예: `neon-lane`). Analytics 는 꺼도 됩니다.
2. **Firestore Database** → 데이터베이스 만들기 → 위치 `asia-northeast3 (서울)` → 프로덕션 모드.
3. **Authentication** → 시작하기 → 로그인 방법 → **익명** 사용 설정.
4. **규칙**: Firestore → 규칙 탭에 이 폴더의 `firestore.rules` 전체를 붙여넣고 게시.
5. **인덱스**: Firestore → 인덱스 → 복합 → 추가: 컬렉션 `neonlane_scores`, `board` 오름차순 / `score` 내림차순 / `ts` 오름차순, 쿼리 범위 "컬렉션".
   (또는 `firebase deploy --only firestore:rules,firestore:indexes --project <프로젝트ID>`)
6. **웹 앱 등록**: 프로젝트 설정 → 일반 → 내 앱 → 웹(</>) 앱 추가 → 표시되는 `firebaseConfig` 의
   `apiKey`, `authDomain`, `projectId`, `appId` 를 `firebase-config.js` 에 입력.

## 한계
- 익명 로그인 + 클라이언트가 보내는 점수라서, 규칙의 범위(점수는 거리 대비 상한 이하, 본인 행만, 더 높은 점수로만 갱신, 삭제 불가) 안에서 조작된 점수까지는 막지 못합니다. 엄격한 방지는 서버(Cloud Functions)에서 검증해야 합니다.
- `day` 는 기기 날짜 기준입니다. 앱 데이터를 지우면 새 익명 uid 가 되어 이전 기록과 연결되지 않습니다.
