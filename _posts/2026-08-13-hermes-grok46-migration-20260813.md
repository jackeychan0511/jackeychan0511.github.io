---
layout: post
title: "Hermes Agent 최신 소식 — Grok 4.6 출시 당일 지원·OpenClaw→Hermes 마이그레이션 가이드 총정리 (2026.8.13)"
date: 2026-08-13 12:00:00 +0900
categories: [career]
tags: [HermesAgent, NousResearch, AI에이전트, Grok4.6, xAI, OpenClaw, 마이그레이션, KimiK3, Moonshot, AI뉴스, 오픈소스AI, AI툴추천, 2026년8월]
author: "40대 블로거"
image: /assets/images/posts/hermes-grok46-migration-20260813/grok46-og.png
description: "8월 12일 xAI가 롱러닝 에이전트 특화 모델 Grok 4.6을 공개했고, 오픈소스 AI 에이전트 Hermes Agent가 같은 날 당일 지원을 추가했습니다. 또 OpenClaw에서 Hermes로 설정·메모리·스킬·API 키를 통째로 옮기는 'hermes claw migrate' 공식 가이드와 Kimi K3 리즈닝 매핑, 프로필 export/import까지 최신 소식을 정리했습니다."
---
요즘 저처럼 **오픈소스 AI 에이전트**를 챙겨보시는 분들, 특히 **OpenClaw**를 쓰면서 "요즘 Hermes Agent도 많이 커지던데, 갈아탈까?" 고민해 보신 분들 많으시죠? 저도 이 블로그 자동 포스팅을 **Hermes Agent 크론잡**으로 돌리고 있어서, 두 에이전트의 소식을 매일 같이 보고 있는데요.

어제(8/12) 하루 만에 **에이전트 생태계를 흔드는 소식이 두 개나** 나왔습니다. 하나는 **xAI의 Grok 4.6** 출시, 다른 하나는 **Hermes Agent의 Grok 4.6 당일 지원**이에요. 게다가 Hermes 공식 문서에는 **OpenClaw → Hermes 마이그레이션 가이드**가 정식으로 공개되어 있더라고요. 솔직히 이거 보자마자 "이제 OpenClaw 유저들도 부담 없이 넘어오라는 뜻이구나" 싶었습니다.

이번 글에서는 **Grok 4.6 출시 → Hermes 당일 지원 → Kimi K3 업데이트 → OpenClaw 마이그레이션 가이드** 순서로 핵심만 정리해 드릴게요.

![Grok 4.6 공식 발표 이미지](/assets/images/posts/hermes-grok46-migration-20260813/grok46-og.png)
*Grok 4.6 공식 발표 이미지 (출처: xAI 공식 뉴스룸 x.ai/news/grok-4-6)*

## 1. xAI, Grok 4.6 공개 — "롱러닝 에이전트"에 올인한 모델 (8/12)

xAI(SpaceXAI)는 **8월 12일 Grok 4.6**을 공식 출시했습니다. 7월에 나온 Grok 4.5의 후속작인데, 이번엔 "원시 지능 점수 올리기"보다 **오래 달리는 에이전트(롱러닝 에이전트)와 인터랙티브·비주얼 작업**에 초점을 맞췄다고 해요.

핵심 벤치마크만 보면 체감이 확 옵니다.

- **AA Intelligence Index 61점** — GPT-5.6 Sol Max(61점)와 **동률**, Fable 5 Max(62점)에 근접, 이전 세대 Grok 4.5 High(56점) 대비 큰 도약
- **CursorBench 3.2 69.9%** — 코딩 에이전트 벤치마크에서 Grok 4.5 High(66.7%)를 확실히 앞섬
- **50만 토큰 컨텍스트**, 텍스트+이미지 입력 지원

