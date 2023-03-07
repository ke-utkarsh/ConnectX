// Generated by Dagger (https://google.github.io/dagger).
package ymsli.com.ea1h.di.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import io.reactivex.disposables.CompositeDisposable;
import javax.inject.Provider;
import ymsli.com.ea1h.EA1HRepository;
import ymsli.com.ea1h.utils.NetworkHelper;
import ymsli.com.ea1h.utils.rx.SchedulerProvider;
import ymsli.com.ea1h.views.home.drivinghistory.TripHistoryDetailViewModel;

public final class ActivityModule_ProvideTripHistoryDetailViewModelFactory
    implements Factory<TripHistoryDetailViewModel> {
  private final ActivityModule module;

  private final Provider<SchedulerProvider> schedulerProvider;

  private final Provider<CompositeDisposable> compositeDisposableProvider;

  private final Provider<NetworkHelper> networkHelperProvider;

  private final Provider<EA1HRepository> ea1hRepositoryProvider;

  public ActivityModule_ProvideTripHistoryDetailViewModelFactory(
      ActivityModule module,
      Provider<SchedulerProvider> schedulerProvider,
      Provider<CompositeDisposable> compositeDisposableProvider,
      Provider<NetworkHelper> networkHelperProvider,
      Provider<EA1HRepository> ea1hRepositoryProvider) {
    this.module = module;
    this.schedulerProvider = schedulerProvider;
    this.compositeDisposableProvider = compositeDisposableProvider;
    this.networkHelperProvider = networkHelperProvider;
    this.ea1hRepositoryProvider = ea1hRepositoryProvider;
  }

  @Override
  public TripHistoryDetailViewModel get() {
    return proxyProvideTripHistoryDetailViewModel(
        module,
        schedulerProvider.get(),
        compositeDisposableProvider.get(),
        networkHelperProvider.get(),
        ea1hRepositoryProvider.get());
  }

  public static ActivityModule_ProvideTripHistoryDetailViewModelFactory create(
      ActivityModule module,
      Provider<SchedulerProvider> schedulerProvider,
      Provider<CompositeDisposable> compositeDisposableProvider,
      Provider<NetworkHelper> networkHelperProvider,
      Provider<EA1HRepository> ea1hRepositoryProvider) {
    return new ActivityModule_ProvideTripHistoryDetailViewModelFactory(
        module,
        schedulerProvider,
        compositeDisposableProvider,
        networkHelperProvider,
        ea1hRepositoryProvider);
  }

  public static TripHistoryDetailViewModel proxyProvideTripHistoryDetailViewModel(
      ActivityModule instance,
      SchedulerProvider schedulerProvider,
      CompositeDisposable compositeDisposable,
      NetworkHelper networkHelper,
      EA1HRepository ea1hRepository) {
    return Preconditions.checkNotNull(
        instance.provideTripHistoryDetailViewModel(
            schedulerProvider, compositeDisposable, networkHelper, ea1hRepository),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
