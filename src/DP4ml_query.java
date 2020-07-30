import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Will handle database query/inserts. Upon receiving location request will initiate ml prediction to
 * obtain location label.
 */
public class DP4ml_query {

    private final String SQLITE_DB_LOCATION = "jdbc:sqlite:C:/sqlite/db/test.db";
    private final String SQLITE_TABLE_NAME = "locationFeatures";

    private final String SQLT_TAG_LABEL = "label";
    private final String SQLT_TAG_WIFI = "wifi";
    private final String SQLT_TAG_LIGHT = "light";
    private final String SQLT_TAG_SOUND = "sound";
    private final String SQLT_TAG_GEOMAG = "geoMag";
    private final String SQLT_TAG_CELLTID = "cellTowerId";
    private final String SQLT_TAG_LAC = "localAreaCode";
    private final String SQLT_TAG_CTS = "cellTowerSignal";
    private final String FEATURES_STRING = "(label,wifi,light,sound,geoMag,cellTowerId,localAreaCode,cellTowerSignal)";

    private final int FEATURES_START_INDEX = 2; // 1 is where label is so features start at 2
    private final int FEATURES_END_INDEX = 9; //one more than the number of features

    /**
     * Connects to current sqlite database and returns the conection object
     * @return Connection
     */
    private Connection connectToSqlite(){
        Connection dbConnection  = null;
        try{
            dbConnection = DriverManager.getConnection(SQLITE_DB_LOCATION);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return dbConnection;
    }

    /**
     * Creates the location data table to hold features, will probably be called once to setup
     * initial table.
     */
    private void createDataTable(){
        String tableCreationString = "CREATE TABLE IF NOT EXISTS locationFeatures (\n"
                                    + "     id integer PRIMARY KEY,\n"
                                    + "     label TEXT,\n"
                                    + "     wifi DOUBLE(32,4),\n"
                                    + "     light DOUBLE(32,4),\n"
                                    + "     sound DOUBLE(32,4),\n"
                                    + "     geoMag DOUBLE(32,4),\n"
                                    + "     cellTowerId DOUBLE(32,4),\n"
                                    + "     localAreaCode DOUBLE(32,4),\n"
                                    + "     cellTowerSignal DOUBLE(32,4)\n"
                                    + ");";

        Connection connectionToDb = connectToSqlite();
        Statement tableCreationStatement = null;
        try {
            tableCreationStatement = connectionToDb.createStatement();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            System.out.println("in creating statement");
        }

        try{
            tableCreationStatement.execute(tableCreationString);
        } catch(SQLException e){
            System.out.println(e.getMessage());
            System.out.println("in executing");
        }

    }

    /**
     * Function to populate sqlite table with random data
     */
    private void populateTableWithRandom(){
        DP4_ml mlObj = new DP4_ml();
        List<LabeledFeatures> samples = mlObj.samples;
        for(LabeledFeatures element:samples){
            insertElemToTable(element);
        }
    }

    /**
     * Function tool to insert a LabeledFeatures object into the database
     * @param o LabeledFeature object to be inserted
     */
    private void insertElemToTable(Object o){
        if(!(o instanceof LabeledFeatures)) return;
        if(o == null) return;

        LabeledFeatures input = (LabeledFeatures) o;
        String insertStatementString = "INSERT INTO " + SQLITE_TABLE_NAME + FEATURES_STRING +
                                " VALUES(?,?,?,?,?,?,?,?)";
        Connection dbConnection = this.connectToSqlite();

        try{
            PreparedStatement insertStatement = dbConnection.prepareStatement(insertStatementString);
            List<Double> featureList = input.features;
            for(int i=0;i<featureList.size();i++){
                if(i==0){
                    insertStatement.setString(i+1,input.label);
                }
                insertStatement.setDouble(i+1,featureList.get(i));
            }
            insertStatement.executeUpdate();
        } catch(SQLException e){

        }

    }

    //querying for info:
    private void queryByLabel(String label){

    }

    /**
     * Obtains all of the available data in database then runs ml algos on them along with
     * input to figure which location the input may be.
     * @param input location features vector
     */
    private void queryByFeatures(LabeledFeatures input){
        List<LabeledFeatures> allSamples = this.getAllSamplesFromDataBase();

        //... from here the machine learning algorithms will be called.
    }

    /**
     * Will query the database for all the samples and return them as a list of labeled features.
     * Each labeled features object is a row or sample.
     * @return List<LabeledFeatures> all data samples
     */
    private List<LabeledFeatures> getAllSamplesFromDataBase(){
        List<LabeledFeatures> databaseSamples = new ArrayList<>();

        String dbRequest = "SELECT label,wifi,light,sound,geoMag,cellTowerId,localAreaCode,cellTowerSignal "+
                "FROM " + SQLITE_TABLE_NAME;
        Connection dbConnection = this.connectToSqlite();

        try{
            PreparedStatement dbRequestStatement = dbConnection.prepareStatement(dbRequest);
            ResultSet queryResults = dbRequestStatement.executeQuery();
            while(queryResults.next()){
                LabeledFeatures sampleRow = new LabeledFeatures(queryResults.getString(SQLT_TAG_LABEL));
                for(int i=FEATURES_START_INDEX;i<FEATURES_END_INDEX;i++){
                    sampleRow.features.add(queryResults.getDouble(i));
                }
                databaseSamples.add(sampleRow);
            }
        } catch (SQLException e){
            System.out.println("problem with mass query");
        }

        return databaseSamples;
    }

    public static void main(String[] args){

    }
}
