Micronaut backend vuejs frontend application - CQRS
---



###### Youtube [Video demonstrating product](https://www.youtube.com/watch?v=-pKr6Zg-MtA). Written [description available here](https://github.com/vahidhedayati/micronaut-vuejs-cqrs/blob/master/detailedDescription.md).

![how this app works](https://raw.githubusercontent.com/vahidhedayati/micronaut-vuejs-cqrs/master/docs/eventstoreCQRS.png)



Running app
----
##### Please refer to [pre-requirements](https://github.com/vahidhedayati/micronaut-vuejs-cqrs/blob/master/configure.md).

```
./gradlew hotel-read:run  userbase-read:run frontend:start gateway-command:run gateway-query:run hotel-write:run  userbase-write:run  --parallel
```

###### Advanced: 
##### When running on linux a process for node hangs on which also keeps jvms active - killing node kills all other jvms hanging off
##### this is all in 1 line to kill if found and start apps

```
kill -9 $(netstat -pln 2>/dev/null |grep LISTEN|grep node|awk '{print $7}'|awk -F"/" '{print $1}');
./gradlew hotel-read:run  userbase-read:run frontend:start gateway-command:run gateway-query:run hotel-write:run  userbase-write:run   --parallel
```




The above will launch 1 instance of frontend vuejs site running on `localhost:3000` 
and a backend micronaut site running on port `localhost:{random}` a gateway micronaut app running on port 
`localhost:8080` 


##### frontend changed to resemble a grails vuejs site: will start up on port 3000
```
c:\xxx\micronaut\hotel-info>gradlew frontend:start

To manually start app using npm or yarn run:

micro-projects/hotel-info/frontend$ npm run dev   

micro-projects/hotel-info/frontend$ yarn run dev


```


##### To run Backend: will currently launch and bind to port {random}  - mutiple instances can be started

```
c:\xxxx\micronaut\hotel-info>gradlew backend:run --stacktrace

```


##### To run gateway app : will currently launch and bind to port  8080 - testing single instance only currently

```
c:\xxxx\micronaut\hotel-info>gradlew gateway:run --stacktrace

```


##### Attempt to build the existing microservice micronaut CRUD app to use CQRS:
This application at the moment has a working read / write for hotel object - 2 separated microservices applications - anything to save/update attempts to :
> Call command feature of hotel-write - generate hotel record saved on local h2 db - send hotel via kafka as an event to hotel-read application. hotel read gets event and stores/updates the record locally.

Save is currently working - update to be done - etc  



##### Please refer to [what CQRS is here](https://github.com/vahidhedayati/micronaut-vuejs-cqrs/blob/master/cqrs-explained.md).

