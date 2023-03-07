// Generated by Dagger (https://google.github.io/dagger).
package ymsli.com.ea1h.views.yourvehicles;

import dagger.MembersInjector;
import javax.inject.Provider;
import ymsli.com.ea1h.base.BaseFragment_MembersInjector;
import ymsli.com.ea1h.views.home.HomeViewModel;
import ymsli.com.ea1h.views.userengagement.ProgressFragment;

public final class YourBikesFragment_MembersInjector implements MembersInjector<YourBikesFragment> {
  private final Provider<HomeViewModel> viewModelProvider;

  private final Provider<ProgressFragment> progressFragmentProvider;

  public YourBikesFragment_MembersInjector(
      Provider<HomeViewModel> viewModelProvider,
      Provider<ProgressFragment> progressFragmentProvider) {
    this.viewModelProvider = viewModelProvider;
    this.progressFragmentProvider = progressFragmentProvider;
  }

  public static MembersInjector<YourBikesFragment> create(
      Provider<HomeViewModel> viewModelProvider,
      Provider<ProgressFragment> progressFragmentProvider) {
    return new YourBikesFragment_MembersInjector(viewModelProvider, progressFragmentProvider);
  }

  @Override
  public void injectMembers(YourBikesFragment instance) {
    BaseFragment_MembersInjector.injectViewModel(instance, viewModelProvider.get());
    BaseFragment_MembersInjector.injectProgressFragment(instance, progressFragmentProvider.get());
  }
}
