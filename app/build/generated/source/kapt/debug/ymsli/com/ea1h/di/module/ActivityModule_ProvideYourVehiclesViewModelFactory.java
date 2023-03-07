// Generated by Dagger (https://google.github.io/dagger).
package ymsli.com.ea1h.di.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import io.reactivex.disposables.CompositeDisposable;
import javax.inject.Provider;
import ymsli.com.ea1h.EA1HRepository;
import ymsli.com.ea1h.utils.NetworkHelper;
import ymsli.com.ea1h.utils.rx.SchedulerProvider;
import ymsli.com.ea1h.views.addbike.ChassisNumberViewModel;

public final class ActivityModule_ProvideYourVehiclesViewModelFactory
    implements Factory<ChassisNumberViewModel> {
  private final ActivityModule module;

  private final Provider<SchedulerProvider> schedulerProvider;

  private final Provider<CompositeDisposable> compositeDisposableProvider;

  private final Provider<NetworkHelper> networkHelperProvider;

  private final Provider<EA1HRepository> ea1hRepositoryProvider;

  public ActivityModule_ProvideYourVehiclesViewModelFactory(
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
  public ChassisNumberViewModel get() {
    return proxyProvideYourVehiclesViewModel(
        module,
        schedulerProvider.get(),
        compositeDisposableProvider.get(),
        networkHelperProvider.get(),
        ea1hRepositoryProvider.get());
  }

  public static ActivityModule_ProvideYourVehiclesViewModelFactory create(
      ActivityModule module,
      Provider<SchedulerProvider> schedulerProvider,
      Provider<CompositeDisposable> compositeDisposableProvider,
      Provider<NetworkHelper> networkHelperProvider,
      Provider<EA1HRepository> ea1hRepositoryProvider) {
    return new ActivityModule_ProvideYourVehiclesViewModelFactory(
        module,
        schedulerProvider,
        compositeDisposableProvider,
        networkHelperProvider,
        ea1hRepositoryProvider);
  }

  public static ChassisNumberViewModel proxyProvideYourVehiclesViewModel(
      ActivityModule instance,
      SchedulerProvider schedulerProvider,
      CompositeDisposable compositeDisposable,
      NetworkHelper networkHelper,
      EA1HRepository ea1hRepository) {
    return Preconditions.checkNotNull(
        instance.provideYourVehiclesViewModel(
            schedulerProvider, compositeDisposable, networkHelper, ea1hRepository),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
