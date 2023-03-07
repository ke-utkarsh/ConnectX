// Generated by Dagger (https://google.github.io/dagger).
package ymsli.com.ea1h.base;

import dagger.MembersInjector;
import javax.inject.Provider;

public final class BaseDialogFragment_MembersInjector<VM extends BaseViewModel>
    implements MembersInjector<BaseDialogFragment<VM>> {
  private final Provider<VM> viewModelProvider;

  public BaseDialogFragment_MembersInjector(Provider<VM> viewModelProvider) {
    this.viewModelProvider = viewModelProvider;
  }

  public static <VM extends BaseViewModel> MembersInjector<BaseDialogFragment<VM>> create(
      Provider<VM> viewModelProvider) {
    return new BaseDialogFragment_MembersInjector<VM>(viewModelProvider);
  }

  @Override
  public void injectMembers(BaseDialogFragment<VM> instance) {
    injectViewModel(instance, viewModelProvider.get());
  }

  public static <VM extends BaseViewModel> void injectViewModel(
      BaseDialogFragment<VM> instance, VM viewModel) {
    instance.viewModel = viewModel;
  }
}