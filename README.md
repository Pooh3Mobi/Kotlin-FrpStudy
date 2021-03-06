# Kotlin-FrpStudy
 map (Sodium) -> RxKotlin. Learn with a Book: "Reactive Functional Programming". 
 
 
 ```Kotlin
     override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)
        
        /** frp spinner **/
        
        val value = BehaviorSubject.create<Int>().apply {
            this.map { i -> i.string }.subscribe(text_output.text())
        }
        val sPlusDelta  = button_plus.clicks().map { 1 }
        val sMinusDelta = button_minus.clicks().map { -1 }
        val sDelta = sPlusDelta.mergeWith(sMinusDelta)
        val sUpdate = sDelta.withLatestFrom(value,
                { delta, value_ -> delta + value_ })
        value.loop(sUpdate.hold(0))
        
    }
 ```

### link of book for study frp

eng.  
http://a.co/8rtUduO

jp.  
http://amzn.to/2kFZhaJ


### Using Library for FRP in this project

Sodium  
https://github.com/SodiumFRP/sodium/

RxJava  
https://github.com/ReactiveX/RxJava

RxBindig  
https://github.com/JakeWharton/RxBinding

RxKotlin  
https://github.com/ReactiveX/RxKotlin

### Using others
- plugin : "Kotlin Android Extensions" for simplify code.  
https://kotlinlang.org/docs/tutorials/android-plugin.html
