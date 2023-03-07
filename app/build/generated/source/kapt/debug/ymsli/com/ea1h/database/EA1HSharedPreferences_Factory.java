// Generated by Dagger (https://google.github.io/dagger).
package ymsli.com.ea1h.database;

import android.content.SharedPreferences;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class EA1HSharedPreferences_Factory implements Factory<EA1HSharedPreferences> {
  private final Provider<SharedPreferences> prefsProvider;

  public EA1HSharedPreferences_Factory(Provider<SharedPreferences> prefsProvider) {
    this.prefsProvider = prefsProvider;
  }

  @Override
  public EA1HSharedPreferences get() {
    return new EA1HSharedPreferences(prefsProvider.get());
  }

  public static EA1HSharedPreferences_Factory create(Provider<SharedPreferences> prefsProvider) {
    return new EA1HSharedPreferences_Factory(prefsProvider);
  }

  public static EA1HSharedPreferences newEA1HSharedPreferences(SharedPreferences prefs) {
    return new EA1HSharedPreferences(prefs);
  }
}
