---
layout: post
title: "Google Gemini 최신 소식 — Gemini Notebook, '인터랙티브 리포트' 기능 준비 중… 보고서를 PDF·워드·엑셀·PPT로 바로 내보내기 (2026.8.30)"
date: 2026-09-01 15:54:00 +0900
categories: [career]
tags: [Gemini, Google, GeminiNotebook, NotebookLM, 인터랙티브리포트, AI리서치, AI보고서, 스튜디오, 구글AI, AI뉴스, 2026년8월]
author: "40대 블로거"
description: "2026년 8월 30일 TestingCatalog가 Gemini Notebook(전 NotebookLM)에 '인터랙티브 리포트(Interactive Reports)' 기능이 준비되고 있다고 보도했습니다. 스튜디오(Studio) 패널에 '대화형 보고서를 만들 수 있습니다' 안내가 등장했고, '스튜디오 콘텐츠를 포함한 인터랙티브 보고서'라는 새 프리셋이 확인됐습니다. 여기에 채팅 창에서 바로 PDF·Word·Excel·PowerPoint로 내보내기가 이미 지원된다는 소식까지. 리서치 툴에서 '구조화된 연구 워크스페이스'로 진화하는 Gemini Notebook의 움직임을 이슈 요약 → 상세 분석 → 영향(사용자·개발자) → 전망 순서로 정리했습니다."
image: /assets/images/posts/gemini-notebook-interactive-reports-20260830/gemini-notebook-official-cover.png
---

요즘 저처럼 **"자료 수집은 AI가 하고, 보고서 정리는 여전히 내 손으로"** 하시는 분들 많으시죠? 솔직히 저도 회사 보고서 쓸 때마다 Gemini Notebook(전 NotebookLM)에 자료를 올려 요약을 뽑고, 그걸 다시 워드·PPT로 옮겨 붙이느라 시간을 꽤 썼는데요. 그런데 이번 주, 그 '옮겨 붙이기' 단계가 통째로 사라질 조짐이 보입니다.

지난 8월 30일, AI 앱 분석 매체 **TestingCatalog**가 **Gemini Notebook에 '인터랙티브 리포트(Interactive Reports)' 기능이 준비되고 있다**고 보도했습니다. 같은 시각, **PDF·Word·Excel·PowerPoint로의 내보내기가 채팅 창에서 바로 지원되는 것**도 확인됐는데요. 오늘은 이 소식을 **이슈 요약 → 상세 분석 → 영향(사용자·개발자) → 전망** 순서로 정리해 드립니다.

![Gemini Notebook 공식 커버 이미지](/assets/images/posts/gemini-notebook-interactive-reports-20260830/gemini-notebook-official-cover.png)
*Gemini Notebook — 구글 공식 블로그 'NotebookLM is now Gemini Notebook' (출처: Google 공식 블로그, 2026.7.16)*

## 📌 이슈 요약: "리서치 툴"에서 "보고서 공장"으로

- **8월 30일**, TestingCatalog가 Gemini Notebook의 스튜디오(Studio) 패널에서 **'인터랙티브 리포트' 준비 징후**를 발견
- 스튜디오 패널에 **"새로워진 기능: 이제 인터랙티브 보고서를 만들 수 있습니다(New: you can now create interactive reports)"** 안내 문구와 함께 기존 보고서 생성(Create Report) 화면으로 연결되는 버튼 등장
- 보고서 생성기 내부에 **'Interactive' 옵션(New 표시)** 추가 — 설명은 **"스튜디오 콘텐츠가 포함된 인터랙티브 보고서"**
- **'Overview' 프리셋**도 확인 — 핵심 정보를 대화형으로 요약하면서 스튜디오 콘텐츠를 결과물에 바로 녹여 넣는 방식
- 아울러 **PDF·Word·Excel·PowerPoint 내보내기가 채팅 창에서 바로 지원**되는 것으로 확인돼, "분석 → 정리 → 제출"까지 한 화면에서 끝나는 워크플로가 가시화
- 구글은 지난 7월 16일 NotebookLM을 **Gemini Notebook으로 리브랜딩**하며 문서·차트·스프레드시트·발표자료 생성(스튜디오)을 확장해 온 상태 — 이번 기능은 그 연장선

> ⚠️ 본 글은 2026년 9월 1일 기준 TestingCatalog(8/30)·Pasquale Pillitteri(8/30)·Google 공식 블로그(Gemini Notebook 리브랜딩, 7/16) 보도를 바탕으로 작성했습니다. 인터랙티브 리포트는 아직 '준비 중' 단계로, 실제 출시 시점과 최종 기능은 달라질 수 있습니다.

## 🔍 상세 분석: 무엇이 달라지나

### 1. "보고서를 대화하듯 만든다" — 인터랙티브 리포트의 정체

TestingCatalog가 공개한 화면을 보면, Gemini Notebook의 스튜디오 패널에 **"이제 인터랙티브 보고서를 만들 수 있습니다"**라는 안내가 떠 있고, 버튼을 누르면 기존의 보고서 생성 커스터마이즈 화면으로 연결됩니다. 그 안에는 두 가지 눈에 띄는 요소가 있었습니다.

- **Interactive(신규)**: "스튜디오 콘텐츠가 포함된 인터랙티브 보고서"
- **Overview(프리셋)**: 핵심 정보를 대화형 요약으로 만들면서 스튜디오 콘텐츠를 결과물에 포함

사용자는 **언어를 고르고, 만들고 싶은 보고서를 자세한 지시문으로 설명**합니다. 구글이 준비한 예시 프롬프트는 "2026년 기능성 음료 시장의 공식 경쟁사 분석 — 경쟁사·유통·가격을 다뤄 출시 전략을 지원" 같은 식입니다. 즉 단순 요약이 아니라 **여러 출처를 묶어 '구조화된 연구 작업물'을 만드는 것**에 초점이 맞춰져 있습니다.

