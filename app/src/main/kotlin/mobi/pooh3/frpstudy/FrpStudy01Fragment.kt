package mobi.pooh3.frpstudy


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.widget.text
import com.jakewharton.rxbinding2.widget.textChanges
import kotlinx.android.synthetic.main.fragment_frp_study01.*


class FrpStudy01Fragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_frp_study01, container, false)
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)

        val sOutput = text_output.text()
        val sInput  = edit_input.textChanges()

        sInput.subscribe(sOutput)
    }


    companion object {
        fun newInstance(): FrpStudy01Fragment {
            return FrpStudy01Fragment()
        }
    }

}
