package BD;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import utilities.UtilityGoal;

public class ConexionSQLiteOpenHelper extends SQLiteOpenHelper {

    public ConexionSQLiteOpenHelper(@Nullable Context context) {
        super(context, UtilityGoal.DATABASE, null, 1);
        SQLiteDatabase db = this.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UtilityGoal.CREATE_TABLA_GOAL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS goals");
        onCreate(db);
    }
}
