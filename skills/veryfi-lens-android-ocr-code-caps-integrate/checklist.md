# Validation checklist

Run this before delivering host code. Fix conflicts by removing extra flags.

- [ ] Dependency is `com.veryfi.lens:veryfi-lens-ocr-sdk:2.7.0.6` once
- [ ] `ocrType` is Caps and `documentTypes` is CODE
- [ ] `ocrRadius` / `ocrBorderStroke` used for the circle
- [ ] Customer `ocrRegex` substituted
- [ ] Delegate reads top-level `ocr_text` and `ocr_type`
- [ ] `uploadImages` is not the cap API
- [ ] Installed app size, not AAR size

- [ ] Every relative link in the generated host notes stays inside this skill folder
- [ ] Unconfirmed behavior the host depends on is labeled **Unconfirmed** in the delivery note
- [ ] Host `<application>` sets `android:allowBackup="false"` and `tools:replace="android:allowBackup"` so a new-project `true` does not fail the manifest merger
- [ ] No repository password, Demo `SettingsHolder` preset, or `VeryfiLensHelper` mutation is in the host code
