package mobi.pooh3.frpstudy

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.RxTextView
import com.jakewharton.rxbinding2.widget.text
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class FrpStudy02WithHeavyFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_frp_study02, container, false)
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)

        val onegaiButton = v.findViewById<Button>(R.id.onegai_shimasu)
        val thxButton    = v.findViewById<Button>(R.id.thx)
        val outputText   = v.findViewById<EditText>(R.id.output)

        val sOnegai = onegaiButton.clicks().map { "Onegai shimasu!" }
        val sThx    = thxButton.clicks().map { "Thank you!" }
        val sOutput = outputText.text()

        val canned = sOnegai.mergeWith(sThx)

        canned.observeOn(Schedulers.io())
                .flatMap(this::heavy)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sOutput)

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