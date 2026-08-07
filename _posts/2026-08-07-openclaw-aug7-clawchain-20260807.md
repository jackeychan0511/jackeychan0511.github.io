---
layout: post
title: "OpenClaw 추천: 'Claw Chain' 보안 경고 재점화 + 메인브랜치 P0 수정 봇물 — 8월 7일자 최신 업데이트 총정리"
date: 2026-08-07 10:50:00 +0900
categories: [career]
tags: [OpenClaw, 오픈클로, AI에이전트, Claw Chain, 보안, 취약점, v2026.7.2, 메인브랜치, TechRadar, 업데이트, 릴리즈, 오픈소스AI, 2026년8월]
author: "40대 블로거"
image: /assets/images/posts/openclaw-aug7-20260807/openclaw-official-site.png
description: "2026년 8월 7일 기준 오픈소스 AI 에이전트 OpenClaw의 최신 소식입니다. 커뮤니티 뉴스 'The Claw Report'가 8월 7일자로 'Claw Chain' 다단계 공격 경로를 다시 조명했고, TechRadar(8/5)는 ThreatLocker CEO 명의로 'OpenClaw 공격 이후 방어 재설계' 오피니언을 게재했습니다. 같은 기간 메인브랜치에는 P0 워크트리 데이터 손실 수정(#119691/#119709)을 포함한 보안·안정성 수정이 봇물처럼 합류했으며, GitHub 스타 385,392개(+132), npm 채널 현황(latest 2026.7.1-2 / extended-stable 2026.6.34 / beta 2026.7.2-beta.7)을 정리했습니다."
---

요즘 저처럼 **오픈소스 AI 에이전트 OpenClaw** 소식을 매일 챙겨보시는 분들, 계시죠? 지난 8월 6일 글([v2026.7.1-1·7.1-2 핫픽스 총정리](/2026/08/06/openclaw-aug6-hotfix-20260806/))에서 "정식 v2026.7.2만 기다리면 된다"고 말씀드렸는데, **하루 사이에 챙길 소식이 또 쌓였습니다.**

오늘(8월 7일) 아침, The Claw Report·openclaw.academy·GitHub API·npm 레지스트리를 직접 확인한 결과 기준으로 정리하면 **① 커뮤니티가 'Claw Chain' 다단계 공격 경로를 다시 조명, ② TechRadar가 ThreatLocker CEO 명의 보안 오피니언 게재, ③ 메인브랜치에 P0 워크트리 데이터 손실 수정 포함 수정 봇물, ④ GitHub 스타 385,392개 돌파 & v2026.7.2 정식판은 여전히 대기 중** — 이렇게 네 가지로 요약됩니다.

![OpenClaw 공식 홈페이지 히어로](/assets/images/posts/openclaw-aug7-20260807/openclaw-official-site.png)
*OpenClaw 공식 홈페이지 메인 — "The AI that really does things." 원라인 설치 명령어 제공 (출처: openclaw.ai, 확인일 2026-08-07)*

---

## 1. 핵심 소식 ① — 'Claw Chain' 보안 경고, 커뮤니티가 다시 조명

커뮤니티 뉴스 사이트 **The Claw Report**가 8월 7일자 헤드라인으로 보안 연구자·업계 그룹이 **오래되거나 잘못 설정된 OpenClaw 배포를 노리는 다단계 "Claw Chain" 공격 경로**를 상세히 정리했다고 전했습니다. 그러면서 **"2026.7.1 같은 일상적 릴리즈가 계속 나오는 가운데, 운영자들은 신속한 업데이트와 설정 강화로 노출을 줄이라"**고 권고했어요.

![The Claw Report 뉴스 페이지](/assets/images/posts/openclaw-aug7-20260807/openclaw-clawreport-news.png)
*The Claw Report 메인 — "Signal over hype." 8/7자 'Claw Chain' 보안 스포트라이트와 8/5~8/6 메인브랜치 수정 뉴스 (출처: theclawreport.com, 확인일 2026-08-07)*

### Claw Chain이 뭔가요? (5월 공개, 4개 CVE 연쇄)

Claw Chain은 지난 5월 보안 연구자 **Vladimir Tokarev와 Cyera**가 공개한 **4개 취약점(CVE)을 사슬처럼 연결한 공격 기법**입니다. 샌드박스 격리 계층, 명령 검증 파이프라인, 신원(identity) 모델을 가로지르며, **인증 없는 공격자가 서버에서 임의 명령을 실행하고 자격증명을 탈취**할 수 있는 경로로 알려졌습니다. 연구진이 특히 강조한 건 **각 단계가 정상적인 에이전트 동작처럼 보여서 기존 호스트 모니터링으로 탐지하기 어렵다**는 점이에요.

