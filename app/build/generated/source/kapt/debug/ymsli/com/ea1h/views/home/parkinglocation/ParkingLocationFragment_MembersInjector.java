// Generated by Dagger (https://google.github.io/dagger).
package ymsli.com.ea1h.views.home.parkinglocation;

import dagger.MembersInjector;
import javax.inject.Provider;
import ymsli.com.ea1h.base.BaseFragment_MembersInjector;
import ymsli.com.ea1h.views.home.HomeViewModel;
import ymsli.com.ea1h.views.userengagement.ProgressFragment;

public final class ParkingLocationFragment_MembersInjector
    implements MembersInjector<ParkingLocationFragment> {
  private final Provider<HomeViewModel> viewModelProvider;

  private final Provider<ProgressFragment> progressFragmentProvider;

  public ParkingLocationFragment_MembersInjector(
      Provider<HomeViewModel> viewModelProvider,
      Provider<ProgressFragment> progressFragmentProvider) {
    this.viewModelProvider = viewModelProvider;
    this.progressFragmentProvider = progressFragmentProvider;
  }

  public static MembersInjector<ParkingLocationFragment> create(
      Provider<HomeViewModel> viewModelProvider,
      Provider<ProgressFragment> progressFragmentProvider) {
    return new ParkingLocationFragment_MembersInjector(viewModelProvider, progressFragmentProvider);
  }

  @Override
  public void injectMembers(ParkingLocationFragment instance) {
    BaseFragment_MembersInjector.injectViewModel(instance, viewModelProvider.get());
    BaseFragment_MembersInjector.injectProgressFragment(instance, progressFragmentProvider.get());
  }
}
