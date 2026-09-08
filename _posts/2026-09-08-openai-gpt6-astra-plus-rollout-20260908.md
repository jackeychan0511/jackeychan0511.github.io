---
layout: post
title: "OpenAI GPT 최신 소식 — GPT-6 Astra, $20 ChatGPT Plus까지 롤아웃 확대… 채팅창 아닌 'Work·Codex'에서 만난다, 대기 1일당 사용량 리셋 보상 (2026.9.8)"
date: 2026-09-08 14:52:00 +0900
categories: [career]
tags: [GPT, OpenAI, Astra, GPT-6, ChatGPT Plus, ChatGPT Work, Codex, 롤아웃, 컴퓨터사용, ComputerUse, API가격, 출시, AI뉴스, 2026년9월]
author: "40대 블로거"
image: /assets/images/posts/openai-gpt6-astra-plus-rollout-20260908/openai-logo-2025-symbol.png
description: "2026년 9월 7일(월, 미국 현지시간), OpenAI가 X 공지를 통해 차세대 플래그십 모델 GPT-6 Astra의 롤아웃을 월 20달러 ChatGPT Plus와 Business 사용자까지 확대한다고 밝혔습니다. Astra는 9월 3일 발표 직후 일부 조직과 Pro·Enterprise·Business Premium(상위 요금제)부터 순차적으로 풀렸고, 지난 주말(9/5~6)부터 Plus 사용자에게도 도달하기 시작했습니다. 다만 Plus에서는 일반 채팅창이 아닌 'ChatGPT Work'와 'Codex'에서만 사용할 수 있고, 채팅창의 최상위 모델은 기존 GPT-5.6 Sol이 유지됩니다(채팅창용 상위 모델 GPT-6 Pro는 Pro·Business·Enterprise 전용). 험난했던 롤아웃 초반을 반영해 OpenAI는 9월 3일부터 Astra를 하루라도 못 쓴 유료 사용자에게 날짜당 사용량 리셋 1회를 보상으로 적립하기로 했고, 샘 올트먼 CEO도 'messy(엉망)'였다고 공개 사과했습니다. 컴퓨터 사용(Computer Use)을 핵심 기능으로 내세운 Astra의 요금제별 사용량 제한(Sol 대비 약 절반), API 가격(입력 100만 토큰당 10달러·출력 50달러), 벤치마크 결과와 논란, 일반 사용자·업무자동화·개발자에게 미칠 영향을 정리했습니다."
sitemap: false
noindex: true
---
> **📌 한줄 요약:** OpenAI가 **2026년 9월 7일(월, 미국 현지시간)** X 공지를 통해 차세대 플래그십 **GPT-6 Astra**의 롤아웃을 **월 20달러 ChatGPT Plus·Business 사용자까지 확대**한다고 밝혔습니다. Astra는 9월 3일 공식 발표 직후 **일부 조직 → Pro($100/$200)·Enterprise·Business Premium → 지난 주말(9/5~6) Plus** 순서로 순차 배포 중이며, 아직 **무료 티어는 대상이 아닙니다.** 다만 Plus에서는 일반 채팅창이 아니라 **'ChatGPT Work'와 'Codex' 화면에서만** Astra를 쓸 수 있고(채팅창 최상위는 GPT-5.6 Sol 유지), 채팅창용 상위 모델 'GPT-6 Pro'는 Pro·Business·Enterprise 전용입니다. 롤아웃이 예고보다 험난했던 만큼 OpenAI는 **9월 3일부터 Astra를 하루라도 못 쓴 유료 사용자에게 날짜당 사용량 리셋 1회를 보상 적립**하기로 했고, 샘 올트먼 CEO도 이를 두고 **"messy(엉망)"였다고 공개 사과**했습니다.

