# TeamCityDatePlugin: date and branch names for build numbers

## Development notes
To build, test, and package the plugin, run `mvn package` from the root directory.

To deploy the plugin, copy `date-plugin.zip` into the TeamCity plugin directory and restart the server.
If you've built locally, get the zip file from `target/date-plugin.zip`


# Credits
Heavily inspired from the following sources
* [Date Build Number](https://confluence.jetbrains.com/display/TW/Date+Build+Number) by [Yegor Yarko](https://confluence.jetbrains.com/display/~yaegor)
* [ParaMetrics](https://github.com/sferencik/ParaMetrics) by [sferencik](https://github.com/sferencik)
