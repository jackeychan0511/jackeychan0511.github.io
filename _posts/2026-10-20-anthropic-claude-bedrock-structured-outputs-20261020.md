---
layout: post
title: "Anthropic Claude 최신 소식 — 아마존 베드락, 모든 Claude 모델에 '구조화 출력' 정식 지원 (2026.10.20)"
date: 2026-10-20 16:58:00 +0900
categories: [career]
tags: [Anthropic, Claude, 앤트로픽, 클로드, AmazonBedrock, 베드락, StructuredOutputs, 구조화출력, JSONSchema, ConstrainedDecoding, StrictToolUse, Haiku55, Sonnet55, Opus55, 업무자동화, AI에이전트, 데이터추출, AWS, API, 2026년10월]
author: "40대 블로거"
image: /assets/images/posts/anthropic-bedrock-structured-outputs-20261020/bedrock-structured-outputs-hero.png
description: "2026년 10월 19일(미국 태평양시간), AWS가 아마존 베드락에서 모든 Anthropic Claude 모델에 대한 구조화 출력(Structured Outputs) 정식 지원을 발표했습니다. 제약 디코딩으로 생성 단계에서 JSON 스키마를 강제해 파싱 오류·재시도 로직을 없애는 방식이고, JSON Schema 출력 형식과 strict 도구 사용 두 축으로 제공됩니다. 문법 캐시 24시간, 배치 추론·교차 리전 지원, 지원 모델과 한계(재귀 스키마·수치 제약 미지원 등)까지 정리했습니다."
sitemap: false
noindex: true
---

![아마존 베드락의 Anthropic Claude 구조화 출력 발표 이미지](/assets/images/posts/anthropic-bedrock-structured-outputs-20261020/bedrock-structured-outputs-hero.png)
*AWS 공식 블로그 「Structured outputs for Anthropic Claude models on Amazon Bedrock」(2026.10.19, 미국 태평양시간) 대표 이미지. (이미지 출처: AWS Machine Learning Blog)*

> **📌 한줄 요약:** **2026년 10월 19일(미국 태평양시간)**, **AWS**가 **아마존 베드락(Amazon Bedrock)에서 모든 Anthropic Claude 모델에 대한 '구조화 출력(Structured Outputs)' 정식 지원(GA)** 을 발표했습니다. **bedrock-runtime**과 **bedrock-mantle** 두 엔드포인트 모두에서, 모델이 **응답을 만들 때부터 JSON 스키마를 강제하는 제약 디코딩(constrained decoding)** 방식으로 동작합니다. 결과는 **"파싱하고 실패하면 재시도"** 가 아니라 **"처음부터 스키마에 맞는 JSON"** 입니다. JSON Schema 출력 형식과 **strict 도구 사용(strict tool use)** 이라는 두 축으로 제공되며, **배치 추론과 교차 리전 추론**에서도 쓸 수 있습니다.

Claude를 AWS 안에서 굴리는 팀이라면 체감이 큰 변화입니다. 지난 몇 달간 Claude 쪽 소식은 신모델(Opus 5.5 → Sonnet 5.5 → Haiku 5.5)과 가격 인하, 정책 이슈가 중심이었는데, 이번 건은 결이 다릅니다. **모델이 똑똑해진 게 아니라, 모델의 출력 형식이 '확률'에서 '보장'으로 바뀐** 이야기입니다. 업무 자동화 파이프라인을 만드는 입장에서는 이쪽이 오히려 실무에 바로 꽂히는 변화인 경우가 많습니다.

**무슨 일이 있었나 → 구조화 출력의 원리 → 두 가지 축 → 코드 → 성능과 캐시 → 지원 범위와 한계 → 실무 활용 → 영향과 체크리스트 → 전망** 순서로 정리했습니다.

---

## 1. 무슨 일이 있었나 — 베드락에서 Claude 전 모델로 확대

AWS는 자사 머신러닝 블로그를 통해 **"아마존 베드락이 이제 모든 Anthropic Claude 모델에 대해 구조화 출력을 지원한다"** 고 밝혔습니다. 조건은 **"해당 모델이 제공되는 리전·엔드포인트에서"** 입니다.

