---
layout: post
title: "오픈클로(OpenClaw) 2026.11.9 보안 하드닝 정리 — exec 가드 우회 봉합과 11월 29일 설정 만료 절벽"
date: 2026-11-25 09:50:00 +0900
categories: [career]
tags: [OpenClaw, 오픈클로, 2026119, 보안패치, 하드닝, exec가드, 디스코드DM, 리다이렉트, doctor, 설정만료, deprecation, 크론잡, MCP타임아웃, TTS설정, 게이트웨이바인드, 플러그인SDK, 업무자동화, AI에이전트, 오픈소스AI, 2026년11월]
author: "40대 블로거"
image: https://pub-a8e00ddf355f47339bd2e01ff3221de7.r2.dev/assets/images/posts/openclaw-2026119-security-boundaries-deprecation-20261125/00-hero.jpg
card_image: https://pub-a8e00ddf355f47339bd2e01ff3221de7.r2.dev/assets/images/posts/openclaw-2026119-security-boundaries-deprecation-20261125/00-card.jpg
description: "2026년 11월 25일 09시 47분(한국시간) OpenClaw 2026.11.9가 나왔습니다. PR 15건짜리 짧은 유지보수 릴리스지만 내용은 가볍지 않습니다. env -S 셸 시작 할당이 exec 가드에 보이지 않던 구멍, 디스코드 DM 정책을 우회한 세션 바인딩 재사용, 셀프호스팅 웹 도구 리다이렉트가 설정 호스트 밖으로 새던 문제를 한 번에 막았습니다. 여기에 11월 29일을 기한으로 잡힌 doctor 설정 만료 40여 건(크론 웹훅 폴백·dmPolicy·MCP 타임아웃·게이트웨이 bind 모드 등)을 함께 정리했습니다."
---

