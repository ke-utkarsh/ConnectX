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
import ymsli.com.ea1h.database.entity.DAPIoTFileEntity;

@SuppressWarnings({"unchecked", "deprecation"})
public final class DAPIoTFileDao_Impl implements DAPIoTFileDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfDAPIoTFileEntity;

  private final SharedSQLiteStatement __preparedStmtOfRemoveFileEntry;

  private final SharedSQLiteStatement __preparedStmtOfUpdateFileEntity;

  public DAPIoTFileDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDAPIoTFileEntity = new EntityInsertionAdapter<DAPIoTFileEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `zip_folder_entity`(`id`,`fileName`,`filePath`,`createdOn`,`nextTry`,`retryAttempts`,`isSent`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, DAPIoTFileEntity value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindLong(1, value.getId());
        }
        if (value.getFileName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getFileName());
        }
        if (value.getFilePath() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getFilePath());
        }
        stmt.bindLong(4, value.getCreatedOn());
        if (value.getNextTry() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindLong(5, value.getNextTry());
        }
        stmt.bindLong(6, value.getRetryAttempts());
        final int _tmp;
        _tmp = value.isSent() ? 1 : 0;
        stmt.bindLong(7, _tmp);
      }
    };
    this.__preparedStmtOfRemoveFileEntry = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Delete from zip_folder_entity where fileName =?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateFileEntity = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Update zip_folder_entity Set nextTry =?,retryAttempts =? where id = ?";
        return _query;
      }
    };
  }

  @Override
  public void storeFileInfo(final DAPIoTFileEntity file) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfDAPIoTFileEntity.insert(file);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void removeFileEntry(final String fileName) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveFileEntry.acquire();
    int _argIndex = 1;
    if (fileName == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, fileName);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfRemoveFileEntry.release(_stmt);
    }
  }

  @Override
  public void updateFileEntity(final long id, final long nextTry, final int retryAttempts) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateFileEntity.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, nextTry);
    _argIndex = 2;
    _stmt.bindLong(_argIndex, retryAttempts);
    _argIndex = 3;
    _stmt.bindLong(_argIndex, id);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateFileEntity.release(_stmt);
    }
  }

  @Override
  public DAPIoTFileEntity getFileEntity(final String fileName) {
    final String _sql = "Select * from zip_folder_entity where fileName =?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (fileName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, fileName);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "fileName");
      final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
      final int _cursorIndexOfCreatedOn = CursorUtil.getColumnIndexOrThrow(_cursor, "createdOn");
      final int _cursorIndexOfNextTry = CursorUtil.getColumnIndexOrThrow(_cursor, "nextTry");
      final int _cursorIndexOfRetryAttempts = CursorUtil.getColumnIndexOrThrow(_cursor, "retryAttempts");
      final int _cursorIndexOfIsSent = CursorUtil.getColumnIndexOrThrow(_cursor, "isSent");
      final DAPIoTFileEntity _result;
      if(_cursor.moveToFirst()) {
        final Long _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getLong(_cursorIndexOfId);
        }
        final String _tmpFileName;
        _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
        final String _tmpFilePath;
        _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
        final long _tmpCreatedOn;
        _tmpCreatedOn = _cursor.getLong(_cursorIndexOfCreatedOn);
        final Long _tmpNextTry;
        if (_cursor.isNull(_cursorIndexOfNextTry)) {
          _tmpNextTry = null;
        } else {
          _tmpNextTry = _cursor.getLong(_cursorIndexOfNextTry);
        }
        final int _tmpRetryAttempts;
        _tmpRetryAttempts = _cursor.getInt(_cursorIndexOfRetryAttempts);
        final boolean _tmpIsSent;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsSent);
        _tmpIsSent = _tmp != 0;
        _result = new DAPIoTFileEntity(_tmpId,_tmpFileName,_tmpFilePath,_tmpCreatedOn,_tmpNextTry,_tmpRetryAttempts,_tmpIsSent);
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
  public DAPIoTFileEntity[] getUnsyncedFiles() {
    final String _sql = "Select * from zip_folder_entity where isSent =0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfFileName = CursorUtil.getColumnIndexOrThrow(_cursor, "fileName");
      final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
      final int _cursorIndexOfCreatedOn = CursorUtil.getColumnIndexOrThrow(_cursor, "createdOn");
      final int _cursorIndexOfNextTry = CursorUtil.getColumnIndexOrThrow(_cursor, "nextTry");
      final int _cursorIndexOfRetryAttempts = CursorUtil.getColumnIndexOrThrow(_cursor, "retryAttempts");
      final int _cursorIndexOfIsSent = CursorUtil.getColumnIndexOrThrow(_cursor, "isSent");
      final DAPIoTFileEntity[] _result = new DAPIoTFileEntity[_cursor.getCount()];
      int _index = 0;
      while(_cursor.moveToNext()) {
        final DAPIoTFileEntity _item;
        final Long _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getLong(_cursorIndexOfId);
        }
        final String _tmpFileName;
        _tmpFileName = _cursor.getString(_cursorIndexOfFileName);
        final String _tmpFilePath;
        _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
        final long _tmpCreatedOn;
        _tmpCreatedOn = _cursor.getLong(_cursorIndexOfCreatedOn);
        final Long _tmpNextTry;
        if (_cursor.isNull(_cursorIndexOfNextTry)) {
          _tmpNextTry = null;
        } else {
          _tmpNextTry = _cursor.getLong(_cursorIndexOfNextTry);
        }
        final int _tmpRetryAttempts;
        _tmpRetryAttempts = _cursor.getInt(_cursorIndexOfRetryAttempts);
        final boolean _tmpIsSent;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsSent);
        _tmpIsSent = _tmp != 0;
        _item = new DAPIoTFileEntity(_tmpId,_tmpFileName,_tmpFilePath,_tmpCreatedOn,_tmpNextTry,_tmpRetryAttempts,_tmpIsSent);
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
