gatling-assertion-decoder 
---

Gatling pickles assertion objects and converts them to Base64 strings then writes them to simulation.log file. 
Later in the report generation phase, it decrypts them and computes the assertions. 

This library uses gatling libraries to decrypt the assertions and outputs them as strings to be processed
by Automaton Gatling services.

Example Usage
---

Add gatling-assertion-decoder as a dependency.

build.sbt:

```
libraryDependencies += "com.github.marufaytekin" % "gatling-assertion-decoder" % "0.1-SNAPSHOT"
```
pom.xml:

```
<dependency>
    <groupId>com.github.marufaytekin</groupId>
    <artifactId>gatling-assertion-decoder</artifactId>
    <version>0.1-SNAPSHOT</version>
</dependency>
```

Import the package and call decoder:
```
import com.github.marufaytekin.Decoder._
......
val assertion = decode(input)
print(assertion)
```

 
Run: 
`java -cp gatling-assertion-decoder-0.1-SNAPSHOT.jar com.github.marufaytekin.Decoder AAMAAQhyZXF1ZXN0MQQGAAAAAAAAAPA/AAAAAAAANEAB`

Outputs: `Details|request1|MeanRequestsPerSecondTarget|null|null|Between|[1.0,20.0]|true::request1 : mean requests per second is between inclusive 1.0 and 20.0`
which consists of two parts decoded and printable assertion seperated by `::` 

