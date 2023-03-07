// Generated by Dagger (https://google.github.io/dagger).
package ymsli.com.ea1h.services;

import dagger.MembersInjector;
import io.reactivex.disposables.CompositeDisposable;
import javax.inject.Provider;
import ymsli.com.ea1h.EA1HRepository;
import ymsli.com.ea1h.base.BaseJobService_MembersInjector;
import ymsli.com.ea1h.utils.rx.SchedulerProvider;

public final class ZipUploadService_MembersInjector implements MembersInjector<ZipUploadService> {
  private final Provider<EA1HRepository> eA1HRepositoryProvider;

  private final Provider<SchedulerProvider> schedulerProvider;

  private final Provider<CompositeDisposable> compositeDisposableProvider;

  public ZipUploadService_MembersInjector(
      Provider<EA1HRepository> eA1HRepositoryProvider,
      Provider<SchedulerProvider> schedulerProvider,
      Provider<CompositeDisposable> compositeDisposableProvider) {
    this.eA1HRepositoryProvider = eA1HRepositoryProvider;
    this.schedulerProvider = schedulerProvider;
    this.compositeDisposableProvider = compositeDisposableProvider;
  }

  public static MembersInjector<ZipUploadService> create(
      Provider<EA1HRepository> eA1HRepositoryProvider,
      Provider<SchedulerProvider> schedulerProvider,
      Provider<CompositeDisposable> compositeDisposableProvider) {
    return new ZipUploadService_MembersInjector(
        eA1HRepositoryProvider, schedulerProvider, compositeDisposableProvider);
  }

  @Override
  public void injectMembers(ZipUploadService instance) {
    BaseJobService_MembersInjector.injectEA1HRepository(instance, eA1HRepositoryProvider.get());
    BaseJobService_MembersInjector.injectSchedulerProvider(instance, schedulerProvider.get());
    BaseJobService_MembersInjector.injectCompositeDisposable(
        instance, compositeDisposableProvider.get());
  }
}