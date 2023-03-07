// Generated by Dagger (https://google.github.io/dagger).
package ymsli.com.ea1h.di.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import ymsli.com.ea1h.views.userengagement.ProgressFragment;

public final class ActivityModule_ProvideProgressFragmentFactory
    implements Factory<ProgressFragment> {
  private final ActivityModule module;

  public ActivityModule_ProvideProgressFragmentFactory(ActivityModule module) {
    this.module = module;
  }

  @Override
  public ProgressFragment get() {
    return proxyProvideProgressFragment(module);
  }

  public static ActivityModule_ProvideProgressFragmentFactory create(ActivityModule module) {
    return new ActivityModule_ProvideProgressFragmentFactory(module);
  }

  public static ProgressFragment proxyProvideProgressFragment(ActivityModule instance) {
    return Preconditions.checkNotNull(
        instance.provideProgressFragment(),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
