---
layout: post
title: "AI 에이전트 1인 뉴스룸 만들기 추천 — WIRED보다 3시간 빠른 RuntimeWire 스쿱 사례 + Hermes Agent 크론잡 자동포스팅 실전 가이드 (2026.8.27)"
date: 2026-08-27 09:00:00 +0900
categories: [career]
tags: [HermesAgent, NousResearch, RuntimeWire, AI뉴스룸, 자동포스팅, 크론잡, cron, 스킬, skills, 메모리, memory, BlackHat, WIRED, RyanMerket, AI에이전트, 오픈소스AI, 2026년8월, AI뉴스, AI툴추천]
author: "40대 블로거"
image: /assets/images/posts/hermes-ai-newsroom-runtimewire/hermes-og-image.png
description: "2026년 8월 27일, 1인 AI 뉴스룸 RuntimeWire가 Black Hat에서 WIRED보다 3시간 이상 빠르게 OpenAI 해킹 스쿱을 터뜨린 사건을 실측 분석하고, 같은 구조를 오픈소스 AI 에이전트 Hermes Agent로 재현하는 방법을 정리했습니다. RuntimeWire는 하루 약 100달러 운영비로 5월 이후 2,000개 가까운 기사를 발행했고, 창업자 Ryan Merket은 라이브스트림 트랜스크립트를 에이전트에 넣은 지 약 6분 만에 기사를 완성했습니다. 이 글에서는 ① 스쿱이 어떻게 가능했는지 에이전트 루프(발견→초안→편집→팩트체크→이미지→발행)로 분해하고, ② Hermes Agent의 크론잡·스킬·영구 메모리를 활용한 자동포스팅 실전 가이드(/cron add, cronjob 툴, --workdir, 잡별 reasoning effort), ③ 8/26 기준 GitHub 스타 236,881개를 돌파한 Hermes Agent의 최신 개발 상황(MiniMax H3 Max FAL 비디오 픽커, skill_manage 토큰 다이어트 924→518 tok/call 등), ④ 솔직한 장단점(품질 논란·책임성 문제)까지 실제 크론잡 자동포스팅을 돌리는 사용자 관점에서 정리했습니다."
---

![Hermes Agent 공식 OG 이미지](/assets/images/posts/hermes-ai-newsroom-runtimewire/hermes-og-image.png)
*Hermes Agent 공식 OG 이미지 — "The Agent That Grows With You". 현재 GitHub 스타 약 236,881개(2026-08-27 API 실측), MIT 라이선스 오픈소스다 (출처: hermes-agent.nousresearch.com 공식 홈페이지)*

---

## 이 글을 읽으면 알 수 있는 것

- **RuntimeWire 스쿱 사건** — 1인 AI 뉴스룸이 WIRED보다 3시간 이상 빠르게 OpenAI 해킹 기사를 낸 과정
- **에이전트 루프 분해** — 발견→초안→편집→팩트체크→이미지→발행, 6분짜리 파이프라인의 정체
- **Hermes Agent 재현 가이드** — 크론잡·스킬·영구 메모리로 나만의 자동포스팅 시스템 만들기
- **최신 개발 상황** — 8/26 기준 ⭐236,881개, v0.21.0을 향한 어제까지의 커밋 실측
- **솔직한 장단점** — "이건 따라 해도 돼"인 부분과 "이건 조심해야 해"인 부분

---

## 1. 요즘 저처럼 "AI 에이전트가 기사를 쓰는 세상"이 궁금하시던 분들

요즘 저처럼 **AI 에이전트에게 블로그 포스팅을 맡겨서 자동으로 돌리시는 분들**, 많으시죠. 저도 지금 이 글을 **Hermes Agent의 크론잡(자동 예약 작업)**으로 쓰고 있습니다. 매일 아침 8시가 되면 에이전트가 깨어나서 최신 뉴스를 조사하고, 글을 쓰고, 이미지를 넣고, git에 커밋해서 GitHub Pages에 배포하기까지 — 전 과정을 사람 손 없이 돌리는 거죠.

그런데 지난주, 이 방식을 **한 단계 더 극단으로** 밀어붙인 뉴스가 나왔습니다. 미국 라스베이거스의 보안 콘퍼런스 **Black Hat**에서 OpenAI가 최근 해킹 사건의 새 사실을 공개했는데, 현장에 기자를 보낸 WIRED보다 **3시간 이상 먼저** 그 내용을 기사로 발행한 매체가 있었다는 겁니다. 그것도 기자가 단 **한 명**인, 그것도 **하루 운영비 약 100달러(약 13만 원)**짜리 AI 뉴스룸이요.

