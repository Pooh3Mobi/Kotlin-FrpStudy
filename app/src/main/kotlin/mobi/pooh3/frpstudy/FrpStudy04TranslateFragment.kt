package mobi.pooh3.frpstudy

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.view.enabled
import com.jakewharton.rxbinding2.widget.text
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.rxkotlin.withLatestFrom
import kotlinx.android.synthetic.main.fragment_frp_study04.*
import mobi.pooh3.frpstudy.extensions.hold
import mobi.pooh3.frpstudy.extensions.rx.widget.textNotEmpty


class FrpStudy04TranslateFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_frp_study04, container, false)
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)

        val english = edit_input.textChanges()
        val translate = btn_translate.clicks()
        val translateEnable = btn_translate.enabled()
        val englishNotEmpty = edit_input.textNotEmpty();

        // enable control
        englishNotEmpty.subscribe(translateEnable)

        // text change control
        val sLatin = translate.withLatestFrom(english, { _, txt ->
                    txt.trim().replace(Regex(""" |$"""), "us ").trim()
                })
        val latin = sLatin.hold("this is output display")
        latin.subscribe(text_output.text())
    }

    companion object {
        fun newInstance(): FrpStudy04TranslateFragment {
            return FrpStudy04TranslateFragment()
        }
    }
}




