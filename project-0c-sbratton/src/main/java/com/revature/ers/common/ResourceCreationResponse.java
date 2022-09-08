package com.revature.ers.common;

public class ResourceCreationResponse {


    private String resourceId;

    public ResourceCreationResponse(int resourceId) {
        this.resourceId = String.valueOf(resourceId);
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public String toString() {
        return "ResourceCreationResponse{" +
                "resourceId='" + resourceId + '\'' +
                '}';
    }
}
