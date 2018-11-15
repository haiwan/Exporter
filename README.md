# Exporter

Allows you to export data from Grid to an Excel or CSV file
```
Exporter.exportAsExcel(grid));
Exporter.exportAsCSV(grid))
```

A typical use case is to let user download the generated file, an Anchor component can be used for such a purpose.
```
new Anchor(new StreamResource("my-excel.xls", Exporter.exportAsExcel(grid)), "Download As Excel");
```

## Use instructions

Each to be exported column should have a key, and the key should be a property name. 
A key will be generated automatically if you are also passing the class in the Grid constructor parameter, e.g.
```
Grid<Person> grid = new Grid<>(Person.class);
```
Otherwise, you need to set the key manually. e.g.
```
Grid<Person> grid = new Grid<>();
grid.addColumn(Person::getEmail).setKey("email");
```

Exporter works as using reflection to read property from each item, any column without a valid key will be ignored.

## Development instructions

Starting the test/demo server:
```
mvn jetty:run
```

This deploys demo at http://localhost:8080
