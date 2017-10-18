package mobi.pooh3.frpstudy.extensions

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

fun FragmentActivity.replaceFragment(id: Int, fragment: Fragment, tag: String) =
        supportFragmentManager.beginTransaction().replace(id, fragment, tag).commit()