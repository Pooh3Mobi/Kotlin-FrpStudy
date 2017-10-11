package mobi.pooh3.frpstudy

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView

class FrpStudy02Fragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_frp_study02, container, false)
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)

        val onegai = RxView.clicks(v.findViewById(R.id.onegai_shimasu)).map { "Onegai shimasu!" }
        val thx = RxView.clicks(v.findViewById(R.id.thx)).map { "Thank you!" }
        val edit = RxTextView.text(v.findViewById(R.id.output))

        val canned = onegai.mergeWith(thx)

        canned.subscribe { s -> edit.accept(s) }
    }


    companion object {
        fun newInstance(): FrpStudy02Fragment {
            return FrpStudy02Fragment()
        }
    }

}