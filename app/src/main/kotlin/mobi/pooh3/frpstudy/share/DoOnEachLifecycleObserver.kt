package mobi.pooh3.frpstudy.share

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class SimpleDisposableRxLifecycleObserver : LifecycleObserver {
    private val disposables: CompositeDisposable = CompositeDisposable()
    fun addAll(vararg disposable: Disposable) = disposables.addAll(*disposable)
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStopped() =  { if (!disposables.isDisposed) disposables.dispose() }
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroyed() =  { if (!disposables.isDisposed) disposables.dispose() }
}

class DoOnEachLifecycleObserver : LifecycleObserver {
    private var onCreateRunner: DoOnEachRunner? = null
    private var onResumeRunner: DoOnEachRunner? = null
    private var onStartRunner: DoOnEachRunner? = null
    private var onPauseRunner: DoOnEachRunner? = null
    private var onStopRunner: DoOnEachRunner? = null
    private var onDestroyRunner: DoOnEachRunner? = null


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreated() = onCreateRunner?.run()
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStarted() = onStartRunner?.run()
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResumed() = onResumeRunner?.run()
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPaused() = onPauseRunner?.run()
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStopped() = onStopRunner?.run()
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroyed() = onDestroyRunner?.run()

    public fun doOnCreatet(run: () -> Unit): DoOnEachLifecycleObserver {
        onCreateRunner = DoOnEachRunner(run)
        return this
    }

    public fun doOnStart(run: () -> Unit): DoOnEachLifecycleObserver {
        onStartRunner = DoOnEachRunner(run)
        return this
    }

    public fun doOnResume(run: () -> Unit): DoOnEachLifecycleObserver {
        onResumeRunner = DoOnEachRunner(run)
        return this
    }

    public fun doOnPause(run: () -> Unit): DoOnEachLifecycleObserver {
        onPauseRunner = DoOnEachRunner(run)
        return this
    }
    public fun doOnStop(run: () -> Unit): DoOnEachLifecycleObserver {
        onStopRunner = DoOnEachRunner(run)
        return this
    }
    public fun doOnDestroy(run: () -> Unit): DoOnEachLifecycleObserver {
        onDestroyRunner = DoOnEachRunner(run)
        return this
    }
    inner class DoOnEachRunner(private val runner: () -> Unit) {
        public fun run() = runner.invoke()
    }
}
