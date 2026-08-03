---
layout: post
title: "Flutter로 'AI 선생님' 앱 만들기 개발일지 — 아이 문제 풀이 앱을 8단계로 완성한 과정 (코드 공유)"
date: 2026-08-03 10:00:00 +0900
categories: [ai-teacher]
tags: [Flutter, 개발일지, AI, 앱개발, 자녀교육, DeepSeek, Gemini, 안드로이드, 코딩, 문제풀이, 앱개발후기, 2026년8월]
author: "40대 블로거"
description: "아이가 모르는 문제를 물어보면 AI가 학년에 맞춰 풀이해주는 'AI 선생님' Flutter 앱을 만든 개발일지입니다. 기획부터 하이브리드 AI 연동, 답변 잘림 버그 수정, 테스트와 APK 배포까지 8단계로 정리했습니다."
---

아까 올린 [Flutter로 만든 'AI 선생님' 앱 — 문제 찍으면 풀이해주는 자녀교육 앱](/2026/08/03/ai-teacher-flutter-app/) 글에서는 완성된 앱의 **핵심 코드**를 소개해드렸는데요.

이번 글은 그 앱을 **어떻게 만들었는지, 어떤 문제를 만나고 어떻게 해결했는지**를 단계별로 정리한 **개발일지**입니다. 혼자서 Flutter 앱을 만들어보고 싶은 분들, 특히 "AI API를 앱에 붙여보고 싶은데 뭘 어떻게 시작해야 할지 모르겠다"는 분들께 도움이 되었으면 좋겠습니다.

---

## 🎯 개발 동기 — "평범한 아빠의 상상에서 시작"

세상에는 늘 다르게 보는 사람들이 있습니다. 틀에 박힌 답보다 새로운 질문을 던지는 사람, 남들이 지나치는 불편함에서 기회를 찾는 사람, "이건 안 될 거야"라는 말 대신 "한번 해보자"라고 말하는 사람입니다.

두 아들을 둔 평범한 아빠로서, 아이가 모르는 문제를 물어볼 때마다 **"이걸 어떻게 초등학생 눈높이로 설명하지?"**라는 벽에 부딪히곤 했습니다. 그리고 이런 생각이 들었습니다.

> 화면 너머의 선생님이 될 수 있다면? 아이가 궁금할 때 언제든 눈높이에 맞춰 설명해주는 AI를 만들어주면 어떨까?

AI 시대에 아이들이 AI를 자연스럽고 익숙하게 접하는 것도 중요하다고 생각했습니다. 그래서 시작한 것이 바로 **"AI 선생님"** 프로젝트입니다.

---

## 📋 이 글을 읽으면 알 수 있는 것

| 단계 | 내용 |
|:----|:----|
| 0단계 | 기획 — 어떤 앱을 만들까 (하이브리드 AI 아이디어) |
| 1단계 | 프로젝트 세팅과 테마 |
| 2단계 | 텍스트 질문 → DeepSeek 연동 |
| 3단계 | 사진 질문 → Gemini 비전 연동 (라우팅 완성) |
| 4단계 | 질문/답변 화면 UI |
| 5단계 | API 키 영구저장 (shared_preferences) |
| 6단계 | 사용자 요청 개선 4가지 |
| 7단계 | ⚠️ 답변 잘림 버그 — 가장 고생한 순간 |
| 8단계 | 테스트 17/17 + APK 배포 |

---

## 🔧 개발 환경

- **Flutter** 3.44.8 stable (Dart 3.12.2)
- **JDK** Temurin 17
- **Android SDK** API 35/36
- 사용 패키지: `http`, `provider`, `shared_preferences`, `image_picker`, `flutter_markdown`

---

## 0단계. 기획 — "아이가 모르는 문제를 AI가 풀어주면?"

아이가 모르는 문제를 물어볼 때, 직접 설명해주려다 보면 **"이걸 어떻게 초등학생 눈높이로 설명하지?"**라는 벽에 부딪히곤 합니다. 그래서 떠올린 아이디어:

> **학년별로 설명 수준이 달라지는 AI 과외 선생님 앱**

기획에서 가장 중요했던 결정이 하나 있었는데, 바로 **어떤 AI를 쓸까**였습니다.

### 💡 핵심 아이디어: 하이브리드 AI 라우팅

사실 처음에는 AI를 하나만 쓰려고 했습니다. 그런데 고민이 생겼어요.

- **텍스트 질문** → 답변이 빠르고 비용이 저렴한 모델이 좋다
- **사진 질문** → 이미지를 읽을 수 있는 비전(vision) 모델이 필요하다

