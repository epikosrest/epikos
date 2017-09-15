# https://yurisubach.com/2016/07/14/jersey-dockerize/
# https://blog.giantswarm.io/getting-started-with-java-development-on-docker/
# https://github.com/spotify/docker-maven-plugin
#https://medium.com/@pimterry/5-ways-to-debug-an-exploding-docker-container-4f729e2c0aa8


#FROM java:8

#FROM java:8-jre
FROM maven:3.3.9-jdk-8
# application placed into /opt/app

RUN mkdir -p /opt/app
WORKDIR /opt/app

RUN mkdir /opt/app/Config
COPY Config/dynamicresource.yml /opt/app/Config/dynamicresource.yml
COPY Config/Application.configuration /opt/app/Config/Application.configuration

COPY target /opt/app/target

# local application port
EXPOSE 8080

#CMD ["/bin/bash", "-ex", "/opt/app/target/bin/Service"]

CMD ["/bin/bash", "-ex","/opt/app/target/bin/Service","-D", "FOREGROUND"]
#ENTRYPOINT [“/opt/app/target/bin/Service”]

