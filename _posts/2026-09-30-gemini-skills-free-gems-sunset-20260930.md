---
layout: post
title: "Google Gemini 최신 소식 — skills 전면 무료 개방, Gems 종료 로드맵 확정 (2026.9.30)"
date: 2026-09-30 15:54:00 +0900
categories: [career]
tags: [Google Gemini, 구글 제미나이, Gemini Skills, skills 무료 개방, Gems 종료, 젬 마이그레이션, 2026년 11월, 2027년 3월, 2027년 6월, SKILL.md, 프롬프트 자산, 커스텀 지시, 업무자동화, 자동화파이프라인, AI뉴스, 2026년 9월]
author: "40대 블로거"
image: /assets/images/posts/gemini-skills-free-gems-sunset-20260930/gemini-logo-wikimedia.png
description: "2026년 9월 29일, 구글이 공식 도움말 페이지를 통해 Gemini 'skills'를 만 18세 이상 개인 구글 계정 전체로 개방했습니다. Google AI Pro·Ultra 구독자 전용이던 skills가 무료로 풀리면서, 11월 개인 계정 Gems 종료를 앞두고 무료 사용자도 커스텀 지시 자산을 그대로 이어갈 수 있게 됐습니다. 종료 일정은 개인 계정 2026년 11월, Workspace 기업·비영리 2027년 3월, Workspace 교육 2027년 6월입니다. Opal과 Gems by Google Labs도 11월에 함께 사라집니다. 이슈 요약 → 상세 분석 → 영향(사용자·개발자) → 전망 순으로 정리했습니다. 이미지는 Google 공식 도움말 페이지 화면과 Wikimedia Commons의 Google Gemini 공식 로고입니다."
---

![Google Gemini 공식 로고 (출처: Wikimedia Commons — Google Gemini 공식 로고)](/assets/images/posts/gemini-skills-free-gems-sunset-20260930/gemini-logo-wikimedia.png)
*Gemini의 'skills'는 채팅창에서 `/`(곧 `@`)만 입력해 불러오는 재사용 지시문입니다. 2026년 9월 29일부터 만 18세 이상 개인 구글 계정이면 누구나 쓸 수 있게 됐습니다 (출처: Wikimedia Commons — Google Gemini 공식 로고)*

> **📌 한줄 요약:** 요즘 저처럼 **Gemini에 '사업자 세금계산서 정리' '영어 메일 다듬기' 같은 커스텀 지시를 만들어 두고 쓰시는 분들**, 지난주 앱 안내문 보고 마음 졸이셨을 겁니다. Gems가 11월에 사라지는데 대체 기능인 skills는 유료 전용이었으니까요. 그런데 **2026년 9월 29일, 구글이 skills를 전면 무료로 열었습니다.** 만 18세 이상 개인 계정이면 AI Pro·Ultra 없이도 씁니다. 솔직히 이번엔 "돈 벌려고 유료 전환 카드로 쓰겠지" 싶었는데, 생각보다 정반대로 갔습니다.

---

## 1. 이슈 요약 — 무엇이, 언제, 어떻게

먼저 확인된 팩트부터 정리하겠습니다.

| 항목 | 내용 |
|:---|:---|
| 확인일 | **2026년 9월 29일** (Google 공식 도움말 'About the transition from Gems to skills' 갱신, Android Authority·Neowin 등 보도) |
| 핵심 변경 | **skills 접근 자격에서 Google AI Pro·Ultra 요구 조건 삭제** — 개인 구글 계정 + 만 18세 이상이면 무료로 사용 |
| 진입 경로 | gemini.google.com → 사이드바 **Settings → Skills** (기존 Settings → Gems 자리) |
| 호출 방식 | 채팅창에 `/` + 스킬 이름 입력 (**곧 `@`로 변경 예정**), 여러 개 조합 가능 |
| 개수 제한 | 생성은 무제한, **동시 활성화 최대 100개** |
| 자동 이관 | Gems는 삭제 시점에 **자동으로 skills로 재생성** (수동 재생성 절차도 공개) |
| 종료 일정 | **2026년 11월** 개인 계정 → **2027년 3월** Workspace 기업·비영리 → **2027년 6월** Workspace 교육 |
| 함께 종료 | **Opal**(미니앱 실험)과 **Gems by Google Labs**도 11월에 종료 |
| 아직 미적용 | 업무·학교(Workspace) 계정은 추후 확대 예정, 파일 업로드 생성은 Mac 앱·웹만 지원 |

