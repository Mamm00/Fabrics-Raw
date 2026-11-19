# Fabrics-Raw

Raw fabric master data for **Al Azhar Tex** (Cairo, Egypt, est. 2006) — the
single source of truth that storefronts, catalog apps and admin panels can pull
from.

> This repository holds **structured raw data only** (no UI, no backend).
> Presentation layers live in their own repos and consume this dataset.

## Contents

```
data/
  fabrics.json     Master fabric catalogue (lines, weights, materials, use cases, prices)
docs/
  schema.md        Field definitions and rules for the dataset
```

## Fabric lines currently tracked

| Line         | Type               | Weight       |
|--------------|--------------------|--------------|
| Silk 180     | Signature satin silk | 180 g/m²   |
| Warsaw L     | Airy georgette     | Lightweight  |
| Pirlanta     | Crisp luxury satin | —            |
| Rotana 150   | Lightweight satin  | 150 g/m²     |
| Rotana 180   | Medium satin       | 180 g/m²     |
| Lexus 150    | Ultra-soft satin   | 150 g/m²     |
| Lexus 180    | Premium heavy satin | 180 g/m²    |

## Data rules

- `id` is stable, lowercase, kebab-case — never reuse or rename once published.
- Prices are **wholesale reference price per meter** in EGP; `null` means
  "confirm before quoting".
- `colors` is an array of colorways; leave it `[]` until the color chart is
  recorded.
- See [`docs/schema.md`](docs/schema.md) for the full field reference.

## Editing

1. Update `data/fabrics.json`.
2. Keep `meta.lastUpdated` in sync with the change date.
3. Validate JSON before committing:

```bash
python3 -m json.tool data/fabrics.json > /dev/null
```
