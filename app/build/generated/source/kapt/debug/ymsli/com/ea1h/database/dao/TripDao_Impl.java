package ymsli.com.ea1h.database.dao;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import io.reactivex.Single;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Float;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import ymsli.com.ea1h.database.entity.TripEntity;

@SuppressWarnings({"unchecked", "deprecation"})
public final class TripDao_Impl implements TripDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfTripEntity;

  private final SharedSQLiteStatement __preparedStmtOfSetAPITripsColumns;

  private final SharedSQLiteStatement __preparedStmtOfUpdateBrakeCount;

  private final SharedSQLiteStatement __preparedStmtOfCompleteOnGoingTrip;

  private final SharedSQLiteStatement __preparedStmtOfRemoveTrip;

  private final SharedSQLiteStatement __preparedStmtOfAppendExistingTrip;

  private final SharedSQLiteStatement __preparedStmtOfUpdateUnsyncedTrip;

  private final SharedSQLiteStatement __preparedStmtOfRemoveExtraRecords;

  private final SharedSQLiteStatement __preparedStmtOfDeleteSyncedTrips;

  private final SharedSQLiteStatement __preparedStmtOfUpdatePotentialTripEndCoordinates;

  private final SharedSQLiteStatement __preparedStmtOfUpdateTripSourceAddress;

  private final SharedSQLiteStatement __preparedStmtOfUpdateTripDestinationAddress;

  public TripDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTripEntity = new EntityInsertionAdapter<TripEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `trip_entity`(`tripId`,`startAddress`,`endAddress`,`chassisNumber`,`createdOn`,`startTime`,`endTime`,`distanceCovered`,`averageSpeed`,`breakCount`,`imei`,`lastBatteryVoltage`,`startLat`,`startLon`,`endLat`,`endLon`,`bikeId`,`isActive`,`isSynced`,`updatedOn`,`userId`,`potentialLastLatitude`,`potentialLastLongitude`,`potentialEndTime`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TripEntity value) {
        if (value.getTripId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getTripId());
        }
        if (value.getStartAddress() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getStartAddress());
        }
        if (value.getEndAddress() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getEndAddress());
        }
        if (value.getChassisNumber() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getChassisNumber());
        }
        if (value.getCreatedOn() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindLong(5, value.getCreatedOn());
        }
        if (value.getStartTime() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindLong(6, value.getStartTime());
        }
        if (value.getEndTime() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindLong(7, value.getEndTime());
        }
        if (value.getDistanceCovered() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindDouble(8, value.getDistanceCovered());
        }
        if (value.getAverageSpeed() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindDouble(9, value.getAverageSpeed());
        }
        if (value.getBreakCount() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindLong(10, value.getBreakCount());
        }
        if (value.getImei() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getImei());
        }
        if (value.getBattery() == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindDouble(12, value.getBattery());
        }
        if (value.getStartLat() == null) {
          stmt.bindNull(13);
        } else {
          stmt.bindDouble(13, value.getStartLat());
        }
        if (value.getStartLon() == null) {
          stmt.bindNull(14);
        } else {
          stmt.bindDouble(14, value.getStartLon());
        }
        if (value.getEndLat() == null) {
          stmt.bindNull(15);
        } else {
          stmt.bindDouble(15, value.getEndLat());
        }
        if (value.getEndLon() == null) {
          stmt.bindNull(16);
        } else {
          stmt.bindDouble(16, value.getEndLon());
        }
        if (value.getBikeId() == null) {
          stmt.bindNull(17);
        } else {
          stmt.bindLong(17, value.getBikeId());
        }
        final int _tmp;
        _tmp = value.isActive() ? 1 : 0;
        stmt.bindLong(18, _tmp);
        final int _tmp_1;
        _tmp_1 = value.isSynced() ? 1 : 0;
        stmt.bindLong(19, _tmp_1);
        if (value.getUpdatedOn() == null) {
          stmt.bindNull(20);
        } else {
          stmt.bindLong(20, value.getUpdatedOn());
        }
        if (value.getUserId() == null) {
          stmt.bindNull(21);
        } else {
          stmt.bindString(21, value.getUserId());
        }
        if (value.getPotentialLastLatitude() == null) {
          stmt.bindNull(22);
        } else {
          stmt.bindDouble(22, value.getPotentialLastLatitude());
        }
        if (value.getPotentialLastLongitude() == null) {
          stmt.bindNull(23);
        } else {
          stmt.bindDouble(23, value.getPotentialLastLongitude());
        }
        if (value.getPotentialEndTime() == null) {
          stmt.bindNull(24);
        } else {
          stmt.bindLong(24, value.getPotentialEndTime());
        }
      }
    };
    this.__preparedStmtOfSetAPITripsColumns = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Update trip_entity Set isActive = 0";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateBrakeCount = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Update trip_entity Set breakCount =? where tripId =?";
        return _query;
      }
    };
    this.__preparedStmtOfCompleteOnGoingTrip = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Update trip_entity Set endAddress=?, isActive = 0 Where isActive = 1";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveTrip = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete FROM trip_entity where tripId =?";
        return _query;
      }
    };
    this.__preparedStmtOfAppendExistingTrip = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Update trip_entity Set isActive = 1 where tripId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateUnsyncedTrip = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Update trip_entity set isSynced =1 where tripId =?";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveExtraRecords = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM trip_entity WHERE tripId IN (SELECT tripId FROM(SELECT tripId FROM trip_entity WHERE bikeId =? ORDER BY tripId DESC LIMIT 2000 OFFSET 20) a)";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteSyncedTrips = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from trip_entity where isSynced=1";
        return _query;
      }
    };
    this.__preparedStmtOfUpdatePotentialTripEndCoordinates = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Update trip_entity set potentialEndTime =?, potentialLastLatitude = ?, potentialLastLongitude =?, distanceCovered =? where tripId =?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateTripSourceAddress = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Update trip_entity set startAddress =? where tripId =?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateTripDestinationAddress = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Update trip_entity set endAddress =? where tripId =?";
        return _query;
      }
    };
  }

  @Override
  public void insertNewTrip(final TripEntity... trip) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTripEntity.insert(trip);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void setAPITripsColumns() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfSetAPITripsColumns.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfSetAPITripsColumns.release(_stmt);
    }
  }

  @Override
  public void updateBrakeCount(final int breakCount, final String tripId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateBrakeCount.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, breakCount);
    _argIndex = 2;
    if (tripId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, tripId);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateBrakeCount.release(_stmt);
    }
  }

  @Override
  public void completeOnGoingTrip(final String addLine) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfCompleteOnGoingTrip.acquire();
    int _argIndex = 1;
    if (addLine == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, addLine);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfCompleteOnGoingTrip.release(_stmt);
    }
  }

  @Override
  public void removeTrip(final Integer tripId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveTrip.acquire();
    int _argIndex = 1;
    if (tripId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindLong(_argIndex, tripId);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfRemoveTrip.release(_stmt);
    }
  }

  @Override
  public void appendExistingTrip(final Integer tripId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfAppendExistingTrip.acquire();
    int _argIndex = 1;
    if (tripId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindLong(_argIndex, tripId);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfAppendExistingTrip.release(_stmt);
    }
  }

  @Override
  public void updateUnsyncedTrip(final String tripId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateUnsyncedTrip.acquire();
    int _argIndex = 1;
    if (tripId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, tripId);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateUnsyncedTrip.release(_stmt);
    }
  }

  @Override
  public int removeExtraRecords(final int bikeId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveExtraRecords.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, bikeId);
    __db.beginTransaction();
    try {
      final int _result = _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
      __preparedStmtOfRemoveExtraRecords.release(_stmt);
    }
  }

  @Override
  public void deleteSyncedTrips() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteSyncedTrips.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteSyncedTrips.release(_stmt);
    }
  }

  @Override
  public void updatePotentialTripEndCoordinates(final long potentialEndTime,
      final double potentialLastLatitude, final double potentialLastLongitude,
      final float distanceCovered, final String tripId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdatePotentialTripEndCoordinates.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, potentialEndTime);
    _argIndex = 2;
    _stmt.bindDouble(_argIndex, potentialLastLatitude);
    _argIndex = 3;
    _stmt.bindDouble(_argIndex, potentialLastLongitude);
    _argIndex = 4;
    _stmt.bindDouble(_argIndex, distanceCovered);
    _argIndex = 5;
    if (tripId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, tripId);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdatePotentialTripEndCoordinates.release(_stmt);
    }
  }

  @Override
  public Single<Integer> updateTripSourceAddress(final String tripId, final String startAddress) {
    return Single.fromCallable(new Callable<Integer>() {
      @Override
      public Integer call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateTripSourceAddress.acquire();
        int _argIndex = 1;
        if (startAddress == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, startAddress);
        }
        _argIndex = 2;
        if (tripId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, tripId);
        }
        __db.beginTransaction();
        try {
          final java.lang.Integer _result = _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
          __preparedStmtOfUpdateTripSourceAddress.release(_stmt);
        }
      }
    });
  }

  @Override
  public Single<Integer> updateTripDestinationAddress(final String tripId,
      final String endAddress) {
    return Single.fromCallable(new Callable<Integer>() {
      @Override
      public Integer call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateTripDestinationAddress.acquire();
        int _argIndex = 1;
        if (endAddress == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, endAddress);
        }
        _argIndex = 2;
        if (tripId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, tripId);
        }
        __db.beginTransaction();
        try {
          final java.lang.Integer _result = _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
          __preparedStmtOfUpdateTripDestinationAddress.release(_stmt);
        }
      }
    });
  }

  @Override
  public TripEntity[] getOnGoingTrip() {
    final String _sql = "Select * from trip_entity where isActive = 1 Order by tripId DESC Limit 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfTripId = CursorUtil.getColumnIndexOrThrow(_cursor, "tripId");
      final int _cursorIndexOfStartAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "startAddress");
      final int _cursorIndexOfEndAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "endAddress");
      final int _cursorIndexOfChassisNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "chassisNumber");
      final int _cursorIndexOfCreatedOn = CursorUtil.getColumnIndexOrThrow(_cursor, "createdOn");
      final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
      final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
      final int _cursorIndexOfDistanceCovered = CursorUtil.getColumnIndexOrThrow(_cursor, "distanceCovered");
      final int _cursorIndexOfAverageSpeed = CursorUtil.getColumnIndexOrThrow(_cursor, "averageSpeed");
      final int _cursorIndexOfBreakCount = CursorUtil.getColumnIndexOrThrow(_cursor, "breakCount");
      final int _cursorIndexOfImei = CursorUtil.getColumnIndexOrThrow(_cursor, "imei");
      final int _cursorIndexOfBattery = CursorUtil.getColumnIndexOrThrow(_cursor, "lastBatteryVoltage");
      final int _cursorIndexOfStartLat = CursorUtil.getColumnIndexOrThrow(_cursor, "startLat");
      final int _cursorIndexOfStartLon = CursorUtil.getColumnIndexOrThrow(_cursor, "startLon");
      final int _cursorIndexOfEndLat = CursorUtil.getColumnIndexOrThrow(_cursor, "endLat");
      final int _cursorIndexOfEndLon = CursorUtil.getColumnIndexOrThrow(_cursor, "endLon");
      final int _cursorIndexOfBikeId = CursorUtil.getColumnIndexOrThrow(_cursor, "bikeId");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
      final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
      final int _cursorIndexOfUpdatedOn = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedOn");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfPotentialLastLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "potentialLastLatitude");
      final int _cursorIndexOfPotentialLastLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "potentialLastLongitude");
      final int _cursorIndexOfPotentialEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "potentialEndTime");
      final TripEntity[] _result = new TripEntity[_cursor.getCount()];
      int _index = 0;
      while(_cursor.moveToNext()) {
        final TripEntity _item;
        final String _tmpTripId;
        _tmpTripId = _cursor.getString(_cursorIndexOfTripId);
        final String _tmpStartAddress;
        _tmpStartAddress = _cursor.getString(_cursorIndexOfStartAddress);
        final String _tmpEndAddress;
        _tmpEndAddress = _cursor.getString(_cursorIndexOfEndAddress);
        final String _tmpChassisNumber;
        _tmpChassisNumber = _cursor.getString(_cursorIndexOfChassisNumber);
        final Long _tmpCreatedOn;
        if (_cursor.isNull(_cursorIndexOfCreatedOn)) {
          _tmpCreatedOn = null;
        } else {
          _tmpCreatedOn = _cursor.getLong(_cursorIndexOfCreatedOn);
        }
        final Long _tmpStartTime;
        if (_cursor.isNull(_cursorIndexOfStartTime)) {
          _tmpStartTime = null;
        } else {
          _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
        }
        final Long _tmpEndTime;
        if (_cursor.isNull(_cursorIndexOfEndTime)) {
          _tmpEndTime = null;
        } else {
          _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
        }
        final Float _tmpDistanceCovered;
        if (_cursor.isNull(_cursorIndexOfDistanceCovered)) {
          _tmpDistanceCovered = null;
        } else {
          _tmpDistanceCovered = _cursor.getFloat(_cursorIndexOfDistanceCovered);
        }
        final Float _tmpAverageSpeed;
        if (_cursor.isNull(_cursorIndexOfAverageSpeed)) {
          _tmpAverageSpeed = null;
        } else {
          _tmpAverageSpeed = _cursor.getFloat(_cursorIndexOfAverageSpeed);
        }
        final Integer _tmpBreakCount;
        if (_cursor.isNull(_cursorIndexOfBreakCount)) {
          _tmpBreakCount = null;
        } else {
          _tmpBreakCount = _cursor.getInt(_cursorIndexOfBreakCount);
        }
        final String _tmpImei;
        _tmpImei = _cursor.getString(_cursorIndexOfImei);
        final Float _tmpBattery;
        if (_cursor.isNull(_cursorIndexOfBattery)) {
          _tmpBattery = null;
        } else {
          _tmpBattery = _cursor.getFloat(_cursorIndexOfBattery);
        }
        final Double _tmpStartLat;
        if (_cursor.isNull(_cursorIndexOfStartLat)) {
          _tmpStartLat = null;
        } else {
          _tmpStartLat = _cursor.getDouble(_cursorIndexOfStartLat);
        }
        final Double _tmpStartLon;
        if (_cursor.isNull(_cursorIndexOfStartLon)) {
          _tmpStartLon = null;
        } else {
          _tmpStartLon = _cursor.getDouble(_cursorIndexOfStartLon);
        }
        final Double _tmpEndLat;
        if (_cursor.isNull(_cursorIndexOfEndLat)) {
          _tmpEndLat = null;
        } else {
          _tmpEndLat = _cursor.getDouble(_cursorIndexOfEndLat);
        }
        final Double _tmpEndLon;
        if (_cursor.isNull(_cursorIndexOfEndLon)) {
          _tmpEndLon = null;
        } else {
          _tmpEndLon = _cursor.getDouble(_cursorIndexOfEndLon);
        }
        final Integer _tmpBikeId;
        if (_cursor.isNull(_cursorIndexOfBikeId)) {
          _tmpBikeId = null;
        } else {
          _tmpBikeId = _cursor.getInt(_cursorIndexOfBikeId);
        }
        final boolean _tmpIsActive;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsActive);
        _tmpIsActive = _tmp != 0;
        final boolean _tmpIsSynced;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsSynced);
        _tmpIsSynced = _tmp_1 != 0;
        final Long _tmpUpdatedOn;
        if (_cursor.isNull(_cursorIndexOfUpdatedOn)) {
          _tmpUpdatedOn = null;
        } else {
          _tmpUpdatedOn = _cursor.getLong(_cursorIndexOfUpdatedOn);
        }
        final String _tmpUserId;
        _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
        final Double _tmpPotentialLastLatitude;
        if (_cursor.isNull(_cursorIndexOfPotentialLastLatitude)) {
          _tmpPotentialLastLatitude = null;
        } else {
          _tmpPotentialLastLatitude = _cursor.getDouble(_cursorIndexOfPotentialLastLatitude);
        }
        final Double _tmpPotentialLastLongitude;
        if (_cursor.isNull(_cursorIndexOfPotentialLastLongitude)) {
          _tmpPotentialLastLongitude = null;
        } else {
          _tmpPotentialLastLongitude = _cursor.getDouble(_cursorIndexOfPotentialLastLongitude);
        }
        final Long _tmpPotentialEndTime;
        if (_cursor.isNull(_cursorIndexOfPotentialEndTime)) {
          _tmpPotentialEndTime = null;
        } else {
          _tmpPotentialEndTime = _cursor.getLong(_cursorIndexOfPotentialEndTime);
        }
        _item = new TripEntity(_tmpTripId,_tmpStartAddress,_tmpEndAddress,_tmpChassisNumber,_tmpCreatedOn,_tmpStartTime,_tmpEndTime,_tmpDistanceCovered,_tmpAverageSpeed,_tmpBreakCount,_tmpImei,_tmpBattery,_tmpStartLat,_tmpStartLon,_tmpEndLat,_tmpEndLon,_tmpBikeId,_tmpIsActive,_tmpIsSynced,_tmpUpdatedOn,_tmpUserId,_tmpPotentialLastLatitude,_tmpPotentialLastLongitude,_tmpPotentialEndTime);
        _result[_index] = _item;
        _index ++;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<TripEntity[]> getOnGoingTripLiveData() {
    final String _sql = "Select * from trip_entity where isActive = 1 Order by tripId DESC Limit 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"trip_entity"}, false, new Callable<TripEntity[]>() {
      @Override
      public TripEntity[] call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false);
        try {
          final int _cursorIndexOfTripId = CursorUtil.getColumnIndexOrThrow(_cursor, "tripId");
          final int _cursorIndexOfStartAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "startAddress");
          final int _cursorIndexOfEndAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "endAddress");
          final int _cursorIndexOfChassisNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "chassisNumber");
          final int _cursorIndexOfCreatedOn = CursorUtil.getColumnIndexOrThrow(_cursor, "createdOn");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfDistanceCovered = CursorUtil.getColumnIndexOrThrow(_cursor, "distanceCovered");
          final int _cursorIndexOfAverageSpeed = CursorUtil.getColumnIndexOrThrow(_cursor, "averageSpeed");
          final int _cursorIndexOfBreakCount = CursorUtil.getColumnIndexOrThrow(_cursor, "breakCount");
          final int _cursorIndexOfImei = CursorUtil.getColumnIndexOrThrow(_cursor, "imei");
          final int _cursorIndexOfBattery = CursorUtil.getColumnIndexOrThrow(_cursor, "lastBatteryVoltage");
          final int _cursorIndexOfStartLat = CursorUtil.getColumnIndexOrThrow(_cursor, "startLat");
          final int _cursorIndexOfStartLon = CursorUtil.getColumnIndexOrThrow(_cursor, "startLon");
          final int _cursorIndexOfEndLat = CursorUtil.getColumnIndexOrThrow(_cursor, "endLat");
          final int _cursorIndexOfEndLon = CursorUtil.getColumnIndexOrThrow(_cursor, "endLon");
          final int _cursorIndexOfBikeId = CursorUtil.getColumnIndexOrThrow(_cursor, "bikeId");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final int _cursorIndexOfUpdatedOn = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedOn");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfPotentialLastLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "potentialLastLatitude");
          final int _cursorIndexOfPotentialLastLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "potentialLastLongitude");
          final int _cursorIndexOfPotentialEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "potentialEndTime");
          final TripEntity[] _result = new TripEntity[_cursor.getCount()];
          int _index = 0;
          while(_cursor.moveToNext()) {
            final TripEntity _item;
            final String _tmpTripId;
            _tmpTripId = _cursor.getString(_cursorIndexOfTripId);
            final String _tmpStartAddress;
            _tmpStartAddress = _cursor.getString(_cursorIndexOfStartAddress);
            final String _tmpEndAddress;
            _tmpEndAddress = _cursor.getString(_cursorIndexOfEndAddress);
            final String _tmpChassisNumber;
            _tmpChassisNumber = _cursor.getString(_cursorIndexOfChassisNumber);
            final Long _tmpCreatedOn;
            if (_cursor.isNull(_cursorIndexOfCreatedOn)) {
              _tmpCreatedOn = null;
            } else {
              _tmpCreatedOn = _cursor.getLong(_cursorIndexOfCreatedOn);
            }
            final Long _tmpStartTime;
            if (_cursor.isNull(_cursorIndexOfStartTime)) {
              _tmpStartTime = null;
            } else {
              _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            }
            final Long _tmpEndTime;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmpEndTime = null;
            } else {
              _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
            }
            final Float _tmpDistanceCovered;
            if (_cursor.isNull(_cursorIndexOfDistanceCovered)) {
              _tmpDistanceCovered = null;
            } else {
              _tmpDistanceCovered = _cursor.getFloat(_cursorIndexOfDistanceCovered);
            }
            final Float _tmpAverageSpeed;
            if (_cursor.isNull(_cursorIndexOfAverageSpeed)) {
              _tmpAverageSpeed = null;
            } else {
              _tmpAverageSpeed = _cursor.getFloat(_cursorIndexOfAverageSpeed);
            }
            final Integer _tmpBreakCount;
            if (_cursor.isNull(_cursorIndexOfBreakCount)) {
              _tmpBreakCount = null;
            } else {
              _tmpBreakCount = _cursor.getInt(_cursorIndexOfBreakCount);
            }
            final String _tmpImei;
            _tmpImei = _cursor.getString(_cursorIndexOfImei);
            final Float _tmpBattery;
            if (_cursor.isNull(_cursorIndexOfBattery)) {
              _tmpBattery = null;
            } else {
              _tmpBattery = _cursor.getFloat(_cursorIndexOfBattery);
            }
            final Double _tmpStartLat;
            if (_cursor.isNull(_cursorIndexOfStartLat)) {
              _tmpStartLat = null;
            } else {
              _tmpStartLat = _cursor.getDouble(_cursorIndexOfStartLat);
            }
            final Double _tmpStartLon;
            if (_cursor.isNull(_cursorIndexOfStartLon)) {
              _tmpStartLon = null;
            } else {
              _tmpStartLon = _cursor.getDouble(_cursorIndexOfStartLon);
            }
            final Double _tmpEndLat;
            if (_cursor.isNull(_cursorIndexOfEndLat)) {
              _tmpEndLat = null;
            } else {
              _tmpEndLat = _cursor.getDouble(_cursorIndexOfEndLat);
            }
            final Double _tmpEndLon;
            if (_cursor.isNull(_cursorIndexOfEndLon)) {
              _tmpEndLon = null;
            } else {
              _tmpEndLon = _cursor.getDouble(_cursorIndexOfEndLon);
            }
            final Integer _tmpBikeId;
            if (_cursor.isNull(_cursorIndexOfBikeId)) {
              _tmpBikeId = null;
            } else {
              _tmpBikeId = _cursor.getInt(_cursorIndexOfBikeId);
            }
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final boolean _tmpIsSynced;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp_1 != 0;
            final Long _tmpUpdatedOn;
            if (_cursor.isNull(_cursorIndexOfUpdatedOn)) {
              _tmpUpdatedOn = null;
            } else {
              _tmpUpdatedOn = _cursor.getLong(_cursorIndexOfUpdatedOn);
            }
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final Double _tmpPotentialLastLatitude;
            if (_cursor.isNull(_cursorIndexOfPotentialLastLatitude)) {
              _tmpPotentialLastLatitude = null;
            } else {
              _tmpPotentialLastLatitude = _cursor.getDouble(_cursorIndexOfPotentialLastLatitude);
            }
            final Double _tmpPotentialLastLongitude;
            if (_cursor.isNull(_cursorIndexOfPotentialLastLongitude)) {
              _tmpPotentialLastLongitude = null;
            } else {
              _tmpPotentialLastLongitude = _cursor.getDouble(_cursorIndexOfPotentialLastLongitude);
            }
            final Long _tmpPotentialEndTime;
            if (_cursor.isNull(_cursorIndexOfPotentialEndTime)) {
              _tmpPotentialEndTime = null;
            } else {
              _tmpPotentialEndTime = _cursor.getLong(_cursorIndexOfPotentialEndTime);
            }
            _item = new TripEntity(_tmpTripId,_tmpStartAddress,_tmpEndAddress,_tmpChassisNumber,_tmpCreatedOn,_tmpStartTime,_tmpEndTime,_tmpDistanceCovered,_tmpAverageSpeed,_tmpBreakCount,_tmpImei,_tmpBattery,_tmpStartLat,_tmpStartLon,_tmpEndLat,_tmpEndLon,_tmpBikeId,_tmpIsActive,_tmpIsSynced,_tmpUpdatedOn,_tmpUserId,_tmpPotentialLastLatitude,_tmpPotentialLastLongitude,_tmpPotentialEndTime);
            _result[_index] = _item;
            _index ++;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public TripEntity[] getLastTrip() {
    final String _sql = "Select * from trip_entity Order by tripId DESC Limit 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfTripId = CursorUtil.getColumnIndexOrThrow(_cursor, "tripId");
      final int _cursorIndexOfStartAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "startAddress");
      final int _cursorIndexOfEndAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "endAddress");
      final int _cursorIndexOfChassisNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "chassisNumber");
      final int _cursorIndexOfCreatedOn = CursorUtil.getColumnIndexOrThrow(_cursor, "createdOn");
      final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
      final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
      final int _cursorIndexOfDistanceCovered = CursorUtil.getColumnIndexOrThrow(_cursor, "distanceCovered");
      final int _cursorIndexOfAverageSpeed = CursorUtil.getColumnIndexOrThrow(_cursor, "averageSpeed");
      final int _cursorIndexOfBreakCount = CursorUtil.getColumnIndexOrThrow(_cursor, "breakCount");
      final int _cursorIndexOfImei = CursorUtil.getColumnIndexOrThrow(_cursor, "imei");
      final int _cursorIndexOfBattery = CursorUtil.getColumnIndexOrThrow(_cursor, "lastBatteryVoltage");
      final int _cursorIndexOfStartLat = CursorUtil.getColumnIndexOrThrow(_cursor, "startLat");
      final int _cursorIndexOfStartLon = CursorUtil.getColumnIndexOrThrow(_cursor, "startLon");
      final int _cursorIndexOfEndLat = CursorUtil.getColumnIndexOrThrow(_cursor, "endLat");
      final int _cursorIndexOfEndLon = CursorUtil.getColumnIndexOrThrow(_cursor, "endLon");
      final int _cursorIndexOfBikeId = CursorUtil.getColumnIndexOrThrow(_cursor, "bikeId");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
      final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
      final int _cursorIndexOfUpdatedOn = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedOn");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfPotentialLastLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "potentialLastLatitude");
      final int _cursorIndexOfPotentialLastLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "potentialLastLongitude");
      final int _cursorIndexOfPotentialEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "potentialEndTime");
      final TripEntity[] _result = new TripEntity[_cursor.getCount()];
      int _index = 0;
      while(_cursor.moveToNext()) {
        final TripEntity _item;
        final String _tmpTripId;
        _tmpTripId = _cursor.getString(_cursorIndexOfTripId);
        final String _tmpStartAddress;
        _tmpStartAddress = _cursor.getString(_cursorIndexOfStartAddress);
        final String _tmpEndAddress;
        _tmpEndAddress = _cursor.getString(_cursorIndexOfEndAddress);
        final String _tmpChassisNumber;
        _tmpChassisNumber = _cursor.getString(_cursorIndexOfChassisNumber);
        final Long _tmpCreatedOn;
        if (_cursor.isNull(_cursorIndexOfCreatedOn)) {
          _tmpCreatedOn = null;
        } else {
          _tmpCreatedOn = _cursor.getLong(_cursorIndexOfCreatedOn);
        }
        final Long _tmpStartTime;
        if (_cursor.isNull(_cursorIndexOfStartTime)) {
          _tmpStartTime = null;
        } else {
          _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
        }
        final Long _tmpEndTime;
        if (_cursor.isNull(_cursorIndexOfEndTime)) {
          _tmpEndTime = null;
        } else {
          _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
        }
        final Float _tmpDistanceCovered;
        if (_cursor.isNull(_cursorIndexOfDistanceCovered)) {
          _tmpDistanceCovered = null;
        } else {
          _tmpDistanceCovered = _cursor.getFloat(_cursorIndexOfDistanceCovered);
        }
        final Float _tmpAverageSpeed;
        if (_cursor.isNull(_cursorIndexOfAverageSpeed)) {
          _tmpAverageSpeed = null;
        } else {
          _tmpAverageSpeed = _cursor.getFloat(_cursorIndexOfAverageSpeed);
        }
        final Integer _tmpBreakCount;
        if (_cursor.isNull(_cursorIndexOfBreakCount)) {
          _tmpBreakCount = null;
        } else {
          _tmpBreakCount = _cursor.getInt(_cursorIndexOfBreakCount);
        }
        final String _tmpImei;
        _tmpImei = _cursor.getString(_cursorIndexOfImei);
        final Float _tmpBattery;
        if (_cursor.isNull(_cursorIndexOfBattery)) {
          _tmpBattery = null;
        } else {
          _tmpBattery = _cursor.getFloat(_cursorIndexOfBattery);
        }
        final Double _tmpStartLat;
        if (_cursor.isNull(_cursorIndexOfStartLat)) {
          _tmpStartLat = null;
        } else {
          _tmpStartLat = _cursor.getDouble(_cursorIndexOfStartLat);
        }
        final Double _tmpStartLon;
        if (_cursor.isNull(_cursorIndexOfStartLon)) {
          _tmpStartLon = null;
        } else {
          _tmpStartLon = _cursor.getDouble(_cursorIndexOfStartLon);
        }
        final Double _tmpEndLat;
        if (_cursor.isNull(_cursorIndexOfEndLat)) {
          _tmpEndLat = null;
        } else {
          _tmpEndLat = _cursor.getDouble(_cursorIndexOfEndLat);
        }
        final Double _tmpEndLon;
        if (_cursor.isNull(_cursorIndexOfEndLon)) {
          _tmpEndLon = null;
        } else {
          _tmpEndLon = _cursor.getDouble(_cursorIndexOfEndLon);
        }
        final Integer _tmpBikeId;
        if (_cursor.isNull(_cursorIndexOfBikeId)) {
          _tmpBikeId = null;
        } else {
          _tmpBikeId = _cursor.getInt(_cursorIndexOfBikeId);
        }
        final boolean _tmpIsActive;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsActive);
        _tmpIsActive = _tmp != 0;
        final boolean _tmpIsSynced;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsSynced);
        _tmpIsSynced = _tmp_1 != 0;
        final Long _tmpUpdatedOn;
        if (_cursor.isNull(_cursorIndexOfUpdatedOn)) {
          _tmpUpdatedOn = null;
        } else {
          _tmpUpdatedOn = _cursor.getLong(_cursorIndexOfUpdatedOn);
        }
        final String _tmpUserId;
        _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
        final Double _tmpPotentialLastLatitude;
        if (_cursor.isNull(_cursorIndexOfPotentialLastLatitude)) {
          _tmpPotentialLastLatitude = null;
        } else {
          _tmpPotentialLastLatitude = _cursor.getDouble(_cursorIndexOfPotentialLastLatitude);
        }
        final Double _tmpPotentialLastLongitude;
        if (_cursor.isNull(_cursorIndexOfPotentialLastLongitude)) {
          _tmpPotentialLastLongitude = null;
        } else {
          _tmpPotentialLastLongitude = _cursor.getDouble(_cursorIndexOfPotentialLastLongitude);
        }
        final Long _tmpPotentialEndTime;
        if (_cursor.isNull(_cursorIndexOfPotentialEndTime)) {
          _tmpPotentialEndTime = null;
        } else {
          _tmpPotentialEndTime = _cursor.getLong(_cursorIndexOfPotentialEndTime);
        }
        _item = new TripEntity(_tmpTripId,_tmpStartAddress,_tmpEndAddress,_tmpChassisNumber,_tmpCreatedOn,_tmpStartTime,_tmpEndTime,_tmpDistanceCovered,_tmpAverageSpeed,_tmpBreakCount,_tmpImei,_tmpBattery,_tmpStartLat,_tmpStartLon,_tmpEndLat,_tmpEndLon,_tmpBikeId,_tmpIsActive,_tmpIsSynced,_tmpUpdatedOn,_tmpUserId,_tmpPotentialLastLatitude,_tmpPotentialLastLongitude,_tmpPotentialEndTime);
        _result[_index] = _item;
        _index ++;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<TripEntity> getLastTripLiveData(final String userId) {
    final String _sql = "Select * from trip_entity where isActive =0 and userId =? Order by startTime DESC Limit 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (userId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userId);
    }
    return __db.getInvalidationTracker().createLiveData(new String[]{"trip_entity"}, false, new Callable<TripEntity>() {
      @Override
      public TripEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false);
        try {
          final int _cursorIndexOfTripId = CursorUtil.getColumnIndexOrThrow(_cursor, "tripId");
          final int _cursorIndexOfStartAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "startAddress");
          final int _cursorIndexOfEndAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "endAddress");
          final int _cursorIndexOfChassisNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "chassisNumber");
          final int _cursorIndexOfCreatedOn = CursorUtil.getColumnIndexOrThrow(_cursor, "createdOn");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfDistanceCovered = CursorUtil.getColumnIndexOrThrow(_cursor, "distanceCovered");
          final int _cursorIndexOfAverageSpeed = CursorUtil.getColumnIndexOrThrow(_cursor, "averageSpeed");
          final int _cursorIndexOfBreakCount = CursorUtil.getColumnIndexOrThrow(_cursor, "breakCount");
          final int _cursorIndexOfImei = CursorUtil.getColumnIndexOrThrow(_cursor, "imei");
          final int _cursorIndexOfBattery = CursorUtil.getColumnIndexOrThrow(_cursor, "lastBatteryVoltage");
          final int _cursorIndexOfStartLat = CursorUtil.getColumnIndexOrThrow(_cursor, "startLat");
          final int _cursorIndexOfStartLon = CursorUtil.getColumnIndexOrThrow(_cursor, "startLon");
          final int _cursorIndexOfEndLat = CursorUtil.getColumnIndexOrThrow(_cursor, "endLat");
          final int _cursorIndexOfEndLon = CursorUtil.getColumnIndexOrThrow(_cursor, "endLon");
          final int _cursorIndexOfBikeId = CursorUtil.getColumnIndexOrThrow(_cursor, "bikeId");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final int _cursorIndexOfUpdatedOn = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedOn");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfPotentialLastLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "potentialLastLatitude");
          final int _cursorIndexOfPotentialLastLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "potentialLastLongitude");
          final int _cursorIndexOfPotentialEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "potentialEndTime");
          final TripEntity _result;
          if(_cursor.moveToFirst()) {
            final String _tmpTripId;
            _tmpTripId = _cursor.getString(_cursorIndexOfTripId);
            final String _tmpStartAddress;
            _tmpStartAddress = _cursor.getString(_cursorIndexOfStartAddress);
            final String _tmpEndAddress;
            _tmpEndAddress = _cursor.getString(_cursorIndexOfEndAddress);
            final String _tmpChassisNumber;
            _tmpChassisNumber = _cursor.getString(_cursorIndexOfChassisNumber);
            final Long _tmpCreatedOn;
            if (_cursor.isNull(_cursorIndexOfCreatedOn)) {
              _tmpCreatedOn = null;
            } else {
              _tmpCreatedOn = _cursor.getLong(_cursorIndexOfCreatedOn);
            }
            final Long _tmpStartTime;
            if (_cursor.isNull(_cursorIndexOfStartTime)) {
              _tmpStartTime = null;
            } else {
              _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            }
            final Long _tmpEndTime;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmpEndTime = null;
            } else {
              _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
            }
            final Float _tmpDistanceCovered;
            if (_cursor.isNull(_cursorIndexOfDistanceCovered)) {
              _tmpDistanceCovered = null;
            } else {
              _tmpDistanceCovered = _cursor.getFloat(_cursorIndexOfDistanceCovered);
            }
            final Float _tmpAverageSpeed;
            if (_cursor.isNull(_cursorIndexOfAverageSpeed)) {
              _tmpAverageSpeed = null;
            } else {
              _tmpAverageSpeed = _cursor.getFloat(_cursorIndexOfAverageSpeed);
            }
            final Integer _tmpBreakCount;
            if (_cursor.isNull(_cursorIndexOfBreakCount)) {
              _tmpBreakCount = null;
            } else {
              _tmpBreakCount = _cursor.getInt(_cursorIndexOfBreakCount);
            }
            final String _tmpImei;
            _tmpImei = _cursor.getString(_cursorIndexOfImei);
            final Float _tmpBattery;
            if (_cursor.isNull(_cursorIndexOfBattery)) {
              _tmpBattery = null;
            } else {
              _tmpBattery = _cursor.getFloat(_cursorIndexOfBattery);
            }
            final Double _tmpStartLat;
            if (_cursor.isNull(_cursorIndexOfStartLat)) {
              _tmpStartLat = null;
            } else {
              _tmpStartLat = _cursor.getDouble(_cursorIndexOfStartLat);
            }
            final Double _tmpStartLon;
            if (_cursor.isNull(_cursorIndexOfStartLon)) {
              _tmpStartLon = null;
            } else {
              _tmpStartLon = _cursor.getDouble(_cursorIndexOfStartLon);
            }
            final Double _tmpEndLat;
            if (_cursor.isNull(_cursorIndexOfEndLat)) {
              _tmpEndLat = null;
            } else {
              _tmpEndLat = _cursor.getDouble(_cursorIndexOfEndLat);
            }
            final Double _tmpEndLon;
            if (_cursor.isNull(_cursorIndexOfEndLon)) {
              _tmpEndLon = null;
            } else {
              _tmpEndLon = _cursor.getDouble(_cursorIndexOfEndLon);
            }
            final Integer _tmpBikeId;
            if (_cursor.isNull(_cursorIndexOfBikeId)) {
              _tmpBikeId = null;
            } else {
              _tmpBikeId = _cursor.getInt(_cursorIndexOfBikeId);
            }
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final boolean _tmpIsSynced;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp_1 != 0;
            final Long _tmpUpdatedOn;
            if (_cursor.isNull(_cursorIndexOfUpdatedOn)) {
              _tmpUpdatedOn = null;
            } else {
              _tmpUpdatedOn = _cursor.getLong(_cursorIndexOfUpdatedOn);
            }
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final Double _tmpPotentialLastLatitude;
            if (_cursor.isNull(_cursorIndexOfPotentialLastLatitude)) {
              _tmpPotentialLastLatitude = null;
            } else {
              _tmpPotentialLastLatitude = _cursor.getDouble(_cursorIndexOfPotentialLastLatitude);
            }
            final Double _tmpPotentialLastLongitude;
            if (_cursor.isNull(_cursorIndexOfPotentialLastLongitude)) {
              _tmpPotentialLastLongitude = null;
            } else {
              _tmpPotentialLastLongitude = _cursor.getDouble(_cursorIndexOfPotentialLastLongitude);
            }
            final Long _tmpPotentialEndTime;
            if (_cursor.isNull(_cursorIndexOfPotentialEndTime)) {
              _tmpPotentialEndTime = null;
            } else {
              _tmpPotentialEndTime = _cursor.getLong(_cursorIndexOfPotentialEndTime);
            }
            _result = new TripEntity(_tmpTripId,_tmpStartAddress,_tmpEndAddress,_tmpChassisNumber,_tmpCreatedOn,_tmpStartTime,_tmpEndTime,_tmpDistanceCovered,_tmpAverageSpeed,_tmpBreakCount,_tmpImei,_tmpBattery,_tmpStartLat,_tmpStartLon,_tmpEndLat,_tmpEndLon,_tmpBikeId,_tmpIsActive,_tmpIsSynced,_tmpUpdatedOn,_tmpUserId,_tmpPotentialLastLatitude,_tmpPotentialLastLongitude,_tmpPotentialEndTime);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<TripEntity[]> getAllTripsLive(final String userId) {
    final String _sql = "Select * FROM trip_entity where userId = ? Order By startTime DESC Limit 20";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (userId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userId);
    }
    return __db.getInvalidationTracker().createLiveData(new String[]{"trip_entity"}, false, new Callable<TripEntity[]>() {
      @Override
      public TripEntity[] call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false);
        try {
          final int _cursorIndexOfTripId = CursorUtil.getColumnIndexOrThrow(_cursor, "tripId");
          final int _cursorIndexOfStartAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "startAddress");
          final int _cursorIndexOfEndAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "endAddress");
          final int _cursorIndexOfChassisNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "chassisNumber");
          final int _cursorIndexOfCreatedOn = CursorUtil.getColumnIndexOrThrow(_cursor, "createdOn");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfDistanceCovered = CursorUtil.getColumnIndexOrThrow(_cursor, "distanceCovered");
          final int _cursorIndexOfAverageSpeed = CursorUtil.getColumnIndexOrThrow(_cursor, "averageSpeed");
          final int _cursorIndexOfBreakCount = CursorUtil.getColumnIndexOrThrow(_cursor, "breakCount");
          final int _cursorIndexOfImei = CursorUtil.getColumnIndexOrThrow(_cursor, "imei");
          final int _cursorIndexOfBattery = CursorUtil.getColumnIndexOrThrow(_cursor, "lastBatteryVoltage");
          final int _cursorIndexOfStartLat = CursorUtil.getColumnIndexOrThrow(_cursor, "startLat");
          final int _cursorIndexOfStartLon = CursorUtil.getColumnIndexOrThrow(_cursor, "startLon");
          final int _cursorIndexOfEndLat = CursorUtil.getColumnIndexOrThrow(_cursor, "endLat");
          final int _cursorIndexOfEndLon = CursorUtil.getColumnIndexOrThrow(_cursor, "endLon");
          final int _cursorIndexOfBikeId = CursorUtil.getColumnIndexOrThrow(_cursor, "bikeId");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final int _cursorIndexOfUpdatedOn = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedOn");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfPotentialLastLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "potentialLastLatitude");
          final int _cursorIndexOfPotentialLastLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "potentialLastLongitude");
          final int _cursorIndexOfPotentialEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "potentialEndTime");
          final TripEntity[] _result = new TripEntity[_cursor.getCount()];
          int _index = 0;
          while(_cursor.moveToNext()) {
            final TripEntity _item;
            final String _tmpTripId;
            _tmpTripId = _cursor.getString(_cursorIndexOfTripId);
            final String _tmpStartAddress;
            _tmpStartAddress = _cursor.getString(_cursorIndexOfStartAddress);
            final String _tmpEndAddress;
            _tmpEndAddress = _cursor.getString(_cursorIndexOfEndAddress);
            final String _tmpChassisNumber;
            _tmpChassisNumber = _cursor.getString(_cursorIndexOfChassisNumber);
            final Long _tmpCreatedOn;
            if (_cursor.isNull(_cursorIndexOfCreatedOn)) {
              _tmpCreatedOn = null;
            } else {
              _tmpCreatedOn = _cursor.getLong(_cursorIndexOfCreatedOn);
            }
            final Long _tmpStartTime;
            if (_cursor.isNull(_cursorIndexOfStartTime)) {
              _tmpStartTime = null;
            } else {
              _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            }
            final Long _tmpEndTime;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmpEndTime = null;
            } else {
              _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
            }
            final Float _tmpDistanceCovered;
            if (_cursor.isNull(_cursorIndexOfDistanceCovered)) {
              _tmpDistanceCovered = null;
            } else {
              _tmpDistanceCovered = _cursor.getFloat(_cursorIndexOfDistanceCovered);
            }
            final Float _tmpAverageSpeed;
            if (_cursor.isNull(_cursorIndexOfAverageSpeed)) {
              _tmpAverageSpeed = null;
            } else {
              _tmpAverageSpeed = _cursor.getFloat(_cursorIndexOfAverageSpeed);
            }
            final Integer _tmpBreakCount;
            if (_cursor.isNull(_cursorIndexOfBreakCount)) {
              _tmpBreakCount = null;
            } else {
              _tmpBreakCount = _cursor.getInt(_cursorIndexOfBreakCount);
            }
            final String _tmpImei;
            _tmpImei = _cursor.getString(_cursorIndexOfImei);
            final Float _tmpBattery;
            if (_cursor.isNull(_cursorIndexOfBattery)) {
              _tmpBattery = null;
            } else {
              _tmpBattery = _cursor.getFloat(_cursorIndexOfBattery);
            }
            final Double _tmpStartLat;
            if (_cursor.isNull(_cursorIndexOfStartLat)) {
              _tmpStartLat = null;
            } else {
              _tmpStartLat = _cursor.getDouble(_cursorIndexOfStartLat);
            }
            final Double _tmpStartLon;
            if (_cursor.isNull(_cursorIndexOfStartLon)) {
              _tmpStartLon = null;
            } else {
              _tmpStartLon = _cursor.getDouble(_cursorIndexOfStartLon);
            }
            final Double _tmpEndLat;
            if (_cursor.isNull(_cursorIndexOfEndLat)) {
              _tmpEndLat = null;
            } else {
              _tmpEndLat = _cursor.getDouble(_cursorIndexOfEndLat);
            }
            final Double _tmpEndLon;
            if (_cursor.isNull(_cursorIndexOfEndLon)) {
              _tmpEndLon = null;
            } else {
              _tmpEndLon = _cursor.getDouble(_cursorIndexOfEndLon);
            }
            final Integer _tmpBikeId;
            if (_cursor.isNull(_cursorIndexOfBikeId)) {
              _tmpBikeId = null;
            } else {
              _tmpBikeId = _cursor.getInt(_cursorIndexOfBikeId);
            }
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final boolean _tmpIsSynced;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp_1 != 0;
            final Long _tmpUpdatedOn;
            if (_cursor.isNull(_cursorIndexOfUpdatedOn)) {
              _tmpUpdatedOn = null;
            } else {
              _tmpUpdatedOn = _cursor.getLong(_cursorIndexOfUpdatedOn);
            }
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final Double _tmpPotentialLastLatitude;
            if (_cursor.isNull(_cursorIndexOfPotentialLastLatitude)) {
              _tmpPotentialLastLatitude = null;
            } else {
              _tmpPotentialLastLatitude = _cursor.getDouble(_cursorIndexOfPotentialLastLatitude);
            }
            final Double _tmpPotentialLastLongitude;
            if (_cursor.isNull(_cursorIndexOfPotentialLastLongitude)) {
              _tmpPotentialLastLongitude = null;
            } else {
              _tmpPotentialLastLongitude = _cursor.getDouble(_cursorIndexOfPotentialLastLongitude);
            }
            final Long _tmpPotentialEndTime;
            if (_cursor.isNull(_cursorIndexOfPotentialEndTime)) {
              _tmpPotentialEndTime = null;
            } else {
              _tmpPotentialEndTime = _cursor.getLong(_cursorIndexOfPotentialEndTime);
            }
            _item = new TripEntity(_tmpTripId,_tmpStartAddress,_tmpEndAddress,_tmpChassisNumber,_tmpCreatedOn,_tmpStartTime,_tmpEndTime,_tmpDistanceCovered,_tmpAverageSpeed,_tmpBreakCount,_tmpImei,_tmpBattery,_tmpStartLat,_tmpStartLon,_tmpEndLat,_tmpEndLon,_tmpBikeId,_tmpIsActive,_tmpIsSynced,_tmpUpdatedOn,_tmpUserId,_tmpPotentialLastLatitude,_tmpPotentialLastLongitude,_tmpPotentialEndTime);
            _result[_index] = _item;
            _index ++;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public TripEntity[] getUnsyncedTrips() {
    final String _sql = "Select * FROM trip_entity where isSynced=0 and isActive=0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfTripId = CursorUtil.getColumnIndexOrThrow(_cursor, "tripId");
      final int _cursorIndexOfStartAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "startAddress");
      final int _cursorIndexOfEndAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "endAddress");
      final int _cursorIndexOfChassisNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "chassisNumber");
      final int _cursorIndexOfCreatedOn = CursorUtil.getColumnIndexOrThrow(_cursor, "createdOn");
      final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
      final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
      final int _cursorIndexOfDistanceCovered = CursorUtil.getColumnIndexOrThrow(_cursor, "distanceCovered");
      final int _cursorIndexOfAverageSpeed = CursorUtil.getColumnIndexOrThrow(_cursor, "averageSpeed");
      final int _cursorIndexOfBreakCount = CursorUtil.getColumnIndexOrThrow(_cursor, "breakCount");
      final int _cursorIndexOfImei = CursorUtil.getColumnIndexOrThrow(_cursor, "imei");
      final int _cursorIndexOfBattery = CursorUtil.getColumnIndexOrThrow(_cursor, "lastBatteryVoltage");
      final int _cursorIndexOfStartLat = CursorUtil.getColumnIndexOrThrow(_cursor, "startLat");
      final int _cursorIndexOfStartLon = CursorUtil.getColumnIndexOrThrow(_cursor, "startLon");
      final int _cursorIndexOfEndLat = CursorUtil.getColumnIndexOrThrow(_cursor, "endLat");
      final int _cursorIndexOfEndLon = CursorUtil.getColumnIndexOrThrow(_cursor, "endLon");
      final int _cursorIndexOfBikeId = CursorUtil.getColumnIndexOrThrow(_cursor, "bikeId");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
      final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
      final int _cursorIndexOfUpdatedOn = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedOn");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfPotentialLastLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "potentialLastLatitude");
      final int _cursorIndexOfPotentialLastLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "potentialLastLongitude");
      final int _cursorIndexOfPotentialEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "potentialEndTime");
      final TripEntity[] _result = new TripEntity[_cursor.getCount()];
      int _index = 0;
      while(_cursor.moveToNext()) {
        final TripEntity _item;
        final String _tmpTripId;
        _tmpTripId = _cursor.getString(_cursorIndexOfTripId);
        final String _tmpStartAddress;
        _tmpStartAddress = _cursor.getString(_cursorIndexOfStartAddress);
        final String _tmpEndAddress;
        _tmpEndAddress = _cursor.getString(_cursorIndexOfEndAddress);
        final String _tmpChassisNumber;
        _tmpChassisNumber = _cursor.getString(_cursorIndexOfChassisNumber);
        final Long _tmpCreatedOn;
        if (_cursor.isNull(_cursorIndexOfCreatedOn)) {
          _tmpCreatedOn = null;
        } else {
          _tmpCreatedOn = _cursor.getLong(_cursorIndexOfCreatedOn);
        }
        final Long _tmpStartTime;
        if (_cursor.isNull(_cursorIndexOfStartTime)) {
          _tmpStartTime = null;
        } else {
          _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
        }
        final Long _tmpEndTime;
        if (_cursor.isNull(_cursorIndexOfEndTime)) {
          _tmpEndTime = null;
        } else {
          _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
        }
        final Float _tmpDistanceCovered;
        if (_cursor.isNull(_cursorIndexOfDistanceCovered)) {
          _tmpDistanceCovered = null;
        } else {
          _tmpDistanceCovered = _cursor.getFloat(_cursorIndexOfDistanceCovered);
        }
        final Float _tmpAverageSpeed;
        if (_cursor.isNull(_cursorIndexOfAverageSpeed)) {
          _tmpAverageSpeed = null;
        } else {
          _tmpAverageSpeed = _cursor.getFloat(_cursorIndexOfAverageSpeed);
        }
        final Integer _tmpBreakCount;
        if (_cursor.isNull(_cursorIndexOfBreakCount)) {
          _tmpBreakCount = null;
        } else {
          _tmpBreakCount = _cursor.getInt(_cursorIndexOfBreakCount);
        }
        final String _tmpImei;
        _tmpImei = _cursor.getString(_cursorIndexOfImei);
        final Float _tmpBattery;
        if (_cursor.isNull(_cursorIndexOfBattery)) {
          _tmpBattery = null;
        } else {
          _tmpBattery = _cursor.getFloat(_cursorIndexOfBattery);
        }
        final Double _tmpStartLat;
        if (_cursor.isNull(_cursorIndexOfStartLat)) {
          _tmpStartLat = null;
        } else {
          _tmpStartLat = _cursor.getDouble(_cursorIndexOfStartLat);
        }
        final Double _tmpStartLon;
        if (_cursor.isNull(_cursorIndexOfStartLon)) {
          _tmpStartLon = null;
        } else {
          _tmpStartLon = _cursor.getDouble(_cursorIndexOfStartLon);
        }
        final Double _tmpEndLat;
        if (_cursor.isNull(_cursorIndexOfEndLat)) {
          _tmpEndLat = null;
        } else {
          _tmpEndLat = _cursor.getDouble(_cursorIndexOfEndLat);
        }
        final Double _tmpEndLon;
        if (_cursor.isNull(_cursorIndexOfEndLon)) {
          _tmpEndLon = null;
        } else {
          _tmpEndLon = _cursor.getDouble(_cursorIndexOfEndLon);
        }
        final Integer _tmpBikeId;
        if (_cursor.isNull(_cursorIndexOfBikeId)) {
          _tmpBikeId = null;
        } else {
          _tmpBikeId = _cursor.getInt(_cursorIndexOfBikeId);
        }
        final boolean _tmpIsActive;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsActive);
        _tmpIsActive = _tmp != 0;
        final boolean _tmpIsSynced;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsSynced);
        _tmpIsSynced = _tmp_1 != 0;
        final Long _tmpUpdatedOn;
        if (_cursor.isNull(_cursorIndexOfUpdatedOn)) {
          _tmpUpdatedOn = null;
        } else {
          _tmpUpdatedOn = _cursor.getLong(_cursorIndexOfUpdatedOn);
        }
        final String _tmpUserId;
        _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
        final Double _tmpPotentialLastLatitude;
        if (_cursor.isNull(_cursorIndexOfPotentialLastLatitude)) {
          _tmpPotentialLastLatitude = null;
        } else {
          _tmpPotentialLastLatitude = _cursor.getDouble(_cursorIndexOfPotentialLastLatitude);
        }
        final Double _tmpPotentialLastLongitude;
        if (_cursor.isNull(_cursorIndexOfPotentialLastLongitude)) {
          _tmpPotentialLastLongitude = null;
        } else {
          _tmpPotentialLastLongitude = _cursor.getDouble(_cursorIndexOfPotentialLastLongitude);
        }
        final Long _tmpPotentialEndTime;
        if (_cursor.isNull(_cursorIndexOfPotentialEndTime)) {
          _tmpPotentialEndTime = null;
        } else {
          _tmpPotentialEndTime = _cursor.getLong(_cursorIndexOfPotentialEndTime);
        }
        _item = new TripEntity(_tmpTripId,_tmpStartAddress,_tmpEndAddress,_tmpChassisNumber,_tmpCreatedOn,_tmpStartTime,_tmpEndTime,_tmpDistanceCovered,_tmpAverageSpeed,_tmpBreakCount,_tmpImei,_tmpBattery,_tmpStartLat,_tmpStartLon,_tmpEndLat,_tmpEndLon,_tmpBikeId,_tmpIsActive,_tmpIsSynced,_tmpUpdatedOn,_tmpUserId,_tmpPotentialLastLatitude,_tmpPotentialLastLongitude,_tmpPotentialEndTime);
        _result[_index] = _item;
        _index ++;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Integer> getBikeIds() {
    final String _sql = "SELECT DISTINCT bikeId from trip_entity";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final List<Integer> _result = new ArrayList<Integer>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Integer _item;
        if (_cursor.isNull(0)) {
          _item = null;
        } else {
          _item = _cursor.getInt(0);
        }
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public String getChassisNumber(final String tripId) {
    final String _sql = "Select chassisNumber from trip_entity where tripId =?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (tripId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, tripId);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final String _result;
      if(_cursor.moveToFirst()) {
        _result = _cursor.getString(0);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public TripEntity getTripEntity(final String tripId) {
    final String _sql = "Select * from trip_entity where tripId =?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (tripId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, tripId);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfTripId = CursorUtil.getColumnIndexOrThrow(_cursor, "tripId");
      final int _cursorIndexOfStartAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "startAddress");
      final int _cursorIndexOfEndAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "endAddress");
      final int _cursorIndexOfChassisNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "chassisNumber");
      final int _cursorIndexOfCreatedOn = CursorUtil.getColumnIndexOrThrow(_cursor, "createdOn");
      final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
      final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
      final int _cursorIndexOfDistanceCovered = CursorUtil.getColumnIndexOrThrow(_cursor, "distanceCovered");
      final int _cursorIndexOfAverageSpeed = CursorUtil.getColumnIndexOrThrow(_cursor, "averageSpeed");
      final int _cursorIndexOfBreakCount = CursorUtil.getColumnIndexOrThrow(_cursor, "breakCount");
      final int _cursorIndexOfImei = CursorUtil.getColumnIndexOrThrow(_cursor, "imei");
      final int _cursorIndexOfBattery = CursorUtil.getColumnIndexOrThrow(_cursor, "lastBatteryVoltage");
      final int _cursorIndexOfStartLat = CursorUtil.getColumnIndexOrThrow(_cursor, "startLat");
      final int _cursorIndexOfStartLon = CursorUtil.getColumnIndexOrThrow(_cursor, "startLon");
      final int _cursorIndexOfEndLat = CursorUtil.getColumnIndexOrThrow(_cursor, "endLat");
      final int _cursorIndexOfEndLon = CursorUtil.getColumnIndexOrThrow(_cursor, "endLon");
      final int _cursorIndexOfBikeId = CursorUtil.getColumnIndexOrThrow(_cursor, "bikeId");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
      final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
      final int _cursorIndexOfUpdatedOn = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedOn");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfPotentialLastLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "potentialLastLatitude");
      final int _cursorIndexOfPotentialLastLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "potentialLastLongitude");
      final int _cursorIndexOfPotentialEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "potentialEndTime");
      final TripEntity _result;
      if(_cursor.moveToFirst()) {
        final String _tmpTripId;
        _tmpTripId = _cursor.getString(_cursorIndexOfTripId);
        final String _tmpStartAddress;
        _tmpStartAddress = _cursor.getString(_cursorIndexOfStartAddress);
        final String _tmpEndAddress;
        _tmpEndAddress = _cursor.getString(_cursorIndexOfEndAddress);
        final String _tmpChassisNumber;
        _tmpChassisNumber = _cursor.getString(_cursorIndexOfChassisNumber);
        final Long _tmpCreatedOn;
        if (_cursor.isNull(_cursorIndexOfCreatedOn)) {
          _tmpCreatedOn = null;
        } else {
          _tmpCreatedOn = _cursor.getLong(_cursorIndexOfCreatedOn);
        }
        final Long _tmpStartTime;
        if (_cursor.isNull(_cursorIndexOfStartTime)) {
          _tmpStartTime = null;
        } else {
          _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
        }
        final Long _tmpEndTime;
        if (_cursor.isNull(_cursorIndexOfEndTime)) {
          _tmpEndTime = null;
        } else {
          _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
        }
        final Float _tmpDistanceCovered;
        if (_cursor.isNull(_cursorIndexOfDistanceCovered)) {
          _tmpDistanceCovered = null;
        } else {
          _tmpDistanceCovered = _cursor.getFloat(_cursorIndexOfDistanceCovered);
        }
        final Float _tmpAverageSpeed;
        if (_cursor.isNull(_cursorIndexOfAverageSpeed)) {
          _tmpAverageSpeed = null;
        } else {
          _tmpAverageSpeed = _cursor.getFloat(_cursorIndexOfAverageSpeed);
        }
        final Integer _tmpBreakCount;
        if (_cursor.isNull(_cursorIndexOfBreakCount)) {
          _tmpBreakCount = null;
        } else {
          _tmpBreakCount = _cursor.getInt(_cursorIndexOfBreakCount);
        }
        final String _tmpImei;
        _tmpImei = _cursor.getString(_cursorIndexOfImei);
        final Float _tmpBattery;
        if (_cursor.isNull(_cursorIndexOfBattery)) {
          _tmpBattery = null;
        } else {
          _tmpBattery = _cursor.getFloat(_cursorIndexOfBattery);
        }
        final Double _tmpStartLat;
        if (_cursor.isNull(_cursorIndexOfStartLat)) {
          _tmpStartLat = null;
        } else {
          _tmpStartLat = _cursor.getDouble(_cursorIndexOfStartLat);
        }
        final Double _tmpStartLon;
        if (_cursor.isNull(_cursorIndexOfStartLon)) {
          _tmpStartLon = null;
        } else {
          _tmpStartLon = _cursor.getDouble(_cursorIndexOfStartLon);
        }
        final Double _tmpEndLat;
        if (_cursor.isNull(_cursorIndexOfEndLat)) {
          _tmpEndLat = null;
        } else {
          _tmpEndLat = _cursor.getDouble(_cursorIndexOfEndLat);
        }
        final Double _tmpEndLon;
        if (_cursor.isNull(_cursorIndexOfEndLon)) {
          _tmpEndLon = null;
        } else {
          _tmpEndLon = _cursor.getDouble(_cursorIndexOfEndLon);
        }
        final Integer _tmpBikeId;
        if (_cursor.isNull(_cursorIndexOfBikeId)) {
          _tmpBikeId = null;
        } else {
          _tmpBikeId = _cursor.getInt(_cursorIndexOfBikeId);
        }
        final boolean _tmpIsActive;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsActive);
        _tmpIsActive = _tmp != 0;
        final boolean _tmpIsSynced;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsSynced);
        _tmpIsSynced = _tmp_1 != 0;
        final Long _tmpUpdatedOn;
        if (_cursor.isNull(_cursorIndexOfUpdatedOn)) {
          _tmpUpdatedOn = null;
        } else {
          _tmpUpdatedOn = _cursor.getLong(_cursorIndexOfUpdatedOn);
        }
        final String _tmpUserId;
        _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
        final Double _tmpPotentialLastLatitude;
        if (_cursor.isNull(_cursorIndexOfPotentialLastLatitude)) {
          _tmpPotentialLastLatitude = null;
        } else {
          _tmpPotentialLastLatitude = _cursor.getDouble(_cursorIndexOfPotentialLastLatitude);
        }
        final Double _tmpPotentialLastLongitude;
        if (_cursor.isNull(_cursorIndexOfPotentialLastLongitude)) {
          _tmpPotentialLastLongitude = null;
        } else {
          _tmpPotentialLastLongitude = _cursor.getDouble(_cursorIndexOfPotentialLastLongitude);
        }
        final Long _tmpPotentialEndTime;
        if (_cursor.isNull(_cursorIndexOfPotentialEndTime)) {
          _tmpPotentialEndTime = null;
        } else {
          _tmpPotentialEndTime = _cursor.getLong(_cursorIndexOfPotentialEndTime);
        }
        _result = new TripEntity(_tmpTripId,_tmpStartAddress,_tmpEndAddress,_tmpChassisNumber,_tmpCreatedOn,_tmpStartTime,_tmpEndTime,_tmpDistanceCovered,_tmpAverageSpeed,_tmpBreakCount,_tmpImei,_tmpBattery,_tmpStartLat,_tmpStartLon,_tmpEndLat,_tmpEndLon,_tmpBikeId,_tmpIsActive,_tmpIsSynced,_tmpUpdatedOn,_tmpUserId,_tmpPotentialLastLatitude,_tmpPotentialLastLongitude,_tmpPotentialEndTime);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public TripEntity getPotentialLastTrip() {
    final String _sql = "Select * from trip_entity order by startTime desc limit 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfTripId = CursorUtil.getColumnIndexOrThrow(_cursor, "tripId");
      final int _cursorIndexOfStartAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "startAddress");
      final int _cursorIndexOfEndAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "endAddress");
      final int _cursorIndexOfChassisNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "chassisNumber");
      final int _cursorIndexOfCreatedOn = CursorUtil.getColumnIndexOrThrow(_cursor, "createdOn");
      final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
      final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
      final int _cursorIndexOfDistanceCovered = CursorUtil.getColumnIndexOrThrow(_cursor, "distanceCovered");
      final int _cursorIndexOfAverageSpeed = CursorUtil.getColumnIndexOrThrow(_cursor, "averageSpeed");
      final int _cursorIndexOfBreakCount = CursorUtil.getColumnIndexOrThrow(_cursor, "breakCount");
      final int _cursorIndexOfImei = CursorUtil.getColumnIndexOrThrow(_cursor, "imei");
      final int _cursorIndexOfBattery = CursorUtil.getColumnIndexOrThrow(_cursor, "lastBatteryVoltage");
      final int _cursorIndexOfStartLat = CursorUtil.getColumnIndexOrThrow(_cursor, "startLat");
      final int _cursorIndexOfStartLon = CursorUtil.getColumnIndexOrThrow(_cursor, "startLon");
      final int _cursorIndexOfEndLat = CursorUtil.getColumnIndexOrThrow(_cursor, "endLat");
      final int _cursorIndexOfEndLon = CursorUtil.getColumnIndexOrThrow(_cursor, "endLon");
      final int _cursorIndexOfBikeId = CursorUtil.getColumnIndexOrThrow(_cursor, "bikeId");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
      final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
      final int _cursorIndexOfUpdatedOn = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedOn");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfPotentialLastLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "potentialLastLatitude");
      final int _cursorIndexOfPotentialLastLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "potentialLastLongitude");
      final int _cursorIndexOfPotentialEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "potentialEndTime");
      final TripEntity _result;
      if(_cursor.moveToFirst()) {
        final String _tmpTripId;
        _tmpTripId = _cursor.getString(_cursorIndexOfTripId);
        final String _tmpStartAddress;
        _tmpStartAddress = _cursor.getString(_cursorIndexOfStartAddress);
        final String _tmpEndAddress;
        _tmpEndAddress = _cursor.getString(_cursorIndexOfEndAddress);
        final String _tmpChassisNumber;
        _tmpChassisNumber = _cursor.getString(_cursorIndexOfChassisNumber);
        final Long _tmpCreatedOn;
        if (_cursor.isNull(_cursorIndexOfCreatedOn)) {
          _tmpCreatedOn = null;
        } else {
          _tmpCreatedOn = _cursor.getLong(_cursorIndexOfCreatedOn);
        }
        final Long _tmpStartTime;
        if (_cursor.isNull(_cursorIndexOfStartTime)) {
          _tmpStartTime = null;
        } else {
          _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
        }
        final Long _tmpEndTime;
        if (_cursor.isNull(_cursorIndexOfEndTime)) {
          _tmpEndTime = null;
        } else {
          _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
        }
        final Float _tmpDistanceCovered;
        if (_cursor.isNull(_cursorIndexOfDistanceCovered)) {
          _tmpDistanceCovered = null;
        } else {
          _tmpDistanceCovered = _cursor.getFloat(_cursorIndexOfDistanceCovered);
        }
        final Float _tmpAverageSpeed;
        if (_cursor.isNull(_cursorIndexOfAverageSpeed)) {
          _tmpAverageSpeed = null;
        } else {
          _tmpAverageSpeed = _cursor.getFloat(_cursorIndexOfAverageSpeed);
        }
        final Integer _tmpBreakCount;
        if (_cursor.isNull(_cursorIndexOfBreakCount)) {
          _tmpBreakCount = null;
        } else {
          _tmpBreakCount = _cursor.getInt(_cursorIndexOfBreakCount);
        }
        final String _tmpImei;
        _tmpImei = _cursor.getString(_cursorIndexOfImei);
        final Float _tmpBattery;
        if (_cursor.isNull(_cursorIndexOfBattery)) {
          _tmpBattery = null;
        } else {
          _tmpBattery = _cursor.getFloat(_cursorIndexOfBattery);
        }
        final Double _tmpStartLat;
        if (_cursor.isNull(_cursorIndexOfStartLat)) {
          _tmpStartLat = null;
        } else {
          _tmpStartLat = _cursor.getDouble(_cursorIndexOfStartLat);
        }
        final Double _tmpStartLon;
        if (_cursor.isNull(_cursorIndexOfStartLon)) {
          _tmpStartLon = null;
        } else {
          _tmpStartLon = _cursor.getDouble(_cursorIndexOfStartLon);
        }
        final Double _tmpEndLat;
        if (_cursor.isNull(_cursorIndexOfEndLat)) {
          _tmpEndLat = null;
        } else {
          _tmpEndLat = _cursor.getDouble(_cursorIndexOfEndLat);
        }
        final Double _tmpEndLon;
        if (_cursor.isNull(_cursorIndexOfEndLon)) {
          _tmpEndLon = null;
        } else {
          _tmpEndLon = _cursor.getDouble(_cursorIndexOfEndLon);
        }
        final Integer _tmpBikeId;
        if (_cursor.isNull(_cursorIndexOfBikeId)) {
          _tmpBikeId = null;
        } else {
          _tmpBikeId = _cursor.getInt(_cursorIndexOfBikeId);
        }
        final boolean _tmpIsActive;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsActive);
        _tmpIsActive = _tmp != 0;
        final boolean _tmpIsSynced;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsSynced);
        _tmpIsSynced = _tmp_1 != 0;
        final Long _tmpUpdatedOn;
        if (_cursor.isNull(_cursorIndexOfUpdatedOn)) {
          _tmpUpdatedOn = null;
        } else {
          _tmpUpdatedOn = _cursor.getLong(_cursorIndexOfUpdatedOn);
        }
        final String _tmpUserId;
        _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
        final Double _tmpPotentialLastLatitude;
        if (_cursor.isNull(_cursorIndexOfPotentialLastLatitude)) {
          _tmpPotentialLastLatitude = null;
        } else {
          _tmpPotentialLastLatitude = _cursor.getDouble(_cursorIndexOfPotentialLastLatitude);
        }
        final Double _tmpPotentialLastLongitude;
        if (_cursor.isNull(_cursorIndexOfPotentialLastLongitude)) {
          _tmpPotentialLastLongitude = null;
        } else {
          _tmpPotentialLastLongitude = _cursor.getDouble(_cursorIndexOfPotentialLastLongitude);
        }
        final Long _tmpPotentialEndTime;
        if (_cursor.isNull(_cursorIndexOfPotentialEndTime)) {
          _tmpPotentialEndTime = null;
        } else {
          _tmpPotentialEndTime = _cursor.getLong(_cursorIndexOfPotentialEndTime);
        }
        _result = new TripEntity(_tmpTripId,_tmpStartAddress,_tmpEndAddress,_tmpChassisNumber,_tmpCreatedOn,_tmpStartTime,_tmpEndTime,_tmpDistanceCovered,_tmpAverageSpeed,_tmpBreakCount,_tmpImei,_tmpBattery,_tmpStartLat,_tmpStartLon,_tmpEndLat,_tmpEndLon,_tmpBikeId,_tmpIsActive,_tmpIsSynced,_tmpUpdatedOn,_tmpUserId,_tmpPotentialLastLatitude,_tmpPotentialLastLongitude,_tmpPotentialEndTime);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
