// Generated by Dagger (https://google.github.io/dagger).
package ymsli.com.ea1h.di.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import ymsli.com.ea1h.views.userengagement.MFECUProgressFragment;

public final class ActivityModule_ProvideMFECUProgressFragmentFactory
    implements Factory<MFECUProgressFragment> {
  private final ActivityModule module;

  public ActivityModule_ProvideMFECUProgressFragmentFactory(ActivityModule module) {
    this.module = module;
  }

  @Override
  public MFECUProgressFragment get() {
    return proxyProvideMFECUProgressFragment(module);
  }

  public static ActivityModule_ProvideMFECUProgressFragmentFactory create(ActivityModule module) {
    return new ActivityModule_ProvideMFECUProgressFragmentFactory(module);
  }

  public static MFECUProgressFragment proxyProvideMFECUProgressFragment(ActivityModule instance) {
    return Preconditions.checkNotNull(
        instance.provideMFECUProgressFragment(),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
