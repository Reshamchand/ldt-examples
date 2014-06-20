
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

public class Lset {
	private AerospikeClient client;
	private String seedHost;
	private int port = 3000;
	private Policy policy;
	private WritePolicy writePolicy;

	public Lset(String seedHost, int port) throws AerospikeException{
		this.policy = new Policy();
		this.writePolicy = new WritePolicy();
		this.seedHost = seedHost;
		this.port = port;
		this.client = new AerospikeClient(seedHost, port);
		
	}

	public Lset(Host[] hosts) throws AerospikeException{
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
		Lset worker = new Lset(hosts);
		*/
		Lset worker = new Lset("P3", 3000);
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

		/* print '<< -----------   LSET  ASCLI EXAMPLE -------------- >> ' */
		System.out.println("<< -----------   LSET  ASCLI EXAMPLE -------------- >> ");

		/* print '<< -----------  <><><><><><><><><><> -------------- >> ' */
		System.out.println("<< -----------  <><><><><><><><><><> -------------- >> ");

		/* print 'The generic format for UDF/LDT calls is:' */
		System.out.println("The generic format for UDF/LDT calls is:");

		/* print 'ascli udf-record-apply NS SET KEY MODULE FUNCTION ARGS' */
		System.out.println("ascli udf-record-apply NS SET KEY MODULE FUNCTION ARGS");

		/* print 'Large SET Commands are:' */
		System.out.println("Large SET Commands are:");

		/* print ' (*) Status = add( topRec, ldtBinName, newValue, userModule )' */
		System.out.println(" (*) Status = add( topRec, ldtBinName, newValue, userModule )");

		/* print ' (*) Status = add_all( topRec, ldtBinName, valueList, userModule )' */
		System.out.println(" (*) Status = add_all( topRec, ldtBinName, valueList, userModule )");

		/* print ' (*) Object = get( topRec, ldtBinName, searchValue ) ' */
		System.out.println(" (*) Object = get( topRec, ldtBinName, searchValue ) ");

		/* print ' (*) Number = exists( topRec, ldtBinName, searchValue ) ' */
		System.out.println(" (*) Number = exists( topRec, ldtBinName, searchValue ) ");

		/* print ' (*) List   = scan( topRec, ldtBinName )' */
		System.out.println(" (*) List   = scan( topRec, ldtBinName )");

		/* print ' (*) List   = filter( topRec, ldtBinName, userModule, filter, fargs )' */
		System.out.println(" (*) List   = filter( topRec, ldtBinName, userModule, filter, fargs )");

		/* print ' (*) Status = remove( topRec, ldtBinName, searchValue ) ' */
		System.out.println(" (*) Status = remove( topRec, ldtBinName, searchValue ) ");

		/* print ' (*) Object = take( topRec, ldtBinName, searchValue ) ' */
		System.out.println(" (*) Object = take( topRec, ldtBinName, searchValue ) ");

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

		/* print 'Add some elements into the Large Set.' */
		System.out.println("Add some elements into the Large Set.");

		/* execute lset.add('LSET_BIN',55) on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"lset", "add" );
		System.out.println("UDF result: " + result);

		/* execute lset.add('LSET_BIN', 5) on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"lset", "add" );
		System.out.println("UDF result: " + result);

		/* execute lset.add('LSET_BIN',25) on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"lset", "add" );
		System.out.println("UDF result: " + result);

		/* execute lset.add('LSET_BIN',75) on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"lset", "add" );
		System.out.println("UDF result: " + result);

		/* execute lset.add('LSET_BIN',15) on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"lset", "add" );
		System.out.println("UDF result: " + result);

		/* execute lset.add('LSET_BIN',45) on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"lset", "add" );
		System.out.println("UDF result: " + result);

		/* execute lset.add('LSET_BIN',35) on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"lset", "add" );
		System.out.println("UDF result: " + result);

		/* print 'get element 25' */
		System.out.println("get element 25");

		/* execute lset.get('LSET_BIN', 25) on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"lset", "get" );
		System.out.println("UDF result: " + result);

		/* print 'Scan the LDT' */
		System.out.println("Scan the LDT");

		/* execute lset.scan('LSET_BIN') on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"lset", "scan" );
		System.out.println("UDF result: " + result);

		/* print 'Get the size of the LDT' */
		System.out.println("Get the size of the LDT");

		/* execute lset.size('LSET_BIN') on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"lset", "size" );
		System.out.println("UDF result: " + result);

		/* print 'Remove element 45 from the LDT' */
		System.out.println("Remove element 45 from the LDT");

		/* execute lset.remove('LSET_BIN', 45) on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"lset", "remove" );
		System.out.println("UDF result: " + result);

		/* print 'Scan the LDT (to verify it was removed)' */
		System.out.println("Scan the LDT (to verify it was removed)");

		/* execute lset.scan('LSET_BIN') on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"lset", "scan" );
		System.out.println("UDF result: " + result);

		/* print 'Look at the configuration of the LDT' */
		System.out.println("Look at the configuration of the LDT");

		/* execute lset.get_config('LSET_BIN') on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"lset", "get_config" );
		System.out.println("UDF result: " + result);

		/* print 'Get the capacity of the LDT' */
		System.out.println("Get the capacity of the LDT");

		/* execute lset.get_capacity('LSET_BIN') on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"lset", "get_capacity" );
		System.out.println("UDF result: " + result);

		/* print 'Remove the LDT' */
		System.out.println("Remove the LDT");

		/* execute lset.destroy('LSET_BIN') on test.demo where pk = 'Key_1' */
		result = client.execute(this.policy, 
			new Key("test", "demo", Value.get("Key_1")), 
			"lset", "destroy" );
		System.out.println("UDF result: " + result);

		/* print ' <><><> ALL DONE!! <><><>' */
		System.out.println(" <><><> ALL DONE!! <><><>");


		
		// AQL statements - finish, total: 44
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
