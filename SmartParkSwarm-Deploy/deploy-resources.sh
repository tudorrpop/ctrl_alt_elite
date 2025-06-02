#!/bin/bash

rg_name="smartparkswarm"
sa_name="sasps"
location="polandcentral"
acr_name="smartparkswarm"
functionapp_name="createworkers"
identity_name="identity-sps"
vnet_name="sps-vnet"
subnet_name="sps-subnet"
vnet_cidr="10.0.0.0/16"
subnet_cidr="10.0.0.0/24"
containerappenv_name="managedEnvironment-sps"
app_name="createworkers"

az group create \
  --name $rg_name \
  --location $location

az acr create \
  --location $location \
  --name $acr_name \
  --resource-group $rg_name \
  --sku Standard

docker build -t $acr_name.azurecr.io/orchestrator:latest -f SmartParkSwarm-Back/Dockerfile SmartParkSwarm-Back/
docker build -t $acr_name.azurecr.io/mobile:latest -f SmartParkSwarm-Mobile/Dockerfile SmartParkSwarm-Mobile/
docker build -t $acr_name.azurecr.io/smartparkswarm:latest -f SmartParkSwarm-Front/Dockerfile SmartParkSwarm-Front/
docker build -t $acr_name.azurecr.io/worker:latest -f SmartParkSwarm-Worker/Dockerfile SmartParkSwarm-Worker/

az acr login -n $acr_name

docker push $acr_name.azurecr.io/orchestrator:latest
docker push $acr_name.azurecr.io/mobile:latest
docker push $acr_name.azurecr.io/smartparkswarm:latest
docker push $acr_name.azurecr.io/worker:latest

az network vnet create \
  --name $vnet_name \
  --resource-group $rg_name \
  --address-prefixes $vnet_cidr \
  --subnet-name $subnet_name \
  --subnet-prefixes $subnet_cidr \
  --location $location

vnetId=$(az network vnet subnet show --resource-group $rg_name --vnet $vnet_name --name $subnet_name --query id -o tsv)

az network vnet subnet update \
  --ids $vnetId \
  --delegations Microsoft.App/environments


az containerapp env create \
  --name $containerappenv_name \
  --resource-group $rg_name \
  --location $location \
  --enable-workload-profiles \
  --logs-destination none \
  --infrastructure-subnet-resource-id $vnetId

az containerapp env identity assign \
  --name $containerappenv_name \
  --resource-group $rg_name \
  --system-assigned

principalId=$(az containerapp env identity show --name $containerappenv_name --resource-group $rg_name --query principalId -o tsv)
acrId=$(az acr show --name $acr_name --query id --output tsv)
az role assignment create \
  --assignee-object-id $principalId \
  --assignee-principal-type ServicePrincipal \
  --role acrpull \
  --scope $acrId

az storage account create \
  --name $sa_name \
  --location $location \
  --resource-group $rg_name \
  --sku Standard_LRS

az functionapp create \
  --name $functionapp_name \
  --storage-account $sa_name \
  --flexconsumption-location "westeurope" \
  --resource-group $rg_name \
  --os-type Linux \
  --runtime python \
  --runtime-version "3.12" \
  --functions-version 4

app_id=$(az ad app create --display-name $app_name --query id -o tsv)
APPLICATION_ID=$(az ad app show --id $app_id --query appId -o tsv)
TENANT_ID=$(az account show --query tenantId --output tsv)
APP_SECRET=$(az ad app credential reset \
  --id $app_id \
  --append \
  --query password \
  --output tsv)
SUBSCRIPTION_ID=$(az account show --query id --output tsv)
CONTAINERAPP_ENV=$(az containerapp env show --resource-group $rg_name --name $containerappenv_name --query id -o tsv)
CONTAINER_IMAGE="$acr_name.azurecr.io/worker:latest"

SP_ID=$(az ad sp create --id $app_id --query id -o tsv)
az role assignment create \
  --assignee $SP_ID \
  --role "Contributor" \
  --scope /subscriptions/$SUBSCRIPTION_ID

az functionapp config appsettings set \
  --name $functionapp_name \
  --resource-group $rg_name \
  --settings AZURE_CLIENT_ID="$APPLICATION_ID" \
  AZURE_TENANT_ID="$TENANT_ID" \
  AZURE_CLIENT_SECRET="$APP_SECRET" \
  AZURE_SUBSCRIPTION_ID="$SUBSCRIPTION_ID" \
  AZURE_RESOURCE_GROUP="$rg_name" \
  MANAGED_ENVIRONMENT_ID="$CONTAINERAPP_ENV" \
  CONTAINER_IMAGE="$CONTAINER_IMAGE" \
  REGISTRY_SERVER="$acr_name.azurecr.io"

# Deploy the app to Azure

az deployment group create \
	--name OrchestratorDBDeployment \
	--resource-group $rg_name\
	--template-file mysql.json
	
az deployment group create \
	--name OrchestratorDeployment \
	--resource-group $rg_name\
	--template-file orchestrator.json
	
az deployment group create \
	--name FrontendDeployment \
	--resource-group $rg_name\
	--template-file smartparkswarm.json

az deployment group create \
	--name MobileDeployment \
	--resource-group $rg_name\
	--template-file mobile.json


