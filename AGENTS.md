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

## 🖼️ 이미지 용량 규칙 (필수)

GitHub Pages(legacy) 빌드는 **사이트 전체를 업로드**하므로 용량이 커지면 10분 제한으로 빌드가 실패합니다. 실제로 966MB까지 누적되어 빌드가 반복 실패한 이력이 있습니다.

**저장 전 반드시 지킬 것:**

| 항목 | 규칙 |
|---|---|
| 긴 변 | **최대 1600px** (초과 시 축소) |
| JPEG | **quality 85**, progressive |
| 파일당 크기 | 목표 **300KB**, 경고 **500KB** |
| PNG | 700KB 초과 시 256색 양자화 |
| 금지 | 영상(.mp4/.mov)·압축파일(.apk/.aab/.zip)·PDF를 저장소에 커밋 |

**압축 방법 (필수 실행):**
```bash
python3 scripts/optimize_images.py              # 저장소 전체 스캔·압축
python3 scripts/optimize_images.py --staged     # 스테이징된 파일만
python3 scripts/optimize_images.py --report     # 현황만 확인
```

**자동 방어:** `.git/hooks/pre-commit`가 스테이징된 이미지를 자동 압축합니다(커밋은 막지 않음).

**영상·APK가 필요하면:** 저장소 대신 YouTube / Google Drive / GitHub Releases 링크를 사용하세요.

**용량 확인:** `du -sh assets` — 600MB를 넘으면 정리가 필요합니다.

