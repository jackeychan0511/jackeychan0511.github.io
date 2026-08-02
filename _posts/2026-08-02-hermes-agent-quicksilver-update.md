---
layout: post
title: "Hermes Agent 추천: v0.19.1 이후 최신 소식 + GitHub 스타 22만 돌파, Nous Research 1.5조 투자 유치 총정리 (2026.8.2)"
date: 2026-08-02 21:30:00 +0900
categories: [career]
tags: [HermesAgent, NousResearch, AI에이전트, 오픈소스, v0.19.0, Quicksilver, v0.19.1, v0.20.0, 투자, 밸류에이션, 스타22만, AI업데이트, 커리어]
author: "40대 블로거"
image: /assets/images/posts/hermes-agent-aug2026-20260802/hermes-agent-og-official.png
description: "2026년 8월 2일 기준 Hermes Agent 최신 소식을 정리했습니다. GitHub 스타 22만 개 돌파, Nous Research의 15억 달러(약 2조 원) 밸류에이션 투자 유치 보도, 7월 30일 v0.19.1 패치 릴리즈 이후 상황, 그리고 다음 정식 릴리즈 v0.20.0 예고까지 한 자리에 모았습니다."
---

요즘 저처럼 오픈소스 AI 에이전트에 꽂혀서 소식을 매일 체크하시는 분들, Hermes Agent 근황 궁금하시죠? 지난주에 [7월 업데이트 총정리](/2026/07/31/hermes-agent-july-updates-2026/)와 [v0.19.1 패치 + 펫 마스코트](/2026/08/01/hermes-agent-v0191-pets-update/) 글을 올렸는데, 그 사이에 **확실히 짚고 넘어가야 할 큰 소식**이 두 개나 나왔습니다.

하나는 **GitHub 스타 22만 개 돌파**, 그리고 다른 하나는 **Nous Research가 약 1.5조 원(15억 달러) 밸류에이션으로 투자 유치를 진행 중**이라는 뉴스입니다. 오늘은 이 두 소식과 함께, 어제 오늘 기준 Hermes Agent의 **정확한 최신 상태**를 한 자리에 정리해드립니다.

![Hermes Agent 공식 홈페이지 대표 이미지](/assets/images/posts/hermes-agent-aug2026-20260802/hermes-agent-og-official.png)
*Hermes Agent — Nous Research가 만든 오픈소스 AI 에이전트 (출처: hermes-agent.nousresearch.com, 확인일 2026-08-02)*

---

## 1. GitHub 스타 22만 개 돌파 — 오픈소스 에이전트 중 가장 빠른 성장

지난 글에서 "5개월 만에 스타 18만 개"라고 말씀드렸는데, 그 숫자가 **8월 2일 현재 224,097개**까지 올라왔습니다. 불과 열흘 사이에 4만 개 넘게 늘어난 셈인데요, 제가 8월 2일 오후에 GitHub API로 직접 확인한 수치입니다.

| 지표 | 수치 (2026-08-02 확인) |
|:----|:----|
| ⭐ GitHub Stars | **224,097** |
| 🍴 Forks | **43,279** |
| 📝 라이선스 | MIT (완전 오픈소스) |
| 🏗️ 최신 정식 릴리즈 | **v0.19.1 (v2026.7.30)** |
| 📅 최초 릴리즈 | 2026년 2월 25일 |

솔직히 이 성장세는 단순히 "인기가 많다"를 넘어섭니다. **개발 활동 자체가 멈추지 않고 있습니다.** 8월 2일 오전(UTC 기준)에도 게이트웨이 안정화, 프로세스 정리(reap) 로직 수정 등 **커밋이 계속 올라오고** 있어요. 프로젝트가 "출시 후 방치"되는 게 아니라 매일 굴러가고 있다는 신호입니다.

![Hermes Agent 공식 배너](/assets/images/posts/hermes-agent-aug2026-20260802/hermes-agent-banner-official.png)
*Hermes Agent 공식 배너 — "The agent that grows with you" (출처: 공식 GitHub 저장소 assets/banner.png, 확인일 2026-08-02)*

---

## 2. Nous Research, 15억 달러 밸류에이션 투자 유치 보도 — 무슨 의미인가?

지난주(7월 중순경) 해외 IT 매체들이 일제히 보도한 소식입니다. **Nous Research가 7,500만 달러(약 1,000억 원) 이상의 신규 투자를 유치하는 과정에 있으며, 기업 가치를 15억 달러(약 2조 원)로 평가받고 있다**는 내용이에요. 리드 투자자는 로봇 벤처스(Robot Ventures), 미국의 유명 VC인 USV(Union Square Ventures)도 참여하는 것으로 알려졌습니다.

