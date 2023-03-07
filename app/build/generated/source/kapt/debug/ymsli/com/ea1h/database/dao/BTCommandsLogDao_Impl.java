package ymsli.com.ea1h.database.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import ymsli.com.ea1h.database.entity.BTCommandsLogEntity;

@SuppressWarnings({"unchecked", "deprecation"})
public final class BTCommandsLogDao_Impl implements BTCommandsLogDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfBTCommandsLogEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateCommandStatus;

  private final SharedSQLiteStatement __preparedStmtOfDeleteLogs;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllSyncedLogs;

  private final SharedSQLiteStatement __preparedStmtOfTrimToMaxSize;

  public BTCommandsLogDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBTCommandsLogEntity = new EntityInsertionAdapter<BTCommandsLogEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `bt_command_logs`(`triggeredOn`,`cmdType`,`chassNum`,`deviceId`,`isSuccessful`,`isCommit`,`btAdd`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, BTCommandsLogEntity value) {
        stmt.bindLong(1, value.getTriggeredOn());
        stmt.bindLong(2, value.getCmdType());
        if (value.getChassNum() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getChassNum());
        }
        if (value.getDeviceId() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getDeviceId());
        }
        final int _tmp;
        _tmp = value.isSuccessful() ? 1 : 0;
        stmt.bindLong(5, _tmp);
        final int _tmp_1;
        _tmp_1 = value.isCommit() ? 1 : 0;
        stmt.bindLong(6, _tmp_1);
        if (value.getBtAdd() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getBtAdd());
        }
      }
    };
    this.__preparedStmtOfUpdateCommandStatus = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Update bt_command_logs Set isSuccessful =?, isCommit = 1  where isCommit = 0";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteLogs = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from bt_command_logs";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllSyncedLogs = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM bt_command_logs where triggeredOn >= ? and triggeredOn <= ?";
        return _query;
      }
    };
    this.__preparedStmtOfTrimToMaxSize = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM bt_command_logs WHERE triggeredOn IN (SELECT triggeredOn FROM(SELECT triggeredOn FROM bt_command_logs ORDER BY triggeredOn DESC LIMIT 2000 OFFSET ?) a)";
        return _query;
      }
    };
  }

  @Override
  public void insert(final BTCommandsLogEntity btCommandsLogEntity) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfBTCommandsLogEntity.insert(btCommandsLogEntity);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateCommandStatus(final boolean isSuccessful) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateCommandStatus.acquire();
    int _argIndex = 1;
    final int _tmp;
    _tmp = isSuccessful ? 1 : 0;
    _stmt.bindLong(_argIndex, _tmp);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateCommandStatus.release(_stmt);
    }
  }

  @Override
  public void deleteLogs() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteLogs.acquire();
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
  public void deleteAllSyncedLogs(final long minTrig, final long maxTrig) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllSyncedLogs.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, minTrig);
    _argIndex = 2;
    _stmt.bindLong(_argIndex, maxTrig);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAllSyncedLogs.release(_stmt);
    }
  }

  @Override
  public int trimToMaxSize(final int maxSize) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfTrimToMaxSize.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, maxSize);
    __db.beginTransaction();
    try {
      final int _result = _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
      __preparedStmtOfTrimToMaxSize.release(_stmt);
    }
  }

  @Override
  public BTCommandsLogEntity[] getFailedCommands() {
    final String _sql = "Select * from bt_command_logs Where isSuccessful=0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfTriggeredOn = CursorUtil.getColumnIndexOrThrow(_cursor, "triggeredOn");
      final int _cursorIndexOfCmdType = CursorUtil.getColumnIndexOrThrow(_cursor, "cmdType");
      final int _cursorIndexOfChassNum = CursorUtil.getColumnIndexOrThrow(_cursor, "chassNum");
      final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceId");
      final int _cursorIndexOfIsSuccessful = CursorUtil.getColumnIndexOrThrow(_cursor, "isSuccessful");
      final int _cursorIndexOfIsCommit = CursorUtil.getColumnIndexOrThrow(_cursor, "isCommit");
      final int _cursorIndexOfBtAdd = CursorUtil.getColumnIndexOrThrow(_cursor, "btAdd");
      final BTCommandsLogEntity[] _result = new BTCommandsLogEntity[_cursor.getCount()];
      int _index = 0;
      while(_cursor.moveToNext()) {
        final BTCommandsLogEntity _item;
        final long _tmpTriggeredOn;
        _tmpTriggeredOn = _cursor.getLong(_cursorIndexOfTriggeredOn);
        final int _tmpCmdType;
        _tmpCmdType = _cursor.getInt(_cursorIndexOfCmdType);
        final String _tmpChassNum;
        _tmpChassNum = _cursor.getString(_cursorIndexOfChassNum);
        final String _tmpDeviceId;
        _tmpDeviceId = _cursor.getString(_cursorIndexOfDeviceId);
        final boolean _tmpIsSuccessful;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsSuccessful);
        _tmpIsSuccessful = _tmp != 0;
        final boolean _tmpIsCommit;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsCommit);
        _tmpIsCommit = _tmp_1 != 0;
        final String _tmpBtAdd;
        _tmpBtAdd = _cursor.getString(_cursorIndexOfBtAdd);
        _item = new BTCommandsLogEntity(_tmpTriggeredOn,_tmpCmdType,_tmpChassNum,_tmpDeviceId,_tmpIsSuccessful,_tmpIsCommit,_tmpBtAdd);
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
  public BTCommandsLogEntity[] getAllCommands(final int rowLimit) {
    final String _sql = "Select * from bt_command_logs order by triggeredOn desc limit ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, rowLimit);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfTriggeredOn = CursorUtil.getColumnIndexOrThrow(_cursor, "triggeredOn");
      final int _cursorIndexOfCmdType = CursorUtil.getColumnIndexOrThrow(_cursor, "cmdType");
      final int _cursorIndexOfChassNum = CursorUtil.getColumnIndexOrThrow(_cursor, "chassNum");
      final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceId");
      final int _cursorIndexOfIsSuccessful = CursorUtil.getColumnIndexOrThrow(_cursor, "isSuccessful");
      final int _cursorIndexOfIsCommit = CursorUtil.getColumnIndexOrThrow(_cursor, "isCommit");
      final int _cursorIndexOfBtAdd = CursorUtil.getColumnIndexOrThrow(_cursor, "btAdd");
      final BTCommandsLogEntity[] _result = new BTCommandsLogEntity[_cursor.getCount()];
      int _index = 0;
      while(_cursor.moveToNext()) {
        final BTCommandsLogEntity _item;
        final long _tmpTriggeredOn;
        _tmpTriggeredOn = _cursor.getLong(_cursorIndexOfTriggeredOn);
        final int _tmpCmdType;
        _tmpCmdType = _cursor.getInt(_cursorIndexOfCmdType);
        final String _tmpChassNum;
        _tmpChassNum = _cursor.getString(_cursorIndexOfChassNum);
        final String _tmpDeviceId;
        _tmpDeviceId = _cursor.getString(_cursorIndexOfDeviceId);
        final boolean _tmpIsSuccessful;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsSuccessful);
        _tmpIsSuccessful = _tmp != 0;
        final boolean _tmpIsCommit;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsCommit);
        _tmpIsCommit = _tmp_1 != 0;
        final String _tmpBtAdd;
        _tmpBtAdd = _cursor.getString(_cursorIndexOfBtAdd);
        _item = new BTCommandsLogEntity(_tmpTriggeredOn,_tmpCmdType,_tmpChassNum,_tmpDeviceId,_tmpIsSuccessful,_tmpIsCommit,_tmpBtAdd);
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
