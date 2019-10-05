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

### Custom column header text

When grid is created with bean type create map where Column is key and header text is value. Pass that map as argument to Exporter subclass constructor.
If map is null then default column key will be used for creating column header in excel.
```
private Map<Column<MyEntity>, String> gridHeaderMap;
...
Exporter.exportAsExcel(grid, gridHeaderMap)
```
Exporter works as using reflection to read property from each item, any column without a valid key will be ignored.
In case when grid is created without bean type (adding columns with value providers) then map with column headers is mandatory. 
 
### Data formats, excel builtin and custom data formats

By default excel exporter will use default excel data formats for date, time and numbers. To set custom formats:
```
DataFormats formats = new TypeDataFormats();
formats.localDateFormat("DDD-MMM-YY");
Exporter.exportAsExcel(grid, gridHeaderMap, formats);
```


## Development instructions

Starting the test/demo server:
```
mvn jetty:run
```

This deploys demo at http://localhost:8080
