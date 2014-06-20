
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

public class Llist {
	private AerospikeClient client;
	private String seedHost;
	private int port = 3000;
	private Policy policy;
	private WritePolicy writePolicy;

	public Llist(String seedHost, int port) throws AerospikeException{
		this.policy = new Policy();
		this.writePolicy = new WritePolicy();
		this.seedHost = seedHost;
		this.port = port;
		this.client = new AerospikeClient(seedHost, port);
		
	}

	public Llist(Host[] hosts) throws AerospikeException{
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
		Llist worker = new Llist(hosts);
		*/
		Llist worker = new Llist("P3", 3000);
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

		/* print '<< -----------   LLIST AQL EXAMPLE ---------------- >> ' */
		System.out.println("<< -----------   LLIST AQL EXAMPLE ---------------- >> ");

		/* print '<< -----------  <><><><><><><><><><> -------------- >> ' */
		System.out.println("<< -----------  <><><><><><><><><><> -------------- >> ");

		/* print 'The generic format for UDF/LDT calls is:' */
		System.out.println("The generic format for UDF/LDT calls is:");

		/* print 'ascli udf-record-apply NS SET KEY MODULE FUNCTION ARGS' */
		System.out.println("ascli udf-record-apply NS SET KEY MODULE FUNCTION ARGS");

		/* print 'Large List Commands are:' */
		System.out.println("Large List Commands are:");

		/* print ' (*) Status = add( topRec, ldtBinName, newValue, userModule )' */
		System.out.println(" (*) Status = add( topRec, ldtBinName, newValue, userModule )");

		/* print ' (*) Status = add_all( topRec, ldtBinName, valueList, userModule )' */
		System.out.println(" (*) Status = add_all( topRec, ldtBinName, valueList, userModule )");

		/* print ' (*) List   = find( topRec, bin, value, module, filter, fargs )' */
		System.out.println(" (*) List   = find( topRec, bin, value, module, filter, fargs )");

		/* print ' (*) List   = scan( topRec, ldtBinName )' */
		System.out.println(" (*) List   = scan( topRec, ldtBinName )");

		/* print ' (*) List   = filter( topRec, ldtBinName, userModule, filter, fargs )' */
		System.out.println(" (*) List   = filter( topRec, ldtBinName, userModule, filter, fargs )");

		/* print ' (*) Status = remove( topRec, ldtBinName, searchValue ) ' */
		System.out.println(" (*) Status = remove( topRec, ldtBinName, searchValue ) ");

		/* print ' (*) Status = destroy( topRec, ldtBinName )' */
		System.out.println(" (*) Status = destroy( topRec, ldtBinName )");

		/* print ' (*) Number = size( topRec, ldtBinName )' */
		System.out.println(" (*) Number = size( topRec, ldtBinName )");

		/* print ' (*) Map    = get_config( topRec, ldtBinName )' */
		System.out.println(" (*) Map    = get_config( topRec, ldtBinName )");

		/* print ' (*) Status = set_capacity( topRec, ldtBinName, new_capacity)' */
		System.out.println(" (*) Status = set_capacity( topRec, ldtBinName, new_capacity)");

		/* print ' (*) Status = get_capacity( topRec, ldtBinName )' */
		System.out.println(" (*) Status = get_capacity( topRec, ldtBinName )");

		/* print 'Add some elements into the LDT' */
		System.out.println("Add some elements into the LDT");

		/* execute llist.add('LLIST_BIN', 55) on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"llist", "add" );
		System.out.println("UDF result: " + result);

		/* execute llist.add('LLIST_BIN',  5) on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"llist", "add" );
		System.out.println("UDF result: " + result);

		/* execute llist.add('LLIST_BIN', 25) on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"llist", "add" );
		System.out.println("UDF result: " + result);

		/* execute llist.add('LLIST_BIN', 75) on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"llist", "add" );
		System.out.println("UDF result: " + result);

		/* execute llist.add('LLIST_BIN', 15) on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"llist", "add" );
		System.out.println("UDF result: " + result);

		/* execute llist.add('LLIST_BIN', 45) on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"llist", "add" );
		System.out.println("UDF result: " + result);

		/* execute llist.add('LLIST_BIN', 35) on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"llist", "add" );
		System.out.println("UDF result: " + result);

		/* print 'Find element 25' */
		System.out.println("Find element 25");

		/* execute llist.find('LLIST_BIN', 25) on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"llist", "find" );
		System.out.println("UDF result: " + result);

		/* print 'Scan the LDT' */
		System.out.println("Scan the LDT");

		/* execute llist.scan('LLIST_BIN') on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"llist", "scan" );
		System.out.println("UDF result: " + result);

		/* print 'Get the size of the LDT' */
		System.out.println("Get the size of the LDT");

		/* execute llist.size('LLIST_BIN') on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"llist", "size" );
		System.out.println("UDF result: " + result);

		/* print 'Remove element 45' */
		System.out.println("Remove element 45");

		/* execute llist.remove('LLIST_BIN', 45) on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"llist", "remove" );
		System.out.println("UDF result: " + result);

		/* print 'Scan the LDT (to verify it was removed)' */
		System.out.println("Scan the LDT (to verify it was removed)");

		/* execute llist.scan('LLIST_BIN') on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"llist", "scan" );
		System.out.println("UDF result: " + result);

		/* print 'Look at the configuration of the LDT' */
		System.out.println("Look at the configuration of the LDT");

		/* execute llist.get_config('LLIST_BIN') on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"llist", "get_config" );
		System.out.println("UDF result: " + result);

		/* print 'Get the capacity of the LDT' */
		System.out.println("Get the capacity of the LDT");

		/* execute llist.get_capacity('LLIST_BIN') on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"llist", "get_capacity" );
		System.out.println("UDF result: " + result);

		/* print 'Remove the LDT' */
		System.out.println("Remove the LDT");

		/* execute llist.destroy('LLIST_BIN') on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"llist", "destroy" );
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
