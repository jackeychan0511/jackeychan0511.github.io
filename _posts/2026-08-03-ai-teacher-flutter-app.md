---
layout: post
title: "Flutter로 만든 'AI 선생님' 앱 — 문제 찍으면 풀이해주는 자녀교육 앱 (코드 공유)"
date: 2026-08-03 09:30:00 +0900
categories: [ai-teacher]
tags: [Flutter, AI, 자녀교육, 문제풀이, 앱개발, DeepSeek, Gemini, 안드로이드, 학습앱, 코딩, 2026년8월]
author: "40대 블로거"
description: "아이가 모르는 문제를 텍스트나 사진으로 올리면 AI가 학년(초5~고3)에 맞춰 풀이해주는 Flutter 앱 'AI 선생님'을 만들었습니다. 텍스트는 DeepSeek, 사진은 Gemini로 자동 라우팅하는 하이브리드 구조와 핵심 코드를 공유합니다."
---

요즘 저처럼 아이가 모르는 문제를 물어볼 때마다 \"어떻게 설명해줘야 쉽게 이해할까\" 고민하시는 분들, 계시죠?

아이에게 **AI 선생님**을 만들어주면 어떨까 싶어서, Flutter로 **문제 풀이를 도와주는 안드로이드 앱**을 하나 만들어봤습니다. 문제를 텍스트로 쓰거나, 사진으로 찍어 올리면 AI가 **학년에 맞춰 단계별로 풀이**를 설명해줍니다.

이 글에서는 실제 만든 앱의 핵심 코드를 공유합니다. (API 키 설정 화면 등 개인정보 관련 부분은 생략했어요.)

> 자녀(또는 학생)가 모르는 문제를 물어보면, AI 선생님이 **학년에 맞춰 단계별로 풀이**를 설명해주는 Android 앱을 Flutter로 만들었습니다.
> 이 글에서는 앱의 핵심인 **"물어보고 대답받는"** 부분만 소개합니다. (API 키 설정 화면 등은 생략)

---

## ✨ 어떤 앱인가요?

- 📝 **텍스트 질문** → 문제를 입력하면 AI가 풀이
- 📷 **사진 질문** → 문제 사진을 찍어 올리면 AI가 읽고 풀이
- 🎓 **학년 맞춤 설명** → 초5~고3 중 선택한 학년 눈높이로 설명
- 🧮 **형식화된 답변** → 문제 정리 → 단계별 풀이 → 개념 정리 → 답

### 핵심 아이디어: 하이브리드 AI 라우팅

| 질문 방식 | 사용 AI | 이유 |
|-----------|---------|------|
| 텍스트 | **DeepSeek** (deepseek-v4-flash) | 저렴하고 빠름 |
| 사진 | **Gemini** (gemini-2.5-flash) | 이미지 인식(비전) 지원 |

같은 앱 안에서 질문 형태에 따라 AI를 자동으로 골라 쓰는 구조입니다.

![Flutter 로고](/assets/images/posts/ai-teacher/flutter-logo.png)
*Flutter — 크로스 플랫폼 앱 개발 프레임워크 (출처: Wikimedia Commons)*

---

## 🛠 기술 스택

- **Flutter** (Material 3, 파란색 테마 `#4F6BED`)
- **http** — REST API 호출
- **flutter_markdown** — AI 답변(마크다운) 렌더링
- **image_picker** — 사진 촬영/갤러리 선택

---

## 📂 프로젝트 구조 (핵심만)

```
lib/
├── main.dart                    # 앱 진입점
├── models/
│   └── question.dart            # 질문/답변 모델
├── services/
│   └── ai_service.dart          # ★ 하이브리드 AI 라우팅 (핵심)
└── screens/
    ├── home_screen.dart         # 질문 입력 화면
    └── answer_screen.dart       # 답변 표시 화면
```

---

## 🚀 1. 앱 진입점 (`main.dart`)

