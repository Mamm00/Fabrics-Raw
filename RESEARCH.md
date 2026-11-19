# RESEARCH.md — Benchmark Sources

The benchmark feature sends batch context to the backend `/analyze` endpoint,
which returns structured industry insights with citations. The provider key lives
server-side only (see `docs/SECRETS.md`).

## Candidate benchmark references (to verify before production)

- Textile costing & yield guides for wholesale garment/fabric production.
- Typical shrinkage ranges by fiber and weave (warp/weft tolerance bands).
- Grade-yield benchmarks: first/second/reject share norms for wholesale fabric.
- Dyeing/finishing cost as a percentage of greige cost (12–25% working band).
- Egyptian textile market price references for women's satin/georgette lines.

## Citation format

Each insight carries `Citation { title, url }`. Citations must be real,
reachable sources; the stub endpoint emits a placeholder `example.invalid` URL
that must be replaced before release.

## Status

- Backend currently ships a deterministic `StubBenchmarkService` used when no
  provider key is configured.
- Real provider integration is a tracked follow-up: choose provider, add server
  client behind `BenchmarkService`, keep key in env, record sources here.
