// Generated by Dagger (https://google.github.io/dagger).
package ymsli.com.ea1h.di.module;

import android.location.LocationManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class FragmentModule_ProvideLocationManagerFactory
    implements Factory<LocationManager> {
  private final FragmentModule module;

  public FragmentModule_ProvideLocationManagerFactory(FragmentModule module) {
    this.module = module;
  }

  @Override
  public LocationManager get() {
    return proxyProvideLocationManager(module);
  }

  public static FragmentModule_ProvideLocationManagerFactory create(FragmentModule module) {
    return new FragmentModule_ProvideLocationManagerFactory(module);
  }

  public static LocationManager proxyProvideLocationManager(FragmentModule instance) {
    return Preconditions.checkNotNull(
        instance.provideLocationManager(),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
