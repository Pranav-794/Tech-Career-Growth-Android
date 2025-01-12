package com.randomappsinc.techcareergrowth.util

import android.content.Context
import android.view.Menu
import android.widget.CompoundButton
import android.widget.Toast
import androidx.annotation.StringRes
import com.joanzapata.iconify.Icon
import com.joanzapata.iconify.IconDrawable
import com.randomappsinc.techcareergrowth.R

object UIUtil {

    fun loadMenuIcon(menu: Menu, itemId: Int, icon: Icon?, context: Context?) {
        menu.findItem(itemId).icon = IconDrawable(context, icon)
            .colorRes(R.color.white)
            .actionBarSize()
    }

    fun setCheckedImmediately(checkableView: CompoundButton, checked: Boolean) {
        checkableView.isChecked = checked
        checkableView.jumpDrawablesToCurrentState()
    }

    fun showShortToast(@StringRes stringId: Int, context: Context) {
        showToast(stringId, Toast.LENGTH_SHORT, context)
    }

    fun showLongToast(@StringRes stringId: Int, context: Context) {
        showToast(stringId, Toast.LENGTH_LONG, context)
    }

    private fun showToast(@StringRes stringId: Int, duration: Int, context: Context) {
        Toast.makeText(context, stringId, duration).show()
    }
}
