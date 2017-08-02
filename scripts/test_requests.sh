#!/usr/bin/env bash

curl -X POST -d '{"id": 123, "ads": ["one", "two", "three"]}' http://localhost:8080/bid -H "Content-Type:application/json" && echo "done1" &
curl -X POST -d '{"id": 123, "ads": ["one", "two", "three"]}' http://localhost:8080/bid -H "Content-Type:application/json" && echo "done2" &
curl -X POST -d '{"id": 123, "ads": ["one", "two", "three"]}' http://localhost:8080/bid -H "Content-Type:application/json" && echo "done3" &

wait