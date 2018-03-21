package mobi.pooh3.frpstudy.extensions

import java.util.*

// CharSequence
// (CharSequence) -> Int
fun CharSequence.parseInt() =
        if (this.isEmpty()) 0
        else Integer.parseInt(this.toString())
// (CharSequence) -> Boolean
val CharSequence.isEmpty get() = this.isEmpty()

// Int
// (Int) -> String
val Int.string: String get() = Integer.toString(this)

// (T) -> Optional<T>
fun <T> T.toOptional() : Optional<T> = Optional.of(this)