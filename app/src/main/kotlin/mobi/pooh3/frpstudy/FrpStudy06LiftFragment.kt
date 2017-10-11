package mobi.pooh3.frpstudy

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.jakewharton.rxbinding2.widget.text
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.rxkotlin.Observables

class FrpStudy06LiftFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_frp_study06, container, false)
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)

        val textA  = v.findViewById<EditText>(R.id.text_a)
        val textB  = v.findViewById<EditText>(R.id.text_b)
        val output = v.findViewById<TextView>(R.id.output)

        val a = textA.textChanges()
                .map { s -> if (s.isEmpty()) 0 else Integer.parseInt(s.toString()) }
        val b = textB.textChanges()
                .map { s -> if (s.isEmpty()) 0 else Integer.parseInt(s.toString()) }

        val sum = Observables.combineLatest(a, b, {a_, b_  -> a_ + b_})

        sum.map{ i -> Integer.toString(i) }.subscribe(output.text())
    }

    companion object {
        fun newInstance(): FrpStudy06LiftFragment {
            return FrpStudy06LiftFragment()
        }
    }
}