그래서 **질문 형태에 따라 AI를 자동으로 골라 쓰는 "하이브리드 라우팅"** 구조를 채택했습니다.

| 질문 방식 | 사용 AI | 선택 이유 |
|:----|:----|:----|
| 📝 텍스트 | **DeepSeek** (`deepseek-v4-flash`) | 빠르고 저렴, OpenAI 호환 API라 연동 쉬움 |
| 📷 사진 | **Gemini** (`gemini-2.5-flash`) | 이미지 인식(비전) 지원 |

![Flutter 로고](/assets/images/posts/ai-teacher/flutter-logo.png)
*Flutter — 하나의 코드로 Android/iOS 대응 (출처: Wikimedia Commons)*

![DeepSeek 로고](/assets/images/posts/ai-teacher/deepseek-logo.svg)
*DeepSeek — 텍스트 질문용 AI (출처: Wikimedia Commons)*

![Gemini 로고](/assets/images/posts/ai-teacher/gemini-logo.svg)
*Google Gemini — 사진(비전) 질문용 AI (출처: Wikimedia Commons)*

> 나중에 알고 보니 이 선택이 **답변 잘림 버그**(7단계)를 만났을 때도 큰 도움이 됐습니다.

---

## 1단계. 프로젝트 세팅 — 테마부터 색깔 있게

`flutter create`로 프로젝트를 만들고, 필요한 패키지를 추가했습니다.

```yaml
# pubspec.yaml
dependencies:
  http: ^1.2.0            # REST API 호출
  provider: ^6.1.0        # 상태 관리
  shared_preferences: ^2.5.3  # API 키 등 설정 영구저장
  image_picker: ^1.1.0    # 사진 촬영/갤러리 선택
  flutter_markdown: ^0.7.0 # AI 답변(마크다운) 렌더링
```

