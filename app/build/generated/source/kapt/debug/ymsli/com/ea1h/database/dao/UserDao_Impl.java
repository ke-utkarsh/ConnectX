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
import ymsli.com.ea1h.database.entity.UserEntity;

@SuppressWarnings({"unchecked", "deprecation"})
public final class UserDao_Impl implements UserDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfUserEntity;

  private final SharedSQLiteStatement __preparedStmtOfRemoveUserEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateReLoginFailure;

  public UserDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUserEntity = new EntityInsertionAdapter<UserEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `user_entity`(`token`,`userId`,`name`,`reLoginFailure`) VALUES (?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, UserEntity value) {
        if (value.getToken() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getToken());
        }
        if (value.getUserId() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getUserId());
        }
        if (value.getName() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getName());
        }
        final int _tmp;
        _tmp = value.getReLoginFailure() ? 1 : 0;
        stmt.bindLong(4, _tmp);
      }
    };
    this.__preparedStmtOfRemoveUserEntity = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete FROM user_entity";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateReLoginFailure = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE user_entity set reLoginFailure = ?";
        return _query;
      }
    };
  }

  @Override
  public void insert(final UserEntity registrationResponse) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfUserEntity.insert(registrationResponse);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void removeUserEntity() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveUserEntity.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfRemoveUserEntity.release(_stmt);
    }
  }

  @Override
  public void updateReLoginFailure(final boolean status) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateReLoginFailure.acquire();
    int _argIndex = 1;
    final int _tmp;
    _tmp = status ? 1 : 0;
    _stmt.bindLong(_argIndex, _tmp);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateReLoginFailure.release(_stmt);
    }
  }

  @Override
  public LiveData<UserEntity[]> getUserEntity() {
    final String _sql = "Select * FROM user_entity";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"user_entity"}, false, new Callable<UserEntity[]>() {
      @Override
      public UserEntity[] call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false);
        try {
          final int _cursorIndexOfToken = CursorUtil.getColumnIndexOrThrow(_cursor, "token");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfReLoginFailure = CursorUtil.getColumnIndexOrThrow(_cursor, "reLoginFailure");
          final UserEntity[] _result = new UserEntity[_cursor.getCount()];
          int _index = 0;
          while(_cursor.moveToNext()) {
            final UserEntity _item;
            final String _tmpToken;
            _tmpToken = _cursor.getString(_cursorIndexOfToken);
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final boolean _tmpReLoginFailure;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfReLoginFailure);
            _tmpReLoginFailure = _tmp != 0;
            _item = new UserEntity(_tmpToken,_tmpUserId,_tmpName,_tmpReLoginFailure);
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
