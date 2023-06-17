package org.geotools.data.kingbase;/**
 * @author simba@onlying.cn
 * @date 2023年6月17日 下午6:29
 */

import org.geotools.data.store.ContentFeatureSource;
import org.geotools.jdbc.JDBCDataStore;

import java.io.IOException;
import java.util.HashMap;

/**
 * author: leek
 * dataTime: 2023年6月17日 下午6:29
 */
public class Test {

    public static void main(String[] args) throws IOException {
        HashMap<String, Object> hashMap = new HashMap<>();

        JDBCDataStore dataStore = new PostgisNGDataStoreFactory().createDataStore(hashMap);

        String databaseSchema = dataStore.getDatabaseSchema();

        ContentFeatureSource featureSource = dataStore.getFeatureSource("");

    }
}
