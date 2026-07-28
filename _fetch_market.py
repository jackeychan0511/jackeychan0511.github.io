#!/usr/bin/env python3
import urllib.request, urllib.error, sys, re

urls = [
    'https://www.newspim.com/news/view/20260728001044',
    'https://codingzero.tistory.com/entry/kospi-crash-rebound-outlook-samsung-sk-hynix-july-29-2026'
]
for url in urls:
    try:
        req = urllib.request.Request(url, headers={'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'})
        resp = urllib.request.urlopen(req, timeout=15)
        html = resp.read().decode('utf-8', errors='replace')
        text = re.sub(r'<[^>]+>', ' ', html)
        text = re.sub(r'\s+', ' ', text).strip()
        print(f'=== {url} ===')
        print(text[:5000])
        print()
    except Exception as e:
        print(f'Error with {url}: {e}')
        print()
