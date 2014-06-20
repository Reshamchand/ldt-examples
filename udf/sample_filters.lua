
-- Define the table that will hold the functions to be exported.
local userModule = {};

-- ======================================================================
-- Function rangeFilter1: Performs a range query on one or more of the
-- entries in the list.
-- Parms (encased in arglist)
-- (1) arglist (Should include comparison details and parms)
-- (2) dbObject: The object we're comparing against
--
-- rangeFilter1() will contain a LIST of MAPs, where each map contains
-- the data we need to evaluate each field:
-- (*) map.FieldName
-- (*) map.MinValue
-- (*) map.MaxValue
-- We use the Less Than or Equal Operator and Greater Than or Equal Operator
-- for this general-purpose range filter. Obviously, if a user wants
-- something else -- they should write their own specific filter, which will
-- no doubt be faster as well.
-- Return:
-- FILTER OK: Return Object
-- FILTER NO: Return nil.
-- ======================================================================
function userModule.rangeFilter1( dbObject, arglist )
  local meth = "rangeFilter1()";
  local result = true;

  GP=F and trace("[ENTER]: <%s:%s> ArgList(%s) dbObject(%s)",
                MOD, meth, tostring(arglist), tostring(dbObject));

  -- Check the "arglist" object -=- it must not be goofy.
  if( type( arglist ) ~= "userdata" ) then
    warn("[ERROR]<%s:%s> arglist is wrong type(%s)", MOD, meth, type(arglist));
    error( ldte.ERR_INTERNAL );
  end

  -- Iterate thru the parameters for each field
  local fieldMap;
  local dbValue;
  for i = 1, list.size( arglist ), 1 do
    fieldMap = arglist[i];
    if( fieldMap.FieldName == nil ) then
      warn("[ERROR]<%s:%s> FieldName is nil, iteration(%d)", MOD, meth, i );
      error( ldte.ERR_INTERNAL );
    end
    dbValue = dbObject[ fieldMap.FieldName ];
    if( dbValue == nil ) then
      result = false;
      break;
    end
    if( type(dbValue) == "userdata" ) then
      warn("[ERROR]<%s:%s> FieldName(%s)must be an atomic val", MOD, meth,
        tostring( fieldMap.FieldName ) )
      error( ldte.ERR_INTERNAL );
    end

    local lowVal = fieldMap.MinValue;
    local lowResult = (lowVal == nil) or (dbValue >= lowVal );
    local hiVal = fieldMap.MaxValue;
    local hiResult = (hiVal == nil) or (dbValue <= hiVal );

    if not( lowResult and hiResult ) then 
      result = false;
      break
    end
  end -- for each term in arglist
  
  GP=F and trace("[EXIT]: <%s:%s> Result(%s) \n", MOD, meth, tostring(result));
  if result then
    return dbObject
  else
    return nil
  end

end -- rangeFilter1()

-- ======================================================================
-- Export the Table for those who want to import this module and its
-- functions.
-- ======================================================================
return userModule;


