# Setting up the development environment

## Requirements:
- python3
- pip3
- virtualenv (can be installed using pip3 with: ```pip install python3-virtualenv```)

## Setup

### Setup from CLI:
In order to prepare the environment from CLI, you need to open a shell and run the following commands:
```bash
git clone https://github.com/tudorrpop/ctrl_alt_elite.git
cd ctrl_alt_elite/SmartParkSwarm-Worker
virtualenv venv
source venv/bin/activate
pip install requirements.txt
```

### Setup from Visual Studio Code
1. Install the Python extension on Visual Studio Code. 
2. Open the SmartParkSwarm-Worker folder from Visual Studio Code (not the whole repository, only the child folder named SmartParkSwarm-Worker).
3. Press ```Ctrl + Shift + P``` and choose the option ```Python: Create Environment```.
4. Choose option ```Venv```.
5. Select the preferred python interpreter.
6. Select the file ```requirements.txt```.

## Run the application
On the first time running the application, you will need to also perform the DB migration for development:
```bash
python3 manage.py migrate
```

In order to run the app, you need to do it from a shell:
```bash
python3 manage.py runserver 0.0.0.0:8000
```