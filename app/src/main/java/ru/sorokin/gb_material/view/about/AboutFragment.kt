package ru.sorokin.gb_material.view.about

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import ru.sorokin.gb_material.R
import ru.sorokin.gb_material.model.RetrofitImpl
import ru.sorokin.gb_material.util.hideAllItems


class AboutFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.about_fragment, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        val aboutTextView = view.findViewById<TextView>(R.id.about_text_view)

        activity?.let {
            aboutTextView.typeface =
                Typeface.createFromAsset(it.assets, "fonts/CuteAurora-PK3lZ.ttf")
        }

        spanText(aboutTextView)
    }

    private fun spanText(view: TextView) {
        val spannableAbout = SpannableString(view.text)
        spannableAbout.setSpan(object : ClickableSpan() {

            override fun onClick(widget: View) {
                startActivity(Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(RetrofitImpl.NASA_URL)
                })
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }

        }, 0, 49, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        view.text = spannableAbout
        view.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.hideAllItems()
    }
}