FROM tomcat:9.0.33

LABEL maintainer="ahmedhi"

RUN rm -rf /usr/local/tomcat/webapps/ROOT

COPY /out/artifacts/SE2_Project_war_exploded /usr/local/tomcat/webapps/SE2_Project_war_exploded

RUN mkdir /db

COPY /database.db /db/database.db

EXPOSE 8080

CMD ["catalina.sh", "run"]
