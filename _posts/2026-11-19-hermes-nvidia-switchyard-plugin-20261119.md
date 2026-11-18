---
layout: post
title: "NVIDIA Switchyard, Hermes 공식 플러그인으로 출시 — 로컬·클라우드 모델 자동 라우팅 추천 (2026.11.18)"
date: 2026-11-19 07:50:00 +0900
categories: [career]
tags: [헤르메스에이전트, HermesAgent, 엔비디아, NVIDIASwitchyard, 모델라우팅, VGR, 로컬LLM, 업무자동화, AI에이전트, 토큰비용절감, NousResearch, 2026년11월]
author: "40대 블로거"
image: https://pub-a8e00ddf355f47339bd2e01ff3221de7.r2.dev/assets/images/posts/hermes-nvidia-switchyard-plugin-20261119/nvidia-local-ai-hero.jpg
description: "2026년 11월 18일 엔비디아가 RTX Spark 컴팩트 데스크톱 출시와 함께 'NVIDIA Switchyard가 이제 Hermes에서 사용 가능하다'고 공식 블로그에서 밝혔습니다. Switchyard는 VGR(검증 게이트 라우팅) 방식으로 매 요청을 로컬 모델에 먼저 보내고, 검증에 실패한 경우에만 클라우드 모델로 승격하는 오픈소스 모델 라우터입니다. Hermes 마켓플레이스에는 인증 플러그인 nvidia-switchyard(v0.1.1, 베타)로 올라와 있고, Hermes 0.21.7 이상에서 `hermes plugins install nvidia-switchyard`로 설치합니다. 설치 조건, 실제 동작 방식, 스트리밍이 안 되는 제약, 어떤 경우에 쓸 만한지를 정리했습니다."
---

자동화 파이프라인을 운영하다 보면 결국 부딪히는 벽이 하나 있습니다. **"이 작업에 굳이 최상위 모델을 써야 하나?"** 라는 질문입니다. 요약하고, 제목 짓고, 승인 여부만 판단하는 잡무성 호출이 전체 토큰의 절반을 먹는 경우가 생각보다 흔합니다. 그렇다고 로컬 모델로 전부 돌리면 어려운 요청에서 답이 무너지고, 클라우드로만 돌리면 청구서가 무너집니다.

2026년 11월 18일, 엔비디아가 이 문제를 정면으로 다루는 소식을 공식 블로그에 올렸습니다. RTX Spark 컴팩트 데스크톱 출시 글 안에서, **"NVIDIA Switchyard, now available in Hermes"** 라고 못 박은 부분입니다. 일상적인 작업은 온디바이스에서, 고난도 추론만 클라우드로 보내 클라우드 비용을 관리한다는 설명이 함께 붙었습니다.

