# GProM Transaction Debugger (GTDB)

The **GProM Transaction Debugger** (**GTDB**) is a Java GUI application for post-mortem debugging of transactions in Oracle. Debugging transactions is hard, in particular tracing and understanding the causes of bugs in a production environment is nearly impossible with existing tools for the following reasons: 1) after the execution of a transaction, its input is no longer available for debugging, 2) internal states of a transaction are transient and cannot be accessed, and 3) the execution of a transaction may be affected by concurrently running transactions (making it hard to reproduce a bug encountered in production in a testing environment).

GTDB is a non-invasive tool for post-mortem debugging of transactions that supports what-if scenarios  - evaluating hypothetical changes to transaction code or data. With GTDB, you can inspect what has happened during the execution of a transaction after the fact. GTDB shows the SQL code executed by the transaction and the states of tables after each statement of the transaction. Additionally, GTDB also recovers provenance for rows, i.e., data dependencies between row versions and information about which updates modified a row. GTDB can be safely user on production databases, since debugging is non-invasive - the debugger does not change the state of the database in any way and does not require transactions to be modified to enable debugging. This is made possible using reenactment, a declarative replay technique we have developed, that replays a transaction over the state of the DB seen by its original execution including all its interactions with concurrently executed transactions from the history. In turn, reenactment relies on the temporal database (Flashback archive) and audit logging (Fine-grained auditing) capabilities available in Oracle. More information about reenactment and GTDB can be found the IIT DBGroup's webpage: [GProM](http://www.cs.iit.edu/%7edbgroup/research/gprom.php).

![debug panel](https://github.com/IITDBGroup/GProMTransactionDebugger/blob/master/doc/images/debugpanel.png)

## Publications

For a full list see [http://www.cs.iit.edu/%7edbgroup/research/gprom.php](http://www.cs.iit.edu/%7edbgroup/research/gprom.php).

* **Debugging Transactions and Tracking their Provenance with Reenactment** - [PDF](http://cs.iit.edu/%7edbgroup/pdfpubls/XG17.pdf)

* **Reenactment for Read-Committed Snapshot Isolation (long version)** - [PDF](http://cs.iit.edu/%7Edbgroup/pdfpubls/AG16a.pdf)

## Installation

You need Java and `ant` for building the system. Furthermore, you need a working version of [GProM](https://github.com/IITDBGroup/GProM). 
For the debugger to work, **fine-grained auditings** (**FGA**) needs to be activated in Oracle and unless only recent transactions are debugged, the tables accessed by the debugged transactions need to be setup for time travel using Oracle's **flashback archive** feature.

### Installing Prerequisites

* Download Oracle's JDBC driver (version 7) and place the `ojdbc7.jar` file into the `lib` folder.
* Install `ant`: 

### Building the System

To build the system run ant from the main repository folder:

~~~sh
ant
~~~

This creates a fat jar file in the `build` folder.

### Installing GProM

You need a working version of **GProM** build with support for Oracle backends. For instructions on how to install GProM see [GProM repository](https://github.com/IITDBGroup/GProM).

### Configuring Oracle

TGDB needs to know the SQL code of transactions executed in the past and needs access to past database versions. We use build-in Oracle features for this purpose. However, these features have to be activated for the system to work properly. 

#### Activating Fine-grained Auditing (FGA)

TGDB requires all SQL statements of transactions to be debugged to be logged. For instructions on how to activate FGA see, e.g., [here](http://blog.yannickjaquier.com/oracle/fine-grained-auditing-fga-hands-on.html). In a nutshell you have to register audit policies that would cause SQL commands to be logged if a particular table is updated. For example, to capture activity on a table `MYTAB`

~~~sql
EXEC DBMS_FGA.ADD_POLICY(object_name=>'MYTAB',policy_name=>'updates_MYTAB',statement_types=>'update,delete,insert');
~~~

#### Activating Flashback Data Archive

TGDB also needs to be able to query past versions of a table using time travel. In Oracle that is possible per default, but past versions are only accessible for a limited about of time (until they are garbage collected). To make them available for as long as you need, you need to use flashback data archive (FDA). For further information see, e.g., [here](http://www.oracle.com/technetwork/issue-archive/2008/08-jul/o48totalrecall-092147.html). In a nutshell, you need to create an archive that will store the past versions of rows and then activate the features for each table of interest.

~~~sql
-- create an archive
CREATE FLASHBACK ARCHIVE archive_name
TABLESPACE table_space_name 
QUOTA 3G
RETENTION 12 MONTH;

-- activate archiving for table mytable
ALTER TABLE mytable FLASHBACK ARCHIVE archive_name;
~~~



### Configuring and Starting the Debugger

The file `tdebug.properties` in the `build` directory stores connection parameters for Oracle. You have to set the parameters to match your Oracle database.

~~~sh
# Oracle instance connection parameters
HOST=my.oracle.host
SID=orcl
PORT=1521
USERNAME=oracleuser
PASSWORD=oraclepassword

# Audit table to use
AUDIT_TABLE=FGA_LOG

# path to gprom binary
GPROM_PATH=/usr/bin
~~~

Now you can start TGDB from the `build` directory

~~~
cd build/
java -jar gtdb.jar
~~~

## Usage

The GUI consists of three panels: the **transaction timeline panel**, the **transaction detail panel**, and the **debug panel**.

### Transaction Timeline Panel

The entry point of the application is the transaction timeline panel which shows a timeline of transactions executed in the past (the x-axis is time and the y-axis are transactions). 

### Transaction Detail Panel

Clicking on a transaction open a new panel showing the details of this transaction: when it was executed, what isolation level it was run under, and the SQL statements of this transaction. From here you can select to debug the transaction which opens a debug panel.

### Debug Panel

The debug panel shows the content of tables affected by the transaction that was selected for debugging. Only rows that were affected by the transaction are displayed by default. In this panel the user can inspect the provenance of rows and evaluate what-if scenarios by changing the data and/or updates of the transaction.

#### Inspecting Provenance

To inspect the provenance of a row version, click on the row version. The debugger will display a provenance graph that shows data dependencies (on which previous row versions does this row depend on) as well as which updates did affect that row (these updates are highlighted in the top row and are shown as nodes in the provenance graph).

#### What-if Scenarios

The debugger supports what-if scenarios, i.e., hypothetical changes to data or SQL code. To create a what-if scenario modify the SQL commands of the transaction and/or edit data in the original (left most) version of the table. Clicking the `What-if` button evaluates the what-if scenario and show the result. To wipe the scenario press the `Reset` button.


