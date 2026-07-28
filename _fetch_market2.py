#!/usr/bin/env python3
"""Fetch KOSPI July 28 market data from Yahoo Finance and related articles."""
import urllib.request, re, json

def fetch(url):
    req = urllib.request.Request(url, headers={
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36',
        'Accept': 'text/html,application/xhtml+xml',
        'Accept-Language': 'ko-KR,ko;q=0.9,en;q=0.8'
    })
    resp = urllib.request.urlopen(req, timeout=20)
    html = resp.read().decode('utf-8', errors='replace')
    text = re.sub(r'<script[^>]*>.*?</script>', '', html, flags=re.DOTALL)
    text = re.sub(r'<style[^>]*>.*?</style>', '', text, flags=re.DOTALL)
    text = re.sub(r'<[^>]+>', ' ', text)
    text = re.sub(r'&[a-z]+;', ' ', text)
    text = re.sub(r'\s+', ' ', text).strip()
    return text[:6000]

for url in [
    'https://www.ksat.com/business/2026/07/28/south-koreas-kospi-index-sinks-more-than-10-on-heavy-selling-of-chipmaking-stocks/',
    'https://www.clickorlando.com/business/2026/07/28/south-koreas-kospi-index-sinks-more-than-9-on-heavy-selling-of-chipmaking-stocks/'
]:
    try:
        print(f'=== {url} ===')
        text = fetch(url)
        # Find the first substantial paragraph
        lines = text.split('. ')
        for i, line in enumerate(lines[:30]):
            if len(line) > 60 and ('Kospi' in line or 'KOSPI' in line or 'index' in line.lower() or 'point' in line.lower() or 'percent' in line.lower()):
                print(f'  {line[:300]}')
        print()
    except Exception as e:
        print(f'Error: {e}')
