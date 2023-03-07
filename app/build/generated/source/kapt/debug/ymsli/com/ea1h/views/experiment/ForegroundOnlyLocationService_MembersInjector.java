// Generated by Dagger (https://google.github.io/dagger).
package ymsli.com.ea1h.views.experiment;

import android.app.NotificationManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import dagger.MembersInjector;
import javax.inject.Provider;
import ymsli.com.ea1h.EA1HRepository;
import ymsli.com.ea1h.base.BaseService_MembersInjector;

public final class ForegroundOnlyLocationService_MembersInjector
    implements MembersInjector<ForegroundOnlyLocationService> {
  private final Provider<EA1HRepository> eA1HRepositoryProvider;

  private final Provider<NotificationManager> notificationManagerProvider;

  private final Provider<LocationRequest> locationRequestProvider;

  private final Provider<FusedLocationProviderClient> fusedLocationProviderClientProvider;

  public ForegroundOnlyLocationService_MembersInjector(
      Provider<EA1HRepository> eA1HRepositoryProvider,
      Provider<NotificationManager> notificationManagerProvider,
      Provider<LocationRequest> locationRequestProvider,
      Provider<FusedLocationProviderClient> fusedLocationProviderClientProvider) {
    this.eA1HRepositoryProvider = eA1HRepositoryProvider;
    this.notificationManagerProvider = notificationManagerProvider;
    this.locationRequestProvider = locationRequestProvider;
    this.fusedLocationProviderClientProvider = fusedLocationProviderClientProvider;
  }

  public static MembersInjector<ForegroundOnlyLocationService> create(
      Provider<EA1HRepository> eA1HRepositoryProvider,
      Provider<NotificationManager> notificationManagerProvider,
      Provider<LocationRequest> locationRequestProvider,
      Provider<FusedLocationProviderClient> fusedLocationProviderClientProvider) {
    return new ForegroundOnlyLocationService_MembersInjector(
        eA1HRepositoryProvider,
        notificationManagerProvider,
        locationRequestProvider,
        fusedLocationProviderClientProvider);
  }

  @Override
  public void injectMembers(ForegroundOnlyLocationService instance) {
    BaseService_MembersInjector.injectEA1HRepository(instance, eA1HRepositoryProvider.get());
    injectNotificationManager(instance, notificationManagerProvider.get());
    injectLocationRequest(instance, locationRequestProvider.get());
    injectFusedLocationProviderClient(instance, fusedLocationProviderClientProvider.get());
  }

  public static void injectNotificationManager(
      ForegroundOnlyLocationService instance, NotificationManager notificationManager) {
    instance.notificationManager = notificationManager;
  }

  public static void injectLocationRequest(
      ForegroundOnlyLocationService instance, LocationRequest locationRequest) {
    instance.locationRequest = locationRequest;
  }

  public static void injectFusedLocationProviderClient(
      ForegroundOnlyLocationService instance,
      FusedLocationProviderClient fusedLocationProviderClient) {
    instance.fusedLocationProviderClient = fusedLocationProviderClient;
  }
}