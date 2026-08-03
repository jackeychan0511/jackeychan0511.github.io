---
layout: post
title: "Hermes Agent 추천: 이제 안드로이드 폰에서도? 공식 Termux 지원 + 스타 22.4만 돌파 총정리 (2026.8.3)"
date: 2026-08-03 09:40:00 +0900
categories: [career]
tags: [HermesAgent, NousResearch, AI에이전트, 오픈소스, Termux, 안드로이드, v0.19.1, v0.20.0, 팩트체크, AI업데이트, 커리어]
author: "40대 블로거"
image: /assets/images/posts/hermes-agent-android-termux-20260803/hermes-official-hero.webp
description: "2026년 8월 3일 기준 Hermes Agent 최신 소식입니다. 가장 큰 변화는 안드로이드 폰에서 공식적으로 쓸 수 있게 된 Termux 지원(공식 문서 등재)입니다. 여기에 GitHub 스타 22만 4,313개 돌파, 8월 2일 새벽에 올라온 팩트체크 모드·세션 워치독 커밋, v0.20.0 예고 상태까지 한 자리에 정리했습니다."
---

요즘 저처럼 오픈소스 AI 에이전트를 폰에서도 켜두고 싶다고 생각하시는 분들, 계시죠? 어제 [Hermes Agent 스타 22만 돌파 + 투자 유치 소식](/2026/08/02/hermes-agent-quicksilver-update/)을 정리해드렸는데, 그 글을 쓰고 **하루 만에 또 짚어야 할 소식**이 나왔습니다.

바로 **안드로이드 폰 공식 지원(Termux)** 입니다. 공식 문서에 전용 설치 가이드가 등재됐고, 공식 설치 스크립트가 Termux 환경을 자동 인식하도록 바뀌었습니다. "폰에서도 되는 거냐"는 질문에 공식적으로 답이 생긴 셈이죠. 오늘은 이 소식과 함께, 8월 3일 아침 기준으로 확인한 **정확한 수치와 최신 커밋**까지 정리해드립니다.

![Hermes Agent 공식 홈페이지 대표 이미지](/assets/images/posts/hermes-agent-android-termux-20260803/hermes-official-hero.webp)
*Hermes Agent — "The agent that grows with you" (출처: hermes-agent.nousresearch.com 공식 홈페이지, 확인일 2026-08-03)*

---

## 1. 가장 큰 소식: 안드로이드 폰(Termux) 공식 지원 시작

8월 2~3일 사이, Hermes Agent 공식 문서에 **"Android / Termux" 전용 가이드**가 등재됐습니다. 설치 가이드 첫머리의 설치 명령도 `Linux / macOS / WSL2 / Android (Termux)`로 확장됐고, 공식 GitHub 저장소 README에도 Termux 설치 경로가 정리돼 있어요.

다만 솔직히 말씀드리면, **"공식 지원"이라 해도 모든 기능이 되는 건 아닙니다.** 공식 문서상 Termux는 **Tier 2 플랫폼**으로 분류됩니다. Tier 1(macOS, Windows, Linux, Docker)은 "절대 설치·업데이트를 깨지 않도록 최우선 관리"하는 반면, Tier 2는 **"최선을 다해 유지하지만 릴리즈로 깨질 수 있다"**는 수준이에요. 폰에서 돌아간다는 건 확인된 사실이지만, 데스크톱과 똑같은 경험을 기대하면 안 된다는 뜻입니다.

![Termux 안드로이드 폰 공식 스크린샷](/assets/images/posts/hermes-agent-android-termux-20260803/termux-fdroid-screen1.jpg)
*Termux — 안드로이드에서 동작하는 터미널 에뮬레이터 (출처: F-Droid 공식 앱 페이지 스크린샷, 확인일 2026-08-03)*

### 공식 검증된 설치 경로에서 지원하는 것

공식 문서에 따르면 Termux 번들 설치 시 다음이 동작합니다.

| 지원 항목 | 비고 |
|:----|:----|
| ✅ Hermes CLI | 폰에서 직접 `hermes` 명령 실행 |
| ✅ cron (자동화) | 예약 작업 실행 |
| ✅ PTY/백그라운드 터미널 | `termux-api`와 연계 |
| ✅ 텔레그램 게이트웨이 | 수동/최선 노력(best-effort) 백그라운드 |
| ✅ MCP | MCP 서버 연동 |
| ✅ Honcho 메모리 | 장기 기억 기능 |
| ✅ ACP | 에이전트 통신 프로토콜 |

### 아직 안 되는 것 (공식 문서 기준)

