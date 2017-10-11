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
import com.jakewharton.rxbinding2.widget.text
import io.reactivex.subjects.BehaviorSubject

class FrpStudy03Fragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_frp_study03, container, false)
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)

        val onegaiButton = v.findViewById<Button>(R.id.onegai_shimasu)
        val thxButton    = v.findViewById<Button>(R.id.thx)
        val outputEdit   = v.findViewById<EditText>(R.id.output)
        val outputText   = v.findViewById<TextView>(R.id.output2)

        val sOnegai  = onegaiButton.clicks().map { "Onegai shimasu!" }
        val sThx     = thxButton.clicks().map { "Thank you!" }
        val sOutputB = outputText.text()
        val sOutputA = outputEdit.text()

        val default = BehaviorSubject.createDefault("default")

        val share = sOnegai.mergeWith(sThx).share().mergeWith(default)

        share.subscribe(sOutputA)
        share.subscribe(sOutputB)
    }


    companion object {
        fun newInstance(): FrpStudy03Fragment {
            return FrpStudy03Fragment()
        }
    }

}