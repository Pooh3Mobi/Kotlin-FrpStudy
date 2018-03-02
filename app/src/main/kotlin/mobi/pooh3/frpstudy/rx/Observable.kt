package mobi.pooh3.frpstudy.rx

import io.reactivex.Observable
import java.util.*


fun <T> Observable<Optional<T>>.unOptional(): Observable<T> =
        this.flatMap { o: Optional<T> ->
            if (o.isPresent) Observable.just(o.get()) else Observable.empty<T>() }
