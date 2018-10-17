FROM domingogallardo/playframework
WORKDIR /app
COPY . /app
RUN sbt clean stage

EXPOSE 9000
ENV CONFIG_FILE=conf/application.conf
ENV SECRET=abcdefghijk

CMD target/universal/stage/bin/mads-todolist-pmateo -Dplay.crypto.secret=$SECRET -Dconfig.file=$CONFIG_FILE