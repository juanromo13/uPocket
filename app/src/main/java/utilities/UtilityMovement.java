package utilities;

public class UtilityMovement {
    public static final String DATABASE = "uPocket.db";
    public static final String TABLA_MOVEMENTS = "movements";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String PRECIO = "precio";
    public static final String TYPE = "type";
    public static final String DATE = "date";
    public static final String FREQUENCY = "frequency";
    public static final String CREATE_TABLA_MOVEMENT =
            "CREATE TABLE " +TABLA_MOVEMENTS+  "("
            +ID+ " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            +NAME+ " TEXT NOT NULL, "
            +PRECIO+ " INTEGER NOT NULL, "
            +TYPE+ " INTEGER NOT NULL, "
            +DATE+ " DATE, "
            +FREQUENCY+ " TEXT)";
}