![OpenAI 2025 공식 로고 (출처: Wikimedia Commons, OpenAI 공식 브랜드 페이지)](/assets/images/posts/openai-gpt6-astra-plus-rollout-20260908/openai-logo-2025-symbol.png)
*OpenAI 2025 공식 로고 (출처: Wikimedia Commons, OpenAI 브랜드 페이지)*

요즘 저처럼 GPT-6 Astra 출시 소식을 매일 확인하시는 분들, 지난 9월 3일 글에서 제가 "Astra 공식 출시 소식이 나오면 바로 다시 정리해 찾아오겠습니다"라고 했잖아요. 솔직히 그 사이 롤아웃이 **예상보다 훨씬 험난**했습니다. 유료 고객은 "플랜에 포함된 모델이 왜 안 보이지?" 하며 문의가 폭주했고, 샘 올트먼 CEO가 직접 "엉망이었다"고 사과하는 일까지 벌어졌거든요. 그런데 **이번 주말을 기점으로 마침내 월 20달러 ChatGPT Plus 사용자에게도 Astra가 도착하기 시작**했습니다. 오늘은 9월 7일 OpenAI 공식 X 공지와 Notebookcheck·Dataconomy·The Decoder 보도를 바탕으로, **어디서 어떻게 Astra를 만날 수 있는지 + 대기 보상 + 요금제별 사용량 제한**까지 실용적으로 정리해 봤습니다.

---

## 이 글을 읽으면 알 수 있는 것

- GPT-6 Astra 롤아웃이 지난 5일간 **어떤 순서로 진행**됐는지(9/3 발표 → 9/4 상위 요금제 → 주말 Plus)
- **ChatGPT Plus($20) 사용자가 Astra를 쓰는 법** — 일반 채팅창이 아니라 'ChatGPT Work'와 'Codex'에서
- 채팅창의 'GPT-6 Pro'는 무엇이고, 왜 Plus에서는 안 보이는지
- '대기 1일당 사용량 리셋 1회' **보상 정책**의 내용과 샘 올트먼의 사과
- 요금제별 **사용량 제한**(Sol 대비 약 절반), **API 가격**(입력 $10/출력 $50, 100만 토큰당), 벤치마크·논란 정리
- 일반 사용자·업무자동화·개발자에게 미칠 영향과 향후 전망

## 이슈 요약 — 5일간의 '층층이' 롤아웃, 드디어 Plus까지

OpenAI는 9월 3일 GPT-6 Astra를 발표하며 "**오늘 제한된 조직에 공개되고, 앞으로 며칠 내 모든 ChatGPT Plus·Pro·Business·Enterprise 사용자와 API·Azure·Bedrock으로 확대**된다"고 예고했습니다. 실제 진행은 다음과 같았습니다.

| 날짜(미국 현지) | 롤아웃 단계 |
|:----|:----|
| 9/3(목) | GPT-6 Astra 공식 발표 + **제한적 조직**부터 출시 시작 |
| 9/3 밤~9/4 | **Pro($100/$200)·Enterprise·Business Premium** → ChatGPT Work·Codex에서 사용 가능 + API·Azure·AWS Bedrock 개방 |
| 9/4 | Codex 리드 티보 소티오(Thibault Sottiaux), **보상 방침 발표** — 9/3부터 Astra를 못 쓴 날마다 사용량 리셋 1회 적립 |
| 9/4 | 샘 올트먼 CEO, 롤아웃을 두고 **"messy(엉망)"였다고 공개 사과** |
| 9/5~6(주말) | **$20 ChatGPT Plus 사용자에게도 확대 시작** |
| 9/7(월) | OpenAI X 공지 — "**Plus·Business 사용자 롤아웃은 며칠 더 걸릴 수 있다**" + Notebookcheck·Dataconomy 등 보도 |

OpenAI는 X 공지에서 Plus·Business 롤아웃이 "며칠(a few days) 걸릴 수 있다"고 밝혀 **계정별로 도달 시점에 차이가 있는 순차 배포**임을 분명히 했습니다. 무료 티어에 대한 접근 계획은 아직 발표되지 않았습니다.