![OpenClaw GitHub v2026.11.9 릴리스 페이지](https://pub-a8e00ddf355f47339bd2e01ff3221de7.r2.dev/assets/images/posts/openclaw-2026119-security-boundaries-deprecation-20261125/00-hero.jpg)
*OpenClaw 공식 GitHub 릴리스 페이지 v2026.11.9 — 2026년 11월 25일 09시 47분(한국시간) 공개 (출처: github.com/openclaw/openclaw/releases, 2026.11.25 캡처)*

> **📌 한줄 요약:** 2026년 11월 25일 09시 47분(한국시간), OpenClaw **2026.11.9**가 올라왔습니다. PR 15건짜리 짧은 유지보수 릴리스라 규모만 보면 지나칠 수 있는데, 내용은 그렇지 않습니다. ① **exec 가드를 지나가던 `env -S` 셸 시작 할당**, ② **디스코드 DM 정책을 무시하고 재사용되던 세션 바인딩**, ③ **설정한 호스트 밖으로 새던 셀프호스팅 웹 도구 리다이렉트** — 세 가지 경계를 한꺼번에 조였습니다. 그리고 같은 릴리스에 **2026년 11월 29일을 가장 이른 제거 시점으로 잡은 doctor 설정 만료 항목 40여 건**이 함께 고지됐습니다. 에이전트에게 정기 작업을 맡겨 둔 구성이라면, 이번 주에 `openclaw doctor`를 한 번 돌려야 할 이유가 여기 있습니다.

---

## 설정 만료일은 알림이 안 옵니다

자동화를 오래 굴리면 장애보다 먼저 오는 게 있습니다. **설정이 조용히 구식이 되는 것**입니다. 잘 돌아가던 작업이 어느 날 갑자기 다른 대화로 결과를 보내거나, 아예 발송을 건너뛰기 시작합니다. 로그를 열어 보면 에러는 없습니다. 그저 "설정 키가 더 이상 읽히지 않는다"는 사실만 남습니다.

OpenClaw는 이런 류의 변경을 `doctor` 레지스트리에 **만료 예정일**로 등록해 두는 구조를 쓰고 있습니다. 문제는 이 날짜가 **알림으로 오지 않는다**는 점입니다. 릴리스 노트의 deprecations 항목을 직접 읽거나, `openclaw doctor`를 돌려서 확인해야만 보입니다. 이번 2026.11.9 릴리스는 그 날짜가 **나흘 앞으로 다가온 상태**로 나왔습니다.

그래서 이번 글은 두 갈래로 정리하겠습니다. **이미 뚫려 있던 경계를 막은 보안 수정 3건**, 그리고 **11월 29일에 몰려 있는 설정 만료 목록**입니다. 나머지 수정은 뒤에서 짧게 훑겠습니다.

---

## 핵심 1 — exec 가드가 보지 못하던 `env -S` 할당

첫 번째 항목이 이번 릴리스에서 가장 무게가 있습니다. 릴리스 노트 표현은 이렇습니다.

> "Keep `env -S` shell-startup assignments visible to exec guards" — `env -S` 셸 시작 할당을 exec 가드에 계속 보이게 유지 (#203934, 기여 @pgondhi987)

`env -S`는 문자열을 쪼개서 여러 인자로 실행하는 옵션입니다. 예를 들어 `env -S "FOO=bar command --flag"` 같은 형태죠. 편리한 대신, **실행 정책(exec guard)이 검사하는 문자열과 실제로 실행되는 명령 사이에 한 겹이 생깁니다.** 가드는 앞부분만 보고 "이건 환경변수 설정이네" 하고 통과시킬 수 있는데, 실제로는 뒤에 붙은 명령이 그대로 실행됩니다.

에이전트 운영에서 이 구조가 위험한 이유는 명확합니다. **승인 목록(allowlist)이나 금지 키 차단(blocked-key)** 을 우회하는 통로가 됩니다. 사람이 승인 prompt를 보고 "환경변수 하나 설정하는구나"라고 판단했는데, 실제 실행은 다른 것이 되는 식입니다.

이번 수정은 그 할당을 **가드가 볼 수 있는 위치로 끌어올린 것**입니다. 단순 버그픽스처럼 보이지만, 성격상 **보안 하드닝**에 가깝습니다. 참고로 이번 릴리스 노트에는 CVE 번호나 GHSA 공개가 함께 붙지 않았습니다. 즉 **"이미 공격받은 사고"가 아니라 "우회 가능성을 미리 닫은 패치"** 로 읽는 게 정확합니다. 11월 21일에 따로 공개됐던 위임 승인 우회(GHSA) 건과는 별개의 작업입니다.

## 핵심 2 — 디스코드 DM 정책이 세션 재사용에서 무시되던 문제

두 번째도 같은 결의 수정입니다.

> "apply current Discord DM pairing and allowlist policy before using a bound session" — 바인딩된 세션을 쓰기 전에 **현재의 DM 페어링·허용목록 정책**을 적용 (#196848, @drobison00)

여기서 핵심 단어는 **"current(현재)"** 와 **"bound session(바인딩된 세션)"** 입니다. OpenClaw는 같은 상대와 대화가 이어지면 세션을 **바인딩**해 두고 재사용합니다. 문제는, 정책을 바꾼 뒤에도 **이미 묶여 있던 세션이 그대로 살아 있으면** 새 정책이 적용되지 않는 경우가 있었다는 것입니다.

시나리오를 만들어 보면 이렇습니다. 특정 사용자를 허용목록에서 빼거나 DM 정책을 `pairing`에서 더 엄격한 쪽으로 바꿉니다. 그런데 그 사용자와의 세션이 이미 바인딩돼 있으면, **정책 변경 이전의 관계가 그대로 유지**됩니다. 접근을 막았다고 생각했는데 막히지 않은 상태가 되는 것이죠.

관리자 입장에서 이건 꽤 불편한 종류의 구멍입니다. **권한을 회수했는데 회수가 반영되지 않는 문제**는 눈으로 확인하기 어렵고, 확인하려면 세션을 직접 들여다봐야 합니다. 이번 수정으로 세션 재사용 시점마다 현재 정책을 다시 평가하게 됐습니다.

## 핵심 3 — 웹 도구 리다이렉트가 설정 호스트를 벗어나던 문제

세 번째는 셀프호스팅 구성에서 의미가 있습니다.

> "confine self-hosted web endpoint redirects to the configured host" — 셀프호스팅 웹 엔드포인트의 리다이렉트를 **설정된 호스트로 제한** (#196847, @drobison00)

웹 도구(web fetch 계열)를 셀프호스팅 엔드포인트로 돌리는 구성이 있습니다. 이때 응답이 302로 다른 호스트를 가리키면, 그대로 따라가서 **원래 의도하지 않았던 곳까지 요청이 나가는** 구조였습니다.

이게 왜 문제인가 하면, 에이전트는 보통 "허용된 도메인만 조회한다"는 전제로 운영됩니다. 리다이렉트 한 번이면 그 전제가 깨집니다. 내부망 주소나 사설 엔드포인트를 허용해 둔 구성이라면, 리다이렉트를 통해 **허용 범위 밖으로 요청이 튀는 경로**가 생깁니다. 이번 수정은 그 리다이렉트를 **설정된 호스트 안으로 가둔 것**입니다.

세 항목을 나란히 놓고 보면 이번 릴리스의 성격이 분명해집니다. **명령 실행 경계, 채널 접근 경계, 네트워크 경계** — 자동화 에이전트에서 사고가 나는 세 지점을 동시에 손본 릴리스입니다. 화려한 기능은 하나도 없습니다.

---

## 11월 29일 — doctor 설정 만료가 한꺼번에 몰려 있습니다

이제 이번 릴리스에서 실무적으로 더 급한 부분입니다. 릴리스 노트 하단의 **Upcoming deprecations** 섹션에 항목이 **40건 가까이** 실려 있고, 거의 전부가 같은 날짜를 달고 있습니다.

> "reach their earliest removal date within seven days of this release preparation (November 24, 2026)" — 이 릴리스 준비 시점(2026년 11월 24일) 기준 **7일 이내에 가장 이른 제거 시점에 도달**하는 항목들

즉 **2026년 11월 29일**이 그 날짜입니다(플러그인 SDK 한 건만 11월 30일). 다만 표현을 정확히 해 두겠습니다. 릴리스 노트는 이 항목들이 **"이번 릴리스에는 여전히 존재하며, 제거에는 문서화된 업그레이드 조건과 메인테이너 리뷰가 필요하다"** 고 명시합니다. **11월 29일에 설정이 자동으로 삭제된다는 뜻은 아닙니다.** 하지만 **"이 날짜 이후에는 언제 사라져도 이상하지 않은 상태"** 가 된다는 뜻이고, 그때부터는 업데이트 한 번에 조용히 깨질 수 있습니다.

### 자동화 운영자에게 직접 걸리는 항목

전체 40건을 다 옮기면 읽기 어려우니, **크론·채널·모델·플러그인** 네 갈래로만 추렸습니다.

| 영역 | 만료 항목 | 대체 경로 (마이그레이션 문서) |
|:--|:--|:--|
| **크론 발송** | `doctor-cron-webhook-fallback` — 작업별 `delivery.to` / `delivery.completionDestination` | `docs.openclaw.ai/automation/cron-jobs` |
| **채널 DM** | `doctor-phase2-channel-dm-aliases`, `doctor-channel-dm-aliases` — 같은 레벨의 `dmPolicy`·`allowFrom` | `docs.openclaw.ai/channels/channel-routing` |
| **세션 정리** | `doctor-session-prune-reset-aliases` — `session.maintenance.pruneAfter`, `session.resetByType.direct` | `docs.openclaw.ai/gateway/configuration-reference` |
| **MCP 타임아웃** | `doctor-mcp-timeout-aliases` — `connectionTimeoutMs`, `requestTimeoutMs` | `docs.openclaw.ai/cli/mcp` |
| **게이트웨이 바인드** | `doctor-gateway-bind-host-aliases` — `gateway.bind.mode`의 `lan`·`loopback`·`tailnet`·`auto` 등 | `docs.openclaw.ai/gateway/configuration` |
| **모델 기본값** | `doctor-root-default-model` — `agents.defaults.model` | `docs.openclaw.ai/gateway/doctor` |
| **컨텍스트 상한** | `doctor-context-budget-one-knob` — `contextTokens`·`contextWindow` 메타데이터 | `docs.openclaw.ai/concepts/model-providers` |
| **TTS** | `doctor-tts-top-level-owner`, `doctor-tts-provider-aliases`(`microsoft`→`edge`), `doctor-tts-speaker-selection-fields` | `docs.openclaw.ai/tools/tts` |
| **플러그인 SDK** | `plugin-sdk-session-agent-resolution-aliases` — `resolveSessionAgentId(s)Strict` (11/29) / `agent-harness-credential-prompt-string-argument` (11/30) | `docs.openclaw.ai/plugins/compatibility` |

정기 작업을 돌리는 구성이라면 **첫 줄이 제일 급합니다.** 크론 작업의 발송 대상이 작업별 `delivery.to` 방식에서 벗어나야 한다는 항목인데, 이게 깨지면 **작업은 성공하는데 결과가 아무 데도 도착하지 않는** 형태로 나타납니다. 실패 알림조차 안 옵니다. 발송 대상이 없으니까요.

![OpenClaw 공식 문서 doctor(진단) 페이지](https://pub-a8e00ddf355f47339bd2e01ff3221de7.r2.dev/assets/images/posts/openclaw-2026119-security-boundaries-deprecation-20261125/01-doctor.jpg)
*OpenClaw 공식 문서의 doctor(진단) 페이지 — 설정 마이그레이션 항목이 이 레지스트리를 통해 관리됩니다 (출처: docs.openclaw.ai/cli/doctor, 2026.11.25 캡처)*

### 그래서 뭘 하면 되나

복잡하지 않습니다. **`openclaw doctor`를 돌리고, 만료 예정 목록에 뜨는 항목만 마이그레이션 문서를 보고 옮기면 됩니다.**

```bash
# 1) 만료 예정 항목 확인 — 11/29, 11/30 날짜가 뜨면 해당
openclaw doctor

# 2) 설정에서 구식 키가 남아 있는지 직접 확인
grep -rn "dmPolicy\|allowFrom\|connectionTimeoutMs\|requestTimeoutMs\|bind.mode" ~/.openclaw/

# 3) 크론 작업 발송 대상 확인
openclaw cron list
```

주의할 점 하나. **40건을 한 번에 다 고치려고 들면 대개 실패합니다.** 채널별 DM 정책은 계정 단위와 채널 단위가 겹쳐 있어서, 한쪽만 바꾸면 검증에서 다시 걸립니다. **크론 발송 → 채널 DM → 나머지** 순서로, 한 묶음씩 바꾸고 그때마다 `doctor`를 다시 돌리는 편이 안전합니다.

---

## 나머지 수정 — 짧게 훑기

이번 릴리스의 Fixes 항목 중 체감이 갈릴 만한 것들입니다.

- **Claude CLI 백그라운드 실행 복구** (#202646) — 비동기 stdin prompt에서 **시크릿 입력 파이프가 끊겨** 백그라운드 실행이 "secret descriptor 없음"으로 실패하던 문제. 시크릿을 쓰는 백그라운드 작업이 있다면 이번에 살아납니다.
- **ACP 응답 완결성** (#202658) — ACP 클라이언트에서 prompt가 다른 작업 뒤에 대기할 때 **답변이 잘려서** 도착하던 문제.
- **엄격한 provider에서 Tool Search** (#202436, 관련 #202415) — assistant prefill을 거부하는 chat-completions provider에서 `web_search`가 Tool Search 경유로 계속 동작.
- **세션 포크 비용 절감** (#197226, #197224) — OpenAI 포크는 부모의 **prompt 캐시를 재사용**하고, Anthropic 포크는 자체 suffix에 캐시 체크포인트를 쓰지 않습니다. 포크를 자주 만드는 구성이라면 비용 차이가 납니다.
- **재로그인 시 모델 설정 보존** (#201742, 관련 #201558) — provider에 다시 로그인할 때 **thinking 레벨 같은 명시 설정이 초기화되던** 문제.
- **번들 Codex 0.172.0** — Codex app-server와 프로토콜을 함께 올렸습니다.
- **헤디드 크롬 백그라운드 탭 스크린샷** (#202878, 관련 #165312) — 백그라운드 탭 캡처가 타임아웃 나면서 **이후 캡처까지 막던** 문제.

![OpenClaw 공식 홈페이지 대표 이미지](https://pub-a8e00ddf355f47339bd2e01ff3221de7.r2.dev/assets/images/posts/openclaw-2026119-security-boundaries-deprecation-20261125/02-official-og.jpg)
*OpenClaw 공식 홈페이지(openclaw.ai) 대표 이미지 (출처: openclaw.ai, 2026.11.25)*

---

## 장점과 단점, 솔직히

**장점**

1. **경계를 조인 방향이 정확합니다.** 명령 실행·채널 접근·네트워크 리다이렉트는 에이전트 자동화에서 실제 사고가 나는 세 지점입니다. 기능 추가 없이 이 세 곳만 손본 건 릴리스 설계로 봐도 합리적입니다.
2. **릴리스가 작습니다.** PR 15건, 이전 버전(2026.11.8은 314 커밋·210 PR)에 비하면 20분의 1 수준입니다. 회귀 리스크가 그만큼 낮아서 **운영 환경에 바로 올리기 쉬운 릴리스**입니다.
3. **비용·안정성 수정이 실용적입니다.** 세션 포크의 프롬프트 캐시 재사용, 백그라운드 시크릿 파이프, ACP 응답 완결성은 "써 보면 알겠다" 성격이 아니라 **지표로 확인되는** 종류입니다.
4. **만료 일정을 릴리스 노트에 명시했습니다.** 조용히 지나가도 되는 걸 나흘 전에 표로 정리해 준 건 운영자 입장에서 실제로 도움이 됩니다.

**단점**

1. **만료 항목이 40건입니다.** 하루 이틀에 정리할 물량이 아닙니다. 게다가 대부분이 **11월 29일 하루에 몰려 있어서**, 그 주에 다른 업데이트까지 겹치면 손이 두 배로 듭니다.
2. **보안 수정의 성격이 문서상 애매합니다.** CVE도 GHSA도 없는 "하드닝"이라, **얼마나 심각했는지 외부에서 판단할 근거가 없습니다.** 영향 범위를 가늠하려면 PR을 직접 읽어야 합니다.
3. **문서가 릴리스를 따라오지 못합니다.** 이 글을 쓰는 시점에 `docs.openclaw.ai/releases/2026.11.9`는 아직 404이고, 릴리스 인덱스도 2026.11.8까지만 올라와 있습니다. GitHub 릴리스 노트가 유일한 1차 자료입니다.
4. **확장 안정판(LTS) 사용자는 해당 없음.** 현재 확장 안정판 라인은 2026.10.33이라, 이번 보안 수정을 받으려면 정식 라인으로 넘어와야 합니다. LTS를 고른 이유가 "변경 최소화"라면 판단이 갈리는 지점입니다.

---

## 업데이트 전에 확인할 순서

1. **현재 라인 확인** — `openclaw --version`. 확장 안정판(2026.10.x)이면 이번 릴리스는 해당 없음.
2. **`openclaw doctor` 먼저** — 업데이트보다 진단이 먼저입니다. 만료 예정 목록에 11/29 항목이 뜨는지 확인.
3. **크론 작업 발송 대상 점검** — `openclaw cron list`로 작업별 `delivery` 설정 확인. 여기가 제일 위험합니다.
4. **채널 DM 정책 정리** — 계정 단위·채널 단위 `dmPolicy`·`allowFrom`이 겹쳐 있으면 한쪽으로 통일.
5. **exec 승인 규칙 재확인** — `env -S` 형태로 명령을 넘기는 스킬·플러그인이 있다면, 이번 패치 이후 가드에 걸려 **막힐 수 있습니다.** 막히는 게 정상 동작이고, 필요하면 승인 규칙에 명시적으로 추가해야 합니다.
6. **업데이트 후 백그라운드 작업 1회 수동 실행** — 시크릿을 쓰는 백그라운드 작업이 실제로 도는지 눈으로 확인.

배포는 npm 기준 `openclaw@2026.11.9`이고, 릴리스 SHA는 `0032b1d7`, 무결성 해시와 CI 검증 기록도 함께 공개돼 있습니다. 1차 자료는 `github.com/openclaw/openclaw/releases/tag/v2026.11.9`입니다.

---

## 정리 — 기능은 없지만, 이번 주에 봐야 할 릴리스

새 기능이 하나도 없는 릴리스입니다. 그런데 **에이전트에게 정기 작업을 맡겨 둔 구성**이라면 이번 주에 확인할 이유가 두 개 있습니다. 하나는 **exec 가드·채널 접근·리다이렉트 세 경계가 조여진 것**, 다른 하나는 **나흘 뒤로 다가온 설정 만료 40건**입니다.

특히 크론 작업을 여러 개 돌리는 구성이라면, 업데이트보다 **`openclaw doctor` 한 번**이 먼저입니다. 만료 예정 목록에 뜨는 항목이 없으면 그냥 넘어가면 되고, 뜬다면 그때 마이그레이션 문서를 펴면 됩니다. 자동화를 오래 굴릴수록 무서운 건 장애가 아니라 **조용히 바뀐 기본값**이라는 게, 이번 릴리스 노트를 읽고 남은 솔직한 감상입니다.

업무 자동화 파이프라인을 안정적으로 운영하려는 분들께는, 이번 버전이 "기능보다 안전" 쪽으로 정리된 릴리스라 오히려 반가운 소식일 겁니다.
