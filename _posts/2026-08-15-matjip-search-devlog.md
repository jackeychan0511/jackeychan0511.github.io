---
layout: post
title: "Flutter로 만든 '재찬키의 맛집 검색' 앱 개발일지 — GPS 기반 맛집 찾기 앱을 9단계로 완성한 과정 (코드 공유)"
date: 2026-08-15 23:30:00 +0900
categories: [ai-teacher]
tags: [Flutter, 개발일지, 앱개발, 맛집, 맛집검색, 카카오API, 카카오맵, GPS, 안드로이드, geolocator, 코딩, 2026년8월]
author: "40대 블로거"
description: "밖에 나가면 어디서 뭐 먹을지 고민될 때, 내 위치 주변 맛집을 카테고리별로 보여주는 '재찬키의 맛집 검색' Flutter 앱을 만든 개발일지입니다. 카카오 로컬 API 연동, GPS 기반 거리순 검색, 카테고리 중복 버그 수정, 테스트 13/13과 APK 배포까지 9단계로 정리했습니다."
image: /assets/images/posts/matjip-search-devlog-20260815/3.jpg
---

며칠 전 [Flutter로 만든 'AI 선생님' 앱 개발일지](/2026/08/03/ai-teacher-devlog/)로 앱 개발기를 공유했더니, 반응이 좋아서 이번엔 **맛집 검색 앱**도 만들어봤습니다.

> 📌 내 위치 주변 2km 맛집을 카테고리별로 찾아주는 앱 — 카카오 로컬 API + GPS 기반 거리순 검색

이번 글은 **'재찬키의 맛집 검색'** 앱을 **어떻게 만들었는지, 어떤 문제를 만나고 어떻게 해결했는지**를 단계별로 정리한 개발일지입니다. 특히 마지막에 나오는 **카테고리 중복 버그**는 카카오 API의 함정을 파고드는 과정이 재미있으니 꼭 끝까지 봐주세요!

---

## 📋 이 글을 읽으면 알 수 있는 것

| 단계 | 내용 |
|:----|:----|
| 0단계 | 기획 — 어떤 맛집 검색 앱을 만들까 |
| 1단계 | 프로젝트 세팅과 개발 환경 |
| 2단계 | 카테고리 모델 — 11가지 맛집 분류 만들기 |
| 3단계 | 카카오 로컬 API 연동 (거리순 검색) |
| 4단계 | GPS 위치 획득 + 행정동 이름 변환 |
| 5단계 | 화면 UI — 홈 / 목록 / 설정 3개 화면 |
| 6단계 | API 키 관리 — 하드코딩 금지 원칙 |
| 7단계 | 사용자 피드백 반영 3가지 |
| 8단계 | ⚠️ 카테고리 중복 버그 — 가장 고생한 순간 |
| 9단계 | 테스트 13/13 + APK 배포 |

---

## 🔧 개발 환경

- **Flutter** 3.44.8 stable (Dart 3.12.2)
- **JDK** 17 (Eclipse Adoptium)
- **Android SDK** API 36
- 사용 패키지: `http`, `geolocator`, `provider`, `shared_preferences`, `url_launcher` (+ 관련 패키지까지 총 51개)

---

## 0단계. 기획 — "밖에 나가면 어디서 뭐 먹지?"

가족과 밖에 나가면 항상 같은 고민이 반복됩니다.

> 아이들은 치킨, 저는 칼국수, 와이프는 카페… 이 근처에 뭐가 있지?

맵 앱을 켜서 검색해도 되지만, **내 위치 기준으로 가까운 맛집을 카테고리별로 한눈에** 보여주는 앱이 있으면 좋겠다고 생각했습니다.

### 💡 핵심 결정 3가지

1. **카카오 로컬 API 사용** — 카테고리 검색으로 내 위치 주변 맛집을 거리순으로 조회
2. **GPS 위치 기반** — 앱을 열면 내 위치를 자동으로 잡고, 그 주변 반경 2km 검색
3. **카테고리 그리드 UI** — 홈에서 카테고리를 고르면 바로 맛집 리스트로 이동

