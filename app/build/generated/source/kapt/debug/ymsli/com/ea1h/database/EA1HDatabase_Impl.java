package ymsli.com.ea1h.database;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import ymsli.com.ea1h.database.dao.AccelerometerDao;
import ymsli.com.ea1h.database.dao.AccelerometerDao_Impl;
import ymsli.com.ea1h.database.dao.AdvPacketDao;
import ymsli.com.ea1h.database.dao.AdvPacketDao_Impl;
import ymsli.com.ea1h.database.dao.BTCommandsDao;
import ymsli.com.ea1h.database.dao.BTCommandsDao_Impl;
import ymsli.com.ea1h.database.dao.BTCommandsLogDao;
import ymsli.com.ea1h.database.dao.BTCommandsLogDao_Impl;
import ymsli.com.ea1h.database.dao.BikeDao;
import ymsli.com.ea1h.database.dao.BikeDao_Impl;
import ymsli.com.ea1h.database.dao.DAPIoTFileDao;
import ymsli.com.ea1h.database.dao.DAPIoTFileDao_Impl;
import ymsli.com.ea1h.database.dao.FilterBikeDao;
import ymsli.com.ea1h.database.dao.FilterBikeDao_Impl;
import ymsli.com.ea1h.database.dao.GyroDao;
import ymsli.com.ea1h.database.dao.GyroDao_Impl;
import ymsli.com.ea1h.database.dao.LatLongDao;
import ymsli.com.ea1h.database.dao.LatLongDao_Impl;
import ymsli.com.ea1h.database.dao.MiscDataDao;
import ymsli.com.ea1h.database.dao.MiscDataDao_Impl;
import ymsli.com.ea1h.database.dao.TripDao;
import ymsli.com.ea1h.database.dao.TripDao_Impl;
import ymsli.com.ea1h.database.dao.TripDetailDao;
import ymsli.com.ea1h.database.dao.TripDetailDao_Impl;
import ymsli.com.ea1h.database.dao.UserDao;
import ymsli.com.ea1h.database.dao.UserDao_Impl;

@SuppressWarnings({"unchecked", "deprecation"})
public final class EA1HDatabase_Impl extends EA1HDatabase {
  private volatile TripDao _tripDao;

  private volatile UserDao _userDao;

  private volatile BikeDao _bikeDao;

  private volatile BTCommandsDao _bTCommandsDao;

  private volatile MiscDataDao _miscDataDao;

  private volatile FilterBikeDao _filterBikeDao;

  private volatile BTCommandsLogDao _bTCommandsLogDao;

  private volatile DAPIoTFileDao _dAPIoTFileDao;

  private volatile TripDetailDao _tripDetailDao;

  private volatile LatLongDao _latLongDao;

  private volatile GyroDao _gyroDao;

  private volatile AccelerometerDao _accelerometerDao;