## 상세 분석 — Plus 사용자는 '채팅창'이 아니라 'Work·Codex'에서 만난다

### 1. 핵심: Plus의 Astra는 ChatGPT Work와 Codex에만 있다

Notebookcheck가 짚은 가장 중요한 포인트입니다. **Plus 사용자라도 평소 쓰는 일반 채팅창(창 위 모델 선택기)에는 Astra가 나타나지 않습니다.** 채팅창의 최상위 모델은 여전히 GPT-5.6 Sol이고, OpenAI 헬프 문서에는 이렇게 적혀 있습니다.

> "Plus 플랜에는 롤아웃에 따라 ChatGPT Work와 Codex에 GPT-6 Astra가 포함됩니다. (Plus plans include GPT-6 Astra in ChatGPT Work and Codex as it rolls out.)"

ChatGPT는 이제 여러 '화면(surface)'으로 나뉘어 운영됩니다.

| 화면 | 역할 | Astra 접근 |
|:----|:----|:----|
| **Chat** | 일반 대화 | Plus는 **불가**(채팅창용 상위 모델 'GPT-6 Pro'는 Pro·Business·Enterprise 전용) |
| **Work** | 장기 작업용 에이전트 — 리서치·분석, 문서·스프레드시트·프레젠테이션 완성 | Plus 포함 롤아웃 대상 |
| **Codex** | 소프트웨어 개발 전용 | Plus 포함 롤아웃 대상 |

'Work'는 이름과 달리 개발자 전용이 아닙니다. OpenAI는 Work를 **리서치·분석과 완성된 문서·스프레드시트·프레젠테이션 제작**용으로 소개하며, "코딩 스킬이 없어도 글쓰기 등에 Astra를 쓰는 데는 올바른 화면만 고르면 된다"고 설명합니다.

**찾는 법:** 데스크톱 앱을 최신 버전으로 업데이트한 뒤 좌상단 'ChatGPT' 메뉴를 열고, 상단 토글을 Chat에서 **Work**로 전환하거나 같은 메뉴의 **Codex** 항목을 선택하면 됩니다. 모바일은 상단 드롭다운에 Work가 표시됩니다. Codex CLI는 v0.153.0 이상이 필요합니다.

### 2. 채팅창의 'GPT-6 Pro'는 별개의 상위 모델

혼란을 막기 위해 짚고 갈 부분입니다. Notebookcheck에 따르면 **채팅(Chat) 화면에서 Astra 계열 모델은 'GPT-6 Pro'라는 이름**으로 제공되며, 이는 **Pro($100/$200)·Business·Enterprise 플랜에서만** 선택할 수 있습니다. 즉 같은 Astra 파운데이션이어도 **채팅창용 'GPT-6 Pro'는 상위 요금제 전용**이고, Plus 사용자는 **Work·Codex에서 표준 GPT-6 Astra**를 쓰는 구조입니다. Plus의 Astra가 언제 일반 채팅창까지 들어올지는 아직 공식적으로 정해지지 않았습니다(9월 5일 이후 OpenAI 개발자 포럼에서도 답이 나오지 않은 상태).

### 3. 사용량 제한 — Sol의 약 절반, 그리고 '대기 보상'

OpenAI 가격 페이지 기준(The Decoder 정리), 표준 GPT-6 Astra는 **5시간 창 기준 메시지 수가 GPT-5.6 Sol의 약 절반** 수준입니다.

| 요금제 | GPT-6 Astra | GPT-5.6 Sol |
|:----|:----|:----|
| Plus | 5~45개 | 10~100개 |
| Pro 5x($100) | 25~225개 | 50~500개 |
| Pro 20x($200) | 100~900개 | 200~2,000개 |
| Business Standard | 5~45개 | 10~100개 |

(실제 수치는 모델 선택·컨텍스트 길이·추론·도구 사용·캐싱에 따라 달라집니다.)

