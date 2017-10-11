package mobi.pooh3.frpstudy

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.text

class FrpStudy02Fragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_frp_study02, container, false)
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)

        val onegaiText = v.findViewById<Button>(R.id.onegai_shimasu)
        val thxText    = v.findViewById<Button>(R.id.thx)
        val outputText = v.findViewById<EditText>(R.id.output)

        val sOnegai = onegaiText.clicks().map { "Onegai shimasu!" }
        val sThx    = thxText.clicks().map { "Thank you!" }
        val sOutput = outputText.text()

        val sCanned = sOnegai.mergeWith(sThx)

        sCanned.subscribe(sOutput)
    }

    companion object {
        fun newInstance(): FrpStudy02Fragment {
            return FrpStudy02Fragment()
        }
    }
}