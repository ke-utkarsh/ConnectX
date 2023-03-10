// Generated by Dagger (https://google.github.io/dagger).
package ymsli.com.ea1h.di.module;

import com.google.gson.Gson;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class FragmentModule_ProvideGsonFactory implements Factory<Gson> {
  private final FragmentModule module;

  public FragmentModule_ProvideGsonFactory(FragmentModule module) {
    this.module = module;
  }

  @Override
  public Gson get() {
    return proxyProvideGson(module);
  }

  public static FragmentModule_ProvideGsonFactory create(FragmentModule module) {
    return new FragmentModule_ProvideGsonFactory(module);
  }

  public static Gson proxyProvideGson(FragmentModule instance) {
    return Preconditions.checkNotNull(
        instance.provideGson(), "Cannot return null from a non-@Nullable @Provides method");
  }
}
