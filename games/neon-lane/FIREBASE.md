# 네온 레인 · Firebase 랭킹 설정 (Orbit Hop 프로젝트 `orbit-hop-jackey` 공유)

Orbit Hop 과 같은 방식(Firestore + 익명 로그인)입니다. 설정이 비어 있으면 이 기기 로컬 랭킹으로 동작합니다.

## 데이터 구조 (컬렉션 `neonlane_scores` — Orbit Hop 의 `scores` 와 분리되어 서로 영향이 없습니다)
- 문서 ID `<board>__<uid>` (플레이어당 보드별 1행), 필드 `{ board, name, score, dist, combo, day, ts }`
- board `dYYYYMMDD` — 일일 랭킹(오늘의 곡, 모두 같은 곡) / board `all` — 명예의 전당(역대 최고)
- 일일 챔피언 = 최근 14일 각 일일 보드의 1위. 정렬: 점수 내림차순, 같으면 먼저 달성한 사람
- 프리 플레이는 랭킹에 반영되지 않습니다

## 설정 순서
1. **`firebase-config.js`**: Orbit Hop 의 `.env.local` 에 있는 `VITE_FIREBASE_*` 4개 값(apiKey, authDomain, projectId, appId)을 옮겨 적고 배포
2. **규칙 게시** — ⚠️ Orbit Hop 의 현재 규칙은 `scores` 외 컬렉션을 모두 막고 있어서, 규칙을 게시하기 전에는 네온 레인의 서버 저장이 거부됩니다(앱은 이 기기 기록으로 자동 폴백).
   이 폴더의 `firestore.rules` 는 **Orbit Hop 의 기존 규칙 + 네온 레인 블록**을 합친 파일입니다. Orbit Hop 의 `firebase/firestore.rules` 에 덮어쓴 뒤:
   ```bash
   firebase deploy --only firestore:rules,firestore:indexes --project orbit-hop-jackey
   ```
   (또는 콘솔 → Firestore → 규칙에 붙여넣고 게시)
3. **인덱스**: `firestore.indexes.json` 은 Orbit Hop 의 기존 인덱스 + 네온 레인 인덱스(`neonlane_scores`: board↑ score↓ ts↑)를 합친 파일입니다. 위 deploy 로 함께 올라갑니다.
4. 콘솔에서 Authentication 의 **익명 로그인**이 켜져 있는지 확인 (Orbit Hop 에서 이미 켰다면 그대로)

## 한계
- Orbit Hop 과 같습니다: 익명 로그인 + 클라이언트가 보내는 점수라서, 규칙의 범위(점수는 거리 대비 상한 이하, 본인 행만, 더 높은 점수로만 갱신, 삭제 불가) 안에서 조작된 점수까지는 막지 못합니다. 엄격한 방지는 서버(Cloud Functions)에서 검증해야 합니다.
- `day` 는 기기 날짜 기준입니다. 앱 데이터를 지우면 새 익명 uid 가 되어 이전 기록과 연결되지 않습니다.
