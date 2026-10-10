---
layout: post
title: "Hermes Agent 보안 취약점 정리 — PKCE 세션 탈취(CVSS 8.8) 발견, v0.21.6 업데이트로 긴급 패치 (2026.10.9)"
date: 2026-10-11 07:55:00 +0900
categories: [career]
tags: [HermesAgent, NousResearch, 보안취약점, CVE, PKCE, 세션탈취, 업무자동화, AI에이전트, Tenable, v0216, 보안업데이트, 2026년10월]
author: "40대 블로거"
image: /assets/images/posts/2026-10-11-hermes-agent-pkce-session-takeover-20261011/hermes-agent-official-og.png
description: "2026년 10월 9일, 보안업체 Tenable이 오픈소스 AI 에이전트 Hermes Agent에서 PKCE 세션 탈취 취약점(TRA-2026-65, CVSS 8.8)을 공개했습니다. 0.21.6 미만 버전이 영향을 받고, 10월 8일 배포된 v0.21.6에서 수정됐습니다. 업무 자동화 파이프라인을 운영하는 분들이라면 꼭 확인해야 할 내용만 정리했습니다."
---

## AI 에이전트를 업무에 붙여 쓰시는 분들, 이번 건은 그냥 넘기면 안 됩니다

요즘 자동화 파이프라인을 운영하는 분들이라면, 챗봇 하나 띄우는 수준을 넘어 **서버에 상주하면서 스스로 명령을 실행하는 AI 에이전트**를 하나쯤 굴리고 계실 겁니다. 크론잡으로 정해진 시간에 작업을 돌리고, 메신저로 지시를 던지면 알아서 결과를 돌려주는 구조죠. 편한 만큼, 그 에이전트의 **로그인 세션**이 곧 내 서버의 열쇠가 됩니다.

문제는 이 세션을 뺏어가는 취약점이 **공개**됐다는 점입니다. 2026년 10월 9일, 보안업체 **Tenable**이 오픈소스 AI 에이전트 **Hermes Agent**(Nous Research 개발)에서 **PKCE 세션 탈취 취약점**을 공개했습니다. 권고 번호는 **TRA-2026-65**, 위험도는 **High**입니다.

![Hermes Agent 공식 홈페이지 랜딩 이미지](/assets/images/posts/2026-10-11-hermes-agent-pkce-session-takeover-20261011/hermes-agent-official-og.png)
*Hermes Agent 공식 사이트(hermes-agent.nousresearch.com) 랜딩 이미지 — 출처: Nous Research 공식 사이트*

업무 자동화에 AI 에이전트를 얹어 쓰는 분들이라면, 결론부터 말하면 **0.21.6 미만 버전을 쓰고 있으면 지금 바로 업데이트**해야 합니다. 이유를 하나씩 짚어보겠습니다.

## 무슨 취약점인가 — "파서가 서로 다른 말을 한다"

이 취약점의 정식 이름은 **"PKCE Session Takeover via Redirect-URI Parser Confusion"**, 우리말로 하면 **리다이렉트 URI 파서 혼동을 통한 PKCE 세션 탈취**입니다.

핵심은 Hermes Agent의 OAuth 로그인 흐름에 있습니다. Hermes는 브라우저 기반 로그인을 붙일 때 `GET /auth/native/authorize` 엔드포인트를 씁니다. 이때 로그인 후 돌아갈 주소(**redirect_uri**)를 검증하는데, 문제는 **검증하는 주체와 실제로 그 주소를 여는 주체가 서로 다른 파서(parser)를 쓴다**는 점입니다.

- **서버 쪽**: Python의 `urllib.parse.urlparse`로 redirect_uri를 검증
- **브라우저 쪽**: WHATWG 표준 파서로 실제 URL을 열어 실행

