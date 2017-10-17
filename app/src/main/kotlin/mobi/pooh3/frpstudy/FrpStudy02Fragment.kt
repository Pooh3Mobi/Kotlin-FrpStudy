package mobi.pooh3.frpstudy

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.text
import kotlinx.android.synthetic.main.fragment_frp_study02.*

class FrpStudy02Fragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_frp_study02, container, false)
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)

        val sOnegai = button_onegai.clicks().map { "Onegai shimasu!" }
        val sThx    = button_thankyou.clicks().map { "Thank you!" }
        val sOutput = edit_output.text()

        val sCanned = sOnegai.mergeWith(sThx)

        sCanned.subscribe(sOutput)
    }

    companion object {
        fun newInstance(): FrpStudy02Fragment {
            return FrpStudy02Fragment()
        }
    }
}