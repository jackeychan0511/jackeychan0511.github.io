# [Gemini에게 보낼 프롬프트 — 아래 전체를 복사해서 붙여넣으세요]

너는 시니어 웹 게임 그래픽 엔지니어야. 아래는 내가 만든 모바일 웹 물리 머지 퍼즐 "Blob Drop"(수박게임 계열)의 **그래픽 관련 코드**야.
게임은 단일 index.html(Canvas 2D + Matter.js 물리)이고, 몽글이(슬라임)는 Matter.js **원형 바디**로 충돌하며 화면 그림은 `drawBlob`이 따로 그려.
현재 그림체는 "말랑 젤리"(방사형 그라데이션 + 하이라이트 + 점눈 + 볼터치 카와이 2D)야.

## 목표
그래픽을 **반3D(2.5D)** 로 업그레이드해줘. (구체감 있는 라이팅, 젤리의 반투명/서브서피스 느낌, 림라이트, 바닥 그림자/접촉 그림자, 배경 시차·원근 등)

## 반드시 지켜야 할 제약
1. **물리는 건드리지 마.** 화면 실루엣은 반지름 `RAD[level]`의 원(+ `o.k`,`o.ka` 찌그러짐 변형)과 일치해야 해. 3D처럼 보여도 윤곽이 원을 벗어나면 안 돼.
2. `drawBlob(ctx,x,y,r,l,o)`의 **시그니처 유지**. o = {k:찌그러짐 정도, ka:찌그러짐 방향(rad), s:팝 스케일, a:회전(rad), happy:행복한 눈 여부}. 도감/다음 미리보기/목표 아이콘에서도 같은 함수를 써(아래 호출 예시 참고).
3. **단일 파일 유지, 외부 이미지 에셋 금지**(필요하면 코드로 생성하거나 data URI). 로딩 실패해도 게임이 떠야 함.
4. **성능**: 모바일에서 몽글이 20~40개 + 배경 애니 + 파티클이 동시에 돌아가. 몽글이마다 무거운 연산 금지. 권장: (레벨×테마) 별로 오프스크린 캔버스에 **스프라이트를 1회 렌더 후 캐시**하고 `drawImage`로 변형(찌그러짐·회전·스케일)만 적용. 눈/입처럼 상태에 따라 바뀌는 부분(happy)은 레이어로 분리.
5. **색은 `THEMES`에서만** 읽어(하드코딩 금지). 테마 6종(별똥 밤하늘/우주/식당/겨울/봄/할로윈) 모두 자연스럽게 보여야 해. 테마 교체 시 캐시를 무효화할 것.
6. 11단계가 **한눈에 구분**돼야 해(색+배지 이모지+크기). 반3D 효과가 색 구분을 해치면 안 돼.
7. 논리 좌표계는 360×560 고정이고 `SCENES[...].base(c,th)`는 오프스크린(2배 해상도)에 1회만 그리고 `anim(c,now)`가 매 프레임 그려. 이 구조 유지(배경 시차를 주려면 base를 레이어로 쪼개도 됨).
8. 마스코트(`drawMascot`)도 같은 톤으로 반3D화.

## 출력 형식 (중요)
- **바꾼 함수/상수만 "통째로 교체 가능한 형태"** 로 줘: `drawBlob`(및 필요한 캐시/헬퍼), `SCENES`(필요한 부분), `drawScene`, `drawMascot`.
- 새 헬퍼가 필요하면 어디에 추가하는지 명시. 기존 코드 중 **삭제/변경해야 할 줄**이 있으면 정확히 짚어줘.
- 각 변경이 성능에 주는 영향(예상 프레임 비용)과, 기존 로직(`render`, 도감, 미리보기)과 충돌하지 않는다는 근거를 짧게 설명해줘.
- 변경 전/후를 비교할 수 있도록 **로컬에서 확인하는 방법**(어떤 값만 바꿔보면 되는지)도 알려줘.

---

