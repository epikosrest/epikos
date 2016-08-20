Copyright (c) [2016] [epikosrest@gmail.com]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

About :

This is "open" source project written in Java based on Jersey (for rest framework), Grizzly (embedded http container)
and is based on Maven (Gradle will be supported in future) for build system.

Motivatoin: 
The motivation is to learn and implement dynamic feature provided by Jersey to make rest api design simple and effective. 
Based on Jersey, the framework try to exploit dynamic feature such as "Programmatic API for Building Resources " 
ref: https://jersey.java.net/documentation/latest/resource-builder.html .

Intention:
The idea of the framework is very simple. It simply try to separate API def/doc from code base and allow adding resource
dynamically rather than static. The framework will have all common essential feature at top level there by allowing
developer to focus on business logic rather than basic plumbing e.g. Coding to start service , adding metrics , defining
rest api in controller/resource handler etc. All these common task should/must be provided at top level framework so that
developers can focus on business logic implementation. Please refer to wiki for more detail.

In nutshell the idea/intention of this project is to create a base rest service/framework which will

1. allow to stand up a restful service using configuration (see below for more detail) and minimum coding.

2. allow it to extend/inherit and use it as base framework to spin off
new RESTful services with reach features that include but not limited to logging, metrics/performance monitoring, api documentation.

Requirement:
Java version 1.8 or above
Maven : latest version is recommended
IDE : IntelliJ IDEA (recommended), Eclipse


How To :

1. To clone the service 
$ git clone https://github.com/epikosrest/epikos.git
2. Compile the serivce 
$ mvn clean install

3. run service : $ sh target/bin/Service


Resources

Source Controller (GitHub):
Repo : https://github.com/epikosrest/epikos

Continuous Build System (Jenkin):
A temporary Jenkin job has been setup and will trigger build for every push event on Master branch:
http://54.191.157.117:8080/job/EpikosRestService/

Collaboration, Project Management and Tracking (Trello) :
Please subscribe/checkout following Trello link for on going project development and task.
https://trello.com/b/7Bw9gke5/epikos-rest-service

(Slack) : Please tune in to following slack channel for update and notification of on going activity
https://epikosrest.slack.com/messages/epikosservice/


This service is free and can be used by anyone to enhance and use for any purpose and hence being released under GNU GPL V2
Feedback are welcome: please send it to epikosrest@gmail.com

