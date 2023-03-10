// Generated by Dagger (https://google.github.io/dagger).
package ymsli.com.ea1h.di.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import ymsli.com.ea1h.database.EA1HDatabase;

public final class ApplicationModule_ProvideEA1HDatabaseFactory implements Factory<EA1HDatabase> {
  private final ApplicationModule module;

  public ApplicationModule_ProvideEA1HDatabaseFactory(ApplicationModule module) {
    this.module = module;
  }

  @Override
  public EA1HDatabase get() {
    return proxyProvideEA1HDatabase(module);
  }

  public static ApplicationModule_ProvideEA1HDatabaseFactory create(ApplicationModule module) {
    return new ApplicationModule_ProvideEA1HDatabaseFactory(module);
  }

  public static EA1HDatabase proxyProvideEA1HDatabase(ApplicationModule instance) {
    return Preconditions.checkNotNull(
        instance.provideEA1HDatabase(), "Cannot return null from a non-@Nullable @Provides method");
  }
}
