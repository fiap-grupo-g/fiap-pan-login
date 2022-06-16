#! /bin/bash
mongoimport --host mongodb --db users --collection regularPerson --type json --file /mongo-seed/regular-person-seed.json --jsonArray
mongoimport --host mongodb --db users --collection legalPerson --type json --file /mongo-seed/legal-person-seed.json --jsonArray