앱 이름은 **'재찬키의 맛집 검색'**으로 정했습니다. 🍽️

---

## 1단계. 프로젝트 세팅

Android 전용 앱이라 플랫폼을 Android로만 지정해 프로젝트를 생성했습니다.

```bash
flutter create matjip-search \
  --project-name matjip_search \
  --org com.jaichanki \
  --platforms android
```

필요한 패키지 6개를 추가했습니다.

```bash
flutter pub add http geolocator provider shared_preferences url_launcher intl
```

- `http` — 카카오 API 호출
- `geolocator` — GPS 위치 획득
- `provider` — 전역 상태 관리 (API 키·현재 위치)
- `shared_preferences` — API 키 영구 저장
- `url_launcher` — 카카오맵·네이버·전화 링크 열기

앱 이름은 AndroidManifest에 **'재찬키의 맛집 검색'**으로, 위치 권한(ACCESS_FINE_LOCATION / ACCESS_COARSE_LOCATION)과 인터넷 권한을 등록했습니다.

---

## 2단계. 카테고리 모델 — 11가지 맛집 분류

카테고리를 이모지와 함께 모델로 정의했습니다. 카카오 API는 음식점(FD6)·카페(CE7) 같은 **그룹 코드**로 검색하는데, 그룹 안에서 세부 분류는 `category_name` 문자열로 구분됩니다. 그래서 `keywords`로 한식·중식 같은 키워드를 매칭하는 구조로 설계했습니다.

```dart
const List<MenuCategory> kCategories = [
  MenuCategory(name: '전체보기', emoji: '🍽️', groupCode: 'FD6'),
  MenuCategory(name: '한식', emoji: '🍚', groupCode: 'FD6', keywords: ['한식']),
  MenuCategory(name: '중식', emoji: '🥡', groupCode: 'FD6', keywords: ['중식']),
  MenuCategory(name: '일식', emoji: '🍣', groupCode: 'FD6', keywords: ['일식']),
  MenuCategory(name: '양식', emoji: '🍝', groupCode: 'FD6', keywords: ['양식']),
  MenuCategory(name: '분식', emoji: '🍢', groupCode: 'FD6', keywords: ['분식']),
  MenuCategory(name: '치킨', emoji: '🍗', groupCode: 'FD6', keywords: ['치킨']),
  MenuCategory(name: '피자·버거', emoji: '🍕', groupCode: 'FD6', keywords: ['피자', '버거']),
  MenuCategory(name: '카페·디저트', emoji: '☕', groupCode: 'CE7'),
  MenuCategory(name: '술집', emoji: '🍻', groupCode: 'FD6', keywords: ['주점']),
  MenuCategory(name: '고기·구이', emoji: '🥩', groupCode: 'FD6', keywords: ['육류', '고기']),
];
```

카페는 그룹 코드가 달라서(CE7) 키워드 없이 그룹 전체를 조회하도록 했고, 전체보기는 FD6 그룹 전체를 보여줍니다. 맛집 모델에는 가게 이름·주소·전화·거리·카카오맵 링크 등을 담았습니다.

---

## 3단계. 카카오 로컬 API 연동

카카오 로컬 API의 **카테고리 검색** 엔드포인트를 사용했습니다.

- 내 위치의 x(경도)·y(위도) 좌표 기준
- 반경 2km, `sort=distance`로 **가까운 순** 정렬
- 한 페이지 15개씩, 최대 5페이지 순회

```dart
final uri = Uri.parse('$_baseUrl/v2/local/search/category.json').replace(
  queryParameters: {
    'category_group_code': groupCode,
    'x': lng.toString(),
    'y': lat.toString(),
    'radius': radiusMeters.toString(),
    'sort': 'distance',
    'page': page.toString(),
    'size': '15',
  },
);
final response = await _client.get(uri, headers: {
  'Authorization': 'KakaoAK $kakaoKey',
});
```

