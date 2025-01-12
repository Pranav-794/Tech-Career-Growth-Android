package com.randomappsinc.techcareergrowth.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.randomappsinc.techcareergrowth.R
import com.randomappsinc.techcareergrowth.databinding.SettingsBinding
import com.randomappsinc.techcareergrowth.persistence.PreferencesManager
import com.randomappsinc.techcareergrowth.theme.ThemeManager
import com.randomappsinc.techcareergrowth.theme.ThemeMode
import com.randomappsinc.techcareergrowth.util.UIUtil
import com.randomappsinc.techcareergrowth.web.WebActivity

class SettingsActivity: AppCompatActivity(), SettingsAdapter.SettingsSelectionListener {

    companion object {

        const val SLACK_URL = "https://join.slack.com/t/techcareergrowth/shared_invite/zt-lt2tbjcn-LOAVIDuGPI~nkuc4woHDLg"
        const val SUPPORT_EMAIL = "randomappsinc61@gmail.com"
        const val TERMS_AND_CONDITIONS_URL =
            "https://github.com/Gear61/Tech-Career-Growth-Documents/blob/main/Terms%20And%20Conditions.md#terms-and-conditions"
        const val PRIVACY_POLICY_URL =
            "https://github.com/Gear61/Tech-Career-Growth-Documents/blob/main/Privacy%20Policy.md#privacy-notice"
        const val REPO_URL = "https://github.com/Gear61/Tech-Career-Growth-Android"
    }

    private lateinit var binding: SettingsBinding
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        preferencesManager = PreferencesManager(this)

        val itemDecorator = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.line_divider)!!)
        binding.settingsOptions.addItemDecoration(itemDecorator)
        val settingsAdapter = SettingsAdapter(this, this)
        binding.settingsOptions.adapter = settingsAdapter
    }

    override fun onSettingsItemClicked(position: Int) {
        when (position) {
            0 -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(SLACK_URL)))
            1 -> {
                val itemCell: View = binding.settingsOptions.getChildAt(SettingsAdapter.DARK_MODE_POSITION)
                val darkModeToggle = itemCell.findViewById<SwitchCompat>(R.id.settings_toggle)
                darkModeToggle.isChecked = !darkModeToggle.isChecked
                val themeMode: Int = if (darkModeToggle.isChecked) ThemeMode.DARK else ThemeMode.LIGHT
                preferencesManager.themeMode = themeMode
                ThemeManager.applyTheme(themeMode)
            }
            2 -> {
                val uriText =
                    "mailto:${SUPPORT_EMAIL}?subject=" + Uri.encode(getString(R.string.feedback_subject))
                val mailUri = Uri.parse(uriText)
                val sendIntent = Intent(Intent.ACTION_SENDTO, mailUri)
                startActivity(Intent.createChooser(sendIntent, getString(R.string.send_email)))
            }
            3 -> {
                val uri = Uri.parse("market://details?id=${packageName}")
                val rateIntent = Intent(Intent.ACTION_VIEW, uri)
                if (packageManager.queryIntentActivities(rateIntent, 0).size <= 0) {
                    UIUtil.showLongToast(R.string.play_store_error, this)
                }
                startActivity(rateIntent)
            }
            4 -> {
                val shareIntent = ShareCompat.IntentBuilder(this)
                    .setType("text/plain")
                    .setText(getString(R.string.share_app_message))
                    .intent
                if (shareIntent.resolveActivity(packageManager) != null) {
                    startActivity(shareIntent)
                }
            }
            5 -> openWebActivity(getString(R.string.terms_and_conditions), TERMS_AND_CONDITIONS_URL)
            6 -> openWebActivity(getString(R.string.privacy_policy), PRIVACY_POLICY_URL)
            7 -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(REPO_URL)))
        }
    }

    private fun openWebActivity(title: String, url: String) {
        val intent = Intent(this, WebActivity::class.java)
        intent.putExtra(WebActivity.TITLE_KEY, title)
        intent.putExtra(WebActivity.URL_KEY, url)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_right_out, R.anim.slide_right_in)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}