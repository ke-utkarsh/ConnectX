// Generated by Dagger (https://google.github.io/dagger).
package ymsli.com.ea1h.di.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import ymsli.com.ea1h.network.swaggerintegration.MiscHandler;

public final class ApplicationModule_ProvideMiscHandlerFactory implements Factory<MiscHandler> {
  private final ApplicationModule module;

  public ApplicationModule_ProvideMiscHandlerFactory(ApplicationModule module) {
    this.module = module;
  }

  @Override
  public MiscHandler get() {
    return proxyProvideMiscHandler(module);
  }

  public static ApplicationModule_ProvideMiscHandlerFactory create(ApplicationModule module) {
    return new ApplicationModule_ProvideMiscHandlerFactory(module);
  }

  public static MiscHandler proxyProvideMiscHandler(ApplicationModule instance) {
    return Preconditions.checkNotNull(
        instance.provideMiscHandler(), "Cannot return null from a non-@Nullable @Provides method");
  }
}
