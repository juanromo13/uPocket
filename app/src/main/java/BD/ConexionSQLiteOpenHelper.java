package BD;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import utilities.UtilityGoal;
import utilities.UtilityMovement;

public class ConexionSQLiteOpenHelper extends SQLiteOpenHelper {


    public ConexionSQLiteOpenHelper(@Nullable Context context) {
        super(context, UtilityMovement.DATABASE, null, 1);
//        SQLiteDatabase db = this.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UtilityMovement.CREATE_TABLA_MOVEMENT);
        db.execSQL(UtilityGoal.CREATE_TABLA_GOAL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS movements");
        db.execSQL("DROP TABLE IF EXISTS goals");
        onCreate(db);
    }
}
