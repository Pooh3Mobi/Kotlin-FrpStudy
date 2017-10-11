package mobi.pooh3.frpstudy

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.view.enabled
import com.jakewharton.rxbinding2.widget.text
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.rxkotlin.withLatestFrom
import mobi.pooh3.frpstudy.extensions.hold
import mobi.pooh3.frpstudy.extensions.textNotEmpty


class FrpStudy04TranslateFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_frp_study04, container, false)
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)

        val englishEditText = v.findViewById<EditText>(R.id.input)
        val translateButton = v.findViewById<Button>(R.id.translate)
        val outputText      = v.findViewById<TextView>(R.id.output)


        val english = englishEditText.textChanges()
        val translate = translateButton.clicks()
        val translateEnable = translateButton.enabled()

        // enable control
        englishEditText.textNotEmpty().subscribe(translateEnable)

        // text change control
        val sLatin = translate.withLatestFrom(english, { _, txt ->
                    txt.trim().replace(Regex(""" |$"""), "us ").trim()
                })
        val latin = sLatin.hold("this is output display")
        latin.subscribe(outputText.text())
    }

    companion object {
        fun newInstance(): FrpStudy04TranslateFragment {
            return FrpStudy04TranslateFragment()
        }
    }
}




