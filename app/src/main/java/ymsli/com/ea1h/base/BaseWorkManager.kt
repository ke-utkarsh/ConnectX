package ymsli.com.ea1h.base

import android.content.Context
import android.net.ConnectivityManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import io.reactivex.disposables.CompositeDisposable
import ymsli.com.ea1h.EA1HApplication
import ymsli.com.ea1h.EA1HRepository
import ymsli.com.ea1h.di.component.DaggerJobServiceComponent
import ymsli.com.ea1h.di.component.JobServiceComponent
import ymsli.com.ea1h.utils.rx.SchedulerProvider
import javax.inject.Inject

abstract class BaseWorkManager(private val context: Context, workerParameters: WorkerParameters) : Worker(context,workerParameters)  {

    @Inject
    lateinit var eA1HRepository: EA1HRepository

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    @Inject
    lateinit var schedulerProvider: SchedulerProvider

    abstract fun inject(jobServiceComponent: JobServiceComponent)

    private fun buildWorkManagerComponent() =
        DaggerJobServiceComponent
            .builder()
            .applicationComponent((context.applicationContext as EA1HApplication).applicationComponent)
            .build()

    fun init(){
        inject(buildWorkManagerComponent())
    }

    /**
     * determines if internet is there or not
     */
    fun isNetworkConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork?.isConnected ?: false
    }
}