## 1) 테마 데이터 (THEMES) — 단계별 [이름, 밝은색, 어두운색, 배지 이모지, 설명]
```js
const THEMES={
  jelly:{name:'별똥 밤하늘',scene:'night',bg:['#2d2560','#171238'],levels:[
    ['반짝콩','#ff9aa2','#e0707c','·','별똥이 부서져 생긴 가장 작은 조각. 아직 말이 서툴러요.'],
    ['별방울','#ffb86b','#e28a3a','💧','밤이슬을 머금고 톡 터질 듯 말랑해졌어요.'],
    ['달빛몽','#ffe066','#d9b72a','☁️','달빛을 쬐며 낮잠 자는 걸 제일 좋아해요.'],
    ['클로버몽','#b8e986','#82bd4f','🍀','별똥이 떨어진 풀밭에서 행운을 주워 왔어요.'],
    ['물결몽','#6fe3c1','#38b393','🌊','밤바다의 물결을 따라 흘러온 수다쟁이.'],
    ['샛별몽','#6cc3ff','#3590d6','⭐','하늘에서 가장 먼저 반짝이는 별을 닮았어요.'],
    ['초승몽','#8d8cff','#5c5bd6','🌙','초승달 모자를 쓰고 하늘길을 안내해요.'],
    ['신비몽','#c38bff','#9556d6','🔮','별자리의 비밀을 알고 있다고 소문났어요.'],
    ['오로라몽','#ff8bd8','#d654aa','✨','몸에서 오로라가 흘러나오는 하늘 직전의 몽글이.'],
    ['왕별몽','#ff6b6b','#c93c3c','👑','모든 몽글이의 존경을 받는 별의 임금님.'],
    ['별의 정령','#fff1a8','#f5b700','🌈','모두가 하나로 합쳐져 마침내 하늘로 돌아가는 전설의 몽글이.']]},
  space:{name:'우주 정거장',scene:'space',bg:['#101a3d','#0a1129'],levels:[
    ['먼지몽','#9aa5c4','#6c7899','·','별똥이 우주로 흩어지며 생긴 반짝이는 먼지.'],
    ['운석몽','#b08968','#7f5d41','☄️','딱딱해 보여도 속은 말랑한 떠돌이 운석.'],
    ['달조각몽','#e5e5e5','#a9a9a9','🌑','달에서 떨어져 나온 조각. 늘 달이 그립대요.'],
    ['위성몽','#7ec8e3','#4a9cba','🛰️','정거장 주위를 빙글빙글 도는 수다쟁이.'],
    ['화성몽','#ff7b54','#c9512c','🔴','붉은 볼이 매력인 불꽃 같은 성격.'],
    ['목성몽','#f2b66d','#c68a3c','🟠','줄무늬 배가 자랑인 덩치 큰 형님.'],
    ['토성몽','#f6e27f','#c8b24e','🪐','고리를 돌리며 노는 걸 좋아해요.'],
    ['푸른별몽','#4f9dff','#2a6ed1','🌍','바다와 구름을 품은 포근한 별.'],
    ['항성몽','#ffd23f','#e09b00','☀️','스스로 빛나는 따뜻한 몽글이.'],
    ['성운몽','#d36bff','#9a3bd1','🌌','별이 태어나는 요람을 지키는 몽글이.'],
    ['은하의 정령','#ffffff','#a0b4ff','🌀','모든 별을 품고 하늘로 돌아가는 전설의 몽글이.']]},
  food:{name:'몽글 식당',scene:'food',bg:['#4a2a2a','#321c1c'],levels:[
    ['깨알몽','#d9c7a3','#a8946b','·','별똥 부스러기가 깨소금이 된 몽글이.'],
    ['콩몽','#a3c76e','#74994a','🫘','밭에서 데굴데굴 굴러온 콩.'],
    ['토마토몽','#ff6b5a','#c93f31','🍅','새빨간 볼이 터질 것 같아요.'],
    ['당근몽','#ff9f43','#d0741b','🥕','토끼들의 인기 스타.'],
    ['달걀몽','#fff2cc','#d9c27a','🥚','아침마다 병아리 꿈을 꿔요.'],
    ['만두몽','#f3e1c4','#c9a96e','🥟','속이 꽉 찬 든든한 친구.'],
    ['김밥몽','#5b7f4a','#3c5a2f','🍙','소풍 날의 주인공.'],
    ['피자몽','#ffc857','#d99a1f','🍕','치즈가 쭉 늘어나는 파티 대장.'],
    ['케이크몽','#ffadd2','#d96fa3','🍰','생일마다 불려 나오는 달콤한 몽글이.'],
    ['잔치몽','#e85d75','#b73a50','🍱','한 상 가득 차려진 마을 잔치의 사회자.'],
    ['황금식탁의 정령','#ffe27a','#e0a800','🏆','모두가 배불러지면 하늘로 돌아가는 전설의 몽글이.']]},
  winter:{name:'눈꽃 겨울밤',scene:'winter',bg:['#16294f','#0c1730'],levels:[
    ['눈알몽','#e6f2ff','#a9c7e6','·','눈송이가 처음 내려앉은 작은 알갱이.'],
    ['눈송이몽','#cfe8ff','#8fb9e0','❄️','하늘에서 천천히 내려온 첫 번째 친구.'],
    ['털장갑몽','#ff8a8a','#d65a5a','🧤','언제나 손을 따뜻하게 해줘요.'],
    ['목도리몽','#ffb347','#d98a1f','🧣','길쭉한 목도리가 자랑이에요.'],
    ['귤몽','#ffa94d','#d6791a','🍊','겨울엔 역시 귤! 상큼 달콤.'],
    ['눈사람몽','#ffffff','#b9cde0','⛄','당근 코가 살짝 삐뚤어졌어요.'],
    ['트리몽','#5fcf80','#2f9c54','🎄','반짝이는 전구를 두르고 있어요.'],
    ['종소리몽','#ffd966','#d9a91f','🔔','딸랑딸랑 겨울밤을 깨워요.'],
    ['선물몽','#ff6b81','#c93c52','🎁','안에 뭐가 들었는지 아무도 몰라요.'],
    ['오로라몽','#7ef0d0','#2fb59a','🌌','겨울 하늘에 초록 커튼을 드리워요.'],
    ['겨울별의 정령','#fff7c2','#f5c542','🌟','첫눈이 내리는 밤, 하늘로 돌아가는 전설의 몽글이.']]},
  spring:{name:'벚꽃 봄밤',scene:'spring',bg:['#3b2a5e','#24173f'],levels:[
    ['꽃가루몽','#ffe0ec','#e6a9c2','·','봄바람에 날아온 아주 작은 꽃가루.'],
    ['꽃봉오리몽','#ffc2d9','#e68fb2','🌱','아직은 수줍어서 꽃을 못 피웠어요.'],
    ['벚꽃몽','#ffb3cf','#e0709c','🌸','살랑살랑 춤추며 떨어지는 꽃잎.'],
    ['나비몽','#c7a6ff','#9a73e0','🦋','꽃잎 사이를 팔랑팔랑 날아다녀요.'],
    ['새싹몽','#b4e87a','#7fbb3e','🌿','쑥쑥 자라는 중이에요.'],
    ['도시락몽','#ffd28a','#d99a3e','🍱','꽃놀이의 든든한 동반자.'],
    ['딸기몽','#ff6b7d','#c93c4f','🍓','봄 한정 새콤달콤 아이돌.'],
    ['무지개몽','#8fd8ff','#4aa3d6','🌈','비 온 뒤 하늘에 걸려 있던 친구.'],
    ['튤립몽','#ff8fb8','#d6527f','🌷','꽃밭의 사회자.'],
    ['봄바람몽','#9ef0c0','#4fbf8a','🍃','모든 꽃잎을 한 번에 날려요.'],
    ['봄의 정령','#fff0b3','#f5b942','🌼','벚꽃비가 내리는 날 하늘로 돌아가는 전설의 몽글이.']]},
  halloween:{name:'오싹 할로윈',scene:'halloween',bg:['#2b1236','#150a1c'],levels:[
    ['먼지몽','#c9b6d9','#8f7aa6','·','다락방 구석에서 깨어난 먼지 뭉치.'],
    ['거미몽','#7d6b8f','#4f405e','🕷️','실은 겁이 많은 거미.'],
    ['박쥐몽','#6b5b95','#3f3466','🦇','밤하늘을 퍼덕이는 날쌘돌이.'],
    ['사탕몽','#ff7eb6','#d4488a','🍬','"트릭 오어 트릿!" 하며 구르는 사탕.'],
    ['유령몽','#f2f2ff','#b9b9e0','👻','깜짝 놀래키려다 본인이 먼저 놀라요.'],
    ['호박몽','#ff9a3c','#d0680f','🎃','웃는 얼굴에 촛불을 품었어요.'],
    ['마녀모자몽','#8a5cd6','#5a329e','🧙','뾰족모자 아래 장난기가 가득해요.'],
    ['해골몽','#f5f5f0','#c4c4b8','💀','무서워 보여도 춤을 제일 잘 춰요.'],
    ['물약몽','#6cf0a0','#2fb36a','🧪','보글보글 끓는 비밀 레시피.'],
    ['보름달몽','#ffe27a','#d9a91f','🌕','할로윈 밤을 환하게 밝혀요.'],
    ['한밤의 정령','#ffd9ff','#d98aff','🪄','보름달이 뜨는 밤 하늘로 돌아가는 전설의 몽글이.']]}
};
```

