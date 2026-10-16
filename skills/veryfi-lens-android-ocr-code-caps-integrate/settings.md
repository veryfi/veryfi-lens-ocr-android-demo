# Veryfi Lens Android — OCR bottle caps — settings

Source of truth: Veryfi Lens Android SDK **2.7.0.6**, commit `7f33726c`. Names below were read from that tree. Items not fully confirmed are marked **Unconfirmed**. Do not copy repository passwords, keystores, or Demo-only types into the host app.


Always construct a fresh `VeryfiLensSettings()`. Android has no `VeryfiLens*Settings` subclasses. Pass that instance to `configure`. The SDK mutates it.

```kotlin
import com.veryfi.lens.helpers.DocumentType
import com.veryfi.lens.helpers.VeryfiLensSettings

fun ocrCapsSettings() = VeryfiLensSettings().apply {
    ignoreRemoteSettings = true            // default false; dashboard lens_settings overwrite this object during configure
    backupDocsToGallery = false            // default true; copies every scan into the device gallery
    moreMenuIsOn = false                   // default true; stored user prefs then replace host capture flags
    moreSettingsMenuIsOn = false           // default true
    autoCaptureOtherIsOn = false           // default true; OTHER auto-captures even when autoCaptureIsOn is false
    showNoDocumentDetectedWarning = false  // default true
    dictateIsOn = false                    // default true; obsolete voice capture, not a partner feature
    locationServicesIsOn = false           // default false; true raises a runtime location request
    glareDetectionIsOn = false             // default true; the glare gate is a weak algorithmic check

    dataExtractionEngine = VeryfiLensSettings.ExtractionEngine.VeryfiInApp
    documentTypes = arrayListOf(DocumentType.CODE)
    showDocumentTypes = false
    autoCaptureIsOn = true
    galleryIsOn = false
    stitchIsOn = false
    autoLightDetectionIsOn = false
    continuousAutoFocus = true
    ocrType = VeryfiLensSettings.OcrType.Caps
    ocrRadius = 60                       // default 60; caps circle radius
    ocrBorderStroke = 10                 // default 10; caps circle stroke
    ocrBorderColor = "#48E195"           // default
    ocrRegex = "[A-Z0-9]{10}"          // replace with the customer pattern
}

```

## Defaults versus this recipe

Android initializers below are the property defaults in `VeryfiLensSettings`.

| Property | Android default | This recipe | Why |
|----------|-----------------|-------------|-----|
| `ignoreRemoteSettings` | false | true | Dashboard `lens_settings` would overwrite this object during configure |
| `backupDocsToGallery` | true | false | Default copies every scan into the device gallery |
| `moreMenuIsOn` | true | false | Stored user prefs would replace host capture flags on later configure calls |
| `moreSettingsMenuIsOn` | true | false | Settings sheet inside More |
| `autoCaptureOtherIsOn` | true | false | `OTHER` auto-captures even when `autoCaptureIsOn` is false |
| `showNoDocumentDetectedWarning` | true | false | Skip the no-document alert |
| `dictateIsOn` | true | false | Obsolete voice control; default is on |
| `glareDetectionIsOn` | true | false | Android default glare gate is a weak check |
| `locationServicesIsOn` | false | false | Leave off unless the host needs lat/lng on the package |

| `ocrType` | `Strips` | `Caps` | Selects caps models and a circular mask |
| `ocrRadius` | 60 | 60 | Circle radius, caps only |
| `ocrBorderStroke` | 10 | 10 | Circle stroke, caps only |
| `ocrRegex` | null | customer pattern | Replace the sample |
| `galleryIsOn` | true | false | Forced off for CODE on prefs load |
| `dataExtractionEngine` | `VeryfiCloudAPI` | `VeryfiInApp` | Local `ocr_text` |


`ocrViewWidth`, `ocrViewHeight`, and `ocrViewCornerRadius` apply to the strips rectangle. Caps uses `FocusView` circle geometry via `ocrRadius` and `ocrBorderStroke`. `shareOCRFeedback()` is optional. Cloud OCR response keys are **Unconfirmed**.

## Suggestions

- Confirm the regex and whether the cap is a full circle in frame before changing `ocrRadius`.
- Measure installed size. Remote Models cannot take `CODE`.
- Keep one Lens UI artifact. Strips and caps share `veryfi-lens-ocr-sdk`; switch `ocrType`, do not add a second dependency.

