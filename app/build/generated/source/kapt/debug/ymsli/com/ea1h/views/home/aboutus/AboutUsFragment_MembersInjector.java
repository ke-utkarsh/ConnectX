// Generated by Dagger (https://google.github.io/dagger).
package ymsli.com.ea1h.views.home.aboutus;

import dagger.MembersInjector;
import javax.inject.Provider;
import ymsli.com.ea1h.base.BaseFragment_MembersInjector;
import ymsli.com.ea1h.views.userengagement.ProgressFragment;

public final class AboutUsFragment_MembersInjector implements MembersInjector<AboutUsFragment> {
  private final Provider<AboutUsViewModel> viewModelProvider;

  private final Provider<ProgressFragment> progressFragmentProvider;

  public AboutUsFragment_MembersInjector(
      Provider<AboutUsViewModel> viewModelProvider,
      Provider<ProgressFragment> progressFragmentProvider) {
    this.viewModelProvider = viewModelProvider;
    this.progressFragmentProvider = progressFragmentProvider;
  }

  public static MembersInjector<AboutUsFragment> create(
      Provider<AboutUsViewModel> viewModelProvider,
      Provider<ProgressFragment> progressFragmentProvider) {
    return new AboutUsFragment_MembersInjector(viewModelProvider, progressFragmentProvider);
  }

  @Override
  public void injectMembers(AboutUsFragment instance) {
    BaseFragment_MembersInjector.injectViewModel(instance, viewModelProvider.get());
    BaseFragment_MembersInjector.injectProgressFragment(instance, progressFragmentProvider.get());
  }
}
