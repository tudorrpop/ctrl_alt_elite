FROM ubuntu:jammy

COPY . /home/ubuntu/src

RUN chmod +x /home/ubuntu/src/start-springapp.sh

RUN apt update
RUN apt install openjdk-21-jdk-headless zip unzip curl -y
RUN curl -s https://get.sdkman.io | bash

RUN bash -c "source /root/.sdkman/bin/sdkman-init.sh && \
             sdk install springboot && \
             sdk install maven"

WORKDIR /home/ubuntu/src/

# RUN bash -c "source /root/.sdkman/bin/sdkman-init.sh && \
#              mvn clean package"
             
CMD [ "/home/ubuntu/src/start-springapp.sh" ]