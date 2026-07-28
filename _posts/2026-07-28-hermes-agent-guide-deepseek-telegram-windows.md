---
layout: post
title: "Hermes Agent 설치 가이드: DeepSeek부터 텔레그램 연동까지 (윈도우 완벽 정리)"
date: 2026-07-28 11:00:00 +0900
categories: [career]
tags: [HermesAgent, NousResearch, AI에이전트, DeepSeek, 텔레그램봇, AI설치, 윈도우]
image: /assets/images/posts/hermes-guide/hero.png
---

AI 에이전트 하면 OpenClaw만 떠오르셨다면, 이번 글을 주목해주세요.

**Hermes Agent**는 Nous Research에서 만든 오픈소스 AI 에이전트로, **스스로 학습하고 성장하는** 차세대 에이전트입니다. OpenClaw와 함께 2026년을 대표하는 AI 에이전트로 자리 잡았죠.

![Hermes Agent 윈도우 데스크탑](/assets/images/posts/hermes-guide/hero.png)

> 이 글은 제가 실제로 Hermes Agent를 설치하고 DeepSeek를 연결하고 텔레그램까지 연동한 경험을 바탕으로 작성했습니다.

---

## 1. Hermes Agent란?

**Hermes Agent**는 Nous Research가 개발한 **자기 학습형 오픈소스 AI 에이전트**입니다.

| 항목 | 설명 |
|------|------|
| 🏢 **개발사** | Nous Research (Hermes, Nomos 모델 개발사) |
| 📜 **라이선스** | MIT (완전 무료) |
| 🔑 **핵심 특징** | **자기 학습 루프** — 경험에서 스킬을 만들고 개선 |
| 📱 **지원 채널** | 텔레그램, 디스코드, 슬랙, 왓츠앱 등 |
| 💻 **지원 OS** | Windows / macOS / Linux |
| 🌐 **언어** | 한국어 완벽 지원 |

### OpenClaw와의 차이점

| 비교 항목 | Hermes Agent | OpenClaw |
|:---------|:------------|:---------|
| **학습 기능** | ✅ **내장 학습 루프** — 사용 경험 → 스킬 자동 생성 | ❌ 수동 스킬 등록 |
| **설치 방식** | npm + 원클릭 + 데스크탑 앱 | npm + 스크립트 |
| **메모리** | 세션을 넘어 지속되는 장기 기억 | 워크스페이스 기반 |
| **데스크탑 UI** | ✅ 네이티브 앱 (macOS/Windows/Linux) | 웹 대시보드 |
| **한국어 문서** | ✅ 공식 문서 + 커뮤니티 가이드 풍부 | ✅ 다수 |

---

## 2. 설치하기

### 사전 준비

| 항목 | 필수 여부 |
|------|:---------:|
| Node.js v22+ | ✅ 필수 |
| Git | ✅ 필수 |
| OpenAI / DeepSeek / Anthropic API 키 | ✅ 하나 이상 |
| 텔레그램 계정 | ☑️ 선택 |

Node.js 확인:
```bash
node -v
# v22.x.x 이상 권장
```

### 설치 방법 1: 원클릭 스크립트 (추천)

**Linux / macOS / WSL2:**
```bash
curl -fsSL https://hermes-agent.nousresearch.com/install.sh | bash
```

**Windows (PowerShell):**
```powershell
iwr -useb https://hermes-agent.nousresearch.com/install.ps1 | iex
```

> 스크립트가 Node.js부터 모든 의존성을 자동으로 처리합니다.

### 설치 방법 2: npm 글로벌 설치

```bash
npm install -g hermes-agent
```

설치 확인:
```bash
hermes --version
```

### 설치 방법 3: 데스크탑 앱 (윈도우 사용자 강력 추천)

Nous Research 공식 사이트에서 **Hermes Desktop** 앱을 다운로드할 수 있습니다.

