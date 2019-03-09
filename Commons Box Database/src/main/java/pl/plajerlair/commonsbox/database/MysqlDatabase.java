package pl.plajerlair.commonsbox.database;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Plajer
 * <p>
 * Created at 09.03.2019
 */
public class MysqlDatabase {

  private BoneCP connectionPool = null;
  private Logger databaseLogger = Logger.getLogger("CommonsBox Database");

  public MysqlDatabase(String address, String user, String password) {
    this(address, user, password, 2, 10);
  }

  public MysqlDatabase(String address, String user, String password, int minConn, int maxConn) {
    databaseLogger.log(Level.INFO, "Configuring MySQL connection!");
    configureConnPool(address, user, password, minConn, maxConn);

    Connection connection = getConnection();
    if (connection == null) {
      databaseLogger.log(Level.SEVERE, "Failed to connect to database!");
      return;
    }
    closeConnection(connection);
  }

  private void configureConnPool(String address, String user, String password, int minConn, int maxConn) {
    try {
      Class.forName("com.mysql.jdbc.Driver"); //also you need the MySQL driver
      databaseLogger.info("Creating BoneCP Configuration...");
      BoneCPConfig config = new BoneCPConfig();
      config.setJdbcUrl(address);
      config.setUsername(user);
      config.setPassword(password);
      config.setMinConnectionsPerPartition(minConn); //if you say 5 here, there will be 10 connection available
      config.setMaxConnectionsPerPartition(maxConn);
      config.setPartitionCount(2); //2*5 = 10 connection will be available
      //config.setLazyInit(true); //depends on the application usage you should chose lazy or not
      //setting Lazy true means BoneCP won't open any connections before you request a one from it.
      databaseLogger.info("Setting up MySQL Connection pool...");
      connectionPool = new BoneCP(config); // setup the connection pool
      databaseLogger.info("Connection pool successfully configured. ");
      databaseLogger.info("Total connections ==> " + connectionPool.getTotalCreatedConnections());
    } catch (Exception e) {
      e.printStackTrace();
      databaseLogger.warning("Cannot connect to MySQL database!");
      databaseLogger.warning("Check configuration of your database settings!");
    }
  }

  public void executeUpdate(String query) {
    try {
      Connection connection = getConnection();
      Statement statement = connection.createStatement();
      statement.executeUpdate(query);
      closeConnection(connection);
    } catch (SQLException e) {
      databaseLogger.warning("Failed to execute update: " + query);
    }
  }

  public ResultSet executeQuery(String query) {
    try {
      Connection connection = getConnection();
      Statement statement = connection.createStatement();
      ResultSet rs = statement.executeQuery(query);
      closeConnection(connection);
      return rs;
    } catch (SQLException exception) {
      exception.printStackTrace();
      databaseLogger.warning("Failed to execute request: " + query);
      return null;
    }
  }

  public void shutdownConnPool() {
    try {
      databaseLogger.info("Shutting down connection pool. Trying to close all connections.");
      if (connectionPool != null) {
        connectionPool.shutdown(); //this method must be called only once when the application stops.
        //you don't need to call it every time when you get a connection from the Connection Pool
        databaseLogger.info("Pool successfully shutdown. ");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Connection getConnection() {
    Connection conn = null;
    try {
      conn = getConnectionPool().getConnection();
      //will get a thread-safe connection from the BoneCP connection pool.
      //synchronization of the method will be done inside BoneCP source

    } catch (Exception e) {
      e.printStackTrace();
    }
    return conn;
  }

  public void closeConnection(Connection conn) {
    try {
      if (conn != null) {
        conn.close(); //release the connection - the name is tricky but connection is not closed it is released
        //and it will stay in pool
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public BoneCP getConnectionPool() {
    return connectionPool;
  }

}
