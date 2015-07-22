# Build and execution instructions

First, download and install SBT, either through your package manager of choice or from [http://www.scala-sbt.org/download.html]()

Now go to the project's root directory. From here you can either execute the program via SBT using:

`sbt run <parameter>`

Try running `sbt run` without parameters to see the valid parameters.

You can also generate a "fat JAR" for distribution, including the code and all dependencies. For this, first execute:

```
sbt assembly
```

This will leave the "fat Jar" in `target/scala-2.11/rock_paper_scissors.jar`. You can then execute the application using:

```
java -jar target/scala-2.11/rock_paper_scissors.jar <parameter>
```

# Testing and coverage

To execute the unit tests, run:

```
sbt clean test
```

To generate the code coverage report, run:

```
sbt clean coverage test
```

The code coverage reports are written to target/scala-2.11/scoverage-report
