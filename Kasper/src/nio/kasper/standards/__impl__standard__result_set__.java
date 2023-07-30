/*

The following describes how to parse result sets.
Result sets are in the following format:

    [
    { "exception" : "QueryError104: exception message here" },
    { "result" : {} , "path" : ""}
    ]

It is formatted as a list of maps, with the maps containing either "Exception"
and "Result".

These exceptions are not raised until the resultSet.next() is called.
Of course, these result sets
 */