공식 발표에 따르면 이번 모델은 **SFT와 RL을 더 오래, 더 정교하게 돌린 게 핵심**입니다. 커널 최적화, 웹 개발, CAD 같은 도메인별 에이전트 RL 태스크로 훈련했고, "대충 아이디어만 던져줘도 프로토타입까지 만들어 주는" 능력이 특히 좋아졌다고 해요. 출시 당일부터 **Cursor와 Grok Build에서 바로 쓸 수 있고, 첫 주에는 사용량 2배**를 제공합니다.

## 2. Hermes Agent, Grok 4.6을 "출시 당일" 지원

그런데 재미있는 건 다음 소식이에요. Hermes Agent의 GitHub 저장소를 보니 **8월 12일 "add grok 4.6 (#84837)" 커밋**이 올라와 있더라고요. **xAI가 공개한 바로 그날, Hermes에 Grok 4.6 모델 지원이 추가된 겁니다.**

이게 왜 중요하냐면, Hermes Agent는 `hermes model` 명령어 하나로 모델을 바꿀 수 있는 구조라서 **릴리즈 노트를 기다릴 필요 없이 바로 Grok 4.6으로 전환**해서 쓸 수 있기 때문이에요. 저처럼 "새 모델 나오면 제일 먼저 써보고 싶은 분들"한테는 이 속도가 생각보다 큰 장점입니다. 솔직히 작년까지만 해도 오픈소스 에이전트가 최신 프론티어 모델을 당일 지원한다는 건 상상도 못 했거든요.

같은 날 올라온 모델 관련 커밋도 눈에 띄었습니다.

- **Kimi K3 리즈닝 매핑** — Moonshot AI가 7/17 공개한 **세계 최대 오픈웨이트 모델 Kimi K3**(OpenAI·Anthropic과 견줄 만한 성능으로 시장에 충격을 준 그 모델)를 Hermes에서 쓸 때, Hermes의 reasoning effort 값을 K3가 쓰는 **low/high/max 어휘에 맞게 자동 변환**하도록 수정
- **Meta AI 프로바이더 매핑 수정** — models.dev의 'meta' ID로 정상 연결되도록 개선

K3는 기존 모델들과 리즈닝 옵션 체계가 달라서 "분명히 설정했는데 왜 안 먹지?" 하는 상황이 나올 수 있었는데, 이제 Hermes에서 K3를 붙여 쓰는 분들은 그런 삽질을 하지 않아도 됩니다.

## 3. OpenClaw → Hermes 마이그레이션 가이드 — `hermes claw migrate`

이번에 가장 눈여겨봐야 할 건 따로 있습니다. Hermes 공식 문서에 **"Migrate from OpenClaw" 가이드**가 정식으로 올라왔는데요, OpenClaw(또는 레거시 Clawdbot/Moldbot) 설정을 **한 번의 명령어로 Hermes로 통째로 이전**할 수 있게 됐습니다.

### 3-1. 사용법은 초간단

```bash
hermes claw migrate              # 미리보기 후 확인하면 이전 시작
hermes claw migrate --dry-run    # 변경 없이 미리보기만
hermes claw migrate --preset full --migrate-secrets --yes   # API 키 포함 전체 이전
```

기본적으로 `~/.openclaw/` 폴더를 자동으로 찾고, 레거시 `~/.clawdbot/`·`~/.moltbot/`도 자동 감지합니다. 그리고 **실제 적용 전에 "뭐가 옮겨지는지" 전체 미리보기를 무조건 보여준 뒤** 확인을 받아요. 마음에 안 들면 거기서 취소하면 됩니다. 안전장치가 잘 돼 있어서 "설정 다 날아가는 거 아니야?" 하는 걱정은 안 하셔도 됩니다.

주요 옵션은 이렇게 정리할 수 있어요.