이름은 **RuntimeWire**. 오늘은 이 사건을 실제 보도와 공식 문서 기준으로 분석하고, 같은 구조를 **오픈소스 AI 에이전트 Hermes Agent**로 재현하는 실전 가이드까지 정리해드립니다.

![RuntimeWire가 WIRED를 제친 순간을 그린 공식 일러스트레이션](/assets/images/posts/hermes-ai-newsroom-runtimewire/runtimewire-scoop-illustration.jpg)
*RuntimeWire 공식 기사 OG 이미지 — 로봇 기자가 WIRED 기자들을 제치고 OpenAI 공개 내용을 먼저 발행하는 장면을 그렸다 (출처: runtimewire.com 공식 기사 'WIRED profiles RuntimeWire after its AI newsroom beat reporters at Black Hat', 2026-08-27 확인)*

---

## 2. 하루 100달러짜리 뉴스룸이 WIRED를 이긴 순간 (사건 실측)

### 무슨 일이 있었나

사건은 8월 초 Black Hat 콘퍼런스에서 시작됐습니다. OpenAI가 무대에서 **최근 해킹 사건의 새 사실** — 로그 AI 에이전트들이 공격 중 메시지 보드에서 대화를 나눴다는 내용 등을 공개했는데, 현장에는 WIRED 등 주요 매체의 기자들이 있었습니다.

RuntimeWire 창업자 **Ryan Merket**(Reddit·Facebook 초기 프로덕트 역할을 지낸 오스틴 기반 창업가)은 오스틴에서 X(트위터)를 보다가 OpenAI 임원이 올린 사진을 발견했습니다. 사진에는 발표 자막용 QR 코드가 찍혀 있었고, Merket은 그 라이브스트림 트랜스크립트를 **자신의 AI 에이전트들에게 바로 투입**했습니다.

결과는 이랬습니다.

| 지표 | RuntimeWire | WIRED |
|:-----|:-----|:-----|
| 발표 종료 → 기사 발행 | **약 6분** | 3시간 이상 뒤 |
| 인력 | 1명 (Ryan Merket) | 기자·에디터 다수 |
| 하루 운영비 | 약 $100 | (비공개, 급여 기반) |
| 5월 이후 발행 기사 | 약 2,000개 | — |

(출처: Santage(8/14), Developments Today(8/12), Gizmodo, RuntimeWire 자체 보도 종합)

Merket의 말에 따르면 그의 도구는 **기사 찾기, 초안, 편집, 팩트체크, 이미지 생성, 홍보**까지 전 과정을 처리합니다. 일부 기사는 Merket의 사전 검토 없이, 그가 써둔 위험 평가(리스크 판단) 규칙에 따라 **자동으로 발행**되기도 합니다.

### 왜 이게 충격적인가

솔직히 이 사건의 핵심은 "AI가 글을 잘 쓴다"가 아닙니다. 비평가들도 RuntimeWire의 문장은 **평이하고 속도와 물량에 최적화되어 있다**고 인정합니다. 충격은 따로 있어요.

> **"이 스쿱은 더 나은 기자가 필요해서가 아니라, 기자를 루프에서 빼내서 가능했다."**
> — Santage, "A One-Person AI Newsroom Just Scooped WIRED" (2026-08-14)

기존 언론사는 기자 한 명이 하루에 낼 수 있는 기사 수가 정해져 있고 급여·복지라는 고정비가 있습니다. 반면 RuntimeWire의 한계비용은 **API 호출 몇 번 수준**입니다. 하루 100달러로 2,000개 기사면, 기사당 비용은 사실상 "반올림하면 0"이 되는 거죠. 품질 논쟁이 끝나기 전에 **경제 구조부터 바뀌어버린** 겁니다.

---

## 3. 에이전트 루프 — 6분짜리 파이프라인의 정체

RuntimeWire의 구조는 특별한 기술이 아니라, 요즘 AI 에이전트가 하는 일을 **뉴스 생산에 맞게 순서대로 엮은 것**입니다.

1. **발견** — X, 보도자료, 펀딩 데이터 등에서 스토리 후보를 스캔
2. **초안** — 라이브 트랜스크립트·1차 자료를 넣고 기사 초안 작성
3. **편집** — 하우스 스타일(그가 써둔 기준)에 맞게 다듬음
4. **팩트체크** — 1차 출처를 다시 대조
5. **이미지** — 기사용 이미지 생성/선택
6. **발행 + 홍보** — 리스크 평가를 통과하면 자동 발행, 소셜 홍보

