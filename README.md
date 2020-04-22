# RoadApp
RoadApp is a parametrized scheduling platform for road maintenance created for iSTEM Geauga ECHS's Spring 2020 Design Challenge. In this app, imported data is used in conjunction with parameters and weights (defined by the user) to score each road and order them. The score is computed as a weighted arithmetic mean. The program's output is a list of roads ordered from highest scoring to lowest scoring. This is intended to be used as a list of priority for road maintenance scheduling&mdash;the main goal of this application.

**A Disclaimer:** I am not a professional developer and this project changed a good bit along the way (like any programming project would), so this code is pretty messy. Also, since this program was written for a school project, I do not anticipate updating the code after the fact.

### Recommended Data
In order to test and evaluate this our program we used the following data from the Ohio Department of Transportation's (ODOT) Transportation Information Mapping System (TIMS):

* PCR State
* Predicted PCR
* Traffic Congestion

We recommend that you use data from your state's department of transportation. The data download portal for ODOT can be found at <https://gis.dot.state.oh.us/tims/Data/Download>, and the data glossary can be found at <https://gis.dot.state.oh.us/tims/Glossary>. Truncated versions of these datasets are also included in the *Example Data Folder* in this repository. Also, please note that **all data must be formatted as a CSV file**.

### Example Parameters
Example parameters for the data we used are in the *Example Parameters Folder*. We recommend you use these parameters for testing only. Also, if you choose to use them, please note that you will have to update the paths of the databases in *Example Parameters/dataset_settings.csv* file. 

### Platform
This program uses JavaSE-1.8 and AWT Swing.

### Performance
Depending on the amount of data imported, this program may need a lot of memory space. If it throws an OutOfMemory error, we recommend adding the flag `-Xmx2G` (or something like it). This allocates 2GB of ram to the program, so other programs may run slowly while the app is calculating its output. The amount of memory may need to be adjusted as well.

## Basic Instructions in How to Use this Program

**Uploading Data:** This is the first step. Add databases by clicking on the "Data" tab (you should already be there if you just opened the application) and then clicking on the "Add" button. In the box that opens, give the database a name and choose a file for the database (it must be a csv). The name is used to reference the database later on. Once you finish adding your databases, click the "Load Data Paths and Columns" button. This allows the program to read the columns of the databases and save them to memory so that they can be referenced individually.

**Setting the Parameters:** Now, hop over to the "Macro Parameter" tab. Here, you can set the parameters that will determine how the schedule is prioritized. The Damage field and the Traffic field both have macro weights; those weights will be multiplied with the weights of the individual parameters. To set individual parameters, click on one of the "Edit Parameters" buttons. Once you do, a window similar to the "Data" tab will appear. Again, click the "Add" button. Then, choose the database and the column from the drop-down menus in the new dialogue box and specify a weight for the parameter. Click "Add" to save the parameter.

Once you've finished adding individual parameters to the Damage and Traffic fields, take a look at the Length field. This field is used, along with the data in the "Contractors" tab, to calculate cost. Add the columns related to the length of each road segment and/or the number of lanes for each segment.

You must also add an ID column for each database. The id column of the first database you added will be referenced in the schedule which this program generates.

**Adding Contractors:** If you go to the "Contractors" tab you will find that it is very similar to the "Data" tab. Use the same steps to add contractors to the list. An explanation of the fields: the "Name" field is used for the names of the contractors, the "Number of Jobs to Schedule" field is used for the number of jobs you would like the contractor to do (this may be limited by the number of teams that contractor has available), and the "Cost per Length Unit" field is used for the cost per length unit of maintaining a road segment. This "length unit" is the culmination of the columns you added to the length field in the "Macro Parameter" tab.

**Scheduling:** Once you are done with all of these things, you can click the big "Schedule" button at the bottom of the window. The program will appear to freeze, but this is normal; the program is reading the databases and generating the schedule, and it takes quite a long time. On my laptop, the program takes from about 100 to 200 seconds to run when I include the complete databases (as opposed to the truncated ones). Once it is done, a window with the schedule will pop up. From there, you can close the application, go back to the main window, or save the generated schedule.

**Note:** All of the tables and parameters in the program can be imported and exported as csv files.