## 2) 크기·점수 상수
```js
const N=11;
const RAD=[15,21,28,36,45,55,66,78,91,105,120];
const SCORE=[1,3,6,10,15,21,28,36,45,55,66];
const W=360,H=560,LINE=96,WALL=60;
```

## 3) 몽글이 그리기 (drawBlob) — 반3D화 핵심
```js
/* ───────── 몽글이 그리기 ───────── */
function drawBlob(ctx,x,y,r,l,o){
  o=o||{};const th=THEMES[S.theme].levels[l];
  const k=o.k||0,sc=o.s==null?1:o.s;
  ctx.save();ctx.translate(x,y);ctx.scale(sc,sc);
  if(k){ctx.rotate(o.ka||0);ctx.scale(1-k,1+k*.9);ctx.rotate(-(o.ka||0))}
  ctx.rotate(o.a||0);
  const g=ctx.createRadialGradient(-r*.35,-r*.4,r*.1,0,0,r);
  g.addColorStop(0,th[1]);g.addColorStop(1,th[2]);
  ctx.fillStyle=g;ctx.beginPath();ctx.arc(0,0,r,0,7);ctx.fill();
  ctx.strokeStyle='#0003';ctx.lineWidth=Math.max(1.5,r*.05);ctx.stroke();
  // 하이라이트
  ctx.fillStyle='#fff6';ctx.beginPath();ctx.ellipse(-r*.4,-r*.5,r*.22,r*.12,-.6,0,7);ctx.fill();
  // 눈
  const ex=r*.32,ey=-r*.05,er=Math.max(2,r*.13);
  ctx.fillStyle='#2b1d3a';
  const happy=o.happy;
  if(happy){ctx.lineWidth=Math.max(1.5,r*.07);ctx.strokeStyle='#2b1d3a';ctx.lineCap='round';
    [-1,1].forEach(s=>{ctx.beginPath();ctx.arc(s*ex,ey+er*.4,er,Math.PI*1.1,Math.PI*1.9);ctx.stroke()})}
  else [-1,1].forEach(s=>{ctx.beginPath();ctx.arc(s*ex,ey,er,0,7);ctx.fill();
    ctx.fillStyle='#fff';ctx.beginPath();ctx.arc(s*ex+er*.3,ey-er*.3,er*.35,0,7);ctx.fill();ctx.fillStyle='#2b1d3a'});
  // 볼터치·입
  ctx.fillStyle='#ff6b9d55';[-1,1].forEach(s=>{ctx.beginPath();ctx.ellipse(s*r*.55,r*.2,r*.12,r*.08,0,0,7);ctx.fill()});
  ctx.strokeStyle='#2b1d3a';ctx.lineWidth=Math.max(1.5,r*.06);ctx.lineCap='round';
  ctx.beginPath();ctx.arc(0,r*.18,r*.14,.15*Math.PI,.85*Math.PI);ctx.stroke();
  // 배지
  if(th[3]!=='·'&&r>=26){ctx.font=Math.round(r*.4)+'px serif';ctx.textAlign='center';ctx.textBaseline='middle';ctx.fillText(th[3],0,-r*.68)}
  ctx.restore();
}
```

