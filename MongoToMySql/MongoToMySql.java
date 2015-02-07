import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.util.JSON;
import com.mysql.jdbc.PreparedStatement;

/**
 * @author KomalKishor
 *
 */
public class MongoToMySql {
	private static DB db;
	private static Connection conn;
	private static final String DRIVER = "com.mysql.jdbc.Driver";

	private static final String AH_URL = "jdbc:mysql://ec2-54-69-158-97.us-west-2.compute.amazonaws.com:3306/team12_db";
	private static final String AH_USER = "komal";
	private static final String AH_PASSWORD = "123";

	private static DB getConnection() throws UnknownHostException {
		if (db == null) {
			MongoClientURI uri = new MongoClientURI(
					"mongodb://logstashuser:Password@ds053320.mongolab.com:53320/logstash");
			MongoClient mongoClient = new MongoClient(uri);
			db = mongoClient.getDB("logstash");
		}
		return db;
	}

	public static Connection getMysqlConnection() {
		if (conn == null) {
			try {
				Class.forName(DRIVER);
				conn = DriverManager
						.getConnection(AH_URL, AH_USER, AH_PASSWORD);
				System.out.println("connected to server ");

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return conn;
	}

	private static void archivedata() throws UnknownHostException {

		try {
			DBCollection tbl = getConnection().getCollection(
					"logstashcollection");
			DBCollection atbl = getConnection().getCollection("atblname");
			DBCursor cur = tbl.find();
			while (cur.hasNext()) {
				atbl.insert(cur.next());
			}
			cur.close();
			tbl.remove(new BasicDBObject());
		} catch (Exception e) {
			System.out.println("Error in archive data:" + e.getMessage());
		}
	}

	public static String getAggregateData() throws UnknownHostException {
		DBCollection tbl = getConnection().getCollection("logstashcollection");
		String grp = "{$group:{_id:'$vmname',avgcpu:{$avg:'$cpu'},avgmemory:{$avg:'$memory'},avgdisk:{$avg:'$disk'},avgnetwork:{$avg:'$network'}}}";

		DBObject group = (DBObject) JSON.parse(grp);
		AggregationOutput output = tbl.aggregate(group);
		ArrayList<DBObject> list = (ArrayList<DBObject>) output.results();
		for (DBObject dbObject : list) {
			System.out.println(dbObject);
			insertMysql(dbObject);
		}
		archivedata();
		return "";
	}

	public static void insertMysql(DBObject obj) {
		try {

			System.out.println(obj.keySet());
			PreparedStatement st = (PreparedStatement) getMysqlConnection()
					.prepareStatement(
							"insert into test.vmstats(vmname,time_stamp,cpu_usage,memory_usage,disk_usage,network_usage) values(?,now(),?,?,?,?)");

			st.setString(1, obj.get("_id").toString());
			st.setDouble(2, Double.parseDouble(obj.get("avgcpu").toString()));
			st.setDouble(3, Double.parseDouble(obj.get("avgmemory").toString()));
			st.setDouble(4, Double.parseDouble(obj.get("avgdisk").toString()));
			st.setDouble(5,
					Double.parseDouble(obj.get("avgnetwork").toString()));
			st.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static Thread t1 = new Thread() {
		public void run() {
			while (true) {
				try {
					getAggregateData();
					Thread.sleep(300000);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};

	public static void main(String[] args) throws UnknownHostException {
		t1.start();
	}
}
