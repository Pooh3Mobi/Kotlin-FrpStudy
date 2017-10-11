package mobi.pooh3.frpstudy.extensions

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

fun <T> Observable<T>.hold(t: T): BehaviorSubject<T> =
        let { o ->
            BehaviorSubject.createDefault(t)
                    .apply { o.subscribe(this) }
        }

fun <T> BehaviorSubject<T>.loop(s: Observable<T>): Unit =
        let{ c -> s.subscribe { value -> c.onNext(value) } }