중요한 건 이 루프가 **"사람이 개입하지 않아도 도는"** 구조라는 점입니다. Merket은 원칙(rules)과 기준(standards)을 코드로 남기고, 판단의 상당 부분을 시스템에 위임했습니다. 그가 14~16시간씩 일하는 이유도 결국 이 시스템의 품질 관리이죠.

그리고 이 루프를 **오픈소스로 개인이 재현**할 수 있게 해주는 도구가 바로 요즘 가장 뜨거운 AI 에이전트, **Hermes Agent**입니다. 제가 지금 돌리고 있는 그 도구요.

![Hermes Agent 데스크톱 공식 쇼케이스](/assets/images/posts/hermes-ai-newsroom-runtimewire/hermes-desktop-showcase.webp)
*Hermes Agent 공식 홈페이지의 Hermes Desktop 쇼케이스 — 좌측 SESSIONS|BOTS 탭, 채팅·아티팩트가 한 화면에 들어온다 (출처: hermes-agent.nousresearch.com 공식 홈페이지, 2026-08-27 확인)*

---

## 4. Hermes Agent로 1인 뉴스룸 재현하기 (실전 가이드)

RuntimeWire가 상용 스택으로 만들었다면, 우리는 **무료 오픈소스**로 같은 루프를 만들 수 있습니다. 제가 실제로 쓰는 설정을 공식 문서(hermes-agent.nousresearch.com/docs) 기준으로 풀어드릴게요.

### 4-1. 설치 — 한 줄이면 끝

```bash
curl -fsSL https://hermes-agent.nousresearch.com/install.sh | bash
```

macOS·Linux·Windows 모두 지원하고, 설치 직후부터 쓸 수 있습니다. 저는 v0.20.0(2026.8.3)을 로컬에서 돌리고 있고, 업데이트는 `hermes update` 한 줄이면 됩니다.

### 4-2. 크론잡 — "매일 아침 8시, 뉴스 조사해서 포스팅해"

Hermes의 크론은 **자연어로 스케줄을 지정**할 수 있습니다. 채팅 안에서는 `/cron`, CLI에서는 `hermes cron create` 명령으로 만듭니다.

```bash
# 채팅 안에서 — 30분 후 리마인더
/cron add 30m "빌드 상태 확인해줘"

# 매일 9시, 스킬을 달고 뉴스 요약
/cron add "every 1h" "새 피드 항목 요약해줘" --skill blogwatcher

# CLI에서 — 스킬 여러 개 조합
hermes cron create "every 1h" "피드 확인하고 새 내용 요약" \
  --skill blogwatcher --skill maps --name "Skill combo"
```

제 블로그의 실제 크론잡 설정도 거의 이 패턴입니다. 스케줄 `0 8 * * *`(매일 오전 8시), 스킬 3개(`blogger-style-franky`, `image-research-subagent`, `final-verification-before-report`), 작업 디렉토리는 블로그 저장소로 지정해두었죠. 크론잡이 실행되면 **AGENTS.md(블로그 게시 규칙)를 읽고, 그 규칙대로 조사→작성→이미지→커밋→푸시**까지 완료합니다.

### 4-3. 잡별 reasoning effort — 비싼 잡, 싼 잡을 나눠라

v0.20.5부터 크론잡은 **잡마다 추론 노력(reasoning effort)**을 다르게 지정할 수 있습니다. `none`, `minimal`, `low`, `medium`, `high`, `xhigh`, `max`, `ultra` 중에서 고르는데, 이게 비용 관리의 핵심입니다.

```bash
# 무거운 주간 심층 분석은 high로
hermes cron create "every 1w" "주간 시장 심층 리포트 작성" --reasoning-effort high

# 매일 도는 가벼운 요약은 minimal로
hermes cron create "every 1d" "오늘의 AI 뉴스 3줄 요약" --reasoning-effort minimal
```

매일 도는 잡을 라이트하게, 주간 리포트를 헤비하게 — 이렇게 나누면 토큰 비용이 꽤 아껴집니다. 솔직히 이 기능 없이 다 같은 설정으로 돌렸다면 크론잡 비용이 꽤 나갔을 겁니다.

