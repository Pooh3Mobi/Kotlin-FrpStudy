package mobi.pooh3.frpstudy

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.text
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.processors.PublishProcessor
import kotlinx.android.synthetic.main.fragment_frp_study02.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.Executors


class FrpStudy02TransactionalFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_frp_study02, container, false)
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)

        val sOutput = edit_output.text()
        val sOnegai = button_onegai.clicks().toFlowable(BackpressureStrategy.LATEST)
                .map { sOutput.accept(""); "Onegai shimasu!" }
        val sThx = button_thankyou.clicks().toFlowable(BackpressureStrategy.LATEST)
                .map { sOutput.accept(""); "Thank you!" }

        val sCanned = Flowable.merge(sOnegai, sThx)

        sCanned.onBackpressureDrop()
                .flatMap(this::heavy, 1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sOutput)

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