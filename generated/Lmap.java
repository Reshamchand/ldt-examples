
import gnu.crypto.util.Base64;

import java.io.File;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.AerospikeException;
import com.aerospike.client.Bin;
import com.aerospike.client.Host;
import com.aerospike.client.Info;
import com.aerospike.client.Key;
import com.aerospike.client.Language;
import com.aerospike.client.Record;
import com.aerospike.client.ScanCallback;
import com.aerospike.client.Value;
import com.aerospike.client.policy.Policy;
import com.aerospike.client.policy.ScanPolicy;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.client.query.Filter;
import com.aerospike.client.query.IndexType;
import com.aerospike.client.query.RecordSet;
import com.aerospike.client.query.ResultSet;
import com.aerospike.client.query.Statement;
import com.aerospike.client.task.RegisterTask;
import com.aerospike.client.task.IndexTask;
import com.aerospike.client.cluster.Node;
import com.aerospike.client.lua.LuaConfig;
import com.aerospike.client.policy.ClientPolicy;

public class Lmap {
	private AerospikeClient client;
	private String seedHost;
	private int port = 3000;
	private Policy policy;
	private WritePolicy writePolicy;

	public Lmap(String seedHost, int port) throws AerospikeException{
		this.policy = new Policy();
		this.writePolicy = new WritePolicy();
		this.seedHost = seedHost;
		this.port = port;
		this.client = new AerospikeClient(seedHost, port);
		
	}

	public Lmap(Host[] hosts) throws AerospikeException{
		this.policy = new Policy();
		this.writePolicy = new WritePolicy();
		this.seedHost = hosts[0].name;
		this.port = hosts[0].port;
		this.client = new AerospikeClient(new ClientPolicy(), hosts);
		
	}

