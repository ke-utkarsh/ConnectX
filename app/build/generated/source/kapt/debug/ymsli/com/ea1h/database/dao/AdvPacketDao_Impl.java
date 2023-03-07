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
import ymsli.com.ea1h.database.entity.AdvPacketEntity;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AdvPacketDao_Impl implements AdvPacketDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfAdvPacketEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteLogs;

  public AdvPacketDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAdvPacketEntity = new EntityInsertionAdapter<AdvPacketEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `adv_packet`(`id`,`btAdd`,`connectionString`,`currentTime`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, AdvPacketEntity value) {
        stmt.bindLong(1, value.getId());
        if (value.getBtAdd() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getBtAdd());
        }
        if (value.getConnectionString() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getConnectionString());
        }
        if (value.getCurrentTime() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, value.getCurrentTime());
        }
      }
    };
    this.__preparedStmtOfDeleteLogs = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from adv_packet where id=?";
        return _query;
      }
    };
  }

  @Override
  public void insertAdvPacket(final AdvPacketEntity... advPacketEntity) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfAdvPacketEntity.insert(advPacketEntity);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteLogs(final long id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteLogs.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, id);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteLogs.release(_stmt);
    }
  }

  @Override
  public AdvPacketEntity[] getAdvPacket() {
    final String _sql = "Select * FROM adv_packet";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfBtAdd = CursorUtil.getColumnIndexOrThrow(_cursor, "btAdd");
      final int _cursorIndexOfConnectionString = CursorUtil.getColumnIndexOrThrow(_cursor, "connectionString");
      final int _cursorIndexOfCurrentTime = CursorUtil.getColumnIndexOrThrow(_cursor, "currentTime");
      final AdvPacketEntity[] _result = new AdvPacketEntity[_cursor.getCount()];
      int _index = 0;
      while(_cursor.moveToNext()) {
        final AdvPacketEntity _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final String _tmpBtAdd;
        _tmpBtAdd = _cursor.getString(_cursorIndexOfBtAdd);
        final String _tmpConnectionString;
        _tmpConnectionString = _cursor.getString(_cursorIndexOfConnectionString);
        final Long _tmpCurrentTime;
        if (_cursor.isNull(_cursorIndexOfCurrentTime)) {
          _tmpCurrentTime = null;
        } else {
          _tmpCurrentTime = _cursor.getLong(_cursorIndexOfCurrentTime);
        }
        _item = new AdvPacketEntity(_tmpId,_tmpBtAdd,_tmpConnectionString,_tmpCurrentTime);
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
