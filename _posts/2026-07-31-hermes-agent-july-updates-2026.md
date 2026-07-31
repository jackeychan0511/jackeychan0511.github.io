---
layout: post
title: "2026년 7월 Hermes Agent 업데이트 총정리: 비동기 서브에이전트부터 Blank Slate 모드까지"
date: 2026-07-31 18:00:00 +0900
categories: [career]
tags: [HermesAgent, NousResearch, AI에이전트, AsyncSubagents, BlankSlate, SmartApproval, 오픈소스, AI업데이트, SelfEvolution]
image: /assets/images/posts/hermes-agent-july-2026/hermes-agent-logo.png
---

요즘 저처럼 AI 에이전트 생태계를 계속 주시하고 계신 분들, Hermes Agent 얘기를 정말 자주 접하실 거예요. Nous Research에서 만든 이 오픈소스 에이전트가 **7월 한 달 동안만 무려 3개의 주요 업데이트**를 쏟아냈거든요.

6월 말부터 7월까지 Hermes Agent에 적용된 변화들을 쭉 훑어보면서, **"이 에이전트, 진짜 달라지고 있구나"** 하는 생각이 들었습니다. 오늘은 그 변화들을 한 자리에 정리해드릴게요.

![Hermes Agent 공식 로고](/assets/images/posts/hermes-agent-july-2026/hermes-agent-logo.png)
*Hermes Agent — Nous Research가 만든 자기학습형 오픈소스 AI 에이전트*

---

## 🏆 먼저, Hermes Agent 지금 상황은?

![Hermes Agent 7월 업데이트 개요](/assets/images/posts/hermes-agent-july-2026/hermes-async-banner.png)
*Hermes Agent — Nous Research의 오픈소스 자기학습 AI 에이전트*

2026년 2월 25일 첫 릴리즈 이후 불과 5개월 만에 Hermes Agent는 **GitHub에서 180,000개가 넘는 스타**를 받았습니다. 오픈소스 AI 에이전트 프레임워크 중 가장 빠른 성장세인데요.

단순히 인기만 있는 게 아닙니다. MIT 라이선스로 완전 무료, 자기학습 루프(Learning Loop), 세션을 넘나드는 지속적 메모리, 24개 메시징 플랫폼 지원, 80개 이상의 내장 스킬 — **기능 리스트만 봐도 2026년 상반기 가장 주목할 만한 에이전트**라는 평가가 아깝지 않아요.

그런데 7월 들어 Nous Research가 더욱 공격적으로 기능을 쏟아내고 있습니다. 하나씩 살펴볼게요.

| 지표 | 수치 |
|:----|:----|
| ⭐ GitHub Stars | **180,000+** |
| 📅 최초 릴리즈 | 2026년 2월 25일 |
| 📝 라이선스 | MIT (완전 오픈소스) |
| 🏗️ 최신 버전 | v0.19.0 Quicksilver (7월 20일) |
| 🔧 커뮤니티 기여자 | **1,500+명** |

---

## 1️⃣ 비동기 서브에이전트 (Async Subagents) — 기다리지 않고 동시에 여러 작업 처리

6월 15일, Nous Research가 `async_delegation` 툴셋을 배포하면서 **Hermes Agent가 드디어 진정한 병렬 작업**을 할 수 있게 됐습니다.

### 무엇이 달라졌나?

기존의 `delegate_task`는 서브에이전트에 작업을 위임하면 **부모 에이전트가 결과를 받을 때까지 아무것도 못 하고 기다려야** 했어요. 리서치 3개를 동시에 시키려면 하나 끝나고, 다음 하나, 또 다음 — 순차적으로만 가능했죠.

이제는 `delegate_task(background=true)` 옵션을 쓰면 **서브에이전트가 백그라운드 스레드로 즉시 실행**되고, 즉시 `task_id`를 반환합니다. 부모 에이전트는 기다리지 않고 자기 할 일을 계속합니다.

```yaml
# 실제 사용 예시 (Hermes Agent 명령)
delegate_task(background=true, goal="2026년 7월 AI 업계 뉴스 5개 리서치")
delegate_task(background=true, goal="블로그 포스팅용 이미지 검색")
delegate_task(background=true, goal="경쟁 에이전트 기능 비교표 작성")
```

### 6개의 전용 도구

비동기 서브에이전트를 완전히 제어할 수 있는 6개의 도구가 함께 제공됩니다.

| 도구 | 기능 |
|:----|:------|
| `delegate_task_async` | 백그라운드 서브에이전트 실행, 즉시 task_id 반환 |
| `check_task` | 진행 상태 + 최근 출력 조회 (논블로킹) |
| `steer_task` | 실행 중인 서브에이전트에 추가 메시지 주입 |
| `collect_task` | 완료된 서브에이전트 결과 수집 |
| `cancel_task` | 실행 중인 작업 취소 |
| `list_tasks` | 현재 실행 중인 모든 작업 목록 |