| 옵션 | 역할 |
|:-----|:-----|
| `--dry-run` | 미리보기만 하고 중단 |
| `--preset full / user-data` | 전체 / 인프라 설정 제외한 사용자 데이터만 |
| `--migrate-secrets` | API 키 포함 (어떤 프리셋이든 **기본은 미포함** — 명시적으로 줘야 함) |
| `--overwrite` | 충돌 시 기존 Hermes 파일 덮어쓰기 (기본은 충돌 시 거부) |
| `--source <경로>` | OpenClaw 폴더 위치 직접 지정 |
| `--workspace-target <경로>` | AGENTS.md(워크스페이스 지침)를 놓을 위치 지정 |
| `--skill-conflict skip/overwrite/rename` | 스킬 이름 충돌 처리 방식 |
| `--no-backup` | 사전 백업 건너뛰기 (기본은 `~/.hermes/backups/pre-migration-*.zip` 자동 생성) |

### 3-2. 뭐가 옮겨지나?

직접 써보니 "생각보다 훨씬 꼼꼼하다"가 첫인상이었습니다. 정리하면 이렇습니다.

- **페르소나·메모리**: SOUL.md, AGENTS.md, 장기 메모리 MEMORY.md, 사용자 프로필 USER.md, 일일 메모리 파일까지 전부 파싱해서 **중복 제거 후 병합**
- **스킬**: 워크스페이스 스킬, 관리형 스킬, 개인 스킬, 프로젝트 공용 스킬 **4개 소스** 모두 `~/.hermes/skills/openclaw-imports/`로 이전
- **모델·프로바이더**: 기본 모델, 커스텀 프로바이더(baseUrl·apiType 포함), API 키
- **에이전트 동작**: 최대 턴 수, 리즈닝 effort, 압축 모드, 휴먼 딜레이, 타임존, Docker 샌드박스 설정
- **세션 리셋 정책**: daily/idle 모드와 시간·유휴 시간 설정
- **MCP 서버**: command/args/env/url, 툴 필터까지 그대로
- **TTS**: ElevenLabs·OpenAI·Edge TTS 설정과 음성 자산 파일
- **메시징 플랫폼**: 텔레그램·디스코드·슬랙·왓츠앱·시그널·매트릭스·매터모스트 토큰과 허용 사용자 목록
- **기타**: 승인 모드(auto→off, always→manual, smart→smart), 명령 허용 목록, 브라우저 CDP·헤드리스 설정, Brave 검색 키, 게이트웨이 토큰

### 3-3. 안 옮겨지는 것도 명확하게

반대로 Hermes에 대응 항목이 없는 것들은 `~/.hermes/migration/openclaw/<타임스탬프>/archive/`에 **보관만** 됩니다. IDENTITY.md, TOOLS.md, HEARTBEAT.md, BOOTSTRAP.md, 크론잡, 플러그인, 훅, 멀티에이전트 목록 같은 건데요 — 크론잡은 `hermes cron create`로 다시 만들고, HEARTBEAT.md는 Hermes 크론잡으로 대체하라는 식으로 **재구성 방법까지 문서에 안내**되어 있습니다. "조용히 버려지는 데이터 없음"이라는 점이 참 마음에 들었습니다.

### 3-4. API 키 처리와 마이그레이션 후 체크리스트

API 키는 `--migrate-secrets`를 줬을 때만 옮겨지고, **허용 목록에 있는 키**(OpenRouter, OpenAI, Anthropic, DeepSeek, Gemini, ZAI, MiniMax, ElevenLabs, 텔레그램 등)만 복사됩니다. openclaw.json 안의 값 → `~/.openclaw/.env` → config의 env 서브객체 → auth-profiles.json **4개 소스에서 순서대로 채워 넣는** 방식이라 "키가 어디 저장돼 있었는지 모르겠다"는 상황에서도 유용합니다.

마이그레이션 후엔 문서가 안내하는 대로 다음 순서만 밟으면 됩니다.

1. `hermes status`로 API 키 인증 확인
2. 게이트웨이 재시작 (`systemctl --user restart hermes-gateway`)
3. **왓츠앱은 QR 재페어링 필수** (토큰이 아니라 Baileys QR 페어링 방식이라 따로 해줘야 함)
4. `hermes config show`로 세션 리셋 정책 확인
5. 전부 정상이면 `hermes claw cleanup`으로 기존 OpenClaw 폴더를 `.pre-migration/`으로 정리