| 항목 | 내용 |
|:---|:---|
| **발표일** | 2026년 10월 19일(미국 태평양시간) / 한국시간 10월 20일 오전 |
| **발표 주체** | AWS (Amazon Bedrock, Machine Learning Blog) |
| **핵심 내용** | **모든 Anthropic Claude 모델**에 구조화 출력 **정식 출시(GA)** |
| **지원 엔드포인트** | **bedrock-runtime**(Boto3·InvokeModel·Converse) + **bedrock-mantle**(Messages API) |
| **동작 방식** | **제약 디코딩**으로 생성 중 스키마 강제 (사후 검증 아님) |
| **제공 기능** | ① JSON Schema 출력 형식(`output_config.format`) ② **strict 도구 사용**(`strict: true`) |
| **함께 지원** | **배치 추론**, **교차 리전 추론**(bedrock-runtime), **스트리밍**(타입 지정 이벤트) |
| **과금** | 별도 기능 요금 없음 (기존 토큰 과금 체계 안에서 처리) |

발표문에서 눈에 띄는 문장은 이 부분입니다.

> "구조화 출력은 **토큰 생성 중에** JSON 스키마를 강제하기 위해 **제약 디코딩(constrained decoding)** 을 사용합니다. 사후에 맞추는 방식이 아닙니다. 모델은 스키마를 위반할 토큰을 아예 생성하지 않습니다. 이것은 출력 형식을 **확률적(probabilistic)에서 결정적(deterministic)으로** 바꿉니다."

LLM을 업무 시스템에 붙일 때 가장 자주 터지는 문제가 뭘까요. 모델이 똑똑하지 않아서가 아니라, **"JSON으로 달라고 했는데 코드블록을 붙이거나, 필드 하나를 빼먹거나, 숫자를 문자열로 주는"** 문제입니다. 그때마다 `try: json.loads()` / `except:` / 재시도 / 검증 스키마… 로 코드가 불어납니다. 이번 발표는 그 구간을 **모델 밖(코드)에서 모델 안(디코딩)** 으로 옮긴 것입니다.

---

## 2. 원리 — '사후 검증'과 '생성 중 강제'는 다르다

기존 방식과 무엇이 다른지 비교하면 이렇습니다.

| 구분 | 프롬프트로 형식 요청 (기존) | 구조화 출력 (이번) |
|:---|:---|:---|
| **스키마 적용 시점** | 응답 생성 **후** 검증·파싱 | 토큰 생성 **중** 제약 |
| **실패 양상** | 파싱 예외, 필드 누락, 타입 불일치 | 실패 시 대부분 **거부(refusal) 또는 토큰 한도** |
| **재시도 로직** | 사실상 필수 | 스키마 위반 재시도 불필요 |
| **출력 길이** | 설명·서두가 섞여 길어짐 | 스키마에 정의된 필드만 생성 → **출력 토큰 감소** |
| **재현성** | 실행마다 형식이 흔들림 | 형식은 **결정적** |

특히 마지막 항목이 실무에서 큽니다. 발표문은 **"스키마에 정의된 필드만 생성하도록 제한되므로 출력 토큰 수가 줄고, 대량 워크로드에서 추론 비용이 낮아질 수 있다"** 고 설명합니다. 형식 보장이 곧 **비용 절감**으로 이어지는 구조입니다.

베드락 내부 동작 순서도 명시돼 있습니다.

1. **스키마 검증** — 지원되는 **JSON Schema Draft 2020-12 하위 집합**인지 확인
2. **문법 컴파일** — 새 스키마면 문법(grammar) 아티팩트를 컴파일 (첫 요청은 조금 느림)
3. **캐싱** — 컴파일된 문법을 **24시간 캐시**
4. **제약 생성** — 스키마에 맞는 JSON만 만들어지는 방식으로 토큰 생성

---

## 3. 두 가지 축 — JSON 출력과 strict 도구 사용

이번 발표의 기능은 **서로 독립적으로, 또는 함께** 쓸 수 있는 두 가지입니다.

| 기능 | 목적 | 대표 사용처 |
|:---|:---|:---|
| **JSON Schema 출력 형식** | 응답 **전체의 구조**를 제어 | 데이터 추출, 보고서 생성, API 응답 |
| **Strict 도구 사용** | **도구 호출 인자**를 스키마로 검증 | 에이전트 워크플로, 함수 호출, 다단계 자동화 |

