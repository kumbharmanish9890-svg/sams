function toggleTheme() {
    const html = document.documentElement;
    const next = html.getAttribute('data-bs-theme') === 'dark' ? 'light' : 'dark';
    html.setAttribute('data-bs-theme', next);
    localStorage.setItem('theme', next);
}
document.documentElement.setAttribute('data-bs-theme', localStorage.getItem('theme') || 'dark');

// Sidebar मध्ये चालू page highlight
document.querySelectorAll('.sidebar a').forEach(a => {
    const href = a.getAttribute('href');
    const path = location.pathname;
    if (href === '/' ? path === '/' : path.startsWith(href)) a.classList.add('active');
});

// Background: glowing orbs + floating particle network
(function () {
    ['o1', 'o2', 'o3'].forEach(c => {
        const d = document.createElement('div');
        d.className = 'bg-orb ' + c;
        document.body.prepend(d);
    });

    const canvas = document.createElement('canvas');
    canvas.id = 'bg-canvas';
    document.body.prepend(canvas);
    const ctx = canvas.getContext('2d');

    const reduce = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
    const mouse = { x: -999, y: -999 };
    let w, h, dots = [];

    function neon() {
        return getComputedStyle(document.documentElement).getPropertyValue('--neon').trim() || '#00f5d4';
    }

    function resize() {
        const dpr = window.devicePixelRatio || 1;
        w = window.innerWidth; h = window.innerHeight;
        canvas.width = w * dpr; canvas.height = h * dpr;
        canvas.style.width = w + 'px'; canvas.style.height = h + 'px';
        ctx.setTransform(dpr, 0, 0, dpr, 0, 0);
        const n = Math.min(80, Math.floor(w * h / 20000));
        dots = Array.from({ length: n }, () => ({
            x: Math.random() * w, y: Math.random() * h,
            vx: (Math.random() - 0.5) * 0.5, vy: (Math.random() - 0.5) * 0.5
        }));
    }

    function draw() {
        ctx.clearRect(0, 0, w, h);
        ctx.fillStyle = ctx.strokeStyle = neon();
        for (let i = 0; i < dots.length; i++) {
            const a = dots[i];
            if (!reduce) {
                a.x += a.vx; a.y += a.vy;
                if (a.x < 0 || a.x > w) a.vx *= -1;
                if (a.y < 0 || a.y > h) a.vy *= -1;
            }
            ctx.globalAlpha = 0.8;
            ctx.beginPath(); ctx.arc(a.x, a.y, 1.8, 0, Math.PI * 2); ctx.fill();

            for (let j = i + 1; j < dots.length; j++) {
                const b = dots[j];
                const d = Math.hypot(a.x - b.x, a.y - b.y);
                if (d < 130) {
                    ctx.globalAlpha = (1 - d / 130) * 0.35;
                    ctx.beginPath(); ctx.moveTo(a.x, a.y); ctx.lineTo(b.x, b.y); ctx.stroke();
                }
            }
            const dm = Math.hypot(a.x - mouse.x, a.y - mouse.y);
            if (dm < 170) {
                ctx.globalAlpha = (1 - dm / 170) * 0.6;
                ctx.beginPath(); ctx.moveTo(a.x, a.y); ctx.lineTo(mouse.x, mouse.y); ctx.stroke();
            }
        }
        ctx.globalAlpha = 1;
        if (!reduce && !document.hidden) requestAnimationFrame(draw);
    }

    window.addEventListener('resize', resize);
    window.addEventListener('mousemove', e => { mouse.x = e.clientX; mouse.y = e.clientY; });
    document.addEventListener('visibilitychange', () => { if (!document.hidden && !reduce) draw(); });
    resize();
    draw();
})();