지난 9월 27일 앱에 뜬 배너는 "Gems가 11월 17일부터 skills로 이관된다"는 사실만 알려줬고, **무료 사용자가 어떻게 되는지는 침묵**했습니다. 그 물음에 구글이 나흘 만에 답을 낸 셈입니다. 답은 "구독하세요"가 아니라 **"그냥 열어드립니다"**였습니다.

---

## 2. 상세 분석 — 무엇이 바뀌었나

### 2-1. 무료 개방: 유료 벽이 사라졌다

공식 도움말의 문장은 짧고 명확합니다. "skills는 이제 Gemini 채팅에서 **만 18세 이상, 개인 구글 계정으로 로그인한 개인**에게 제공됩니다." 그동안 skills는 Gemini Spark와 함께 등장하면서 **AI Pro·AI Ultra 구독자 전용**이었습니다. Gems는 2025년 3월부터 무료였는데, 대체 기능이 유료가 되는 역방향이라 반발이 예상됐던 지점입니다.

실제 접근 방식도 Gems와 같습니다. **Settings → Skills**로 들어가면 되고, 기존 Gems 자산은 이관 시 자동으로 skills로 재생성됩니다. Google은 도움말에 "Gems를 제거할 때 자동으로 skills로 재생성한다"고 적었고, 먼저 옮기고 싶은 사람을 위해 **수동 재생성 3단계 절차**도 공개했습니다 — ① Gem을 열어 'Knowledge' 파일 다운로드 → ② Settings → Skills에서 'Create manually'로 이름·설명·지시문 복사 → ③ 파일이 필요하면 스킬을 `.zip`으로 내려받아 `SKILL.md`와 함께 폴더째 다시 업로드. 폴더 이름은 스킬 이름과 **정확히 일치**해야 합니다.

![Google 공식 도움말 페이지의 Gems 종료 타임라인 화면 (출처: Google 공식 도움말 페이지 캡처 — Android Authority)](/assets/images/posts/gemini-skills-free-gems-sunset-20260930/google-support-page-gems-timeline.jpg)
*구글 공식 도움말에 명시된 3단계 종료 일정 — 개인 계정 2026년 11월, Workspace 기업·비영리 2027년 3월, Workspace 교육 2027년 6월 (출처: Google 공식 도움말 페이지 캡처 — Android Authority, 2026.9.29)*

### 2-2. Gems보다 나은 지점 — 자동 호출과 조합

기능 스펙만 보면 skills가 한 세대 위입니다.

| 구분 | Gems | skills |
|:---|:---|:---|
| 호출 | 사이드 패널에서 목록을 스크롤 | **채팅창에 `/` (곧 `@`) 입력** |
| 자동 적용 | 없음 (직접 선택) | **프롬프트 맥락을 보고 관련 스킬을 자동 적용** |
| 조합 | 1개 | **여러 스킬 동시 조합** |
| 이식성 | 플랫폼 내부 저장 | **`SKILL.md` 파일로 내보내기·업로드 가능** |
| 활성 상한 | — | 100개 |

