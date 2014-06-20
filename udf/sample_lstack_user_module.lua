-- ======================================================================
-- This is an example "userModule" for LDTs. This userModule does
-- several things:
-- (1) It specifies the package for configuring this LDT.
-- (2) It defines two different filters that may be applied during
--     queries for this LDT.
-- (3) It defines a transform() and untransform() function
--
-- We will perform the following steps in this file:
-- (1) Define a TABLE ("userModule") that will hold the functions we're
--     exporting to the LDT code that will call it at the appropriate time.
-- (2) Create the "adjust_settings()" function, which is a specifically
--     named function that the LDT code calls to configure the control
--     settings (either call a packaged set, or make individual calls).
-- (3) Create a general-purpose filter function ("rangeFilter2") that can be
--     called in a read operation (such as find(), range() and filter() ) to
--     apply additional tests to the elements retrieved from the Large Stack.
--     rangeFilter2() employs a predicate that is expressed as a JSON object
--     and performs tests in a given Map Object for every field specified
--     in the arglist JSON object.
-- (4) Create a second filter function that looks for any person object
--     where the first name is "Peter", and the hobby is "Helicopters".
--
-- ======================================================================

-- Must define a table that holds the functions that will be exported
-- and used by the LDT instance.
local userModule = {};

-- Import the functions we will be using in Large Stack to perform the
-- LSTACK LDT configuration.
local lstack_settings = require('ldt/settings_lstack');

-- ======================================================================
-- This is a specially named function "adjust_settings()" that the LDT
-- configure code looks for.  If an "adjust_settings()" function exists
-- in this module (it's a Lua Table entry), it will be called, with the
-- ldtMap as a parameter, to initialize this LDT instance on either a
-- create call or on the first insert.
-- The naming convention is Storage type first -- and here we are using
-- "LIST" storage, as opposed to "BINARY" storage.
-- Second, the type of data item being stored, and in this case it is
-- Medium-size Object.
-- ======================================================================
function userModule.adjust_settings( ldtMap )
  lstack_settings.use_package( ldtMap, "ListMediumObject" );
end


-- ======================================================================
-- Function rangeFilter2: Performs a range query on one or more of the
-- entries in the list.
-- Parms (encased in arglist)
-- (1) arglist (Should include comparison details and parms)
-- (2) dbObject: The object we're comparing against
--
-- rangeFilter2() will contain a LIST of MAPs, where each map contains
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
function userModule.rangeFilter2( dbObject, arglist )
  local meth = "rangeFilter2()";
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

end -- rangeFilter2()

-- ======================================================================
-- Function flyingPeterFilter: Performs a query on a set of person objects
-- looking for certain exact matches.
-- (1) arglist (Should include comparison details and parms)
-- (2) dbObject: Ignored.
--
-- flyingPeterFilter() does not take any inputs, as it is an example of a
-- specifically targetted filter that does one specific thing.  Notice that
-- this function could be made general with passed in parameters.
-- 
-- Return:
-- FILTER OK: Return Object
-- FILTER NO: Return nil.
-- ======================================================================
function userModule.flyingPeterFilter( dbObject, arglist )
  local meth = "flyingPeterFilter()";
  local result = true;

  GP=F and trace("[ENTER]: <%s:%s> ArgList(%s) dbObject(%s)",
                MOD, meth, tostring(arglist), tostring(dbObject));

  -- Check for valid objects, but do not complain if we get handed something
  -- else;  If bad inputs, just return quietly.
  if ( dbObject == nil or type(dbObject) ~= "userdata" ) then
    return nil;
  end

  -- This function does a very specific thing.  It looks to see if there's
  -- a "FirstName" field, and if the value is "Peter".  ALSO, in the Hobbies
  -- Field, if that contains either an entry or a LIST that contains an entry
  -- called "Helicopter", then we return the object. 
  local nameMatch = false;
  local hobbyMatch = false;
  local fieldMap;
  local dbValue;
  local hobby;
  for name, value in map.pairs( dbObject ) do
    -- Check for Name match
    if ( type(name) == "string" ) then
      if ( name == "FirstName" ) then
        if ( type(value) == "string" and value == "Peter" ) then
          if ( hobbyMatch ) then
            dbObject.Rating = "High"; -- An exceptional person.
            return dbObject;
          end
          nameMatch = true;
        end -- if Peter match
      elseif ( name == "Hobbies" ) then
        -- Process the Hobbies field.  It should be a list, but
        -- it might just be a single name string.  Check for both.
        if ( type(value) == "string" ) then
          if ( value == "Helicopter" ) then
            if ( nameMatch ) then
              dbObject.Rating = "High"; -- An exceptional person.
              return dbObject;
            end
            hobbyMatch = true;
          end
        elseif ( type(value) == "userdata" ) then
          -- Process the Hobbies LIST.
          for i = 1, list.size( value ), 1 do
            hobby = value[i];
            if ( hobby == "Helicopter" ) then
              dbObject.Rating = "High"; -- An exceptional person.
              return dbObject;
            end
            hobbyMatch = true;
          end -- for every entry in Hobby List
        end -- user data processing
      end -- Hobbies Field
    end -- end name is string type
  end -- for each pair in the dbObject Map

  GP=F and trace("[EXIT]: <%s:%s> Did not match", MOD, meth );
  return nil

end -- flyingPeterFilter()

-- ======================================================================
-- Return the value of this module's table so that others importing this
-- module get the table reference.
-- ======================================================================
return userModule;

-- <EOF> -- <EOF> -- <EOF> -- <EOF> -- <EOF> -- <EOF> -- <EOF> -- <EOF> --
