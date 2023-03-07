// Generated by Dagger (https://google.github.io/dagger).
package ymsli.com.ea1h.di.module;

import com.google.gson.Gson;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class ApplicationModule_ProvideGsonFactory implements Factory<Gson> {
  private final ApplicationModule module;

  public ApplicationModule_ProvideGsonFactory(ApplicationModule module) {
    this.module = module;
  }

  @Override
  public Gson get() {
    return proxyProvideGson(module);
  }

  public static ApplicationModule_ProvideGsonFactory create(ApplicationModule module) {
    return new ApplicationModule_ProvideGsonFactory(module);
  }

  public static Gson proxyProvideGson(ApplicationModule instance) {
    return Preconditions.checkNotNull(
        instance.provideGson(), "Cannot return null from a non-@Nullable @Provides method");
  }
}