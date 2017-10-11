package mobi.pooh3.frpstudy.rx.widget

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView

import com.jakewharton.rxbinding2.InitialValueObservable

import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

public class TextViewEmptyObservable(private val view: TextView, private val wantKnowNotEmpty: Boolean = false) : InitialValueObservable<Boolean>() {

    override fun subscribeListener(observer: Observer<in Boolean>) {
        val listener = Listener(view, observer, wantKnowNotEmpty)
        observer.onSubscribe(listener)
        view.addTextChangedListener(listener)
    }

    override fun getInitialValue(): Boolean {
        return if (wantKnowNotEmpty) view.text.isNotEmpty() else view.text.isEmpty()
    }

    internal class Listener(private val view: TextView, private val observer: Observer<in Boolean>, private val wantKnowNotEmpty: Boolean) : MainThreadDisposable(), TextWatcher {

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (!isDisposed) {
                observer.onNext(if (wantKnowNotEmpty) s.isNotEmpty() else s.isEmpty())
            }
        }

        override fun afterTextChanged(s: Editable) {}

        override fun onDispose() {
            view.removeTextChangedListener(this)
        }
    }
}