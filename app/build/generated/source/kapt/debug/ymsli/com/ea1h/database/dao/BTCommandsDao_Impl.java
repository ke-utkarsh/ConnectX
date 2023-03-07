package ymsli.com.ea1h.database.dao;

import android.database.Cursor;
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
import java.util.concurrent.Callable;
import ymsli.com.ea1h.database.entity.BTCommandsEntity;

@SuppressWarnings({"unchecked", "deprecation"})
public final class BTCommandsDao_Impl implements BTCommandsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfBTCommandsEntity;

  public BTCommandsDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBTCommandsEntity = new EntityInsertionAdapter<BTCommandsEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `bt_commands`(`commandId`,`key`,`commandCode`,`description`) VALUES (?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, BTCommandsEntity value) {
        stmt.bindLong(1, value.getCommandId());
        if (value.getKey() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getKey());
        }
        if (value.getCommandCode() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getCommandCode());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getDescription());
        }
      }
    };
  }

  @Override
  public void insertBTCommands(final BTCommandsEntity... btCommandsEntity) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfBTCommandsEntity.insert(btCommandsEntity);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Maybe<BTCommandsEntity[]> getBTCommands() {
    final String _sql = "Select * FROM bt_commands";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return Maybe.fromCallable(new Callable<BTCommandsEntity[]>() {
      @Override
      public BTCommandsEntity[] call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false);
        try {
          final int _cursorIndexOfCommandId = CursorUtil.getColumnIndexOrThrow(_cursor, "commandId");
          final int _cursorIndexOfKey = CursorUtil.getColumnIndexOrThrow(_cursor, "key");
          final int _cursorIndexOfCommandCode = CursorUtil.getColumnIndexOrThrow(_cursor, "commandCode");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final BTCommandsEntity[] _result = new BTCommandsEntity[_cursor.getCount()];
          int _index = 0;
          while(_cursor.moveToNext()) {
            final BTCommandsEntity _item;
            final int _tmpCommandId;
            _tmpCommandId = _cursor.getInt(_cursorIndexOfCommandId);
            final String _tmpKey;
            _tmpKey = _cursor.getString(_cursorIndexOfKey);
            final String _tmpCommandCode;
            _tmpCommandCode = _cursor.getString(_cursorIndexOfCommandCode);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            _item = new BTCommandsEntity(_tmpCommandId,_tmpKey,_tmpCommandCode,_tmpDescription);
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
  public String getSpecificCommand(final int commandId) {
    final String _sql = "Select commandCode from bt_commands where commandId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, commandId);
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