- `. [all]` 전체 확장 설치는 안드로이드에서 미지원
- 음성(voice) 확장 — `faster-whisper → ctranslate2`가 안드로이드 휠(wheel)을 제공하지 않아 차단
- 브라우저/Playwright 자동 부트스트랩 — Termux 설치기에서 생략
- Docker 기반 터미널 격리 — Termux 내부에서는 사용 불가
- 안드로이드가 Termux 백그라운드 작업을 중단시킬 수 있어 게이트웨이 상시 구동은 best-effort

쉽게 말하면 **"폰에서 CLI 에이전트로 쓰기엔 충분하지만, 데스크톱/서버급 풀 세팅은 아니다"**입니다. 공식 문서도 "모바일 설치는 의도적으로 더 좁은 범위"라고 명시하고 있어요.

---

## 2. 설치 방법 — 공식 문서 기준 한 줄 요약

공식 가이드의 설치 명령은 데스크톱과 **완전히 동일**합니다.

```bash
curl -fsSL https://hermes-agent.nousresearch.com/install.sh | bash
```

이 스크립트가 Termux 환경을 감지하면 자동으로:

1. 시스템 패키지를 `pkg`로 설치
2. `python -m venv`로 가상환경 생성
3. 넓은 범위의 `. [termux-all]` 먼저 시도 → 실패 시 `. [termux]` → 기본 설치로 폴백
4. `hermes`를 `$PREFIX/bin`에 링크 (Termux PATH 유지)
5. 미검증된 브라우저/WhatsApp 부트스트랩은 건너뜀

수동 설치는 `pkg install -y git python clang rust make pkg-config libffi openssl nodejs ripgrep ffmpeg` 후 공식 문서의 매뉴얼 경로를 따르면 됩니다. 주의할 점은 `ANDROID_API_LEVEL` 환경변수 설정 — Rust/maturin 기반 패키지(`jiter` 등) 빌드에 필요하다고 문서에 명시돼 있습니다.

![Termux 공식 사이트 htop 실행 화면](/assets/images/posts/hermes-agent-android-termux-20260803/termux-official-htop.png)
*Termux 공식 홈페이지에 게시된 실제 실행 화면 — 폰에서 시스템 모니터링 (출처: termux.dev, 확인일 2026-08-03)*

---

## 3. GitHub 스타 224,313개 — 어제보다 +216, 아침에도 커밋 중

8월 3일 오전, GitHub API로 직접 확인한 현재 수치입니다.

| 지표 | 수치 (2026-08-03 09:20 KST 확인) |
|:----|:----|
| ⭐ GitHub Stars | **224,313** (어제 224,097 → +216) |
| 🍴 Forks | **43,374** |
| 📝 라이선스 | MIT (완전 오픈소스) |
| 🏗️ 최신 정식 릴리즈 | **v0.19.1 (v2026.7.30)** |
| 🕐 마지막 push | **2026-08-03 00:20 UTC** (한국 시간 오전 9시 20분 경) |

솔직히 하루 +216개면 "폭발적"이라기보다 **꾸준한 일일 성장** 수준이지만, 중요한 건 수치가 아니라 **개발이 멈추지 않는다는 것**입니다. 제가 확인한 시각(한국 오전 9시 20분 = UTC 00:20)에도 리포지토리에 push가 있었어요. 주말·휴일 없이 돌아가는 프로젝트라는 뜻입니다.

---

## 4. 8월 2일 새벽 커밋 하이라이트 3가지

어제 밤~오늘 새벽(UTC 기준 8/2)에 올라온 커밋 중, 일반 사용자에게 의미 있는 것만 골랐습니다.

### ① 스킬에 "팩트체크 모드" 추가 (grounded-citations)

`feat(skills): add fact-checking mode to grounded-citations` — 근거 인용(grounded citations) 스킬에 **팩트체크 모드**가 추가됐습니다. 이어서 `fix(skills): match evidence quotes through markdown markup` 커밋으로 인용문 매칭 정확도도 보정했어요. AI가 쓴 글의 출처를 검증하는 작업을 에이전트가 스스로 더 정확히 하도록 만든 변경입니다. 블로그·리서치 작업을 자동화하는 분께는 직접 체감되는 업데이트입니다.

### ② 게이트웨이 "세션 활동 워치독" (session activity watchdog)

`feat(gateway): session activity watchdog, stall notify, compress timeout (#72424)` — 오래된 세션 활동 감시, 응답 멈춤(stall) 시 알림, 압축 타임아웃을 한 번에 묶은 기능입니다. 밤새 자동화를 돌리는 분이 "응답이 왜 안 오지?" 하고 깨는 일이 줄어들 것으로 보입니다. 관련 설정 키(`agent.session_stall_timeout`, 압축 타임아웃) 문서화 커밋도 함께 올라왔어요.

