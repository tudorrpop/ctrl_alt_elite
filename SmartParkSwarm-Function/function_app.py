import azure.functions as func
import logging
import os
import json

from azure.identity import DefaultAzureCredential
from azure.mgmt.resource import ResourceManagementClient
from azure.mgmt.resource.resources.models import DeploymentMode

app = func.FunctionApp(http_auth_level=func.AuthLevel.FUNCTION)

@app.route(route="containerapp_creator")
def containerapp_creator(req: func.HttpRequest) -> func.HttpResponse:
    logging.info('Processing request to create Container App.')

    try:
        req_body = req.get_json()
    except ValueError:
        return func.HttpResponse("Invalid JSON in request body.", status_code=400)

    name = req_body.get('name')
    if not name:
        return func.HttpResponse("Please provide 'name' in the request body.", status_code=400)

    # Load environment variables
    subscription_id = os.environ.get("AZURE_SUBSCRIPTION_ID")
    resource_group = os.environ.get("AZURE_RESOURCE_GROUP")
    managed_env_id = os.environ.get("MANAGED_ENVIRONMENT_ID")
    location = os.environ.get("LOCATION_NAME", "Poland Central")  # Optional default
    target_port = int(os.environ.get("TARGET_PORT", 80))
    container_image = os.environ.get("CONTAINER_IMAGE")
    container_name = os.environ.get("CONTAINER_NAME")

    if not all([subscription_id, resource_group, managed_env_id, container_image, container_name]):
        return func.HttpResponse("Missing required environment variables.", status_code=500)

    try:
        # Load ARM template
        with open("template.json", "r") as template_file:
            template_body = json.load(template_file)

        # Create client and deploy
        resource_client = ResourceManagementClient(
            credential=DefaultAzureCredential(),
            subscription_id=subscription_id,
        )

        deployment_params = {
            "properties": {
                "template": template_body,
                "parameters": {
                    "containerapp_name": {"value": name},
                    "managedEnvironment_id": {"value": managed_env_id},
                    "location_name": {"value": location},
                    "target_port": {"value": target_port},
                    "container_image": {"value": container_image},
                    "container_name": {"value": container_name},
                },
                "mode": DeploymentMode.incremental
            }
        }

        deployment_result = resource_client.deployments.begin_create_or_update(
            resource_group,
            "containerappDeployment",
            deployment_params
        ).result()

        outputs = deployment_result.properties.outputs
        fqdn = outputs["fqdn"]["value"]

        return func.HttpResponse(f"{fqdn}")

    except Exception as e:
        logging.error(f"Error creating Container App: {str(e)}")
        return func.HttpResponse(f"Error creating Container App: {str(e)}", status_code=500)
