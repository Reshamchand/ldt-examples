echo "<< -----------  <><><><><><><><><><> -------------- >> "
echo "<< -----------   LMAP  ASCLI EXAMPLE -------------- >> "
echo "<< -----------  <><><><><><><><><><> -------------- >> "

echo "The generic format for UDF/LDT calls is:"
echo "ascli udf-record-apply NS SET KEY MODULE FUNCTION ARGS"

echo "Large MAP Commands are:"
echo "(*) Status = put( topRec, ldtBinName, newName, newValue, userModule) "
echo "(*) Status = put_all( topRec, ldtBinName, nameValueMap, userModule)"
echo "(*) List   = get( topRec, ldtBinName, searchName )"
echo "(*) List   = scan( topRec, ldtBinName )"
echo "(*) List   = filter( topRec, ldtBinName, userModule, filter, fargs )"
echo "(*) Object = remove( topRec, ldtBinName, searchName )"
echo "(*) Status = destroy( topRec, ldtBinName )"
echo "(*) Number = size( topRec, ldtBinName )"
echo "(*) Map    = get_config( topRec, ldtBinName )"
echo "(*) Status = set_capacity( topRec, ldtBinName, new_capacity)"
echo "(*) Status = get_capacity( topRec, ldtBinName )"

echo "Add some elements into the Large Map."
set -v
ascli udf-record-apply "test" "set" "Key_1" "lmap" "put" "LMAP_BIN" 55 "55 string"
ascli udf-record-apply "test" "set" "Key_1" "lmap" "put" "LMAP_BIN"  5 "05 string"
ascli udf-record-apply "test" "set" "Key_1" "lmap" "put" "LMAP_BIN" 25 "25 string"
ascli udf-record-apply "test" "set" "Key_1" "lmap" "put" "LMAP_BIN" 75 "75 string"
ascli udf-record-apply "test" "set" "Key_1" "lmap" "put" "LMAP_BIN" 15 "15 string"
ascli udf-record-apply "test" "set" "Key_1" "lmap" "put" "LMAP_BIN" 45 "45 string"
ascli udf-record-apply "test" "set" "Key_1" "lmap" "put" "LMAP_BIN" 35 "35 string"
set +v

echo "Find element 25"
ascli udf-record-apply "test" "set" "Key_1" "lmap" "get" "LMAP_BIN" 25

echo "Scan the LDT"
ascli udf-record-apply "test" "set" "Key_1" "lmap" "scan" "LMAP_BIN"

echo "Get the size of the LDT"
ascli udf-record-apply "test" "set" "Key_1" "lmap" "size" "LMAP_BIN"

echo "Remove element 45"
ascli udf-record-apply "test" "set" "Key_1" "lmap" "remove" "LMAP_BIN" 45

echo "Scan the LDT (to verify it was removed)"
ascli udf-record-apply "test" "set" "Key_1" "lmap" "scan" "LMAP_BIN"

echo "Look at the configuration of the LDT"
ascli udf-record-apply "test" "set" "Key_1" "lmap" "get_config" "LMAP_BIN"

echo "Get the capacity of the LDT"
ascli udf-record-apply "test" "set" "Key_1" "lmap" "get_capacity" "LMAP_BIN"

echo "Remove the LDT"
ascli udf-record-apply "test" "set" "Key_1" "lmap" "destroy" "LMAP_BIN"

echo " <><><> ALL DONE!! <><><>"