### ③ 압축 풀(compression pool) 안정화 대공사

`SCHEMA_VERSION 24`로 올라가며 활동 추적 컬럼이 추가됐고, durable compression lease(압축 중간에 자원을 놓치지 않도록 하는 잠금)부터 `pool_saturated` 텔레메트리까지 **컨텍스트 압축 관련 버그 수정이 10건 이상** 연속으로 올라왔습니다. 긴 세션에서 "갑자기 기억이 리셋되는" 문제를 겪던 분이라면 눈여겨볼 부분입니다. 덤으로 `perf(agent,gateway): back cancel-wait polls off from 1ms to 25ms` — 취소 대기 폴링을 1ms → 25ms로 늦춰 CPU 부담을 줄이는 최적화도 포함됐어요.

![Hermes Agent 공식 데스크톱 쇼케이스](/assets/images/posts/hermes-agent-android-termux-20260803/hermes-official-showcase.webp)
*Hermes Agent 데스크톱 앱 공식 쇼케이스 이미지 (출처: hermes-agent.nousresearch.com, 확인일 2026-08-03)*

---

## 5. 다음 정식 릴리즈는 여전히 v0.20.0

현재 공식 최신 릴리즈는 **7월 30일의 v0.19.1** 그대로입니다. 공식 릴리즈 노트에는 "이 기간 전체의 큐레이션 릴리즈 노트는 **v0.20.0**과 함께 배포되며, v0.19.0 이후의 모든 하이라이트·기능 영역·컨트리뷰터 크레딧을 문서화한다"고 명시돼 있습니다.

어제 글에서 "8월 안에 나올 가능성이 높다"고 말씀드렸는데, 8월 2일에도 이렇게 커밋이 쏟아지고 있으니 **8월 중순~하순 사이 v0.20.0** 전망은 여전히 유효합니다. 급하지 않으시면 정식 릴리즈 노트와 함께 한 번에 업데이트하셔도 좋습니다.

---

## 마무리 — 오늘 소식 한 줄 정리

1. **안드로이드 폰 공식 지원 시작** — Termux 전용 가이드 등재, 설치 스크립트 자동 감지. 단 Tier 2 수준(음성·브라우저·Docker 제외)
2. **스타 224,313개 (8/3 오전)** — 어제보다 +216, 확인 시각에도 push 중
3. **8/2 새벽 커밋** — 팩트체크 모드, 세션 활동 워치독, 압축 풀 안정화
4. **다음 릴리즈 v0.20.0** — 8월 중순~하순 예상, 전체 릴리즈 노트 동봉 예정

저처럼 **폰에서도 가볍게 AI 에이전트를 돌려보고 싶은 분**에게는 Termux 지원이 꽤 반가운 소식입니다. 다만 "공식 지원 = 모든 기능"이 아니라는 점, 그리고 안드로이드 백그라운드 제한 때문에 상시 게이트웨이 운영은 아직 아쉽다는 점은 솔직하게 말씀드려야겠어요. 폰에서는 **CLI + cron + 텔레그램 연동** 위주로, 무거운 작업은 데스크톱이나 서버에서 돌리는 조합이 현실적입니다.

**가성비 좋은 오픈소스 AI 에이전트를 찾는 분께 추천**합니다. 지난 글([7월 업데이트 총정리](/2026/07/31/hermes-agent-july-updates-2026/), [v0.19.1 + 펫 마스코트](/2026/08/01/hermes-agent-v0191-pets-update/), [스타 22만 + 투자 유치](/2026/08/02/hermes-agent-quicksilver-update/))과 함께 읽으시면 흐름이 잡힙니다. v0.20.0 나오는 대로 다시 찾아뵙겠습니다.

---

*참고 자료*
- [Hermes Agent 공식 문서 — Android / Termux 가이드](https://hermes-agent.nousresearch.com/docs/getting-started/termux) (확인일 2026-08-03)
- [Hermes Agent 공식 문서 — Platform Support (Tier 분류)](https://hermes-agent.nousresearch.com/docs/getting-started/platform-support) (확인일 2026-08-03)
- [Hermes Agent 공식 GitHub 저장소 (스타·포크·push 수치, GitHub API 2026-08-03 확인)](https://github.com/NousResearch/hermes-agent)
- [GitHub 커밋 히스토리 — 2026-08-02 ~ 08-03](https://github.com/NousResearch/hermes-agent/commits/main)
- [Termux 공식 홈페이지](https://termux.dev/en/) / [F-Droid Termux 페이지](https://f-droid.org/packages/com.termux/) (확인일 2026-08-03)
- 확인일: 2026-08-03
