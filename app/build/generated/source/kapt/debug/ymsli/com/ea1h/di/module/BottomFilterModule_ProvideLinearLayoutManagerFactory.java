// Generated by Dagger (https://google.github.io/dagger).
package ymsli.com.ea1h.di.module;

import androidx.recyclerview.widget.LinearLayoutManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class BottomFilterModule_ProvideLinearLayoutManagerFactory
    implements Factory<LinearLayoutManager> {
  private final BottomFilterModule module;

  public BottomFilterModule_ProvideLinearLayoutManagerFactory(BottomFilterModule module) {
    this.module = module;
  }

  @Override
  public LinearLayoutManager get() {
    return proxyProvideLinearLayoutManager(module);
  }

  public static BottomFilterModule_ProvideLinearLayoutManagerFactory create(
      BottomFilterModule module) {
    return new BottomFilterModule_ProvideLinearLayoutManagerFactory(module);
  }

  public static LinearLayoutManager proxyProvideLinearLayoutManager(BottomFilterModule instance) {
    return Preconditions.checkNotNull(
        instance.provideLinearLayoutManager(),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
