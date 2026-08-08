---
layout: post
title: "Google Gemini 최신 소식 — DeepMind 리더십 대개편, Hassabis CEO 사임하고 Gemini 4로 집중 (2026.8.8)"
date: 2026-08-08 16:02:00 +0900
categories: [career]
tags: [Gemini, Google, DeepMind, DemisHassabis, KorayKavukcuoglu, JeffDean, Gemini4, 구글AI, AI뉴스, 2026년8월, 리더십개편]
author: "40대 블로거"
description: "Google DeepMind가 8월 5일 대대적인 리더십 개편을 발표했습니다. Demis Hassabis가 CEO에서 물러나 회장 겸 Alphabet 최고과학자로, Koray Kavukcuoglu가 SVP로 일상 운영을 총괄하게 됐고, Jeff Dean은 퇴사해 Discovery Loop를 창업합니다. Gemini 3.5 Pro 지연 속에서 Gemini 4로 전략을 집중하는 이번 개편의 의미를 요약·분석·전망 순서로 정리했습니다."
---

요즘 저처럼 **AI 뉴스**를 매일 챙겨보시는 분들, 그리고 "구글의 차세대 모델은 언제 나올까" 궁금하신 분들 많으시죠? 특히 지난주 Gemini 4 사전학습 소식(8월 5일 포스팅)을 보신 분이라면, 이번 소식이 더 궁금하실 텐데요.

2026년 8월 5일, 구글의 AI 연구 조직 **Google DeepMind가 대대적인 리더십 개편**을 발표했습니다. 창업자이자 CEO였던 **Demis Hassabis(데미스 하사비스)**가 CEO 자리에서 물러나고, **Koray Kavukcuoglu(코레이 카부쿠올루)**가 일상 운영을 총괄하게 된 겁니다. 솔직히 DeepMind가 이렇게 큰 폭으로 지휘부를 바꾼 건 설립 이후 처음이라, AI 업계에서는 "구글이 뭔가 크게 바꾸려는 신호"라는 반응이 나오고 있습니다.

이번 글에서는 이 이슈를 **요약 → 상세 분석 → 영향(사용자·개발자) → 전망** 순서로 정리해 드립니다.

![Demis Hassabis, 2024년 노벨 화학상 기자회견 사진](/assets/images/posts/gemini-deepmind-leadership-20260808/demis-hassabis-nobel-2024.jpg)
*Demis Hassabis — Google DeepMind CEO에서 물러나 회장 겸 Alphabet 최고과학자로 (출처: Wikimedia Commons, 2024년 노벨상 기자회견 공식 사진)*

## 📌 이슈 요약: DeepMind, 설립 이래 최대 리더십 개편

- **Demis Hassabis**: Google DeepMind CEO 사임 → **회장(Chair) + Alphabet 최고과학자(Chief Scientist)**로 전환. AGI 전략과 AI 과학 응용에 집중하며, AI 신약개발 자회사 **Isomorphic Labs** 수장도 계속 겸임 (8월 5일 Alphabet 발표, Sundar Pichai가 X로 공식 확인)
- **Koray Kavukcuoglu**: DeepMind CTO 겸 Alphabet 최고 AI 아키텍트 → **SVP(선임 부사장)**로 일상 운영 총괄. **Sundar Pichai CEO에게 직접 보고**하며 Gemini 모델 개발을 총괄
- **Jeff Dean**: 구글 최고과학자(27년 근속) → **퇴사**, ML·과학·엔지니어링 연구를 자동화하는 공공이익법인 **Discovery Loop** 공동창업. Sanjay Ghemawat·Oriol Vinyals·Quoc Le 등 전 구글 연구진 합류, Alphabet이 창업 투자자로 참여
- **배경**: 플래그십 **Gemini 3.5 Pro가 6월 출시 목표 대비 수개월 지연**되고, 3.5 Flash·3.6 Flash도 기대 이하 성능 논란이 이어지면서 전략 재점검이 불가피했음
- **시장 반응**: 발표 직후 **Alphabet 주가 약 5% 하락**

> 📌 참고: 본 글은 2026년 8월 8일 기준 Fortune, Axios, Geeky Gadgets, News18 등 주요 매체 보도와 Sundar Pichai의 공식 게시물을 바탕으로 작성한 정보형 기사입니다.

## 🔍 상세 분석: 왜 지금, 이런 개편인가

### (1) Hassabis의 선택 — "AGI는 가까이 왔다"

Hassabis는 직원들에게 보낸 서한에서 **"AGI(범용인공지능)가 가까이 왔고, 다음 단계를 제대로 밟는 것이 인류에게 중요하다"**고 밝혔습니다. 2024년 노벨 화학상을 받은 AlphaFold 연구를 이끈 그답게, 앞으로는 **조직 관리보다 AGI 전략과 과학 응용**에 힘을 싣겠다는 의미로 읽힙니다. 특히 그가 "AI의 가장 위대한 응용"이라고 부르는 **Isomorphic Labs(신약개발)**에 더 많은 시간을 쏟을 예정입니다.

### (2) Kavukcuoglu 체제 — Gemini 개발의 새 총괄

새 SVP가 된 Kavukcuoglu는 DeepMind에서 **딥러닝 팀을 창설**한 1세대 연구자입니다. CTO 겸 Alphabet 최고 AI 아키텍트로서 Gemini 개발을 이끌어온 만큼, 이번 개편으로 **"Gemini 모델 개발 = Kavukcuoglu 1인 체제"**가 확실해졌습니다. 다만 CEO 타이틀 대신 SVP로 Pichai에게 직접 보고하는 구조라, 의사결정 라인이 짧아진 것이 특징입니다.