```dart
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import 'screens/home_screen.dart';
import 'services/app_state.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  // 저장된 설정(API 키, 학년)을 기기에서 불러온다.
  final appState = AppState();
  await appState.loadSettings();
  runApp(AiTeacherApp(appState: appState));
}

class AiTeacherApp extends StatelessWidget {
  final AppState appState;

  const AiTeacherApp({super.key, required this.appState});

  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
      create: (_) => appState,
      child: MaterialApp(
        title: 'AI 선생님',
        debugShowCheckedModeBanner: false,
        theme: ThemeData(
          colorScheme: ColorScheme.fromSeed(
            seedColor: const Color(0xFF4F6BED), // 밝고 친근한 파랑
          ),
          useMaterial3: true,
        ),
        home: const HomeScreen(),
      ),
    );
  }
}
```

---

## 📦 2. 질문/답변 모델 (`question.dart`)

```dart
import 'dart:typed_data';

/// 사용자가 올린 질문 (텍스트 또는 사진)
class Question {
  final String text; // 텍스트 질문
  final Uint8List? imageBytes; // 사진 원본 (선택)
  final String? imageMimeType; // image/jpeg 등

  const Question({
    required this.text,
    this.imageBytes,
    this.imageMimeType,
  });

  bool get hasImage => imageBytes != null && imageBytes!.isNotEmpty;

  /// 질문 내용이 하나라도 있는지 (빈 질문 방지)
  bool get isValid => text.trim().isNotEmpty || hasImage;
}

/// AI 선생님의 답변
class Answer {
  final String content; // 마크다운 풀이
  final bool isError; // 에러 여부

  const Answer({required this.content, this.isError = false});
}
```

---

## 🤖 3. ★ 하이브리드 AI 라우팅 (`ai_service.dart`)

이 앱의 심장입니다. **텍스트면 DeepSeek, 사진이면 Gemini**를 자동으로 호출하고,
응답이 토큰 한도에 걸려 잘리면 **이어서 계속 생성**하는 로직까지 들어 있습니다.

### 3-1. 학년별 시스템 프롬프트

```dart
/// 지원 학년 목록 (설정에서 선택)
const List<String> kGrades = ['초5', '초6', '중1', '중2', '중3', '고1', '고2', '고3'];

/// 학년별 설명 수준 안내 문구
String _gradeLevelNote(String grade) {
  if (grade.startsWith('초')) {
    return '초등학생($grade) 눈높이에 맞춰 아주 쉽고 친근하게, 일상생활 예시를 곁들여 설명하세요.';
  }
  if (grade.startsWith('중')) {
    return '중학생($grade) 눈높이에 맞춰 쉬운 말로 설명하세요.';
  }
  return '고등학생($grade) 수준에 맞춰 개념을 정확하고 간결하게 설명하세요.';
}

/// 학년에 맞춘 시스템 프롬프트 생성
String buildTeacherSystemPrompt(String grade) {
  return '''
당신은 $grade 학생을 가르치는 친절한 수학/과학 선생님입니다.
학생이 올린 문제를 선생님처럼 풀이해주세요.

반드시 다음 형식으로 답변하세요:
1. **문제 정리**: 문제를 쉽게 다시 설명하고 무엇을 구하는지 한 문장으로 정리하세요.
2. **단계별 풀이**: 풀이를 단계로 나누어 설명하세요. 각 단계에서 "왜" 그렇게 하는지 이유를 꼭 덧붙이세요.
3. **개념 정리**: 사용된 공식/개념을 ${_gradeLevelNote(grade)}
4. **답**: 마지막에 답을 굵은 글씨로 강조하세요.

규칙:
- 반드시 한국어로 답변하세요.
- 전문 용어는 괄호로 풀이하세요.
- 수식은 `x + 3 = 7`처럼 읽기 쉽게 표기하세요. 분수는 `a/b`, 제곱은 `x^2`, 루트는 `√a` 형태로 쓰세요.
- 정답만 말하지 말고 반드시 풀이 과정을 가르쳐주세요.
''';
}
```