솔직히 이 기능은 **실무에서 체감이 큽니다**. 가령 "3개 블로그 포스팅 자료 조사" 같은 걸 시키면 예전에는 한참 기다려야 했는데, 지금은 모든 서브에이전트가 동시에 작업하고 중간중간 진행 상황만 확인하면 되거든요.

### 주의할 점
- 서브에이전트는 부모의 대화 히스토리를 공유하지 않습니다. 각자 완전히 독립된 컨텍스트로 시작해요.
- `goal`과 `context` 필드에 충분한 정보를 담아 전달해야 원하는 결과가 나옵니다.
- 백그라운드 작업은 프로세스 로컬에서 실행되므로, Hermes 세션이 종료되면 함께 사라집니다. 오래 걸리는 작업은 크론잡을 활용하세요.

---

## 2️⃣ Blank Slate 모드 — 최소한의 권한으로 시작하는 에이전트

6월 20일, Nous Research는 **Blank Slate 모드**라는 완전히 새로운 설치 방식을 선보였습니다.

### 기존 방식의 문제점

Hermes Agent를 처음 설치하면 기본적으로 **웹 검색, 브라우저, 코드 실행, 메모리, 배포, 크론, MCP** 등 모든 도구가 활성화됩니다. 편리하긴 한데, 생각해보면 이게 항상 바람직한 건 아니에요.

예를 들어 "파일 정리만 하는 에이전트"가 필요한데 웹 검색 권한이 있으면 보안상 찜찜하죠. 특히 기업 환경에서는 **최소 권한 원칙(Least Privilege)** 이 매우 중요합니다.

### Blank Slate 모드가 해결한 것

Blank Slate 모드로 설치하면 **파일 작업과 터미널만 활성화**되고, 나머지는 전부 꺼진 상태로 시작합니다.

```
hermes setup
# → Quick / Full / Blank Slate 중 선택
#   Blank Slate 선택 시:
#   ✅ 파일 작업
#   ✅ 터미널
#   ❌ 웹 검색, 브라우저, 코드 실행, 메모리, 배포, 크론 등
```

이 설정은 `platform_toolsets.cli` 파일과 `agent.disabled_toolsets`에 명시적으로 기록됩니다. 덕분에 **나중에 `hermes update`를 해도 설정이 유지**됩니다. 업데이트로 새로운 도구가 추가돼도 자동으로 활성화되지 않아요.

필요한 도구는 나중에 `hermes tools` 또는 `hermes setup agent` 명령어로 하나씩 추가할 수 있습니다.

### 누구에게 유용한가?
- **보안이 중요한 기업 환경**: 에이전트가 할 수 있는 일을 정확히 제한
- **특정 업무용 에이전트**: 파일 정리 전용, 리서치 전용 등 목적에 맞게 최소 구성
- **실수 방지**: 불필요한 도구 접근으로 인한 사이드 이펙트 차단

---

## 3️⃣ Smart Approval 기본 적용 + Deny Rules — 승인 피로도 DOWN

7월 20일 v0.19.0 Quicksilver 릴리즈에서 가장 실용적인 변화 중 하나는 **Smart Approval이 기본 모드**가 된 것입니다.

### Smart Approval이 뭔가요?

기존에는 두 가지 선택지가 있었습니다.
- `manual`: 모든 명령을 사람이 일일이 승인 → **안전하지만 생산성 급락**
- `auto`: 모든 명령을 자동 실행 → **빠르지만 위험**

Smart Approval은 **별도의 LLM 검토자(reviewer)** 가 명령을 독립적으로 평가합니다. 저위험 명령(파일 읽기, 웹 검색, 코드 작성)은 자동 승인, 고위험 명령(파일 삭제, 결제, 시스템 명령)만 사용자에게 질의하는 방식이에요.

### User-Defined Deny Rules

여기에 더해 **사용자 정의 거부 규칙(User-Defined Deny Rules)** 도 추가됐습니다. 특정 명령 패턴을 아예 차단할 수 있는 기능인데, 심지어 `yolo` 모드(모든 승인 생략)에서도 동작합니다.

```
# ~/.hermes/config.yaml 예시
deny_rules:
  - pattern: "rm -rf"
    reason: "파일 대량 삭제는 수동 확인 필요"
  - pattern: "shutdown|reboot"
    reason: "서버 재시작 금지"
```

그리고 `/deny` 명령어로 에이전트가 제안한 명령을 거절하면서 이유를 설명할 수 있습니다. 에이전트는 이 피드백을 바탕으로 다음에는 더 나은 접근 방식을 제시합니다.

> 💡 실제 사용해보니 승인 요청이 70% 이상 줄었습니다. 예전처럼 "이거 할까요?" 알림이 계속 뜨는 게 아니라서 업무 흐름이 훨씬 매끄러워졌어요.

---

## 4️⃣ Self-Evolution 저장소 — 에이전트가 스스로 진화한다

6월 6일, Nous Research는 `hermes-agent-self-evolution`이라는 **별도의 저장소**를 공개했습니다. 이게 흥미로운 점은 Hermes Agent 자체가 아니라 **에이전트를 스스로 개선하는 시스템**이라는 거예요.

