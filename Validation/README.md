The Access database "IDS_validation_v3.accdb" runs SQL queries that validate an IDS database.  Entries in the Type and Value columns are compared to the Metadata table.  Checks are performed to be sure that entries in the INDIV_INDIV table are reciprocal.    
   
Executing the macro called "Run checks" will run all of the queries and report results.     
   
Test data have been provided in the Fam_Recon_test_data_v2.accdb file.  Links to this table should be renewed using the the Access Linked Table Manager.   
     
The "Metadata_standard" table should be linked to the latest version of IDS Metadata.   The "Metadata_local" table can be used for metadata definitions developed locally that are not part of the IDS standard.    
   
The Linked Table Manager can be used to link to data in other IDS databases in Access or other database systems.   Databases other than Access (MySQL, Posgres, Oracle, etc.) can be linked via ODBC.   
   
The queries given here may require editing before they can be used in other database systems.  While basic SQL keywords are the same in all versions of SQL, functions are often implemented differently.  These queries make extensive use of functions for dates and for parsing text, which may not be compatible with other dialects of SQL.    