## 4) 배경 (도구 함수 + SCENES + drawScene)
```js
/* ───────── 배경 장면 (이미지 파일 없이 코드로 그림 · 테마별 교체) ───────── */
const STARS=Array.from({length:70},(_,i)=>({x:(i*97%360),y:(i*53%420),r:.6+(i%4)*.45,p:i*.7}));
function fall(c,now,col,n,size,speed,petal){
  c.fillStyle=col;
  for(let i=0;i<n;i++){const x=((i*71)%W+Math.sin(now/900+i)*18+W)%W,y=(i*131+now*speed*.05*(1+(i%3)*.4))%(H+20)-10,r=size*(.1+(i%4)*.05);
    c.beginPath();if(petal)c.ellipse(x,y,r*1.4,r*.8,now/700+i,0,7);else c.arc(x,y,Math.max(1,r*.3),0,7);c.fill()}
}
/* ── 배경 도구 ── */
function glow(c,x,y,r,col){const g=c.createRadialGradient(x,y,0,x,y,r);g.addColorStop(0,col);g.addColorStop(1,'#0000');c.fillStyle=g;c.fillRect(x-r,y-r,r*2,r*2)}
function sky(c,stops){const g=c.createLinearGradient(0,0,0,H);stops.forEach((s,i)=>g.addColorStop(i/(stops.length-1),s));c.fillStyle=g;c.fillRect(0,0,W,H)}
function hills(c,y,amp,col,seed,fr){c.fillStyle=col;c.beginPath();c.moveTo(0,H);for(let x=0;x<=W;x+=6)c.lineTo(x,y+Math.sin(x*fr+seed)*amp+Math.sin(x*fr*2.3+seed*2)*amp*.4);c.lineTo(W,H);c.fill()}
function pine(c,x,y,h,col){c.fillStyle=col;for(let i=0;i<4;i++){const w=h*.28*(1-i*.17),ty=y-h*(.25+i*.2);c.beginPath();c.moveTo(x-w,ty+h*.22);c.lineTo(x,ty-h*.12);c.lineTo(x+w,ty+h*.22);c.fill()}c.fillRect(x-h*.03,y-h*.1,h*.06,h*.12)}
function house(c,x,y,w,h,col,win){c.fillStyle=col;c.fillRect(x,y-h,w,h);c.beginPath();c.moveTo(x-4,y-h);c.lineTo(x+w/2,y-h-w*.5);c.lineTo(x+w+4,y-h);c.fill();
  c.fillStyle=win;[[.2,.35],[.6,.35],[.4,.7]].forEach(p=>c.fillRect(x+w*p[0],y-h*p[1]-6,w*.16,w*.2))}
function sparkle(c,x,y,r,a){c.globalAlpha=a;c.strokeStyle='#fff';c.lineWidth=1;c.beginPath();c.moveTo(x-r,y);c.lineTo(x+r,y);c.moveTo(x,y-r);c.lineTo(x,y+r);c.stroke();c.globalAlpha=1}
function fireflies(c,now,n,col){for(let i=0;i<n;i++){const x=(i*83%W)+Math.sin(now/1300+i*2)*22,y=H*.45+(i*47%(H*.5))+Math.cos(now/1100+i)*16,a=.35+.65*Math.abs(Math.sin(now/600+i*1.7));glow(c,x,y,10,col.replace('A',a.toFixed(2)));c.fillStyle='#fff';c.globalAlpha=a;c.fillRect(x-1,y-1,2,2);c.globalAlpha=1}}
function clouds(c,now,col,n,yMin,yMax,sp){c.fillStyle=col;for(let i=0;i<n;i++){const w=60+(i%3)*30,x=((i*130+now*sp*(1+i%2*.5))%(W+w*2))-w,y=yMin+(i*61%(yMax-yMin));
  c.beginPath();c.ellipse(x,y,w*.5,10,0,0,7);c.ellipse(x+w*.25,y-7,w*.28,11,0,0,7);c.ellipse(x-w*.2,y-4,w*.25,9,0,0,7);c.fill()}}
/* 장면: base는 한 번만 그려 캐시, anim은 매 프레임 */
const SCENES={
 night:{
  base(c,th){sky(c,['#1e1a5c','#2d2570','#4a2f7a','#2a1d58']);
   glow(c,70,150,150,'#6a3fb855');glow(c,300,260,130,'#3f7fd144');
   c.fillStyle='#fff';STARS.forEach(s=>{c.globalAlpha=.25+(s.r/2);c.fillRect(s.x,s.y*1.1,s.r,s.r)});c.globalAlpha=1;
   glow(c,296,64,70,'#ffe9a844');c.fillStyle='#ffeaa8';c.beginPath();c.arc(296,64,22,0,7);c.fill();c.fillStyle='#2e2672';c.beginPath();c.arc(305,57,20,0,7);c.fill();
   hills(c,H-170,22,'#2a2263',1,.012);hills(c,H-120,20,'#221b52',3,.016);
   house(c,40,H-112,26,22,'#17123a','#ffd36a');house(c,78,H-118,20,18,'#17123a','#ffd36a');house(c,250,H-108,30,26,'#17123a','#ffd36a');
   hills(c,H-70,16,'#161135',5,.02);for(let i=0;i<7;i++)pine(c,18+i*52+(i%2)*14,H-58,34+(i%3)*10,'#100c29');
  },
  anim(c,now){
   STARS.forEach(s=>{if(s.r>1.4){sparkle(c,s.x,s.y*1.1,3+s.r,.2+.6*Math.abs(Math.sin(now/800+s.p)))}});
   clouds(c,now,'#8f7fd01a',4,60,260,.006);
   const t=(now/4500)%1;if(t<.25){const k=t/.25;c.strokeStyle='#fff';c.globalAlpha=1-k;c.lineWidth=2;c.beginPath();c.moveTo(60+k*240,20+k*110);c.lineTo(20+k*240,k*110-8);c.stroke();c.globalAlpha=1}
   fireflies(c,now,10,'rgba(255,240,150,A)');
  }},
 space:{
  base(c,th){sky(c,['#0a0f2e','#141a4a','#1c1250','#0a0b22']);
   glow(c,70,130,170,'#7b4bff55');glow(c,300,310,150,'#ff4bb444');glow(c,180,470,170,'#2b9bff33');
   c.fillStyle='#fff';STARS.forEach(s=>{c.globalAlpha=.3+s.r/2.5;c.fillRect(s.x,s.y*1.3,s.r,s.r)});c.globalAlpha=1;
   const pg=c.createRadialGradient(290,110,4,300,120,46);pg.addColorStop(0,'#ffd7a0');pg.addColorStop(1,'#c4683a');c.fillStyle=pg;c.beginPath();c.arc(300,120,40,0,7);c.fill();
   c.strokeStyle='#ffe6c4aa';c.lineWidth=4;c.beginPath();c.ellipse(300,120,70,14,-.35,0,7);c.stroke();
   const bg=c.createRadialGradient(40,430,5,40,430,95);bg.addColorStop(0,'#8ec8ff');bg.addColorStop(1,'#1d4a9e');c.fillStyle=bg;c.beginPath();c.arc(30,460,95,0,7);c.fill();
   glow(c,30,460,140,'#6fb4ff33');
  },
  anim(c,now){
   STARS.forEach(s=>{if(s.r>1.2)sparkle(c,s.x,s.y*1.3,2+s.r,.15+.6*Math.abs(Math.sin(now/700+s.p)))});
   // 정거장 실루엣
   c.save();c.translate(70+Math.sin(now/3000)*10,230);c.rotate(Math.sin(now/4000)*.05);c.fillStyle='#0d1030cc';c.fillRect(-26,-3,52,6);c.fillRect(-4,-14,8,28);c.fillStyle='#3a5fd9';c.fillRect(-26,-12,14,24);c.fillRect(12,-12,14,24);c.fillStyle='#ffd36a';c.fillRect(-1,-2,2,2);c.restore();
   // 소행성 띠
   for(let i=0;i<9;i++){const x=((i*53+now*.012*(1+i%3))%(W+40))-20,y=330+i*18+Math.sin(now/900+i)*4;c.fillStyle='#8a7d9c';c.beginPath();c.arc(x,y,2+(i%3),0,7);c.fill()}
   const t=(now/6000)%1;if(t<.2){const k=t/.2;c.strokeStyle='#bfe6ff';c.lineWidth=2.5;c.globalAlpha=1-k;c.beginPath();c.moveTo(340-k*300,40+k*80);c.lineTo(370-k*300,30+k*80);c.stroke();c.globalAlpha=1}
  }},
 food:{
  base(c,th){sky(c,['#5a3330','#4a2a2a','#3a2020']);
   c.fillStyle='#ffffff08';for(let y=0;y<300;y+=36)for(let x=(y/36%2)*18;x<W;x+=36)c.fillRect(x+1,y+1,34,34);
   c.fillStyle='#2e1b19';c.fillRect(0,120,W,10);c.fillRect(0,230,W,10);
   [['#ff6b5a',20],['#ffc857',70],['#a3c76e',120],['#fff2cc',170],['#ff9f43',240],['#e85d75',290]].forEach(([col,x])=>{c.fillStyle=col+'88';c.fillRect(x,92,28,28);c.fillStyle='#fff3';c.fillRect(x+3,95,6,22);c.fillStyle='#b9894f';c.fillRect(x+4,86,20,6)});
   [['#d9c27a',40],['#6fe3c1',110],['#ffadd2',200],['#8d8cff',280]].forEach(([col,x])=>{c.fillStyle=col+'77';c.beginPath();c.arc(x+14,206,14,0,7);c.fill();c.fillStyle='#b9894f';c.fillRect(x+6,188,16,6)});
   glow(c,180,40,200,'#ffcf6644');c.strokeStyle='#2b1a18';c.lineWidth=2;c.beginPath();c.moveTo(180,0);c.lineTo(180,30);c.stroke();c.fillStyle='#ffd57a';c.beginPath();c.moveTo(150,52);c.quadraticCurveTo(180,10,210,52);c.fill();
   const s=40;for(let i=0;i<8;i++)for(let x=-4;x<12;x++)if((x+i)%2===0){c.fillStyle='#e8433a22';c.beginPath();c.moveTo(180+(x-4)*s*(1+i*.18),H-170+i*i*5+i*14);c.lineTo(180+(x-3)*s*(1+i*.18),H-170+i*i*5+i*14);c.lineTo(180+(x-3)*s*(1+(i+1)*.18),H-170+(i+1)*(i+1)*5+(i+1)*14);c.lineTo(180+(x-4)*s*(1+(i+1)*.18),H-170+(i+1)*(i+1)*5+(i+1)*14);c.fill()}
  },
  anim(c,now){
   for(let i=0;i<6;i++){const x=(i*61+40),k=((now/2200+i*.3)%1);c.strokeStyle='#fff';c.globalAlpha=.16*(1-k);c.lineWidth=3;c.beginPath();c.moveTo(x,H-130-k*130);c.bezierCurveTo(x+12,H-150-k*130,x-12,H-170-k*130,x+4,H-190-k*130);c.stroke();c.globalAlpha=1}
   for(let i=0;i<9;i++){const x=(i*47+now/50)%W,y=(i*83+Math.sin(now/1500+i)*20)%300;glow(c,x,y,14+(i%3)*6,'#ffd16622')}
  }},
 winter:{
  base(c,th){sky(c,['#0d1e45','#17305f','#2c4a7a','#10203f']);
   c.fillStyle='#fff';STARS.slice(0,40).forEach(s=>{c.globalAlpha=.3+s.r/3;c.fillRect(s.x,s.y*.9,s.r,s.r)});c.globalAlpha=1;
   hills(c,H-190,18,'#27456f',2,.013);
   for(let i=0;i<9;i++)pine(c,10+i*44+(i%2)*10,H-150+(i%3)*10,50+(i%3)*14,'#122a47');
   hills(c,H-120,16,'#dfeaf7',4,.015);c.fillStyle='#ffffff22';c.fillRect(0,H-120,W,H);
   house(c,250,H-102,44,34,'#3c2a2a','#ffd36a');c.fillStyle='#fff';c.beginPath();c.moveTo(246,H-136);c.lineTo(272,H-166);c.lineTo(298,H-136);c.fill();
   hills(c,H-70,14,'#eaf3ff',6,.02);for(let i=0;i<5;i++)pine(c,28+i*80,H-52,44+(i%2)*14,'#0e2037');
   c.fillStyle='#dfeaf7';c.fillRect(0,H-40,W,40);
  },
  anim(c,now){
   for(let i=0;i<3;i++){c.beginPath();const col=['#5df5c0','#6aa8ff','#c78bff'][i];c.fillStyle=col;c.globalAlpha=.1+.04*Math.sin(now/1500+i);
    c.moveTo(0,70+i*28);for(let x=0;x<=W;x+=12)c.lineTo(x,70+i*28+Math.sin(x*.02+now/1200+i)*22+Math.sin(x*.05+now/700)*8);for(let x=W;x>=0;x-=12)c.lineTo(x,150+i*28+Math.sin(x*.02+now/1200+i)*26);c.fill()}c.globalAlpha=1;
   fall(c,now,'#ffffffcc',44,24,1.8);fall(c,now+999,'#ffffff77',26,14,1.0);
  }},
 spring:{
  base(c,th){sky(c,['#3b2a6e','#6b3f86','#a85a8a','#3a2158']);
   glow(c,90,110,160,'#ffe9b855');c.fillStyle='#fff2c8';c.beginPath();c.arc(90,110,26,0,7);c.fill();
   c.fillStyle='#fff';STARS.slice(0,25).forEach(s=>{c.globalAlpha=.3+s.r/3;c.fillRect(s.x,s.y*.5,s.r,s.r)});c.globalAlpha=1;
   hills(c,H-150,20,'#4a2a6a',1,.012);hills(c,H-100,16,'#34204f',3,.017);
   c.strokeStyle='#1c1030';c.lineCap='round';c.lineWidth=16;c.beginPath();c.moveTo(W+10,H);c.quadraticCurveTo(W-60,H-200,W-10,H-360);c.stroke();
   c.lineWidth=8;c.beginPath();c.moveTo(W-30,H-250);c.quadraticCurveTo(W-120,H-300,W-170,H-290);c.stroke();c.lineWidth=6;c.beginPath();c.moveTo(W-20,H-340);c.quadraticCurveTo(W-100,H-400,W-150,H-420);c.stroke();
   for(let i=0;i<46;i++){const a=i*2.4,r=18+(i%5)*12,px=W-80+Math.cos(a)*(40+i*2.5%110),py=H-330+Math.sin(a)*(30+i*3%100);c.fillStyle=['#ffb3cf88','#ffc7dd77','#ff9fc233'][i%3];c.beginPath();c.arc(px,py,r*.5,0,7);c.fill()}
   c.strokeStyle='#2a1a3a';c.lineWidth=1.5;c.beginPath();c.moveTo(0,40);c.quadraticCurveTo(180,90,W,30);c.stroke();
   hills(c,H-56,12,'#251538',6,.02);
  },
  anim(c,now){
   for(let i=0;i<8;i++){const x=i*48+8,y=40+Math.sin(x*.012)*30+(i%2)*10;glow(c,x,y+12,18,'#ffcf6a'+(Math.floor(110+60*Math.sin(now/500+i)).toString(16).padStart(2,'0')));c.fillStyle='#ffb347';c.beginPath();c.ellipse(x,y+12,4,6,0,0,7);c.fill()}
   fireflies(c,now,8,'rgba(255,225,160,A)');fall(c,now,'#ffc2d9',34,18,1.3,true);fall(c,now+500,'#ffe0ecaa',18,12,.8,true);
  }},
 halloween:{
  base(c,th){sky(c,['#1d0b2c','#3a1646','#5a2250','#190a22']);
   glow(c,90,110,170,'#ffb04a44');c.fillStyle='#ffd27a';c.beginPath();c.arc(90,110,40,0,7);c.fill();c.fillStyle='#e8b95a66';[[78,100,9],[102,120,7],[72,124,5],[100,96,5]].forEach(k=>{c.beginPath();c.arc(k[0],k[1],k[2],0,7);c.fill()});
   c.fillStyle='#fff';STARS.slice(0,20).forEach(s=>{c.globalAlpha=.3;c.fillRect(s.x,s.y*.6,s.r,s.r)});c.globalAlpha=1;
   hills(c,H-170,16,'#2a1138',2,.013);
   c.fillStyle='#120618';c.fillRect(215,H-250,86,110);c.fillRect(205,H-140,106,20);c.beginPath();c.moveTo(210,H-250);c.lineTo(258,H-310);c.lineTo(306,H-250);c.fill();c.fillRect(280,H-330,14,50);
   c.fillStyle='#ffb04a';[[228,H-230],[262,H-230],[228,H-195],[262,H-195]].forEach(w=>c.fillRect(w[0],w[1],14,20));glow(c,258,H-210,60,'#ffb04a33');
   c.strokeStyle='#120618';c.lineCap='round';c.lineWidth=7;c.beginPath();c.moveTo(30,H-120);c.quadraticCurveTo(26,H-200,40,H-290);c.stroke();
   [[34,H-230,-70,-30],[36,H-260,50,-24],[40,H-290,-40,-40]].forEach(b=>{c.lineWidth=3;c.beginPath();c.moveTo(b[0],b[1]);c.quadraticCurveTo(b[0]+b[2]/2,b[1]-6,b[0]+b[2]*.6,b[1]+b[3]);c.stroke()});
   hills(c,H-90,14,'#150820',4,.018);
   [[60,H-52],[140,H-44],[330,H-50]].forEach(([x,y],i)=>{c.fillStyle='#e8751a';c.beginPath();c.ellipse(x,y,15,12,0,0,7);c.fill();c.fillStyle='#ffd36a';c.beginPath();c.moveTo(x-7,y-3);c.lineTo(x-2,y-3);c.lineTo(x-5,y-8);c.moveTo(x+7,y-3);c.lineTo(x+2,y-3);c.lineTo(x+5,y-8);c.fill();c.fillRect(x-6,y+3,12,3)});
   c.fillStyle='#0c0512';c.fillRect(0,H-30,W,30);
  },
  anim(c,now){
   [[250,120],[300,170],[190,70],[120,200]].forEach((p,i)=>{const w=8*Math.sin(now/170+i),px=p[0]+Math.sin(now/1500+i)*20,py=p[1]+Math.cos(now/1200+i)*10;c.fillStyle='#0c0512';c.beginPath();c.moveTo(px,py);c.quadraticCurveTo(px-12,py-8+w,px-22,py);c.quadraticCurveTo(px-12,py+4,px,py+8);c.quadraticCurveTo(px+12,py+4,px+22,py);c.quadraticCurveTo(px+12,py-8+w,px,py);c.fill()});
   [[310,300],[60,330]].forEach((p,i)=>{const x=p[0]+Math.sin(now/1100+i)*14,y=p[1]+Math.sin(now/800+i*2)*10;c.globalAlpha=.28;c.fillStyle='#e8e8ff';c.beginPath();c.arc(x,y,16,Math.PI,0);c.lineTo(x+16,y+22);for(let k=3;k>=0;k--)c.lineTo(x-16+k*10.6+(k%2?-4:4),y+22+(k%2?-5:3));c.fill();c.globalAlpha=.5;c.fillStyle='#120618';c.fillRect(x-7,y-2,4,6);c.fillRect(x+3,y-2,4,6);c.globalAlpha=1});
   c.fillStyle='#c9a6ff14';for(let i=0;i<3;i++){const x=((now*.01*(1+i*.3)+i*140)%(W+200))-100;c.beginPath();c.ellipse(x,H-60-i*28,130,16,0,0,7);c.fill()}
   fall(c,now,'#ff9a3c66',12,10,.5);
  }}
};
const SCENE_CACHE={};
function drawScene(c,now,th,key){
  let cache=SCENE_CACHE[key];
  if(!cache){const o=document.createElement('canvas');o.width=W*2;o.height=H*2;const oc=o.getContext('2d');oc.scale(2,2);SCENES[th.scene].base(oc,th);SCENE_CACHE[key]=cache=o}
  c.drawImage(cache,0,0,W,H);SCENES[th.scene].anim(c,now);
  const v=c.createRadialGradient(W/2,H/2,H*.35,W/2,H/2,H*.75);v.addColorStop(0,'#0000');v.addColorStop(1,'#00000066');c.fillStyle=v;c.fillRect(0,0,W,H);
}
```