핵심은 **"구버전·공개 노출·과한 권한" 조합**입니다. 최신 버전으로 업데이트하고, 관리 인터페이스를 외부에 노출하지 않으며, 최소 권한 원칙을 지키면 실질 위험이 크게 줄어듭니다.

### TechRadar 오피니언 (8/5) — "OpenClaw 공격 이후, 방어를 재설계하라"

여기에 더해 8월 5일 **TechRadar Pro**에는 ThreatLocker CEO **대니 젠킨스** 명의의 오피니언 **"Rethinking defense in the wake of OpenClaw attacks"**가 실렸습니다. 요지는 이렇습니다.

- 오픈소스 직후 연구자들이 **노출된 관리 인터페이스와 악성 "skills" 패키지**를 발견했고, 공격자도 똑같이 빠르게 움직였다
- AI 에이전트는 엔드포인트에서 직접 실행되며 **파일시스템·브라우저·클라우드 서비스 접근**을 요구하므로, **"탐지(Detection)"만으로는 늦다**
- **기본 거부(deny-by-default) 원칙** — 승인되지 않은 앱·스크립트는 처음부터 실행되지 못하게 하는 **애플리케이션 허용 목록(allowlisting)** 접근이 필요하다
- **섀도우 AI(Shadow AI)** 문제 — 직원들이 승인 없이 로컬 AI 에이전트를 깔고 커뮤니티 스킬을 설치해 회사 자원에 연결하는 현상이 핵심

솔직히 "또 보안 뉴스냐" 싶을 수 있는데, **5월 Claw Chain 공개 → 6월 피싱 악용 사례 → 8월 TechRadar 오피니언**으로 이어지는 흐름을 보면 **AI 에이전트 보안이 이제 일반 IT 미디어의 메인 이슈**가 됐다는 게 체감됩니다. 그리고 재밌게도, 제가 오늘 아침 GitHub를 확인하는 순간에도 **steipete 님의 최신 커밋(804ae7f)이 "fix(security): stop recommending retired install-policy bypasses"** — 즉 **"퇴역한 설치정책 우회법 추천 중단"** — 였습니다. 개발팀도 보안을 최우선으로 두고 있다는 방증이에요.

---

## 2. 핵심 소식 ② — 메인브랜치 보안·안정성 수정 봇물 (8/5~8/6)

v2026.7.2 정식판이 아직 안 나왔는데도, 메인브랜치에는 **8월 5~6일 이틀간 운영자에게 중요한 수정이 잇따라 합류**했습니다. openclaw.academy가 검증한 것들 중 눈에 띄는 것만 골라볼게요.

### ⚠️ P0: 워크트리 정리 중 데이터 손실 (최우선 확인!)

