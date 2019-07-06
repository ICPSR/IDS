The Access database "Fam_Recon_checks_ver_3.accdb" runs SQL queries that check for inconsistencies in family reconstitution data.  For example, dates of birth of mothers and children are compared to find women that would have been too young or too old when the child was born.  Most of these checks will also work on data from population registers.   
   
Executing the macro called "Run checks" will run all of the queries and report results.    (Note that the "Run BDdates" macro is called by the "Run checks" macro.)   
   
Test data have been provided in the Fam_Recon_test_data_v2.accdb file.  Links to this table should be renewed using the the Access Linked Table Manager.     
   
The Linked Table Manager can be used to link to data in other IDS databases in Access or other database systems.   Databases other than Access (MySQL, Posgres, Oracle, etc.) can be linked via ODBC.   
   
The queries given here may require editing before they can be used in other database systems.  While basic SQL keywords are the same in all versions of SQL, functions are often implemented differently.  These queries make extensive use of functions for dates and for parsing text, which may not be compatible with other dialects of SQL.    