### 이 뉴스가 왜 의미가 있나?

- **오픈소스 에이전트가 "돈 버는 사업"으로 인정받는 신호** — OpenClaw에 이어 Hermes Agent를 만든 Nous Research도 자본시장의 주목을 받기 시작했다는 뜻입니다.
- **개발 속도가 꺾이지 않을 것** — 투자 유치가 성사되면 팀 규모 확대와 인프라 투자가 가능해져, 에이전트 기능 발전 속도에 긍정적 영향을 줍니다.
- **사용자 입장에선?** — Hermes Agent는 여전히 MIT 라이선스의 무료 오픈소스입니다. 밸류에이션이 올라간다고 해서 갑자기 유료화될 가능성은 낮지만, **Nous Portal(유료 구독) 같은 부가 서비스가 더 확장될** 가능성은 있습니다.

참고로 이 보도는 "진행 중인 협상" 단계라 아직 공식 확정 발표는 아닙니다. 그래도 오픈소스 AI 생태계가 어디로 가고 있는지 보여주는 좋은 신호로 읽을 수 있습니다.

---

## 3. 현재 최신 버전은 v0.19.1 — 다음은 v0.20.0 예고

현재 공식 최신 릴리즈는 **7월 30일 배포된 v0.19.1 (v2026.7.30)** 입니다. 지난 글에서 패치 규모(약 1,000+ PR)를 소개해드렸는데, 공식 릴리즈 노트의 정확한 수치를 다시 확인해보니 이렇습니다.

> **v0.19.0(7/20) → v0.19.1(7/30) 사이:** 커밋 약 2,789개 · 변경 파일 약 4,748개 · 추가 44만 2,000줄 / 삭제 39만 2,300줄

게이트웨이, 음성 서브시스템, 데스크톱 앱, 인스톨러 버그 수정이 주를 이루고, **Buzz/Nostr 채널, FLUX3 비디오 생성·전달, 텔레그램 미디어 안정성** 같은 작업도 포함됐습니다.

그리고 공식 릴리즈 노트에 **v0.20.0 예고**가 명시돼 있습니다.

> "이 기간의 전체 큐레이션 릴리즈 노트는 **v0.20.0**과 함께 배포될 예정이며, v0.19.0 이후의 모든 하이라이트와 기능 영역, 컨트리뷰터 크레딧을 문서화합니다."

즉 **다음 정식 릴리즈는 v0.20.0**이고, 그때 7월 말부터 쌓인 변경점이 제대로 정리된 릴리즈 노트가 나옵니다. 업데이트 주기로 보면 **8월 안에 v0.20.0이 나올 가능성이 높아** 보입니다.

![Hermes Agent 웹 대시보드 — 세션 관리 화면](/assets/images/posts/hermes-agent-aug2026-20260802/hermes-dashboard-sessions.png)
*Hermes Agent 웹 대시보드 실제 화면 — 브라우저에서 세션·채널·메모리를 관리 (출처: 공식 GitHub 저장소 website/static/img, 확인일 2026-08-02)*

---

## 4. 놓치기 아쉬운 v0.19.0 Quicksilver 기능 3가지

7월 20일 나온 **v0.19.0 Quicksilver**는 "속도"가 핵심이었습니다. 콜드스타트가 4.3초 → 0.9초로 80% 줄어든 게 대표적이죠. 그런데 이 릴리즈에는 **아직 널리 소개되지 않은 실용 기능**이 몇 개 더 있습니다. 제 기준으로 골라봤습니다.

### ① 터미널에서 바로 구독 관리 — `/subscription`, `/topup`

예전에는 Nous 구독 플랜을 바꾸려면 결제 웹사이트에 들어가야 했는데, 이제 **TUI나 CLI에서 바로** 가능합니다. 현재 플랜과 남은 사용량 확인, 업그레이드 비용 미리보기("$46.30 지불하고 업그레이드"), 다운그레이드 적용 시점까지 터미널 안에서 처리돼요. 데스크톱 앱에도 결제 설정 탭이 추가됐습니다.

### ② 비밀번호 관리자 연동 — Bitwarden & 1Password

API 키를 더 이상 평문 `.env` 파일에 둘 필요가 없어졌습니다. **Bitwarden과 1Password를 비밀번호 소스(SecretSource)로 연결**하면, 에이전트가 로드할 때 자격증명을 자동으로 가져옵니다. 여러 볼트 동시 활성화, 충돌 경고, 변수별 출처 추적까지 지원합니다. 개인적으로 가장 반가운 기능이에요. 보안에 민감한 분들에게 특히 추천합니다.

### ③ 완료된 답변이 사라지지 않는 "전달 원장(Delivery Ledger)"