### 3-2. Gemini 호출 (사진/비전 질문용)

```dart
// Gemini API 호출 서비스 (사진/비전 질문용)
class AiService {
  final String apiKey;
  final String model; // 예: gemini-2.5-flash
  final String grade; // 예: 중2, 고3

  AiService({
    required this.apiKey,
    this.model = 'gemini-2.5-flash',
    this.grade = '중2',
  });

  /// 질문을 보내고 풀이를 받아온다.
  /// 응답이 maxOutputTokens 한도에 걸려 잘렸으면(finishReason == 'MAX_TOKENS')
  /// 이전 대화를 컨텍스트로 유지한 채 자동으로 이어서 생성한다.
  Future<String> ask(Question question) async {
    // ─── 핵심 구현 (API 호출부) ───
    // generateContent 엔드포인트 호출
    // 사진은 base64로 인코딩 → inline_data로 전송
    // finishReason == 'MAX_TOKENS' 감지 → 이어서 생성 (최대 4회)
    // (전체 코드는 공개하지 않습니다 🙏)
    return _buildAnswerWithContinuation(question);
  }

  // 잘림 감지 → 자동 이어받기 로직 (최대 4회 반복)
  Future<String> _buildAnswerWithContinuation(Question question) async {
    // ... (이어서 생성하는 핵심 로직 생략)
    throw UnimplementedError('코드 공개 제한');
  }
}
```

### 3-3. DeepSeek 호출 (텍스트 질문용, OpenAI 호환)

```dart
/// DeepSeek API 호출 서비스 (텍스트 질문용, OpenAI 호환)
class DeepSeekService {
  final String apiKey;
  final String model;
  final String grade; // 예: 중2, 고3

  DeepSeekService({
    required this.apiKey,
    this.model = 'deepseek-v4-flash',
    this.grade = '중2',
  });

  /// 텍스트 질문을 보내고 풀이를 받아온다.
  /// 응답이 max_tokens 한도에 걸려 잘렸으면(finish_reason == 'length')
  /// 이전 대화를 컨텍스트로 유지한 채 자동으로 이어서 생성한다.
  Future<String> ask(Question question) async {
    // ─── 핵심 구현 (API 호출부) ───
    // chat/completions 엔드포인트 호출 (OpenAI 호환)
    // finish_reason == 'length' 감지 → 이어서 생성 (최대 4회)
    // (전체 코드는 공개하지 않습니다 🙏)
    return _buildAnswerWithContinuation(question);
  }

  // 잘림 감지 → 자동 이어받기 로직 (최대 4회 반복)
  Future<String> _buildAnswerWithContinuation(Question question) async {
    // ... (이어서 생성하는 핵심 로직 생략)
    throw UnimplementedError('코드 공개 제한');
  }
}

class AiException implements Exception {
  final String message;
  AiException(this.message);

  @override
  String toString() => message;
}
```

> 💡 **잘림 자동 복구**: `finish_reason`/`finishReason`을 검사해서 응답이 토큰 한도에 걸려 끊겼으면, 지금까지의 답변을 대화 컨텍스트에 넣고 "이어서 설명해줘"를 다시 요청합니다. 덕분에 긴 풀이도 중간에 끊기지 않아요.

---

## 🧠 4. 앱 상태 & 라우팅 결정 (`app_state.dart` 핵심)

질문에 사진이 있으면 Gemini로, 텍스트만 있으면 DeepSeek로 보내는 **라우팅 판정**이 여기 있습니다.

```dart
/// 전역 앱 상태 (API 키, 학년, 질문 플로우)
class AppState extends ChangeNotifier {
  String _geminiApiKey = '';
  String _deepSeekApiKey = '';
  String _geminiModel = 'gemini-2.5-flash';
  String _grade = '중2';

  // ─── 핵심 구현 (상태 관리부) ───
  // API 키·학년 영구저장 (shared_preferences)
  // 사진 유무에 따른 라우팅 판정 (사진 → Gemini, 텍스트 → DeepSeek)
  // (전체 코드는 공개하지 않습니다 🙏)
}
```