앱 이름은 **"AI 선생님"**, 테마는 아이들이 좋아할 만한 **밝은 파란색(#4F6BED)**으로 정했습니다.

```dart
theme: ThemeData(
  colorScheme: ColorScheme.fromSeed(
    seedColor: const Color(0xFF4F6BED),
  ),
  useMaterial3: true,
),
```

---

## 2단계. 텍스트 질문 → DeepSeek 연동

가장 먼저 **텍스트 질문**부터 만들었습니다. DeepSeek는 OpenAI 호환 API라 `chat/completions` 엔드포인트를 그대로 사용할 수 있어 연동이 쉬웠습니다.

```dart
// ─── 핵심 구현 (DeepSeek API 호출부) ───
// chat/completions 엔드포인트 호출 (OpenAI 호환)
// 학년 맞춤 시스템 프롬프트 + 사용자 질문 전송
// (전체 코드는 공개하지 않습니다 🙏)
```

### 🎓 학년 맞춤 프롬프트 — 이 앱의 차별점

단순히 "풀이해줘"라고 시키는 게 아니라, **학년에 따라 설명 수준이 달라지도록** 시스템 프롬프트를 만들었습니다. 초5~고3까지 8개 학년을 지원합니다.

```dart
const List<String> kGrades = ['초5', '초6', '중1', '중2', '중3', '고1', '고2', '고3'];

String _gradeLevelNote(String grade) {
  if (grade.startsWith('초')) {
    return '초등학생($grade) 눈높이에 맞춰 아주 쉽고 친근하게, 일상생활 예시를 곁들여 설명하세요.';
  }
  if (grade.startsWith('중')) {
    return '중학생($grade) 눈높이에 맞춰 쉬운 말로 설명하세요.';
  }
  return '고등학생($grade) 수준에 맞춰 개념을 정확하고 간결하게 설명하세요.';
}
```

답변 형식도 고정했습니다. AI가 아무렇게나 답하면 아이가 보기 어려우니까요.

> **문제 정리 → 단계별 풀이(왜 그렇게 하는지 이유 포함) → 개념 정리 → 답(굵은 글씨)**

---

## 3단계. 사진 질문 → Gemini 비전 연동 (라우팅 완성)

텍스트가 되니 이제 **사진으로 문제를 찍어 올리는 기능**을 추가했습니다. `image_picker`로 사진을 고르면, `base64`로 인코딩해서 Gemini의 `generateContent`에 `inline_data`로 넘겨주는 방식입니다.

```dart
// ─── 핵심 구현 (Gemini 비전 호출부) ───
// generateContent 엔드포인트 호출
// 사진은 base64 인코딩 → inline_data로 전송
// system_instruction에 학년 맞춤 프롬프트 주입
// (전체 코드는 공개하지 않습니다 🙏)
```

### 🚦 라우팅 판정 — 사진이 있으면 Gemini, 없으면 DeepSeek

이제 두 AI가 준비됐으니, **어느 쪽으로 보낼지 판정하는 로직**을 넣었습니다.

```dart
if (question.hasImage) {
  // 사진 질문 → Gemini (비전)
  final service = AiService(apiKey: _geminiApiKey, model: _geminiModel, grade: _grade);
  _answer = await service.ask(question);
} else {
  // 텍스트 질문 → DeepSeek
  final service = DeepSeekService(apiKey: _deepSeekApiKey, grade: _grade);
  _answer = await service.ask(question);
}
```

이렇게 **"사진이 있으면 Gemini, 텍스트만 있으면 DeepSeek"** 자동 라우팅이 완성됐습니다.

---

## 4단계. UI — 질문 화면과 답변 화면

### 질문 화면

큰 텍스트 입력창 + 사진 첨부 버튼 + 질문하기 버튼으로 구성했습니다. 사진은 **카메라 촬영 / 갤러리 선택** 둘 다 지원합니다.

```dart
TextField(
  controller: _textController,
  maxLines: 5,
  decoration: const InputDecoration(
    hintText: '예) 2x + 5 = 13일 때 x의 값을 구하시오.\n또는 사진으로 문제를 찍어 올려도 돼요!',
    border: OutlineInputBorder(),
  ),
),
```

### 답변 화면

AI가 마크다운으로 답변하니까, `flutter_markdown`으로 **굵은 글씨·목록·수식이 그대로 살아있는 풀이 카드**를 만들었습니다. 선택해서 복사도 가능하게 했습니다.

```dart
MarkdownBody(
  data: answer,
  selectable: true,
  styleSheet: MarkdownStyleSheet.fromTheme(Theme.of(context)),
)
```

---

## 5단계. API 키 영구저장 — shared_preferences

앱이 완성돼 가니 **API 키를 매번 입력하는 문제**가 생겼습니다. 설정 화면에서 입력한 키를 `shared_preferences`에 저장해서, **앱을 껐다 켜도 유지되도록** 했습니다.

```dart
Future<void> saveSettings() async {
  final prefs = await SharedPreferences.getInstance();
  await prefs.setString('deepseek_api_key', _deepSeekApiKey);
  await prefs.setString('gemini_api_key', _geminiApiKey);
  await prefs.setString('grade', _grade);
}
```

학년 선택도 설정 화면에 드롭다운으로 넣었습니다. 기본값은 **중2**입니다.

> ⚠️ 참고: API 키는 절대 코드에 하드코딩하면 안 됩니다. 이 앱처럼 **사용자가 직접 입력하고 기기에 저장**하는 구조가 안전해요.

---

## 6단계. 사용자 요청 개선 4가지

앱을 실제로 쓰다 보니 개선 요청이 하나둘 나왔습니다. 순서대로 반영했어요.

| # | 요청 | 반영 내용 |
|:--|:----|:----|
| 1 | 과목 선택이 번거롭다 | **과목 선택 UI 제거** — 모든 과목 질문 허용 |
| 2 | 앱을 껐다 켜면 설정이 사라진다 | **API 키·학년 영구저장** (5단계) |
| 3 | 이전 문제 사진이 다음 질문에 남는다 | 답변 화면에서 돌아오면 **사진 첨부 자동 초기화** |
| 4 | 예전 답변을 다시 보고 싶다 | **질문 히스토리 화면 추가** (아이콘 버튼) |

특히 3번은 작지만 UX에 큰 영향을 주는 버그였습니다. 다음 문제를 풀 때 이전 사진이 계속 붙어 있으면, **"왜 이 사진이 또 보이지?"** 하면서 헷갈리니까요. 답변 화면에서 돌아올 때 이미지를 비워주는 코드 한 줄로 해결했습니다.

```dart
// 답변 화면에서 돌아오면 사진 첨부를 초기화해서
// 다음 문제에 이전 사진이 남지 않게 한다.
appState.setQuestionImage(null);
```

---

## 7단계. ⚠️ 답변 잘림 버그 — 가장 고생한 순간

개발일지에서 빼놓을 수 없는, **이번 프로젝트에서 가장 고생한 문제**입니다.

### 증상

긴 풀이를 요청하면 **답변이 중간에 뚝 끊기는** 현상이 발생했습니다. "단계별 풀이"를 설명하다가 갑자기 문장이 끝나버리는 거죠. 처음에는 AI 모델 문제인 줄 알았는데...

### 원인 분석

코드를 들여다보니 원인이 두 개 겹쳐 있었습니다.

1. **`max_tokens` 4096 한도** — 처음에는 토큰 한도를 4096으로 설정했는데, 학년별 상세 풀이는 이걸 자주 넘어갔습니다.
2. **`finish_reason` 미검사** — 응답이 끊겼는지 확인하는 필드(`finish_reason` / `finishReason`)를 전혀 검사하지 않고 있었습니다. 즉 **"답변이 잘렸다는 사실 자체를 모르고 있었던"** 겁니다.

### 해결

1. **토큰 한도 상향** — `max_tokens`를 8192로 두 배 늘렸습니다.
2. **잘림 감지** — 응답의 `finish_reason`이 `'length'`(DeepSeek) 또는 `'MAX_TOKENS'`(Gemini)면 잘렸다고 판단.
3. **자동 이어받기** — 잘렸으면 지금까지의 답변을 대화 컨텍스트로 유지한 채 **"끊긴 부분부터 이어서 설명해줘"** 를 다시 요청. 최대 4회까지 반복.
4. **안전장치** — 그래도 다 못 채우면 마지막에 *"💡 답변이 길어 일부만 표시됐어요"* 안내 문구를 붙여서, 사용자가 이해할 수 있게 했습니다.

```dart
// 잘렸는지 확인: 'length'면 토큰 한도에 걸려 끊긴 것
final finishReason = choices[0]['finish_reason'] as String?;
if (finishReason != 'length') {
  truncated = false;
  break;
}
truncated = true;

// ─── 핵심 구현 (이어서 생성 로직) ───
// 지금까지의 답변을 대화 컨텍스트로 유지한 채
// "끊긴 부분부터 이어서 설명해줘" 재요청 (최대 4회)
// (전체 코드는 공개하지 않습니다 🙏)
```

> 💡 **배운 점**: AI 응답은 "성공했다"는 것만 확인하면 안 됩니다. **"제대로 완성됐는지"** 까지 확인해야 해요. 응답이 잘렸는지, 비어 있는지, 에러인지 — 모든 경우를 코드로 검사하는 습관이 중요하다는 걸 배웠습니다.

---

## 8단계. 테스트 17/17 + APK 배포

### 회귀 테스트로 버그 재발 방지

잘림 수정을 한 뒤, **이어받기 로직 전용 테스트 5건**을 추가했습니다. 앞으로 코드를 고쳐도 이 기능이 다시 망가지지 않도록요.

- `flutter analyze` → **No issues found!**
- `flutter test` → **17/17 통과** ✅
  - 이어받기 동작 테스트 (잘림 → 이어받기 → 완성)
  - AI 라우팅 테스트 (텍스트/사진 분기)
  - 설정 영구저장 테스트

### APK 빌드 & 실기기 설치

```bash
flutter build apk --release
# 결과: build/app/outputs/flutter-apk/app-release.apk (약 52MB)
```

빌드된 APK를 **실제 폰에 설치해서** 텍스트 질문과 사진 질문을 모두 테스트했습니다. 사진으로 문제 찍어서 풀이받는 장면이 나올 때가 가장 뿌듯했어요.

---

## 🎉 마무리 — 이번 프로젝트에서 배운 것

1. **AI는 "하나"일 필요가 없다** — 용도별로 다른 AI를 쓰는 하이브리드 구조가 성능과 비용 면에서 훨씬 효율적입니다.
2. **프롬프트가 곧 제품** — 같은 AI여도 학년별 프롬프트를 넣으니 설명 수준이 완전히 달라졌습니다.
3. **에러 처리는 "실패"만이 아니라 "불완전한 성공"도 잡아야 한다** — 이번 프로젝트의 최대 교훈입니다.
4. **회귀 테스트는 투자다** — 고생해서 고친 버그는 테스트로 영원히 묶어둬야 합니다.

혼자서 Flutter + AI 앱을 만들어보고 싶다면, 이 글의 단계를 하나씩 따라 해보시길 추천합니다. **완성된 앱의 전체 코드와 화면 구성**이 궁금하시면 [아까 올린 앱 소개 글](/2026/08/03/ai-teacher-flutter-app/)을 함께 봐주세요! 🙌

질문이나 궁금한 점은 댓글로 남겨주세요.

---

<p style="color:#000000; font-weight:bold; margin-top:24px;">개발자: 심종주 (2026.08.03 기준)</p>
