package mobi.pooh3.frpstudy.rx

import io.reactivex.Observable
import io.reactivex.annotations.NonNull
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import java.util.*


fun <T> Observable<Optional<T>>.unOptional(): Observable<T> =
        this.flatMap { o: Optional<T> ->
            if (o.isPresent) Observable.just(o.get()) else Observable.empty<T>() }


inline fun <T1,T2,R> Observable<T1>.combineLatest(source2: Observable<T2>, crossinline combineFunction: (T1, T2) -> R): Observable<R> =
        Observable.combineLatest(this, source2,
                BiFunction<T1, T2, R> { t1, t2 -> combineFunction(t1,t2) })!!

inline fun <T1,T2,T3,R> Observable<T1>.combineLatest(source2: Observable<T2>, source3: Observable<T3>, crossinline combineFunction: (T1, T2, T3) -> R): Observable<R> =
        Observable.combineLatest(this, source2, source3,
                Function3<T1, T2, T3, R> { t1, t2, t3-> combineFunction(t1, t2, t3) })!!


