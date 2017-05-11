Start server
============

mvn spring-boot:run

Access restricted area (will fail)
==================================

curl http://localhost:8080/api/getusers

Login as admin using credentials to get Bearer Token
====================================================

curl -H "Content-Type: application/json" -i -X POST -d '{"username":"admin","password":"password"}' http://localhost:8080/login

HTTP/1.1 200  
X-Content-Type-Options: nosniff  
X-XSS-Protection: 1; mode=block  
Cache-Control: no-cache, no-store, max-age=0, must-revalidate  
Pragma: no-cache  
Expires: 0  
X-Frame-Options: DENY
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTQ5NTI3NjQwMn0.5rvVv0vGo6m0LltFLacZOBvfXggu1_dwi4Lzi6OyC1cSN8Qz3y5_zV8R_bPsGzAvfLxPdOiXUaHPowoN_j75kA  
Content-Length: 0  
Date: Wed, 10 May 2017 10:33:22 GMT  

Access restricted area (/getusers) as admin
===========================================

curl -H "Authorization:Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTQ5NTI3NjQwMn0.5rvVv0vGo6m0LltFLacZOBvfXggu1_dwi4Lzi6OyC1cSN8Qz3y5_zV8R_bPsGzAvfLxPdOiXUaHPowoN_j75kA" http://localhost:8080/api/getusers  

[{"firstname":"Steve","surname":"Davis"},{"firstname":"Robbie","surname":"Whiting"}]  

Access restricted area (/getusers) as admin
===========================================

curl -H "Authorization:Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTQ5NTI3NjQwMn0.5rvVv0vGo6m0LltFLacZOBvfXggu1_dwi4Lzi6OyC1cSN8Qz3y5_zV8R_bPsGzAvfLxPdOiXUaHPowoN_j75kA" http://localhost:8080/api/getsupersecret  

{"timestamp":1494413546067,"status":403,"error":"Forbidden","exception":"org.springframework.security.access.AccessDeniedException","message":"Access is denied","path":"/api/getsupersecret"}  

Login as jamesbond using credentials to get Bearer Token
========================================================

curl -H "Content-Type: application/json" -i -X POST -d '{"username":"jamesbond","password":"007"}' http://localhost:8080/login  

HTTP/1.1 200  
X-Content-Type-Options: nosniff  
X-XSS-Protection: 1; mode=block  
Cache-Control: no-cache, no-store, max-age=0, must-revalidate  
Pragma: no-cache  
Expires: 0  
X-Frame-Options: DENY
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqYW1lc2JvbmQiLCJleHAiOjE0OTUyNzc2NDN9.nctRT6W83aR_1QdKwkiT78jQd72EaZEWlZwq995Gb7nu-Y5Ryvb5drennloN5lTv92q6YI0j3QiL2QiOT3okjQ
Content-Length: 0  
Date: Wed, 10 May 2017 10:54:03 GMT   

Access restricted areas (/getusers and /getsupersecret) as jamesbond
====================================================================

curl -H "Authorization:Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqYW1lc2JvbmQiLCJleHAiOjE0OTUyNzc2NDN9.nctRT6W83aR_1QdKwkiT78jQd72EaZEWlZwq995Gb7nu-Y5Ryvb5drennloN5lTv92q6YI0j3QiL2QiOT3okjQ" http://localhost:8080/api/getusers  

[{"firstname":"Steve","surname":"Davis"},{"firstname":"Robbie","surname":"Whiting"}]  

curl -H "Authorization:Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqYW1lc2JvbmQiLCJleHAiOjE0OTUyNzc2NDN9.nctRT6W83aR_1QdKwkiT78jQd72EaZEWlZwq995Gb7nu-Y5Ryvb5drennloN5lTv92q6YI0j3QiL2QiOT3okjQ" http://localhost:8080/api/getsupersecret  





Setting up HTTPs
================

Generate the Keystore
---------------------

keytool -genkey -alias sjd.spring.boot -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 3650  

Enter keystore password:  <password>  
Re-enter new password:  <password>  
What is your first and last name?  
  [Unknown]:  Steve Davis  
What is the name of your organizational unit?  
  [Unknown]:  IG  
What is the name of your organization?  
  [Unknown]:  IG  
What is the name of your City or Locality?   
  [Unknown]:  Fareham 
What is the name of your State or Province?  
  [Unknown]:  Hampshire  
What is the two-letter country code for this unit?  
  [Unknown]:  GB  
Is CN=Steve Davis, OU=IG, O=IG, L=Fareham, ST=Hampshire, C=GB correct?  
  [no]:  yes  


Add this to application.properties  
----------------------------------

# SSL Setup
server.port: 8443  
server.ssl.key-store: classpath:keystore.p12  
server.ssl.key-store-password: password  
server.ssl.key-password = password  
server.ssl.keyStoreType: PKCS12  
server.ssl.keyAlias: sjd.spring.boot  

Add this to pom.xml
-------------------

<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
        <folders>
            <folder>.</folder>
        </folders>
    </configuration>
</plugin>

Test
----

curl -H "Content-Type: application/json" -i **-k** -X POST -d '{"username":"admin","password":"password"}' https://localhost:8443/login  


References
==========

https://auth0.com/blog/securing-spring-boot-with-jwts/
https://www.mkyong.com/spring/curl-post-json-data-to-spring-rest/
http://www.linuxask.com/questions/show-http-response-header-using-curl