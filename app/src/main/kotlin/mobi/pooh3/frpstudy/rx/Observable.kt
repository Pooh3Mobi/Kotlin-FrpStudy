package mobi.pooh3.frpstudy.rx

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import java.util.*


fun <T> Observable<Optional<T>>.unOptional(): Observable<T> =
        this.flatMap { o: Optional<T> ->
            if (o.isPresent) Observable.just(o.get()) else Observable.empty<T>() }


inline fun <T1,T2,R> Observable<T1>.combineLatest(source2: Observable<T2>, crossinline combineFunction: (T1, T2) -> R): Observable<R> =
        Observable.combineLatest(this, source2,
                BiFunction<T1, T2, R> { t1, t2 -> combineFunction(t1,t2) })!!

