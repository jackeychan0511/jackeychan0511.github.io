---
layout: post
title: "Anthropic Claude 최신 소식 — 화이트보드·시트·미팅 베타 공개, 클로드가 '오피스 스위트'가 됐다 (2026.11.11)"
date: 2026-11-11 16:52:00 +0900
categories: [career]
tags: [Anthropic, Claude, 앤트로픽, 클로드, ClaudeWhiteboards, ClaudeSheets, ClaudeMeetings, ClaudeData, 클로드화이트보드, 클로드시트, 클로드미팅, 인터랙티브커넥터, MCPApps, 오피스스위트, GoogleMeet, 업무자동화, AI에이전트, 2026년11월]
author: "40대 블로거"
image: https://pub-a8e00ddf355f47339bd2e01ff3221de7.r2.dev/assets/images/posts/anthropic-claude-office-suite-20261111/claude-office-suite-official-og.png
description: "2026년 11월 10일, Anthropic이 Claude Whiteboards·Claude Sheets·Claude Meetings를 베타로 공개하고 Claude Dashboards를 Claude Data로 정식 출시했습니다. 화이트보드는 브레인스토밍을 타임라인과 액션아이템으로 바꾸고, 시트는 프롬프트 한 줄로 수식·탭·드롭다운이 있는 표를 만들며, 데이터는 인증 쿼리와 라이브 공유를 붙였습니다. 요금제별 제공 범위, 관리자 기본값(Enterprise는 11월 19일부터 Data 자동 활성), 인터랙티브 커넥터·MCP Apps 변화, 사용자·개발자 영향과 한계까지 정리했습니다."
---

