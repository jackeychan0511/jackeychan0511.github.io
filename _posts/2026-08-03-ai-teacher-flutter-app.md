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
import 'dart:convert';
import 'dart:typed_data';
import 'package:http/http.dart' as http;
import '../models/question.dart';

/// Google Gemini API 호출 서비스 (사진/비전 질문용)
class AiService {
  final String apiKey;
  final String model; // 예: gemini-2.5-flash
  final String grade; // 예: 중2, 고3
  final http.Client _client;

  AiService({
    required this.apiKey,
    this.model = 'gemini-2.5-flash',
    this.grade = '중2',
    http.Client? client,
  }) : _client = client ?? http.Client();

  static const String _baseUrl =
      'https://generativelanguage.googleapis.com/v1beta/models';

  /// 질문을 보내고 풀이를 받아온다.
  ///
  /// 응답이 maxOutputTokens 한도에 걸려 잘렸으면(finishReason == 'MAX_TOKENS')
  /// 이전 대화를 컨텍스트로 유지한 채 자동으로 이어서 생성한다.
  Future<String> ask(Question question) async {
    final uri = Uri.parse('$_baseUrl/$model:generateContent?key=$apiKey');

    // 사용자 질문 텍스트
    final userText = StringBuffer();
    if (question.text.trim().isNotEmpty) {
      userText.write('문제: ${question.text.trim()}\n');
    } else {
      userText.write('문제: [첨부한 사진 속 문제]\n');
    }
    userText.write('위 문제를 $grade 수준으로 풀이해주세요.');

    // 대화 컨텍스트: 첫 턴(질문+사진)으로 시작해 잘리면 이어붙인다
    final contents = <Map<String, dynamic>>[
      {
        'role': 'user',
        'parts': [
          {'text': userText.toString()},
          if (question.hasImage)
            {
              'inline_data': {
                'mime_type': question.imageMimeType ?? 'image/jpeg',
                'data': base64Encode(question.imageBytes!),
              }
            },
        ],
      }
    ];

    final allText = StringBuffer();
    var truncated = false;

    // 잘린 응답은 최대 3회까지 이어서 생성한다 (총 4회 호출)
    for (var attempt = 0; attempt < 4; attempt++) {
      final body = jsonEncode({
        'contents': contents,
        'system_instruction': {
          'parts': [
            {'text': buildTeacherSystemPrompt(grade)}
          ]
        },
        'generationConfig': {
          'temperature': 0.7,
          'maxOutputTokens': 8192,
        },
      });

      final response = await _client
          .post(
            uri,
            headers: {'Content-Type': 'application/json'},
            body: body,
          )
          .timeout(const Duration(seconds: 90));

      if (response.statusCode != 200) {
        throw AiException(
          'Gemini API 오류 (${response.statusCode}): ${response.body}',
        );
      }

      final json = jsonDecode(utf8.decode(response.bodyBytes));
      final candidates = json['candidates'] as List?;
      if (candidates == null || candidates.isEmpty) {
        throw AiException('AI 응답이 비어 있습니다. 문제가 너무 어렵거나 사진이 흐릴 수 있어요.');
      }

      final parts = candidates[0]['content']?['parts'] as List?;
      if (parts == null || parts.isEmpty) {
        throw AiException('AI 응답을 해석할 수 없습니다.');
      }

      final text = parts.map((p) => p['text'] as String? ?? '').join('\n');
      if (text.trim().isEmpty) {
        throw AiException('AI가 답변을 생성하지 못했어요. 다시 시도해주세요.');
      }
      allText.write(text);

      // 잘렸는지 확인: 'MAX_TOKENS'면 토큰 한도에 걸려 끊긴 것
      final finishReason = candidates[0]['finishReason'] as String?;
      if (finishReason != 'MAX_TOKENS') {
        truncated = false;
        break;
      }
      truncated = true;

      // 이어서 생성: 지금까지의 답변을 대화에 넣고 계속 요청
      contents.add({
        'role': 'model',
        'parts': [
          {'text': text}
        ],
      });
      contents.add({
        'role': 'user',
        'parts': [
          {
            'text': '방금 설명을 중간에 끊었어. 끊긴 부분부터 이어서 계속 설명해줘. 처음부터 다시 쓰지 말고. 마지막에 **답**을 반드시 포함해줘.',
          }
        ],
      });
    }

    var result = allText.toString().trim();
    if (truncated) {
      result += '\n\n> 💡 답변이 길어 일부만 표시됐어요. 다시 물어보면 더 길게 설명해줘요.';
    }
    return result;
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
  final http.Client _client;

  DeepSeekService({
    required this.apiKey,
    this.model = 'deepseek-v4-flash',
    this.grade = '중2',
    http.Client? client,
  }) : _client = client ?? http.Client();

  static const String _baseUrl = 'https://api.deepseek.com/v1';

  /// 텍스트 질문을 보내고 풀이를 받아온다.
  ///
  /// 응답이 max_tokens 한도에 걸려 잘렸으면(finish_reason == 'length')
  /// 이전 대화를 컨텍스트로 유지한 채 자동으로 이어서 생성한다.
  Future<String> ask(Question question) async {
    final uri = Uri.parse('$_baseUrl/chat/completions');

    final userText = StringBuffer();
    if (question.text.trim().isNotEmpty) {
      userText.write('문제: ${question.text.trim()}\n');
    } else {
      userText.write('문제: [첨부한 사진 속 문제]\n');
    }
    userText.write('위 문제를 $grade 수준으로 풀이해주세요.');

    final messages = <Map<String, String>>[
      {'role': 'system', 'content': buildTeacherSystemPrompt(grade)},
      {'role': 'user', 'content': userText.toString()},
    ];

    final allText = StringBuffer();
    var truncated = false;

    // 잘린 응답은 최대 3회까지 이어서 생성한다 (총 4회 호출)
    for (var attempt = 0; attempt < 4; attempt++) {
      final body = jsonEncode({
        'model': model,
        'messages': messages,
        'temperature': 0.7,
        'max_tokens': 8192,
      });

      final response = await _client
          .post(
            uri,
            headers: {
              'Content-Type': 'application/json',
              'Authorization': 'Bearer $apiKey',
            },
            body: body,
          )
          .timeout(const Duration(seconds: 90));

      if (response.statusCode != 200) {
        throw AiException(
          'DeepSeek API 오류 (${response.statusCode}): ${response.body}',
        );
      }

      final json = jsonDecode(utf8.decode(response.bodyBytes));
      final choices = json['choices'] as List?;
      if (choices == null || choices.isEmpty) {
        throw AiException('AI 응답이 비어 있습니다. 다시 시도해주세요.');
      }

      final text = choices[0]['message']?['content'] as String? ?? '';
      if (text.trim().isEmpty) {
        throw AiException('AI가 답변을 생성하지 못했어요. 다시 시도해주세요.');
      }
      allText.write(text);

      // 잘렸는지 확인: 'length'면 토큰 한도에 걸려 끊긴 것
      final finishReason = choices[0]['finish_reason'] as String?;
      if (finishReason != 'length') {
        truncated = false;
        break;
      }
      truncated = true;

      // 이어서 생성: 지금까지의 답변을 대화에 넣고 계속 요청
      messages.add({'role': 'assistant', 'content': text});
      messages.add({
        'role': 'user',
        'content': '방금 설명을 중간에 끊었어. 끊긴 부분부터 이어서 계속 설명해줘. 처음부터 다시 쓰지 말고. 마지막에 **답**을 반드시 포함해줘.',
      });
    }

    var result = allText.toString().trim();
    if (truncated) {
      result += '\n\n> 💡 답변이 길어 일부만 표시됐어요. 다시 물어보면 더 길게 설명해줘요.';
    }
    return result;
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

  bool get hasGeminiApiKey => _geminiApiKey.trim().isNotEmpty;
  bool get hasDeepSeekApiKey => _deepSeekApiKey.trim().isNotEmpty;

  /// 현재 질문 방식에 필요한 키가 있는지 (사진 → Gemini, 텍스트 → DeepSeek)
  bool get hasRequiredApiKey => hasImage ? hasGeminiApiKey : hasDeepSeekApiKey;

  // --- 현재 질문 ---
  String _questionText = '';
  Uint8List? _imageBytes;
  String? _imageMimeType;

  bool get hasImage => _imageBytes != null && _imageBytes!.isNotEmpty;

  // --- 답변 상태 ---
  bool _isLoading = false;
  String? _answer;
  String? _error;
  String? _usedModel; // 이번 답변에 사용된 모델 (표시용)

  /// AI에게 질문하고 답변을 받아온다.
  /// 사진이 있으면 Gemini(비전), 텍스트만 있으면 DeepSeek를 사용한다.
  Future<void> askQuestion() async {
    final question = Question(
      text: _questionText,
      imageBytes: _imageBytes,
      imageMimeType: _imageMimeType,
    );

    if (!question.isValid) {
      _error = '문제를 텍스트로 입력하거나 사진을 첨부해주세요.';
      notifyListeners();
      return;
    }

    if (question.hasImage && !hasGeminiApiKey) {
      _error = '사진 질문에는 Gemini API 키가 필요해요. 설정에서 입력해주세요.';
      notifyListeners();
      return;
    }
    if (!question.hasImage && !hasDeepSeekApiKey) {
      _error = '텍스트 질문에는 DeepSeek API 키가 필요해요. 설정에서 입력해주세요.';
      notifyListeners();
      return;
    }

    _isLoading = true;
    _error = null;
    _answer = null;
    _usedModel = null;
    notifyListeners();

    try {
      if (question.hasImage) {
        // 사진 질문 → Gemini (비전)
        final service = AiService(
          apiKey: _geminiApiKey,
          model: _geminiModel,
          grade: _grade,
        );
        _answer = await service.ask(question);
        _usedModel = 'Gemini $_geminiModel';
      } else {
        // 텍스트 질문 → DeepSeek
        final service = DeepSeekService(
          apiKey: _deepSeekApiKey,
          grade: _grade,
        );
        _answer = await service.ask(question);
        _usedModel = 'DeepSeek ${service.model}';
      }
    } catch (e) {
      _error = e.toString();
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }
}
```

---

## 🏠 5. 질문 입력 화면 (`home_screen.dart` 핵심)

텍스트 입력 + 사진 첨부 + 질문하기 버튼으로 구성됩니다.

```dart
import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:provider/provider.dart';

import '../services/app_state.dart';
import 'answer_screen.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final _picker = ImagePicker();
  final _textController = TextEditingController();

  @override
  void dispose() {
    _textController.dispose();
    super.dispose();
  }

  Future<void> _pickImage(ImageSource source) async {
    try {
      final file = await _picker.pickImage(
        source: source,
        maxWidth: 1600, // 전송 크기 제한
        imageQuality: 85,
      );
      if (file == null) return;

      final bytes = await file.readAsBytes();
      if (!mounted) return;
      final appState = context.read<AppState>();
      appState.setQuestionImage(bytes, file.mimeType ?? 'image/jpeg');
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('사진을 불러오지 못했어요: $e')),
        );
      }
    }
  }

  void _showImageSourceSheet() {
    showModalBottomSheet(
      context: context,
      builder: (ctx) => SafeArea(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            ListTile(
              leading: const Icon(Icons.photo_camera),
              title: const Text('카메라로 촬영'),
              onTap: () {
                Navigator.pop(ctx);
                _pickImage(ImageSource.camera);
              },
            ),
            ListTile(
              leading: const Icon(Icons.photo_library),
              title: const Text('갤러리에서 선택'),
              onTap: () {
                Navigator.pop(ctx);
                _pickImage(ImageSource.gallery);
              },
            ),
          ],
        ),
      ),
    );
  }

  Future<void> _askQuestion() async {
    final appState = context.read<AppState>();
    if (!appState.hasRequiredApiKey) return; // 키가 없으면 안내 (생략)
    await appState.askQuestion();
    if (!mounted) return;
    if (appState.error == null) {
      await Navigator.push(
        context,
        MaterialPageRoute(
          builder: (_) => const AnswerScreen(),
        ),
      );
      // 답변 화면에서 돌아오면 사진 첨부를 초기화해서
      // 다음 문제에 이전 사진이 남지 않게 한다.
      if (mounted) {
        appState.setQuestionImage(null);
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    final appState = context.watch<AppState>();

    return Scaffold(
      appBar: AppBar(
        title: const Text('AI 선생님'),
        centerTitle: true,
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            // 문제 입력
            Text('모르는 문제를 물어보세요', style: Theme.of(context).textTheme.titleMedium),
            const SizedBox(height: 8),
            TextField(
              controller: _textController,
              maxLines: 5,
              decoration: const InputDecoration(
                hintText: '예) 2x + 5 = 13일 때 x의 값을 구하시오.\n또는 사진으로 문제를 찍어 올려도 돼요!',
                border: OutlineInputBorder(),
              ),
              onChanged: appState.setQuestionText,
            ),
            const SizedBox(height: 12),

            // 사진 첨부
            Row(
              children: [
                OutlinedButton.icon(
                  icon: const Icon(Icons.add_photo_alternate),
                  label: const Text('사진 첨부'),
                  onPressed: _showImageSourceSheet,
                ),
                const SizedBox(width: 12),
                if (appState.hasImage)
                  Expanded(
                    child: Text(
                      '사진 첨부됨 ✓',
                      style: TextStyle(
                        color: Theme.of(context).colorScheme.primary,
                      ),
                    ),
                  ),
              ],
            ),
            const SizedBox(height: 16),

            // 질문하기 버튼
            FilledButton.icon(
              icon: appState.isLoading
                  ? const SizedBox(
                      width: 20,
                      height: 20,
                      child: CircularProgressIndicator(strokeWidth: 2),
                    )
                  : const Icon(Icons.send),
              label: Text(appState.isLoading ? '선생님이 생각 중...' : '선생님께 질문하기'),
              style: FilledButton.styleFrom(
                padding: const EdgeInsets.symmetric(vertical: 14),
              ),
              onPressed: appState.isLoading ? null : _askQuestion,
            ),
            if (appState.error != null) ...[
              const SizedBox(height: 12),
              Container(
                padding: const EdgeInsets.all(12),
                decoration: BoxDecoration(
                  color: Theme.of(context).colorScheme.errorContainer,
                  borderRadius: BorderRadius.circular(8),
                ),
                child: Text(
                  appState.error!,
                  style: TextStyle(
                    color: Theme.of(context).colorScheme.onErrorContainer,
                  ),
                ),
              ),
            ],
          ],
        ),
      ),
    );
  }
}
```

---

## 📖 6. 답변 표시 화면 (`answer_screen.dart` 핵심)

AI의 마크다운 답변을 카드 형태로 보여줍니다. `flutter_markdown`으로 수식·목록·굵은 글씨가 그대로 렌더링됩니다.

```dart
import 'package:flutter/material.dart';
import 'package:flutter_markdown/flutter_markdown.dart';
import 'package:provider/provider.dart';

