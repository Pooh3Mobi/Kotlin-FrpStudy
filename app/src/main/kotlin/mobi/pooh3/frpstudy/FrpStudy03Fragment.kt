package mobi.pooh3.frpstudy

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.text
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.fragment_frp_study03.*

class FrpStudy03Fragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_frp_study03, container, false)
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)

        val sOnegai  = button_onegai.clicks().map { "Onegai shimasu!" }
        val sThx     = button_thankyou.clicks().map { "Thank you!" }
        val sOutputB = edit_output.text()
        val sOutputA = text_output2.text()

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