채팅창의 GPT-6 Pro 할당량은 별도입니다. **Pro $200 → 주 200개 메시지**(GPT-5.6 Sol Pro는 별도로 일 170개, 두 모델 합산 일 200개 제한), **Pro $100 → 주 50개**(Sol Pro와 공유), **Business Premium → 주 50개**, **Business Standard → 월 15개**입니다.

험난했던 초반을 반영한 **보상 정책**도 기억해 둘 만합니다. 9월 3일 밤 Codex 리드 소티오가 발표한 내용으로, **유료 사용자는 9월 3일부터 Astra를 하루라도 사용하지 못한 날마다 '사용량 리셋(banked usage reset) 1회'를 적립**받습니다. 샘 올트먼 CEO는 9월 4일 "플랜에 포함된 모델을 못 쓰게 된 유료 고객에게 사과한다"며 롤아웃이 "messy"였다고 인정했습니다. 다만 **크레딧을 구매한다고 롤아웃이 빨라지지는 않는다**고 OpenAI는 못 박았습니다.

### 4. API 가격과 벤치마크 — '컴퓨터를 조작하는 모델'

Astra는 **OpenAI API·Microsoft Azure·AWS Bedrock**에서도 이용 가능하며, 가격은 **입력 100만 토큰당 $10, 출력 100만 토큰당 $50**입니다(Dataconomy 기준 GPT-5.6 Sol 대비 약 2.5배, Anthropic Claude Opus 5의 약 2배 수준).

핵심 기능은 단연 **컴퓨터 사용(Computer Use)**입니다. 별도 API·통합 없이 모델이 **소프트웨어를 탐색하고, 웹을 브라우징하고, 프로그램을 직접 조작**할 수 있습니다. 공동창업자 그렉 브록만은 발표 브리핑에서 "컴퓨터 사용이 **질적 임계점(qualitative threshold)을 넘었다**"며 "매우 정확하고 효율적이며 신뢰할 수 있다"고 평가했고, 공개 데모에서는 온라인 리서치·스크립트 작성·**블렌더(Blender) 3D 모델링 프로그램 자율 조작**까지 선보였습니다.

주요 벤치마크(Dataconomy·Vellum 취합):

- **ARC-AGI-3: 99.9%** (메모리 유지 'Adapter Harness' 모드) — GPT-5.6 7.8%, Claude Opus 5 30.2% 대비 압도적
- **FrontierMath Tier 4: 98%**
- **ExploitBench: 100%** — 알려진 취약점으로 동작하는 익스플로잇 제작 테스트 만점. 지난 9/3 글에서 다룬 **'Critical(심각)' 사이버보안 등급 판정의 직접적 근거**
- **OSWorld 2.0: 72.6%** (GPT-5.6 Sol 65.7%) — 컴퓨터 사용 벤치마크, 작업당 소요 시간도 75분 → 약 40분으로 단축

![OpenAI 본사 — 샌프란시스코 미션베이 1515 Third Street (출처: Wikimedia Commons, Coolcaesar 촬영, CC BY 4.0)](/assets/images/posts/openai-gpt6-astra-plus-rollout-20260908/openai-hq-1515-third-street.jpg)
*OpenAI 본사 — 샌프란시스코 미션베이 1515 Third Street (출처: Wikimedia Commons, Coolcaesar 촬영, CC BY 4.0)*

### 5. 논란 — 'AGI 시대' 선언에 대한 회의론도

출시와 함께 잡음도 만만치 않습니다. ① 조선일보는 **ARC Prize 벤치마크 결과를 두고 "모델 크기보다 메모리 유지(memory retention)가 성능을 좌우한다"**는 해석을 전했고, ② 블렌더 5.2 스플래시 아트 작가가 **OpenAI 마케팅 영상에 자신의 작품이 쓰인 데 "disgust(혐오감)"를 느낀다**고 밝히는 등 크리에이터 반발이 있었으며, ③ 초기 테스터들은 **여전한 환각·지시 무시 사례**를 들며 "AGI에 근접했다"는 OpenAI 측 표현에 의문을 제기하고 있습니다. 다만 OpenAI는 Astra를 **"세계에서 가장 지능적이고 정렬된(aligned) 모델"**로 부르며 GPT-5.6 Sol에서의 '풀 버전 점프(full version jump)'로 규정했습니다.

