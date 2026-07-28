---
layout: post
title: "OpenClaw 설치 가이드: OpenAI 뇌로 Telegram AI 비서 만들기 (2026)"
date: 2026-07-28 11:00:00 +0900
categories: [career]
tags: [OpenClaw, ChatGPT, OpenAI, 텔레그램봇, AI에이전트, AI자동화, 개인비서]
image: /assets/images/posts/openclaw-guide/hero.png
---

요즘 AI 에이전트에 대한 관심이 뜨겁습니다. 그중에서도 **OpenClaw**는 단연 화제의 중심에 있는 오픈소스 AI 에이전트 플랫폼인데요.

간단히 말하면 **OpenClaw = AI 두뇌(OpenAI, Claude 등) + 메신저(Telegram, WhatsApp 등)** 를 연결해주는 오픈소스 게이트웨이입니다.

이 글에서는 OpenClaw를 설치하고, OpenAI를 두뇌로 연결한 뒤, Telegram에서 바로 명령할 수 있는 AI 비서를 만드는 전 과정을 단계별로 정리했습니다.

---

## 1. OpenClaw란?

**OpenClaw**는 개인 PC나 서버에 설치해서 사용하는 **자율형 AI 에이전트 프레임워크**입니다.

| 특징 | 설명 |
|------|------|
| 🆓 **오픈소스** | GitHub에서 전체 소스 공개, 무료 사용 |
| 📱 **멀티 채널** | Telegram, WhatsApp, Discord, iMessage 등 지원 |
| 🧠 **멀티 AI** | OpenAI(GPT), Anthropic(Claude), Google(Gemini) 등 연동 |
| 🔧 **확장성** | 500+ 스킬, 플러그인, 크론잡, 웹훅 지원 |
| 🔒 **프라이버시** | 내 PC에서 실행, 데이터가 외부로 나가지 않음 |

특히 **AI 에이전트가 직접 파일을 읽고, 코드를 실행하고, 웹 검색을 하면서 작업을 수행**한다는 점이 일반 챗봇과의 가장 큰 차이점입니다.

---

## 2. 사전 준비

### 필요 사양

| 항목 | 요구사항 |
|------|---------|
| Node.js | v22 이상 |
| OS | Windows / macOS / Linux |
| API 키 | OpenAI 또는 Anthropic 중 하나 |
| Telegram 계정 | 봇 생성용 |

### Node.js 설치 확인

```bash
node -v
# v22.x.x 이상이어야 함
```

