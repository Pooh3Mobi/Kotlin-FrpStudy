package mobi.pooh3.frpstudy.extensions

// CharSequence
// (CharSequence) -> Int
fun CharSequence.parseInt() =
        if (this.isEmpty()) 0
        else Integer.parseInt(this.toString())
// (CharSequence) -> Boolean
val CharSequence.isEmpty get() = this.isEmpty()

// Int
// (Int) -> String
val Int.string get() = Integer.toString(this)