## 영향 — 사용자·업무자동화·개발자에게 무엇이 달라지나

### 👤 일반 사용자(ChatGPT Plus)

- **채팅창에서 Astra가 안 보여도 당황하지 마세요** — Plus의 Astra는 Work·Codex 화면에 있습니다. 데스크톱 앱을 업데이트한 뒤 좌상단 메뉴에서 Work/Codex로 진입하면 됩니다.
- **'며칠 걸릴 수 있다'는 공지대로 계정별 도달 시점이 다릅니다.** 못 받은 날은 **날짜당 사용량 리셋 1회**가 쌓이니, '내 플랜엔 왜 없지?'보다는 보상 적립을 확인해 보는 게 낫습니다.
- **사용량은 Sol의 절반 수준**입니다. 장기 리서치·대용량 문서 작업을 매일 돌리는 분이라면 Plus의 Astra 할당량(5~45개/5시간)으로는 빠듯할 수 있고, Pro 업그레이드 판단이 필요해집니다.
- 채팅창 최상위 모델(GPT-5.6 Sol)과 Astra의 **역할 분담**에 익숙해지는 게 좋습니다. 가벼운 대화는 Sol, '컴퓨터를 시켜서 끝내는' 장기 작업은 Astra 구조입니다.

### 🤖 업무자동화·AI 에이전트 활용자

- Astra의 핵심인 **컴퓨터 사용**은 '지시 → 답변'을 넘어 **'지시 → 브라우저·앱 조작 → 결과물 완성'**을 뜻합니다. Work에서 파일·스프레드시트·슬라이드를 '완성본'으로 받아내는 워크플로우가 Plus 요금제에서도 열린 셈입니다.
- 오픈소스 에이전트 생태계도 이미 따라붙었습니다. 어제 정리한 **OpenClaw v2026.9.2**가 GPT-6 Astra를 공식 지원(텍스트·이미지 입력, API 키/ChatGPT·Codex 구독 모두)하기 시작했듯, Astra는 '직접 도구'와 '에이전트 프레임워크' 양쪽에서 표준 모델로 자리 잡는 중입니다.
- 다만 **Critical 사이버 등급** 때문에 민감 기능은 여전히 게이팅돼 있습니다(9/3 글 참고). 업무 자동화로 쓸 때는 일반 작업과 고위험 작업의 경계를 염두에 두어야 합니다.

### 💻 개발자·API 사용자

- **비용 설계 주의** — 입력 $10/출력 $50(100만 토큰당)은 Sol 대비 약 2.5배입니다. 컴퓨터 사용·장기 에이전트 작업은 토큰 소모가 크므로, Astra 전용 에이전트는 캐싱·컨텍스트 관리가 수익성의 관건이 됩니다.
- Azure·AWS Bedrock에서도 쓸 수 있어 **클라우드 계약(엔터프라이즈) 경로로도 도입**이 가능합니다.
- Codex CLI 0.153.0+부터 Astra를 쓸 수 있고, 채팅창용 GPT-6 Pro 할당량은 요금제별로 다르니(Pro $200 주 200개 등) **앱에서 모델 노출 시 요금제별 한도 안내**를 구현해 두는 게 좋습니다.

## 전망 — 관건은 '채팅창 진입 시점'과 '순차 확대의 마무리'