Node.js가 없다면 [공식 사이트](https://nodejs.org)에서 LTS 버전을 다운로드하세요.

### API 키 준비

OpenAI를 두뇌로 사용하려면 **OpenAI API 키**가 필요합니다.

1. [OpenAI 플랫폼](https://platform.openai.com) 접속
2. 우측 상단 프로필 → **API keys**
3. **Create new secret key** 클릭
4. 발급된 키를 복사해서 **안전한 곳에 저장**

> 💡 API 키는 한 번만 보여주니 꼭 바로 복사해두세요!

---

## 3. OpenClaw 설치하기

![OpenClaw 설치 화면](/assets/images/posts/openclaw-guide/setup.png)

설치 방법은 3가지가 있습니다. 초보자에게는 **npm 글로벌 설치**를 가장 추천합니다.

### 방법 1: npm 글로벌 설치 (추천)

```bash
npm install -g openclaw@latest
```

설치 확인:

```bash
openclaw --version
```

### 방법 2: 원클릭 스크립트 (빠른 설치)

**macOS / Linux:**
```bash
curl -fsSL https://openclaw.ai/install.sh | bash
```

**Windows (PowerShell):**
```powershell
iwr -useb https://openclaw.ai/install.ps1 | iex
```

### 방법 3: 소스 빌드 (개발자용)

```bash
git clone https://github.com/openclaw/openclaw.git
cd openclaw
pnpm install
pnpm build
```

---

## 4. 온보딩 마법사로 핵심 설정하기

설치 후 첫 실행은 **온보딩 마법사**가 안내해줍니다.

```bash
openclaw onboard --install-daemon
```

대화형 프롬프트를 따라가며 설정하는 항목들:

| 설정 항목 | 설명 |
|----------|------|
| 🔗 Gateway | AI 에이전트 상시 실행 데몬 |
| 🔑 인증 | AI 모델 API 키 등록 (OpenAI 등) |
| 📱 채널 | Telegram, Discord 등 연결 |
| 📂 워크스페이스 | 에이전트 작업 공간 경로 |

`--install-daemon` 플래그를 붙이면 Gateway가 OS 서비스로 자동 등록되어, **재부팅 후에도 자동 실행**됩니다.

---

## 5. OpenAI를 두뇌로 연결하기

### 환경변수 설정

가장 간단한 방법은 **환경변수**에 API 키를 등록하는 것입니다.

**Windows:**
```powershell
setx OPENAI_API_KEY "sk-proj-xxxxxxxxxxxx"
```

**macOS / Linux:**
```bash
export OPENAI_API_KEY="sk-proj-xxxxxxxxxxxx"
```

### 설정 파일 직접 편집

또는 OpenClaw 설정 파일을 직접 편집할 수도 있습니다.

```bash
# 설정 파일 위치
# ~/.openclaw/openclaw.json
```

```json
{
  "agents": {
    "defaults": {
      "workspace": "~/.openclaw/workspace",
      "models": {
        "default": "openai/gpt-4o"
      }
    }
  }
}
```

> 💡 `openai/gpt-4o` 대신 `openai/gpt-4o-mini`를 사용하면 비용을 절약할 수 있습니다.

---

## 6. Telegram 봇 생성 및 연결

이제 Telegram에서 AI 에이전트와 대화할 수 있도록 봇을 연결해보겠습니다.

### 6-1. BotFather에서 봇 생성

1. Telegram에서 **[@BotFather](https://t.me/botfather)** 검색
2. `/newbot` 명령어 입력
3. 봇 이름 입력 (예: `내 AI 비서`)
4. 봇 username 입력 (예: `my_ai_assistant_bot`)
5. 발급된 **API 토큰** 복사

> 토큰 형식: `7123456789:AAHx...`

### 6-2. 설정 파일에 토큰 등록

온보딩 마법사에서 등록하지 않았다면, 설정 파일에 직접 추가합니다:

```json
{
  "channels": {
    "telegram": {
      "botToken": "7123456789:AAHx...",
      "dmPolicy": "pairing"
    }
  }
}
```

**dmPolicy** 옵션 설명:

| 정책 | 설명 |
|-----|------|
| `pairing` | 페어링 승인 필요 (보안상 **강력 추천** ✅) |
| `open` | 누구나 DM 가능 (테스트용) |
| `deny` | DM 차단 |

### 6-3. 페어링 승인

처음 봇에게 DM을 보내면 페어링 요청이 발생합니다. 터미널에서 승인합니다:

```bash
# 대기 중인 페어링 목록 확인
openclaw pairing list

# 페어링 승인
openclaw pairing approve <요청ID>
```

또는 웹 대시보드(`http://127.0.0.1:18789/`)에서 시각적으로 승인할 수도 있습니다.

---

## 7. Gateway 실행 및 테스트

### Gateway 실행

```bash
# 상태 확인
openclaw gateway status

# 실행
openclaw gateway start

# 중지
openclaw gateway stop
```

### Telegram에서 테스트

페어링이 완료된 계정으로 봇에게 아무 메시지나 보내보세요.

**예시:**
```
@my_ai_assistant_bot 안녕? 오늘 할 일 정리해줘
```

AI 에이전트가 응답하면 성공입니다! 🎉

### 슬래시 명령어

Telegram 채팅에서 사용할 수 있는 기본 명령어:

| 명령어 | 설명 |
|-------|------|
| `/status` | Gateway 상태 확인 |
| `/model <모델>` | AI 모델 변경 |
| `/thinking <level>` | 사고 레벨 조정 |
| `/stop` | 실행 중인 작업 중지 |

---

## 8. 활용 팁: AI 워크스페이스 꾸미기

OpenClaw는 단순한 챗봇이 아니라 **진짜 업무를 처리하는 에이전트**입니다. 워크스페이스 파일을 설정하면 더 똑똑하게 활용할 수 있습니다.

```
~/.openclaw/workspace/
├── AGENTS.md        # 에이전트 행동 규칙
├── SOUL.md          # 페르소나, 말투 정의
├── USER.md          # 사용자 정보
├── MEMORY.md        # 장기 기억
└── memory/          # 일별 메모리 로그
```

**SOUL.md 예시:**
```markdown
당신은 친근하고 전문적인 AI 비서입니다.
- 한국어로 자연스럽게 대답합니다.
- 질문에 대해 충분히 생각한 후 답변합니다.
- 모르는 것은 솔직히 모른다고 말합니다.
```

---

## ⚠️ 주의사항

1. **API 키 보안** — OpenAI API 키는 절대 공유하지 마세요. GitHub에 커밋하지 않도록 `.gitignore`에 추가하세요.
2. **토큰 비용** — OpenAI API는 사용량에 따라 비용이 발생합니다. `gpt-4o-mini` 등 저렴한 모델로 시작하는 것을 추천합니다.
3. **처음엔 로컬 테스트** — VPS 등에 바로 배포하기보다 로컬 PC에서 먼저 테스트해보세요.
4. **Gateway 보안** — 원격 접속 시 반드시 인증 토큰을 설정하세요.

---

## ✅ 마무리

지금까지 **OpenClaw 설치 → OpenAI 연결 → Telegram 봇 연동**까지 전 과정을 살펴봤습니다.

한 줄로 요약하면:

> **npm install 한 줄로 시작해서, Telegram에서 AI 비서와 대화하는 데까지 15분이면 충분하다.**

AI 에이전트는 더 이상 개발자만의 장난감이 아닙니다. 이제 누구나 자신만의 AI 비서를 만들어서 업무 자동화, 정보 검색, 일정 관리 등에 활용할 수 있습니다.

다음 글에서는 **OpenClaw 스킬 등록과 크론잡을 활용한 자동화**에 대해 다뤄보겠습니다.

---

*이 글은 직접 OpenClaw를 설치하고 운영한 경험을 바탕으로 작성되었습니다.*
