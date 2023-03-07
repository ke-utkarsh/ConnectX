package ymsli.com.ea1h.database.dao;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import io.reactivex.Single;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import ymsli.com.ea1h.database.entity.BikeEntity;
import ymsli.com.ea1h.database.entity.SliderImage;

@SuppressWarnings({"unchecked", "deprecation"})
public final class BikeDao_Impl implements BikeDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfBikeEntity;

  private final EntityInsertionAdapter __insertionAdapterOfSliderImage;

  private final SharedSQLiteStatement __preparedStmtOfResetLastConnectedBike;

  private final SharedSQLiteStatement __preparedStmtOfSetLastConnectedBike;

  private final SharedSQLiteStatement __preparedStmtOfHardResetLastConnectedBike;

  private final SharedSQLiteStatement __preparedStmtOfRemoveAllBikes;

  public BikeDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBikeEntity = new EntityInsertionAdapter<BikeEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `bike_entity`(`chassisNumber`,`registrationNumber`,`model`,`make`,`btAdd`,`bike_pic`,`is_last_connected`,`bikeId`,`bkModNm`,`ccuId`,`vehType`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, BikeEntity value) {
        if (value.getChasNum() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getChasNum());
        }
        if (value.getRegNum() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getRegNum());
        }
        if (value.getMod() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getMod());
        }
        if (value.getMk() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getMk());
        }
        if (value.getBtAdd() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getBtAdd());
        }
        if (value.getBdp() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getBdp());
        }
        final int _tmp;
        _tmp = value.isLastConnected() ? 1 : 0;
        stmt.bindLong(7, _tmp);
        stmt.bindLong(8, value.getBikeId());
        if (value.getBkModNm() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getBkModNm());
        }
        if (value.getCcuId() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getCcuId());
        }
        if (value.getVehType() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getVehType());
        }
      }
    };
    this.__insertionAdapterOfSliderImage = new EntityInsertionAdapter<SliderImage>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `slider_image`(`imageUrl`,`dataType`) VALUES (?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SliderImage value) {
        if (value.getImageUrl() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getImageUrl());
        }
        if (value.getDataType() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getDataType());
        }
      }
    };
    this.__preparedStmtOfResetLastConnectedBike = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Update bike_entity Set is_last_connected=0 ";
        return _query;
      }
    };
    this.__preparedStmtOfSetLastConnectedBike = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Update bike_entity Set is_last_connected=1 where chassisNumber =?";
        return _query;
      }
    };
    this.__preparedStmtOfHardResetLastConnectedBike = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "Update bike_entity Set is_last_connected=0 where chassisNumber =?";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveAllBikes = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM bike_entity";
        return _query;
      }
    };
  }

  @Override
  public void insertBike(final BikeEntity... trip) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfBikeEntity.insert(trip);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertSliderImage(final SliderImage... images) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfSliderImage.insert(images);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Single<Integer> resetLastConnectedBike() {
    return Single.fromCallable(new Callable<Integer>() {
      @Override
      public Integer call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfResetLastConnectedBike.acquire();
        __db.beginTransaction();
        try {
          final java.lang.Integer _result = _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
          __preparedStmtOfResetLastConnectedBike.release(_stmt);
        }
      }
    });
  }

  @Override
  public void setLastConnectedBike(final String chassisNumber) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfSetLastConnectedBike.acquire();
    int _argIndex = 1;
    if (chassisNumber == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, chassisNumber);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfSetLastConnectedBike.release(_stmt);
    }
  }

  @Override
  public void hardResetLastConnectedBike(final String chassisNumber) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfHardResetLastConnectedBike.acquire();
    int _argIndex = 1;
    if (chassisNumber == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, chassisNumber);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfHardResetLastConnectedBike.release(_stmt);
    }
  }

  @Override
  public void removeAllBikes() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveAllBikes.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfRemoveAllBikes.release(_stmt);
    }
  }

  @Override
  public LiveData<BikeEntity[]> retrieveBikes() {
    final String _sql = "Select * From bike_entity";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"bike_entity"}, false, new Callable<BikeEntity[]>() {
      @Override
      public BikeEntity[] call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false);
        try {
          final int _cursorIndexOfChasNum = CursorUtil.getColumnIndexOrThrow(_cursor, "chassisNumber");
          final int _cursorIndexOfRegNum = CursorUtil.getColumnIndexOrThrow(_cursor, "registrationNumber");
          final int _cursorIndexOfMod = CursorUtil.getColumnIndexOrThrow(_cursor, "model");
          final int _cursorIndexOfMk = CursorUtil.getColumnIndexOrThrow(_cursor, "make");
          final int _cursorIndexOfBtAdd = CursorUtil.getColumnIndexOrThrow(_cursor, "btAdd");
          final int _cursorIndexOfBdp = CursorUtil.getColumnIndexOrThrow(_cursor, "bike_pic");
          final int _cursorIndexOfIsLastConnected = CursorUtil.getColumnIndexOrThrow(_cursor, "is_last_connected");
          final int _cursorIndexOfBikeId = CursorUtil.getColumnIndexOrThrow(_cursor, "bikeId");
          final int _cursorIndexOfBkModNm = CursorUtil.getColumnIndexOrThrow(_cursor, "bkModNm");
          final int _cursorIndexOfCcuId = CursorUtil.getColumnIndexOrThrow(_cursor, "ccuId");
          final int _cursorIndexOfVehType = CursorUtil.getColumnIndexOrThrow(_cursor, "vehType");
          final BikeEntity[] _result = new BikeEntity[_cursor.getCount()];
          int _index = 0;
          while(_cursor.moveToNext()) {
            final BikeEntity _item;
            final String _tmpChasNum;
            _tmpChasNum = _cursor.getString(_cursorIndexOfChasNum);
            final String _tmpRegNum;
            _tmpRegNum = _cursor.getString(_cursorIndexOfRegNum);
            final String _tmpMod;
            _tmpMod = _cursor.getString(_cursorIndexOfMod);
            final String _tmpMk;
            _tmpMk = _cursor.getString(_cursorIndexOfMk);
            final String _tmpBtAdd;
            _tmpBtAdd = _cursor.getString(_cursorIndexOfBtAdd);
            final String _tmpBdp;
            _tmpBdp = _cursor.getString(_cursorIndexOfBdp);
            final boolean _tmpIsLastConnected;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsLastConnected);
            _tmpIsLastConnected = _tmp != 0;
            final long _tmpBikeId;
            _tmpBikeId = _cursor.getLong(_cursorIndexOfBikeId);
            final String _tmpBkModNm;
            _tmpBkModNm = _cursor.getString(_cursorIndexOfBkModNm);
            final String _tmpCcuId;
            _tmpCcuId = _cursor.getString(_cursorIndexOfCcuId);
            final String _tmpVehType;
            _tmpVehType = _cursor.getString(_cursorIndexOfVehType);
            _item = new BikeEntity(_tmpChasNum,_tmpRegNum,_tmpMod,_tmpMk,_tmpBtAdd,_tmpBdp,_tmpIsLastConnected,_tmpBikeId,_tmpBkModNm,_tmpCcuId,_tmpVehType);
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
  public LiveData<BikeEntity> getLastConnectedBike() {
    final String _sql = "Select * From bike_entity where is_last_connected = 1 limit 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"bike_entity"}, false, new Callable<BikeEntity>() {
      @Override
      public BikeEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false);
        try {
          final int _cursorIndexOfChasNum = CursorUtil.getColumnIndexOrThrow(_cursor, "chassisNumber");
          final int _cursorIndexOfRegNum = CursorUtil.getColumnIndexOrThrow(_cursor, "registrationNumber");
          final int _cursorIndexOfMod = CursorUtil.getColumnIndexOrThrow(_cursor, "model");
          final int _cursorIndexOfMk = CursorUtil.getColumnIndexOrThrow(_cursor, "make");
          final int _cursorIndexOfBtAdd = CursorUtil.getColumnIndexOrThrow(_cursor, "btAdd");
          final int _cursorIndexOfBdp = CursorUtil.getColumnIndexOrThrow(_cursor, "bike_pic");
          final int _cursorIndexOfIsLastConnected = CursorUtil.getColumnIndexOrThrow(_cursor, "is_last_connected");
          final int _cursorIndexOfBikeId = CursorUtil.getColumnIndexOrThrow(_cursor, "bikeId");
          final int _cursorIndexOfBkModNm = CursorUtil.getColumnIndexOrThrow(_cursor, "bkModNm");
          final int _cursorIndexOfCcuId = CursorUtil.getColumnIndexOrThrow(_cursor, "ccuId");
          final int _cursorIndexOfVehType = CursorUtil.getColumnIndexOrThrow(_cursor, "vehType");
          final BikeEntity _result;
          if(_cursor.moveToFirst()) {
            final String _tmpChasNum;
            _tmpChasNum = _cursor.getString(_cursorIndexOfChasNum);
            final String _tmpRegNum;
            _tmpRegNum = _cursor.getString(_cursorIndexOfRegNum);
            final String _tmpMod;
            _tmpMod = _cursor.getString(_cursorIndexOfMod);
            final String _tmpMk;
            _tmpMk = _cursor.getString(_cursorIndexOfMk);
            final String _tmpBtAdd;
            _tmpBtAdd = _cursor.getString(_cursorIndexOfBtAdd);
            final String _tmpBdp;
            _tmpBdp = _cursor.getString(_cursorIndexOfBdp);
            final boolean _tmpIsLastConnected;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsLastConnected);
            _tmpIsLastConnected = _tmp != 0;
            final long _tmpBikeId;
            _tmpBikeId = _cursor.getLong(_cursorIndexOfBikeId);
            final String _tmpBkModNm;
            _tmpBkModNm = _cursor.getString(_cursorIndexOfBkModNm);
            final String _tmpCcuId;
            _tmpCcuId = _cursor.getString(_cursorIndexOfCcuId);
            final String _tmpVehType;
            _tmpVehType = _cursor.getString(_cursorIndexOfVehType);
            _result = new BikeEntity(_tmpChasNum,_tmpRegNum,_tmpMod,_tmpMk,_tmpBtAdd,_tmpBdp,_tmpIsLastConnected,_tmpBikeId,_tmpBkModNm,_tmpCcuId,_tmpVehType);
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
  public BikeEntity getLastConnectedBikeForService() {
    final String _sql = "Select * From bike_entity where is_last_connected = 1 limit 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfChasNum = CursorUtil.getColumnIndexOrThrow(_cursor, "chassisNumber");
      final int _cursorIndexOfRegNum = CursorUtil.getColumnIndexOrThrow(_cursor, "registrationNumber");
      final int _cursorIndexOfMod = CursorUtil.getColumnIndexOrThrow(_cursor, "model");
      final int _cursorIndexOfMk = CursorUtil.getColumnIndexOrThrow(_cursor, "make");
      final int _cursorIndexOfBtAdd = CursorUtil.getColumnIndexOrThrow(_cursor, "btAdd");
      final int _cursorIndexOfBdp = CursorUtil.getColumnIndexOrThrow(_cursor, "bike_pic");
      final int _cursorIndexOfIsLastConnected = CursorUtil.getColumnIndexOrThrow(_cursor, "is_last_connected");
      final int _cursorIndexOfBikeId = CursorUtil.getColumnIndexOrThrow(_cursor, "bikeId");
      final int _cursorIndexOfBkModNm = CursorUtil.getColumnIndexOrThrow(_cursor, "bkModNm");
      final int _cursorIndexOfCcuId = CursorUtil.getColumnIndexOrThrow(_cursor, "ccuId");
      final int _cursorIndexOfVehType = CursorUtil.getColumnIndexOrThrow(_cursor, "vehType");
      final BikeEntity _result;
      if(_cursor.moveToFirst()) {
        final String _tmpChasNum;
        _tmpChasNum = _cursor.getString(_cursorIndexOfChasNum);
        final String _tmpRegNum;
        _tmpRegNum = _cursor.getString(_cursorIndexOfRegNum);
        final String _tmpMod;
        _tmpMod = _cursor.getString(_cursorIndexOfMod);
        final String _tmpMk;
        _tmpMk = _cursor.getString(_cursorIndexOfMk);
        final String _tmpBtAdd;
        _tmpBtAdd = _cursor.getString(_cursorIndexOfBtAdd);
        final String _tmpBdp;
        _tmpBdp = _cursor.getString(_cursorIndexOfBdp);
        final boolean _tmpIsLastConnected;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsLastConnected);
        _tmpIsLastConnected = _tmp != 0;
        final long _tmpBikeId;
        _tmpBikeId = _cursor.getLong(_cursorIndexOfBikeId);
        final String _tmpBkModNm;
        _tmpBkModNm = _cursor.getString(_cursorIndexOfBkModNm);
        final String _tmpCcuId;
        _tmpCcuId = _cursor.getString(_cursorIndexOfCcuId);
        final String _tmpVehType;
        _tmpVehType = _cursor.getString(_cursorIndexOfVehType);
        _result = new BikeEntity(_tmpChasNum,_tmpRegNum,_tmpMod,_tmpMk,_tmpBtAdd,_tmpBdp,_tmpIsLastConnected,_tmpBikeId,_tmpBkModNm,_tmpCcuId,_tmpVehType);
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
  public List<String> getBikeChassis(final String make) {
    final String _sql = "Select chassisNumber from bike_entity where make =?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (make == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, make);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final List<String> _result = new ArrayList<String>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final String _item;
        _item = _cursor.getString(0);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<String> getBikeMake() {
    final String _sql = "Select make from bike_entity";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final List<String> _result = new ArrayList<String>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final String _item;
        _item = _cursor.getString(0);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<SliderImage[]> getSliderImages() {
    final String _sql = "Select * From slider_image";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"slider_image"}, false, new Callable<SliderImage[]>() {
      @Override
      public SliderImage[] call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false);
        try {
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final int _cursorIndexOfDataType = CursorUtil.getColumnIndexOrThrow(_cursor, "dataType");
          final SliderImage[] _result = new SliderImage[_cursor.getCount()];
          int _index = 0;
          while(_cursor.moveToNext()) {
            final SliderImage _item;
            final String _tmpImageUrl;
            _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            final String _tmpDataType;
            _tmpDataType = _cursor.getString(_cursorIndexOfDataType);
            _item = new SliderImage(_tmpImageUrl,_tmpDataType);
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
  public LiveData<String> getBikeRegNum(final String chassisNumber) {
    final String _sql = "Select registrationNumber from bike_entity where chassisNumber =?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (chassisNumber == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, chassisNumber);
    }
    return __db.getInvalidationTracker().createLiveData(new String[]{"bike_entity"}, false, new Callable<String>() {
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
  public LiveData<BikeEntity> getBikeRegNumModel(final String chassisNumber) {
    final String _sql = "Select * from bike_entity where chassisNumber =?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (chassisNumber == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, chassisNumber);
    }
    return __db.getInvalidationTracker().createLiveData(new String[]{"bike_entity"}, false, new Callable<BikeEntity>() {
      @Override
      public BikeEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false);
        try {
          final int _cursorIndexOfChasNum = CursorUtil.getColumnIndexOrThrow(_cursor, "chassisNumber");
          final int _cursorIndexOfRegNum = CursorUtil.getColumnIndexOrThrow(_cursor, "registrationNumber");
          final int _cursorIndexOfMod = CursorUtil.getColumnIndexOrThrow(_cursor, "model");
          final int _cursorIndexOfMk = CursorUtil.getColumnIndexOrThrow(_cursor, "make");
          final int _cursorIndexOfBtAdd = CursorUtil.getColumnIndexOrThrow(_cursor, "btAdd");
          final int _cursorIndexOfBdp = CursorUtil.getColumnIndexOrThrow(_cursor, "bike_pic");
          final int _cursorIndexOfIsLastConnected = CursorUtil.getColumnIndexOrThrow(_cursor, "is_last_connected");
          final int _cursorIndexOfBikeId = CursorUtil.getColumnIndexOrThrow(_cursor, "bikeId");
          final int _cursorIndexOfBkModNm = CursorUtil.getColumnIndexOrThrow(_cursor, "bkModNm");
          final int _cursorIndexOfCcuId = CursorUtil.getColumnIndexOrThrow(_cursor, "ccuId");
          final int _cursorIndexOfVehType = CursorUtil.getColumnIndexOrThrow(_cursor, "vehType");
          final BikeEntity _result;
          if(_cursor.moveToFirst()) {
            final String _tmpChasNum;
            _tmpChasNum = _cursor.getString(_cursorIndexOfChasNum);
            final String _tmpRegNum;
            _tmpRegNum = _cursor.getString(_cursorIndexOfRegNum);
            final String _tmpMod;
            _tmpMod = _cursor.getString(_cursorIndexOfMod);
            final String _tmpMk;
            _tmpMk = _cursor.getString(_cursorIndexOfMk);
            final String _tmpBtAdd;
            _tmpBtAdd = _cursor.getString(_cursorIndexOfBtAdd);
            final String _tmpBdp;
            _tmpBdp = _cursor.getString(_cursorIndexOfBdp);
            final boolean _tmpIsLastConnected;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsLastConnected);
            _tmpIsLastConnected = _tmp != 0;
            final long _tmpBikeId;
            _tmpBikeId = _cursor.getLong(_cursorIndexOfBikeId);
            final String _tmpBkModNm;
            _tmpBkModNm = _cursor.getString(_cursorIndexOfBkModNm);
            final String _tmpCcuId;
            _tmpCcuId = _cursor.getString(_cursorIndexOfCcuId);
            final String _tmpVehType;
            _tmpVehType = _cursor.getString(_cursorIndexOfVehType);
            _result = new BikeEntity(_tmpChasNum,_tmpRegNum,_tmpMod,_tmpMk,_tmpBtAdd,_tmpBdp,_tmpIsLastConnected,_tmpBikeId,_tmpBkModNm,_tmpCcuId,_tmpVehType);
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
  public List<String> getAllModelNames() {
    final String _sql = "Select DISTINCT bkModNm from bike_entity";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final List<String> _result = new ArrayList<String>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final String _item;
        _item = _cursor.getString(0);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<String> getChassisNoByModelName(final List<String> models) {
    StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("Select DISTINCT chassisNumber from bike_entity where bkModNm IN (");
    final int _inputSize = models.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (String _item : models) {
      if (_item == null) {
        _statement.bindNull(_argIndex);
      } else {
        _statement.bindString(_argIndex, _item);
      }
      _argIndex ++;
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final List<String> _result = new ArrayList<String>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final String _item_1;
        _item_1 = _cursor.getString(0);
        _result.add(_item_1);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public BikeEntity getBikeModelDetails(final String chassisNumber) {
    final String _sql = "Select * from bike_entity where chassisNumber =?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (chassisNumber == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, chassisNumber);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfChasNum = CursorUtil.getColumnIndexOrThrow(_cursor, "chassisNumber");
      final int _cursorIndexOfRegNum = CursorUtil.getColumnIndexOrThrow(_cursor, "registrationNumber");
      final int _cursorIndexOfMod = CursorUtil.getColumnIndexOrThrow(_cursor, "model");
      final int _cursorIndexOfMk = CursorUtil.getColumnIndexOrThrow(_cursor, "make");
      final int _cursorIndexOfBtAdd = CursorUtil.getColumnIndexOrThrow(_cursor, "btAdd");
      final int _cursorIndexOfBdp = CursorUtil.getColumnIndexOrThrow(_cursor, "bike_pic");
      final int _cursorIndexOfIsLastConnected = CursorUtil.getColumnIndexOrThrow(_cursor, "is_last_connected");
      final int _cursorIndexOfBikeId = CursorUtil.getColumnIndexOrThrow(_cursor, "bikeId");
      final int _cursorIndexOfBkModNm = CursorUtil.getColumnIndexOrThrow(_cursor, "bkModNm");
      final int _cursorIndexOfCcuId = CursorUtil.getColumnIndexOrThrow(_cursor, "ccuId");
      final int _cursorIndexOfVehType = CursorUtil.getColumnIndexOrThrow(_cursor, "vehType");
      final BikeEntity _result;
      if(_cursor.moveToFirst()) {
        final String _tmpChasNum;
        _tmpChasNum = _cursor.getString(_cursorIndexOfChasNum);
        final String _tmpRegNum;
        _tmpRegNum = _cursor.getString(_cursorIndexOfRegNum);
        final String _tmpMod;
        _tmpMod = _cursor.getString(_cursorIndexOfMod);
        final String _tmpMk;
        _tmpMk = _cursor.getString(_cursorIndexOfMk);
        final String _tmpBtAdd;
        _tmpBtAdd = _cursor.getString(_cursorIndexOfBtAdd);
        final String _tmpBdp;
        _tmpBdp = _cursor.getString(_cursorIndexOfBdp);
        final boolean _tmpIsLastConnected;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsLastConnected);
        _tmpIsLastConnected = _tmp != 0;
        final long _tmpBikeId;
        _tmpBikeId = _cursor.getLong(_cursorIndexOfBikeId);
        final String _tmpBkModNm;
        _tmpBkModNm = _cursor.getString(_cursorIndexOfBkModNm);
        final String _tmpCcuId;
        _tmpCcuId = _cursor.getString(_cursorIndexOfCcuId);
        final String _tmpVehType;
        _tmpVehType = _cursor.getString(_cursorIndexOfVehType);
        _result = new BikeEntity(_tmpChasNum,_tmpRegNum,_tmpMod,_tmpMk,_tmpBtAdd,_tmpBdp,_tmpIsLastConnected,_tmpBikeId,_tmpBkModNm,_tmpCcuId,_tmpVehType);
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
  public List<Long> getAllBikeIds() {
    final String _sql = "Select bikeId From bike_entity";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final List<Long> _result = new ArrayList<Long>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Long _item;
        if (_cursor.isNull(0)) {
          _item = null;
        } else {
          _item = _cursor.getLong(0);
        }
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<String> getAllRegNumbers() {
    final String _sql = "Select DISTINCT registrationNumber from bike_entity";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final List<String> _result = new ArrayList<String>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final String _item;
        _item = _cursor.getString(0);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<String> getChassisNoByRegNumbers(final List<String> regNumbers) {
    StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("Select DISTINCT chassisNumber from bike_entity where registrationNumber IN (");
    final int _inputSize = regNumbers.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (String _item : regNumbers) {
      if (_item == null) {
        _statement.bindNull(_argIndex);
      } else {
        _statement.bindString(_argIndex, _item);
      }
      _argIndex ++;
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final List<String> _result = new ArrayList<String>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final String _item_1;
        _item_1 = _cursor.getString(0);
        _result.add(_item_1);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public BikeEntity[] getAllBikes() {
    final String _sql = "Select * From bike_entity";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false);
    try {
      final int _cursorIndexOfChasNum = CursorUtil.getColumnIndexOrThrow(_cursor, "chassisNumber");
      final int _cursorIndexOfRegNum = CursorUtil.getColumnIndexOrThrow(_cursor, "registrationNumber");
      final int _cursorIndexOfMod = CursorUtil.getColumnIndexOrThrow(_cursor, "model");
      final int _cursorIndexOfMk = CursorUtil.getColumnIndexOrThrow(_cursor, "make");
      final int _cursorIndexOfBtAdd = CursorUtil.getColumnIndexOrThrow(_cursor, "btAdd");
      final int _cursorIndexOfBdp = CursorUtil.getColumnIndexOrThrow(_cursor, "bike_pic");
      final int _cursorIndexOfIsLastConnected = CursorUtil.getColumnIndexOrThrow(_cursor, "is_last_connected");
      final int _cursorIndexOfBikeId = CursorUtil.getColumnIndexOrThrow(_cursor, "bikeId");
      final int _cursorIndexOfBkModNm = CursorUtil.getColumnIndexOrThrow(_cursor, "bkModNm");
      final int _cursorIndexOfCcuId = CursorUtil.getColumnIndexOrThrow(_cursor, "ccuId");
      final int _cursorIndexOfVehType = CursorUtil.getColumnIndexOrThrow(_cursor, "vehType");
      final BikeEntity[] _result = new BikeEntity[_cursor.getCount()];
      int _index = 0;
      while(_cursor.moveToNext()) {
        final BikeEntity _item;
        final String _tmpChasNum;
        _tmpChasNum = _cursor.getString(_cursorIndexOfChasNum);
        final String _tmpRegNum;
        _tmpRegNum = _cursor.getString(_cursorIndexOfRegNum);
        final String _tmpMod;
        _tmpMod = _cursor.getString(_cursorIndexOfMod);
        final String _tmpMk;
        _tmpMk = _cursor.getString(_cursorIndexOfMk);
        final String _tmpBtAdd;
        _tmpBtAdd = _cursor.getString(_cursorIndexOfBtAdd);
        final String _tmpBdp;
        _tmpBdp = _cursor.getString(_cursorIndexOfBdp);
        final boolean _tmpIsLastConnected;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsLastConnected);
        _tmpIsLastConnected = _tmp != 0;
        final long _tmpBikeId;
        _tmpBikeId = _cursor.getLong(_cursorIndexOfBikeId);
        final String _tmpBkModNm;
        _tmpBkModNm = _cursor.getString(_cursorIndexOfBkModNm);
        final String _tmpCcuId;
        _tmpCcuId = _cursor.getString(_cursorIndexOfCcuId);
        final String _tmpVehType;
        _tmpVehType = _cursor.getString(_cursorIndexOfVehType);
        _item = new BikeEntity(_tmpChasNum,_tmpRegNum,_tmpMod,_tmpMk,_tmpBtAdd,_tmpBdp,_tmpIsLastConnected,_tmpBikeId,_tmpBkModNm,_tmpCcuId,_tmpVehType);
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
