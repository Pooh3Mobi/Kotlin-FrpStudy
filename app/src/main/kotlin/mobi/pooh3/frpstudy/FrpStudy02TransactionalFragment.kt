package mobi.pooh3.frpstudy

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.CompletableObserver
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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

        val onegai = RxView.clicks(onegaiButton).map { "Onegai shimasu!" }
        val thx = RxView.clicks(thxButton).map { "Thank you!" }
        val edit = RxTextView.text(v.findViewById(R.id.output))

        val onegaiExc = onegai.doOnNext { thxButton.isEnabled = false }.doOnComplete{ thxButton.isEnabled = true }
        val thxExc = thx.doOnNext { onegaiButton.isEnabled = false }.doOnComplete{ onegaiButton.isEnabled = true }

        val canned = onegaiExc.mergeWith(thxExc)


        canned.observeOn(Schedulers.io())
                .flatMap{ s -> heavy(s) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ s ->
                    edit.accept(s)
                }



        // not action o2
        // /o1........x
        //     /o2x

    }

    private fun heavy(s: String): Observable<String> {
        TimeUnit.SECONDS.sleep(3)
        return Observable.just(s)
    }

    companion object {
        fun newInstance(): FrpStudy02TransactionalFragment {
            return FrpStudy02TransactionalFragment()
        }
    }

}