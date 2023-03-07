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
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.concurrent.Callable;
import ymsli.com.ea1h.database.entity.FilterBikeEntity;

@SuppressWarnings({"unchecked", "deprecation"})
public final class FilterBikeDao_Impl implements FilterBikeDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfFilterBikeEntity;

  private final SharedSQLiteStatement __preparedStmtOfClearSelectedBikeFromFilter;

  public FilterBikeDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFilterBikeEntity = new EntityInsertionAdapter<FilterBikeEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `filter_bike`(`filterID`,`bike_selected`) VALUES (?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, FilterBikeEntity value) {
        if (value.getFilterID() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindLong(1, value.getFilterID());
        }
        if (value.getBikeSelected() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getBikeSelected());
        }
      }
    };
    this.__preparedStmtOfClearSelectedBikeFromFilter = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete FROM filter_bike";
        return _query;
      }
    };
  }

  @Override
  public void insert(final FilterBikeEntity filterBikeEntity) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfFilterBikeEntity.insert(filterBikeEntity);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void clearSelectedBikeFromFilter() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfClearSelectedBikeFromFilter.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfClearSelectedBikeFromFilter.release(_stmt);
    }
  }

  @Override
  public LiveData<FilterBikeEntity[]> getBikeInFilter() {
    final String _sql = "Select bike_selected FROM filter_bike";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"filter_bike"}, false, new Callable<FilterBikeEntity[]>() {
      @Override
      public FilterBikeEntity[] call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false);
        try {
          final int _cursorIndexOfBikeSelected = CursorUtil.getColumnIndexOrThrow(_cursor, "bike_selected");
          final FilterBikeEntity[] _result = new FilterBikeEntity[_cursor.getCount()];
          int _index = 0;
          while(_cursor.moveToNext()) {
            final FilterBikeEntity _item;
            final String _tmpBikeSelected;
            _tmpBikeSelected = _cursor.getString(_cursorIndexOfBikeSelected);
            _item = new FilterBikeEntity(null,_tmpBikeSelected);
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
}