### (3) Jeff Dean의 퇴사 — 또 하나의 변수

구글에서 27년을 근무하며 TensorFlow 등 구글 AI 인프라의 기초를 만든 Jeff Dean도 퇴사했습니다. 그는 **Discovery Loop**라는 공공이익법인(PBC)을 공동창업하는데, 머신러닝·과학·엔지니어링 연구 자체를 자동화하는 것이 목표입니다. Alphabet이 창업 투자자로 참여하고 클라우드 컴퓨팅을 공급하기 때문에, 적대적 이탈이라기보다 **"구글의 지원을 받는 외부 연구소"**에 가깝습니다.

### (4) 왜 이런 개편이 필요했나 — 2026년의 위기감

이번 개편의 배경에는 냉정한 현실이 있습니다.

- **Gemini 3.5 Pro 지연**: 6월 출시 목표가 수개월 밀리면서 "코딩 성능이 내부 기준에 미달"이라는 보도까지 나왔습니다. 8월 12일 출시설(루머)이 돌고 있지만 아직 공식 발표는 없습니다.
- **성능 논란**: 3.5 Flash·3.6 Flash가 벤치마크 기대치를 밑돌았다는 평가가 이어졌습니다.
- **핵심 인력 이탈**: Gemini 공동 리더 Noam Shazeer가 OpenAI로, 노벨상 수상자 John Jumper가 Anthropic으로 떠나는 등 핵심 연구진 이탈이 겹쳤습니다.

이런 상황에서 구글은 **"증분 업데이트(3.5 Pro)보다 차세대 모델 Gemini 4에 올인"**하는 전략을 선택했습니다. 실제로 8월 5일 공식 블로그에서 **Gemini 4 사전학습(pre-training) 시작**이 확인됐고, 이번 리더십 개편으로 그 방향이 더 확고해졌습니다.

![Google DeepMind 공식 로고](/assets/images/posts/gemini-deepmind-leadership-20260808/google-deepmind-official-logo.png)
*Google DeepMind 공식 로고 — Hassabis·Kavukcuoglu 체제로 새출발 (출처: Wikimedia Commons, Google 공식 로고)*

## 👤 영향: 사용자와 개발자에게 어떤 변화가?

### 사용자 입장
- **Gemini 3.5 Pro 대기가 더 길어질 수 있음**: 리더십 개편과 Gemini 4 집중 전략으로 인해 3.5 Pro 출시가 더 밀리거나, 아예 **Gemini 4로 건너뛸 가능성**도 있습니다. 지금 Gemini 앱에서 쓰는 3.5 Flash·3.6 Flash가 당분간 주력 모델이 될 전망입니다.
- **장기적으로는 더 나은 모델 기대**: 조직 개편이 성공하면 의사결정이 빨라져, Gemini 4 개발 속도와 품질에 긍정적 영향을 줄 수 있습니다.
- **기업 고객**: Vertex AI·Gemini Enterprise를 쓰는 기업은 모델 로드맵 불확실성이 커진 만큼, "지금 3.5 계열에 맞출지, 4를 기다릴지" 전략적 판단이 필요해졌습니다.

### 개발자 입장
- **Gemini API 로드맵 주시 필요**: 3.5 Pro 출시 시점이 어긋나면서 API 버전 전환 계획을 세우기 어려워졌습니다. 공식 changelog와 AI Studio 업데이트를 평소보다 자주 확인하는 게 좋습니다.
- **Kavukcuoglu 체제의 방향성**: 그가 강조해 온 **에이전트(Agent)·도구 통합·추론 효율** 쪽으로 Gemini API의 우선순위가 맞춰질 가능성이 높습니다.
- **기회 요인**: Jeff Dean의 Discovery Loop처럼 "AI로 연구·개발 자동화"라는 새 분야가 떠오르면서, AI 에이전트 기반 개발 도구 시장이 더 커질 수 있습니다.

## 🔮 전망: Gemini 4가 승부처

이번 개편의 핵심은 결국 **"Gemini 4로 승부를 건다"**는 메시지입니다. Hassabis는 AGI 안전과 과학 응용(Isomorphic Labs)으로, Kavukcuoglu는 Gemini 개발로 역할이 깔끔하게 나뉘면서, 구글은 AI 연구와 제품화를 동시에 가속할 수 있는 체제를 갖췄습니다.

다만 관건은 두 가지입니다. 첫째, **Gemini 4가 언제, 어떤 성능으로 나오느냐** — OpenAI·Anthropic과의 격차를 좁히려면 지연된 3.5 Pro의 공백을 메울 만한 결과물이 필요합니다. 둘째, **핵심 인력 이탈이 멈추느냐** — 이번 개편이 "안정"이 아니라 "재편의 시작"이 될지 지켜봐야 합니다.

개인적으로는 이번 사건이 **"AI 3강(구글·오픈AI·앤스로픽) 경쟁이 모델 대결에서 조직 역량 대결로 옮겨가는 신호"**라고 생각합니다. 같은 모델 아키텍처 시대에선 누가 더 빠르게, 더 좋은 팀으로 움직이느냐가 승부를 가르니까요.

Gemini를 쓰시는 분이라면 지금 당장 바뀌는 건 없지만, **AI Studio와 Gemini 앱의 모델 업데이트를 평소보다 자주 확인해 보시길 추천**합니다. 3.5 Pro든 Gemini 4든, 다음 플래그십이 발표되는 순간이 올해 AI 시장의 가장 큰 변곡점이 될 테니까요.
