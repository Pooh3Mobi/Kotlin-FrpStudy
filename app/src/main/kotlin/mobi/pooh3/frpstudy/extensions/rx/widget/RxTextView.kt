package mobi.pooh3.frpstudy.extensions.rx.widget

import android.widget.TextView
import com.jakewharton.rxbinding2.InitialValueObservable
import mobi.pooh3.frpstudy.extensions.rx.widget.TextViewEmptyObservable

public fun TextView.textEmpty(): InitialValueObservable<Boolean> { return TextViewEmptyObservable(this) }
public fun TextView.textNotEmpty(): InitialValueObservable<Boolean> { return TextViewEmptyObservable(this, true) }