/* 네온 레인 Firebase 설정 — Orbit Hop 과 같은 프로젝트(orbit-hop-jackey)를 사용합니다.
 * Orbit Hop 폴더의 .env.local 에 있는 VITE_FIREBASE_* 4개 값을 아래에 그대로 옮겨 적으세요
 * (웹 앱 설정값은 앱에 그대로 포함되는 공개 값이며, 접근 제어는 firestore.rules 가 담당합니다).
 * null 이면 이 기기에만 저장되는 로컬 랭킹으로 동작합니다. 설정 순서는 FIREBASE.md 참고. */
window.NL_FIREBASE = null;
/* 값을 넣은 예:
window.NL_FIREBASE = {
  apiKey:     "VITE_FIREBASE_API_KEY 값",
  authDomain: "VITE_FIREBASE_AUTH_DOMAIN 값",
  projectId:  "VITE_FIREBASE_PROJECT_ID 값",
  appId:      "VITE_FIREBASE_APP_ID 값"
};
*/