![OpenClaw 공식 문서 대표 이미지](/assets/images/posts/hermes-grok46-migration-20260813/openclaw-hero.png)
*OpenClaw 공식 문서 대표 이미지 (출처: OpenClaw 공식 문서 docs.openclaw.ai)*

솔직한 한 줄 평가를 하자면, **OpenClaw를 오래 쓰면서 SOUL.md·스킬·메시징 설정을 많이 쌓아둔 분일수록 이 가이드의 가치가 큽니다.** 처음부터 다시 설정하는 것과는 비교가 안 되거든요. 반대로 "나 그냥 심플하게 쓰는 편"이신 분들도 `--preset user-data`로 필요한 것만 골라서 옮길 수 있습니다.

![Hermes Agent 공식 홈페이지 — 메시징 플랫폼 연결 화면](/assets/images/posts/hermes-grok46-migration-20260813/hermes-feature-connect.jpg)
*Hermes Agent 공식 홈페이지 — 메시징 플랫폼 연결 기능 (출처: Hermes Agent 공식 홈페이지)*

## 4. 프로필 export/import — 이제 `/export`, `/import`로 프로필 공유

마지막으로, 에이전트 사용자라면 쓸모가 많은 작은 업데이트 하나 더 알려드릴게요. Hermes가 **`/export`, `/import` 슬래시 커맨드로 프로필(설정·메모리·스킬 묶음)을 통째로 공유하는 방법**을 공식 문서에서 정식 안내하기 시작했습니다. 기존엔 백업/복원 중심이었다면, 이제 "내가 만든 SOUL.md와 스킬 세트를 다른 PC나 동료한테 그대로 넘겨주는" 시나리오가 훨씬 쉬워진 거죠. 저처럼 데스크톱+클라우드 VM을 오가며 쓰는 분들한테는 꽤 반가운 기능입니다.

## 5. 정리하며 — 오픈소스 에이전트의 속도가 무서워지는 시점

오늘 정리한 내용을 한 줄로 요약하면 이렇습니다.

> **모델은 당일 지원(Grok 4.6), 이전은 원클릭(hermes claw migrate), 공유는 슬래시 커맨드(/export·/import).**

Hermes Agent는 이 글을 쓰는 시점(8/13) 기준 **GitHub 스타 229,551개**로 오픈소스 에이전트 중 최상위권을 유지하고 있고, 커밋 로그만 봐도 하루에도 수십 개의 PR이 머지되고 있습니다. 특히 이번 **OpenClaw 마이그레이션 가이드 공개**는 "에이전트 시장이 경쟁을 넘어 흡수·통합의 단계"로 접어들었다는 신호로 읽힙니다.

**마이그레이션을 고민 중이신 분께 드리는 팁** — 일단 `hermes claw migrate --dry-run`으로 미리보기만 돌려보세요. 1분이면 "내 데이터가 어디로 어떻게 옮겨지는지" 전체 목록을 볼 수 있고, 부담 없이 결정할 수 있습니다. API 키까지 옮기고 싶다면 `--migrate-secrets`를 꼭 붙이는 것만 기억하시면 됩니다. 가성비 좋은 오픈소스 AI 에이전트 찾는 분께 추천드립니다.

![Hermes Agent 공식 홈페이지 — 브라우저·리서치 기능 화면](/assets/images/posts/hermes-grok46-migration-20260813/hermes-feature-browse.jpg)
*Hermes Agent 공식 홈페이지 — 브라우저·리서치 기능 (출처: Hermes Agent 공식 홈페이지)*

- 공식 발표: [Introducing Grok 4.6 — xAI](https://x.ai/news/grok-4-6)
- 공식 가이드: [Migrate from OpenClaw — Hermes Agent Docs](https://hermes-agent.nousresearch.com/docs/guides/migrate-from-openclaw)
- 저장소: [NousResearch/hermes-agent (GitHub)](https://github.com/NousResearch/hermes-agent)
