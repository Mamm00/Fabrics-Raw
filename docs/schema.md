# Fabrics-Raw Dataset Schema

File: `data/fabrics.json`

## Top-level object

| Field        | Type    | Description |
|--------------|---------|-------------|
| `meta`       | object  | Dataset-level metadata (see below) |
| `fabrics`    | array   | List of fabric records |

### `meta`

| Field           | Type   | Description |
|-----------------|--------|-------------|
| `company`       | string | Business name |
| `established`   | string | Founding year (display string) |
| `location`      | string | City / country |
| `currency`      | string | ISO-ish currency code for prices |
| `notes`         | string | Free-text caveats about the data |
| `lastUpdated`   | string | ISO date `YYYY-MM-DD` of last dataset change |

## Fabric record

| Field                    | Type     | Required | Description |
|--------------------------|----------|----------|-------------|
| `id`                     | string   | yes      | Stable kebab-case identifier, e.g. `rotana-150` |
| `name`                   | string   | yes      | Display name as sold, e.g. `Rotana 150` |
| `type`                   | string   | yes      | Short category description |
| `weight`                 | string\|null | yes | Weight label, e.g. `150 g/m²`; `null` if unknown |
| `material`               | string   | yes      | Blend / composition description |
| `hand`                   | string   | no       | Hand-feel and drape notes |
| `bestFor`                | string[] | no       | Typical end uses |
| `colors`                 | string[] | no       | Available colorways; `[]` until recorded |
| `wholesalePricePerMeter` | number\|null | no | Wholesale reference price per meter in `meta.currency`; `null` = confirm before quoting |
| `stockStatus`            | string   | yes      | One of `in-stock`, `low-stock`, `out-of-stock`, `discontinued` |
| `tags`                   | string[] | no       | Free-form search tags |

## Conventions

- **IDs**: lowercase, letters and digits separated by single hyphens; do not
  change an existing `id` after other repos reference it.
- **Units**: weight uses `g/m²`; price is per meter.
- **Nulls**: use `null` (not `""`) for missing numeric/weight values; empty
  lists use `[]`.
- **No personal or sensitive contact data** belongs in this raw dataset; keep
  phone/email in the storefront or admin config instead.
