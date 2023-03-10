// Generated by Dagger (https://google.github.io/dagger).
package ymsli.com.ea1h.base;

import dagger.MembersInjector;
import io.reactivex.disposables.CompositeDisposable;
import javax.inject.Provider;
import ymsli.com.ea1h.EA1HRepository;
import ymsli.com.ea1h.utils.rx.SchedulerProvider;

public final class BaseJobService_MembersInjector implements MembersInjector<BaseJobService> {
  private final Provider<EA1HRepository> eA1HRepositoryProvider;

  private final Provider<SchedulerProvider> schedulerProvider;

  private final Provider<CompositeDisposable> compositeDisposableProvider;

  public BaseJobService_MembersInjector(
      Provider<EA1HRepository> eA1HRepositoryProvider,
      Provider<SchedulerProvider> schedulerProvider,
      Provider<CompositeDisposable> compositeDisposableProvider) {
    this.eA1HRepositoryProvider = eA1HRepositoryProvider;
    this.schedulerProvider = schedulerProvider;
    this.compositeDisposableProvider = compositeDisposableProvider;
  }

  public static MembersInjector<BaseJobService> create(
      Provider<EA1HRepository> eA1HRepositoryProvider,
      Provider<SchedulerProvider> schedulerProvider,
      Provider<CompositeDisposable> compositeDisposableProvider) {
    return new BaseJobService_MembersInjector(
        eA1HRepositoryProvider, schedulerProvider, compositeDisposableProvider);
  }

  @Override
  public void injectMembers(BaseJobService instance) {
    injectEA1HRepository(instance, eA1HRepositoryProvider.get());
    injectSchedulerProvider(instance, schedulerProvider.get());
    injectCompositeDisposable(instance, compositeDisposableProvider.get());
  }

  public static void injectEA1HRepository(BaseJobService instance, EA1HRepository eA1HRepository) {
    instance.eA1HRepository = eA1HRepository;
  }

  public static void injectSchedulerProvider(
      BaseJobService instance, SchedulerProvider schedulerProvider) {
    instance.schedulerProvider = schedulerProvider;
  }

  public static void injectCompositeDisposable(
      BaseJobService instance, CompositeDisposable compositeDisposable) {
    instance.compositeDisposable = compositeDisposable;
  }
}