특히 눈에 띄는 건 **`SKILL.md` 이식성**입니다. 구글은 "다른 플랫폼에서 만든 스킬도 `SKILL.md` 파일을 업로드해 가져올 수 있다"고 안내합니다. 업무 자동화 파이프라인을 돌리는 입장에서 이건 꽤 큰 변화입니다. 커스텀 지시가 특정 서비스 DB에만 갇혀 있던 시절이 끝나고, **파일로 들고 다닐 수 있는 자산**이 되는 셈이니까요. 제 경우 사업자 정산용 지시문을 로컬 리포지토리에 원문으로 남겨두고 있었는데, 이제 그 파일을 그대로 올려 쓸 수 있게 됐습니다.

### 2-3. 아직 안 되는 것 — 도구 호환의 빈틈

무료 개방이 전부 좋은 소식은 아닙니다. 도움말에는 명확한 제약이 함께 적혀 있습니다.

- **일부 기본 도구 미지원** — Create video, Create music, Canvas, Deep Research, Guided learning은 skills에서 아직 동작하지 않습니다. "Gems에서 쓰던 대부분의 기본 도구가 skills와 호환되지 않는다"고 구글이 직접 밝혔습니다.
- **특정 기능 미연동** — Canvas, Deep Research와는 결합되지 않고, Connected Apps(Workspace 계열)와만 연동됩니다.
- **전용 채팅 목록 없음** — "이 스킬을 쓴 최근 대화"를 모아 보는 페이지가 없습니다.
- **파일 업로드 제한** — 파일을 붙여 스킬을 만드는 기능은 현재 **Gemini Mac 앱과 gemini.google.com**에서만 됩니다.
- **단계적 배포** — 공식 페이지가 열려도 앱에는 아직 'Gems'만 보이는 계정이 있습니다. Android Authority도 무료 계정에서 skills가 안 보였다고 전했습니다.

![무료 계정에는 아직 Gems 항목이 남아 있는 화면 (출처: Android Authority, 2026.9.29)](/assets/images/posts/gemini-skills-free-gems-sunset-20260930/gemini-free-account-gems.jpg)
*공식 개방 발표 이후에도 무료 계정에서는 Skills 대신 기존 Gems 항목이 보이는 사례가 확인됐습니다 — 단계적 배포로 보입니다 (출처: Android Authority, 2026.9.29)*

---

## 3. 영향 — 사용자와 개발자에게

### 3-1. 일반 사용자 관점

가장 실질적인 변화는 **"구독하지 않아도 커스텀 지시 자산을 잃지 않는다"**입니다.

- **무료 사용자** — Gems가 그대로 skills로 이관되고, 오히려 호출이 편해집니다. 단 Create video·Deep Research 같은 도구를 Gems에 묶어 쓰던 분은 기능이 줄어들 수 있습니다.
- **AI Pro·Ultra 구독자** — 기능상 이득은 제한적입니다(이미 쓰고 있었으니). 다만 자동 적용·조합 기능은 체감 차이가 납니다.
- **Gems 공유 링크를 배포한 분** — 이제 skills 공유 기능도 예고됐습니다. 공유 링크 의존은 여전히 리스크이니 원문 백업은 남겨두시는 게 좋습니다.

### 3-2. 업무자동화·개발자 관점

이번 사안의 본질은 **"프롬프트 자산의 저장 형식이 표준화됐다"**는 점입니다. Gem은 플랫폼 안에만 있는 설정이었지만, skills는 `SKILL.md`라는 파일로 존재합니다. 지시문이 **버전 관리 대상**이 되는 겁니다.

실무적으로는 이렇게 대비하면 됩니다.

- **지시문을 리포지토리에 둡니다** — `SKILL.md` 형태로 git에 커밋해 두면 모델·플랫폼이 바뀌어도 자산이 남습니다.
- **도구 의존은 따로 문서화합니다** — Canvas·Deep Research를 쓰는 워크플로는 skills에서 끊깁니다. 대체 경로를 미리 적어두세요.
- **기업은 거버넌스를 준비합니다** — Workspace 계정 마이그레이션은 **2027년 3월**입니다. 스킬 공유와 Google Drive 파일 임포트가 열리는 만큼, 사내 지시문·문서가 개인 스킬을 타고 흐를 수 있습니다. 관리자 정책을 미리 정해두는 편이 안전합니다.
- **자동화 파이프라인은 그대로** — Gemini API에는 이번 Gems 이관 이슈가 없습니다. 앱 기능 변화와 API 변화를 분리해서 보시면 됩니다.