### 2. "이미 쓸 수 있는 것" — 채팅 창에서 바로 Office 내보내기

인터랙티브 리포트는 준비 중이지만, **내보내기 기능은 이미 활성화**되어 있다는 보도가 이어졌습니다. 지난 7월 NotebookLM이 Gemini Notebook으로 바뀌면서 스튜디오가 문서·차트·스프레드시트·발표자료 생성까지 확장됐는데, 이제 **채팅 창에서 바로 PDF·Word·Excel·PowerPoint 파일로 내보내는 것**이 가능해졌다는 것입니다. 별도 툴에 복사·붙여넣기 하던 단계가 사라지는 셈이죠.

### 3. 흐름으로 보는 구글의 의도

| 시점 | 변화 |
|:---|:---|
| 7월 16일 | NotebookLM → **Gemini Notebook** 리브랜딩 + 코드 실행·클라우드 기능 추가 |
| 7월 중 | 스튜디오 확장 — 문서·차트·스프레드시트·발표자료 생성 |
| 8월 15일 | (발견) Google Drive 파일을 소스 추가 없이 바로 참조 |
| 8월 30일 | **(발견) 인터랙티브 리포트 준비 + Office 포맷 내보내기 활성화** |

구글은 Gemini Notebook을 단순 'PDF 요약 도구'가 아니라, **자료를 넣고 → 분석하고 → 보고서·발표자료까지 완성해 내보내는 연구 워크스페이스**로 키우고 있습니다.

![Gemini Notebook 스튜디오 패널 화면](/assets/images/posts/gemini-notebook-interactive-reports-20260830/gemini-notebook-interactive-reports-testingcatalog.jpg)
*Gemini Notebook 스튜디오 패널 — '인터랙티브 보고서' 안내 문구가 확인된 화면 (출처: TestingCatalog, 2026.8.30)*

## 👥 영향: 사용자와 개발자

### 일반 사용자 (직장인·연구자·학생)
- **"요약만" 하던 AI가 "제출용 산출물"까지** 만들어 줌 — 보고서·제안서·발표자료 워크플로의 복붙 단계가 대폭 줄어듦
- 영어뿐 아니라 언어 선택이 가능해, 한국어 보고서 작성에도 바로 활용 가능할 전망
- 자료 출처 기반(grounded) 답변 특성상 **회사 내부 문서·연구자료를 다루는 지식 근로자**에게 특히 유용

### 개발자·스타트업
- PDF·워드·엑셀·PPT 내보내기가 API·자동화 파이프라인과 결합하면 **리서치 → 산출물 생성 자동화** 가능성
- Gemini Notebook이 코드 실행(secure cloud computer)까지 갖추고 있어, 데이터 분석 결과를 보고서에 바로 녹이는 시나리오도 열림
- 다만 인터랙티브 리포트는 아직 '준비 중'이므로 **프로덕션 의존은 이르다** — 안정화 후 적용 검토 권장

### 경쟁 구도
- ChatGPT(대화·문서), Claude(코드·문서)와 달리 **"출처 기반 리서치 + 스튜디오 산출물 + Office 내보내기"** 조합으로 차별화
- 구글은 Cloud Next 2026에서 공개한 **Gemini Enterprise 'Projects'**(사람+에이전트 공동 워크스페이스)와도 자연스럽게 연결되는 방향 — 8월 31일엔 엔터프라이즈용 'Rooms' 프로토타입 정황까지 포착됨

## 🔮 전망

1. **"AI 리서치 툴"의 정의가 바뀐다** — 요약·질의응답을 넘어 '최종 산출물 생산'이 기본이 되는 방향
2. **사무직 워크플로 재편** — 자료 정리·보고서 초안·발표자료 변환까지 한 화면에서 해결되면서, AI 활용 격차가 실무 생산성 차이로 직결될 전망
3. **엔터프라이즈 확장** — Projects·Rooms 등 '사람+AI 공동 작업 공간' 개념과 결합해 팀 단위 협업 툴로 진화할 가능성
4. **출시 시점 주목** — TestingCatalog의 발견이 실제 공개로 이어질지는 수주 내 확인될 전망. 구글의 최근 업데이트 속도(3.7 Flash→3.8 Flash 내부 테스트)를 고려하면 생각보다 빠를 수 있습니다

## 💡 마무리: 지금 바로 써보는 법

- **아직 인터랙티브 리포트는 준비 중**이지만, **PDF·Word·Excel·PPT 내보내기는 이미 확인된 기능**입니다. Gemini Notebook(notebook.google)에 업무 자료를 올리고, "이 내용을 바탕으로 3쪽짜리 제안서 초안을 만들어줘"라고 요청한 뒤 **내보내기 메뉴에서 워드·PPT를 선택**해 보세요. 복붙 시간이 확실히 줄어듭니다.
- **보고서를 자주 쓰는 분**이라면 스튜디오의 차트·스프레드시트 생성과 조합해 "분석 → 시각화 → 제출"을 한 번에 해보는 걸 추천합니다. 솔직히 아직 '인터랙티브' 단계의 최종 모습은 두고 봐야 하지만, **가성비 좋은 AI 리서치 툴을 찾는 분께는 지금도 충분히 쓸 만한 업데이트**입니다.

*이미지 출처: Google 공식 블로그(NotebookLM is now Gemini Notebook, 2026.7.16) / TestingCatalog(Google prepares Interactive Reports for Gemini Notebook, 2026.8.30) — Google 로고 및 제품 이미지는 Google의 자산입니다.*