두 번째가 특히 중요합니다. 에이전트가 여러 단계를 도는 구조에서 실제로 깨지는 지점은 **"툴 이름은 맞는데 인자가 스키마와 다른"** 경우입니다. `strict: true`를 걸면 모델이 스키마에 없는 필드를 추가할 수 없고, 타입과 enum 값도 고정됩니다. 발표문은 이 기능이 **웹 검색·코드 인터프리터 같은 서버 측 도구를 조합하는 멀티스텝 에이전트**에서 특히 유용하다고 설명합니다.

---

## 4. 코드 — InvokeModel과 Messages API, 두 갈래

### (1) bedrock-runtime / InvokeModel (Anthropic 네이티브 바디)

InvokeModel API는 Anthropic 원형 요청 바디를 그대로 받기 때문에, 스키마를 **JSON 문자열이 아니라 JSON 객체**로 `output_config.format`에 넣습니다.

```python
import boto3, json

REGION = "us-east-1"
bedrock_runtime = boto3.client(service_name="bedrock-runtime", region_name=REGION)

schema = {
    "type": "object",
    "properties": {
        "name": {"type": "string", "description": "Customer name"},
        "email": {"type": "string", "description": "Customer email address"},
        "plan_interest": {"type": "string", "description": "Product plan of interest"},
        "demo_requested": {"type": "boolean", "description": "Whether a demo was requested"}
    },
    "required": ["name", "email", "plan_interest", "demo_requested"],
    "additionalProperties": False   # 필수
}

body = {
    "anthropic_version": "bedrock-2026-01-01",
    "max_tokens": 512,
    "messages": [{"role": "user", "content": "다음 문의 메일에서 리드 정보를 추출해줘: ..."}],
    "output_config": {"format": {"type": "json_schema", "schema": schema}}
}
```

### (2) bedrock-mantle / Messages API

`bedrock-mantle` 엔드포인트는 Messages API 형태로 호출합니다. 이쪽은 **도구 정의에 `strict: True`** 를 넣는 방식이 핵심입니다.

```python
tools=[{
    "name": "get_weather",
    "description": "Get the current weather for a specified location",
    "strict": True,
    "input_schema": {
        "type": "object",
        "properties": {
            "location": {"type": "string", "description": "The city and state, e.g. San Francisco, CA"},
            "unit": {"type": "string", "enum": ["celsius", "fahrenheit"]}
        },
        "required": ["location", "unit"],
        "additionalProperties": False
    }
}]
```

이렇게 하면 `location`은 항상 문자열, `unit`은 항상 `celsius` 또는 `fahrenheit`이고, **스키마 밖 필드는 생성되지 않습니다.**

---

## 5. 성능 — 첫 요청만 느리고, 이후엔 캐시

| 항목 | 내용 |
|:---|:---|
| **첫 요청** | 새 스키마 → 문법 컴파일 때문에 **약간 지연** |
| **이후 요청** | 캐시된 문법 사용 → **오버헤드 최소** |
| **캐시 범위** | **계정 단위**, 첫 접근 후 **24시간** |
| **캐시 무효화** | 스키마 **구조**를 바꾸면 재컴파일. **이름·설명(description)만 바꾸면 캐시 유지** |

실무적으로 이건 **"스키마를 자주 갈아치우는 설계는 손해"** 라는 뜻입니다. 요청마다 스키마를 동적으로 생성하는 구조라면 매번 컴파일 비용을 냅니다. 반대로 **스키마를 상수처럼 고정해 두고 재사용**하면 두 번째 요청부터 거의 공짜에 가깝습니다. 필드 설명을 다듬는 건 캐시를 깨지 않으니, 프롬프트 튜닝하듯 description을 개선해도 된다는 점도 기억할 만합니다.

---

## 6. 지원 범위와 한계 — 여기가 실제 함정

발표문에는 **지원되는 것**과 **"API는 받아주지만 강제하지는 않는 것"** 이 분명히 나뉘어 있습니다. 이 구분을 놓치면 "스키마에 걸었는데 왜 안 지켜지지?" 하는 상황이 생깁니다.

