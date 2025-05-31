FROM ubuntu:jammy

COPY . /home/ubuntu/src

RUN apt update && apt install -y python3 pip
WORKDIR /home/ubuntu/src
RUN pip install -r requirements.txt

RUN python3 manage.py migrate
CMD [ "python3", "manage.py", "runserver", "0.0.0.0:8000" ]