여기서 **나중에 큰 사건(8단계)이 터집니다.** 일단 API 연동 자체는 순조로웠고, 에러 처리로 401(키 오류)일 때 "카카오 API 키가 올바르지 않습니다" 안내를 따로 구분해 두었습니다.

---

## 4단계. GPS 위치 + 행정동 이름

`geolocator`로 현재 위치를 가져오고, 좌표를 카카오 **좌표→행정동** API로 보내 **"서울 강남구 역삼동"** 같은 동네 이름을 표시하도록 했습니다.

위치 권한 처리도 단계별로 꼼꼼히 했습니다.

1. GPS 서비스가 꺼져 있으면 → "위치 서비스(GPS)가 꺼져 있습니다"
2. 권한이 거부되면 → 권한 요청 팝업
3. 영구 거부 상태면 → "앱 설정에서 권한을 허용해주세요"

이 메시지들은 전부 홈 화면의 위치 카드에 표시되어, 사용자가 뭘 해야 하는지 바로 알 수 있습니다.

---

## 5단계. 화면 UI — 홈 / 목록 / 설정

### 🏠 홈 화면
- 상단: 현재 위치 카드 (행정동 이름 + 새로고침 버튼)
- API 키 미설정 시 노란 안내 배너 + 설정 버튼
- 중앙: 11개 카테고리 이모지 그리드

### 📋 맛집 목록 화면
- 거리 뱃지(예: 850m, 1.2km)가 붙은 맛집 카드 리스트
- 카드를 누르면 바텀시트가 열리고 **3가지 액션** 제공:
  - **카카오맵에서 보기** — 가게 위치 확인
  - **네이버 플레이스에서 보기** — 가게명으로 웹 검색 (별도 키 불필요!)
  - **전화 걸기** — 예약할 때 유용
- 아래로 당기면 새로고침 (RefreshIndicator)

![맛집 목록 화면 — 거리순 카드 리스트](/assets/images/posts/matjip-search-devlog-20260815/3.jpg)
*맛집 목록 화면 — 가까운 순으로 정렬된 카드 리스트에 거리 뱃지(280m~392m) 표시*

네이버 플레이스는 카카오와 달리 API 키가 필요 없어서, 가게명 + 시군구를 조합한 검색 URL을 직접 열어주는 방식으로 구현했습니다.

### ⚙️ 설정 화면
카카오 REST API 키를 입력하고 저장하는 화면. 키 발급 방법 5단계도 화면 안에 친절하게 안내해 넣었습니다.

---

## 6단계. API 키 관리 — 하드코딩 금지 원칙

카카오 REST API 키는 **앱 코드에 절대 하드코딩하지 않았습니다.** 이유는 간단합니다. 하드코딩하면 APK를 뜯어보는 사람 누구나 키를 가져갈 수 있기 때문입니다.

대신 설정 화면에서 **사용자가 직접 키를 입력**하고, `shared_preferences`로 기기에 영구 저장하는 구조로 했습니다.

```dart
Future<void> saveKakaoKey(String key) async {
  final trimmed = key.trim();
  kakaoKey = trimmed;
  final prefs = await SharedPreferences.getInstance();
  await prefs.setString('kakao_api_key', trimmed);
  notifyListeners();
}
```

키는 이 앱의 로컬 저장소에만 보관되고, 서버로 전송되지 않습니다. 사용자는 카카오 개발자 콘솔(developers.kakao.com)에서 **내 애플리케이션 → 앱 키** 메뉴의 REST API 키를 복사해 붙여넣기만 하면 됩니다.

![설정 화면 — 카카오 API 키 입력](/assets/images/posts/matjip-search-devlog-20260815/1.jpg)
*설정 화면 — 개발자 표기와 함께 사용자가 직접 카카오 REST API 키를 입력하는 구조*

![카카오 API 키 발급 방법 안내](/assets/images/posts/matjip-search-devlog-20260815/2.jpg)
*설정 화면 안에 넣은 키 발급 방법 5단계 안내 (developers.kakao.com 접속부터 복사까지)*

---

## 7단계. 사용자 피드백 반영 3가지

