# Validation checklist

Run this before delivering host code. Fix conflicts by removing extra flags.

- [ ] Dependency is `com.veryfi.lens:veryfi-lens-ocr-sdk:2.7.0.6`
- [ ] `documentTypes` is CODE and `ocrType` is Strips
- [ ] Engine is VeryfiInApp unless the host accepted Unconfirmed Cloud OCR
- [ ] `ocrRegex` is the customer pattern
- [ ] Delegate reads top-level `ocr_text`
- [ ] Gallery stays off
- [ ] `uploadImages` is not the OCR API
- [ ] Any-Documents escalated
- [ ] Installed app size, not AAR size

- [ ] Every relative link in the generated host notes stays inside this skill folder
- [ ] Unconfirmed behavior the host depends on is labeled **Unconfirmed** in the delivery note
- [ ] Host `<application>` sets `android:allowBackup="false"` and `tools:replace="android:allowBackup"` so a new-project `true` does not fail the manifest merger
- [ ] No repository password, Demo `SettingsHolder` preset, or `VeryfiLensHelper` mutation is in the host code
