echo "<< -----------  <><><><><><><><><><> -------------- >> "
echo "<< -----------   LLIST ASCLI EXAMPLE -------------- >> "
echo "<< -----------  <><><><><><><><><><> -------------- >> "

echo "The generic format for UDF/LDT calls is:"
echo "ascli udf-record-apply NS SET KEY MODULE FUNCTION ARGS"

echo "Large List Commands are:"
echo " (*) Status = add( topRec, ldtBinName, newValue, userModule )"
echo " (*) Status = add_all( topRec, ldtBinName, valueList, userModule )"
echo " (*) List   = find( topRec, bin, value, module, filter, fargs )"
echo " (*) List   = scan( topRec, ldtBinName )"
echo " (*) List   = filter( topRec, ldtBinName, userModule, filter, fargs )"
echo " (*) Status = remove( topRec, ldtBinName, searchValue ) "
echo " (*) Status = destroy( topRec, ldtBinName )"
echo " (*) Number = size( topRec, ldtBinName )"
echo " (*) Map    = get_config( topRec, ldtBinName )"
echo " (*) Status = set_capacity( topRec, ldtBinName, new_capacity)"
echo " (*) Status = get_capacity( topRec, ldtBinName )"


echo "Add some elements into the LDT"
set -v
ascli udf-record-apply "test" "demo" "Key_1" "llist" "add" "LLIST_BIN" 55
ascli udf-record-apply "test" "demo" "Key_1" "llist" "add" "LLIST_BIN"  5
ascli udf-record-apply "test" "demo" "Key_1" "llist" "add" "LLIST_BIN" 25
ascli udf-record-apply "test" "demo" "Key_1" "llist" "add" "LLIST_BIN" 75
ascli udf-record-apply "test" "demo" "Key_1" "llist" "add" "LLIST_BIN" 15
ascli udf-record-apply "test" "demo" "Key_1" "llist" "add" "LLIST_BIN" 45
ascli udf-record-apply "test" "demo" "Key_1" "llist" "add" "LLIST_BIN" 35
set +v

echo "Find element 25"
ascli udf-record-apply "test" "demo" "Key_1" "llist" "find" "LLIST_BIN" 25

echo "Scan the LDT"
ascli udf-record-apply "test" "demo" "Key_1" "llist" "scan" "LLIST_BIN"

echo "Get the size of the LDT"
ascli udf-record-apply "test" "demo" "Key_1" "llist" "size" "LLIST_BIN"

echo "Remove element 45"
ascli udf-record-apply "test" "demo" "Key_1" "llist" "remove" "LLIST_BIN" 45

echo "Scan the LDT (to verify it was removed)"
ascli udf-record-apply "test" "demo" "Key_1" "llist" "scan" "LLIST_BIN"

echo "Look at the configuration of the LDT"
ascli udf-record-apply "test" "demo" "Key_1" "llist" "get_config" "LLIST_BIN"

echo "Get the capacity of the LDT"
ascli udf-record-apply "test" "demo" "Key_1" "llist" "get_capacity" "LLIST_BIN"

echo "Remove the LDT"
ascli udf-record-apply "test" "demo" "Key_1" "llist" "destroy" "LLIST_BIN"

echo " <><><> ALL DONE!! <><><>"
