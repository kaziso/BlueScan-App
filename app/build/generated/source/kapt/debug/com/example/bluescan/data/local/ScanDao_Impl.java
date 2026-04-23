package com.example.bluescan.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@SuppressWarnings({"unchecked", "deprecation"})
public final class ScanDao_Impl implements ScanDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ScanEntity> __insertionAdapterOfScanEntity;

  private final EntityDeletionOrUpdateAdapter<ScanEntity> __updateAdapterOfScanEntity;

  private final SharedSQLiteStatement __preparedStmtOfMarkAsSynced;

  public ScanDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfScanEntity = new EntityInsertionAdapter<ScanEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `scans` (`id`,`projectId`,`date`,`technicianId`,`unit`,`part`,`barcode`,`timestamp`,`isSynced`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ScanEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getProjectId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getProjectId());
        }
        if (entity.getDate() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDate());
        }
        if (entity.getTechnicianId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getTechnicianId());
        }
        statement.bindLong(5, entity.getUnit());
        if (entity.getPart() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getPart());
        }
        if (entity.getBarcode() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getBarcode());
        }
        statement.bindLong(8, entity.getTimestamp());
        final int _tmp = entity.isSynced() ? 1 : 0;
        statement.bindLong(9, _tmp);
      }
    };
    this.__updateAdapterOfScanEntity = new EntityDeletionOrUpdateAdapter<ScanEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `scans` SET `id` = ?,`projectId` = ?,`date` = ?,`technicianId` = ?,`unit` = ?,`part` = ?,`barcode` = ?,`timestamp` = ?,`isSynced` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ScanEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getProjectId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getProjectId());
        }
        if (entity.getDate() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDate());
        }
        if (entity.getTechnicianId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getTechnicianId());
        }
        statement.bindLong(5, entity.getUnit());
        if (entity.getPart() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getPart());
        }
        if (entity.getBarcode() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getBarcode());
        }
        statement.bindLong(8, entity.getTimestamp());
        final int _tmp = entity.isSynced() ? 1 : 0;
        statement.bindLong(9, _tmp);
        statement.bindLong(10, entity.getId());
      }
    };
    this.__preparedStmtOfMarkAsSynced = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE scans SET isSynced = 1 WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertScan(final ScanEntity scan, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfScanEntity.insertAndReturnId(scan);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateScan(final ScanEntity scan, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfScanEntity.handle(scan);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object markAsSynced(final int scanId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkAsSynced.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, scanId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfMarkAsSynced.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getUnsyncedScans(final Continuation<? super List<ScanEntity>> $completion) {
    final String _sql = "SELECT * FROM scans WHERE isSynced = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ScanEntity>>() {
      @Override
      @NonNull
      public List<ScanEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "projectId");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfTechnicianId = CursorUtil.getColumnIndexOrThrow(_cursor, "technicianId");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfPart = CursorUtil.getColumnIndexOrThrow(_cursor, "part");
          final int _cursorIndexOfBarcode = CursorUtil.getColumnIndexOrThrow(_cursor, "barcode");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final List<ScanEntity> _result = new ArrayList<ScanEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ScanEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpProjectId;
            if (_cursor.isNull(_cursorIndexOfProjectId)) {
              _tmpProjectId = null;
            } else {
              _tmpProjectId = _cursor.getString(_cursorIndexOfProjectId);
            }
            final String _tmpDate;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmpDate = null;
            } else {
              _tmpDate = _cursor.getString(_cursorIndexOfDate);
            }
            final String _tmpTechnicianId;
            if (_cursor.isNull(_cursorIndexOfTechnicianId)) {
              _tmpTechnicianId = null;
            } else {
              _tmpTechnicianId = _cursor.getString(_cursorIndexOfTechnicianId);
            }
            final int _tmpUnit;
            _tmpUnit = _cursor.getInt(_cursorIndexOfUnit);
            final String _tmpPart;
            if (_cursor.isNull(_cursorIndexOfPart)) {
              _tmpPart = null;
            } else {
              _tmpPart = _cursor.getString(_cursorIndexOfPart);
            }
            final String _tmpBarcode;
            if (_cursor.isNull(_cursorIndexOfBarcode)) {
              _tmpBarcode = null;
            } else {
              _tmpBarcode = _cursor.getString(_cursorIndexOfBarcode);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final boolean _tmpIsSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp != 0;
            _item = new ScanEntity(_tmpId,_tmpProjectId,_tmpDate,_tmpTechnicianId,_tmpUnit,_tmpPart,_tmpBarcode,_tmpTimestamp,_tmpIsSynced);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getScanByBarcode(final String barcode,
      final Continuation<? super ScanEntity> $completion) {
    final String _sql = "SELECT * FROM scans WHERE barcode = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (barcode == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, barcode);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ScanEntity>() {
      @Override
      @Nullable
      public ScanEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "projectId");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfTechnicianId = CursorUtil.getColumnIndexOrThrow(_cursor, "technicianId");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfPart = CursorUtil.getColumnIndexOrThrow(_cursor, "part");
          final int _cursorIndexOfBarcode = CursorUtil.getColumnIndexOrThrow(_cursor, "barcode");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final ScanEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpProjectId;
            if (_cursor.isNull(_cursorIndexOfProjectId)) {
              _tmpProjectId = null;
            } else {
              _tmpProjectId = _cursor.getString(_cursorIndexOfProjectId);
            }
            final String _tmpDate;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmpDate = null;
            } else {
              _tmpDate = _cursor.getString(_cursorIndexOfDate);
            }
            final String _tmpTechnicianId;
            if (_cursor.isNull(_cursorIndexOfTechnicianId)) {
              _tmpTechnicianId = null;
            } else {
              _tmpTechnicianId = _cursor.getString(_cursorIndexOfTechnicianId);
            }
            final int _tmpUnit;
            _tmpUnit = _cursor.getInt(_cursorIndexOfUnit);
            final String _tmpPart;
            if (_cursor.isNull(_cursorIndexOfPart)) {
              _tmpPart = null;
            } else {
              _tmpPart = _cursor.getString(_cursorIndexOfPart);
            }
            final String _tmpBarcode;
            if (_cursor.isNull(_cursorIndexOfBarcode)) {
              _tmpBarcode = null;
            } else {
              _tmpBarcode = _cursor.getString(_cursorIndexOfBarcode);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final boolean _tmpIsSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp != 0;
            _result = new ScanEntity(_tmpId,_tmpProjectId,_tmpDate,_tmpTechnicianId,_tmpUnit,_tmpPart,_tmpBarcode,_tmpTimestamp,_tmpIsSynced);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object isSlotFilled(final int unit, final String part,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM scans WHERE unit = ? AND part = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, unit);
    _argIndex = 2;
    if (part == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, part);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
