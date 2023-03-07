// Generated by Dagger (https://google.github.io/dagger).
package ymsli.com.ea1h.di.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import io.swagger.client.apis.UserControllerApi;

public final class ApplicationModule_ProvideUserControllerApiFactory
    implements Factory<UserControllerApi> {
  private final ApplicationModule module;

  public ApplicationModule_ProvideUserControllerApiFactory(ApplicationModule module) {
    this.module = module;
  }

  @Override
  public UserControllerApi get() {
    return proxyProvideUserControllerApi(module);
  }

  public static ApplicationModule_ProvideUserControllerApiFactory create(ApplicationModule module) {
    return new ApplicationModule_ProvideUserControllerApiFactory(module);
  }

  public static UserControllerApi proxyProvideUserControllerApi(ApplicationModule instance) {
    return Preconditions.checkNotNull(
        instance.provideUserControllerApi(),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
