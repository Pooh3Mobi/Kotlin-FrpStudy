package mobi.pooh3.frpstudy.extensions

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

fun <T> Observable<T>.hold(t: T): BehaviorSubject<T> =
        this.let { o ->
            BehaviorSubject.createDefault(t)
                    .apply { o.subscribe(this) }
        }
