import org.geotools.util.Version;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation") // not yet a JUnit4 test
public class PostGIS3DTestSetup {

    static final Version V_2_0_0 = new Version("2.0.0");

    private Version version;


    public Version getVersion() throws SQLException, IOException {
        if (version == null) {
            try (Connection conn = getDataSource().getConnection();
                 Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("select " + "PostGIS_Lib_Version()")) {
                if (rs.next()) {
                    version = new Version(rs.getString(1));
                }
            }
        }

        return version;
    }

    private Statement getDataSource() {
        throw new RuntimeException("");
    }


    protected void createLine3DTable() throws Exception {
        Version version = getVersion();
        boolean atLeastV2 = version.compareTo(V_2_0_0) >= 0;
        String geometryType = atLeastV2 ? "geometry(LINESTRINGZ, 4326)" : "geometry";

        // setup table
        run(
                "CREATE TABLE \"line3d\"(\"fid\" serial PRIMARY KEY, \"id\" int, "
                        + "\"geom\" "
                        + geometryType
                        + ", \"name\" varchar )");
        if (!atLeastV2) {
            run(
                    "INSERT INTO GEOMETRY_COLUMNS VALUES('', 'public', 'line3d', 'geom', 3, '4326', 'LINESTRING')");
        }
        run("CREATE INDEX line3d_GEOM_IDX ON \"line3d\" USING GIST (\"geom\") ");

        // insert data
        run(
                "INSERT INTO \"line3d\" (\"id\",\"geom\",\"name\") VALUES (0,"
                        + "ST_GeomFromText('LINESTRING(1 1 0, 2 2 0, 4 2 1, 5 1 1)', 4326),"
                        + "'l1')");
        run(
                "INSERT INTO \"line3d\" (\"id\",\"geom\",\"name\") VALUES (1,"
                        + "ST_GeomFromText('LINESTRING(3 0 1, 3 2 2, 3 3 3, 3 4 5)', 4326),"
                        + "'l2')");
    }

    private void run(String s) {


    }


    protected void createPoint3DTable() throws Exception {
        Version version = getVersion();
        boolean atLeastV2 = version.compareTo(V_2_0_0) >= 0;
        String geometryType = atLeastV2 ? "geometry(POINTZ, 4326)" : "geometry";

        // setup table
        run(
                "CREATE TABLE \"point3d\"(\"fid\" serial PRIMARY KEY, \"id\" int, "
                        + "\"geom\" "
                        + geometryType
                        + ", \"name\" varchar )");
        if (atLeastV2) {
            run(
                    "INSERT INTO GEOMETRY_COLUMNS VALUES('', 'public', 'point3d', 'geom', 3, '4326', 'POINT')");
        }
        run("CREATE INDEX POINT3D_GEOM_IDX ON \"point3d\" USING GIST (\"geom\") ");

        // insert data
        run(
                "INSERT INTO \"point3d\" (\"id\",\"geom\",\"name\") VALUES (0,"
                        + "ST_GeomFromText('POINT(1 1 1)', 4326),"
                        + "'p1')");
        run(
                "INSERT INTO \"point3d\" (\"id\",\"geom\",\"name\") VALUES (1,"
                        + "ST_GeomFromText('POINT(3 0 1)', 4326),"
                        + "'p2')");
    }


    protected void dropLine3DTable() throws Exception {
        run("DELETE FROM  GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'line3d'");
        run("DROP TABLE \"line3d\"");
    }


    protected void dropPoly3DTable() throws Exception {
        run("DELETE FROM  GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'poly3d'");
        run("DROP TABLE \"poly3d\"");
    }


    protected void dropPoint3DTable() throws Exception {
        run("DELETE FROM  GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'point3d'");
        run("DROP TABLE \"point3d\"");
    }
}