FROM tomcat:9.0-jdk8
WORKDIR /usr/app
COPY /web/target/web-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]