---

## 4. 전망 — 어떻게 흘러갈까

제 판단으로는 네 갈래입니다.

1. **skills가 표준 인터페이스로 정착** — `/`에서 `@`로 호출 방식이 바뀌고, Windows 앱·업무 계정으로 확대되면 사실상 기본 진입점이 됩니다.
2. **도구 격차 재편** — 지금 skills에서 빠진 Canvas·Deep Research는 별도 기능으로 남거나, 시간이 지나 재통합됩니다. 구글도 "아직"이라는 표현을 씁니다.
3. **`SKILL.md` 생태계 확장** — 지시문 파일을 서로 주고받는 흐름은 다른 AI 도구에서도 관찰됩니다. 이식성이 곧 락인(lock-in) 방어 수단이 됩니다.
4. **Workspace 단계가 진짜 시험대** — 개인 계정은 무료 개방으로 마무리됐지만, 기업 계정은 2027년 3월에 관리자 정책과 충돌할 여지가 있습니다.

결국 이번 건은 **"기능 변경"이 아니라 "정책 한 줄이 사용자 워크플로를 바꾼 사례"**입니다. 지난주엔 유료 전환이 유력해 보였고, 하루 만에 판이 뒤집혔습니다.

---

## 5. 마무리 — 지금 뭘 해야 하나

결론부터 말씀드리면, **이번엔 구독할 이유가 사라졌습니다.** 무료로 열렸으니 굳이 결제하지 않아도 됩니다. 대신 세 가지는 지금 해두시면 손해가 없습니다.

- **skills 페이지부터 확인** — Settings → Skills에서 내 계정에 열렸는지 봅니다. 아직 Gems만 보이면 단계적 배포 중이니 며칠 기다리면 됩니다.
- **내 Gem 원문 백업** — 11월 자동 이관 전에 지시문·첨부 파일 목록을 텍스트로 남겨두세요. 자동 이관이 실패하는 경우의 보험입니다.
- **도구 의존 점검** — Canvas·Deep Research·영상·음악 생성에 묶인 Gem이 있다면 대체 흐름을 정리해 두세요.

저처럼 반복 업무를 커스텀 지시로 돌리는 분들이라면, 이번 소식은 "무료로 열렸다"보다 **"내 프롬프트가 파일로 들고 다닐 수 있는 자산이 됐다"**는 쪽이 더 중요한 변화입니다. 가성비 좋은 자동화를 찾는 분들께는, 지금 무료로 skills를 써보고 잘 되는 지시문만 남겨두는 걸 추천드립니다.

---

### 📚 출처
- Google 공식 도움말 — 'About the transition from Gems to skills' (support.google.com/gemini/answer/18560919, 2026.9.29 갱신)
- Android Authority — 'Gemini Skills are expanding to free accounts as Google prepares to sunset Gems' (2026.9.29)
- Neowin — 'Google opens Gemini Skills to all free account users' (2026.9.29)
- ixbt.com — 'Google открыла бесплатный доступ к Skills для пользователей Gemini' (2026.9.29)
- 9to5Google — 'Gemini app replacing Gems with skills in November' (2026.9.27)
- 이미지 — Wikimedia Commons(Google Gemini 공식 로고), Android Authority(Google 공식 도움말 페이지 캡처)

> 본 글은 공개된 공식 도움말·언론 보도를 바탕으로 작성한 개인 의견이며, skills 제공 범위와 Gems 종료 일정은 구글 발표에 따라 달라질 수 있습니다.