### 어떻게 작동하나?

DSPy(Declarative Self-Improving Python)와 GEPA(Genetic-Pareto Prompt Evolution)를 결합한 방식입니다.

1. **실행 추적 수집**: 에이전트가 어떤 스킬을 어떻게 실행했는지 로그를 수집
2. **유전 알고리즘 적용**: 프롬프트, 스킬 설명, 도구 코드를 변이(mutation)시켜 여러 버전 생성
3. **평가**: 각 버전을 실제로 실행해 성능 측정
4. **선택**: 가장 성능이 좋은 버전을 채택
5. **반복**: 이 과정을 여러 세대(generation)에 걸쳐 반복

### 5단계 로드맵

| 단계 | 목표 | 상태 |
|:----|:------|:----|
| Phase 1 | 스킬 프롬프트 최적화 | ✅ 완료 |
| Phase 2 | 도구 설명 및 시스템 프롬프트 최적화 | ✅ 완료 |
| Phase 3 | 스킬 코드 생성 및 최적화 | ⏳ 진행 중 |
| Phase 4 | 에이전트 코드 자체 최적화 | 📅 예정 |
| Phase 5 | 완전 자율 진화 루프 | 📅 예정 |

솔직히 처음에는 "또 하나의 자가최적화 프레임워크네" 하고 넘겼는데요. 실제로 해보니 **직접 작성한 스킬이 실행 추적을 바탕으로 조금씩 더 똑똑해지는** 모습이 꽤 인상적이었습니다.

**단 GPU가 필요 없습니다.** 모든 최적화가 API 호출과 텍스트 변이로 이루어지기 때문에, 일반 개발자도 부담 없이 실험해볼 수 있어요.

---

## 5️⃣ Tool Gateway & Nous Portal — 하나의 계정으로 모든 도구 사용

4월에 출시된 Tool Gateway가 7월 들어 더욱 안정화되었습니다. **Nous Portal** 구독자라면 별도의 API 키 없이 Hermes Agent의 주요 도구를 한 번에 쓸 수 있어요.

| 도구 | Gateway 제공 | 별도 설정 필요 |
|:----|:------------|:-------------|
| 🌐 웹 검색 | ✅ Firecrawl 연동 | ❌ 불필요 |
| 🖼️ 이미지 생성 | ✅ FAL 연동 | ❌ 불필요 |
| 🗣️ 텍스트→음성 | ✅ TTS 제공 | ❌ 불필요 |
| 🌍 클라우드 브라우저 | ✅ Browser Use 연동 | ❌ 불필요 |

`hermes setup --portal` 명령 한 줄로 OAuth 로그인만 하면 **300개 이상의 모델**과 위 4가지 도구를 바로 사용할 수 있습니다. API 키 5개를 각각 발급받아 설정 파일에 넣던 시절과 비교하면 편의성이 확실히 좋아졌어요.

---

## 🎯 정리: 7월 Hermes Agent, 무엇이 달라졌나?

| 업데이트 | 날짜 | 핵심 포인트 |
|:---------|:----|:-----------|
| **Self-Evolution** | 6월 6일 | GEPA+DSPy로 스킬·프롬프트 자동 진화 |
| **비동기 서브에이전트** | 6월 15일 | background=true로 병렬 처리, 6개 전용 도구 |
| **Blank Slate 모드** | 6월 20일 | 최소 권한 설치, 업데이트에도 설정 유지 |
| **Quicksilver v0.19.0** | 7월 20일 | 콜드스타트 80% 개선, Smart Approval 기본 |

개인적으로 가장 크게 체감되는 변화를 꼽으라면 **비동기 서브에이전트**와 **Smart Approval**입니다. 서브에이전트가 동시에 작업하는 모습을 보면 "아, 이제 진짜 에이전트구나" 싶고, 승인 알림이 70% 줄어든 건 일상 업무 효율에 직접적인 영향을 줍니다.

아직 Hermes Agent를 안 써보셨다면 **지금이 시작하기 가장 좋은 타이밍**입니다. MIT 라이선스에 무료, 설치 2분이면 끝납니다. Nous Research의 업데이트 속도와 방향성을 보면 하반기에도 계속 발전할 가능성이 높아요.

AI 에이전트 하나를 정착시키려는 분, 또는 현재 쓰고 있는 에이전트에 불편함을 느끼는 분 — Hermes Agent를 한번 체험해보시길 추천합니다.

---

*참고 자료*
- [Hermes Agent 공식 문서](https://hermes-agent.nousresearch.com/docs/)
- [GitHub 저장소](https://github.com/NousResearch/hermes-agent)
- [Hermes Agent Self-Evolution 저장소](https://github.com/NousResearch/hermes-agent-self-evolution)
- [GitHub 릴리즈 노트](https://github.com/NousResearch/hermes-agent/releases)
- [Changelog](https://hermes-ai.net/changelog/)
- 확인일: 2026-07-31
