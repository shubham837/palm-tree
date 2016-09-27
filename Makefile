DOCKER_MACHINE := default
WORKDIR := $(shell pwd)

# for OS specific cheks
ifeq ($(OS),Windows_NT)
	OSFLAG := Windows
else
	UNAME_S := $(shell uname -s)
	
	ifeq ($(UNAME_S),Darwin)
		OSFLAG := Darwin
	endif

	ifeq ($(UNAME_S),Linux)
		OSFLAG := Linux
	endif
endif

# make sure docker is installed
DOCKER_EXISTS := @echo "Found docker"
DOCKER_WHICH := $(shell which docker)
ifeq ($(strip $(DOCKER_WHICH)),)
	DOCKER_EXISTS := @echo "\nERROR:\n docker not found.\n See: https://docs.docker.com/\n" && exit 1
endif

# make sure docker-machien is available, for Macs (and Windows)
DOCKER_MACHINE_EXISTS := @echo "Found docker-machine"
DOCKER_MACHINE_WHICH := $(shell which docker-machine)
ifneq ($(OSFLAG),Darwin)
	ifeq ($(strip $(DOCKER_MACHINE_WHICH)),)
		DOCKER_MACHINE_EXISTS := @echo "\nERROR:\n docker-machine not found.\n See: https://docs.docker.com/machine/\n" && exit 1
	endif
endif

# make sure docker machine is running
DOCKER_MACHINE_RUNS := @echo "Docker-machine is running"
DOCKER_MACHINE_RUNNING := $(shell docker-machine env default 2>&1 | grep -o 'not running')
ifneq ($(strip $(DOCKER_MACHINE_RUNNING)),)
	DOCKER_MACHINE_RUNS := docker-machine start $(DOCKER_MACHINE)
endif

# make sure the proper environemnt variables are set
DOCKER_MACHINE_ENV := @echo "Docker environment set"
DOCKER_MACHINE_EV1 := $(DOCKER_TLS_VERIFY)
DOCKER_MACHINE_EV2 := $(DOCKER_HOST)
DOCKER_MACHINE_EV3 := $(DOCKER_CERT_PATH)
DOCKER_MACHINE_EV4 := $(DOCKER_MACHINE_NAME)
ifeq ($(strip $(DOCKER_MACHINE_EV1)),)
	DOCKER_MACHINE_ENV = @echo "\n Docker environment missing, run: eval \"\$$(docker-machine env default)\"\n" && exit 1
endif
ifeq ($(strip $(DOCKER_MACHINE_EV2)),)
	DOCKER_MACHINE_ENV = @echo "\n Docker environment missing, run: eval \"\$$(docker-machine env default)\"\n" && exit 1
endif
ifeq ($(strip $(DOCKER_MACHINE_EV3)),)
	DOCKER_MACHINE_ENV = @echo "\n Docker environment missing, run: eval \"\$$(docker-machine env default)\"\n" && exit 1
endif
ifeq ($(strip $(DOCKER_MACHINE_EV4)),)
	DOCKER_MACHINE_ENV = @echo "\n Docker environment missing, run: eval \"\$$(docker-machine env default)\"\n" && exit 1
endif

DOCKER_MACHINE_START := @echo "Docker machine '$(DOCKER_MACHINE)' already running"
DOCKER_MACHINE_STATUS := $(shell docker-machine status $(DOCKER_MACHINE))
ifneq ($(DOCKER_MACHINE_STATUS),Running)
	DOCKER_MACHINE_START := docker-machine start $(DOCKER_MACHINE)
endif

DOCKER_MACHINE_IP := @docker-machine ip $(DOCKER_MACHINE)
DOCKER_MACHINE_IP_NOW := $(shell docker-machine ip $(DOCKER_MACHINE))


BASE_COMMAND = docker-compose -f docker-compose.yml
status = $(shell docker-machine status default)

.PHONY: build up test setup dobuild dorun clean dotest status

help:
	@fgrep -h "##" $(MAKEFILE_LIST) | fgrep -v fgrep | sed -e 's/\\$$//' | sed -e 's/##//'

up:  dorun
build:  dobuild
test:  dotest
clean:  stop doclean
stop:  dostop
setup: dosetup build
dosetup:
	$(DOCKER_EXISTS)
	$(DOCKER_MACHINE_EXISTS)
	$(DOCKER_MACHINE_RUNS)
	$(DOCKER_MACHINE_ENV)
	$(BASE_COMMAND) up -d cassandra
	@sleep 3.5
	$(BASE_COMMAND) run webapp mvn clean install

dobuild:
	$(DOCKER_MACHINE_ENV)
	$(BASE_COMMAND) build

dorun:
	$(BASE_COMMAND) up

dostop:
	$(BASE_COMMAND) stop

doclean:
	$(BASE_COMMAND) rm --all -f -v

dotest: ## run test cases
	$(DOCKER_MACHINE_ENV)
	$(BASE_COMMAND) run webapp mvn clean install

status:
	docker-machine ls
	@echo
	$(BASE_COMMAND) ps
	@echo
	@echo
	docker-machine ip $(DOCKER_MACHINE)
	@echo
