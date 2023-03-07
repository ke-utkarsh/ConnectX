// Generated by Dagger (https://google.github.io/dagger).
package ymsli.com.ea1h.views.userengagement;

import dagger.MembersInjector;
import javax.inject.Provider;
import ymsli.com.ea1h.base.BaseDialogFragment_MembersInjector;
import ymsli.com.ea1h.views.entrance.EntranceViewModel;

public final class SuccessAckFragment_MembersInjector
    implements MembersInjector<SuccessAckFragment> {
  private final Provider<EntranceViewModel> viewModelProvider;

  public SuccessAckFragment_MembersInjector(Provider<EntranceViewModel> viewModelProvider) {
    this.viewModelProvider = viewModelProvider;
  }

  public static MembersInjector<SuccessAckFragment> create(
      Provider<EntranceViewModel> viewModelProvider) {
    return new SuccessAckFragment_MembersInjector(viewModelProvider);
  }

  @Override
  public void injectMembers(SuccessAckFragment instance) {
    BaseDialogFragment_MembersInjector.injectViewModel(instance, viewModelProvider.get());
  }
}