## 5) 마스코트 (drawMascot)
```js
/* ───────── 마스코트 '별똥이' ───────── */
const mascot={text:'',until:0,mood:'idle'};
function say(t,mood,ms){mascot.text=t;mascot.mood=mood||'idle';mascot.until=performance.now()+(ms||1800)}
function drawMascot(c,now){
  const x=W-30,y=44+Math.sin(now/600)*3,r=19,m=now<mascot.until?mascot.mood:'idle';
  c.save();c.translate(x,y);
  const g=c.createRadialGradient(-6,-8,2,0,0,r);g.addColorStop(0,'#fff3a8');g.addColorStop(1,'#f5b700');c.fillStyle=g;c.beginPath();c.arc(0,0,r,0,7);c.fill();
  c.strokeStyle='#0003';c.lineWidth=1.5;c.stroke();
  // 별 더듬이
  c.fillStyle='#ffe066';c.save();c.translate(0,-r-6);c.rotate(Math.sin(now/400)*.3);c.beginPath();for(let i=0;i<10;i++){const rr=i%2?3:7,a=i/10*Math.PI*2-Math.PI/2;c.lineTo(Math.cos(a)*rr,Math.sin(a)*rr)}c.closePath();c.fill();c.restore();
  c.strokeStyle='#2b1d3a';c.fillStyle='#2b1d3a';c.lineWidth=2;c.lineCap='round';
  const blink=Math.floor(now/200)%18===0;
  if(m==='happy'||blink){[-1,1].forEach(s=>{c.beginPath();c.arc(s*7,-2+(blink&&m!=='happy'?2:0),3,blink&&m!=='happy'?0:Math.PI*1.1,blink&&m!=='happy'?Math.PI:Math.PI*1.9);c.stroke()})}
  else{[-1,1].forEach(s=>{c.beginPath();c.arc(s*7,-2,m==='scared'?4:2.8,0,7);c.fill()})}
  c.beginPath();if(m==='scared'){c.arc(0,8,3,0,7);c.stroke()}else if(m==='sad'){c.arc(0,10,4,Math.PI*1.15,Math.PI*1.85);c.stroke()}else{c.arc(0,4,5,.15*Math.PI,.85*Math.PI);c.stroke()}
  c.restore();
  if(now<mascot.until&&mascot.text){
    c.font='bold 12px sans-serif';const w=c.measureText(mascot.text).width+16,bx=x-r-6-w,by=y-14;
    c.fillStyle='#fffffff0';c.beginPath();c.roundRect(bx,by,w,26,10);c.fill();
    c.beginPath();c.moveTo(bx+w,by+8);c.lineTo(bx+w+7,by+13);c.lineTo(bx+w,by+18);c.fill();
    c.fillStyle='#2b1d3a';c.textAlign='left';c.textBaseline='middle';c.fillText(mascot.text,bx+8,by+13)}
}
```

