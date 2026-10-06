// 웹 게임(../../games/neon-lane)을 www/ 로 복사. --release 이면 ads-config.js 의 test 를 false 로 바꿉니다.
import { cpSync, rmSync, mkdirSync, readFileSync, writeFileSync } from 'node:fs';
import { fileURLToPath } from 'node:url';
import { dirname, join } from 'node:path';
const root = join(dirname(fileURLToPath(import.meta.url)), '..');
const src = join(root, '..', '..', 'games', 'neon-lane');
const out = join(root, 'www');
rmSync(out, { recursive: true, force: true });
mkdirSync(out, { recursive: true });
for (const f of ['index.html', 'firebase-config.js', 'ads-config.js', 'team-logo.png', 'icon-192.png', 'icon-512.png', 'apple-touch-icon.png'])
  cpSync(join(src, f), join(out, f));
if (process.argv.includes('--release')) {
  const p = join(out, 'ads-config.js');
  const t = readFileSync(p, 'utf8');
  if (!/interstitial:\s*'ca-app-pub-/.test(t) || !/banner:\s*'ca-app-pub-/.test(t)) {
    console.error('release 빌드: games/neon-lane/ads-config.js 에 실제 AdMob 단위 ID를 먼저 채우세요.');
    process.exit(1);
  }
  writeFileSync(p, t.replace('test: true', 'test: false'));
}
console.log('web synced →', out);
