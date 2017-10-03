package mobi.pooh3.frpstudy

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.processors.BehaviorProcessor

class FrpStudy04Fragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_frp_study04, container, false)
    }

    override fun onViewCreated(v: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(v!!, savedInstanceState)

        val english = RxTextView.textChanges(v.findViewById(R.id.input))
        val translate = RxView.clicks(v.findViewById(R.id.translate))

        val sLatin = translate.snapShot(english, { txt ->
            txt.trim().replace(Regex(""" |$"""), "us ").trim()
        })

        val latin = sLatin.hold("enter")

        RxTextView.text(v.findViewById(R.id.output))(latin)

    }

    companion object {
        fun newInstance(): FrpStudy04Fragment {
            return FrpStudy04Fragment()
        }
    }
}

fun <B, C> Observable<Any>.snapShot(t: Observable<B>, block: (B) -> C): BehaviorProcessor<C> =
        this.let{ o ->
            BehaviorProcessor.create<C>()
                .apply { o.subscribe { t.subscribe { s -> this.onNext(block(s)) }.dispose() } }
        }

fun <T> BehaviorProcessor<T>.hold(t: T): BehaviorProcessor<T> =
        this.apply { onNext(t) }

operator fun Consumer<in CharSequence>.invoke(s: BehaviorProcessor<String>): Disposable =
        s.subscribe { t-> this.accept(t) }