1. [hermes-agent.nousresearch.com](https://hermes-agent.nousresearch.com) 접속
2. 상단 **Download** 버튼 클릭
3. Windows용 `.exe` 설치 파일 다운로드
4. 설치 마법사 따라 실행

> 데스크탑 앱은 설치부터 설정까지 GUI로 진행되므로 CLI가 익숙하지 않은 분들에게 특히 좋습니다.

---

## 3. DeepSeek 연동하기

DeepSeek는 **Hermes Agent와 공식 연동**을 지원하는 AI 모델입니다. 비용 대비 성능이 뛰어나서 개인 AI 에이전트의 두뇌로 인기가 많습니다.

### 설정 방법

`hermes configure` 명령어로 대화형 설정을 시작하거나, 설정 파일을 직접 편집할 수 있습니다.

```yaml
# ~/.hermes/config.yaml
provider: deepseek
model: deepseek/deepseek-v4
apiKey: sk-xxxxxxxxxxxxxxxxxxxxx  # DeepSeek API 키
```

> 🔑 DeepSeek API 키는 [platform.deepseek.com](https://platform.deepseek.com)에서 발급받을 수 있습니다.

### DeepSeek V4 활용 팁

| 팁 | 설명 |
|:---|------|
| 🧠 **추론 성능** | DeepSeek V4는 복잡한 추론에 강점 |
| 💰 **가격** | GPT-4o 대비 약 1/5 수준으로 경제적 |
| 🌏 **한국어** | 한국어 처리 능력 우수 |
| ⚡ **속도** | 응답 속도 빠름 (스트리밍 지원) |

---

## 4. 텔레그램 연동하기

Hermes Agent를 텔레그램 봇으로 연결하면 **언제 어디서든 AI 에이전트와 대화**할 수 있습니다.

### 4-1. BotFather에서 봇 생성

1. 텔레그램에서 **[@BotFather](https://t.me/BotFather)** 검색
2. `/newbot` 명령어 입력
3. 봇 이름 설정 (예: `내 Hermes 비서`)
4. 봇 username 설정 (예: `my_hermes_bot` — 반드시 `bot`으로 끝나야 함)
5. 발급된 **API 토큰** 복사

> 토큰 예시: `123456789:ABCdefGHIjklMNOpqrSTUvwxYZ`

### 4-2. 봇 설정 커스터마이즈 (선택)

BotFather에서 아래 명령어로 봇을 예쁘게 꾸밀 수 있습니다:

| BotFather 명령어 | 설명 |
|:----------------|:-----|
| `/setdescription` | 봇 소개 설명 설정 |
| `/setabouttext` | 프로필 짧은 소개 |
| `/setuserpic` | 프로필 사진 업로드 |
| `/setcommands` | 명령어 메뉴 등록 |

### 4-3. Hermes에 텔레그램 연결

```yaml
# ~/.hermes/config.yaml (텔레그램 설정 추가)
gateway:
  platforms:
    telegram:
      enabled: true
      botToken: "123456789:ABCdefGHIjklMNOpqrSTUvwxYZ"
      extra:
        status_indicator: true
        status_online: "🟢 Online"
        status_offline: "🔴 Offline"
```

### 4-4. 게이트웨이 실행

```bash
# 게이트웨이 실행
hermes gateway start

# 상태 확인
hermes gateway status
```

게이트웨이가 실행되면 텔레그램에서 봇에게 메시지를 보내보세요. AI 에이전트가 응답하면 성공입니다!

### 4-5. 온라인/오프라인 표시

Hermes Agent는 봇의 **상태 표시(Online/Offline)** 기능을 지원합니다. 위 설정에서 `status_indicator: true`로 설정하면 봇 프로필에 현재 상태가 표시됩니다.

---

## 5. 윈도우 UI 사용 가이드

Hermes Desktop 앱은 윈도우에서 AI 에이전트를 GUI로 사용할 수 있게 해줍니다.

### 주요 화면 구성

| 영역 | 설명 |
|:----|:------|
| 💬 **채팅** | AI 에이전트와 대화하는 메인 화면 |
| ⚙️ **설정** | 모델, API 키, 채널 설정 |
| 🧠 **메모리** | 에이전트가 기억하는 내용 관리 |
| 🔧 **스킬** | 설치된 스킬 목록 및 관리 |
| 📋 **크론** | 예약 작업 관리 |

### 윈도우 앱 장점

- **설치부터 설정까지 GUI**로 직관적 처리
- **자동 업데이트** 알림
- **시스템 트레이**에 상주하여 백그라운드 실행
- **한국어 UI** 완벽 지원
- **로그 뷰어** 내장

---

## 6. 주의사항 및 꿀팁

### ⚠️ 주의사항

1. **API 키 보안** — 절대 GitHub에 커밋하지 마세요. 환경변수나 `.env` 파일로 관리
2. **토큰 비용 관리** — DeepSeek는 저렴하지만, 대량 사용 시 비용이 쌓일 수 있습니다. 사용량 모니터링 추천
3. **게이트웨이 보안** — 텔레그램 봇 토큰이 유출되면 즉시 BotFather에서 `/revoke`로 폐기
4. **처음에는 로컬 테스트** — VPS에 바로 올리기 전에 로컬 PC에서 충분히 테스트
5. **메모리 정리** — 장기 사용 시 메모리가 쌓이면 가끔 초기화 필요

### 💡 설치 꿀팁

| 상황 | 추천 설치법 |
|:-----|:-----------|
| **윈도우 일반 사용자** | 데스크탑 앱 (GUI) |
| **개발자 / 파워유저** | npm 글로벌 설치 + CLI |
| **맥북 / 리눅스** | 원클릭 스크립트 |
| **서버 / VPS** | npm 설치 + 데몬 모드 |

### 🎯 활용 아이디어

- **일일 브리핑** — 크론잡으로 매일 아침 뉴스 요약
- **메모/아이디어 기록** — 텔레그램으로 바로 메모
- **파일 자동 정리** — 다운로드 폴더 자동 분류
- **코드 리뷰** — GitHub PR 자동 리뷰
- **블로그 자동 포스팅** — 키워드만 주면 AI가 작성

---

## 7. 마무리

**Hermes Agent**는 단순한 챗봇이 아니라 **스스로 성장하는 AI 에이전트**입니다.

설치부터 DeepSeek 연결, 텔레그램 연동까지 전 과정이 **10분이면 완료**됩니다. 윈도우 사용자라면 데스크탑 앱 하나로 모든 설정이 끝나니 더 쉬워요.

> 💡 **한 줄 요약:** `npm install -g hermes-agent` 한 줄이면 끝. DeepSeek 연결하면 월几千원으로 24시간 AI 비서 운영 가능.

AI 에이전트 시대, Hermes Agent로 나만의 AI 비서를 만들어보세요.

---

*이 글은 실제 Hermes Agent 사용 경험과 공식 문서(Docs, DeepSeek 연동 가이드)를 참고하여 작성되었습니다.*
