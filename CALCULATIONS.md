# CALCULATIONS.md — Formulas & Textile Glossary

All money and percentage accumulation uses `BigDecimal`, `RoundingMode.HALF_UP`,
scale 2. The domain engine never uses `Double` for money. Display formatting is
locale-aware; stored values stay exact.

## 1. Raw material intake conversion

- **Meter purchase:** per-meter cost = recorded unit cost.
- **Kg purchase:**
  - `widthM = widthCm / 100`
  - `gramsPerMeter = GSM × widthM`
  - `kgPerMeter = gramsPerMeter / 1000`
  - `costPerMeter = costPerKg × kgPerMeter`
- **Bolt purchase:** `costPerMeter = boltCost / metersPerBolt`

## 2. Shrinkage

`shrinkage% = ((Original − Final) / Original) × 100`

- Warp (length) and weft (width) computed independently.
- Growth toggle: a negative result (final > original) is allowed and flagged `GROWTH`.
- Warn/flag thresholds come from `TextileRules`, not hardcoded.

## 3. Grade yield & loss

- `pctFirst = qFirst / totalOutput × 100`
- `pctSecond = qSecond / totalOutput × 100`
- `pctReject = qReject / totalOutput × 100`
- Validation: no negative quantities; `qFirst + qSecond + qReject ≤ totalOutput`.
- `compensationRequired = pctSecond > rules.secondGradeCompensationThresholdPct`.

## 4. Cost build-up

- Raw material adjusted for shrinkage before layering.
- Greige = shrinkage-adjusted raw + weaving/processing.
- Dyeing/finishing = greige × dyeingFinishingPct (guidance band 12–25%, editable).
- Wastage = subtotal × wastagePct.
- VAT = preTax × vatPct.
- `totalCost`; `costPerMeter = totalCost / meters`; `costPerKg = totalCost / batchKg`.

## 5. Pricing & compensation

- Solve first-grade price:
  `P_first = (TargetRevenue − Q_second × P_second) / Q_first`
- Reverse (fixed first-grade price):
  `P_second = (TargetRevenue − Q_first × P_first) / Q_second`
- Discount: `(referencePrice − P_second) / referencePrice × 100`
- Sensitivity table: second-grade share moves by −5, −2, −1, 0, +1, +2, +5 points;
  quantities re-split proportionally, `P_first` recomputed each row.

## Glossary (English / العربية)

| English | العربية |
|---|---|
| Shrinkage | انكماش |
| Growth (negative shrinkage) | تمدّد |
| Warp (length) | سداة |
| Weft (width) | لحمة |
| First grade | درجة أولى |
| Second grade | درجة ثانية |
| Reject / waste | رفض / هدر |
| Yield | مردود / إنتاجية |
| Greige fabric | قماش رمادي (قبل الصباغة) |
| Dyeing | صباغة |
| Finishing | تشطيب |
| Weaving | نسج |
| Overhead | مصروفات عامة |
| Wastage | هدر |
| Inspection | فحص |
| Packaging | تعبئة |
| VAT | ضريبة القيمة المضافة |
| GSM (g/m²) | وزن المتر المربع |
| Width | عرض |
| Bolt | بكرة |
| Compensation pricing | تسعير تعويضي |
| Benchmark | مؤشر صناعي / مرجع |
