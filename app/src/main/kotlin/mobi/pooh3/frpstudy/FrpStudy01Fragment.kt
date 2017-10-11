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


class FrpStudy01Fragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_frp_study01, container, false)
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)

        val outputText = v.findViewById<TextView>(R.id.output)
        val inputText  = v.findViewById<EditText>(R.id.input)

        val sOutput = outputText.text()
        val sInput  = inputText.textChanges()

        sInput.subscribe(sOutput)
    }


    companion object {
        fun newInstance(): FrpStudy01Fragment {
            return FrpStudy01Fragment()
        }
    }

}
