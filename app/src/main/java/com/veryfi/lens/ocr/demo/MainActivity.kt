package com.veryfi.lens.ocr.demo

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.rarepebble.colorpicker.ColorPickerView
import com.veryfi.lens.VeryfiLens
import com.veryfi.lens.helpers.DocumentType
import com.veryfi.lens.helpers.VeryfiLensCredentials
import com.veryfi.lens.helpers.VeryfiLensSettings
import com.veryfi.lens.ocr.demo.databinding.ActivityMainBinding
import com.veryfi.lens.ocr.demo.helpers.ThemeHelper
import com.veryfi.lens.ocr.demo.logs.LogsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var materialAlertDialogBuilder: MaterialAlertDialogBuilder
    private lateinit var customAlertDialogView: View
    private lateinit var customAlertDialogSliderView: View
    private lateinit var customAlertDialogTextFieldView: View
    private lateinit var colorPickerView: ColorPickerView
    private lateinit var barSliderView: Slider
    private lateinit var textFieldLayoutView: TextInputLayout
    private lateinit var textFieldView: TextInputEditText
    private val veryfiLensSettings = VeryfiLensSettings()

    private var autoCaptureIsOn = true
    private var autoRotateIsOn = false
    private var autoDocDetectionAndCropIsOn = veryfiLensSettings.autoDocDetectionAndCropIsOn
    private var isProduction = veryfiLensSettings.isProduction
    private var primaryColor = veryfiLensSettings.primaryColor ?: "#FF005AC1"
    private var primaryDarkColor = veryfiLensSettings.primaryDarkColor ?: "#FFADC6FF"
    private var secondaryColor = veryfiLensSettings.secondaryColor ?: "#FFDBE2F9"
    private var secondaryDarkColor = veryfiLensSettings.secondaryDarkColor ?: "#FF3F4759"
    private var accentColor = veryfiLensSettings.accentColor ?: "#FF005AC1"
    private var accentDarkColor = veryfiLensSettings.accentDarkColor ?: "#FFDBE2F9"
    private var docDetectFillUIColor = veryfiLensSettings.docDetectFillUIColor ?: "#9653BF8A"
    private var submitButtonBackgroundColor = veryfiLensSettings.submitButtonBackgroundColor
    private var submitButtonBorderColor = veryfiLensSettings.submitButtonBorderColor
    private var submitButtonFontColor = veryfiLensSettings.submitButtonFontColor
    private var docDetectStrokeUIColor = veryfiLensSettings.docDetectStrokeUIColor ?: "#00000000"
    private var submitButtonCornerRadius = veryfiLensSettings.submitButtonCornerRadius
    private var ocrRegex = veryfiLensSettings.ocrRegex ?: "[A-Z0-9]{10}"
    private var ocrViewCornerRadius = veryfiLensSettings.ocrViewCornerRadius ?: 20
    private var ocrViewWidth = 85
    private var ocrViewHeight = 8

    override fun onStart() {
        super.onStart()
        initVeryfiLogo()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        materialAlertDialogBuilder = MaterialAlertDialogBuilder(this)
        applicationContext?.let { ThemeHelper.setBackgroundColorToStatusBar(this, it) }
        initVeryfiSettings()
        setUpClickEvents()
    }

    private fun initVeryfiLogo() {
        when (applicationContext?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                viewBinding.veryfiLogo.setImageResource(R.drawable.ic_veryfi_logo_white)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                viewBinding.veryfiLogo.setImageResource(R.drawable.ic_veryfi_logo_black)
            }
        }
    }

    private fun initVeryfiSettings() {
        viewBinding.switchAutoCapture.isChecked = autoCaptureIsOn
        viewBinding.switchAutoRotate.isChecked = autoRotateIsOn
        viewBinding.switchAutoDocDetection.isChecked = autoDocDetectionAndCropIsOn
        viewBinding.switchIsProduction.isChecked = isProduction
        initColors()
        initFloatValues()
        initStringValues()
    }

    private fun initColors() {
        viewBinding.imgPrimaryColor.setBackgroundColor(Color.parseColor(primaryColor))
        viewBinding.imgPrimaryDarkColor.setBackgroundColor(Color.parseColor(primaryDarkColor))
        viewBinding.imgSecondaryColor.setBackgroundColor(Color.parseColor(secondaryColor))
        viewBinding.imgSecondaryDarkColor.setBackgroundColor(Color.parseColor(secondaryDarkColor))
        viewBinding.imgAccentColor.setBackgroundColor(Color.parseColor(accentColor))
        viewBinding.imgAccentDarkColor.setBackgroundColor(Color.parseColor(accentDarkColor))
        viewBinding.imgDetectFillColor.setBackgroundColor(Color.parseColor(docDetectFillUIColor))
        viewBinding.imgSubmitBackgroundColor.setBackgroundColor(
            Color.parseColor(
                submitButtonBackgroundColor
            )
        )
        viewBinding.imgSubmitBorderColor.setBackgroundColor(Color.parseColor(submitButtonBorderColor))
        viewBinding.imgSubmitFontColor.setBackgroundColor(Color.parseColor(submitButtonFontColor))
        viewBinding.imgDocDetectStrokeColor.setBackgroundColor(
            Color.parseColor(
                docDetectStrokeUIColor
            )
        )
    }

    private fun initFloatValues() {
        viewBinding.txtCornerRadius.text = submitButtonCornerRadius.toString()
        viewBinding.txtOcrCornerRadius.text = ocrViewCornerRadius.toString()
        viewBinding.txtOcrViewWidth.text = ocrViewWidth.toString()
        viewBinding.txtOcrViewHeight.text = ocrViewHeight.toString()
    }

    private fun initStringValues() {
        viewBinding.txtOcrRegex.text = (ocrRegex.ifEmpty { "[A-Z0-9]{10}" }).toString()
    }

    @SuppressLint("InflateParams")
    private fun setUpClickEvents() {
        viewBinding.btnScan.setOnClickListener {
            setVeryfiSettings()
        }

        viewBinding.switchAutoCapture.setOnCheckedChangeListener { _, isChecked ->
            autoCaptureIsOn = isChecked
        }

        viewBinding.switchAutoRotate.setOnCheckedChangeListener { _, isChecked ->
            autoRotateIsOn = isChecked
        }

        viewBinding.switchAutoDocDetection.setOnCheckedChangeListener { _, isChecked ->
            autoDocDetectionAndCropIsOn = isChecked
        }

        viewBinding.switchIsProduction.setOnCheckedChangeListener { _, isChecked ->
            isProduction = isChecked
        }

        viewBinding.imgPrimaryColor.setOnClickListener {
            customAlertDialogView = LayoutInflater.from(this)
                .inflate(R.layout.fragment_color_picker, null, false)
            showDialog(primaryColor, 0)
        }

        viewBinding.imgPrimaryDarkColor.setOnClickListener {
            customAlertDialogView = LayoutInflater.from(this)
                .inflate(R.layout.fragment_color_picker, null, false)
            showDialog(primaryDarkColor, 1)
        }

        viewBinding.imgSecondaryColor.setOnClickListener {
            customAlertDialogView = LayoutInflater.from(this)
                .inflate(R.layout.fragment_color_picker, null, false)
            showDialog(secondaryColor, 2)
        }

        viewBinding.imgSecondaryDarkColor.setOnClickListener {
            customAlertDialogView = LayoutInflater.from(this)
                .inflate(R.layout.fragment_color_picker, null, false)
            showDialog(secondaryDarkColor, 3)
        }

        viewBinding.imgAccentColor.setOnClickListener {
            customAlertDialogView = LayoutInflater.from(this)
                .inflate(R.layout.fragment_color_picker, null, false)
            showDialog(accentColor, 4)
        }

        viewBinding.imgAccentDarkColor.setOnClickListener {
            customAlertDialogView = LayoutInflater.from(this)
                .inflate(R.layout.fragment_color_picker, null, false)
            showDialog(accentDarkColor, 5)
        }

        viewBinding.imgDetectFillColor.setOnClickListener {
            customAlertDialogView = LayoutInflater.from(this)
                .inflate(R.layout.fragment_color_picker, null, false)
            showDialog(docDetectFillUIColor, 6)
        }

        viewBinding.imgSubmitBackgroundColor.setOnClickListener {
            customAlertDialogView = LayoutInflater.from(this)
                .inflate(R.layout.fragment_color_picker, null, false)
            showDialog(submitButtonBackgroundColor, 7)
        }

        viewBinding.imgSubmitBorderColor.setOnClickListener {
            customAlertDialogView = LayoutInflater.from(this)
                .inflate(R.layout.fragment_color_picker, null, false)
            showDialog(submitButtonBorderColor, 8)
        }

        viewBinding.imgSubmitFontColor.setOnClickListener {
            customAlertDialogView = LayoutInflater.from(this)
                .inflate(R.layout.fragment_color_picker, null, false)
            showDialog(submitButtonFontColor, 9)
        }

        viewBinding.imgDocDetectStrokeColor.setOnClickListener {
            customAlertDialogView = LayoutInflater.from(this)
                .inflate(R.layout.fragment_color_picker, null, false)
            showDialog(docDetectStrokeUIColor, 10)
        }

        viewBinding.txtCornerRadius.setOnClickListener {
            customAlertDialogSliderView = LayoutInflater.from(this)
                .inflate(R.layout.fragment_bar_selection, null, false)
            showDialogWithSlider(submitButtonCornerRadius.toFloat(), 0)
        }

        viewBinding.txtOcrRegex.setOnClickListener {
            customAlertDialogTextFieldView = LayoutInflater.from(this)
                .inflate(R.layout.fragment_edit_text, null, false)
            showDialogWithTextField(ocrRegex)
        }

        viewBinding.txtOcrCornerRadius.setOnClickListener {
            customAlertDialogSliderView = LayoutInflater.from(this)
                .inflate(R.layout.fragment_bar_selection, null, false)
            showDialogWithSlider(ocrViewCornerRadius.toFloat(), 1)
        }

        viewBinding.txtOcrViewWidth.setOnClickListener {
            customAlertDialogSliderView = LayoutInflater.from(this)
                .inflate(R.layout.fragment_bar_selection, null, false)
            showDialogWithSlider(ocrViewWidth.toFloat(), 2)
        }

        viewBinding.txtOcrViewHeight.setOnClickListener {
            customAlertDialogSliderView = LayoutInflater.from(this)
                .inflate(R.layout.fragment_bar_selection, null, false)
            showDialogWithSlider(ocrViewHeight.toFloat(), 3)
        }
    }

    private fun showDialog(color: String?, typeColor: Int) {
        colorPickerView = customAlertDialogView.findViewById(R.id.colorPicker)
        colorPickerView.color = Color.parseColor(color)

        materialAlertDialogBuilder.setView(customAlertDialogView)
            .setTitle(resources.getString(R.string.settings_set_color_title))
            .setPositiveButton(resources.getString(R.string.btn_ok)) { dialog, _ ->
                val colorSelected = "#".plus(formatColor(colorPickerView.color))

                when (typeColor) {
                    0 -> {
                        primaryColor = colorSelected
                    }
                    1 -> {
                        primaryDarkColor = colorSelected
                    }
                    2 -> {
                        secondaryColor = colorSelected
                    }
                    3 -> {
                        secondaryDarkColor = colorSelected
                    }
                    4 -> {
                        accentColor = colorSelected
                    }
                    5 -> {
                        accentDarkColor = colorSelected
                    }
                    6 -> {
                        docDetectFillUIColor = colorSelected
                    }
                    7 -> {
                        submitButtonBackgroundColor = colorSelected
                    }
                    8 -> {
                        submitButtonBorderColor = colorSelected
                    }
                    9 -> {
                        submitButtonFontColor = colorSelected
                    }
                    10 -> {
                        docDetectStrokeUIColor = colorSelected
                    }
                }
                initColors()
                dialog.dismiss()
            }
            .setNegativeButton(resources.getString(R.string.btn_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showDialogWithSlider(value: Float, type: Int) {
        barSliderView = customAlertDialogSliderView.findViewById(R.id.barSlider)
        barSliderView.value = value
        var titleDialog = ""
        when (type) {
            0 -> {
                titleDialog = resources.getString(R.string.settings_set_submit_button_corner_radius)
                barSliderView.valueFrom = 0.0f
                barSliderView.valueTo = 30.0f
                barSliderView.stepSize = 1.0f
            }
            1 -> {
                titleDialog = resources.getString(R.string.settings_set_ocr_view_corner_radius)
                barSliderView.valueFrom = 0.0f
                barSliderView.valueTo = 20.0f
                barSliderView.stepSize = 1.0f
            }
            2 -> {
                titleDialog = resources.getString(R.string.settings_set_ocr_view_width)
                barSliderView.valueFrom = 0.0f
                barSliderView.valueTo = 100.0f
                barSliderView.stepSize = 1.0f
            }
            3 -> {
                titleDialog = resources.getString(R.string.settings_set_ocr_view_height)
                barSliderView.valueFrom = 0.0f
                barSliderView.valueTo = 10.0f
                barSliderView.stepSize = 1.0f
            }
        }

        materialAlertDialogBuilder.setView(customAlertDialogSliderView)
            .setTitle(titleDialog)
            .setPositiveButton(resources.getString(R.string.btn_ok)) { dialog, _ ->

                when (type) {
                    0 -> {
                        submitButtonCornerRadius = barSliderView.value.toInt()
                    }
                    1 -> {
                        ocrViewCornerRadius = barSliderView.value.toInt()
                    }
                    2 -> {
                        ocrViewWidth = barSliderView.value.toInt()
                    }
                    3 -> {
                        ocrViewHeight = barSliderView.value.toInt()
                    }
                }
                initFloatValues()
                dialog.dismiss()
            }
            .setNegativeButton(resources.getString(R.string.btn_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showDialogWithTextField(value: String) {
        textFieldLayoutView = customAlertDialogTextFieldView.findViewById(R.id.textField_layout)
        textFieldView = customAlertDialogTextFieldView.findViewById(R.id.textField)
        textFieldView.setText(value)
        val titleDialog = resources.getString(R.string.settings_set_ocr_regex)
        textFieldLayoutView.hint = resources.getString(R.string.settings_hint_ocr_regex)

        materialAlertDialogBuilder.setView(customAlertDialogTextFieldView)
            .setTitle(titleDialog)
            .setPositiveButton(resources.getString(R.string.btn_ok)) { dialog, _ ->
                ocrRegex = textFieldView.text.toString()
                initStringValues()
                dialog.dismiss()
            }
            .setNegativeButton(resources.getString(R.string.btn_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun setVeryfiSettings() {
        veryfiLensSettings.stitchIsOn = false
        veryfiLensSettings.moreMenuIsOn = false
        veryfiLensSettings.galleryIsOn = false
        veryfiLensSettings.showDocumentTypes = true
        veryfiLensSettings.closeCameraOnSubmit = true
        veryfiLensSettings.autoCaptureIsOn = autoCaptureIsOn
        veryfiLensSettings.autoRotateIsOn = autoRotateIsOn
        veryfiLensSettings.autoDocDetectionAndCropIsOn = autoDocDetectionAndCropIsOn
        veryfiLensSettings.primaryColor = primaryColor
        veryfiLensSettings.primaryDarkColor = primaryDarkColor
        veryfiLensSettings.secondaryColor = secondaryColor
        veryfiLensSettings.secondaryDarkColor = secondaryDarkColor
        veryfiLensSettings.accentColor = accentColor
        veryfiLensSettings.accentDarkColor = accentDarkColor
        veryfiLensSettings.docDetectFillUIColor = docDetectFillUIColor
        veryfiLensSettings.submitButtonBackgroundColor = submitButtonBackgroundColor
        veryfiLensSettings.submitButtonBorderColor = submitButtonBorderColor
        veryfiLensSettings.submitButtonFontColor = submitButtonFontColor
        veryfiLensSettings.docDetectStrokeUIColor = docDetectStrokeUIColor
        veryfiLensSettings.submitButtonCornerRadius = submitButtonCornerRadius
        veryfiLensSettings.isProduction = isProduction
        veryfiLensSettings.ocrRegex = ocrRegex
        veryfiLensSettings.ocrViewCornerRadius = ocrViewCornerRadius
        veryfiLensSettings.ocrViewWidth = ocrViewWidth
        veryfiLensSettings.ocrViewHeight = ocrViewHeight
        veryfiLensSettings.dataExtractionEngine = VeryfiLensSettings.ExtractionEngine.VeryfiCloudAPI
        veryfiLensSettings.documentTypes = arrayListOf(DocumentType.CODE)

        val veryfiLensCredentials = VeryfiLensCredentials()
        veryfiLensCredentials.apiKey = AUTH_API_KEY
        veryfiLensCredentials.username = AUTH_USERNAME
        veryfiLensCredentials.clientId = CLIENT_ID
        veryfiLensCredentials.url = URL

        VeryfiLens.configure(application, veryfiLensCredentials, veryfiLensSettings) {}
        startActivity(Intent(this, LogsActivity::class.java))
    }

    private fun formatColor(color: Int): String {
        return String.format("%08x", color)
    }

    companion object {
        // REPLACE YOUR KEYS HERE
        const val CLIENT_ID = BuildConfig.VERYFI_CLIENT_ID
        const val AUTH_USERNAME = BuildConfig.VERYFI_USERNAME
        const val AUTH_API_KEY = BuildConfig.VERYFI_API_KEY
        const val URL = BuildConfig.VERYFI_URL
    }
}