package mobi.pooh3.frpstudy.extensions

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

fun <T> Observable<T>.hold(value: T): BehaviorSubject<T> =
        BehaviorSubject.createDefault(value).apply { this@hold.subscribe(this) }

fun <T> BehaviorSubject<T>.loop(stream: Observable<T>): Disposable =
        stream.subscribe { value -> this.onNext(value) }