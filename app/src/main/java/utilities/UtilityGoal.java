package utilities;

public class UtilityGoal {
    public static final String DATABASE = "uPocket.db";
    public static final String TABLA_GOALS = "goals";
    public static final String ID = "id";
    public static final String META = "meta";
    public static final String PRECIO = "precio";
    public static final String CREATE_TABLA_GOAL =
            "CREATE TABLE " +TABLA_GOALS+  "("
            +ID+ " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            +META+ " TEXT NOT NULL, "
            +PRECIO+ " INTEGER NOT NULL)";
}
