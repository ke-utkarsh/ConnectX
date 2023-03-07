package ymsli.com.ea1h.viewmodeltests

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import ymsli.com.ea1h.EA1HRepository
import ymsli.com.ea1h.SchedulerProviderTest
import ymsli.com.ea1h.database.entity.MiscDataEntity
import ymsli.com.ea1h.utils.NetworkHelper
import ymsli.com.ea1h.views.splash.SplashViewModel

@RunWith(MockitoJUnitRunner::class)
class SplashViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var networkHelper: NetworkHelper

    @Mock
    private lateinit var repository: EA1HRepository

    private lateinit var splashViewModel: SplashViewModel

    private lateinit var testScheduler: TestScheduler

    /**
     * variables need to be initialized
     * before execution of the test
     */
    @Before
    fun setup(){
        val compositeDisposable = CompositeDisposable()
        testScheduler = TestScheduler()
        val schedulerProviderTest = SchedulerProviderTest(testScheduler)
        splashViewModel = SplashViewModel(
            schedulerProviderTest,
            compositeDisposable,
            networkHelper,
            repository
        )
    }

    //@Test
    fun checkGetBluetoothCommands(){
        val btResponse = String()

        doReturn(true)
            .`when`(networkHelper)
            .isNetworkConnected()
/*
        doReturn(Observable.just(btResponse))
            .`when`(repository)
            .getBTCommands()

        splashViewModel.getBluetoothCommands()
        verify(repository).getBTCommands()*/
    }

    //@Test
    fun checkGetMiscData(){
        val miscDataEntity = ArrayList<MiscDataEntity>()

        doReturn(true)
            .`when`(networkHelper)
            .isNetworkConnected()

        doReturn(Observable.just(miscDataEntity))
            .`when`(repository)
            .getMiscData()
/*
        splashViewModel.getMiscData()
        verify(repository).getMiscData()*/
    }
}