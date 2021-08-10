package ru.sorokin.gb_material.util

import android.text.Editable
import android.view.Menu
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import retrofit2.Response
import ru.sorokin.gb_material.R
import ru.sorokin.gb_material.model.pod.PODServerResponseData
import ru.sorokin.gb_material.viewmodel.AppState
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

const val DATE_TIME_FORMAT = "yyyy-MM-dd"

fun Date.format(): String = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
    .format(this)

fun Date.subDays(days: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.DATE, -days)
    return calendar.time
}

fun View.show() {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
}

fun View.hide() {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
}

fun View.showSnackBar(
    text: String,
    actionText: String,
    action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar.make(this, text, length)
        .setAction(actionText, action)
        .show()
}

fun FragmentManager.addFragmentWithBackStack(fragment: Fragment) = this.apply {
    beginTransaction()
        .add(R.id.container, fragment)
        .addToBackStack(null)
        .commit()
}

fun Menu.hideAllItems() {
    for (i: Int in 0 until this.size()) {
        this.getItem(i).let {
            if (it.isVisible) {
                it.isVisible = false
            }
        }
    }
}

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

fun Response<*>.getResponse(): AppState {
    if (isSuccessful && body() != null) {
        return AppState.Success(body()!!)
    } else {
        val message = message()
        if (message.isNullOrEmpty()) {
            return AppState.Error(Throwable("Неизвестная ошибка"))
        } else {
            return AppState.Error(Throwable(message))
        }
    }
}

fun Fragment.loadImageFromCallback(
    url: String?,
    imageView: EquilateralImageView,
    loadingLayout: View,
    rootView: View,
    actionReload: (View) -> Unit
) {
    Picasso
        .get()
        .load(url)
        .placeholder(R.drawable.ic_no_photo_vector)
        .into(imageView, object : Callback {
            override fun onSuccess() {
                loadingLayout.hide()
            }

            override fun onError(e: Exception?) {
                rootView.showSnackBar(
                    e?.message
                        ?: getString(R.string.error_server_msg),
                    getString(R.string.reload_msg),
                    actionReload
                )
            }
        })
}

fun Fragment.callbackError(
    errorMessage: String?,
    loadingLayout: View,
    rootView: View,
    actionReload: (View) -> Unit
) {
    loadingLayout.hide()
    rootView.showSnackBar(
        errorMessage ?: getString(R.string.error_msg),
        getString(R.string.reload_msg),
        actionReload
    )
}