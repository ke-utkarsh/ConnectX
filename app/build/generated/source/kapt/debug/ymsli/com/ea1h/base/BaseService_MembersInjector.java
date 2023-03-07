// Generated by Dagger (https://google.github.io/dagger).
package ymsli.com.ea1h.base;

import dagger.MembersInjector;
import javax.inject.Provider;
import ymsli.com.ea1h.EA1HRepository;

public final class BaseService_MembersInjector implements MembersInjector<BaseService> {
  private final Provider<EA1HRepository> eA1HRepositoryProvider;

  public BaseService_MembersInjector(Provider<EA1HRepository> eA1HRepositoryProvider) {
    this.eA1HRepositoryProvider = eA1HRepositoryProvider;
  }

  public static MembersInjector<BaseService> create(
      Provider<EA1HRepository> eA1HRepositoryProvider) {
    return new BaseService_MembersInjector(eA1HRepositoryProvider);
  }

  @Override
  public void injectMembers(BaseService instance) {
    injectEA1HRepository(instance, eA1HRepositoryProvider.get());
  }

  public static void injectEA1HRepository(BaseService instance, EA1HRepository eA1HRepository) {
    instance.eA1HRepository = eA1HRepository;
  }
}