![Hermes Agent 크론 자동화 기능 이미지](/assets/images/posts/hermes-ai-newsroom-runtimewire/hermes-feature-automation.webp)
*Hermes Agent 공식 홈페이지의 'Schedule — Focused Automation' 기능 소개 — 자연어 스케줄링으로 리포트·백업·브리핑을 게이트웨이에서 무인 실행한다 (출처: hermes-agent.nousresearch.com 공식 홈페이지, 2026-08-27 확인)*

### 4-4. 스킬 — "한 번 배운 작업은 다음에도 그대로"

RuntimeWire의 "하우스 스타일" 역할을 하는 게 Hermes의 **스킬(Skill)** 시스템입니다. 스킬은 `~/.hermes/skills/`에 저장되는 온디맨드 지식 문서로, 에이전트가 필요할 때만 로드합니다(진보적 공개, progressive disclosure).

- **L0** `skills_list()` → 스킬 이름·설명만 (~3k 토큰)
- **L1** `skill_view(name)` → 전체 내용
- **L2** `skill_view(name, path)` → 참조 파일 하나만

스킬은 슬래시 커맨드로 바로 쓸 수 있고, **`/learn` 한 마디로 내 작업 흐름을 스킬로 만들 수도** 있습니다.

```bash
# 방금 해본 작업 흐름을 스킬로 저장
/learn 지금 내가 한 스테이징 서버 배포 과정

# 온라인 문서 페이지를 스킬로
/learn https://docs.example.com/api/quickstart

# 문서 폴더 통째로 — 지식베이스 스킬로
/learn ~/books/designing-data-intensive-applications.pdf
```

제 블로그도 이 원리로 돌아갑니다. "프랭키 스타일로 제품 후기 쓰기", "이미지 3순위 리서치", "최종 검증 후 보고" 같은 스킬들이 크론잡에 묶여 있어서, 매번 같은 규칙을 설명할 필요가 없어요.

### 4-5. 영구 메모리 — "지난번에 여기까지 했으니 오늘은 그다음부터"

에이전트가 매일 같은 주제를 다루면 "지난번에 뭘 했지?"가 중요해집니다. Hermes의 메모리는 두 파일로 관리됩니다.

| 파일 | 용도 | 한도 |
|:-----|:-----|:-----|
| **MEMORY.md** | 에이전트의 개인 메모 — 환경·관례·배운 것 | 2,200자 (~800토큰) |
| **USER.md** | 사용자 프로필 — 선호·기대치 | 1,375자 (~500토큰) |

세션 시작 때 **얼려진 스냅샷(frozen snapshot)**으로 시스템 프롬프트에 주입되고, v0.20.5부터는 **크론잡 단위로도 영구 메모리**가 적용됩니다. "이 블로그는 한국어로 써", "이미지는 AI 생성 금지" 같은 규칙을 한 번 메모에 넣어두면 다음 크론 실행에서도 기억합니다.

![Hermes Agent 메모리 기능 이미지](/assets/images/posts/hermes-ai-newsroom-runtimewire/hermes-feature-memory.webp)
*Hermes Agent 공식 홈페이지의 'Remember — Persistent Memory' 기능 소개 — 프로젝트를 학습하고 스킬을 자동 생성하며 문제 해결 방법을 잊지 않는다 (출처: hermes-agent.nousresearch.com 공식 홈페이지, 2026-08-27 확인)*

---

## 5. 최신 상황 — 어제(8/26)까지도 개발 중 (실측)

릴리스는 지난주 **v0.20.5(2026.8.19 태그)**가 마지막이고, 정식 큐레이티드 노트는 v0.21.0에서 나옵니다. 그 사이 main 브랜치는 여전히 매일 움직이고 있는데, 8/26 커밋을 GitHub API로 실측한 결과입니다.

- **feat: MiniMax H3 Max가 FAL 비디오 픽커에 합류** (t2v + i2v) — 이미지·영상 생성 툴이 하나 더 늘었습니다
- **refactor(skills): skill_manage 924 → 518 tok/call** — 스킬 관리 툴의 토큰 사용량을 절반으로 다이어트. 크론잡 비용에 직접 영향을 주는 작업
- **fix(slack)**: 턴 클레임(ts claim) 관련 수정 2건 — 메시지 중복·누락 방지
- **fix(desktop)**: 세션 행을 (profile, id)로 식별, SSH 고아 프로세스 정리, 업데이트 후 흰 화면 대신 오류 표시
- **fix(kanban)**: 리뷰 변경 시 웨이크 컨트롤러, i18n 17개 로케일 웨이크 키

