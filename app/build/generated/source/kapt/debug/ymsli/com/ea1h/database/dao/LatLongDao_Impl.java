package ymsli.com.ea1h.database.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import ymsli.com.ea1h.database.entity.LatLongEntity;

@SuppressWarnings({"unchecked", "deprecation"})
public final class LatLongDao_Impl implements LatLongDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfLatLongEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateTripParameter;

  private final SharedSQLiteStatement __preparedStmtOfDeleteSyncedLatLongs;

  public LatLongDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfLatLongEntity = new EntityInsertionAdapter<LatLongEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `lat_long_entity`(`locationId`,`tripId`,`latitude`,`longitude`,`locationCaptureTime`,`isFileCreated`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, LatLongEntity value) {
        if (value.getLocationId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindLong(1, value.getLocationId());
        }
        if (value.getTripId() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getTripId());
        }
        if (value.getLatitude() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getLatitude());
        }
        if (value.getLongitude() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getLongitude());
        }
        if (value.getLocationCaptureTime() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getLocationCaptureTime());
        }
        final int _tmp;
        _tmp = value.isFileCreated() ? 1 : 0;
        stmt.bindLong(6, _tmp);
      }
    };
    this.__preparedStmtOfUpdateTripParameter = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Update lat_long_entity Set isFileCreated = 1 where locationId =?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteSyncedLatLongs = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM lat_long_entity where isFileCreated = 1";
        return _query;
      }
    };
  }

  @Override
  public void insertNewLocation(final LatLongEntity... latLong) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfLatLongEntity.insert(latLong);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateTripParameter(final long locationId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateTripParameter.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, locationId);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateTripParameter.release(_stmt);
    }
  }

  @Override
  public void deleteSyncedLatLongs() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteSyncedLatLongs.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteSyncedLatLongs.release(_stmt);
    }
  }

  @Override
  public LatLongEntity getTripLastLocation(final String tripId) {
    final String _sql = "Select *,max(locationId) from lat_long_entity where tripId =?";
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
      final int _cursorIndexOfLocationId = CursorUtil.getColumnIndexOrThrow(_cursor, "locationId");
      final int _cursorIndexOfTripId = CursorUtil.getColumnIndexOrThrow(_cursor, "tripId");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfLocationCaptureTime = CursorUtil.getColumnIndexOrThrow(_cursor, "locationCaptureTime");
      final int _cursorIndexOfIsFileCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "isFileCreated");
      final LatLongEntity _result;
      if(_cursor.moveToFirst()) {
        final Long _tmpLocationId;
        if (_cursor.isNull(_cursorIndexOfLocationId)) {
          _tmpLocationId = null;
        } else {
          _tmpLocationId = _cursor.getLong(_cursorIndexOfLocationId);
        }
        final String _tmpTripId;
        _tmpTripId = _cursor.getString(_cursorIndexOfTripId);
        final String _tmpLatitude;
        _tmpLatitude = _cursor.getString(_cursorIndexOfLatitude);
        final String _tmpLongitude;
        _tmpLongitude = _cursor.getString(_cursorIndexOfLongitude);
        final String _tmpLocationCaptureTime;
        _tmpLocationCaptureTime = _cursor.getString(_cursorIndexOfLocationCaptureTime);
        final boolean _tmpIsFileCreated;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsFileCreated);
        _tmpIsFileCreated = _tmp != 0;
        _result = new LatLongEntity(_tmpLocationId,_tmpTripId,_tmpLatitude,_tmpLongitude,_tmpLocationCaptureTime,_tmpIsFileCreated);
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
  public LatLongEntity getTripFirstLocation(final String tripId) {
    final String _sql = "Select *,min(locationId) from lat_long_entity where tripId =?";
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
      final int _cursorIndexOfLocationId = CursorUtil.getColumnIndexOrThrow(_cursor, "locationId");
      final int _cursorIndexOfTripId = CursorUtil.getColumnIndexOrThrow(_cursor, "tripId");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfLocationCaptureTime = CursorUtil.getColumnIndexOrThrow(_cursor, "locationCaptureTime");
      final int _cursorIndexOfIsFileCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "isFileCreated");
      final LatLongEntity _result;
      if(_cursor.moveToFirst()) {
        final Long _tmpLocationId;
        if (_cursor.isNull(_cursorIndexOfLocationId)) {
          _tmpLocationId = null;
        } else {
          _tmpLocationId = _cursor.getLong(_cursorIndexOfLocationId);
        }
        final String _tmpTripId;
        _tmpTripId = _cursor.getString(_cursorIndexOfTripId);
        final String _tmpLatitude;
        _tmpLatitude = _cursor.getString(_cursorIndexOfLatitude);
        final String _tmpLongitude;
        _tmpLongitude = _cursor.getString(_cursorIndexOfLongitude);
        final String _tmpLocationCaptureTime;
        _tmpLocationCaptureTime = _cursor.getString(_cursorIndexOfLocationCaptureTime);
        final boolean _tmpIsFileCreated;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsFileCreated);
        _tmpIsFileCreated = _tmp != 0;
        _result = new LatLongEntity(_tmpLocationId,_tmpTripId,_tmpLatitude,_tmpLongitude,_tmpLocationCaptureTime,_tmpIsFileCreated);
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
  public LatLongEntity[] getUnsyncedTrips() {
    final String _sql = "Select * from lat_long_entity where isFileCreated = 0 limit 40";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfLocationId = CursorUtil.getColumnIndexOrThrow(_cursor, "locationId");
      final int _cursorIndexOfTripId = CursorUtil.getColumnIndexOrThrow(_cursor, "tripId");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfLocationCaptureTime = CursorUtil.getColumnIndexOrThrow(_cursor, "locationCaptureTime");
      final int _cursorIndexOfIsFileCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "isFileCreated");
      final LatLongEntity[] _result = new LatLongEntity[_cursor.getCount()];
      int _index = 0;
      while(_cursor.moveToNext()) {
        final LatLongEntity _item;
        final Long _tmpLocationId;
        if (_cursor.isNull(_cursorIndexOfLocationId)) {
          _tmpLocationId = null;
        } else {
          _tmpLocationId = _cursor.getLong(_cursorIndexOfLocationId);
        }
        final String _tmpTripId;
        _tmpTripId = _cursor.getString(_cursorIndexOfTripId);
        final String _tmpLatitude;
        _tmpLatitude = _cursor.getString(_cursorIndexOfLatitude);
        final String _tmpLongitude;
        _tmpLongitude = _cursor.getString(_cursorIndexOfLongitude);
        final String _tmpLocationCaptureTime;
        _tmpLocationCaptureTime = _cursor.getString(_cursorIndexOfLocationCaptureTime);
        final boolean _tmpIsFileCreated;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsFileCreated);
        _tmpIsFileCreated = _tmp != 0;
        _item = new LatLongEntity(_tmpLocationId,_tmpTripId,_tmpLatitude,_tmpLongitude,_tmpLocationCaptureTime,_tmpIsFileCreated);
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
  public LatLongEntity[] getAllValues() {
    final String _sql = "Select * from lat_long_entity";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfLocationId = CursorUtil.getColumnIndexOrThrow(_cursor, "locationId");
      final int _cursorIndexOfTripId = CursorUtil.getColumnIndexOrThrow(_cursor, "tripId");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfLocationCaptureTime = CursorUtil.getColumnIndexOrThrow(_cursor, "locationCaptureTime");
      final int _cursorIndexOfIsFileCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "isFileCreated");
      final LatLongEntity[] _result = new LatLongEntity[_cursor.getCount()];
      int _index = 0;
      while(_cursor.moveToNext()) {
        final LatLongEntity _item;
        final Long _tmpLocationId;
        if (_cursor.isNull(_cursorIndexOfLocationId)) {
          _tmpLocationId = null;
        } else {
          _tmpLocationId = _cursor.getLong(_cursorIndexOfLocationId);
        }
        final String _tmpTripId;
        _tmpTripId = _cursor.getString(_cursorIndexOfTripId);
        final String _tmpLatitude;
        _tmpLatitude = _cursor.getString(_cursorIndexOfLatitude);
        final String _tmpLongitude;
        _tmpLongitude = _cursor.getString(_cursorIndexOfLongitude);
        final String _tmpLocationCaptureTime;
        _tmpLocationCaptureTime = _cursor.getString(_cursorIndexOfLocationCaptureTime);
        final boolean _tmpIsFileCreated;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsFileCreated);
        _tmpIsFileCreated = _tmp != 0;
        _item = new LatLongEntity(_tmpLocationId,_tmpTripId,_tmpLatitude,_tmpLongitude,_tmpLocationCaptureTime,_tmpIsFileCreated);
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
  public LatLongEntity[] getLocationOfTrip(final String tripId) {
    final String _sql = "Select * from lat_long_entity where tripId =? order by tripId";
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
      final int _cursorIndexOfLocationId = CursorUtil.getColumnIndexOrThrow(_cursor, "locationId");
      final int _cursorIndexOfTripId = CursorUtil.getColumnIndexOrThrow(_cursor, "tripId");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfLocationCaptureTime = CursorUtil.getColumnIndexOrThrow(_cursor, "locationCaptureTime");
      final int _cursorIndexOfIsFileCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "isFileCreated");
      final LatLongEntity[] _result = new LatLongEntity[_cursor.getCount()];
      int _index = 0;
      while(_cursor.moveToNext()) {
        final LatLongEntity _item;
        final Long _tmpLocationId;
        if (_cursor.isNull(_cursorIndexOfLocationId)) {
          _tmpLocationId = null;
        } else {
          _tmpLocationId = _cursor.getLong(_cursorIndexOfLocationId);
        }
        final String _tmpTripId;
        _tmpTripId = _cursor.getString(_cursorIndexOfTripId);
        final String _tmpLatitude;
        _tmpLatitude = _cursor.getString(_cursorIndexOfLatitude);
        final String _tmpLongitude;
        _tmpLongitude = _cursor.getString(_cursorIndexOfLongitude);
        final String _tmpLocationCaptureTime;
        _tmpLocationCaptureTime = _cursor.getString(_cursorIndexOfLocationCaptureTime);
        final boolean _tmpIsFileCreated;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsFileCreated);
        _tmpIsFileCreated = _tmp != 0;
        _item = new LatLongEntity(_tmpLocationId,_tmpTripId,_tmpLatitude,_tmpLongitude,_tmpLocationCaptureTime,_tmpIsFileCreated);
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
  public LatLongEntity[] getLocationForMapPlot(final String tripId) {
    final String _sql = "Select * from lat_long_entity where tripId =? order by locationId";
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
      final int _cursorIndexOfLocationId = CursorUtil.getColumnIndexOrThrow(_cursor, "locationId");
      final int _cursorIndexOfTripId = CursorUtil.getColumnIndexOrThrow(_cursor, "tripId");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfLocationCaptureTime = CursorUtil.getColumnIndexOrThrow(_cursor, "locationCaptureTime");
      final int _cursorIndexOfIsFileCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "isFileCreated");
      final LatLongEntity[] _result = new LatLongEntity[_cursor.getCount()];
      int _index = 0;
      while(_cursor.moveToNext()) {
        final LatLongEntity _item;
        final Long _tmpLocationId;
        if (_cursor.isNull(_cursorIndexOfLocationId)) {
          _tmpLocationId = null;
        } else {
          _tmpLocationId = _cursor.getLong(_cursorIndexOfLocationId);
        }
        final String _tmpTripId;
        _tmpTripId = _cursor.getString(_cursorIndexOfTripId);
        final String _tmpLatitude;
        _tmpLatitude = _cursor.getString(_cursorIndexOfLatitude);
        final String _tmpLongitude;
        _tmpLongitude = _cursor.getString(_cursorIndexOfLongitude);
        final String _tmpLocationCaptureTime;
        _tmpLocationCaptureTime = _cursor.getString(_cursorIndexOfLocationCaptureTime);
        final boolean _tmpIsFileCreated;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsFileCreated);
        _tmpIsFileCreated = _tmp != 0;
        _item = new LatLongEntity(_tmpLocationId,_tmpTripId,_tmpLatitude,_tmpLongitude,_tmpLocationCaptureTime,_tmpIsFileCreated);
        _result[_index] = _item;
        _index ++;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
