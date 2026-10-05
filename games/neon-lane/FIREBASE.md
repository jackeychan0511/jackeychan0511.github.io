# 네온 레인 · Firebase 랭킹 설정

랭킹(일일 랭킹 · 명예의 전당)은 Firestore + 익명 로그인으로 동작합니다. 설정이 없으면 이 기기 로컬 랭킹으로 동작합니다.

## 1. Firebase 콘솔에서 (Orbit Hop과 같은 프로젝트를 써도 됩니다)
1. **Authentication → Sign-in method → 익명(Anonymous)** 사용 설정
2. **Firestore Database** 생성 (이미 있다면 그대로 사용)
3. **Firestore → 규칙**: `firestore.rules` 의 `neonlane_*` 3개 `match` 블록(와 위의 `okName`/`okRec` 함수)을 기존 규칙에 추가하고 게시
4. **Firestore → 인덱스**: 아래 복합 인덱스 생성 (또는 `firebase deploy --only firestore:indexes` 로 `firestore.indexes.json` 배포)
   - 컬렉션 `neonlane_scores` · `day` 오름차순 + `score` 내림차순
   - (처음 일일 랭킹을 열면 콘솔에 인덱스 생성 링크가 오류로 뜨기도 합니다)
5. **프로젝트 설정 → 내 앱 → 웹 앱** 의 SDK 설정값 복사

## 2. 이 저장소에서
`firebase-config.js` 의 `window.NL_FIREBASE = null;` 을 복사한 설정값(apiKey, authDomain, projectId, appId)으로 바꾸고 배포합니다.

## 데이터 구조
| 컬렉션 | 문서 ID | 용도 |
|---|---|---|
| `neonlane_scores` | `{day}_{uid}` | 일일 랭킹(플레이어당 하루 최고 점수) |
| `neonlane_hof` | `{uid}` | 명예의 전당 · 역대 최고 기록 TOP 20 |
| `neonlane_champs` | `{day}` | 일일 챔피언 (그날 최고 점수) |

문서 필드: `uid, name, score, dist, combo, day(YYYYMMDD), ts`

## 한계
- 클라이언트가 점수를 보내는 구조라 규칙(범위·본인 문서·점수 증가만 허용)으로 막을 수 있는 건 일부입니다. 규칙의 상한 안에서 조작된 점수까지는 막지 못합니다. 엄격한 방지가 필요하면 Cloud Functions 로 서버에서 검증(리플레이 검증 등)하세요.
- `day` 는 기기 날짜 기준입니다(시계를 바꾸면 다른 날로 제출 가능).
- 익명 로그인이라 앱 데이터를 지우면 새 uid 가 되어 이전 기록과 연결되지 않습니다.