  private volatile AdvPacketDao _advPacketDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `trip_entity` (`tripId` TEXT NOT NULL, `startAddress` TEXT, `endAddress` TEXT, `chassisNumber` TEXT, `createdOn` INTEGER, `startTime` INTEGER, `endTime` INTEGER, `distanceCovered` REAL, `averageSpeed` REAL, `breakCount` INTEGER, `imei` TEXT, `lastBatteryVoltage` REAL, `startLat` REAL, `startLon` REAL, `endLat` REAL, `endLon` REAL, `bikeId` INTEGER, `isActive` INTEGER NOT NULL, `isSynced` INTEGER NOT NULL, `updatedOn` INTEGER, `userId` TEXT NOT NULL, `potentialLastLatitude` REAL, `potentialLastLongitude` REAL, `potentialEndTime` INTEGER, PRIMARY KEY(`tripId`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `user_entity` (`token` TEXT NOT NULL, `userId` TEXT NOT NULL, `name` TEXT, `reLoginFailure` INTEGER NOT NULL, PRIMARY KEY(`userId`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `bike_entity` (`chassisNumber` TEXT NOT NULL, `registrationNumber` TEXT, `model` TEXT, `make` TEXT, `btAdd` TEXT, `bike_pic` TEXT, `is_last_connected` INTEGER NOT NULL, `bikeId` INTEGER NOT NULL, `bkModNm` TEXT, `ccuId` TEXT, `vehType` TEXT, PRIMARY KEY(`chassisNumber`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `misc_data` (`discriptionId` INTEGER NOT NULL, `descriptionKey` TEXT NOT NULL, `descriptionValue` TEXT NOT NULL, `updatedOn` TEXT NOT NULL, PRIMARY KEY(`discriptionId`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `bt_commands` (`commandId` INTEGER NOT NULL, `key` TEXT NOT NULL, `commandCode` TEXT NOT NULL, `description` TEXT NOT NULL, PRIMARY KEY(`commandId`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `filter_bike` (`filterID` INTEGER PRIMARY KEY AUTOINCREMENT, `bike_selected` TEXT NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `bt_command_logs` (`triggeredOn` INTEGER NOT NULL, `cmdType` INTEGER NOT NULL, `chassNum` TEXT, `deviceId` TEXT, `isSuccessful` INTEGER NOT NULL, `isCommit` INTEGER NOT NULL, `btAdd` TEXT, PRIMARY KEY(`triggeredOn`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `slider_image` (`imageUrl` TEXT NOT NULL, `dataType` TEXT, PRIMARY KEY(`imageUrl`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `zip_folder_entity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `fileName` TEXT NOT NULL, `filePath` TEXT NOT NULL, `createdOn` INTEGER NOT NULL, `nextTry` INTEGER, `retryAttempts` INTEGER NOT NULL, `isSent` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `trip_detail` (`tripId` TEXT NOT NULL, `lats` TEXT, `longs` TEXT, PRIMARY KEY(`tripId`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `lat_long_entity` (`locationId` INTEGER PRIMARY KEY AUTOINCREMENT, `tripId` TEXT NOT NULL, `latitude` TEXT NOT NULL, `longitude` TEXT NOT NULL, `locationCaptureTime` TEXT NOT NULL, `isFileCreated` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `gyro_entity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `tripId` TEXT NOT NULL, `xCoordinate` TEXT NOT NULL, `yCoordinate` TEXT NOT NULL, `zCoordinate` TEXT NOT NULL, `sensorTime` TEXT NOT NULL, `isFileCreated` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `accel_entity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `tripId` TEXT NOT NULL, `xCoordinate` TEXT NOT NULL, `yCoordinate` TEXT NOT NULL, `zCoordinate` TEXT NOT NULL, `sensorTime` TEXT NOT NULL, `isFileCreated` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `adv_packet` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `btAdd` TEXT, `connectionString` TEXT, `currentTime` INTEGER)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '5ab363adf2b1f019bceadd4f0c6826d2')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `trip_entity`");
        _db.execSQL("DROP TABLE IF EXISTS `user_entity`");
        _db.execSQL("DROP TABLE IF EXISTS `bike_entity`");
        _db.execSQL("DROP TABLE IF EXISTS `misc_data`");
        _db.execSQL("DROP TABLE IF EXISTS `bt_commands`");
        _db.execSQL("DROP TABLE IF EXISTS `filter_bike`");
        _db.execSQL("DROP TABLE IF EXISTS `bt_command_logs`");
        _db.execSQL("DROP TABLE IF EXISTS `slider_image`");
        _db.execSQL("DROP TABLE IF EXISTS `zip_folder_entity`");
        _db.execSQL("DROP TABLE IF EXISTS `trip_detail`");
        _db.execSQL("DROP TABLE IF EXISTS `lat_long_entity`");
        _db.execSQL("DROP TABLE IF EXISTS `gyro_entity`");
        _db.execSQL("DROP TABLE IF EXISTS `accel_entity`");
        _db.execSQL("DROP TABLE IF EXISTS `adv_packet`");
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsTripEntity = new HashMap<String, TableInfo.Column>(24);
        _columnsTripEntity.put("tripId", new TableInfo.Column("tripId", "TEXT", true, 1));
        _columnsTripEntity.put("startAddress", new TableInfo.Column("startAddress", "TEXT", false, 0));
        _columnsTripEntity.put("endAddress", new TableInfo.Column("endAddress", "TEXT", false, 0));
        _columnsTripEntity.put("chassisNumber", new TableInfo.Column("chassisNumber", "TEXT", false, 0));
        _columnsTripEntity.put("createdOn", new TableInfo.Column("createdOn", "INTEGER", false, 0));
        _columnsTripEntity.put("startTime", new TableInfo.Column("startTime", "INTEGER", false, 0));
        _columnsTripEntity.put("endTime", new TableInfo.Column("endTime", "INTEGER", false, 0));
        _columnsTripEntity.put("distanceCovered", new TableInfo.Column("distanceCovered", "REAL", false, 0));
        _columnsTripEntity.put("averageSpeed", new TableInfo.Column("averageSpeed", "REAL", false, 0));
        _columnsTripEntity.put("breakCount", new TableInfo.Column("breakCount", "INTEGER", false, 0));
        _columnsTripEntity.put("imei", new TableInfo.Column("imei", "TEXT", false, 0));
        _columnsTripEntity.put("lastBatteryVoltage", new TableInfo.Column("lastBatteryVoltage", "REAL", false, 0));
        _columnsTripEntity.put("startLat", new TableInfo.Column("startLat", "REAL", false, 0));
        _columnsTripEntity.put("startLon", new TableInfo.Column("startLon", "REAL", false, 0));
        _columnsTripEntity.put("endLat", new TableInfo.Column("endLat", "REAL", false, 0));
        _columnsTripEntity.put("endLon", new TableInfo.Column("endLon", "REAL", false, 0));
        _columnsTripEntity.put("bikeId", new TableInfo.Column("bikeId", "INTEGER", false, 0));
        _columnsTripEntity.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0));
        _columnsTripEntity.put("isSynced", new TableInfo.Column("isSynced", "INTEGER", true, 0));
        _columnsTripEntity.put("updatedOn", new TableInfo.Column("updatedOn", "INTEGER", false, 0));
        _columnsTripEntity.put("userId", new TableInfo.Column("userId", "TEXT", true, 0));
        _columnsTripEntity.put("potentialLastLatitude", new TableInfo.Column("potentialLastLatitude", "REAL", false, 0));
        _columnsTripEntity.put("potentialLastLongitude", new TableInfo.Column("potentialLastLongitude", "REAL", false, 0));
        _columnsTripEntity.put("potentialEndTime", new TableInfo.Column("potentialEndTime", "INTEGER", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTripEntity = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTripEntity = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTripEntity = new TableInfo("trip_entity", _columnsTripEntity, _foreignKeysTripEntity, _indicesTripEntity);
        final TableInfo _existingTripEntity = TableInfo.read(_db, "trip_entity");
        if (! _infoTripEntity.equals(_existingTripEntity)) {
          throw new IllegalStateException("Migration didn't properly handle trip_entity(ymsli.com.ea1h.database.entity.TripEntity).\n"
                  + " Expected:\n" + _infoTripEntity + "\n"
                  + " Found:\n" + _existingTripEntity);
        }
        final HashMap<String, TableInfo.Column> _columnsUserEntity = new HashMap<String, TableInfo.Column>(4);
        _columnsUserEntity.put("token", new TableInfo.Column("token", "TEXT", true, 0));
        _columnsUserEntity.put("userId", new TableInfo.Column("userId", "TEXT", true, 1));
        _columnsUserEntity.put("name", new TableInfo.Column("name", "TEXT", false, 0));
        _columnsUserEntity.put("reLoginFailure", new TableInfo.Column("reLoginFailure", "INTEGER", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUserEntity = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUserEntity = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUserEntity = new TableInfo("user_entity", _columnsUserEntity, _foreignKeysUserEntity, _indicesUserEntity);
        final TableInfo _existingUserEntity = TableInfo.read(_db, "user_entity");
        if (! _infoUserEntity.equals(_existingUserEntity)) {
          throw new IllegalStateException("Migration didn't properly handle user_entity(ymsli.com.ea1h.database.entity.UserEntity).\n"
                  + " Expected:\n" + _infoUserEntity + "\n"
                  + " Found:\n" + _existingUserEntity);
        }
        final HashMap<String, TableInfo.Column> _columnsBikeEntity = new HashMap<String, TableInfo.Column>(11);
        _columnsBikeEntity.put("chassisNumber", new TableInfo.Column("chassisNumber", "TEXT", true, 1));
        _columnsBikeEntity.put("registrationNumber", new TableInfo.Column("registrationNumber", "TEXT", false, 0));
        _columnsBikeEntity.put("model", new TableInfo.Column("model", "TEXT", false, 0));
        _columnsBikeEntity.put("make", new TableInfo.Column("make", "TEXT", false, 0));
        _columnsBikeEntity.put("btAdd", new TableInfo.Column("btAdd", "TEXT", false, 0));
        _columnsBikeEntity.put("bike_pic", new TableInfo.Column("bike_pic", "TEXT", false, 0));
        _columnsBikeEntity.put("is_last_connected", new TableInfo.Column("is_last_connected", "INTEGER", true, 0));
        _columnsBikeEntity.put("bikeId", new TableInfo.Column("bikeId", "INTEGER", true, 0));
        _columnsBikeEntity.put("bkModNm", new TableInfo.Column("bkModNm", "TEXT", false, 0));
        _columnsBikeEntity.put("ccuId", new TableInfo.Column("ccuId", "TEXT", false, 0));
        _columnsBikeEntity.put("vehType", new TableInfo.Column("vehType", "TEXT", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBikeEntity = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBikeEntity = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBikeEntity = new TableInfo("bike_entity", _columnsBikeEntity, _foreignKeysBikeEntity, _indicesBikeEntity);
        final TableInfo _existingBikeEntity = TableInfo.read(_db, "bike_entity");
        if (! _infoBikeEntity.equals(_existingBikeEntity)) {
          throw new IllegalStateException("Migration didn't properly handle bike_entity(ymsli.com.ea1h.database.entity.BikeEntity).\n"
                  + " Expected:\n" + _infoBikeEntity + "\n"
                  + " Found:\n" + _existingBikeEntity);
        }
        final HashMap<String, TableInfo.Column> _columnsMiscData = new HashMap<String, TableInfo.Column>(4);
        _columnsMiscData.put("discriptionId", new TableInfo.Column("discriptionId", "INTEGER", true, 1));
        _columnsMiscData.put("descriptionKey", new TableInfo.Column("descriptionKey", "TEXT", true, 0));
        _columnsMiscData.put("descriptionValue", new TableInfo.Column("descriptionValue", "TEXT", true, 0));
        _columnsMiscData.put("updatedOn", new TableInfo.Column("updatedOn", "TEXT", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMiscData = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMiscData = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMiscData = new TableInfo("misc_data", _columnsMiscData, _foreignKeysMiscData, _indicesMiscData);
        final TableInfo _existingMiscData = TableInfo.read(_db, "misc_data");
        if (! _infoMiscData.equals(_existingMiscData)) {
          throw new IllegalStateException("Migration didn't properly handle misc_data(ymsli.com.ea1h.database.entity.MiscDataEntity).\n"
                  + " Expected:\n" + _infoMiscData + "\n"
                  + " Found:\n" + _existingMiscData);
        }
        final HashMap<String, TableInfo.Column> _columnsBtCommands = new HashMap<String, TableInfo.Column>(4);
        _columnsBtCommands.put("commandId", new TableInfo.Column("commandId", "INTEGER", true, 1));
        _columnsBtCommands.put("key", new TableInfo.Column("key", "TEXT", true, 0));
        _columnsBtCommands.put("commandCode", new TableInfo.Column("commandCode", "TEXT", true, 0));
        _columnsBtCommands.put("description", new TableInfo.Column("description", "TEXT", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBtCommands = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBtCommands = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBtCommands = new TableInfo("bt_commands", _columnsBtCommands, _foreignKeysBtCommands, _indicesBtCommands);
        final TableInfo _existingBtCommands = TableInfo.read(_db, "bt_commands");
        if (! _infoBtCommands.equals(_existingBtCommands)) {
          throw new IllegalStateException("Migration didn't properly handle bt_commands(ymsli.com.ea1h.database.entity.BTCommandsEntity).\n"
                  + " Expected:\n" + _infoBtCommands + "\n"
                  + " Found:\n" + _existingBtCommands);
        }
        final HashMap<String, TableInfo.Column> _columnsFilterBike = new HashMap<String, TableInfo.Column>(2);
        _columnsFilterBike.put("filterID", new TableInfo.Column("filterID", "INTEGER", false, 1));
        _columnsFilterBike.put("bike_selected", new TableInfo.Column("bike_selected", "TEXT", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFilterBike = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFilterBike = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFilterBike = new TableInfo("filter_bike", _columnsFilterBike, _foreignKeysFilterBike, _indicesFilterBike);
        final TableInfo _existingFilterBike = TableInfo.read(_db, "filter_bike");
        if (! _infoFilterBike.equals(_existingFilterBike)) {
          throw new IllegalStateException("Migration didn't properly handle filter_bike(ymsli.com.ea1h.database.entity.FilterBikeEntity).\n"
                  + " Expected:\n" + _infoFilterBike + "\n"
                  + " Found:\n" + _existingFilterBike);
        }
        final HashMap<String, TableInfo.Column> _columnsBtCommandLogs = new HashMap<String, TableInfo.Column>(7);
        _columnsBtCommandLogs.put("triggeredOn", new TableInfo.Column("triggeredOn", "INTEGER", true, 1));
        _columnsBtCommandLogs.put("cmdType", new TableInfo.Column("cmdType", "INTEGER", true, 0));
        _columnsBtCommandLogs.put("chassNum", new TableInfo.Column("chassNum", "TEXT", false, 0));
        _columnsBtCommandLogs.put("deviceId", new TableInfo.Column("deviceId", "TEXT", false, 0));
        _columnsBtCommandLogs.put("isSuccessful", new TableInfo.Column("isSuccessful", "INTEGER", true, 0));
        _columnsBtCommandLogs.put("isCommit", new TableInfo.Column("isCommit", "INTEGER", true, 0));
        _columnsBtCommandLogs.put("btAdd", new TableInfo.Column("btAdd", "TEXT", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBtCommandLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBtCommandLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBtCommandLogs = new TableInfo("bt_command_logs", _columnsBtCommandLogs, _foreignKeysBtCommandLogs, _indicesBtCommandLogs);
        final TableInfo _existingBtCommandLogs = TableInfo.read(_db, "bt_command_logs");
        if (! _infoBtCommandLogs.equals(_existingBtCommandLogs)) {
          throw new IllegalStateException("Migration didn't properly handle bt_command_logs(ymsli.com.ea1h.database.entity.BTCommandsLogEntity).\n"
                  + " Expected:\n" + _infoBtCommandLogs + "\n"
                  + " Found:\n" + _existingBtCommandLogs);
        }
        final HashMap<String, TableInfo.Column> _columnsSliderImage = new HashMap<String, TableInfo.Column>(2);
        _columnsSliderImage.put("imageUrl", new TableInfo.Column("imageUrl", "TEXT", true, 1));
        _columnsSliderImage.put("dataType", new TableInfo.Column("dataType", "TEXT", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSliderImage = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSliderImage = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSliderImage = new TableInfo("slider_image", _columnsSliderImage, _foreignKeysSliderImage, _indicesSliderImage);
        final TableInfo _existingSliderImage = TableInfo.read(_db, "slider_image");
        if (! _infoSliderImage.equals(_existingSliderImage)) {
          throw new IllegalStateException("Migration didn't properly handle slider_image(ymsli.com.ea1h.database.entity.SliderImage).\n"
                  + " Expected:\n" + _infoSliderImage + "\n"
                  + " Found:\n" + _existingSliderImage);
        }
        final HashMap<String, TableInfo.Column> _columnsZipFolderEntity = new HashMap<String, TableInfo.Column>(7);
        _columnsZipFolderEntity.put("id", new TableInfo.Column("id", "INTEGER", false, 1));
        _columnsZipFolderEntity.put("fileName", new TableInfo.Column("fileName", "TEXT", true, 0));
        _columnsZipFolderEntity.put("filePath", new TableInfo.Column("filePath", "TEXT", true, 0));
        _columnsZipFolderEntity.put("createdOn", new TableInfo.Column("createdOn", "INTEGER", true, 0));
        _columnsZipFolderEntity.put("nextTry", new TableInfo.Column("nextTry", "INTEGER", false, 0));
        _columnsZipFolderEntity.put("retryAttempts", new TableInfo.Column("retryAttempts", "INTEGER", true, 0));
        _columnsZipFolderEntity.put("isSent", new TableInfo.Column("isSent", "INTEGER", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysZipFolderEntity = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesZipFolderEntity = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoZipFolderEntity = new TableInfo("zip_folder_entity", _columnsZipFolderEntity, _foreignKeysZipFolderEntity, _indicesZipFolderEntity);
        final TableInfo _existingZipFolderEntity = TableInfo.read(_db, "zip_folder_entity");
        if (! _infoZipFolderEntity.equals(_existingZipFolderEntity)) {
          throw new IllegalStateException("Migration didn't properly handle zip_folder_entity(ymsli.com.ea1h.database.entity.DAPIoTFileEntity).\n"
                  + " Expected:\n" + _infoZipFolderEntity + "\n"
                  + " Found:\n" + _existingZipFolderEntity);
        }
        final HashMap<String, TableInfo.Column> _columnsTripDetail = new HashMap<String, TableInfo.Column>(3);
        _columnsTripDetail.put("tripId", new TableInfo.Column("tripId", "TEXT", true, 1));
        _columnsTripDetail.put("lats", new TableInfo.Column("lats", "TEXT", false, 0));
        _columnsTripDetail.put("longs", new TableInfo.Column("longs", "TEXT", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTripDetail = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTripDetail = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTripDetail = new TableInfo("trip_detail", _columnsTripDetail, _foreignKeysTripDetail, _indicesTripDetail);
        final TableInfo _existingTripDetail = TableInfo.read(_db, "trip_detail");
        if (! _infoTripDetail.equals(_existingTripDetail)) {
          throw new IllegalStateException("Migration didn't properly handle trip_detail(ymsli.com.ea1h.database.entity.TripDetailEntity).\n"
                  + " Expected:\n" + _infoTripDetail + "\n"
                  + " Found:\n" + _existingTripDetail);
        }
        final HashMap<String, TableInfo.Column> _columnsLatLongEntity = new HashMap<String, TableInfo.Column>(6);
        _columnsLatLongEntity.put("locationId", new TableInfo.Column("locationId", "INTEGER", false, 1));
        _columnsLatLongEntity.put("tripId", new TableInfo.Column("tripId", "TEXT", true, 0));
        _columnsLatLongEntity.put("latitude", new TableInfo.Column("latitude", "TEXT", true, 0));
        _columnsLatLongEntity.put("longitude", new TableInfo.Column("longitude", "TEXT", true, 0));
        _columnsLatLongEntity.put("locationCaptureTime", new TableInfo.Column("locationCaptureTime", "TEXT", true, 0));
        _columnsLatLongEntity.put("isFileCreated", new TableInfo.Column("isFileCreated", "INTEGER", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLatLongEntity = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesLatLongEntity = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoLatLongEntity = new TableInfo("lat_long_entity", _columnsLatLongEntity, _foreignKeysLatLongEntity, _indicesLatLongEntity);
        final TableInfo _existingLatLongEntity = TableInfo.read(_db, "lat_long_entity");
        if (! _infoLatLongEntity.equals(_existingLatLongEntity)) {
          throw new IllegalStateException("Migration didn't properly handle lat_long_entity(ymsli.com.ea1h.database.entity.LatLongEntity).\n"
                  + " Expected:\n" + _infoLatLongEntity + "\n"
                  + " Found:\n" + _existingLatLongEntity);
        }
        final HashMap<String, TableInfo.Column> _columnsGyroEntity = new HashMap<String, TableInfo.Column>(7);
        _columnsGyroEntity.put("id", new TableInfo.Column("id", "INTEGER", false, 1));
        _columnsGyroEntity.put("tripId", new TableInfo.Column("tripId", "TEXT", true, 0));
        _columnsGyroEntity.put("xCoordinate", new TableInfo.Column("xCoordinate", "TEXT", true, 0));
        _columnsGyroEntity.put("yCoordinate", new TableInfo.Column("yCoordinate", "TEXT", true, 0));
        _columnsGyroEntity.put("zCoordinate", new TableInfo.Column("zCoordinate", "TEXT", true, 0));
        _columnsGyroEntity.put("sensorTime", new TableInfo.Column("sensorTime", "TEXT", true, 0));
        _columnsGyroEntity.put("isFileCreated", new TableInfo.Column("isFileCreated", "INTEGER", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysGyroEntity = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesGyroEntity = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoGyroEntity = new TableInfo("gyro_entity", _columnsGyroEntity, _foreignKeysGyroEntity, _indicesGyroEntity);
        final TableInfo _existingGyroEntity = TableInfo.read(_db, "gyro_entity");
        if (! _infoGyroEntity.equals(_existingGyroEntity)) {
          throw new IllegalStateException("Migration didn't properly handle gyro_entity(ymsli.com.ea1h.database.entity.GyroEntity).\n"
                  + " Expected:\n" + _infoGyroEntity + "\n"
                  + " Found:\n" + _existingGyroEntity);
        }
        final HashMap<String, TableInfo.Column> _columnsAccelEntity = new HashMap<String, TableInfo.Column>(7);
        _columnsAccelEntity.put("id", new TableInfo.Column("id", "INTEGER", false, 1));
        _columnsAccelEntity.put("tripId", new TableInfo.Column("tripId", "TEXT", true, 0));
        _columnsAccelEntity.put("xCoordinate", new TableInfo.Column("xCoordinate", "TEXT", true, 0));
        _columnsAccelEntity.put("yCoordinate", new TableInfo.Column("yCoordinate", "TEXT", true, 0));
        _columnsAccelEntity.put("zCoordinate", new TableInfo.Column("zCoordinate", "TEXT", true, 0));
        _columnsAccelEntity.put("sensorTime", new TableInfo.Column("sensorTime", "TEXT", true, 0));
        _columnsAccelEntity.put("isFileCreated", new TableInfo.Column("isFileCreated", "INTEGER", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAccelEntity = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAccelEntity = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAccelEntity = new TableInfo("accel_entity", _columnsAccelEntity, _foreignKeysAccelEntity, _indicesAccelEntity);
        final TableInfo _existingAccelEntity = TableInfo.read(_db, "accel_entity");
        if (! _infoAccelEntity.equals(_existingAccelEntity)) {
          throw new IllegalStateException("Migration didn't properly handle accel_entity(ymsli.com.ea1h.database.entity.AccelerometerEntity).\n"
                  + " Expected:\n" + _infoAccelEntity + "\n"
                  + " Found:\n" + _existingAccelEntity);
        }
        final HashMap<String, TableInfo.Column> _columnsAdvPacket = new HashMap<String, TableInfo.Column>(4);
        _columnsAdvPacket.put("id", new TableInfo.Column("id", "INTEGER", true, 1));
        _columnsAdvPacket.put("btAdd", new TableInfo.Column("btAdd", "TEXT", false, 0));
        _columnsAdvPacket.put("connectionString", new TableInfo.Column("connectionString", "TEXT", false, 0));
        _columnsAdvPacket.put("currentTime", new TableInfo.Column("currentTime", "INTEGER", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAdvPacket = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAdvPacket = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAdvPacket = new TableInfo("adv_packet", _columnsAdvPacket, _foreignKeysAdvPacket, _indicesAdvPacket);
        final TableInfo _existingAdvPacket = TableInfo.read(_db, "adv_packet");
        if (! _infoAdvPacket.equals(_existingAdvPacket)) {
          throw new IllegalStateException("Migration didn't properly handle adv_packet(ymsli.com.ea1h.database.entity.AdvPacketEntity).\n"
                  + " Expected:\n" + _infoAdvPacket + "\n"
                  + " Found:\n" + _existingAdvPacket);
        }
      }
    }, "5ab363adf2b1f019bceadd4f0c6826d2", "8873f68a1c98ddc81efcf487be9037ca");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "trip_entity","user_entity","bike_entity","misc_data","bt_commands","filter_bike","bt_command_logs","slider_image","zip_folder_entity","trip_detail","lat_long_entity","gyro_entity","accel_entity","adv_packet");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `trip_entity`");
      _db.execSQL("DELETE FROM `user_entity`");
      _db.execSQL("DELETE FROM `bike_entity`");
      _db.execSQL("DELETE FROM `misc_data`");
      _db.execSQL("DELETE FROM `bt_commands`");
      _db.execSQL("DELETE FROM `filter_bike`");
      _db.execSQL("DELETE FROM `bt_command_logs`");
      _db.execSQL("DELETE FROM `slider_image`");
      _db.execSQL("DELETE FROM `zip_folder_entity`");
      _db.execSQL("DELETE FROM `trip_detail`");
      _db.execSQL("DELETE FROM `lat_long_entity`");
      _db.execSQL("DELETE FROM `gyro_entity`");
      _db.execSQL("DELETE FROM `accel_entity`");
      _db.execSQL("DELETE FROM `adv_packet`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public TripDao tripDao() {
    if (_tripDao != null) {
      return _tripDao;
    } else {
      synchronized(this) {
        if(_tripDao == null) {
          _tripDao = new TripDao_Impl(this);
        }
        return _tripDao;
      }
    }
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public BikeDao bikeDao() {
    if (_bikeDao != null) {
      return _bikeDao;
    } else {
      synchronized(this) {
        if(_bikeDao == null) {
          _bikeDao = new BikeDao_Impl(this);
        }
        return _bikeDao;
      }
    }
  }

  @Override
  public BTCommandsDao btCommandsDao() {
    if (_bTCommandsDao != null) {
      return _bTCommandsDao;
    } else {
      synchronized(this) {
        if(_bTCommandsDao == null) {
          _bTCommandsDao = new BTCommandsDao_Impl(this);
        }
        return _bTCommandsDao;
      }
    }
  }

  @Override
  public MiscDataDao miscDataDao() {
    if (_miscDataDao != null) {
      return _miscDataDao;
    } else {
      synchronized(this) {
        if(_miscDataDao == null) {
          _miscDataDao = new MiscDataDao_Impl(this);
        }
        return _miscDataDao;
      }
    }
  }

  @Override
  public FilterBikeDao filterBikeDao() {
    if (_filterBikeDao != null) {
      return _filterBikeDao;
    } else {
      synchronized(this) {
        if(_filterBikeDao == null) {
          _filterBikeDao = new FilterBikeDao_Impl(this);
        }
        return _filterBikeDao;
      }
    }
  }

  @Override
  public BTCommandsLogDao btCommandsLogDao() {
    if (_bTCommandsLogDao != null) {
      return _bTCommandsLogDao;
    } else {
      synchronized(this) {
        if(_bTCommandsLogDao == null) {
          _bTCommandsLogDao = new BTCommandsLogDao_Impl(this);
        }
        return _bTCommandsLogDao;
      }
    }
  }

  @Override
  public DAPIoTFileDao dapIoTFileDao() {
    if (_dAPIoTFileDao != null) {
      return _dAPIoTFileDao;
    } else {
      synchronized(this) {
        if(_dAPIoTFileDao == null) {
          _dAPIoTFileDao = new DAPIoTFileDao_Impl(this);
        }
        return _dAPIoTFileDao;
      }
    }
  }

  @Override
  public TripDetailDao tripDetailDao() {
    if (_tripDetailDao != null) {
      return _tripDetailDao;
    } else {
      synchronized(this) {
        if(_tripDetailDao == null) {
          _tripDetailDao = new TripDetailDao_Impl(this);
        }
        return _tripDetailDao;
      }
    }
  }

  @Override
  public LatLongDao latLongDao() {
    if (_latLongDao != null) {
      return _latLongDao;
    } else {
      synchronized(this) {
        if(_latLongDao == null) {
          _latLongDao = new LatLongDao_Impl(this);
        }
        return _latLongDao;
      }
    }
  }

  @Override
  public GyroDao gyroDao() {
    if (_gyroDao != null) {
      return _gyroDao;
    } else {
      synchronized(this) {
        if(_gyroDao == null) {
          _gyroDao = new GyroDao_Impl(this);
        }
        return _gyroDao;
      }
    }
  }

  @Override
  public AccelerometerDao accelerometerDao() {
    if (_accelerometerDao != null) {
      return _accelerometerDao;
    } else {
      synchronized(this) {
        if(_accelerometerDao == null) {
          _accelerometerDao = new AccelerometerDao_Impl(this);
        }
        return _accelerometerDao;
      }
    }
  }

  @Override
  public AdvPacketDao advPacketDao() {
    if (_advPacketDao != null) {
      return _advPacketDao;
    } else {
      synchronized(this) {
        if(_advPacketDao == null) {
          _advPacketDao = new AdvPacketDao_Impl(this);
        }
        return _advPacketDao;
      }
    }
  }
}
