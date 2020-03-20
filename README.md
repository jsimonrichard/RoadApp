# RoadApp
RoadApp is a parametrized scheduling platform for road maintenance created for iSTEM Geauga ECHS's Spring 2020 Design Challege. In this app, imported data is used in conjunction with parameters and weights (defined by the user) to score each road and order them. The score is computed as a weighted arithmetic mean. The program's output is a list of roads ordered from highest scoring to lowest scoring. This is intended to be used as a list of priority for road maintenance scheduling &mdash the main goal of this application.

### Recommended Data
In order to test and evaluate this our program we used the following data from the Ohio Department of Transportation's (ODOT) Transportation Information Mapping System (TIMS):
* PCR Local
* PCR State
* Predicted PCR
* Road Inventory
* Traffic Congestion

We recommend that you use data from your state's department of transportation. The data download portal for ODOT can be found at <https://gis.dot.state.oh.us/tims/Data/Download>, and the data glossary can be found at <https://gis.dot.state.oh.us/tims/Glossary>.