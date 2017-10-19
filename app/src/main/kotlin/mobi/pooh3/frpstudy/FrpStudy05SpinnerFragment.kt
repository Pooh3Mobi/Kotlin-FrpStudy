package mobi.pooh3.frpstudy

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.text
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.fragment_frp_study05.*
import mobi.pooh3.frpstudy.extensions.hold
import mobi.pooh3.frpstudy.extensions.loop
import mobi.pooh3.frpstudy.extensions.string

class FrpStudy05SpinnerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_frp_study05, container, false)
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)

        val value = BehaviorSubject.create<Int>().apply {
            this.map { i -> i.string }.subscribe(text_output.text())
        }
        val sPlusDelta  = button_plus.clicks().map { 1 }
        val sMinusDelta = button_minus.clicks().map { -1 }
        val sDelta = sPlusDelta.mergeWith(sMinusDelta)
        val sUpdate = sDelta.withLatestFrom(value,
                { delta, value_ -> delta + value_ })
        value.loop(sUpdate.hold(0))
    }

    companion object {
        fun newInstance(): FrpStudy05SpinnerFragment {
            return FrpStudy05SpinnerFragment()
        }
    }
}

