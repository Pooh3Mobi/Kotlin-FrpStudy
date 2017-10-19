package mobi.pooh3.frpstudy

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.widget.text
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.rxkotlin.Observables
import kotlinx.android.synthetic.main.fragment_frp_study06.*
import mobi.pooh3.frpstudy.extensions.parseInt
import mobi.pooh3.frpstudy.extensions.string

class FrpStudy06LiftFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_frp_study06, container, false)
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)

        val a = edit_a.textChanges()
                .map { s -> s.parseInt() }
        val b = edit_b.textChanges()
                .map { s -> s.parseInt() }

        val sum = Observables.combineLatest(a, b, {a_, b_  -> a_ + b_})

        sum.map{ i -> i.string }.subscribe(text_output.text())
    }

    companion object {
        fun newInstance(): FrpStudy06LiftFragment {
            return FrpStudy06LiftFragment()
        }
    }
}