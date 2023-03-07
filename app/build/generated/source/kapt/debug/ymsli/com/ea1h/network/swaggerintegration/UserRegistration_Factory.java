// Generated by Dagger (https://google.github.io/dagger).
package ymsli.com.ea1h.network.swaggerintegration;

import com.google.gson.Gson;
import dagger.internal.Factory;
import io.swagger.client.apis.LoginControllerApi;
import io.swagger.client.apis.OtpControllerApi;
import io.swagger.client.apis.UserControllerApi;
import javax.inject.Provider;

public final class UserRegistration_Factory implements Factory<UserRegistration> {
  private final Provider<OtpControllerApi> otpControllerApiProvider;

  private final Provider<UserControllerApi> userControllerApiProvider;

  private final Provider<LoginControllerApi> loginControllerApiProvider;

  private final Provider<Gson> gsonProvider;

  public UserRegistration_Factory(
      Provider<OtpControllerApi> otpControllerApiProvider,
      Provider<UserControllerApi> userControllerApiProvider,
      Provider<LoginControllerApi> loginControllerApiProvider,
      Provider<Gson> gsonProvider) {
    this.otpControllerApiProvider = otpControllerApiProvider;
    this.userControllerApiProvider = userControllerApiProvider;
    this.loginControllerApiProvider = loginControllerApiProvider;
    this.gsonProvider = gsonProvider;
  }

  @Override
  public UserRegistration get() {
    return new UserRegistration(
        otpControllerApiProvider.get(),
        userControllerApiProvider.get(),
        loginControllerApiProvider.get(),
        gsonProvider.get());
  }

  public static UserRegistration_Factory create(
      Provider<OtpControllerApi> otpControllerApiProvider,
      Provider<UserControllerApi> userControllerApiProvider,
      Provider<LoginControllerApi> loginControllerApiProvider,
      Provider<Gson> gsonProvider) {
    return new UserRegistration_Factory(
        otpControllerApiProvider,
        userControllerApiProvider,
        loginControllerApiProvider,
        gsonProvider);
  }

  public static UserRegistration newUserRegistration(
      OtpControllerApi otpControllerApi,
      UserControllerApi userControllerApi,
      LoginControllerApi loginControllerApi,
      Gson gson) {
    return new UserRegistration(otpControllerApi, userControllerApi, loginControllerApi, gson);
  }
}