# 블로그 게시 규칙

- 작성한 글은 `_posts/` 폴더에 markdown 파일로 저장하세요.
- 파일명 규칙: `YYYY-MM-DD-{slug}.md`
- frontmatter: layout, title, date, categories, tags, author, description, image
- **작성 즉시 git add → commit → push까지 완료**하고 GitHub Pages에 배포하세요.
- 게시 후 라이브 URL을 보고하세요.

## 🚫 절대 금지 — 포스팅 자동화 언급

글의 **본문·제목·description·태그 어디에도** 아래 내용을 쓰지 마세요.

**금지 표현 (예시):**
- "자동 포스팅", "자동포스팅", "포스팅 자동화", "블로그 자동화"
- "자동 발행", "자동 게시", "무인 발행"
- "이 블로그의 모든 포스팅이 자동으로 올라갑니다"
- "지금 이 글도 크론잡이 썼습니다"
- "이 블로그 자체가 실사용 테스트베드입니다"
- "제 블로그 크론잡 설정은 …" (크론 설정·스킬 목록 노출)

**허용 표현 (이렇게 쓰세요):**
- "AI 에이전트를 실사용하고 있습니다"
- "크론잡을 돌리고 있습니다"
- "자동화 파이프라인을 운영합니다"

**중요:** 다른 자동화(업무 자동화, AI 에이전트의 자동화 기능 소개, 봇 모드, 스케줄러 기능 설명 등)는 자유롭게 다뤄도 됩니다.
**포스팅 자동화(블로그 글이 자동으로 작성·게시된다는 내용)만 금지**입니다.

## 🖼️ 이미지 정책 (필수) — Cloudflare R2 CDN

**이미지는 저장소에 커밋하지 않습니다.** 저장소에는 텍스트만 두고, 이미지는 R2에서 서빙합니다.
(2026-11-10 이전: 저장소에 원본 이미지 누적 → `_site` 562MB → 배포 타임아웃 반복. 현재 `_site` 약 40MB)

**업로드 방법 (권장):**
```bash
python3 scripts/r2_images.py <이미지파일>       # 업로드 후 CDN URL 출력
```

**글에 넣는 URL 형식:**
```
https://pub-a8e00ddf355f47339bd2e01ff3221de7.r2.dev/assets/images/posts/<slug>/<파일>
```
> `/assets/images/...` 같은 **로컬 경로를 쓰지 마세요.** CDN URL을 그대로 붙입니다.

**저장 전 압축 (업로드 전 권장):**
| 항목 | 규칙 |
|---|---|
| 긴 변 | 최대 **1600px** |
| JPEG | **quality 85**, progressive |
| 파일당 | 목표 **300KB**, 최대 **500KB** |
| 금지 | 영상(.mp4/.mov)·APK·ZIP·PDF는 저장소/R2에 올리지 말고 외부 링크 |

```bash
python3 scripts/optimize_images.py <파일>     # 압축만
```

**자동 방어 (`.git/hooks/pre-commit`):**
- `assets/images/`, `assets/downloads/` 에 스테이징된 파일을 **자동 압축 → R2 업로드 → 글의 경로를 CDN URL로 치환 → 저장소에서 제외**
- 즉 로컬 경로로 커밋해도 자동으로 CDN으로 바뀝니다. 커밋을 막지는 않습니다.

**자격증명:** `~/.hermes/r2_credentials.json` (저장소에 커밋 금지)
**확인:** `python3 scripts/r2_images.py --check`

**카드 썸네일 잘림 방지 — `card_image` (선택)**
목록·카테고리 카드 박스는 **280×261 (비율 1.072)** 이고 `object-fit: cover`라서, 2:1 대표이미지는 좌우 약 48%가 잘립니다. 글자·로고가 좌우에 있으면 잘려 보입니다.

```
card_image: https://pub-a8e00ddf355f47339bd2e01ff3221de7.r2.dev/assets/images/posts/<slug>/00-card.jpg
image:      (기존 대표이미지 — 글 상단 히어로·OG에 사용)
```

- `card_image`가 있으면 **카드(홈·카테고리·관련글)에서만** 이 이미지를 씁니다. 없으면 `image`로 자동 대체됩니다.
- 카드용은 **1.072:1 비율(권장 560×522)** 로 만들고, **글자·로고가 잘리지 않는 영역**을 잡으세요.
  ```python
  from PIL import Image
  im = Image.open('00-hero.jpg')
  cw = round(im.height * 280/261)          # 카드 비율
  im.crop((0, 0, cw, im.height)).resize((560,522), Image.LANCZOS).save('00-card.jpg','JPEG',quality=90)
  ```


## 🚫 배포 실패 시 — 재시도 커밋 금지

배포(Pages 빌드/Actions)가 실패하거나 오래 걸려도 **재시도용 커밋을 만들지 마세요.** 배포는 `main` push 시 자동으로 다시 실행됩니다.

**금지:**
- `chore: Pages 재배포 트리거`, `🔁 Pages 배포 재시도` 같은 커밋
- `git commit --allow-empty`로 빌드를 유도하는 행위
- 같은 내용을 다시 push 하는 행위

**대신:**
- 포스트 파일과 이미지는 push까지 완료하고 종료한다
- 배포 실패는 **사실과 오류 메시지만 보고**한다 (예: `deploy 단계 Timeout reached`)
- 사이트는 이전 배포본을 계속 서빙하므로 급하지 않다
- 수동 재배포가 필요하면 **직접 커밋하지 말고 사용자에게 알린다** (Hermes가 GitHub Actions API로 대신 실행할 수 있습니다)


