# How To Use

- [Introduction](#introduction)
- [Converting UTM](#converting-utm-to-latitude-longitude)
	- [Video Demonstration](#conversion-video-demo)
- [Combining CSV](#combining-csv-files)
	- [Video Demonstration](combining-video-demo)

## Introduction

This program was created to assist with 2 of the routine procedures needed for the mapping project. 

The first procedure is for converting the crime data found [here](http://data.vancouver.ca/datacatalogue/crime-data-details.htm). For this routine, we would download the Crime (XLSX) file. This XLSX file contains the needed data for all years, but uses UTM coordinates for the events. We need to convert this data into a tab delimited TXT file with Latitude and Longitude coordinates before we can use it for our mapping software. This program automates the process of converting the coordinates and changing the file format. Coordinate conversion functions used for this program were based on [this](https://github.com/Berico-Technologies/Geo-Coordinate-Conversion-Java). 

The second procedure was to combine weekly patrol reports emailed in CSV format into monthly reports. This program lets you drag and drop all the CSV files needing to be combined and combines them into one file automatically. 

Note: I have heard that sometimes the third party software responsible for emailing the CSV files might generate some invalid data in cetain columns, and we hav ebeen manually correcting them. Please feel free to let me know of those issues with details on where the values are and where they are supposed to be, etc, and I can add a function to automatically correct them as well. 

## Converting UTM to Latitude Longitude

### Instructions

1. Press on the Gear/Settings icon and select the output directory (where the file will be created). Some locations such as C:/ might not work due to permission issues, so you can try something like the Desktop or somewhere in My Documents. 

2. Click Browse and select the XLSX file downloaded from [here]((http://data.vancouver.ca/datacatalogue/crime-data-details.htm). It should be named something like "crime_xlsx_all_years.xlsx." 

3. Click on the Convert button to start converting the file's coordinates from UTM to Latitude/Longitude and into a TXT file. Wait for the checkmark to appear to indicate completion. 

4. The file should be in the output directory with the name of the first file + Combined at the end, in CSV format. If it is not there, try a different output directory. 

Please note that this function only uses the worksheet on the very right (should be named the current year). This is so that it takes the most recent worksheet for the current year. To run this for older years, you can just move the sheets that you want to use the right most tab in Excel. 

### Conversion Video Demo


## Combining CSV files

### Instructions

1. Press on the Gear/Settings icon and select the output directory (where the file will be created). Some locations such as C:/ might not work due to permission issues, so you can try something like the Desktop or somewhere in My Documents. 

2. Go back to the CSV menu. 

3. Drag and drop all of the CSV files that you want combined into the box that says "Drop .CSV Files Here." 

4. Click on the Combine button to combine the CSV files and wait for the Check mark to appear to indicate completion. 

5. The file should be in the output directory with the name of the first file + Combined at the end, in CSV format. If it is not there, try a different output directory. 

### Combining Video Demo
