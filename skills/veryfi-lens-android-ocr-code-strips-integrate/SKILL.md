---
name: veryfi-lens-android-ocr-code-strips-integrate
description: "Integrates the Veryfi Lens Android OCR SDK (com.veryfi.lens:veryfi-lens-ocr-sdk:2.7.0.6) for strip codes: Maven setup, VeryfiInApp, OcrType.Strips, ocrRegex, VeryfiLens.configure, delegate ocr_text payloads, showCamera, and shareOCRFeedback. Use when scanning alphanumeric codes on strips with Lens on Android."
---

# Veryfi Lens Android — OCR code strips

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
veryfi-lens-android-ocr-code-strips-integrate quick:
- [ ] Confirm this artifact (install.md)
- [ ] Add the Maven repository and this one dependency
- [ ] Permissions (install.md)
- [ ] Credentials + settings from settings.md
- [ ] setDelegate, then configure { ok -> }, then showCamera() only when ok is true
- [ ] Handle the callbacks in callbacks.md
- [ ] Run the conflicts in troubleshooting.md
```

Assumptions when the host was specific about the product and silent on the rest: Lens camera, `CODE`, `OcrType.Strips`, `VeryfiInApp`, gallery off, regex placeholder `[A-Z0-9]{10}` replaced by the host pattern.

### Guided workflow

1. **Shape** — strips (this skill) or bottle caps (`OcrType.Caps` on the same artifact)?
2. **Pattern** — the customer's `ocrRegex`?
3. **Engine** — on-device `VeryfiInApp` (default here) or Cloud OCR (**Unconfirmed** response shape)?
4. **Entry** — camera only?

Defaults: strips, InApp, gallery off, camera.


## Rules

1. `documentTypes[0]` is `CODE`. `isLensOCR()` is that check.
2. When stored prefs load, `galleryIsOn` is forced false for `CODE`. Leave it false.
3. Local results are top-level `ocr_uuid`, `ocr_text`, `ocr_score`, `ocr_image`, `ocr_type` from `OCRDetector.createOCRJSON`. `ocr_type` is `settings.ocrType.name.lowercase()` (`strips`).
4. `getRequiredModels` loads `OCRStrips` and `ReadWordStrips` when `ocrType` is `Strips`.
5. `VeryfiLens.shareOCRFeedback()` uploads OCR feedback logs. It is optional.
6. `uploadImages` is not the OCR reader. Headless SDKs do not implement code OCR.


## Do not

- Copy a repository password, keystore, or credential from the SDK repo or the Demo app. Maven access comes from Veryfi. Keep it in `~/.gradle/gradle.properties` or CI secrets.
- Depend on a second Lens UI flavor, or on `com.veryfi.lens.core:veryfi-lens-core-sdk` (email and obsolete dictation; adds `WRITE_CONTACTS` and `RECORD_AUDIO`).
- Treat Demo types as the client contract: `SettingsHolder`, Demo analyzers, `ImageProxyUtils`, `VeryfiLensHelper`.
- Set `dictateIsOn = true` or call `VeryfiLensCore.showDictation()`.
- Set `anyDocumentType` (deprecated local regex OCR). A new template id is `anyDocumentTemplate`, and only after Veryfi support confirms it.
- Set `useLegacyModels` (default true; no partner contract).
- Quote AAR size as the installed app size. Compare the final app with the SDK linked and removed. [SDK binary sizes](https://docs.veryfi.com/lens/mobile/settings/sdk_binary_sizes/).
- Treat `allowSubmittingScreenshots` (rejects imported screenshots) and `enableScreenshots` (device screenshots of Lens screens, default true) as one switch.
- Put `CODE` on Full, Remote Models, or Lite.
- Leave `ocrRegex` as `[A-Z0-9]{10}` when the customer provided a different pattern.
- Read `data.ocr_text` for InApp. The keys are top-level.
- Enable gallery.
- Set `ocrType = Caps` in this strips integration.


## What to hand back

1. `implementation "com.veryfi.lens:veryfi-lens-ocr-sdk:2.7.0.6"`.
2. `ocrStripsSettings()` with the customer's regex, not a guessed one left in production if they supplied a pattern.
3. Delegate reading top-level `ocr_text` (not `data`).
4. Note that Cloud OCR response keys are **Unconfirmed**.


## Files in this folder

- [install.md](install.md) — Maven coordinate, requirements, permissions, app size
- [implementation.md](implementation.md) — configure, delegate, and launch or camera lifecycle
- [settings.md](settings.md) — `VeryfiLensSettings` for this product, with Android defaults
- [callbacks.md](callbacks.md) — delegate payloads
- [troubleshooting.md](troubleshooting.md) — symptom, cause, fix
- [clarifications.md](clarifications.md) — Unconfirmed items, Any-Documents, fraud, field detection
- [checklist.md](checklist.md) — validation checklist before handing code to the host