두 파서는 **백슬래시(`\`)**를 서로 다르게 해석합니다. 그래서 서버는 "안전한 호스트"라고 판단한 URL을 브라우저 쪽은 "공격자의 호스트"로 열어버리는, 소위 **파서 혼동(parser confusion)** 상황이 벌어집니다.

결과적으로 서버가 검증했다고 믿는 주소로 **인증 토큰이 흘러가고**, 공격자는 그 토큰으로 **세션을 통째로 가로챌 수 있습니다.** Tenable은 이 취약점에 **CVSS v3 8.8 / CVSS v4 8.6**이라는 높은 점수를 매겼습니다.

| 항목 | 내용 |
|:---|:---|
| 권고 번호 | TRA-2026-65 (Tenable Research Advisory) |
| 취약점 유형 | PKCE 세션 탈취 (Redirect-URI Parser Confusion) |
| CVSS v3 (Base/Temporal) | 8.8 — AV:N/AC:L/PR:N/UI:R/S:U/C:H/I:H/A:H |
| CVSS v4 (Base) | 8.6 |
| 위험도 | High |
| 영향 버전 | Hermes Agent **0.21.6 미만** |
| 발견자 | Joshua Martinelle (Tenable) |

## 영향 범위 — 0.21.6 미만은 전부 해당

Tenable 권고에 명시된 **영향받는 버전은 Hermes Agent 0.21.6 미만**입니다. 즉, 비교적 최근 릴리스였던 0.21.5, 0.21.4 같은 버전을 쓰고 있었다면 이 취약점에 노출돼 있었다는 뜻입니다.

Hermes Agent를 **서버에 상주시켜 메신저·웹 로그인과 연동**해 쓰는 구성이라면 영향이 더 큽니다. 세션이 곧 에이전트의 권한이기 때문에, 세션이 탈취되면 그 에이전트가 접근할 수 있는 **파일·터미널·메시징 채널**이 그대로 넘어갈 수 있습니다.

![Hermes Agent v0.21.6 GitHub 릴리스 페이지](/assets/images/posts/2026-10-11-hermes-agent-pkce-session-takeover-20261011/hermes-v0216-github-release.png)
*Hermes Agent v0.21.6 GitHub 릴리스(2026.10.8) — 출처: github.com/NousResearch/hermes-agent/releases*

## 해결책 — v0.21.6으로 업데이트하면 끝

다행히 패치는 이미 나와 있습니다. Nous Research는 **2026년 10월 8일**에 **Hermes Agent v0.21.6**을 배포하면서 이 취약점을 수정했습니다. 이번 태그에는 **약 2,100여 개의 PR**이 롤업됐고, 그중 **세 개의 보안 수정 그룹**이 포함된 것으로 정리돼 있습니다.

즉, **v0.21.6 이상으로 올리면 이 건은 해결**됩니다. 방법은 간단합니다.

```bash
# git 설치 사용자
hermes update

# 또는 설치 스크립트 재실행
```

업데이트 후에는 아래 명령으로 **실제 반영된 버전**을 꼭 확인하는 게 좋습니다.

```bash
hermes version
# 0.21.6 이상이면 OK
```

참고로 Tenable 권고의 **공개 타임라인**을 보면, 취약점은 2026년 9월 초에 Nous Research에 먼저 제보됐고(9월 3일 1차 연락), 협의를 거쳐 **10월 9일**에 일반 공개됐습니다. 연구자뿐 아니라 **Nous Research 측도 같은 취약점을 별도로 인지**하고 있었다는 점이 권고문에 함께 적혀 있습니다.

## 업데이트가 어렵다면 — 최소한 이건 해두시길

버전 업데이트가 당장 어려운 환경이라면, 노출을 줄이는 것만으로도 위험을 크게 낮출 수 있습니다.

- **외부 노출 최소화**: `/auth/native/authorize` 같은 인증 엔드포인트를 인터넷에 그대로 열어두지 말고, 가능한 한 **VPN·사설망 뒤**에 둡니다.
- **세션 토큰 로테이션**: 의심되는 시점이 있다면 OAuth 세션을 **재발급**하고, 기존 토큰을 무효화합니다.
- **자동 업데이트 점검**: `hermes update --check` 같은 프리체크 명령으로 새 버전이 나왔는지 주기적으로 확인합니다.
- **로그 감사**: 리다이렉트 관련 비정상 요청(백슬래시가 섞인 redirect_uri 등)이 로그에 찍혔는지 살펴봅니다.

솔직히, AI 에이전트는 "편하니까" 쓰는 도구라서 보안은 뒤로 밀리기 쉽습니다. 그런데 상주형 에이전트는 **권한이 곧 세션**이라는 점에서, 일반 앱 취약점보다 훨씬 무겁게 다뤄야 합니다.

## 정리 — 5분만 투자하면 되는 일

- 2026년 10월 9일, **Tenable이 TRA-2026-65** 공개 (PKCE 세션 탈취, **CVSS 8.8**)
- **영향 버전: Hermes Agent 0.21.6 미만**
- **패치: 10월 8일 배포된 v0.21.6** (세 개 보안 수정 그룹 포함)
- 할 일: **`hermes update` 실행 → `hermes version`으로 0.21.6 이상 확인**

업무 자동화 파이프라인을 돌리면서 AI 에이전트를 실사용하는 분들이라면, 이번 주 안에 버전 한 번만 확인해 보시길 권합니다. 5분 투자로 세션 탈취 위험을 정리할 수 있는, 가성비 좋은 보안 점검입니다.

![Hermes Agent 사이드 배너 이미지](/assets/images/posts/2026-10-11-hermes-agent-pkce-session-takeover-20261011/hermes-agent-official-hero.webp)
*Hermes Agent 공식 사이트 배너 이미지 — 출처: Nous Research 공식 사이트*

> 참고 자료
> - Tenable Research Advisory TRA-2026-65 — Hermes Agent PKCE Session Takeover via Redirect-URI Parser Confusion (2026.10.9)
> - Hermes Agent GitHub Releases — v0.21.6 (2026.10.8), PR #130685
