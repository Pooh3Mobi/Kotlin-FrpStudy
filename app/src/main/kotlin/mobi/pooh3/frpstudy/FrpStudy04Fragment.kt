package mobi.pooh3.frpstudy

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.processors.BehaviorProcessor

class FrpStudy04Fragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_frp_study04, container, false)
    }

    override fun onViewCreated(v: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(v!!, savedInstanceState)

        val input = RxTextView.textChanges(v.findViewById(R.id.input))
        val output = RxTextView.text(v.findViewById(R.id.output))
        val translate = RxView.clicks(v.findViewById(R.id.translate))
        val latin: (CharSequence) -> String = {
            s -> s.toString().trim().replace(Regex(""" |$"""), "us ").trim()
        }

        // for snapshot
        val inputProcessor = BehaviorProcessor.create<CharSequence>()

        input.subscribe { s -> inputProcessor.onNext(s) }

        translate.subscribe{ inputProcessor.subscribe { s -> output.accept(latin(s)) }.dispose() }
    }


    companion object {
        fun newInstance(): FrpStudy04Fragment {
            return FrpStudy04Fragment()
        }
    }

}