import '../services/app_state.dart';

class AnswerScreen extends StatelessWidget {
  const AnswerScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final appState = context.watch<AppState>();

    return Scaffold(
      appBar: AppBar(
        title: const Text('선생님의 풀이'),
        centerTitle: true,
      ),
      body: _buildBody(appState, context),
    );
  }

  Widget _buildBody(AppState appState, BuildContext context) {
    // 로딩 중
    if (appState.isLoading) {
      return const Center(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            CircularProgressIndicator(),
            SizedBox(height: 16),
            Text('선생님이 문제를 읽고 있어요...'),
          ],
        ),
      );
    }

    // 에러
    if (appState.error != null) {
      return Center(
        child: Padding(
          padding: const EdgeInsets.all(24),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Icon(
                Icons.error_outline,
                size: 56,
                color: Theme.of(context).colorScheme.error,
              ),
              const SizedBox(height: 16),
              Text('문제가 생겼어요', style: Theme.of(context).textTheme.titleLarge),
              const SizedBox(height: 8),
              Text(
                appState.error!,
                textAlign: TextAlign.center,
              ),
              const SizedBox(height: 24),
              FilledButton.icon(
                icon: const Icon(Icons.refresh),
                label: const Text('다시 시도'),
                onPressed: () => appState.askQuestion(),
              ),
            ],
          ),
        ),
      );
    }

    // 답변 표시
    final answer = appState.answer;
    if (answer == null) {
      return const Center(child: Text('아직 답변이 없어요.'));
    }

    return SingleChildScrollView(
      padding: const EdgeInsets.all(16),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          // 풀이 카드
          Card(
            elevation: 0,
            color: Theme.of(context).colorScheme.surfaceContainerHighest,
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(12),
            ),
            child: Padding(
              padding: const EdgeInsets.all(16),
              child: MarkdownBody(
                data: answer,
                selectable: true,
                styleSheet: MarkdownStyleSheet.fromTheme(Theme.of(context)),
              ),
            ),
          ),
          const SizedBox(height: 16),

          // 다시 질문하기
          FilledButton.icon(
            icon: const Icon(Icons.replay),
            label: const Text('다른 문제 물어보기'),
            onPressed: () {
              appState.reset();
              Navigator.pop(context);
            },
          ),
        ],
      ),
    );
  }
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
