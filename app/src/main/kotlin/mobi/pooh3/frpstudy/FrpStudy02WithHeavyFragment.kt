package mobi.pooh3.frpstudy

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class FrpStudy02WithHeavyFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_frp_study02, container, false)
    }

    override fun onViewCreated(v: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(v!!, savedInstanceState)

        val onegai = RxView.clicks(v.findViewById(R.id.onegai_shimasu)).map { "Onegai shimasu!" }
        val thx = RxView.clicks(v.findViewById(R.id.thx)).map { "Thank you!" }
        val edit = RxTextView.text(v.findViewById(R.id.output))

        val canned = onegai.mergeWith(thx)

        canned.observeOn(Schedulers.io())
                .flatMap(this::heavy)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { s -> edit.accept(s) }

        // not parallel like...
        // /o1........x
        //     /o2........x

        // just serial like this.
        // /o1........x/o2.........x
    }

    private fun heavy(s: String): Observable<String> {
        TimeUnit.SECONDS.sleep(3)
        return Observable.just(s)
    }

    companion object {
        fun newInstance(): FrpStudy02WithHeavyFragment {
            return FrpStudy02WithHeavyFragment()
        }
    }

}