**지원되는 JSON Schema 기능**

- 기본 타입 전체: `object`, `array`, `string`, `integer`, `number`, `boolean`, `null`
- `enum` (문자열·숫자·불리언·null)
- `const`, `anyOf`, `allOf` (일부 제한)
- `$ref`, `$def`, `definitions` (**내부 참조만**)
- 문자열 포맷: `date-time`, `time`, `date`, `duration`, `email`, `hostname`, `uri`, `ipv4`, `ipv6`, `uuid`
- 배열 `minItems` (**0과 1만**)

**지원되지 않는 것 (요청은 통과, 생성 시 강제 안 됨)**

- **재귀 스키마**
- **외부 `$ref`**
- **수치 제약**: `minimum`, `maximum`, `multipleOf`
- **문자열 제약**: `minLength`, `maxLength`
- `additionalProperties`를 **`false` 이외의 값**으로 지정

즉 **"값의 범위"는 스키마가 아니라 애플리케이션에서 검증**해야 합니다. `minimum`/`maximum`을 스키마에 적어두고 안심하는 게 가장 흔한 함정입니다.

**필수 규칙 하나**: 모든 객체에 **`additionalProperties: false`** 를 넣어야 합니다. 없으면 스키마 자체가 거부됩니다.

**응답 처리 규칙**: 스키마를 강제해도 **두 가지 경우에는 비정상 응답이 나올 수 있습니다.** ① 안전상 **거부(refusal)**, ② **`max_tokens` 도달로 잘린 경우**. 그래서 코드에서 **`stop_reason`을 반드시 확인**해야 합니다. AWS 예제 코드도 `end_turn`일 때만 파싱하고, `max_tokens`면 "토큰 늘려서 재시도"로 분기합니다.

---

## 7. 실제로 어디에 쓰나 — 세 가지 패턴

발표문이 제시한 활용처는 화려하지 않지만, 업무 자동화에서 반복적으로 나오는 지점들입니다.

| 패턴 | 예시 | 왜 구조화 출력인가 |
|:---|:---|:---|
| **데이터 추출** | 인보이스·계약서·메일·녹취록에서 필드 추출 → DB·CRM 적재 | 필드 누락·타입 불일치가 곧 적재 실패 |
| **에이전트 도구 호출** | 다단계 워크플로에서 함수 인자 검증 | 잘못된 인자가 다음 단계 전체를 깨뜨림 |
| **대량 분류** | 티켓·리드·콘텐츠를 의도/우선순위/감정으로 분류 | 라우팅·모더레이션은 라벨 집합이 고정돼야 함 |

산업 예시로는 **재무 실적 보고서·컴플라이언스 문서의 필수 필드 추출**, **임상 노트를 전자의무기록(EHR) 구조로 변환**, **상품 카탈로그 보강**, **계약서의 핵심 조항·당사자·의무 분석** 등이 언급됩니다.

비용 관점에서도 의미가 있습니다. 발표문은 반복적·대량 작업에는 **Claude Haiku 5.5**를 예로 들며, 티켓 분류·라우팅처럼 **짧고 반복적인 작업**에 적합하다고 설명합니다. 파싱 실패로 재호출하던 토큰이 사라지는 것만으로도 대량 배치에서는 차이가 납니다.

---

## 8. 영향 — 누구에게 무엇이 달라지나

**① Claude를 API로 붙여 자동화를 만드는 개발자**
가장 직접적입니다. `output_config.format` 한 줄로 **검증 코드·재시도 로직**을 상당 부분 덜어냅니다. 다만 위에서 본 것처럼 **수치·문자열 제약은 여전히 직접 검증**해야 하고, **거부/토큰 한도** 분기는 남습니다. "구조화 출력이면 모든 검증이 사라진다"는 기대는 과합니다.

**② 사내 AI 도입을 검토하는 조직**
벤더 심사 질문지가 조금 바뀝니다. "이 모델이 정해진 스키마로 응답하는가"가 **모델 선택 기준**이 되고, 그 답이 **AWS 리전·엔드포인트 단위**로 갈립니다. 같은 Claude라도 **어느 엔드포인트로 부르느냐**에 따라 스키마 강제 여부가 달라질 수 있다는 점은 계약·아키텍처 검토 때 확인해야 합니다.

