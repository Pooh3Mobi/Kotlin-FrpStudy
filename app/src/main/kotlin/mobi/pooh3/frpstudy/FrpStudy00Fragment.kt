package mobi.pooh3.frpstudy

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class FrpStudy00Fragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_frp_study00, container, false)
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)
    }


    companion object {
        fun newInstance(): FrpStudy00Fragment {
            return FrpStudy00Fragment()
        }
    }

}// Required empty public constructor