## 6) 렌더 루프 (몽글이가 실제로 어떻게 그려지는지)
```js
function render(now){
  ctx.setTransform(scale*dpr,0,0,scale*dpr,0,0);const _th=THEMES[S.theme];drawScene(ctx,now,_th,S.theme);drawMascot(ctx,now);
  shake*=.86;if(shake>.2)ctx.translate((Math.random()-.5)*shake,(Math.random()-.5)*shake);
  // 위험선
  const warn=blobs.some(b=>b.overT);if(warn&&now>mascot.until)say('앗, 위험해요!','scared',1000);
  ctx.setLineDash([8,8]);ctx.strokeStyle=warn?'#ff5470':'#ffffff44';ctx.lineWidth=warn&&Math.floor(now/200)%2?3:2;
  ctx.beginPath();ctx.moveTo(0,LINE);ctx.lineTo(W,LINE);ctx.stroke();ctx.setLineDash([]);
  // 조준
  if(!over&&dropReady&&!bombMode){
    const r=RAD[curLevel],x=Math.max(r+2,Math.min(W-r-2,aimX));
    ctx.strokeStyle='#ffffff30';ctx.setLineDash([4,6]);ctx.beginPath();ctx.moveTo(x,r*2+10);ctx.lineTo(x,H);ctx.stroke();ctx.setLineDash([]);
    drawBlob(ctx,x,r+8,r,curLevel,{});
  }else if(!over&&!bombMode){const r=RAD[curLevel];ctx.globalAlpha=.35;drawBlob(ctx,Math.max(r+2,Math.min(W-r-2,aimX)),r+8,r,curLevel,{});ctx.globalAlpha=1}
  for(const b of blobs){
    b.kv+=-.22*b.k-.1*b.kv;b.k+=b.kv;if(Math.abs(b.k)<.001&&Math.abs(b.kv)<.001)b.k=b.kv=0;
    b.sv+=-.18*(b.s-1)-.12*b.sv;b.s+=b.sv;
    const near=Math.hypot(b.velocity.x,b.velocity.y)>2;
    drawBlob(ctx,b.position.x,b.position.y,RAD[b.lvl],b.lvl,{a:b.angle,k:b.k+Math.sin(now/500+b.id)*.008,ka:b.ka,s:b.s,happy:Math.abs(b.k)>.06||near&&b.lvl>3});
    if(bombMode){ctx.strokeStyle='#ff547088';ctx.lineWidth=2;ctx.beginPath();ctx.arc(b.position.x,b.position.y,RAD[b.lvl]+3,0,7);ctx.stroke()}
  }
  for(let i=fx.length-1;i>=0;i--){const p=fx[i];p.x+=p.vx;p.y+=p.vy;p.vy+=.12;p.life-=.025;
    if(p.life<=0){fx.splice(i,1);continue}
    ctx.globalAlpha=p.life;ctx.fillStyle=p.c;ctx.beginPath();ctx.arc(p.x,p.y,p.r*p.life,0,7);ctx.fill();ctx.globalAlpha=1}
}
```

## 7) drawBlob 호출 예시 (다른 곳에서도 같은 함수를 씀 — 시그니처 유지)
```js
drawBlob(c.getContext('2d'),48,48,40,i,{happy:true});
function drawNext(){nctx.clearRect(0,0,88,88);if(nextLevel!=null)drawBlob(nctx,44,44,Math.min(RAD[nextLevel],38)*(38/RAD[4]>1?1:1),nextLevel)}
drawBlob(ctx,x,r+8,r,curLevel,{});
}else if(!over&&!bombMode){const r=RAD[curLevel];ctx.globalAlpha=.35;drawBlob(ctx,Math.max(r+2,Math.min(W-r-2,aimX)),r+8,r,curLevel,{});ctx.globalAlpha=1}
drawBlob(ctx,b.position.x,b.position.y,RAD[b.lvl],b.lvl,{a:b.angle,k:b.k+Math.sin(now/500+b.id)*.008,ka:b.ka,s:b.s,happy:Math.abs(b.k)>.06||near&&b.lvl>3});
```

---
(전체 소스가 필요하면 말해줘. index.html 전체(약 60KB)를 추가로 붙여줄게.)
