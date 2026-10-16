---
name: veryfi-lens-android-ocr-code-caps-integrate
description: "Integrates the Veryfi Lens Android OCR SDK (com.veryfi.lens:veryfi-lens-ocr-sdk:2.7.0.6) for bottle-cap codes: OcrType.Caps, ocrRadius, ocrRegex, VeryfiInApp, VeryfiLens.configure, and delegate ocr_text. Use when scanning codes on caps with Lens on Android. Same artifact as strip OCR with a different ocrType."
---

# Veryfi Lens Android — OCR bottle caps

Walk a host Android app from Gradle to a working first scan. Change the fewest flags. Do not invent SDK APIs. Facts were read from the Android SDK at version `2.7.0.6` (commit `7f33726c`). Anything not confirmed there is marked **Unconfirmed**.

**Coordinate:** `com.veryfi.lens:veryfi-lens-ocr-sdk:2.7.0.6`  
**Gradle flavor:** `lensOCR`  
**Allowed document types:** `DocumentType.CODE` only.

Public entry point: `com.veryfi.lens.VeryfiLens` with `com.veryfi.lens.VeryfiLensDelegate`. Settings type: `com.veryfi.lens.helpers.VeryfiLensSettings`.

This folder is complete by itself. Duplicate guidance across products is intentional.

## Pick a workflow

- **Quick** — the host already asked for this product. Ask nothing else. Apply the settings in [settings.md](settings.md), wire [implementation.md](implementation.md), and state the assumptions below.
- **Guided** — something material is missing. Ask only the unknown questions (at most four, one message), then continue.

### Quick workflow

```
veryfi-lens-android-ocr-code-caps-integrate quick:
- [ ] Confirm this artifact (install.md)
- [ ] Add the Maven repository and this one dependency
- [ ] Permissions (install.md)
- [ ] Credentials + settings from settings.md
- [ ] setDelegate, then configure { ok -> }, then showCamera() only when ok is true
- [ ] Handle the callbacks in callbacks.md
- [ ] Run the conflicts in troubleshooting.md
```

Assumptions when the host was specific about the product and silent on the rest: Lens camera, `CODE`, `OcrType.Caps`, `VeryfiInApp`, gallery off, circular mask, customer regex required.

### Guided workflow

1. **Shape** — caps (this skill) or strips (`OcrType.Strips`)?
2. **Pattern** — customer `ocrRegex`?
3. **Radius** — keep default `ocrRadius` 60 or a host value?
4. **Engine** — InApp (default) or Cloud OCR (**Unconfirmed** shape)?

Defaults: caps, InApp, radius 60, gallery off.


## Rules

1. Same artifact and `DocumentType.CODE` as strips. The difference is `ocrType = VeryfiLensSettings.OcrType.Caps`.
2. Circle geometry is `ocrRadius` (default 60) and `ocrBorderStroke` (default 10). Strip rectangle fields do not shape the caps mask.
3. InApp payload `ocr_type` is `caps` (`enum name` lowercased).
4. Gallery is forced off for `CODE` when stored prefs load.
5. `uploadImages` is not the cap reader.


## Do not

- Copy a repository password, keystore, or credential from the SDK repo or the Demo app. Maven access comes from Veryfi. Keep it in `~/.gradle/gradle.properties` or CI secrets.
- Depend on a second Lens UI flavor, or on `com.veryfi.lens.core:veryfi-lens-core-sdk` (email and obsolete dictation; adds `WRITE_CONTACTS` and `RECORD_AUDIO`).
- Treat Demo types as the client contract: `SettingsHolder`, Demo analyzers, `ImageProxyUtils`, `VeryfiLensHelper`.
- Set `dictateIsOn = true` or call `VeryfiLensCore.showDictation()`.
- Set `anyDocumentType` (deprecated local regex OCR). A new template id is `anyDocumentTemplate`, and only after Veryfi support confirms it.
- Set `useLegacyModels` (default true; no partner contract).
- Quote AAR size as the installed app size. Compare the final app with the SDK linked and removed. [SDK binary sizes](https://docs.veryfi.com/lens/mobile/settings/sdk_binary_sizes/).
- Treat `allowSubmittingScreenshots` (rejects imported screenshots) and `enableScreenshots` (device screenshots of Lens screens, default true) as one switch.
- Set `ocrViewWidth` and expect a circle. Caps uses `ocrRadius`.
- Use Full or Lite for `CODE`.
- Leave the sample regex in production when the customer gave a pattern.


## What to hand back

1. `com.veryfi.lens:veryfi-lens-ocr-sdk:2.7.0.6`.
2. `ocrCapsSettings()` with `OcrType.Caps` and the customer regex.
3. Delegate reading top-level `ocr_text` and checking `ocr_type` is `caps`.


## Files in this folder

- [install.md](install.md) — Maven coordinate, requirements, permissions, app size
- [implementation.md](implementation.md) — configure, delegate, and launch or camera lifecycle
- [settings.md](settings.md) — `VeryfiLensSettings` for this product, with Android defaults
- [callbacks.md](callbacks.md) — delegate payloads
- [troubleshooting.md](troubleshooting.md) — symptom, cause, fix
- [clarifications.md](clarifications.md) — Unconfirmed items, Any-Documents, fraud, field detection
- [checklist.md](checklist.md) — validation checklist before handing code to the host

