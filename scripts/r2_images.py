#!/usr/bin/env python3
"""블로그 이미지 → Cloudflare R2 업로드 + CDN URL 치환

사용:
  python3 scripts/r2_images.py <파일> [<파일> ...]   # 업로드 후 CDN URL 출력
  python3 scripts/r2_images.py --staged              # pre-commit 훅용 (자동 마이그레이션)
  python3 scripts/r2_images.py --check               # 자격증명/버킷 확인

자격증명: ~/.hermes/r2_credentials.json  (저장소에 커밋하지 않음)
"""
import os, sys, json, mimetypes, subprocess

REPO = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
CRED = os.path.expanduser('~/.hermes/r2_credentials.json')
CACHE = 'public, max-age=31536000, immutable'
EXTRA = {'.jpg': 'image/jpeg', '.jpeg': 'image/jpeg', '.png': 'image/png', '.webp': 'image/webp',
         '.gif': 'image/gif', '.svg': 'image/svg+xml', '.ico': 'image/x-icon',
         '.mp4': 'video/mp4', '.pdf': 'application/pdf', '.zip': 'application/zip',
         '.apk': 'application/vnd.android.package-archive'}
UPLOAD_DIRS = ('assets/images/', 'assets/downloads/')


def creds():
    with open(CRED) as f:
        return json.load(f)


def client():
    import boto3
    from botocore.config import Config
    c = creds()
    return boto3.client('s3', endpoint_url=f"https://{c['account_id']}.r2.cloudflarestorage.com",
                        aws_access_key_id=c['access_key_id'],
                        aws_secret_access_key=c['secret_access_key'],
                        region_name='auto',
                        config=Config(signature_version='s3v4', max_pool_connections=8,
                                      retries={'max_attempts': 4}))


def upload(s3, local_path, key=None):
    """업로드 후 CDN URL 반환. key 기본값은 저장소 기준 상대경로."""
    c = creds()
    key = key or os.path.relpath(os.path.abspath(local_path), REPO).replace(os.sep, '/')
    ext = os.path.splitext(local_path)[1].lower()
    ct = EXTRA.get(ext) or mimetypes.guess_type(local_path)[0] or 'application/octet-stream'
    with open(local_path, 'rb') as fh:
        s3.put_object(Bucket=c['bucket'], Key=key, Body=fh,
                      ContentType=ct, CacheControl=CACHE)
    return f"{c['public_base'].rstrip('/')}/{key}"


def git(*args):
    return subprocess.run(['git'] + list(args), cwd=REPO, capture_output=True, text=True)


def staged_names():
    return [x for x in git('diff', '--cached', '--name-only', '--diff-filter=AM').stdout.splitlines() if x]


def migrate_staged():
    """스테이징된 이미지를 R2로 올리고, 스테이징된 글의 경로를 CDN URL로 바꾼다."""
    c = creds()
    base = c['public_base'].rstrip('/')
    names = staged_names()
    imgs = [p for p in names if p.startswith(UPLOAD_DIRS) and os.path.isfile(os.path.join(REPO, p))]
    if not imgs:
        return 0

    s3 = client()
    mapping = {}
    for p in imgs:
        try:
            mapping['/' + p] = upload(s3, os.path.join(REPO, p), p)
        except Exception as e:
            print(f"  ⚠️ 업로드 실패 {p}: {str(e)[:80]}")
    if not mapping:
        return 0

    touched = []
    for p in [x for x in names if x.endswith(('.md', '.html'))]:
        fp = os.path.join(REPO, p)
        if not os.path.isfile(fp):
            continue
        t = open(fp, encoding='utf-8', errors='ignore').read()
        o = t
        for local, url in mapping.items():
            t = t.replace(local, url)
        if t != o:
            open(fp, 'w', encoding='utf-8').write(t)
            touched.append(p)

    git('rm', '--cached', '-q', '--', *imgs)
    for p in imgs:
        try:
            os.remove(os.path.join(REPO, p))
        except OSError:
            pass
    if touched:
        git('add', '--', *touched)

    print(f"🖼  R2 업로드 {len(mapping)}개 → CDN 경로 치환 {len(touched)}개 글")
    return len(mapping)


def main():
    args = sys.argv[1:]
    if '--check' in args:
        c = creds()
        s3 = client()
        s3.list_objects_v2(Bucket=c['bucket'], MaxKeys=1)
        print(f"✅ R2 연결 OK — bucket={c['bucket']} / {c['public_base']}")
        return 0
    if '--staged' in args:
        migrate_staged()
        return 0
    files = [a for a in args if not a.startswith('--')]
    if not files:
        print(__doc__)
        return 1
    s3 = client()
    for f in files:
        print(upload(s3, f))
    return 0


if __name__ == '__main__':
    sys.exit(main())
