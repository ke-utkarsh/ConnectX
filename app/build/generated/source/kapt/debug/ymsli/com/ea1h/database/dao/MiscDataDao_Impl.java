package ymsli.com.ea1h.database.dao;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import io.reactivex.Maybe;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import ymsli.com.ea1h.database.entity.MiscDataEntity;

@SuppressWarnings({"unchecked", "deprecation"})
public final class MiscDataDao_Impl implements MiscDataDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfMiscDataEntity;

  public MiscDataDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMiscDataEntity = new EntityInsertionAdapter<MiscDataEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `misc_data`(`discriptionId`,`descriptionKey`,`descriptionValue`,`updatedOn`) VALUES (?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, MiscDataEntity value) {
        stmt.bindLong(1, value.getDiscriptionId());
        if (value.getDescriptionKey() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getDescriptionKey());
        }
        if (value.getDescriptionValue() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getDescriptionValue());
        }
        if (value.getUpdatedOn() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getUpdatedOn());
        }
      }
    };
  }

  @Override
  public void insertMiscData(final MiscDataEntity... miscDataEntity) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMiscDataEntity.insert(miscDataEntity);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Maybe<MiscDataEntity[]> getMiscData() {
    final String _sql = "Select * FROM misc_data";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return Maybe.fromCallable(new Callable<MiscDataEntity[]>() {
      @Override
      public MiscDataEntity[] call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false);
        try {
          final int _cursorIndexOfDiscriptionId = CursorUtil.getColumnIndexOrThrow(_cursor, "discriptionId");
          final int _cursorIndexOfDescriptionKey = CursorUtil.getColumnIndexOrThrow(_cursor, "descriptionKey");
          final int _cursorIndexOfDescriptionValue = CursorUtil.getColumnIndexOrThrow(_cursor, "descriptionValue");
          final int _cursorIndexOfUpdatedOn = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedOn");
          final MiscDataEntity[] _result = new MiscDataEntity[_cursor.getCount()];
          int _index = 0;
          while(_cursor.moveToNext()) {
            final MiscDataEntity _item;
            final int _tmpDiscriptionId;
            _tmpDiscriptionId = _cursor.getInt(_cursorIndexOfDiscriptionId);
            final String _tmpDescriptionKey;
            _tmpDescriptionKey = _cursor.getString(_cursorIndexOfDescriptionKey);
            final String _tmpDescriptionValue;
            _tmpDescriptionValue = _cursor.getString(_cursorIndexOfDescriptionValue);
            final String _tmpUpdatedOn;
            _tmpUpdatedOn = _cursor.getString(_cursorIndexOfUpdatedOn);
            _item = new MiscDataEntity(_tmpDiscriptionId,_tmpDescriptionKey,_tmpDescriptionValue,_tmpUpdatedOn);
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
  public LiveData<String> getContent(final int descriptionId) {
    final String _sql = "Select descriptionValue from misc_data where discriptionId=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, descriptionId);
    return __db.getInvalidationTracker().createLiveData(new String[]{"misc_data"}, false, new Callable<String>() {
      @Override
      public String call() throws Exception {
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
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public List<MiscDataEntity> getContentByKey(final String key) {
    final String _sql = "Select * from misc_data where descriptionKey =?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (key == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, key);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfDiscriptionId = CursorUtil.getColumnIndexOrThrow(_cursor, "discriptionId");
      final int _cursorIndexOfDescriptionKey = CursorUtil.getColumnIndexOrThrow(_cursor, "descriptionKey");
      final int _cursorIndexOfDescriptionValue = CursorUtil.getColumnIndexOrThrow(_cursor, "descriptionValue");
      final int _cursorIndexOfUpdatedOn = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedOn");
      final List<MiscDataEntity> _result = new ArrayList<MiscDataEntity>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final MiscDataEntity _item;
        final int _tmpDiscriptionId;
        _tmpDiscriptionId = _cursor.getInt(_cursorIndexOfDiscriptionId);
        final String _tmpDescriptionKey;
        _tmpDescriptionKey = _cursor.getString(_cursorIndexOfDescriptionKey);
        final String _tmpDescriptionValue;
        _tmpDescriptionValue = _cursor.getString(_cursorIndexOfDescriptionValue);
        final String _tmpUpdatedOn;
        _tmpUpdatedOn = _cursor.getString(_cursorIndexOfUpdatedOn);
        _item = new MiscDataEntity(_tmpDiscriptionId,_tmpDescriptionKey,_tmpDescriptionValue,_tmpUpdatedOn);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<List<MiscDataEntity>> getContentByKeyLiveData(final String key) {
    final String _sql = "Select * from misc_data where descriptionKey =?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (key == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, key);
    }
    return __db.getInvalidationTracker().createLiveData(new String[]{"misc_data"}, false, new Callable<List<MiscDataEntity>>() {
      @Override
      public List<MiscDataEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false);
        try {
          final int _cursorIndexOfDiscriptionId = CursorUtil.getColumnIndexOrThrow(_cursor, "discriptionId");
          final int _cursorIndexOfDescriptionKey = CursorUtil.getColumnIndexOrThrow(_cursor, "descriptionKey");
          final int _cursorIndexOfDescriptionValue = CursorUtil.getColumnIndexOrThrow(_cursor, "descriptionValue");
          final int _cursorIndexOfUpdatedOn = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedOn");
          final List<MiscDataEntity> _result = new ArrayList<MiscDataEntity>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final MiscDataEntity _item;
            final int _tmpDiscriptionId;
            _tmpDiscriptionId = _cursor.getInt(_cursorIndexOfDiscriptionId);
            final String _tmpDescriptionKey;
            _tmpDescriptionKey = _cursor.getString(_cursorIndexOfDescriptionKey);
            final String _tmpDescriptionValue;
            _tmpDescriptionValue = _cursor.getString(_cursorIndexOfDescriptionValue);
            final String _tmpUpdatedOn;
            _tmpUpdatedOn = _cursor.getString(_cursorIndexOfUpdatedOn);
            _item = new MiscDataEntity(_tmpDiscriptionId,_tmpDescriptionKey,_tmpDescriptionValue,_tmpUpdatedOn);
            _result.add(_item);
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
  public String getContentValue(final int descriptionId) {
    final String _sql = "Select descriptionValue from misc_data where discriptionId =?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, descriptionId);
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
}
