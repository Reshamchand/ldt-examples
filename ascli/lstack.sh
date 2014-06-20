echo "<< -----------  <><><><><><><><><><><><> -------------- >> "
echo "<< -----------    LSTACK  ASCLI EXAMPLE  -------------- >> "
echo "<< -----------  <><><><><><><><><><><><> -------------- >> "

echo "The generic format for UDF/LDT calls is:"
echo "ascli udf-record-apply NS SET KEY MODULE FUNCTION ARGS"

echo "Large STACK Commands are:"
echo " (*) Status = push( topRec, ldtBinName, newValue, userModule )"
echo " (*) Status = push_all( topRec, ldtBinName, valueList, userModule )"
echo " (*) List   = peek( topRec, ldtBinName, peekCount ) "
echo " (*) List   = pop( topRec, ldtBinName, popCount ) "
echo " (*) List   = scan( topRec, ldtBinName )"
echo " (*) List   = filter( topRec, ldtBinName, peekCount,userModule,filter,fargs)"
echo " (*) Status = destroy( topRec, ldtBinName )"
echo " (*) Number = size( topRec, ldtBinName )"
echo " (*) Map    = get_config( topRec, ldtBinName )"
echo " (*) Status = set_capacity( topRec, ldtBinName, new_capacity)"
echo " (*) Status = get_capacity( topRec, ldtBinName )"

echo "Add some elements into the LDT"
set -v
ascli udf-record-apply "test" "demo" "Key_1" "lstack" "push" "LSTACK_BIN" 55
ascli udf-record-apply "test" "demo" "Key_1" "lstack" "push" "LSTACK_BIN"  5
ascli udf-record-apply "test" "demo" "Key_1" "lstack" "push" "LSTACK_BIN" 25
ascli udf-record-apply "test" "demo" "Key_1" "lstack" "push" "LSTACK_BIN" 75
ascli udf-record-apply "test" "demo" "Key_1" "lstack" "push" "LSTACK_BIN" 15
ascli udf-record-apply "test" "demo" "Key_1" "lstack" "push" "LSTACK_BIN" 45
ascli udf-record-apply "test" "demo" "Key_1" "lstack" "push" "LSTACK_BIN" 35
set +v

echo "get one element from the large stack"
ascli udf-record-apply "test" "demo" "Key_1" "lstack" "peek" "LSTACK_BIN" 1

echo "Scan the LDT"
ascli udf-record-apply "test" "demo" "Key_1" "lstack" "scan" "LSTACK_BIN"

echo "Get the size of the LDT"
ascli udf-record-apply "test" "demo" "Key_1" "lstack" "size" "LSTACK_BIN"

echo "Look at the configuration of the LDT"
ascli udf-record-apply "test" "demo" "Key_1" "lstack" "get_config" "LSTACK_BIN"

echo "Get the capacity of the LDT"
ascli udf-record-apply "test" "demo" "Key_1" "lstack" "get_capacity" "LSTACK_BIN"

echo "Remove the LDT"
ascli udf-record-apply "test" "demo" "Key_1" "lstack" "destroy" "LSTACK_BIN"

echo " <><><> ALL DONE!! <><><>"