앱을 폰에 설치해서 써보니 피드백이 들어왔습니다. 딱 3건!

### ① 카카오맵 "보기" 링크가 열리지 않음 🐛

가장 난감했던 버그였습니다. 원인은 **Android 11+의 패키지 가시성(package visibility)** 제한 때문이었는데요.

- `canLaunchUrl()`로 미리 확인하면 Android 11+에서 false를 반환 (실제로는 열 수 있는데도!)
- 매니페스트에 `queries`로 열고자 하는 스킴을 등록해야 함

해결책은 두 가지입니다.
1. 매니페스트에 `tel`, `https`, `http` 스킴을 `queries`로 등록
2. `canLaunchUrl()` 확인 없이 `launchUrl()`을 바로 호출 (실패 시 예외 처리)

추가로 카카오맵 링크가 `http://`로 오는 경우가 있어서, 열기 전에 `https://`로 치환하는 방어 코드도 넣었습니다.

### ② 네이버 플레이스 보기 버튼 추가

"카카오맵 말고 네이버 지도로도 보고 싶다"는 의견에, 바텀시트에 네이버 플레이스 버튼을 추가했습니다. 가게명 + 시군구를 조합해 `m.place.naver.com` 검색 URL을 여는 방식이라 별도 키가 필요 없습니다.

### ③ 개발자 표기 추가

앱 상단에 **"개발자: 심종주"**를 표기해서, 누가 만들었는지 한눈에 보이도록 했습니다.

---

## 8단계. ⚠️ 카테고리 중복 버그 — 가장 고생한 순간

2차 APK를 배포한 뒤 이런 피드백이 들어왔습니다.

> **"카테고리별 리스트에 같은 가게가 반복해서 보여요"**

### 첫 번째 시도: 가설 세우기

범인 후보를 3가지로 좁혔습니다.

| 가설 | 내용 | 판정 |
|:----|:----|:----|
| ① | 키워드/그룹 필터 교집합이 겹친다? | ❌ |
| ② | 리스트에 append하면서 중복 누적? | ❌ |
| ③ | API 페이지네이션에 문제가 있다? | ✅ |

### 두 번째 시도: 실측 재현

가설이 아니라 **실제 데이터로 확인**하기로 했습니다. Python 스크립트로 카카오 API를 직접 5페이지 호출해서 결과를 분석했습니다.

충격적인 결과가 나왔습니다.

- 5페이지에서 받은 **75건 중 고유한 가게는 단 25건**
- 같은 가게가 최대 **3번씩 반복** 등장
- 페이지 1~3은 각각 15건씩 새 가게가 나오는데, **페이지 4부터는 페이지 3과 완전히 동일한 결과가 반복!**

즉, 카카오 로컬 API는 `sort=distance`로 검색할 때 **페이지 4 이후부터 같은 결과를 반복 반환**합니다. 앱 로직 버그가 아니라 **API의 페이지네이션 함정**이었습니다. 5페이지를 무조건 순회하도록 짠 제 코드가 그 함정을 그대로 드러낸 것이죠.

### 세 번째 시도: 해결 로직

두 가지를 함께 적용했습니다.

1. **place id 기준 중복 제거** — Set에 가게 id를 기록해 처음 본 가게만 리스트에 추가
2. **조기 중단** — 필터 적용 전에, 한 페이지가 전부 이미 본 가게로만 구성되어 있으면 이후 페이지도 반복일 것이므로 즉시 break

이때 핵심 포인트가 하나 있습니다. **"이미 본 가게" 판단(seenIds)은 카테고리 필터와 분리**해야 한다는 것. 필터에 걸려서 리스트에 못 들어간 가게도 "본 적 있는 가게"로 기록해야 반복 페이지 감지가 정확해집니다.

