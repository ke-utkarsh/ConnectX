// Generated by Dagger (https://google.github.io/dagger).
package ymsli.com.ea1h.services;

import dagger.MembersInjector;
import io.reactivex.disposables.CompositeDisposable;
import javax.inject.Provider;
import ymsli.com.ea1h.EA1HRepository;
import ymsli.com.ea1h.utils.rx.SchedulerProvider;

public final class FCMService_MembersInjector implements MembersInjector<FCMService> {
  private final Provider<EA1HRepository> eA1HRepositoryProvider;

  private final Provider<SchedulerProvider> schedulerProvider;

  private final Provider<CompositeDisposable> compositeDisposableProvider;

  public FCMService_MembersInjector(
      Provider<EA1HRepository> eA1HRepositoryProvider,
      Provider<SchedulerProvider> schedulerProvider,
      Provider<CompositeDisposable> compositeDisposableProvider) {
    this.eA1HRepositoryProvider = eA1HRepositoryProvider;
    this.schedulerProvider = schedulerProvider;
    this.compositeDisposableProvider = compositeDisposableProvider;
  }

  public static MembersInjector<FCMService> create(
      Provider<EA1HRepository> eA1HRepositoryProvider,
      Provider<SchedulerProvider> schedulerProvider,
      Provider<CompositeDisposable> compositeDisposableProvider) {
    return new FCMService_MembersInjector(
        eA1HRepositoryProvider, schedulerProvider, compositeDisposableProvider);
  }

  @Override
  public void injectMembers(FCMService instance) {
    injectEA1HRepository(instance, eA1HRepositoryProvider.get());
    injectSchedulerProvider(instance, schedulerProvider.get());
    injectCompositeDisposable(instance, compositeDisposableProvider.get());
  }

  public static void injectEA1HRepository(FCMService instance, EA1HRepository eA1HRepository) {
    instance.eA1HRepository = eA1HRepository;
  }

  public static void injectSchedulerProvider(
      FCMService instance, SchedulerProvider schedulerProvider) {
    instance.schedulerProvider = schedulerProvider;
  }

  public static void injectCompositeDisposable(
      FCMService instance, CompositeDisposable compositeDisposable) {
    instance.compositeDisposable = compositeDisposable;
  }
}