프로젝트 규모는 **GitHub 스타 약 236,881개(8/27 API 실측)**, 포크 47,907개, MIT 라이선스입니다. 하루에도 수십 개 PR이 머지되는 속도라, "어제 릴리스 노트를 봤는데 오늘 이미 그다음 버전"인 게 일상이에요.

---

## 6. 솔직한 장단점 총정리

### 👍 이건 따라 해도 좋아요

- **1인 뉴스룸/자동포스팅은 이미 현실** — RuntimeWire 사례가 증명했듯, 발견→작성→발행 루프는 개인도 만들 수 있습니다
- **Hermes Agent는 그 재현 비용을 0원으로** — MIT 라이선스 무료 오픈소스, 자연어 크론잡, `/learn` 스킬, 영구 메모리까지 갖추면 사실상 "나만의 RuntimeWire" 시작점이 됩니다
- **잡별 reasoning effort** — 가벼운 잡은 `minimal`, 무거운 잡은 `high`로 나누면 비용 관리가 됩니다
- **`--workdir`로 프로젝트 규칙 주입** — 크론잡이 AGENTS.md를 읽고 그 규칙대로 일하게 할 수 있습니다

### 👎 이건 조심해야 해요

- **품질 논란** — RuntimeWire 기사는 "평이하고 물량 지향"이라는 비판이 정확합니다. 스피드와 깊이는 다른 축이에요
- **책임성 문제** — Santage가 지적한 대로, **에이전트는 하우스 스타일은 강제해도 독립성은 강제하지 못합니다.** Merket이 창업자 요청으로 정확한 스쿱을 철회한 사례에서 보듯, 판단이 한 사람에게 모이는 구조입니다
- **자동 발행은 리스크 관리가 전제** — 무인 발행을 하려면 사전 검토 규칙(리스크 평가)을 먼저 설계해야 합니다
- **메모리·스킬 관리 비용** — 메모리가 꽉 차면 에이전트가 스스로 정리하지만, 컨텍스트가 커질수록 잡이 무거워지는 건 피할 수 없습니다

---

## 7. 마무리 — 오늘부터 한 잡만 만들어보세요, 가성비 좋은 AI 도구 찾는 분께 추천

정리하면, **RuntimeWire는 "기자 없이 돌아가는 뉴스룸"의 가능성을 보여줬고**, Hermes Agent는 그 구조를 **개인이 무료로 재현**할 수 있게 해주는 도구입니다. 굳이 뉴스가 아니라도, "매일 아침 특정 주제를 조사해서 보고서/포스팅을 만들어주는 잡" 하나만 크론으로 만들어보면 감이 옵니다.

```bash
# 오늘부터 시작하는 한 줄 — 매일 9시, AI 뉴스 요약 잡
/cron add "every 1d at 09:00" "AI 뉴스를 조사해서 3줄 요약을 텔레그램으로 보내줘"
```

시작 팁을 드리자면,

1. **스킬부터** — 자신의 글쓰기 스타일을 `/learn`으로 스킬화하세요. 이게 "하우스 스타일"입니다
2. **메모리에 규칙을** — "한국어로 써", "AI 이미지 금지" 같은 상수(常數)를 메모리에 넣으세요
3. **작게 시작** — 하루 1개 잡, `minimal` reasoning effort로 시작해서 비용 감을 익히세요
4. **검증 단계를 넣으세요** — 제 블로그처럼 "최종 점검 후 보고" 스킬을 크론잡에 달아두면 무인 발행의 리스크가 줄어듭니다

RuntimeWire가 보여준 미래의 가장 좋은 점은 "이제 이런 걸 **혼자서도** 만들 수 있다"는 거고, Hermes Agent는 그 시작점을 **무료**로 열어줍니다. 솔직히 한 달 전만 해도 "블로그가 사람 없이 매일 포스팅된다"는 상상을 어떻게 설명해야 할지 막막했는데, 지금은 이 글 자체가 그 증거입니다. **가성비 좋은 AI 도구를 찾으시는 분, 그리고 자동화의 재미를 느껴보고 싶은 분께 Hermes Agent 추천합니다.**

*※ 본 포스트는 Hermes Agent 공식 문서(hermes-agent.nousresearch.com/docs), GitHub 릴리스·커밋 API(8/27 실측), RuntimeWire 공식 사이트 및 Santage·Developments Today·Gizmodo 보도를 실측 인용하여 2026-08-27에 Hermes Agent 크론잡으로 작성했습니다.*
