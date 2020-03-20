# RoadApp
RoadApp is a parametrized scheduling platform for road maintenance created for iSTEM Geauga ECHS's Spring 2020 Design Challege. In this app, imported data is used in conjunction with parameters and weights (defined by the user) to score each road and order them. The score is computed as a weighted arithmetic mean. The program's output is a list of roads ordered from highest scoring to lowest scoring. This is intended to be used as a list of priority for road maintenance scheduling&mdash;the main goal of this application.

### Recommended Data
In order to test and evaluate this our program we used the following data from the Ohio Department of Transportation's (ODOT) Transportation Information Mapping System (TIMS):

* PCR Local
* PCR State
* Predicted PCR
* Road Inventory
* Traffic Congestion

We recommend that you use data from your state's department of transportation. The data download portal for ODOT can be found at <https://gis.dot.state.oh.us/tims/Data/Download>, and the data glossary can be found at <https://gis.dot.state.oh.us/tims/Glossary>. Also, please note that **all data must be formatted as a CSV file**.

### Example Parameters
Example parameters for the data we used (as shown above) will be added in an *Example Parameters Folder*.

### Platform
This program uses JavaSE-1.8 and Java Swing.

### Performance
Depending on the amount of data imported, this program may need a lot of memory space. If it throws an OutOfMemory error, we recommend adding the flag `-Xmx2G`. This allocates 2GB of ram to the program, so other programs may run slowly while the app is calculating its output. The amount of memory may need to be adjusted as well.

Please note that, although this program works, we do not think it is optimized for performance. Using the example data and the example parameters, it takes about 200 seconds to run. However, it fulfills our requirements. If you need the program to run faster and more efficiently than it already does, please feel free to make a new branch and optimize it yourself.