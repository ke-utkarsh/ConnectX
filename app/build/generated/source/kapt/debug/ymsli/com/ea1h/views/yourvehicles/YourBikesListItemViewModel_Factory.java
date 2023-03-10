// Generated by Dagger (https://google.github.io/dagger).
package ymsli.com.ea1h.views.yourvehicles;

import dagger.internal.Factory;
import io.reactivex.disposables.CompositeDisposable;
import javax.inject.Provider;
import ymsli.com.ea1h.EA1HRepository;
import ymsli.com.ea1h.utils.NetworkHelper;
import ymsli.com.ea1h.utils.rx.SchedulerProvider;

public final class YourBikesListItemViewModel_Factory
    implements Factory<YourBikesListItemViewModel> {
  private final Provider<SchedulerProvider> schedulerProvider;

  private final Provider<CompositeDisposable> compositeDisposableProvider;

  private final Provider<NetworkHelper> networkHelperProvider;

  private final Provider<EA1HRepository> eA1HRepositoryProvider;

  public YourBikesListItemViewModel_Factory(
      Provider<SchedulerProvider> schedulerProvider,
      Provider<CompositeDisposable> compositeDisposableProvider,
      Provider<NetworkHelper> networkHelperProvider,
      Provider<EA1HRepository> eA1HRepositoryProvider) {
    this.schedulerProvider = schedulerProvider;
    this.compositeDisposableProvider = compositeDisposableProvider;
    this.networkHelperProvider = networkHelperProvider;
    this.eA1HRepositoryProvider = eA1HRepositoryProvider;
  }

  @Override
  public YourBikesListItemViewModel get() {
    return new YourBikesListItemViewModel(
        schedulerProvider.get(),
        compositeDisposableProvider.get(),
        networkHelperProvider.get(),
        eA1HRepositoryProvider.get());
  }

  public static YourBikesListItemViewModel_Factory create(
      Provider<SchedulerProvider> schedulerProvider,
      Provider<CompositeDisposable> compositeDisposableProvider,
      Provider<NetworkHelper> networkHelperProvider,
      Provider<EA1HRepository> eA1HRepositoryProvider) {
    return new YourBikesListItemViewModel_Factory(
        schedulerProvider,
        compositeDisposableProvider,
        networkHelperProvider,
        eA1HRepositoryProvider);
  }

  public static YourBikesListItemViewModel newYourBikesListItemViewModel(
      SchedulerProvider schedulerProvider,
      CompositeDisposable compositeDisposable,
      NetworkHelper networkHelper,
      EA1HRepository eA1HRepository) {
    return new YourBikesListItemViewModel(
        schedulerProvider, compositeDisposable, networkHelper, eA1HRepository);
  }
}
