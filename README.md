[![Published on Vaadin  Directory](https://img.shields.io/badge/Vaadin%20Directory-published-00b4f0.svg)](https://vaadin.com/directory/component/exporter)
[![Stars on Vaadin Directory](https://img.shields.io/vaadin-directory/star/exporter.svg)](https://vaadin.com/directory/component/exporter)

# Exporter Add-on for Vaadin 7

Exporter is a Data component add-on for Vaadin 7.

## Online demo

Try the add-on demo at http://haijian.virtuallypreinstalled.com/ExporterDemo/

## Download release

Official releases of this add-on are available at Vaadin Directory. For Maven instructions, download and reviews, go to hhttps://github.com/haiwan/Exporter.git

## Building and running demo

git clone https://github.com/haiwan/Exporter.git
mvn clean install
cd exporter-demo
mvn jetty:run

To see the demo, navigate to http://localhost:8080/

## Development with Eclipse IDE

For further development of this add-on, the following tool-chain is recommended:
- Eclipse IDE
- m2e wtp plug-in (install it from Eclipse Marketplace)
- Vaadin Eclipse plug-in (install it from Eclipse Marketplace)
- JRebel Eclipse plug-in (install it from Eclipse Marketplace)
- Chrome browser

### Importing project

Choose File > Import... > Existing Maven Projects

Note that Eclipse may give "Plugin execution not covered by lifecycle configuration" errors for pom.xml. Use "Permanently mark goal resources in pom.xml as ignored in Eclipse build" quick-fix to mark these errors as permanently ignored in your project. Do not worry, the project still works fine. 

### Debugging server-side

If you have not already compiled the widgetset, do it now by running vaadin:install Maven target for exporter-root project.

If you have a JRebel license, it makes on the fly code changes faster. Just add JRebel nature to your exporter-demo project by clicking project with right mouse button and choosing JRebel > Add JRebel Nature

To debug project and make code modifications on the fly in the server-side, right-click the exporter-demo project and choose Debug As > Debug on Server. Navigate to http://localhost:8080/exporter-demo/ to see the application.

### Debugging client-side

Debugging client side code in the exporter-demo project:
  - run "mvn vaadin:run-codeserver" on a separate console while the application is running
  - activate Super Dev Mode in the debug window of the application or by adding ?superdevmode to the URL
  - You can access Java-sources and set breakpoints inside Chrome if you enable source maps from inspector settings.
 
## Release notes

### Version 0.0.6
- ...
- ...

## Roadmap

This component is developed as a hobby with no public roadmap or any guarantees of upcoming releases. 

## Issue tracking

The issues for this add-on are tracked on its github.com page. All bug reports and feature requests are appreciated. 

## Contributions

Contributions are welcome, but there are no guarantees that they are accepted as such. Process for contributing is the following:
- Fork this project
- Create an issue to this project about the contribution (bug or feature) if there is no such issue about it already. Try to keep the scope minimal.
- Develop and test the fix or functionality carefully. Only include minimum amount of code needed to fix the issue.
- Refer to the fixed issue in commit
- Send a pull request for the original project
- Comment on the original issue that you have implemented a fix for it

## License & Author

Add-on is distributed under Apache License 2.0. For license terms, see LICENSE.txt.

MyComponent is written by Haijian Wang

# Developer Guide

## Getting started

Here is a simple example on how to try out the add-on component:

ExcelExporter excelExporter = new ExcelExporter();
excelExporter.setDateFormat("yyyy-MM-dd");
excelExporter.setTableToBeExported(sampleTable);
excelExporter.setCaption("Export to Excel");
layout.addComponent(excelExporter);

For a more comprehensive example, see exporter-demo org.vaadin.addons.demo.DemoUI.java

## Features

### Feature A

export data to excel file

### Feature B

export data to csv file
