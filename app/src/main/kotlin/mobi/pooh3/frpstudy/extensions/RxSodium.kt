package mobi.pooh3.frpstudy.extensions

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.subjects.BehaviorSubject
import mobi.pooh3.frpstudy.extensions.rx.unOptional
import java.util.*

fun <T> Observable<T>.hold(value: T): BehaviorSubject<T> =
        BehaviorSubject.createDefault(value).apply { this@hold.subscribe(this) }

fun <T> BehaviorSubject<T>.loop(stream: Observable<T>): Disposable =
        stream.subscribe { value -> this.onNext(value) }

fun <T>Observable<T>.gate(c: Observable<Boolean>) =
        this.withLatestFrom(c, BiFunction<T, Boolean, Optional<T>> { t, pred ->
            if (pred)
                Optional.of(t)
            else
                Optional.empty()
        }).unOptional()


fun <R> Observable<*>.snapshot(r: Observable<R>): Observable<R> =
        this.withLatestFrom(r, { _, r_ -> r_ })
