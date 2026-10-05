/* 네온 레인 서비스워커: 네트워크 우선, 실패 시 캐시(오프라인 실행) */
const V='neon-lane-v2';
const ASSETS=['./','./index.html','./manifest.webmanifest','./firebase-config.js','./team-logo.png','./icon-192.png','./icon-512.png','./apple-touch-icon.png'];
self.addEventListener('install',e=>{e.waitUntil(caches.open(V).then(c=>c.addAll(ASSETS)).then(()=>self.skipWaiting()))});
self.addEventListener('activate',e=>{e.waitUntil(caches.keys().then(ks=>Promise.all(ks.filter(k=>k!==V).map(k=>caches.delete(k)))).then(()=>self.clients.claim()))});
self.addEventListener('fetch',e=>{
  if(e.request.method!=='GET'||new URL(e.request.url).origin!==location.origin)return;
  e.respondWith(fetch(e.request).then(r=>{if(r.ok){const c=r.clone();caches.open(V).then(x=>x.put(e.request,c))}return r})
    .catch(()=>caches.match(e.request).then(m=>m||caches.match('./index.html'))));
});