![엔비디아 RTX Spark 로컬 AI 공식 블로그 대표 이미지](https://pub-a8e00ddf355f47339bd2e01ff3221de7.r2.dev/assets/images/posts/hermes-nvidia-switchyard-plugin-20261119/nvidia-local-ai-hero.jpg)
*엔비디아 공식 블로그 'RTX Spark 컴팩트 데스크톱' 글 대표 이미지 (출처: NVIDIA Blog, 2026.11.18)*

> **📌 한줄 요약:** Switchyard는 **"로컬 모델이 먼저 답하고, 검증에서 떨어지면 그때 클라우드로 승격"** 하는 라우터입니다. Hermes에는 인증 플러그인 `nvidia-switchyard`(v0.1.1, **베타**)로 들어왔고, Hermes 0.21.7 이상에서 설치됩니다. 토큰 비용과 프라이버시를 동시에 잡을 수 있지만, **스트리밍이 사라지고 로컬 모델이 토큰 확률을 반환해야 한다**는 조건이 붙습니다.

---

## 1. 무슨 일이 있었나 — 하드웨어 발표에 묻혀 있던 한 줄

먼저 배경부터. 엔비디아는 11월 18일 ASUS·Dell·MSI의 **RTX Spark 컴팩트 데스크톱** 판매 시작을 알렸습니다. 스펙은 이렇습니다.

| 항목 | 내용 |
|---|---|
| CPU | 20코어 NVIDIA Grace |
| GPU | Blackwell RTX, 6,144 CUDA 코어 |
| 메모리 | 최대 128GB 통합 메모리 |
| AI 성능 | 최대 1 페타플롭 |
| 네트워크 | 10기가비트 이더넷 |
| 로컬 구동 | 최대 1,200억 파라미터 모델 |

여기서 끝이면 그냥 PC 신제품 뉴스입니다. 중요한 건 그 아래 문단이었습니다. 엔비디아는 "개발자는 **Hermes Agent**나 NVIDIA NemoClaw 블루프린트로 에이전트를 세팅할 수 있다"고 적은 뒤, 곧바로 이렇게 이어붙였습니다.

> "NVIDIA Switchyard, now available in Hermes, automatically routes agent tasks between local and cloud models."

즉 **엔비디아가 자사 모델 라우터를 Hermes 생태계에 공식으로 얹었다**는 뜻입니다. 하드웨어 발표 글의 곁가지 문장이지만, 에이전트를 오래 굴리는 쪽에는 이 한 줄이 본문보다 중요합니다.

![NVIDIA Switchyard 공식 소개 그래픽](https://pub-a8e00ddf355f47339bd2e01ff3221de7.r2.dev/assets/images/posts/hermes-nvidia-switchyard-plugin-20261119/nvidia-switchyard-graphic.jpg)
*NVIDIA 개발자 블로그의 Switchyard 소개 그래픽 (출처: NVIDIA Developer Blog)*

---

## 2. Switchyard가 실제로 하는 일 — VGR

핵심은 **VGR(Verification-Gated Routing, 검증 게이트 라우팅)** 입니다. 이름은 거창한데 동작은 단순합니다.

1. 요청이 들어오면 **로컬 모델이 먼저 답을 만듭니다.**
2. 그 답을 **검증**합니다. 로컬 모델이 반환한 토큰 확률을 근거로 답의 신뢰도를 점수화하는 방식입니다.
3. 검증을 **통과하면 그대로 사용자에게** 갑니다. 비용은 0에 가깝습니다.
4. **떨어지면 그때만 클라우드 모델로 승격**해서 다시 생성합니다.

"일단 로컬로 던지고, 애매하면 비싼 모델을 부른다"는 발상 자체는 새롭지 않습니다. 다른 점은 **라우팅 판단을 사람이 규칙으로 정하지 않고 모델 출력의 확률로 한다**는 쪽입니다. 그래서 로컬 모델이 토큰 확률을 돌려주지 않으면 이 구조가 성립하지 않습니다.

Hermes 쪽 구현은 이렇습니다.

- Switchyard는 Hermes 옆에 **루프백 사이드카**(`127.0.0.1:4110`)로 붙습니다.
- 라우팅 프리셋은 `switchyard/<preset>` 형태의 **모델 이름**으로 노출됩니다. 예를 들어 기본 프리셋은 `switchyard/vgr`입니다.
- 대시보드와 Hermes 데스크톱에 **Switchyard 페이지**가 생겨, 매 요청이 로컬로 갔는지 클라우드로 승격됐는지 실시간으로 볼 수 있습니다.
- 터미널에서는 `hermes switchyard` 계열 명령으로 설정·감시합니다.

---

## 3. 플러그인 스펙 — 설치 전에 확인할 것

Hermes 마켓플레이스의 `nvidia-switchyard` 페이지에 적힌 조건을 그대로 옮기면 이렇습니다.

| 항목 | 조건 |
|---|---|
| 상태 | **베타** (프로덕션 사용 미지원 명시) |
| 버전 | v0.1.1 |
| Hermes | **0.21.7 이상** (CLI 또는 데스크톱) |
| OS | Windows(x64/ARM64), macOS(애플 실리콘 macOS 11+ / 인텔 10.12+), Linux(x86_64/ARM64) |
| 로컬 모델 | OpenAI 호환 Chat Completions 서버이면서 **토큰 확률을 반환**해야 함 (예: `llama-server`) |
| 클라우드 모델 | OpenAI·Anthropic 호환 서버 또는 Hermes에 로그인된 프로바이더 |
| 텔레메트리 | 없음 (로그·라우팅 기록은 `~/.hermes/switchyard/`에 로컬 저장) |

설치 자체는 간단합니다. 데스크톱에서는 **Capabilities ▸ Plugins**에서 `NVIDIA Switchyard`를 찾아 Install을 누르면 되고, 터미널에서는 한 줄입니다.

```bash
hermes plugins install nvidia-switchyard
```

이때 Hermes가 플러그인과 함께 런타임인 `nemo-switchyard`를 PyPI에서 자체 파이썬 환경으로 설치합니다. **처음 설치에 몇 분 걸릴 수 있습니다.**

설정 화면에서 각 티어(로컬/클라우드) 모델을 고르는 방식은 세 가지입니다. 이미 Hermes에 붙여둔 프로바이더를 그대로 쓰거나, **"Set up llama.cpp for me"** 로 이 머신에 맞는 모델을 자동 다운로드하거나, 직접 URL을 넣는 방식입니다. 설정을 저장하면 `switchyard/vgr` 프리셋이 **기본 모델로 지정**됩니다.

CLI로 하려면 이렇게 됩니다.

```bash
hermes switchyard setup \
  --local-url http://127.0.0.1:8080/v1 --local-model <local model id> \
  --cloud-url <cloud server URL> --cloud-model <cloud model id> \
  --cloud-key-env SWITCHYARD_CLOUD_API_KEY
```

키는 커맨드라인에 직접 넣지 않고 환경변수 이름으로 지정하는 방식입니다.

---

## 4. 켜두면 달라지는 것 — 실제 사용감

기능 목록보다 중요한 건 **켜두었을 때 체감되는 차이**입니다. 문서에 명시된 동작을 정리하면 이렇습니다.

**좋아지는 부분**

- **부수 작업이 자동으로 로컬로 내려갑니다.** 세션 제목 생성, 스마트 승인 판단, 컨텍스트 압축 같은 잡다한 호출은 VGR 검증 없이 로컬 모델이 처리합니다. 에이전트를 하루 종일 켜두는 구성에서 이 부분이 의외로 큰 비중을 차지합니다.
- **비용 구조가 예측 가능해집니다.** 로컬에서 끝난 턴은 과금이 없으니, 클라우드 청구서는 "정말 어려웠던 요청"의 함수가 됩니다.
- **민감한 요청이 밖으로 나가지 않는 경우가 생깁니다.** 로컬에서 검증을 통과하면 그 요청 본문은 외부로 전송되지 않습니다.
- **판단 근거를 볼 수 있습니다.** 대시보드 Monitor 탭과 `hermes switchyard watch`로 각 턴이 어디로 갔는지, 왜 승격됐는지 따라갈 수 있습니다.

**달라지는(때로는 불편한) 부분**

- **응답이 스트리밍되지 않습니다.** VGR이 로컬 답변을 검증한 뒤에야 내용을 보여주기 때문에, 답이 통째로 한 번에 나타납니다. 타이핑되는 걸 보며 기다리는 흐름에 익숙하다면 이건 꽤 큰 변화입니다. 대기 중에 Stop을 누르거나 다시 보내면 안 됩니다.
- **로컬 모델 응답에 기본 시간 제한이 없습니다.** 요청당 최대 30분까지 로컬이 붙잡을 수 있고, 그 뒤에 클라우드로 넘어갑니다. 빠른 모델을 물려두지 않으면 체감 속도가 떨어질 수 있습니다.
- **사이드카가 Hermes를 종료해도 남습니다.** `hermes switchyard stop`으로 직접 내려야 합니다. 백그라운드 프로세스를 신경 쓰는 편이라면 알아둘 부분입니다.
- **Claude Pro/Max 로그인은 라우팅할 수 없습니다.** 구독 로그인 티어를 쓰려면 별도 동의 절차가 필요하고, Claude Pro/Max는 아예 대상이 아닙니다. API 키 기반 프로바이더를 준비해야 합니다.

![MSI EdgeMesa N AI+ RTX Spark 컴팩트 데스크톱](https://pub-a8e00ddf355f47339bd2e01ff3221de7.r2.dev/assets/images/posts/hermes-nvidia-switchyard-plugin-20261119/msi-edgemesa-rtx-spark.jpg)
*MSI EdgeMesa N AI+ 시리즈 RTX Spark 컴팩트 데스크톱 (출처: NVIDIA Blog)*

---

## 5. 어떤 경우에 쓸 만한가

정리하면 Switchyard는 **"항상 켜져 있는 에이전트"** 와 궁합이 맞습니다. 반대로 짧은 대화 몇 번 하고 마는 용도라면 설정 비용이 이득을 넘습니다.

**추천하는 경우**

- 크론잡이나 예약 작업으로 하루에 수십~수백 번 에이전트를 호출하는 구성
- 요약·분류·제목 생성 같은 **저난도 호출이 전체의 절반 이상**인 파이프라인
- 로컬 GPU가 이미 있는데 놀고 있는 경우 (llama.cpp 한 번 클릭 설정으로 붙일 수 있습니다)
- 클라우드 청구서가 매달 예측 불가능하게 튀는 걸 막고 싶은 경우

**서두르지 않는 게 나은 경우**

- 응답이 글자 단위로 흘러나오는 스트리밍 UX가 업무 흐름에 필수인 경우
- 프로덕션 서비스에 바로 물려야 하는 경우 (**베타**이고, 라우팅·설정·명령이 릴리스마다 바뀔 수 있다고 명시돼 있습니다)
- 로컬 모델을 띄울 GPU가 없거나, 토큰 확률을 반환하지 않는 서버만 있는 경우
- Claude 구독 로그인만으로 운영 중인 경우

설치 전 체크리스트로 줄이면 이렇습니다. **① Hermes 0.21.7 이상인가 ② 토큰 확률을 주는 로컬 서버(또는 llama.cpp)가 있는가 ③ 클라우드 쪽 API 키가 있는가 ④ 스트리밍 없이도 되는 작업인가.**

생각보다 걸림돌은 1번보다 2번입니다. 로컬 모델을 이미 돌리고 있어도 확률을 반환하지 않는 엔드포인트라면 VGR이 성립하지 않으니, `llama-server` 기반인지 먼저 확인하는 편이 좋습니다.

가성비 좋은 구성은 결국 **로컬은 잡무, 클라우드는 판단**으로 역할을 나누는 쪽입니다. 모델 라우팅은 몇 년 전만 해도 직접 규칙을 짜야 했는데, 이제는 플러그인 설치 한 줄로 붙는 단계까지 왔습니다. 베타 딱지를 떼는 시점이 이 기능의 진짜 시작일 겁니다.

> **💡 참고**
> - 엔비디아 공식 발표: [NVIDIA RTX Spark Compact Desktops Out Now](https://blogs.nvidia.com/blog/rtx-spark-desktop-local-ai-available-now/) (2026.11.18)
> - 플러그인 페이지: [nvidia-switchyard — Hermes Marketplace](https://hermes-agent.nousresearch.com/marketplace/nvidia-switchyard)
> - 기술 배경: [Route AI Agent Workloads Across Models with NVIDIA NeMo Switchyard](https://developer.nvidia.com/blog/route-ai-agent-workloads-across-models-with-nvidia-nemo-switchyard/)
> - Hermes 문서: [hermes-agent.nousresearch.com/docs](https://hermes-agent.nousresearch.com/docs)
