package studia.ium.warehouseandroidclient.application.configuration.database.migrations;

import androidx.room.migration.Migration;

public class AllMigrations {
    public static Migration[] get() {
        return new Migration[]{
            Migration1_2.get()
        };
    }
}
