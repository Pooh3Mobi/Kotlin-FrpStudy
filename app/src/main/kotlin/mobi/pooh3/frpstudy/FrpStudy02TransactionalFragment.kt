package mobi.pooh3.frpstudy

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.processors.PublishProcessor
import java.util.concurrent.TimeUnit
import java.util.concurrent.Executors


class FrpStudy02TransactionalFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_frp_study02, container, false)
    }

    override fun onViewCreated(v: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(v!!, savedInstanceState)

        val onegaiButton = v.findViewById<Button>(R.id.onegai_shimasu)
        val thxButton    = v.findViewById<Button>(R.id.thx)

        val edit   = RxTextView.text(v.findViewById(R.id.output))
        val onegai = RxView.clicks(onegaiButton)
                .toFlowable(BackpressureStrategy.LATEST)
                .map { edit.accept(""); "Onegai shimasu!" }
        val thx = RxView.clicks(thxButton)
                .toFlowable(BackpressureStrategy.LATEST)
                .map { edit.accept(""); "Thank you!" }

        val canned = Flowable.merge(onegai, thx)

        canned.onBackpressureDrop()
                .flatMap(this::heavy, 1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(edit::accept)

        // not action o2
        // /o1........x
        //     /o2x

    }

    private fun heavy(s: String): Flowable<String> {
        val processor = PublishProcessor.create<String>()
        Executors.newSingleThreadExecutor().execute {
            TimeUnit.SECONDS.sleep(3)
            processor.onNext(s)
            processor.onComplete()
        }
        return processor
    }

    companion object {
        fun newInstance(): FrpStudy02TransactionalFragment {
            return FrpStudy02TransactionalFragment()
        }
    }
}