이번 뉴스의 최대 관전 포인트는 두 가지입니다. 첫째, **Plus의 Astra가 언제 일반 채팅창(Chat)까지 들어오는가**입니다. 현재 공식 문서상 Plus는 Work·Codex 한정이고, 개발자 포럼 질문에도 답이 없는 상태라, 이 문제가 풀리는 순간 '사실상 전 사용자용 Astra' 시대가 열립니다. 둘째, **무료 티어 정책**입니다. GPT-5.6 Sol이 Free·Go에서 제외된 것처럼 Astra도 유료 전용으로 갈 가능성이 높지만, 광고 기반 무료 전략(지난 8월 유럽 광고 도입)과 맞물려 어떻게 바뀔지 지켜볼 만합니다.

경쟁 구도도 치열합니다. Claude Opus 5가 절반 가격으로 맞서고 있고, ARC-AGI-3 등 벤치마크 해석을 둘러싼 논쟁(메모리 유지 효과)도 이어지고 있습니다. '가장 똑똑한 모델'과 '가장 경제적인 모델'의 선택지가 갈리는 시점인 셈입니다. 다만 OpenAI가 **Critical 등급 모델을 '분할 접근'으로 단계 배포**하는 이번 패턴은, 앞으로 프론티어 모델 출시의 표준 절차가 될 가능성이 큽니다.

## 마무리 — 지금 할 수 있는 것

솔직히 이번 롤아웃을 지켜보며 "아무리 좋은 모델도 배포가 엉망이면 유료 고객의 신뢰를 잃는구나"라는 생각과, "그래도 드디어 Plus에서 Astra를 만져볼 수 있게 됐다"는 기대가 동시에 들었습니다. 정리하면 지금 할 수 있는 일은 이렇습니다.

1. **데스크톱 앱을 최신 버전으로 업데이트**하고, 좌상단 메뉴에서 **Work(또는 Codex)로 전환**해 Astra가 떴는지 확인하기 — 채팅창이 아님에 유의
2. **아직 안 보인다면 보상 확인** — 9/3부터 하루당 사용량 리셋 1회가 적립 중이니, 헬프센터·계정 설정에서 누적 내역을 살펴보기
3. **업무자동화 실험은 '완성본 산출' 위주로** — 리서치·문서·스프레드시트를 '중간 답변'이 아닌 '끝난 결과물'로 받아내는 Work 워크플로우부터 시도해 보면 Astra의 컴퓨터 사용 능력을 가장 실감할 수 있습니다
4. Pro($100) 업그레이드는 **주 50회(채팅 GPT-6 Pro) 할당량이 실제로 필요한지** 확인한 뒤 결정 — Plus의 Work·Codex Astra로 충분하다면 당분간 유지가 가성비가 좋습니다

지난 9/3 글에서 예고한 대로, 이제 **'Critical 등급 모델의 첫 대중 롤아웃'**이 실제로 진행되고 있습니다. 저처럼 최신 AI 모델 소식을 꼼꼼히 따라가며 내 요금제에 맞는 활용법을 찾고 싶은 분들께 이 글이 도움이 되길 바랍니다. Astra가 채팅창에 정식으로 들어오거나 무료 티어 정책이 나오면 바로 다시 정리해 찾아오겠습니다!

---

*참고 출처: OpenAI 공식 X 계정 공지(2026-09-07), OpenAI Help Center(ChatGPT Work and Codex·요금제 문서), Dataconomy "OpenAI rolls out GPT-6 Astra to $20 ChatGPT Plus users"(2026-09-07), Notebookcheck "GPT-6 Astra is on ChatGPT Plus, but only in Work and Codex"(2026-09-07), The Decoder(2026-09-05), Vellum "GPT-6 Astra Benchmarks Explained", CNBC(2026-09-03) / 관련 글: [Astra 'Critical' 사이버 등급 공식 판정 (2026.9.3)](https://jackeychan0511.github.io/2026/09/03/openai-astra-critical-20260903/), [OpenClaw v2026.9.2 — GPT-6 Astra 공식 지원 (2026.9.8)](https://jackeychan0511.github.io/2026/09/08/openclaw-v2026.9.2-update-20260908/)*