	public static void main(String[] args) throws AerospikeException{
		/*
		Host[] hosts = new Host[] {new Host("a.host", 3000),
									new Host("another.host", 3000),
									new Host("and.another.host", 300)};
		Lmap worker = new Lmap(hosts);
		*/
		Lmap worker = new Lmap("P3", 3000);
		worker.run();
	}
	public void run() throws AerospikeException {
		Bin[] bins = null;
		Key key = null; 
		ScanPolicy scanPolicy = null;
		Record record = null;
		RecordSet recordSet = null;
		ResultSet resultSet = null;
		Statement stmt = null;	
		File udfFile = null;
		RegisterTask task =	null;
		IndexTask indexTask = null;
		Object result;
		LuaConfig.SourceDirectory = "udf"; // change this to match your UDF directory 
		String udfString;
		String[] udfparts;
		
		// AQL statements - start
		
		/* print '<< -----------  <><><><><><><><><><> -------------- >> ' */
		System.out.println("<< -----------  <><><><><><><><><><> -------------- >> ");

		/* print '<< -----------   LMAP  AQL EXAMPLE ---------------- >> ' */
		System.out.println("<< -----------   LMAP  AQL EXAMPLE ---------------- >> ");

		/* print '<< -----------  <><><><><><><><><><> -------------- >> ' */
		System.out.println("<< -----------  <><><><><><><><><><> -------------- >> ");

		/* print 'The generic format for UDF/LDT calls is:' */
		System.out.println("The generic format for UDF/LDT calls is:");

		/* print 'execute MODULE.FUNCTION(ARGS) on NS.SET where pk = KEY' */
		System.out.println("execute MODULE.FUNCTION(ARGS) on NS.SET where pk = KEY");

		/* print 'Large MAP Commands are:' */
		System.out.println("Large MAP Commands are:");

		/* print '(*) Status = put( topRec, ldtBinName, newName, newValue, userModule) ' */
		System.out.println("(*) Status = put( topRec, ldtBinName, newName, newValue, userModule) ");

		/* print '(*) Status = put_all( topRec, ldtBinName, nameValueMap, userModule)' */
		System.out.println("(*) Status = put_all( topRec, ldtBinName, nameValueMap, userModule)");

		/* print '(*) List   = get( topRec, ldtBinName, searchName )' */
		System.out.println("(*) List   = get( topRec, ldtBinName, searchName )");

		/* print '(*) List   = scan( topRec, ldtBinName )' */
		System.out.println("(*) List   = scan( topRec, ldtBinName )");

		/* print '(*) List   = filter( topRec, ldtBinName, userModule, filter, fargs )' */
		System.out.println("(*) List   = filter( topRec, ldtBinName, userModule, filter, fargs )");

		/* print '(*) Object = remove( topRec, ldtBinName, searchName )' */
		System.out.println("(*) Object = remove( topRec, ldtBinName, searchName )");

		/* print '(*) Status = destroy( topRec, ldtBinName )' */
		System.out.println("(*) Status = destroy( topRec, ldtBinName )");

		/* print '(*) Number = size( topRec, ldtBinName )' */
		System.out.println("(*) Number = size( topRec, ldtBinName )");

		/* print '(*) Map    = get_config( topRec, ldtBinName )' */
		System.out.println("(*) Map    = get_config( topRec, ldtBinName )");

		/* print '(*) Status = set_capacity( topRec, ldtBinName, new_capacity)' */
		System.out.println("(*) Status = set_capacity( topRec, ldtBinName, new_capacity)");

		/* print '(*) Status = get_capacity( topRec, ldtBinName )' */
		System.out.println("(*) Status = get_capacity( topRec, ldtBinName )");

		/* print 'Add some elements into the Large Map.' */
		System.out.println("Add some elements into the Large Map.");

		/* execute lmap.put('LMAP_BIN', 55, '55 string') on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"lmap", "put" );
		System.out.println("UDF result: " + result);

		/* execute lmap.put('LMAP_BIN',  5, '05 string') on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"lmap", "put" );
		System.out.println("UDF result: " + result);

		/* execute lmap.put('LMAP_BIN', 25, '25 string') on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"lmap", "put" );
		System.out.println("UDF result: " + result);

		/* execute lmap.put('LMAP_BIN', 75, '75 string') on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"lmap", "put" );
		System.out.println("UDF result: " + result);

		/* execute lmap.put('LMAP_BIN', 15, '15 string') on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"lmap", "put" );
		System.out.println("UDF result: " + result);

		/* execute lmap.put('LMAP_BIN', 45, '45 string') on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"lmap", "put" );
		System.out.println("UDF result: " + result);

		/* execute lmap.put('LMAP_BIN', 35, '35 string') on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"lmap", "put" );
		System.out.println("UDF result: " + result);

		/* print 'Find element 25' */
		System.out.println("Find element 25");

		/* execute lmap.get('LMAP_BIN', 25) on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"lmap", "get" );
		System.out.println("UDF result: " + result);

		/* print 'Scan the LDT' */
		System.out.println("Scan the LDT");

		/* execute lmap.scan('LMAP_BIN') on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"lmap", "scan" );
		System.out.println("UDF result: " + result);

		/* print 'Get the size of the LDT' */
		System.out.println("Get the size of the LDT");

		/* execute lmap.size('LMAP_BIN') on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"lmap", "size" );
		System.out.println("UDF result: " + result);

		/* print 'Remove element 45' */
		System.out.println("Remove element 45");

		/* execute lmap.remove('LMAP_BIN', 45) on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"lmap", "remove" );
		System.out.println("UDF result: " + result);

		/* print 'Scan the LDT (to verify it was removed)' */
		System.out.println("Scan the LDT (to verify it was removed)");

		/* execute lmap.scan('LMAP_BIN') on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"lmap", "scan" );
		System.out.println("UDF result: " + result);

		/* print 'Look at the configuration of the LDT' */
		System.out.println("Look at the configuration of the LDT");

		/* execute lmap.get_config('LMAP_BIN') on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"lmap", "get_config" );
		System.out.println("UDF result: " + result);

		/* print 'Get the capacity of the LDT' */
		System.out.println("Get the capacity of the LDT");

		/* execute lmap.get_capacity('LMAP_BIN') on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"lmap", "get_capacity" );
		System.out.println("UDF result: " + result);

		/* print 'Remove the LDT' */
		System.out.println("Remove the LDT");

		/* execute lmap.destroy('LMAP_BIN') on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"lmap", "destroy" );
		System.out.println("UDF result: " + result);

		/* print ' <><><> ALL DONE!! <><><>' */
		System.out.println(" <><><> ALL DONE!! <><><>");


		
		// AQL statements - finish, total: 42
	}
	
	protected void finalize() throws Throwable {
	    if (this.client != null) {
	        this.client.close();
	        this.client = null;
	    }
	}
	protected void printInfo(String title, String infoString){
		if (infoString == null){
			System.out.println("Null info string");
			return;
		}
		String[] outerParts = infoString.split(";");
		System.out.println(title);
		for (String s : outerParts){

			String[] innerParts = s.split(":");
			for (String parts : innerParts){
				System.out.println("\t" + parts);
			}
			System.out.println();
		}
		
	}
	protected String infoAll(String cmd) throws AerospikeException{
		Node[] nodes = client.getNodes();
		StringBuilder results = new StringBuilder();
		for (Node node : nodes){
			results.append(Info.request(node.getHost().name, node.getHost().port, cmd)).append("\n");
		}
		return results.toString();
	}
}