| 항목 | 내용 |
|:----|:----|
| **이슈** | [#119691](https://github.com/openclaw/openclaw/issues/119691) — P0 워크트리 정리 데이터 손실 보고 |
| **수정** | [#119709](https://github.com/openclaw/openclaw/pull/119709) "Preserve registered worktree roots during orphan cleanup" (커밋 1e347e2) |
| **문제 상황** | OpenClaw가 관리하는 worktrees 루트 **바로 아래**에 등록된 Git 워크트리가 **고아(orphan) 디렉터리로 오인**되어 재귀적으로 삭제될 수 있었음 |
| **추가 변형** | 첫 커밋이 없는 unborn 워크트리, **심볼릭 링크로 연결된 상태 루트** 경로도 해당 |
| **수정 방향** | 삭제 전 **Git 등록 워크트리 뷰·정규 경로 기준으로 확인**하고, 메타데이터 검사가 실패하면 **fail-closed(보존)** 처리 |

`.git` 폴더가 있다고 안전한 게 아니라는 점이 함정이었습니다. 워크트리를 OpenClaw 상태 루트 아래에 두고 쓰시는 분은 **지금 당장 해당 경로를 백업**해두시고, 수정이 포함된 빌드가 나오면 **테스트용 워크트리(직접 루트/중첩/unborn/심볼릭 레이아웃)로 검증**하시길 권합니다. 관련해서 **게이트웨이 실행 중에도 검증된 백업이 가능하도록** 하는 PR [#119782](https://github.com/openclaw/openclaw/pull/119782)도 함께 합류했어요.

### 그 외 8/5~8/6 주요 수정

| 날짜 | 수정 내용 |
|:----|:----|
| 8/6 | **크로스채널 전달 순서·폴백 수리** — 여러 채널(Telegram·Slack·Discord 등) 간 메시지 전달 순서와 장애 시 대체 경로 정리 |
| 8/6 | **긴 마무리(finalization) 중 미디어·실행 상태 보존** — 오래 걸리는 턴에서 생성된 미디어가 사라지지 않도록 수정 |
| 8/6 | **본문 없는 HTTP 400 오류가 컴팩션(compaction)을 유발하던 문제 수정** — 컨텍스트 오버플로로 오인해 불필요한 압축·재시도가 돌던 문제 차단 |
| 8/5 | **세 가지 "조용한" 최종 답변 전달 실패 수정** — 중간 진행 메시지가 전체 답변을 삼키던 문제, 이미지 컨텍스트가 턴과 분리되던 문제, 구조적 하트비트 응답 보존 |
| 8/5~8/6 | **게이트웨이 워밍업을 지연 민감 RPC 경로에서 분리** — 응답성 개선 |
| 8/5 | **디스크 예산 0으로 설정 시 세션 기록이 삭제되던 문제 수정** |

솔직히 이번 수정들은 **"화려한 신기능"보다 "조용한 데이터 손실·메시지 유실"을 정조준**하고 있습니다. 특히 P0 워크트리 건은 **"에이전트가 알아서 Git 작업을 하다가 체크아웃이 통째로 사라진"** 분들이라면 체감이 클 것 같아요. 정식판 직전에 이렇게 안정성·데이터 안전에 집중하는 건, **v2026.7.2가 "복구와 내구성"을 테마로 한 릴리즈**라는 기존 전망과도 맞물립니다.

---

## 3. 핵심 소식 ③ — GitHub 스타 385,392개 돌파 & 버전 현황

오늘(8/7) 아침 GitHub API로 직접 확인한 수치입니다.

| 지표 | 수치 (2026-08-07 확인) | 변화 |
|:----|:----|:----|
| ⭐ GitHub Stars | **385,392** | 8/6 385,260 → **+132** |
| 🍴 Forks | **81,020** | 8/6 80,987 → +33 |
| 🔢 오픈 이슈 | 5,553 | — |
| 🕐 마지막 push | **2026-08-07 01:38 UTC** | 확인 직전에도 커밋 진행 중 |
| 📝 전체 커밋 | **76,883** | 최신 커밋은 steipete 님의 보안 수정(804ae7f) |

![OpenClaw 공식 GitHub 저장소](/assets/images/posts/openclaw-aug7-20260807/openclaw-github-repo.png)
*OpenClaw 공식 GitHub 저장소 메인 — Star 385k, Fork 81k, 최신 커밋이 계속 올라오는 중 (출처: github.com/openclaw/openclaw, 확인일 2026-08-07)*

npm 채널 현황도 다시 확인했습니다. **어제와 동일하게** 정식 v2026.7.2는 아직 나오지 않았습니다.

| npm 채널 | 버전 | 상태 |
|:----|:----|:----|
| **latest** (안정판) | **2026.7.1-2** | 8/4 핫픽스 반영, 최신 안정판 |
| **extended-stable** (LTS 지향) | **2026.6.34** | 8/4 발행, 검증판 |
| **beta** | **2026.7.2-beta.7** | 8/2 발행, 안정화 막바지 |
| 예정 | **v2026.7.2 정식판** | 하이라이트 문서 완비 → 릴리즈 대기 중 |

### 8/6~8/7 커밋 하이라이트 (일반 사용자 눈높이)

- **`fix(googlechat): redact reflected credentials in API errors (#119965)`** — Google Chat API 오류에 **자격증명이 그대로 반사되어 노출되던 문제** 수정. 보안 관련 커밋이라 눈에 띕니다
- **`fix(plugins): preserve startup release during repair (#120085)`** — 플러그인 복구 중 시작 릴리즈 보존
- **`refactor(browser): remove model-backed page extraction (#120101)`** — 브라우저 페이지 추출 경량화
- **`fix(release): restart gateway after migration convergence (#120091)`** — 마이그레이션 수렴 후 게이트웨이 자동 재시작
- **`fix(tui): avoid repeated runtime rebuilds in local sessions (#120051)`** — 로컬 세션에서 TUI 재빌드 반복 방지
- **`fix(agents): resolve authoritative session keys for delegated compaction (#120047)`**, **`fix(memory): report persisted vector index state (#120048)`** — 세션·메모리 상태 정확성
- **`fix(doctor): recover terminal NUL-only tails in archived session JSONL (#120041)`** — 보관 세션 파일 끝부분 복구
- **`fix(protocol): preserve gateway session attribution across node runs`** (8/5) — 노드 실행 간 세션 귀속 보존

---

## 4. 지금 설치해도 될까? — 솔직한 판단 + 보안 체크리스트

**제 솔직한 판단**은 이렇습니다.

- **지금 안정판(2026.7.1-2)을 쓰고 계신 분** → 그대로 유지하셔도 좋습니다. 다만 이번 주 보안 분위기를 고려해 **아래 체크리스트는 꼭 한 번** 돌려보세요
- **자동화를 밤새 돌리는 분** → extended-stable(2026.6.34) 유지가 여전히 무난합니다
- **베타.7을 쓰고 계신 분** → 정식 v2026.7.2가 나오면 한 번에 올리는 게 가장 안전합니다. 특히 **워크트리 정리 수정(#119709)이 포함된 빌드인지 릴리즈 노트에서 확인**하세요

```bash
# 안정판 유지 (현재 최신: 2026.7.1-2)
npm install -g openclaw@latest

# LTS 지향 검증판
npm install -g openclaw@extended-stable

# 베타 미리 써보기
npm install -g openclaw@beta

# 버전 확인 & 문제 자동 수정
openclaw version
openclaw doctor --fix
```

**보안 체크리스트 (이번 주 뉴스 기준):**

1. **업데이트 상태 확인** — `openclaw version`으로 최신 안정판 여부 체크. Claw Chain은 "오래된 버전 + 잘못된 설정" 조합을 노립니다
2. **관리 인터페이스 노출 차단** — 대시보드·게이트웨이 포트를 **공개 인터넷에 열어두지 마세요**. VPN·로컬 접근만 허용
3. **스킬(skill) 설치 시 출처 확인** — 커뮤니티 스킬은 **공식 저장소·검증된 게시자**만. TechRadar도 악성 스킬 패키지를 첫 번째 위협으로 꼽았습니다
4. **최소 권한 원칙** — 에이전트에게 파일·브라우저·자격증명 접근을 줄 때 **꼭 필요한 것만**. "기본 거부(deny-by-default)" 마인드가 이제 업계 표준 권고입니다
5. **워크트리 경로 점검** — OpenClaw 상태 루트 아래에 둔 Git 워크트리가 있다면 **지금 백업** (P0 이슈 반영)

![ClawCon 2026 현장 사진](/assets/images/posts/openclaw-aug7-20260807/openclaw-clawcon-2026.jpg)
*ClawCon 2026 현장 — OpenClaw 창시자 Peter Steinberger와 커뮤니티의 만남. 보안 이슈에도 커뮤니티는 계속 성장 중 (출처: Wikimedia Commons, CC0, 촬영 2026-02-04)*

---

## 마무리 — 오늘 소식 한 줄 정리

1. **'Claw Chain' 보안 경고 재조명 (8/7)** — The Claw Report가 다단계 공격 경로 정리, "업데이트 + 설정 강화" 권고
2. **TechRadar 오피니언 (8/5)** — ThreatLocker CEO, "탐지보다 기본 거부(deny-by-default)" Zero Trust 방어 주장
3. **메인브랜치 수정 봇물 (8/5~8/6)** — **P0 워크트리 데이터 손실 수정(#119691/#119709)** 포함, 전달·복구·컴팩션 오류 정리
4. **스타 385,392개 (+132) & v2026.7.2 정식판은 여전히 대기 중** — latest 2026.7.1-2 / extended-stable 2026.6.34 / beta 2026.7.2-beta.7

저처럼 **"신기능은 정식판에서, 보안은 지금 당장"** 전략으로 OpenClaw를 운영하시는 분께는, 이번 주 뉴스가 **"업데이트 주기 관리의 중요성"**을 다시금 일깨워주는 한 주였을 겁니다. 보안 경고가 나올 때마다 놀라기보다, **업데이트 확인 → 노출 차단 → 최소 권한** 3가지를 루틴으로 만들어두면 훨씬 마음이 편해져요.

관련해서 지난 글([8/6 핫픽스 총정리](/2026/08/06/openclaw-aug6-hotfix-20260806/), [8/3 크롬 확장프로그램 등재 총정리](/2026/08/03/openclaw-aug3-20260803/), [v2026.7.1 LTS·성숙도 점수카드](/2026/07/31/openclaw-v202671-lts-maturity-scorecard/))과 함께 읽으시면 전체 흐름이 잡힙니다. **정식 v2026.7.2가 나오는 대로, 그리고 보안 공지가 업데이트되는 대로 다시 찾아뵙겠습니다.**

---

*본문의 수치·커밋·릴리즈 정보는 GitHub 공식 API(github.com/openclaw/openclaw), npm 레지스트리(registry.npmjs.org), The Claw Report(theclawreport.com), openclaw.academy, TechRadar(techradar.com)를 기준으로 2026-08-07에 직접 확인했습니다. Claw Chain 취약점 상세는 Cyera 연구 노트와 [공식 릴리즈 페이지](https://github.com/openclaw/openclaw/releases)에서 확인하실 수 있습니다.*