**③ 이미 파이프라인을 운영 중인 팀**
전면 재작성보다 **가장 자주 깨지는 단계 한두 개**에 먼저 적용하는 편이 안전합니다. 예를 들어 ① 문서 → JSON 추출, ② 분류 라벨링. 이 두 곳은 스키마가 안정적이라 캐시 효과도 바로 납니다.

---

## 9. 도입 전 체크리스트

- **스키마는 고정, 설명은 유연하게** — 구조 변경은 24시간 캐시를 깨고 첫 요청을 느리게 만듭니다. 필드 `description` 개선은 캐시에 영향이 없습니다.
- **`additionalProperties: false` 누락 확인** — 없으면 스키마가 거부됩니다.
- **수치·문자열 제약은 코드에서** — `minimum`/`maximum`/`minLength`는 강제되지 않습니다.
- **`stop_reason` 분기 필수** — `end_turn` / `max_tokens` / 거부를 구분해 처리하세요.
- **스키마 크기 관리** — Converse API에서는 스키마가 **페이로드 크기에 포함**됩니다. 최소 필드로 시작해 확장하는 편이 컴파일도 빠르고 디버깅도 쉽습니다.
- **enum 적극 활용** — 자유 문자열보다 enum이 라우팅·분류에서 훨씬 안전합니다.
- **실데이터로 사전 검증** — 엣지 케이스가 스키마 설계 문제를 드러냅니다.
- **엔드포인트·리전 확인** — bedrock-runtime과 bedrock-mantle, 그리고 모델이 제공되는 리전을 먼저 확인하세요.

---

## 10. 전망 — '형식 보장'이 기본값이 되는 중

이번 발표를 하나의 사건으로만 보면 "베드락 기능 하나 추가"입니다. 하지만 흐름으로 보면 조금 다릅니다. 최근 프런티어 모델 경쟁의 초점은 **"얼마나 똑똑한가"** 에서 **"얼마나 예측 가능하게 시스템에 붙는가"** 로 옮겨가고 있습니다. 구조화 출력, strict 도구 사용, 그리고 최근 Claude 플랫폼에서 확대된 **에이전트 실행 추적(OpenTelemetry)·동적 워크플로** 같은 기능은 모두 같은 방향입니다.

앞으로 지켜볼 지점은 세 가지입니다.

1. **다른 클라우드로의 확산 속도** — 구글 클라우드(Vertex)·마이크로소프트 파운드리에 같은 수준의 스키마 강제가 언제, 어느 모델까지 적용되는지
2. **강제되지 않는 제약의 축소** — `minimum`/`maximum`·문자열 길이 제한이 향후 지원 목록에 들어오는지
3. **에이전트 표준화** — strict 도구 사용이 사실상의 표준이 되면, 툴 스키마를 관리하는 별도 계층(스키마 레지스트리)이 업무 자동화 스택에 자리 잡을 가능성

솔직히 말하면, 이번 변화는 **"써보면 바로 체감되는"** 종류입니다. 프롬프트를 아무리 다듬어도 1%씩 섞이던 파싱 실패가 사라지는 경험은 문서 설명보다 명확합니다. 다만 **스키마 설계 능력이 곧 파이프라인 품질**이 되는 만큼, "어떤 필드를 필수로 둘 것인가"를 먼저 정하는 사람이 결국 이득을 봅니다.

Claude를 AWS 위에서 업무 자동화에 쓰는 분들이라면, 다음 배포 때 **가장 자주 깨지는 단계 하나**에만 적용해 보는 걸 권합니다. 그 한 곳에서 재시도 로그가 사라지는지 보면, 나머지 확장 여부는 자연스럽게 판단이 됩니다.

---

**참고 자료**
- AWS Machine Learning Blog, "Structured outputs for Anthropic Claude models on Amazon Bedrock", 2026.10.19 — aws.amazon.com/blogs/machine-learning/structured-outputs-for-anthropic-claude-models-on-amazon-bedrock/
- Amazon Bedrock 사용자 가이드, "Structured outputs"
- Claude Platform Docs, "Structured outputs" (지원 모델·플랫폼 목록)
