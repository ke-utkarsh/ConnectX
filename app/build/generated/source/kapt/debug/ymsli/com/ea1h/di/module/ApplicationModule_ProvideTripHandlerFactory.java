// Generated by Dagger (https://google.github.io/dagger).
package ymsli.com.ea1h.di.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import ymsli.com.ea1h.network.swaggerintegration.TripHandler;

public final class ApplicationModule_ProvideTripHandlerFactory implements Factory<TripHandler> {
  private final ApplicationModule module;

  public ApplicationModule_ProvideTripHandlerFactory(ApplicationModule module) {
    this.module = module;
  }

  @Override
  public TripHandler get() {
    return proxyProvideTripHandler(module);
  }

  public static ApplicationModule_ProvideTripHandlerFactory create(ApplicationModule module) {
    return new ApplicationModule_ProvideTripHandlerFactory(module);
  }

  public static TripHandler proxyProvideTripHandler(ApplicationModule instance) {
    return Preconditions.checkNotNull(
        instance.provideTripHandler(), "Cannot return null from a non-@Nullable @Provides method");
  }
}
