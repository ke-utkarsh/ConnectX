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
import ymsli.com.ea1h.database.entity.AccelerometerEntity;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AccelerometerDao_Impl implements AccelerometerDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfAccelerometerEntity;

  private final SharedSQLiteStatement __preparedStmtOfRemoveSyncedAccelData;

  private final SharedSQLiteStatement __preparedStmtOfClearAccelTable;

  public AccelerometerDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAccelerometerEntity = new EntityInsertionAdapter<AccelerometerEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `accel_entity`(`id`,`tripId`,`xCoordinate`,`yCoordinate`,`zCoordinate`,`sensorTime`,`isFileCreated`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, AccelerometerEntity value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindLong(1, value.getId());
        }
        if (value.getTripId() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getTripId());
        }
        if (value.getX() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getX());
        }
        if (value.getY() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getY());
        }
        if (value.getZ() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getZ());
        }
        if (value.getSensorTime() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getSensorTime());
        }
        final int _tmp;
        _tmp = value.isFileCreated() ? 1 : 0;
        stmt.bindLong(7, _tmp);
      }
    };
    this.__preparedStmtOfRemoveSyncedAccelData = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from accel_entity where id =?";
        return _query;
      }
    };
    this.__preparedStmtOfClearAccelTable = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from accel_entity";
        return _query;
      }
    };
  }

  @Override
  public void insertNewAccel(final AccelerometerEntity accelerometerEntity) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfAccelerometerEntity.insert(accelerometerEntity);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void removeSyncedAccelData(final long id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveSyncedAccelData.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, id);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfRemoveSyncedAccelData.release(_stmt);
    }
  }

  @Override
  public void clearAccelTable() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfClearAccelTable.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfClearAccelTable.release(_stmt);
    }
  }

  @Override
  public AccelerometerEntity[] getUnsyncedAccelEntity() {
    final String _sql = "Select * from accel_entity where isFileCreated = 0 limit 60";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTripId = CursorUtil.getColumnIndexOrThrow(_cursor, "tripId");
      final int _cursorIndexOfX = CursorUtil.getColumnIndexOrThrow(_cursor, "xCoordinate");
      final int _cursorIndexOfY = CursorUtil.getColumnIndexOrThrow(_cursor, "yCoordinate");
      final int _cursorIndexOfZ = CursorUtil.getColumnIndexOrThrow(_cursor, "zCoordinate");
      final int _cursorIndexOfSensorTime = CursorUtil.getColumnIndexOrThrow(_cursor, "sensorTime");
      final int _cursorIndexOfIsFileCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "isFileCreated");
      final AccelerometerEntity[] _result = new AccelerometerEntity[_cursor.getCount()];
      int _index = 0;
      while(_cursor.moveToNext()) {
        final AccelerometerEntity _item;
        final Long _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getLong(_cursorIndexOfId);
        }
        final String _tmpTripId;
        _tmpTripId = _cursor.getString(_cursorIndexOfTripId);
        final String _tmpX;
        _tmpX = _cursor.getString(_cursorIndexOfX);
        final String _tmpY;
        _tmpY = _cursor.getString(_cursorIndexOfY);
        final String _tmpZ;
        _tmpZ = _cursor.getString(_cursorIndexOfZ);
        final String _tmpSensorTime;
        _tmpSensorTime = _cursor.getString(_cursorIndexOfSensorTime);
        final boolean _tmpIsFileCreated;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsFileCreated);
        _tmpIsFileCreated = _tmp != 0;
        _item = new AccelerometerEntity(_tmpId,_tmpTripId,_tmpX,_tmpY,_tmpZ,_tmpSensorTime,_tmpIsFileCreated);
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