```dart
final results = <Restaurant>[];
final seenIds = <String>{};
for (var page = 1; page <= 5; page++) {
  final docs = await _fetchCategoryPage(...);
  // 필터 전 기준: 전부 이미 본 가게 → 이후 페이지도 반복이므로 중단
  final allSeen = docs.every((d) => seenIds.contains(d['id']?.toString()));
  for (final doc in docs) {
    final restaurant = Restaurant.fromJson(doc);
    // seenIds에는 필터와 무관하게 모든 id를 기록
    if (seenIds.add(restaurant.id) &&
        category.matches(restaurant.categoryName)) {
      results.add(restaurant);
    }
  }
  if (docs.length < 15 || allSeen) break; // 마지막 페이지도 여기서 처리
}
```

### 검증: 테스트 + 실데이터

- `flutter analyze` → **No issues found**
- `flutter test` → **13/13 통과** (MockClient로 API 응답을 흉내 낸 단위 테스트 3개 추가)
- **실데이터 검증** — 수정 로직을 실제 카카오 응답에 적용한 결과:

| 카테고리 | 수정 전 | 수정 후 (고유) | 제거된 중복 |
|:----|:----|:----|:----|
| 전체보기 | 60건 | 45건 | 15건 |
| 한식 | 60건 | 21건 | 39건 |
| 치킨 | 60건 | 1건 | 59건 |
| 카페 | 60건 | 45건 | 15건 |

- **동작 검증** — 실제 API로 5페이지를 순회하는 시나리오에서 수정 로직 적용 시: 고유 45건, 중복 0건, **페이지 4에서 조기 중단** (API 호출 5회 → 4회, 통신 비용도 절약!)

치킨이 60건에서 1건이 된 건 카테고리 필터 때문에 원래 그런 것이고, 중복 30건이 싹 사라진 게 핵심입니다. 🎉

---

## 9단계. 테스트 13/13 + APK 배포

### 빌드 이력

하루 동안 3차례 빌드했습니다.

| 차수 | 시각 | APK 크기 | 비고 |
|:----|:----|:----|:----|
| 1차 | 22:44 | 49,997,361 bytes | 최초 배포 |
| 2차 | 23:14 | 50,013,861 bytes | 피드백 3건 반영 |
| 3차 | 23:24 | 50,013,861 bytes | 중복 버그 수정 |

### 재미있었던 에피소드: "크기가 똑같은데?"

3차 APK가 나왔는데 **2차와 파일 크기가 정확히 같았습니다(50,013,861 bytes).** 순간 "빌드가 새 코드를 안 반영한 건가?" 싶었는데, 파일 크기는 우연히 같을 수 있으니 **SHA-256 해시로 비교**했습니다.

- 2차 APK: `6c47f...`
- 3차 APK: `c40ba...`

**해시가 다르다 = 새 코드가 확실히 반영됐다.** 크기 비교 대신 해시로 검증한 덕분에 안심하고 배포할 수 있었습니다.

### 최종 상태

- `flutter analyze` — 이슈 0건
- `flutter test` — **13/13 통과** (모델 파싱·거리 표시·네이버 URL 생성·API 중복 제거·화면 스모크 테스트)
- 3차 APK — **50,013,861 bytes** (sha256 `c40ba...`)

---

## 🎉 마무리 — 이렇게 쓰고 있습니다

설치하고 나면 이렇게 씁니다.

1. ⚙️ 설정 → 카카오 REST API 키 입력 → 저장
2. 🏠 홈 화면에서 위치 새로고침 (행정동 이름 확인)
3. 🍗 카테고리 선택 → 주변 2km 맛집이 거리순으로 표시
4. 📞 마음에 드는 가게를 누르면 카카오맵·네이버·전화 중 선택

이번 프로젝트에서 가장 크게 배운 점은 **"버그는 추측하지 말고 실측하자"**였습니다. 중복 버그를 코드를 들여다보며 추측만 했다면 한참을 헤맸을 텐데, 실제 API 응답을 받아 페이지별로 분석하니 원인이 5분 만에 명확해졌거든요.

> 📌 추측하지 말고 실측하자 — 실제 데이터가 답을 알고 있다

Flutter로 앱을 만들어 보고 싶은 분들께 조금이라도 도움이 되었으면 좋겠습니다. 다음엔 어떤 앱을 만들지… 벌써부터 고민 중입니다. 😄
