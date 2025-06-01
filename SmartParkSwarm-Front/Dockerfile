FROM ubuntu:jammy

COPY . /home/ubuntu/src

RUN apt update && apt install -y curl
RUN curl -sL https://deb.nodesource.com/setup_20.x | bash
RUN apt-get install -y nodejs
RUN npm install -g @angular/cli

WORKDIR /home/ubuntu/src

RUN npm install
RUN ng build --configuration=production
RUN npm install -g http-server

CMD [ "http-server", "/home/ubuntu/src/dist/smart-park-swarm-front/browser", "-p", "80" ]