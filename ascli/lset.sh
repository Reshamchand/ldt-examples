echo "<< -----------  <><><><><><><><><><> -------------- >> "
echo "<< -----------   LSET  ASCLI EXAMPLE -------------- >> "
echo "<< -----------  <><><><><><><><><><> -------------- >> "

echo "The generic format for UDF/LDT calls is:"
echo "ascli udf-record-apply NS SET KEY MODULE FUNCTION ARGS"

echo "Large SET Commands are:"
echo " (*) Status = add( topRec, ldtBinName, newValue, userModule )"
echo " (*) Status = add_all( topRec, ldtBinName, valueList, userModule )"
echo " (*) Object = get( topRec, ldtBinName, searchValue ) "
echo " (*) Number = exists( topRec, ldtBinName, searchValue ) "
echo " (*) List   = scan( topRec, ldtBinName )"
echo " (*) List   = filter( topRec, ldtBinName, userModule, filter, fargs )"
echo " (*) Status = remove( topRec, ldtBinName, searchValue ) "
echo " (*) Object = take( topRec, ldtBinName, searchValue ) "
echo " (*) Status = destroy( topRec, ldtBinName )"
echo " (*) Number = size( topRec, ldtBinName )"
echo " (*) Map    = get_config( topRec, ldtBinName )"
echo " (*) Status = set_capacity( topRec, ldtBinName, new_capacity)"
echo " (*) Status = get_capacity( topRec, ldtBinName )"


echo "Add some elements into the Large Set."
set -v
ascli udf-record-apply "test" "demo" "Key_1" "lset" "add" "LSET_BIN" 55
ascli udf-record-apply "test" "demo" "Key_1" "lset" "add" "LSET_BIN"  5
ascli udf-record-apply "test" "demo" "Key_1" "lset" "add" "LSET_BIN" 25
ascli udf-record-apply "test" "demo" "Key_1" "lset" "add" "LSET_BIN" 75
ascli udf-record-apply "test" "demo" "Key_1" "lset" "add" "LSET_BIN" 15
ascli udf-record-apply "test" "demo" "Key_1" "lset" "add" "LSET_BIN" 45
ascli udf-record-apply "test" "demo" "Key_1" "lset" "add" "LSET_BIN" 35
set +v

echo "get element 25"
ascli udf-record-apply "test" "demo" "Key_1" "lset" "get" "LSET_BIN" 25

echo "Scan the LDT"
ascli udf-record-apply "test" "demo" "Key_1" "lset" "scan" "LSET_BIN"

echo "Get the size of the LDT"
ascli udf-record-apply "test" "demo" "Key_1" "lset" "size" "LSET_BIN"

echo "Remove element 45 from the LDT"
ascli udf-record-apply "test" "demo" "Key_1" "lset" "remove" "LSET_BIN" 45

echo "Scan the LDT (to verify it was removed)"
ascli udf-record-apply "test" "demo" "Key_1" "lset" "scan" "LSET_BIN"

echo "Look at the configuration of the LDT"
ascli udf-record-apply "test" "demo" "Key_1" "lset" "get_config" "LSET_BIN"

echo "Get the capacity of the LDT"
ascli udf-record-apply "test" "demo" "Key_1" "lset" "get_capacity" "LSET_BIN"

echo "Remove the LDT"
ascli udf-record-apply "test" "demo" "Key_1" "lset" "destroy" "LSET_BIN"

echo " <><><> ALL DONE!! <><><>"
