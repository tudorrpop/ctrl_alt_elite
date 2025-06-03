# Advanced Distributed Systems Project - Milestone 1

## Team: Ctrl_Alt_Elite

**Members:**
- Mergea Sergiu
- Marius Panduru
- Pop Tudor
- Pop Adrian (adrian.pop2@student.upt.ro)

**GitHub:** [Ctrl_Alt_Elite](https://github.com/tudorrpop/ctrl_alt_elite)  
**Trello:** [Ctrl_Alt_Elite Trello](https://trello.com/b/N1t9sIdN/ctrlaltelite)

---

## General Description & Features

This distributed application is concieved to offer a complete car parking management
solution across multiple stores of the same brand in a city. The application is designed to
serve both driving users and store administrators.
The first category of users are going to use a mobile solution where they will register and
login into an application and utilize a digital generated card to enter the parking space. The
mobile component will have an AI module integrated. This AI model will be capable of
offering responses to the users’s questions, related to both the store parking lots and their
availability and it will be capable of making predictions based on the history data. The
interactions will take part through a chat interface.
A web component will also be available for the store administrators. They will be able to see
the live state of the parking lots and several statistics regarding the traffic matters.
Each component will be hosted on a specific server in order to provide modularity.

---

## Technologies to be used

- **Servers:** Azure Services  
- **Backend:** Spring Boot (Java)  
- **Frontend:** Angular + NativeScript (TypeScript)  
- **Database:** MySQL  
- **AI Technologies:** LM Studio / Amazon Lex  
- **Authentication:** JSON Web Tokens

## Commands to run the project

# Backend

cp -r . /home/ubuntu/src

chmod +x /home/ubuntu/src/start-springapp.sh

sudo apt update

sudo apt install openjdk-21-jdk-headless zip unzip curl -y

curl -s https://get.sdkman.io | bash

source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install springboot
sdk install maven

cd /home/ubuntu/src/

source "$HOME/.sdkman/bin/sdkman-init.sh"
mvn clean package

/home/ubuntu/src/start-springapp.sh

# Frontend

cp -r . /home/ubuntu/src

sudo apt update && sudo apt install -y curl

curl -sL https://deb.nodesource.com/setup_20.x | sudo bash -
sudo apt-get install -y nodejs

sudo npm install -g @angular/cli

cd /home/ubuntu/src

npm install

ng build --base-href / --configuration=production

sudo npm install -g http-server

http-server /home/ubuntu/src/dist/smart-park-swarm-front/browser -p 80 --spa index.html --proxy http://localhost?

# Mobile

cp -r . /home/ubuntu/src

sudo apt update && sudo apt install -y curl

curl -sL https://deb.nodesource.com/setup_20.x | sudo bash -
sudo apt-get install -y nodejs

sudo npm install -g @angular/cli
sudo npm install -g @ionic/cli

cd /home/ubuntu/src

npm install

ng build --base-href / --configuration=production

sudo npm install -g http-server

http-server /home/ubuntu/src/www -p 80 --spa index.html --proxy http://localhost?

# Worker

cp -r . /home/ubuntu/src

sudo apt update && sudo apt install -y python3 pip

cd /home/ubuntu/src

pip install -r requirements.txt

python3 manage.py migrate

python3 manage.py runserver 0.0.0.0:8000
