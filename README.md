Bidder
======

Reference implementation Bidder supporting OpenRTB 2.5 spec.

Based on akka toolkit.

- server - `akka-http`
- concurrency - `akka-actors`
- json parsing - `json4s` and `jackson`

Load test are based on gatling - @see exchange project



###Test
sbt clean compile
sbt run

curl -X POST -d '{"bid": "Test bid"}' http://localhost:8080/bid -H "Content-Type:application/json"