---

## 🏠 5. 질문 입력 화면 (`home_screen.dart` 핵심)

텍스트 입력 + 사진 첨부 + 질문하기 버튼으로 구성됩니다.

```dart
class _HomeScreenState extends State<HomeScreen> {
  final _picker = ImagePicker();
  final _textController = TextEditingController();

  // ─── 핵심 구현 (질문 입력부) ───
  // 텍스트 입력 + 사진 첨부(카메라/갤러리) + 질문하기 버튼
  // 사진은 maxWidth 1600, quality 85로 압축 후 전송
  // 답변 화면에서 돌아오면 사진 첨부 자동 초기화
  // (전체 코드는 공개하지 않습니다 🙏)
}
```

---

## 📖 6. 답변 표시 화면 (`answer_screen.dart` 핵심)

AI의 마크다운 답변을 카드 형태로 보여줍니다. `flutter_markdown`으로 수식·목록·굵은 글씨가 그대로 렌더링됩니다.

```dart
class AnswerScreen extends StatelessWidget {
  // ─── 핵심 구현 (답변 표시부) ───
  // 로딩 / 에러 / 답변 3가지 상태 처리
  // flutter_markdown으로 AI 답변(마크다운) 렌더링
  // "다른 문제 물어보기" → 상태 초기화 후 홈으로
  // (전체 코드는 공개하지 않습니다 🙏)
}
```

---

## ▶️ 실행 방법

```bash
# 1. 패키지 설치
flutter pub get

# 2. Android 기기에서 실행
flutter run

# 3. 릴리스 APK 빌드
flutter build apk --release
# 결과: build/app/outputs/flutter-apk/app-release.apk
```

### 📲 앱 직접 다운로드 (베타 버전)

> 실제 빌드된 안드로이드 APK를 아래에서 받아서 바로 설치해보실 수 있습니다.

**[⬇️ AI 선생님 APK 다운로드 (약 50MB)](/assets/downloads/ai-teacher-app-release.apk)**

> ⚠️ **설치 전 확인사항**
> - Android 기기에서 "출처를 알 수 없는 앱" 허용 필요
> - 설치 후 앱 실행 → 설정에서 **DeepSeek / Gemini API 키 입력** 필요
> - API 키 발급: [DeepSeek](https://platform.deepseek.com) / [Gemini](https://aistudio.google.com/apikey) — 둘 다 무료 티어 지원

> ⚠️ **API 키는 어떻게?**
> 이 글에서는 설정 화면 코드를 생략했지만, 실제 앱에서는 **설정 화면에서 사용자가 직접 API 키를 입력**하고 기기에 저장하는 구조입니다. (키를 코드에 하드코딩하면 보안상 위험해요!)
> - DeepSeek 키: https://platform.deepseek.com
> - Gemini 키: https://aistudio.google.com/apikey
>
> 두 키 모두 무료 티어가 있어 취미로 만들기 좋습니다.

---

## 📝 마무리

이 앱의 핵심 포인트 3가지를 정리하면:

1. **하이브리드 라우팅** — 질문 형태(텍스트/사진)에 따라 DeepSeek/Gemini를 자동 선택
2. **학년 맞춤 프롬프트** — 초5~고3 눈높이에 맞는 설명을 시스템 프롬프트로 주입
3. **잘림 자동 복구** — 응답이 길어 토큰 한도에 걸리면 이어서 계속 생성

Flutter 하나로 Android/iOS 모두 대응할 수 있고, AI API만 있으면 누구나 비슷한 "AI 과외 선생님" 앱을 만들 수 있습니다. 궁금한 점은 댓글로 남겨주세요! 🙌

---

<p style="color:#000000; font-weight:bold; margin-top:24px;">개발자: 심종주 (2026.08.03 기준)</p>
