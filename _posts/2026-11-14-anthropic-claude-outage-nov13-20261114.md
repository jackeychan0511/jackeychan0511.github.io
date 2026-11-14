---
layout: post
title: "Anthropic Claude 최신 소식 — 하루에 두 번 멈춘 Claude, MS 365 장애까지 겹친 48시간 (2026.11.14)"
date: 2026-11-14 16:55:00 +0900
categories: [career]
tags: [Anthropic, Claude, 앤트로픽, 클로드, 서비스장애, ClaudeCode, ClaudeAPI, 인증장애, 커넥터, Microsoft365, Outlook, ClaudeCowork, ManagedAgents, 상태페이지, 업무자동화, 안정성, 재시도설계, 2026년11월]
author: "40대 블로거"
image: https://pub-a8e00ddf355f47339bd2e01ff3221de7.r2.dev/assets/images/posts/anthropic-claude-outage-nov13-20261114/anthropic-official.jpg
description: "2026년 11월 13일 Claude가 하루에 두 번 멈췄습니다. 09:58 UTC에는 claude.ai·Claude Code 로그인(인증)이 막혔고, 13:09 UTC에는 API·커넥터·Cowork·Meetings·Managed Agents까지 오류가 번졌습니다. 여기에 Microsoft 365 장애로 Claude의 메일·캘린더 커넥터가 약 24시간 37분 동안 흔들렸습니다. 상태페이지 타임라인, 두 장애의 성격 차이, 예약 작업 2시간 지연·일부 실행 스킵이 업무 자동화 파이프라인에 주는 영향, 재시도·멱등성 체크리스트를 정리했습니다."
---

