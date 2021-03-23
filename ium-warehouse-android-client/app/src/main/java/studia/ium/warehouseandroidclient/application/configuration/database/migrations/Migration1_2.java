package studia.ium.warehouseandroidclient.application.configuration.database.migrations;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class Migration1_2 {
    public static Migration get() {
        return new Migration(1, 2) {
            @Override
            public void migrate(@NonNull SupportSQLiteDatabase database) {
                getProductTableMigration(database);
                getProductOperationTableMigration(database);
            }
        };
    }

    private static void getProductTableMigration(SupportSQLiteDatabase database) {
        database.execSQL("CREATE TABLE PRODUCT_NEW" +
            " (id TEXT NOT NULL, manufacturerName TEXT, modelName TEXT, price REAL," +
            " expirationDate INTEGER, quantitiesInWarehouses TEXT, deleted INTEGER NOT NULL," +
            " PRIMARY KEY(id))");
        database.execSQL("INSERT INTO PRODUCT_NEW" +
            " (id, manufacturerName, modelName, price, deleted)" +
            " SELECT id, manufacturerName, modelName, price, deleted FROM PRODUCT");
        database.execSQL("DROP TABLE PRODUCT");
        database.execSQL("ALTER TABLE PRODUCT_NEW RENAME TO PRODUCT");
    }

    private static void getProductOperationTableMigration(SupportSQLiteDatabase database) {
        database.execSQL("CREATE TABLE PRODUCTOPERATION_NEW" +
            " (guid TEXT NOT NULL, timestamp INTEGER, type TEXT, productId TEXT," +
            " product TEXT, productPatches TEXT, amount INTEGER, warehouseId INTEGER," +
            " PRIMARY KEY(guid))");
        database.execSQL("INSERT INTO PRODUCTOPERATION_NEW" +
            " (guid, timestamp, type, productId, product, amount)" +
            " SELECT guid, timestamp, type, productId, product, amount FROM PRODUCTOPERATION");
        database.execSQL("DROP TABLE PRODUCTOPERATION");
        database.execSQL("ALTER TABLE PRODUCTOPERATION_NEW RENAME TO PRODUCTOPERATION");
    }
}
