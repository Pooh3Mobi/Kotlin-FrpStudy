package mobi.pooh3.frpstudy

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class FrpStudy02TransactionalFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_frp_study02, container, false)
    }

    override fun onViewCreated(v: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(v!!, savedInstanceState)

        val onegaiButton = v.findViewById<Button>(R.id.onegai_shimasu)
        val thxButton = v.findViewById<Button>(R.id.thx)

        val edit = RxTextView.text(v.findViewById(R.id.output))
        val onegai = RxView.clicks(onegaiButton).map { edit.accept(""); "Onegai shimasu!" }
        val thx = RxView.clicks(thxButton).map { edit.accept(""); "Thank you!" }


        val canned = Observable.merge(onegai, thx).toFlowable(BackpressureStrategy.DROP)

        val processor = PublishProcessor.create<String>()


        canned.observeOn(Schedulers.computation(), true, 1)
                .flatMap{ s -> heavy(processor, s) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{s->
                    edit.accept(s)
                    Log.d("test", "onNext" + s)
                }

        // not action o2
        // /o1........x
        //     /o2x

    }

    private fun heavy(processor: PublishProcessor<String>, s: String): Flowable<String> {
        Log.d("test", "start heavy:" + s)
        TimeUnit.SECONDS.sleep(3)
        processor.onNext(s)
        return processor
    }

    companion object {
        fun newInstance(): FrpStudy02TransactionalFragment {
            return FrpStudy02TransactionalFragment()
        }
    }
}