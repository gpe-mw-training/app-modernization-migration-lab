Red Hat Application Modernization & Migration CoolStore Monolith Demo
=====================================================================
Technologies: CDI, JAX-RS, JPA
Java Version: Java6  
Target Product: EAP 6.3
Persistence: Postgresql

This is an example demo showing a retail store consisting of several microservices based on [Redhat Coolstore MicroServices Demo](https://github.com/jbossdemocentral/coolstore-microservice.git)

It demonstrates how to modernize a large scale monolith application, its runtime environments and lifecycles without business functional change.

### The coolstore functionality is shown to comprise of :

1. Catalog : serves products and prices for retail products.
* Cart : showcases shopping cart of each customer.
* Inventory : serves inventory and availability data for retail products


#### Prerequisites
In order to deploy the coolstore monolith you need
* **EAP 6.3.0.GA**
* **Postgresql** support does not come out of the box. Please add its support as
 * Add *datasource* in standalone.xml as per [config](./standalone.xml).
 * Create `module.xml` at `HOME_EAP/modules/org/postgresql/main/` as per [module.xml](./module.xml)

#### Build & Deploy
* mvn clean package
* Copy the `monolith-coolstore.war` to `$EAP_HOME/standalone/deployments`

#### EndPoints
1. Swagger:
```
http://127.0.0.1:8080/monolith-coolstore/rest/swagger.json
```

* Inventory:
```
http://127.0.0.1:8080/monolith-coolstore/rest/inventory/444435
```
* Cart:
```
curl -XPOST http://127.0.0.1:8080/monolith-coolstore/rest/cart/111/322/100
curl -XPOST http://127.0.0.1:8080/monolith-coolstore/rest/cart/222/444436/100
curl -XPOST http://127.0.0.1:8080/monolith-coolstore/rest/cart/222/444435/100
curl -XGET http://127.0.0.1:8080/monolith-coolstore/rest/cart/222
```
* Catalog:
```
curl -XPOST -H "Content-Type: application/json" -d '{"itemId":"322","name":"curl","description":"Red Fedora Official Red Hat Fedora","price":34.99}' http://127.0.0.1:8080/monolith-coolstore/rest/catalog/
```

## Redhat Application Migration Toolkit(AMT)
version:  4.0.0.Beta4

#### XML Rule
Copy the custom xml rule `proprietary-servlet-annotations.windup.xml` to `WINDUP_RULES_DIR`. where `WINDUP_RULES_DIR` is `${user.home}/.windup/rules/` 

#### RHAMT command
```
 ./bin/rhamt-cli --input  ~/projects/gpe-mw-training/app-modernization-migration-lab/monolith-coolstore-app/target/monolith-coolstore-app.war   --output ~/projects/gpe-mw-training/app-modernization-migration-lab/windup/output/war   --target eap:7   --target cloud-readiness --packages com.redhat.coolstore
```

##### TODO
* In cart initialise infinispan cache, at present hashmap being used as in default case.