![Claude 상태 페이지의 11월 13~14일 장애 기록](https://pub-a8e00ddf355f47339bd2e01ff3221de7.r2.dev/assets/images/posts/anthropic-claude-outage-nov13-20261114/claude-status-history.png)
*Claude 공식 상태 페이지(status.claude.com/history)의 장애 이력. 위에서부터 11월 14일 결제 오류, 11월 13일 Microsoft 365 커넥터 장애, 11월 13일 claude.ai·API·Claude Code 장애가 시간순으로 올라와 있습니다 (이미지: status.claude.com 화면 캡처, 2026년 11월 14일)*

> **📌 한 줄 요약:** **2026년 11월 13일**, Claude가 **하루에 두 번** 멈췄습니다. ① **09:58 UTC(한국시간 18:58)** 에는 **claude.ai와 Claude Code 로그인 자체가 막히는 인증 장애**가 2시간, ② **13:09 UTC(한국시간 22:09)** 에는 **API·커넥터·Cowork·Meetings·Managed Agents까지 번진 오류**가 2시간 29분 이어졌습니다. 여기에 별개로 **Microsoft 365 글로벌 장애**가 겹치면서 Claude의 **메일·캘린더·검색 커넥터가 약 24시간 37분** 동안 정상 동작하지 않았습니다. Anthropic은 **11월 14일 현재까지 원인을 공개하지 않았습니다.**

업무 자동화 파이프라인을 Claude 위에 얹어 돌리는 쪽에서는 이번 건이 "또 장애 났네"로 끝나지 않습니다. 상태 페이지에 적힌 문장 하나가 눈에 걸리기 때문입니다.

> "**Scheduled tasks and routines can run more than two hours late and some runs have been skipped**"
> — Claude 상태 페이지, 2026년 11월 13일 15:33 UTC 업데이트

예약 작업이 **2시간 이상 늦게 돌거나, 일부는 아예 건너뛰어졌다**는 뜻입니다. 크론잡을 돌려 무인으로 배치를 처리하는 구조라면, 이 문장은 곧 **"오늘 새벽 작업이 조용히 사라졌을 수 있다"** 는 의미입니다.

이 글은 **① 무슨 일이 있었나(타임라인) → ② 두 장애는 성격이 다르다 → ③ MS 365 연쇄 장애 → ④ 업무 자동화 관점의 진짜 리스크 → ⑤ 영향(사용자/개발자) → ⑥ 대비 체크리스트 → ⑦ 전망** 순서로 정리합니다.

---

## 1. 무슨 일이 있었나 — 48시간 타임라인

Claude 상태 페이지에 공개된 기록을 한국시간 기준으로 다시 정리했습니다.

| 시각 (KST) | 사건 | 등급 | 지속 |
|:---|:---|:---|:---|
| 11/13 01:03 | Microsoft 365 장애로 **커넥터 메일·캘린더·검색 오류** 시작 | major | **24시간 37분** |
| 11/13 18:58 | **claude.ai·Claude Code 로그인(인증) 실패** — claude.ai 전체 장애, Code·Console 부분 장애 | major | **2시간** |
| 11/13 20:05 | 수정본 배포, 모니터링 전환 | — | — |
| 11/13 20:58 | 1차 장애 종료 | — | — |
| 11/13 22:09 | **2차 장애** — claude.ai, Claude API, Claude Code, 커넥터(Docs·Sheets), Cowork, Meetings, Managed Agents, Claude Security | major | **2시간 29분** |
| 11/14 00:33 | 회복 신호. 단 **예약 작업 2시간 이상 지연·일부 실행 스킵** 명시 | — | — |
| 11/14 00:38 | 2차 장애 종료 | — | — |
| 11/14 06:49 | Microsoft 365 커넥터 장애 종료 (Microsoft 복구는 계속 진행 중) | — | — |
| 11/14 11:54~13:23 | **결제·플랜 변경·해지 오류** (핵심 기능은 정상) | major | 1시간 29분 |
| 11/14 16:11 | iOS 앱 **Apple 로그인 신규 가입** 문제 — 조사 중 | major | 진행 중 |

숫자로만 보면 이렇습니다.

- **11월 13일 하루에 major 등급 장애 2건**, 합산 **4시간 29분**
- **Microsoft 365 커넥터는 사실상 하루 종일** 흔들림 (약 24시간 37분)
- **11월 들어 상태 페이지에 올라온 장애 7건**, 그중 **major 등급이 5건**

11월 8일 Fable 5.1 성능 저하(26분), 11월 10일 Fable 5.2·Mythos 5.2 과부하(4분)까지 포함하면, **2주 사이에 일곱 번**입니다. 신모델을 내놓은 직후의 트래픽과 무관하지 않아 보이는 흐름입니다.

---

## 2. 두 장애는 성격이 다르다 — 인증 vs 추론

이번 건에서 가장 중요한 구분은 **"모델이 죽었나"와 "로그인이 죽었나"** 입니다. 1차와 2차는 공격 범위가 다릅니다.

### ① 1차(18:58) — 인증 계층 장애

Anthropic이 처음 올린 문구는 이렇습니다.

> "We are investigating an issue with users authenticating to Claude.ai and Claude Code."
> — 2026년 11월 13일 09:58 UTC

**모델 추론이 아니라 세션·인증 경로**가 문제였습니다. 이때 상태판에는 **api.anthropic.com은 정상**으로 표시됐습니다. 즉 **API 키로 직접 호출하는 파이프라인은 살아 있었고, 웹과 IDE에서 로그인하려는 사람만 막힌** 구간입니다.

이 차이가 실무에서 중요한 이유는 단순합니다. **API는 멀쩡한데 사람이 못 들어가는 상황**에서는 자동화는 돌아가고 의사결정만 멈춥니다. 반대로 **인증이 API까지 확장되면** 자동화도 함께 섭니다.

### ② 2차(22:09) — 추론·커넥터까지 확장

1차가 끝나고 **1시간 11분 뒤** 더 넓은 장애가 왔습니다.

> "We are investigating reports of degraded performance affecting a variety of surfaces including claude.ai, the Claude API, Claude Code, connectors (including Claude Docs and Claude Sheets), Claude Security and Claude Managed Agents."
> — 2026년 11월 13일 14:03 UTC

이번에는 **API가 포함**됐고, **Cowork(오피스 작업), Meetings(회의), Managed Agents, Claude Security, Docs/Sheets 커넥터**까지 명단에 올랐습니다. 상태 페이지는 회복 국면에서 이렇게 덧붙였습니다.

> "Adding a new connector on claude.ai is temporarily unavailable. Scheduled tasks and routines can run more than two hours late and some runs have been skipped, and Claude Security scans, reviews and project setup remain delayed."
> — 2026년 11월 13일 15:33 UTC

정리하면 **1차는 '출입문' 문제, 2차는 '작업실' 문제**입니다. 같은 날 두 번 멈췄지만, **자동화 입장에서 실제로 데이터가 어긋난 건 2차**입니다.

| 구분 | 1차 (18:58 KST) | 2차 (22:09 KST) |
|:---|:---|:---|
| 성격 | 인증·세션 | 추론·도구·커넥터 전반 |
| claude.ai | 전체 장애 | 오류 |
| Claude Code | 부분 장애 | 오류 |
| Claude API | **정상** | **오류** |
| 커넥터 | 영향 없음 | Docs·Sheets 등 저하 |
| 예약 작업 | 해당 없음 | **2시간+ 지연, 일부 스킵** |
| 원인 공개 | 없음 | 없음 |

---

## 3. 별개의 축 — Microsoft 365 장애가 Claude를 끌어내렸다

이번 48시간을 설명할 때 **Anthropic 장애와 Microsoft 장애를 한 덩어리로 묶으면 안 됩니다.** 원인이 다르고, 책임 소재도 다릅니다.

Microsoft 쪽 상황은 이랬습니다.

- **11월 12일**부터 Exchange Online·Outlook·Teams·SharePoint·Copilot에서 **전 세계 70개국 이상** 사용자가 문제를 겪었습니다 (Wikipedia, "2026 Microsoft outage")
- Downdetector 신고는 **700만 건 이상**, 최근 12개월 최다 기록 (Ookla 분석)
- **사이버 공격 징후는 없다**고 Microsoft가 밝혔고, **11월 14일(토) 전면 복구**를 선언했습니다 (Reuters)
- 미국 외에도 **싱가포르·필리핀·일본 정부 기관, 인도 항공사, 영국 NHS 일부**가 영향을 받았습니다

Claude는 이 장애를 **커넥터 계층에서 그대로 맞았습니다.**

> "This issue is caused by a Microsoft 365 service incident, and mail, calendar and search tools in the Microsoft 365 connector are still failing for most requests. Claude for Outlook remains affected."
> — 2026년 11월 12일 22:23 UTC

핵심은 **의존 구조**입니다. Claude의 메일·일정·문서 자동화는 자체 인프라가 아니라 **Microsoft 365 API 위에 얹혀** 있습니다. 상류가 24시간 넘게 흔들리면, Claude 쪽은 정상이어도 **"오늘 회의 요약해서 슬랙에 올려줘"** 같은 작업은 실패합니다. 같은 이유로 **OpenAI의 Microsoft 커넥터도 함께 깨졌다**고 OpenAI가 별도로 공지했습니다.

즉 이번 주 Claude 사용자가 겪은 장애는 **최소 세 갈래**입니다.

1. **Anthropic 인증 장애** (11/13 18:58, 2시간)
2. **Anthropic 추론·커넥터 장애** (11/13 22:09, 2시간 29분)
3. **Microsoft 365 상류 장애 → Claude 커넥터** (11/13 01:03~11/14 06:49, 약 24시간 37분)

---

## 4. 업무 자동화 관점의 진짜 리스크 — '조용히 스킵된 실행'

여기서부터는 **분석**입니다. 상태 페이지 문구를 실무 언어로 번역해 보겠습니다.

### 리스크 ① 재시도 폭풍

장애가 2시간 반 이어지면, 대부분의 파이프라인은 **지수 백오프로 재시도**를 겁니다. 문제는 회복 시점(00:38)에 **밀린 요청이 한꺼번에 몰린다**는 점입니다. 2차 장애가 끝난 직후가 가장 불안정한 구간인 이유입니다.

### 리스크 ② 지연이 아니라 '누락'

"**some runs have been skipped**"는 재시도로 해결되지 않습니다. 예약 실행 자체가 큐에서 사라졌다는 뜻이기 때문입니다. 하루 한 번 도는 배치라면 **그날 데이터가 통째로 비는** 구조입니다.

- **지연(late)**: 결국 실행됨 → 재시도·순서 보정으로 커버 가능
- **누락(skipped)**: 실행되지 않음 → **다음 날 스케줄이 직접 메꿔야 함**

### 리스크 ③ 인증 만료와 세션 재발급

1차 장애는 **로그인 경로**였습니다. 자동화가 **장수명 API 키**가 아니라 **세션·OAuth 토큰 갱신**에 의존한다면, 짧은 장애에도 **토큰 갱신 실패 → 전 작업 중단**으로 번집니다. 이번처럼 짧고 굵은 인증 장애에서 특히 취약한 지점입니다.

### 리스크 ④ 커넥터 의존 워크플로

회의록·메일 분류·일정 조정처럼 **Microsoft 365 커넥터를 경유하는 작업**은 Anthropic이 정상인데도 하루 종일 실패했습니다. "AI가 죽었나?"를 확인하러 상태 페이지에 갔더니 **Anthropic은 초록불, Microsoft가 빨간불**인 상황이 실제로 벌어졌습니다.

> **정리:** 이번 주에 자동화가 깨진 이유는 **모델 성능이 아니라 '경로'** 입니다. 인증 경로, 커넥터 경로, 상류 클라우드 경로 — 세 갈래 중 어디가 끊겼는지 구분해서 봐야 복구가 빨라집니다.

---

## 5. 영향 — 사용자와 개발자에게 무엇이 달라지나

### 👤 개인 사용자: 결제 오류는 '두 번 결제' 주의

11월 14일 11:54~13:23 KST에 **플랜 업그레이드·해지·결제 오류**가 있었습니다. Anthropic은 상태 페이지에 이렇게 적었습니다.

> "Some customers who recently paid may need to wait up to an hour for their plan to update. **There is no need to pay again.**"
> — 2026년 11월 14일 03:09 UTC

**이미 결제했는데 플랜이 안 올라갔다면 재결제하지 말고 최대 1시간 기다리는 것**이 맞습니다. 실제로 지연된 결제와 플랜 변경은 이후 일괄 반영됐습니다.

### 👩‍💻 개발자: '어느 계층이 죽었는지'를 로그로 남겨야

이번 장애에서 배울 점은 **API가 정상인데도 서비스가 멈출 수 있다**는 사실입니다.

| 계층 | 이번 장애에서 | 대응 |
|:---|:---|:---|
| 인증(OAuth·세션) | 1차에서 붕괴 | 장수명 키 분리, 토큰 갱신 실패 시 알림 |
| 추론 API | 2차에서 붕괴 | 타임아웃·백오프·서킷브레이커 |
| 커넥터(M365 등) | 상류 장애로 24시간 붕괴 | 상류 상태 페이지 동시 모니터링 |
| 예약 실행 | 2시간 지연·일부 스킵 | 실행 로그 대조, 누락 감지 잡 |

특히 **"오늘 배치가 정말 돌았는지"** 를 확인하는 **완료 마커(completion marker)** 가 없다면, 이번 같은 날은 조용히 데이터가 비어 있을 수 있습니다.

### 🏢 기업·팀: SLA와 크레딧 조항을 다시 볼 시점

한 달에 major 장애가 다섯 번이면, **엔터프라이즈 계약의 가동률 조항·크레딧 청구 조건**을 다시 확인할 이유가 됩니다. 상태 페이지의 **인시던트 ID와 시간대**를 남겨 두면 청구 근거로 쓰기 좋습니다.

---

## 6. 대비 체크리스트

이번 사건을 겪고 나서 정리해 둘 만한 항목입니다.

| # | 항목 | 확인 질문 | 조치 |
|:--|:---|:---|:---|
| 1 | 실행 완료 감지 | 오늘 배치가 돌았는지 **어디서** 확인하나 | 완료 마커·행 수 검증 잡 추가 |
| 2 | 멱등성 | 재시도 시 **중복 생성**되지 않나 | 요청 키·업무 키 기반 중복 차단 |
| 3 | 인증 의존도 | 자동화가 **세션 토큰**에 기대나 | 장수명 키·서비스 계정 분리 |
| 4 | 상류 의존 | 커넥터 뒤에 **누가** 있나 | Anthropic + Microsoft 상태 동시 감시 |
| 5 | 알림 경로 | 장애를 **사람이 언제** 아나 | 상태 페이지 API 폴링 → 알림 |
| 6 | 대체 모델 | Claude가 죽으면 **무엇으로** 도나 | 모델 추상화 레이어, 폴백 라우팅 |
| 7 | 재시도 정책 | 회복 직후 **몰림**을 견디나 | 지터(jitter) 포함 백오프, 동시성 상한 |
| 8 | 기록 | 장애 시간대를 **남겼나** | 인시던트 ID·시각 로그 보관 |

3번과 6번이 이번 사건의 핵심입니다. **인증과 모델을 분리해 두면 짧은 장애는 흡수되고, 모델을 교체 가능하게 만들어 두면 긴 장애도 버팁니다.** 실제로 자동화 파이프라인을 운영하는 입장에서, 이번에 가장 크게 느낀 건 **"성능 좋은 모델보다 끊겨도 덜 아픈 구조"** 입니다.

---

## 7. 전망 — 무엇을 지켜봐야 하나

**① 원인 공개(postmortem) 여부.** Anthropic은 11월 14일 현재까지 1·2차 장애의 **원인을 밝히지 않았습니다.** 같은 날 두 번, 그것도 인증과 추론이라는 다른 계층에서 연속으로 났다는 점은 **공통 상위 의존성(배포·설정·네트워크)** 문제였을 가능성을 남깁니다. 공식 회고가 나오면 재발 방지 설계를 참고할 수 있습니다.

**② 상장 절차와 안정성 지표.** Anthropic은 IPO 절차를 진행 중입니다. 투자자·기업 고객 모두 **가동률(uptime)** 을 보게 되는데, 11월 한 달에 major 5건은 좋은 숫자가 아닙니다. 앞으로 **SLA 공개 범위가 넓어질지**가 관전 포인트입니다.

**③ 커넥터 의존 구조 개선.** Microsoft 365 같은 외부 SaaS 위에 얹힌 기능은 **상류가 죽으면 함께 죽습니다.** 커넥터별로 **장애를 격리**하는 설계(예: 메일 요약 실패 시 나머지 작업은 계속)가 강화될 가능성이 큽니다.

**④ 남은 이슈.** 11월 14일 16:11 KST 기준 **iOS 앱 Apple 로그인 신규 가입 문제**가 아직 조사 중입니다. 신규 가입자는 **Google 또는 이메일 가입**으로 우회할 수 있다고 공지돼 있습니다.

![Anthropic 공식 브랜드 이미지](https://pub-a8e00ddf355f47339bd2e01ff3221de7.r2.dev/assets/images/posts/anthropic-claude-outage-nov13-20261114/anthropic-official.jpg)
*이번 주 관전 포인트는 성능표가 아니라 가동률입니다. 사진은 Anthropic이 공식 배포 채널에 올린 브랜드 이미지 (이미지 출처: Anthropic 공식 assets.claude.com)*

---

## 마무리 — 좋은 모델보다 중요한 것

솔직히 이번 주 Claude 뉴스는 새 모델도, 벤치마크도 아니었습니다. **상태 페이지**였습니다. 성능이 아무리 좋아도 **로그인이 안 되면 아무 일도 못 하고**, 커넥터가 죽으면 **정상인 모델이 무용지물**이 됩니다.

업무 자동화를 Claude 위에 올려 운영하는 분들이라면, 이번 기회에 세 가지만 확인해 보시길 권합니다.

1. **오늘 배치가 돌았는지 증명할 수 있는가** (완료 마커)
2. **인증이 끊겨도 자동화가 서지 않는가** (키·세션 분리)
3. **Claude가 죽으면 대신할 경로가 있는가** (모델 추상화·폴백)

AI 에이전트를 실사용하고 크론잡을 돌리는 쪽에서 이번 48시간은 좋은 교재입니다. 장애는 반복되지만, **장애에 덜 아픈 구조**는 미리 만들어 둘 수 있으니까요. 상태 페이지를 즐겨찾기에 넣어 두는 것부터 시작하면 됩니다.

---

### 참고 자료

- Claude Status, [Incident History](https://status.claude.com/history) — 2026년 11월 12~14일 인시던트 기록 (2026년 11월 14일 확인)
- Claude Status API, [incidents.json](https://status.claude.com/api/v2/incidents.json) — 11월 인시던트 7건 원문
- Android Authority, ["Anthropic's Claude is experiencing a widespread outage"](https://www.androidauthority.com/anthropic-claude-outage-nov-2026-3735078/) (2026년 11월 13일)
- Moneycontrol, ["Claude down: Anthropic confirms authentication issues affecting Claude.ai, Claude Code and console"](https://www.moneycontrol.com/technology/claude-down-anthropic-confirms-authentication-issues-affecting-claude-ai-claude-code-and-console-article-14079728.html) (2026년 11월 13일)
- ExplainX, ["Claude Authentication Outage Hits Code + Web"](https://www.explainx.ai/blog/claude-authentication-outage-claude-ai-code-november-13-2026) (2026년 11월 13일)
- Reuters, ["Microsoft 365, Outlook down for thousands of US users"](https://www.reuters.com/technology/microsoft-365-outlook-down-thousands-us-users-downdetector-shows-2026-11-12/) (2026년 11월 12일 / 14일 복구 보도)
- Wikipedia, ["2026 Microsoft outage"](https://en.wikipedia.org/wiki/2026_Microsoft_outage) (2026년 11월 14일 확인)
- Ookla, ["Microsoft's Outage Clocked In Around the World"](https://www.ookla.com/articles/microsoft-365-outage-november-2026-downdetector) (2026년 11월 13일)

*본문의 장애 시각·영향 범위는 Claude 공식 상태 페이지 기록을 기준으로 했고, 4장의 리스크 분석과 6장 체크리스트는 취재 내용이 아닌 실무 관점의 정리입니다. 원인에 대한 추정은 Anthropic의 공식 발표가 아직 없음을 밝혀 둡니다.*
