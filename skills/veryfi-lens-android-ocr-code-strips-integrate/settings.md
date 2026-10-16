# Veryfi Lens Android — OCR code strips — settings

Source of truth: Veryfi Lens Android SDK **2.7.0.6**, commit `7f33726c`. Names below were read from that tree. Items not fully confirmed are marked **Unconfirmed**. Do not copy repository passwords, keystores, or Demo-only types into the host app.


Always construct a fresh `VeryfiLensSettings()`. Android has no `VeryfiLens*Settings` subclasses. Pass that instance to `configure`. The SDK mutates it.

```kotlin
import com.veryfi.lens.helpers.DocumentType
import com.veryfi.lens.helpers.VeryfiLensSettings

fun ocrStripsSettings() = VeryfiLensSettings().apply {
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
    galleryIsOn = false                    // forced false for CODE when stored prefs load
    stitchIsOn = false
    autoLightDetectionIsOn = false
    continuousAutoFocus = true             // default false
    ocrType = VeryfiLensSettings.OcrType.Strips // default Strips
    ocrRegex = "[A-Z0-9]{10}"            // default null; replace with the customer pattern
    ocrViewWidth = 85                      // default 85 dp
    ocrViewHeight = 8                      // default 8 dp
    ocrViewCornerRadius = 20               // default 20 dp
    ocrBorderColor = "#48E195"             // default #48E195
    usesAlternativeOCRModel = false        // default false
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

| `dataExtractionEngine` | `VeryfiCloudAPI` | `VeryfiInApp` | Local `ocr_text` |
| `ocrType` | `Strips` | `Strips` | Caps is the other enum value |
| `ocrRegex` | null | customer pattern | Null uses no host pattern. Do not ship the sample regex unchanged |
| `galleryIsOn` | true | false | Forced off for CODE on prefs load |
| `autoCaptureIsOn` | false | true | Auto-capture the code |
| `continuousAutoFocus` | false | true | Close-up codes |
| `ocrViewWidth` / `ocrViewHeight` | 85 / 8 | 85 / 8 | Strip rectangle, dp |
| `usesAlternativeOCRModel` | false | false | Turn on only when Veryfi says to |


`ocrViewWidth`, `ocrViewHeight`, and `ocrViewCornerRadius` shape the strips rectangle. Caps ignores that rectangle and uses a circle (`ocrRadius`, `ocrBorderStroke`).

`VeryfiCloudAPI` uploads to the partner OCR endpoint instead. The Demo reads `data.code`, `data.score`, `data.img_url`, `data.id`. That response shape is **Unconfirmed** as a contract. InApp keys above are in `OCRDetector.createOCRJSON`.

Updates during scan can carry `ocr_uuid`, `ocr_text`, and `ocr_score` before success.


## Suggestions

- Ask for the regex before generating the final snippet. The sample `[A-Z0-9]{10}` is a placeholder.
- Call `VeryfiLens.shareOCRFeedback()` only if the host wants to upload OCR feedback logs.
- There is no Remote Models build for `CODE`. Measure the installed app with `veryfi-lens-ocr-sdk` linked and removed.