![Claude Whiteboards·Sheets·Data 공식 발표 대표 이미지](https://pub-a8e00ddf355f47339bd2e01ff3221de7.r2.dev/assets/images/posts/anthropic-claude-office-suite-20261111/claude-office-suite-official-og.png)
*(이미지 출처: Anthropic(앤트로픽) 공식 사이트 claude.com — "Introducing Claude Whiteboards, Claude Sheets, and Claude Data", 2026-11-10)*

> **📌 한 줄 요약:** **2026년 11월 10일**, **Anthropic(앤트로픽)** 이 **Claude Whiteboards**·**Claude Sheets**·**Claude Meetings**를 **베타**로 공개하고, 10월에 베타로 나왔던 **Claude Dashboards를 Claude Data**라는 이름으로 **정식 출시(GA)** 했습니다. 화이트보드는 말로 흩뿌린 계획을 **담당자·날짜가 붙은 타임라인**으로 바꾸고, 시트는 **프롬프트 한 줄로 탭·수식·서식·드롭다운이 있는 표**를 만들며, 데이터는 **인증 쿼리·라이브 공유·캐싱·변경 이력**을 붙였습니다. 같은 날 **인터랙티브 커넥터**가 대화 옆에 **앱 전체 화면으로 열리는** 형태로 확장됐습니다. 요약하면 **"Claude가 답변을 주는 도구에서, 팀이 결과물을 계속 고치는 작업 공간으로"** 넘어간 발표입니다.

팀 단위로 AI를 굴리는 쪽에서 늘 걸리는 지점은 **"결과물이 Claude 밖으로 나가는 순간 맥락이 끊긴다"** 는 것입니다. 회의록은 노션으로, 표는 엑셀로, 아이디어 정리는 미로로 옮기는 사이에 Claude는 더 이상 그 파일의 변화를 따라가지 못합니다. 이번 발표는 그 단절을 **제품 쪽에서** 메우겠다는 시도입니다. 성능 지표가 아니라 **"일이 어디서 머무는가"** 를 바꾸는 뉴스라서, 도구 조합을 짜는 입장에서는 모델 발표보다 실무 영향이 큽니다.

이번 글은 **① 이슈 요약 → ② 상세 분석(제품별) → ③ 관리자 기본값 → ④ 영향(사용자/개발자) → ⑤ 유의점 → ⑥ 전망** 순으로 정리합니다.

---

## 1. 이슈 요약 — 무엇이 나왔나

Anthropic은 **2026년 11월 10일(현지시간)**, 샌프란시스코에서 연 행사에서 **비개발자 지식 노동자**를 겨냥한 제품군을 공개했습니다. The Verge는 이 발표를 **"Anthropic이 이제 자체 AI 오피스 스위트를 갖게 됐다"** 로 정리했습니다.

| 제품 | 상태 | 제공 범위 | 한 줄 설명 |
|:---|:---|:---|:---|
| **Claude Whiteboards** | 베타 | 모든 유료 요금제 | 팀이 Claude와 함께 브레인스토밍하는 공유 보드 |
| **Claude Sheets** | 베타 | 모든 유료 요금제 | Claude가 함께 만들고 고치는 스프레드시트 |
| **Claude Meetings** | 베타 | **Team·Enterprise** | 회의에 참여해 기록하고 문서를 갱신 |
| **Claude Data** | **정식(GA)** | 모든 유료 요금제 | 대시보드의 개명·확장판. 인증 쿼리·데이터 스토리·라이브 공유 |
| **인터랙티브 커넥터** | 확장 | 모든 요금제(웹·데스크톱) | 파트너 앱을 대화 옆에 **앱 화면으로** 띄움 |

세 가지 신규 제품은 **웹·데스크톱·모바일 앱**에서 모두 동작합니다. Whiteboards·Sheets·Data는 **모든 유료 요금제**에 열렸고, **Meetings만 Team·Enterprise**로 범위가 좁습니다. 이 차이가 실무에서는 꽤 중요합니다. 개인 유료 사용자는 화이트보드와 시트를 바로 쓸 수 있지만, **회의 에이전트는 팀 요금제부터**입니다.

Anthropic 제품 담당 니콜라스 린(Nicholas Lin)은 사전 브리핑에서 **"Claude Code가 1년 전 엔지니어의 업무 방식을 바꿨다"** 며, 이번에는 **비개발자가 남은 격차**라는 뉘앙스로 설명했습니다. "비개발자의 AI 도입에는 여전히 마찰이 크다"는 표현이 그대로 인용됐습니다.

---

## 2. 상세 분석 — 제품별로 무엇이 달라졌나

### 2-1. Claude Whiteboards — "말로 한 계획이 보드가 된다"

기존에는 Claude와 아이디어를 정리한 뒤 **그 결과를 복사해서 다른 협업 도구로 옮겨야** 했습니다. 화이트보드는 그 이동을 없앱니다.

- **보드 자동 구성:** 계획을 말로 풀어놓으면 Claude가 **타임라인 + 담당자 + 날짜**가 붙은 보드로 정리
- **회고(Retro) 활용:** 팀원이 각자 메모를 붙이면 Claude가 **주제별로 묶고, 사람별 액션아이템 초안**을 작성
- **영역 지정 질문:** 보드의 특정 구역을 **드래그로 박스 지정**하면 그 부분만 놓고 질문 가능
- **산출물 전환:** 보드를 **문서나 덱으로 변환**하거나, **Lucid·Miro·Vani by Zoho**로 넘겨 이어서 작업

"AI가 회의실 화이트보드를 대신한다"기보다는, **휘발되던 발언을 구조가 있는 보드로 고정**하는 쪽에 가깝습니다. 보드가 Claude 안에 남아 있으니 다음 날 이어서 물어볼 수 있다는 점이 실질적인 차이입니다.

### 2-2. Claude Sheets — "캠페인 트래커 하나 만들어줘"로 끝나는 표

시트는 **탭·수식·서식·드롭다운**을 갖춘 스프레드시트를 **프롬프트 한 줄**로 생성합니다. 예를 들어 "캠페인 트래커"나 "채용 예산표"를 요청하면 바로 편집 가능한 표가 나옵니다.

- **셀 단위 편집:** Claude가 셀 안에서 수정하고, **바꾼 내용을 하나씩 표시**해 추적 가능
- **공유·동시 작업:** 팀과 시트를 공유하고 Claude와 같이 편집
- **외부 반출:** **.xlsx / CSV 내보내기**, 또는 **Claude for Excel**로 이동
- **파트너 도구 연계:** **Airtable·Asana·monday.com·Smartsheet**에서 계속 작업

여기서 Anthropic이 직접 밝힌 한계가 있습니다. 현재 시트는 **"간단한 스프레드시트 기능"** 수준이고, 확장은 개발 중이라는 설명입니다. 엑셀의 피벗·매크로·외부 데이터 연결 같은 기능이 필요한 업무라면 **시트에서 시작해 xlsx로 넘기는 흐름**이 현실적입니다.

![Claude 오피스 스위트 제품 화면 — Whiteboards·Sheets·Data](https://pub-a8e00ddf355f47339bd2e01ff3221de7.r2.dev/assets/images/posts/anthropic-claude-office-suite-20261111/claude-office-suite-artifacts.png)
*(이미지 출처: The Verge 보도 «Anthropic has its own AI-powered office suite now»(2026-11-10)에 실린 Anthropic 제공 화면)*

### 2-3. Claude Meetings — 회의에 들어와서 "일을 시작하는" 에이전트

Meetings는 **Google Meet 통화에 Claude를 넣는** 방식입니다. 회의에 참여해 **발언을 기록하고, 회의 중에 팀이 작업 중인 문서를 갱신**합니다. TechRadar는 이 기능을 두고 **"주간 회의에 들어와 메모를 하고, 심지어 일을 시작한다"** 고 표현했습니다.

- **상태:** 베타
- **요금제:** **Team·Enterprise**
- **플랫폼:** 출시 시점에는 **Google Meet만** 지원, 이후 확대 예정
- **역할:** 회의록 정리 → 액션아이템 도출 → 관련 문서 업데이트

Otter.ai류 회의 기록 도구와 겹치는 영역이지만, 차이는 **기록한 내용을 곧바로 팀 문서에 반영**하는 쪽으로 붙어 있다는 점입니다. 다만 **Google Meet 우선 지원**이라, Teams나 Zoom 중심 조직은 당장 도입 대상이 아닙니다. 이건 꽤 큰 제약입니다.

### 2-4. Claude Data — 대시보드가 이름을 바꾸고 '데이터 제품'이 됐다

10월에 베타로 나온 **Claude Dashboards**가 **Claude Data**로 개명하며 **정식 출시**했습니다. 이름을 바꾼 이유를 Anthropic은 **"기업이 데이터를 쓰는 방식이 대시보드보다 넓어서"** 라고 설명합니다.

새로 붙은 기능은 네 가지입니다.

| 기능 | 내용 |
|:---|:---|
| **인증 쿼리(Certified queries)** | 데이터팀이 표준 쿼리를 인증. 해당 지표로 만든 차트에 **'certified' 배지** 표시 |
| **데이터 스토리** | 지표를 바탕으로 **스토리·나레이션 영상 초안** 생성(공유 전 검토) |
| **라이브 공유** | 커넥터 권한이 없는 동료도 **최신 숫자**를 열람. 단, **승인된 쿼리 결과만** 보이고 다른 쿼리는 실행 불가 |
| **디자인 시스템·캐싱·변경 이력** | 사내 디자인 시스템 적용, **대시보드별 갱신 주기** 설정, **누가 무엇을 고쳤는지 추적 + 버전 롤백** |

특히 **라이브 공유**의 권한 설계가 눈에 띕니다. "대시보드를 보여주려고 데이터 웨어하우스 권한까지 넘기는" 기존 관행을 끊고, **승인된 쿼리의 결과만** 보여주는 구조입니다. 보안 검토를 통과해야 하는 조직에서 이 부분은 도입 여부를 가르는 조건이 됩니다.

연결 대상은 **Amazon Redshift·BigQuery·ClickHouse** 같은 웨어하우스이고, 파트너로 **Amplitude·Mixpanel·Omni·PostHog·Sigma**가 이름을 올렸습니다. 데이터 커넥터가 **필수**라서, 커넥터 없이는 이 제품 자체를 쓸 수 없습니다.

### 2-5. 인터랙티브 커넥터 — 앱이 대화 '옆'에 전체 화면으로 열린다

같은 날 별도로, **인터랙티브 커넥터**가 대화 옆 또는 **전체 화면 앱**으로 열리도록 확장됐습니다. 지금까지는 연결된 도구를 호출해 결과를 받는 수준이었다면, 이제는 **Claude 안에서 그 앱의 화면을 직접 조작**할 수 있습니다.

- **대상 앱:** Adobe, Atlassian, Box, Notion, HubSpot 등
- **동작 예시:** **Confluence** 문서 편집, **Box** 파일의 나란히 비교(side-by-side) 수정, **Notion** 문서·DB 생성, **Adobe PDF 편집기**, **HubSpot** 캠페인 트래커
- **기반 기술:** **MCP Apps** — MCP 서버가 자체 UI를 AI 앱 안에 공급하는 확장
- **생태계 규모:** Anthropic 마켓플레이스의 플러그인 옵션은 **9,000개 이상**으로 언급

MCP를 처음 공개한 지 약 2년 만에, Anthropic은 **"연결"에서 "내장"으로** 무게를 옮겼습니다. 개발자용 **앱 도구·임베디드 사이트** 기능은 **퍼블릭 프리뷰** 단계로, 사용자 대상 롤아웃과 상태가 다릅니다.

---

## 3. 관리자 관점 — 요금제마다 기본값이 다르다

이번 발표에서 실무자가 가장 먼저 확인해야 할 부분은 **관리자 기본값**입니다. 제품이 켜져 있는지가 요금제별로 다릅니다.

| 구분 | Team | Enterprise |
|:---|:---|:---|
| Whiteboards | 기본 **켜짐** | 기본 **꺼짐** |
| Sheets | 기본 **켜짐** | 기본 **꺼짐** |
| Data | 기본 켜짐 | **2026년 11월 19일부터 기본 켜짐** |

설정 위치는 **조직 설정의 Artifacts 섹션**입니다. Enterprise에서 Whiteboards·Sheets가 기본 꺼짐인 이유는 명확합니다. **공유 보드와 시트에 사내 정보가 쌓이기 시작하면** 보존·감사·외부 공유 정책을 먼저 정해야 하기 때문입니다. 반면 Data는 **11월 19일 자동 활성**이 예고돼 있어, 데이터 커넥터를 이미 쓰는 조직이라면 **그 전에 권한·인증 쿼리 정책을 점검**하는 편이 안전합니다.

---

## 4. 영향 — 사용자와 개발자에게 무엇이 달라지나

### 4-1. 사용자(기획·운영·마케팅)

- **도구 왕복이 줄어듭니다.** 회의 → 보드 → 표 → 대시보드가 Claude 안에서 이어지므로, 복사·붙여넣기로 맥락을 잃는 구간이 짧아집니다.
- **"표 만들어줘"의 기준이 바뀝니다.** 빈 시트에 서식을 잡는 시간 대신, **무엇을 측정할지 정의하는 일**에 시간이 쏠립니다.
- **요금제 확인이 먼저입니다.** Meetings는 Team·Enterprise 전용, Google Meet만 지원입니다. 개인 Pro/Max로는 회의 기능을 쓸 수 없습니다.

### 4-2. 개발자·자동화 운영자

- **API 가격·모델 라인업 변화는 없습니다.** 이번 발표는 **제품 표면(surface)** 의 변화이고, Fable 5.2·Mythos 5.2 같은 모델 변경과는 별개 트랙입니다. 파이프라인 단가를 다시 계산할 이유는 없습니다.
- **MCP Apps가 새 접점입니다.** 서버가 **자체 UI를 공급**할 수 있게 되면서, 사내 도구를 "API 호출 대상"이 아니라 **Claude 안에 뜨는 작업 화면**으로 붙일 수 있습니다. 앱 도구·임베디드 사이트는 퍼블릭 프리뷰이므로, 사내 자동화에 붙이려면 프리뷰 범위와 권한 모델을 먼저 확인해야 합니다.
- **커넥터 권한 설계가 자동화의 병목이 됩니다.** 에이전트가 여러 시스템을 넘나들수록 **누구의 권한으로 무엇까지 실행하는지**가 핵심 쟁점이 됩니다. Data의 "승인된 쿼리만 노출" 방식은 사내 에이전트 설계에도 참고할 만한 패턴입니다.

업무 자동화 파이프라인을 운영하는 입장에서 이번 발표의 의미는 **"자동화의 끝단이 사람이 쓰는 화면과 만난다"** 는 쪽입니다. 지금까지는 스크립트가 만든 결과를 사람이 다른 앱에서 열어 고쳤다면, 이제는 **Claude 안의 보드·시트·대시보드에서 바로 이어서** 고칠 수 있게 됩니다. 사람이 개입하는 지점이 문서 밖에서 문서 안으로 옮겨오는 셈입니다.

---

## 5. 유의점 — 지금 도입을 검토한다면

- **대부분이 베타입니다.** Whiteboards·Sheets·Meetings 모두 베타이고, Data의 **인증 쿼리·라이브 공유도 베타**입니다. 공유·권한 제어가 완성되지 않은 상태에서 민감 데이터를 올리는 건 권하지 않습니다.
- **시트는 "엑셀 대체"가 아닙니다.** Anthropic 스스로 "간단한 기능"이라고 밝혔습니다. 재무 모델링처럼 함수·연결이 복잡한 작업은 **xlsx 내보내기 + Claude for Excel** 조합이 현실적입니다.
- **회의 기능은 생태계에 종속됩니다.** Google Meet 우선 지원이라 Teams·Zoom 중심 조직은 대기해야 합니다.
- **결과물이 Claude에 남는다는 건 곧 락인 위험입니다.** 보드·시트·대시보드가 Claude 안에 쌓이면, 요금제 변경이나 정책 변경 시 **이관 경로**가 중요해집니다. 지금은 Lucid·Miro·Airtable·Asana·Smartsheet 등 **외부로 내보내는 경로가 열려 있다**는 점을 확인해 두는 게 좋습니다.
- **조직 정책을 먼저 정하세요.** Enterprise 기본값이 꺼져 있다는 건 Anthropic도 **"관리자가 판단하라"** 는 뜻입니다. 보존 기간, 외부 공유, 감사 로그 정책 없이 켜면 나중에 정리 비용이 큽니다.

---

## 6. 전망 — IPO를 앞둔 '화이트칼라 공략'

이번 발표의 배경에는 **상장**이 있습니다. Anthropic은 **2026년 11월 10일**, IPO 공모가 희망 범위를 **주당 70~78달러**로 제시했고, 상단 기준 **약 1조 4,000억 달러(풀 희석 기준)** 밸류에이션을 목표로 한다고 알려졌습니다. 중간값 기준 조달 규모는 **약 610억 달러** 수준으로 보도됐습니다. The Verge는 이번 제품 발표를 **"IPO를 앞두고 화이트칼라를 공략하는 행보"** 로 해석했습니다.

흐름을 정리하면 이렇습니다.

1. **개발자 시장은 이미 선점** — Claude Code가 1년 사이 엔지니어링 영역의 표준급 도구로 자리 잡았습니다.
2. **남은 시장은 비개발자** — 회의·브레인스토밍·지표 분석으로 대표되는 지식 노동입니다.
3. **경쟁 구도는 구글·MS와 정면** — Gemini의 회의 노트, Google Sheets, Workspace 커넥터, ChatGPT의 통합 앱 전략과 겹칩니다.

관건은 **깊이**입니다. 오피스 스위트는 "기능이 있다"보다 **"매일 열게 되는가"** 로 승부가 납니다. 지금 Claude Sheets는 엑셀을 대체하지 못하고, Meetings는 Google Meet에 묶여 있습니다. 반대로 **보드 → 표 → 대시보드 → 문서**가 한 맥락으로 이어지는 경험은 구글·MS 조합에서도 아직 매끄럽지 않습니다. 이 연결성이 유지된다면, **"회의에서 나온 결정이 그날 안에 지표와 문서로 이어지는"** 워크플로를 원하는 팀에게는 실질적인 선택지가 됩니다.

도입을 검토한다면 순서를 이렇게 잡는 게 좋습니다. **① 팀 요금제 여부 확인 → ② 관리자 설정에서 Whiteboards·Sheets 기본값 점검 → ③ 데이터 커넥터·인증 쿼리 정책 정리 → ④ 베타 기능은 파일럿 팀에만 개방.** 회의 에이전트는 Google Meet을 쓰는 팀에서만 효과가 나므로, 협업 도구 스택을 먼저 확인하는 것이 순서입니다.

---

### 참고 자료

- Anthropic 공식 — [Introducing Claude Whiteboards, Claude Sheets, and Claude Data](https://claude.com/resources/articles/sheets-whiteboards-data) (2026-11-10)
- The Verge — [Anthropic has its own AI-powered office suite now](https://www.theverge.com/ai-artificial-intelligence/1030709/anthropic-has-its-own-ai-powered-office-suite-now) (2026-11-10)
- TechRadar — [Claude can now join your weekly meeting to take notes…](https://www.techradar.com/pro/claude-can-now-join-your-weekly-meeting-to-take-notes-and-even-start-work-for-you-and-thats-not-all-with-new-spreadsheet-whiteboard-tools-and-more-helping-conquer-the-world-of-frontier-work) (2026-11-10)
- Unite.AI — [Anthropic Launches Claude Whiteboards and Sheets in Beta](https://www.unite.ai/anthropic-launches-claude-whiteboards-and-sheets-in-beta/) (2026-11-10)
- CNN Business — [Anthropic to seek $1.4 trillion valuation in blockbuster IPO](https://edition.cnn.com/2026/11/10/investing/weve-never-seen-anything-like-anthropic-ipo) (2026-11-10)
