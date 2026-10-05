/* 네온 레인 Firebase 설정 (웹 앱 설정값은 공개되어도 되는 값이며, 보안은 firestore.rules 가 담당합니다)
 * Firebase 콘솔 → 프로젝트 설정 → 일반 → 내 앱(웹) → SDK 설정 및 구성 의 값을 아래에 붙여 넣으세요.
 * null 이면 이 기기에만 저장되는 로컬 랭킹으로 동작합니다. 자세한 설정 순서는 FIREBASE.md 참고. */
window.NL_FIREBASE = null;
/* 예:
window.NL_FIREBASE = {
  apiKey: "AIza...",
  authDomain: "your-project.firebaseapp.com",
  projectId: "your-project",
  appId: "1:1234567890:web:abcdef"
};
*/
