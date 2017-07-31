Bidder
======

Reference implementation Bidder supporting OpenRTB 2.5 spec.

Based on akka toolkit.

- server - `akka-http`
- concurrency - `akka-actors`
- json parsing - `json4s` and `jackson`

Load test are based on gatling - @see exchange project



### Test

Run the application:
```sbtshell
sbt clean compile
sbt run
```
Test by posting json to endpoints.
```bash
curl -X POST -d '{"id": 123, "ads": ["one", "two", "three"]}' http://localhost:8080/bid -H "Content-Type:application/json"
curl -X POST -d '{"id": 123}' http://localhost:8080/bid -H "Content-Type:application/json"
```