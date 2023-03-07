// Generated by Dagger (https://google.github.io/dagger).
package ymsli.com.ea1h.views.password.forgot;

import android.location.LocationManager;
import com.gigya.android.sdk.Gigya;
import dagger.MembersInjector;
import javax.inject.Provider;
import ymsli.com.ea1h.GigyaResponse;
import ymsli.com.ea1h.base.BaseActivity_MembersInjector;
import ymsli.com.ea1h.views.userengagement.ErrorAckFragment;
import ymsli.com.ea1h.views.userengagement.MFECUProgressFragment;
import ymsli.com.ea1h.views.userengagement.ProgressFragment;

public final class ForgotPasswordActivity_MembersInjector
    implements MembersInjector<ForgotPasswordActivity> {
  private final Provider<ForgotPasswordViewModel> viewModelProvider;

  private final Provider<Gigya<GigyaResponse>> gigyaProvider;

  private final Provider<LocationManager> locationManagerProvider;

  private final Provider<ErrorAckFragment> userAckFragmentProvider;

  private final Provider<ProgressFragment> progressFragmentProvider;

  private final Provider<MFECUProgressFragment> mfecuProgressFragmentProvider;

  public ForgotPasswordActivity_MembersInjector(
      Provider<ForgotPasswordViewModel> viewModelProvider,
      Provider<Gigya<GigyaResponse>> gigyaProvider,
      Provider<LocationManager> locationManagerProvider,
      Provider<ErrorAckFragment> userAckFragmentProvider,
      Provider<ProgressFragment> progressFragmentProvider,
      Provider<MFECUProgressFragment> mfecuProgressFragmentProvider) {
    this.viewModelProvider = viewModelProvider;
    this.gigyaProvider = gigyaProvider;
    this.locationManagerProvider = locationManagerProvider;
    this.userAckFragmentProvider = userAckFragmentProvider;
    this.progressFragmentProvider = progressFragmentProvider;
    this.mfecuProgressFragmentProvider = mfecuProgressFragmentProvider;
  }

  public static MembersInjector<ForgotPasswordActivity> create(
      Provider<ForgotPasswordViewModel> viewModelProvider,
      Provider<Gigya<GigyaResponse>> gigyaProvider,
      Provider<LocationManager> locationManagerProvider,
      Provider<ErrorAckFragment> userAckFragmentProvider,
      Provider<ProgressFragment> progressFragmentProvider,
      Provider<MFECUProgressFragment> mfecuProgressFragmentProvider) {
    return new ForgotPasswordActivity_MembersInjector(
        viewModelProvider,
        gigyaProvider,
        locationManagerProvider,
        userAckFragmentProvider,
        progressFragmentProvider,
        mfecuProgressFragmentProvider);
  }

  @Override
  public void injectMembers(ForgotPasswordActivity instance) {
    BaseActivity_MembersInjector.injectViewModel(instance, viewModelProvider.get());
    BaseActivity_MembersInjector.injectGigya(instance, gigyaProvider.get());
    BaseActivity_MembersInjector.injectLocationManager(instance, locationManagerProvider.get());
    BaseActivity_MembersInjector.injectUserAckFragment(instance, userAckFragmentProvider.get());
    BaseActivity_MembersInjector.injectProgressFragment(instance, progressFragmentProvider.get());
    BaseActivity_MembersInjector.injectMfecuProgressFragment(
        instance, mfecuProgressFragmentProvider.get());
  }
}