게이트웨이가 응답을 생성한 직후 크래시가 나면, 예전에는 **결제된 턴의 결과물이 조용히 유실**되는 경우가 있었습니다. v0.19.0부터는 최종 응답을 `state.db`의 **durable 원장(ledger)** 에 기록해, 크래시가 나도 결과가 복원되고 전달됩니다. 밤새 자동화를 돌리는 분에게는 정말 중요한 안정성 개선입니다.

이 밖에도 **서브에이전트 라이브 트랜스크립트**(실행 즉시 `tail -f`로 진행 상황을 볼 수 있음), **Smart Approval 기본 적용**, **사용자 정의 거부 규칙(Deny Rules)** 도 v0.19.0에 포함돼 있습니다.

---

## 5. 지금 Hermes Agent를 시작한다면? — 솔직한 판단 기준

**이미 쓰고 계신 분**
- `hermes update` 한 줄이면 v0.19.1로 올라갑니다. 게이트웨이·음성 쪽 버그가 많았던 분이라면 체감이 클 수 있습니다.
- v0.20.0이 곧 나올 예정이니, **급하지 않으면 다음 릴리즈 노트를 보고 한 번에 정리**하는 것도 방법입니다.

**아직 안 써보신 분**
- MIT 라이선스, 무료, 설치 2분. 지금이 시작하기 가장 좋은 타이밍입니다.
- 처음부터 모든 기능을 켜기보다 **Blank Slate 모드**로 최소 권한부터 시작하고, 필요한 도구만 추가하는 걸 추천합니다.

```bash
# 최신 버전 업데이트
hermes update

# 새로 설치
curl -fsSL https://hermes-agent.nousresearch.com/install.sh | bash

# 설치 상태 진단
hermes doctor
```

![Hermes Agent 웹 대시보드 — MCP 카탈로그 화면](/assets/images/posts/hermes-agent-aug2026-20260802/hermes-dashboard-mcp.png)
*Hermes Agent 웹 대시보드의 MCP 카탈로그 실제 화면 (출처: 공식 GitHub 저장소 website/static/img, 확인일 2026-08-02)*

---

## 마무리 — 핵심만 다시 정리하면

1. **GitHub 스타 224,097개 (8/2 기준)** — 열흘 만에 4만 개 이상 증가, 8월 2일에도 커밋 활동 지속
2. **Nous Research, 15억 달러 밸류에이션 투자 유치 보도** — Robot Ventures 리드, USV 참여 (진행 중인 협상)
3. **최신 정식 릴리즈 v0.19.1 (7/30)** — 게이트웨이·음성·데스크톱·인스톨러 안정화 중심
4. **다음은 v0.20.0** — v0.19.0 이후 전체 변경점이 공식 문서화될 예정
5. **v0.19.0의 숨은 기능** — `/subscription`, Bitwarden/1Password 연동, 전달 원장(durable ledger)

저처럼 **오픈소스 AI 에이전트를 직접 운영하시는 분**, 아니면 이제 막 관심을 갖기 시작한 분이라면, 지금의 Hermes Agent 성장 속도는 꽤 인상적입니다. 스타 22만 개, 투자 유치, 그리고 8월 중 v0.20.0 예고까지 — **가성비 좋은 오픈소스 AI 에이전트를 찾는 분께 추천**합니다.

관련해서 지난 글([2026년 7월 Hermes Agent 업데이트 총정리](/2026/07/31/hermes-agent-july-updates-2026/), [Hermes Agent v0.19.1 + 펫 마스코트](/2026/08/01/hermes-agent-v0191-pets-update/))도 함께 읽어보시면 전체 그림이 잡힙니다. v0.20.0이 나오는 대로 다시 찾아뵙겠습니다.

---

*참고 자료*
- [Hermes Agent 공식 홈페이지](https://hermes-agent.nousresearch.com/)
- [Hermes Agent 공식 GitHub 저장소 (스타·포크 수치, 2026-08-02 확인)](https://github.com/NousResearch/hermes-agent)
- [GitHub 릴리즈 노트 — v0.19.0 Quicksilver / v0.19.1](https://github.com/NousResearch/hermes-agent/releases)
- [MasterNodeAI — Nous Research raising $75M+ at $1.5B valuation](https://www.masternodeai.com/en/news/nous-research-hermes-agent-75m-1-5b-valuation)
- [Times of India — Nous Research funding talks at $1.5B valuation](https://timesofindia.indiatimes.com/technology/tech-news/nous-research-maker-of-openclaws-american-rival-hermes-agent-in-talks-to-raise-new-funding-at-1-5-billion-valuation/articleshow/132389419.cms)
- 확인일: 2026-08-02
