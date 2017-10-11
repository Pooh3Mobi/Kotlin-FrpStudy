package mobi.pooh3.frpstudy

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.BackpressureStrategy
import io.reactivex.processors.BehaviorProcessor

class FrpStudy03Fragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_frp_study03, container, false)
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)

        val edit = RxTextView.text(v.findViewById(R.id.output))
        val text = RxTextView.text(v.findViewById(R.id.output2))
        val onegai = RxView.clicks(v.findViewById(R.id.onegai_shimasu))
                .toFlowable(BackpressureStrategy.LATEST)
                .map { "Onegai shimasu!" }
        val thx = RxView.clicks(v.findViewById(R.id.thx))
                .toFlowable(BackpressureStrategy.LATEST)
                .map { "Thank you!" }
        val default = BehaviorProcessor.createDefault("default")

        val share = onegai.mergeWith(thx).share().mergeWith(default)

        share.subscribe { s -> edit.accept(s) }
        share.subscribe { s -> text.accept(s) }

    }


    companion object {
        fun newInstance(): FrpStudy03Fragment {
            return FrpStudy03Fragment()
        }
    }

}