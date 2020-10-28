package utilities;

public class UtilityMovement {
    public static final String DATABASE = "uPocket.db";
    public static final String TABLA_MOVEMENTS = "movements";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String VALUE = "value";
    public static final String TYPE = "type";
    public static final String DATE = "date";
    public static final String FREQUENCY = "frequency";

    public static final String CREATE_TABLA_MOVEMENTS =
            "CREATE TABLE " +TABLA_MOVEMENTS+  "("
                    +ID+ " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    +NAME+ " TEXT NOT NULL, "
                    +VALUE+ " INTEGER NOT NULL, "
                    +TYPE+ " INTEGER NOT NULL, "
                    +DATE+ " DATE, "
                    +FREQUENCY+ " TEXT)";
}
