# GProM Transaction Debugger

The GProM Transaction Debugger is a Java GUI application that allows a user to debug past executions of transactions in Oracle. The user selects


## Installation

You need Java and `ant` for building the system. Furthermore, you need a working version of [GProM](https://github.com/IITDBGroup/GProM). 
For the debugger to work, **fine-grained auditings** (**FGA**) needs to be activated in Oracle and unless only recent transactions are debugged, the tables accessed by the debugged transactions need to be setup for time travel using Oracle's **flashback archive** feature.

### Installing Prerequisites

~~~
ant
~~~

### Building the System

### Configuring Oracle

### Configuring and Starting the Debugger

The file `Options.txt` in 

~~~
java -jar 
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

### Scientific Background


