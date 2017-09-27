package mobi.pooh3.frpstudy


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.widget.RxTextView


class FrpStudy01Fragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_frp_study01, container, false)
    }

    override fun onViewCreated(v: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(v!!, savedInstanceState)

        val text = RxTextView.text(v.findViewById(R.id.output))
        val edit = RxTextView.textChanges(v.findViewById(R.id.input))

        edit.subscribe { c -> text.accept(c) }

    }


    companion object {
        fun newInstance(): FrpStudy01Fragment {
            return FrpStudy01Fragment()
        }
    }

}
