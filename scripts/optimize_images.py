#!/usr/bin/env python3
"""블로그 이미지 최적화 — 누적 방지용 공용 스크립트

규칙: 긴 변 최대 1600px, JPEG q85, 파일당 목표 300KB(경고 500KB)
사용:
  python3 scripts/optimize_images.py                # 저장소 전체 스캔 후 초과분 자동 압축
  python3 scripts/optimize_images.py --staged       # git staged 이미지만 (pre-commit 훅용)
  python3 scripts/optimize_images.py --report       # 압축 없이 현황만
  python3 scripts/optimize_images.py path1 path2    # 지정 파일
"""
import os, sys, subprocess
from PIL import Image

Image.MAX_IMAGE_PIXELS = None
MAX_SIDE = 1600
Q = 85
WARN_KB = 500
TARGET_KB = 300
EXTS = ('.jpg', '.jpeg', '.png', '.webp')
REPO = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
ASSETS = os.path.join(REPO, 'assets')
try:
    RESAMPLE = Image.Resampling.LANCZOS
except AttributeError:          # Pillow < 9.1
    RESAMPLE = Image.LANCZOS  # type: ignore[attr-defined]
MC = getattr(getattr(Image, 'Quantize', None), 'MEDIANCUT', 0)
FS = getattr(getattr(Image, 'Dither', None), 'FLOYDSTEINBERG', 1)


def optimize(path, dry=False):
    """반환: (before_bytes, after_bytes) — 변경 없으면 (b, b)"""
    b = os.path.getsize(path)
    try:
        im = Image.open(path)
        im.load()
    except Exception:
        return b, b
    w, h = im.size
    over_side = max(w, h) > MAX_SIDE
    over_size = b > WARN_KB * 1024
    if not over_side and not over_size:
        return b, b
    if dry:
        return b, b
    ext = os.path.splitext(path)[1].lower()
    try:
        if over_side:
            sc = MAX_SIDE / max(w, h)
            im = im.resize((int(w * sc), int(h * sc)), RESAMPLE)
        if ext in ('.jpg', '.jpeg'):
            if im.mode not in ('RGB', 'L'):
                im = im.convert('RGB')
            im.save(path, 'JPEG', quality=Q, optimize=True, progressive=True)
        elif ext == '.png':
            # 큰 PNG는 256색 양자화 (스크린샷·차트에 효과적, 30% 이상 줄 때만 적용)
            if b > 700 * 1024 and im.mode in ('RGB', 'P') and not over_side:
                q = im.convert('RGB').quantize(colors=256, method=MC, dither=FS)
                q.save(path, 'PNG', optimize=True)
                if os.path.getsize(path) > b * 0.75:
                    im.save(path, 'PNG', optimize=True)   # 효과 적으면 원본 유지
            else:
                im.save(path, 'PNG', optimize=True)
        else:
            im.save(path, 'WEBP', quality=Q, method=6)
    except Exception:
        return b, b
    a = os.path.getsize(path)
    if a >= b:                      # 커지면 원본 유지
        return b, b
    return b, a


def iter_assets():
    for root, _, files in os.walk(ASSETS):
        for f in files:
            if f.lower().endswith(EXTS):
                yield os.path.join(root, f)


def staged_images():
    out = subprocess.run(['git', 'diff', '--cached', '--name-only', '--diff-filter=AM'],
                         cwd=REPO, capture_output=True, text=True).stdout
    return [os.path.join(REPO, p) for p in out.splitlines()
            if p.lower().endswith(EXTS) and os.path.isfile(os.path.join(REPO, p))]


def main():
    args = [a for a in sys.argv[1:] if not a.startswith('--')]
    dry = '--report' in sys.argv
    if '--staged' in sys.argv:
        targets = staged_images()
    elif args:
        targets = [os.path.abspath(a) for a in args]
    else:
        targets = list(iter_assets())

    tb = ta = n = 0
    big = []
    for p in targets:
        b, a = optimize(p, dry=dry)
        tb += b
        ta += a
        if a < b:
            n += 1
        if a > WARN_KB * 1024:
            big.append((a, p))

    saved = (tb - ta) / 1024 / 1024
    print(f"대상 {len(targets)}개 · 압축 {n}개 · {tb/1024/1024:.1f}MB → {ta/1024/1024:.1f}MB (−{saved:.1f}MB)")
    if big:
        print(f"\n⚠️  {WARN_KB}KB 초과 {len(big)}개 (권장 {TARGET_KB}KB):")
        for a, p in sorted(big, reverse=True)[:10]:
            print(f"   {a/1024:7.0f}KB  {os.path.relpath(p, REPO)}")
    return 0


if __name__ == '__main__':
    sys.exit(main())
