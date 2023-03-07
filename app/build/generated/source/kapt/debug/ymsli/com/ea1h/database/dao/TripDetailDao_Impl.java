package ymsli.com.ea1h.database.dao;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import ymsli.com.ea1h.database.entity.TripDetailEntity;

@SuppressWarnings({"unchecked", "deprecation"})
public final class TripDetailDao_Impl implements TripDetailDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfTripDetailEntity;

  public TripDetailDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTripDetailEntity = new EntityInsertionAdapter<TripDetailEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `trip_detail`(`tripId`,`lats`,`longs`) VALUES (?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TripDetailEntity value) {
        if (value.getTripId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getTripId());
        }
        if (value.getLats() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getLats());
        }
        if (value.getLongs() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getLongs());
        }
      }
    };
  }

  @Override
  public void insertDetail(final TripDetailEntity... trip) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTripDetailEntity.insert(trip);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public LiveData<TripDetailEntity> getTripDetailLive(final long tripId) {
    final String _sql = "Select * from trip_detail where tripId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, tripId);
    return __db.getInvalidationTracker().createLiveData(new String[]{"trip_detail"}, false, new Callable<TripDetailEntity>() {
      @Override
      public TripDetailEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false);
        try {
          final int _cursorIndexOfTripId = CursorUtil.getColumnIndexOrThrow(_cursor, "tripId");
          final int _cursorIndexOfLats = CursorUtil.getColumnIndexOrThrow(_cursor, "lats");
          final int _cursorIndexOfLongs = CursorUtil.getColumnIndexOrThrow(_cursor, "longs");
          final TripDetailEntity _result;
          if(_cursor.moveToFirst()) {
            final String _tmpTripId;
            _tmpTripId = _cursor.getString(_cursorIndexOfTripId);
            final String _tmpLats;
            _tmpLats = _cursor.getString(_cursorIndexOfLats);
            final String _tmpLongs;
            _tmpLongs = _cursor.getString(_cursorIndexOfLongs);
            _result = new TripDetailEntity(_tmpTripId,_tmpLats,_tmpLongs);
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
  public List<TripDetailEntity> getTripDetail(final long tripId) {
    final String _sql = "Select * from trip_detail where tripId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, tripId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfTripId = CursorUtil.getColumnIndexOrThrow(_cursor, "tripId");
      final int _cursorIndexOfLats = CursorUtil.getColumnIndexOrThrow(_cursor, "lats");
      final int _cursorIndexOfLongs = CursorUtil.getColumnIndexOrThrow(_cursor, "longs");
      final List<TripDetailEntity> _result = new ArrayList<TripDetailEntity>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final TripDetailEntity _item;
        final String _tmpTripId;
        _tmpTripId = _cursor.getString(_cursorIndexOfTripId);
        final String _tmpLats;
        _tmpLats = _cursor.getString(_cursorIndexOfLats);
        final String _tmpLongs;
        _tmpLongs = _cursor.getString(_cursorIndexOfLongs);
        _item = new TripDetailEntity(_tmpTripId,_tmpLats,_tmpLongs);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
