# Sunshine Forge Front End

## Background

You have been hired as a developer at Sunshine Forge - a young startup that intends to compete with Cloud Foundry.

The API has been created for you, and you are now tasked with creating a front end using React.

For this assignment, you don't yet need to use Router or Redux (we will learn more about those later). 

Instead you will be making a "vanilla" react app using state and props to show and hide components as necessary.

All logic should take place in a RootComponent, and other components should communicate by firing events up to the root.

You will not need any form of caching: just reload the entire state tree after each action the user takes.


## Space Epic

1. As a user, I would like to be able to click a button to add an initial Space if the database is empty.
1. As a user, given that I have clicked "add space", I would like to be taken to a screen that will allow me to set: name, disk, and memory quotas.
1. As a user, I would like to see a sidebar with a list of all Spaces and the percentage of memory consumed.
1. As a user, given that I click on a Space in the Sidebar, I would like to see details of that Space.
1. As a user, given tht I am viewing the details of a Space, I would like to click an edit button to edit that space.
1. As a user, given that I clicked edit on a Space, I would like to see form fields for: name, disk, and memory.
1. As a user, given that I have edited a Space, I would like to be able to click "Update" and be returned to the details for that space.
1. As a user, given that I am editing a Space, I would like to be able to click "Cancel" to return to Space Details without causing changes.
1. As a user, given that I am viewing the details of a Space, I would like to see a list of apps contained under that space.
1. As a user, given that I am viewing the details of a Space, I would like to see a delete button for the space.
1. As a user, given that I click the delete button for a Space, I would like to see a confirmation dialog so that I can cancel deletion.
1. As a user, given that I confirm deletion of a space, I would like to see the sidebar updated with the given space removed.

## Space Mockups

![Add space](../img/add_space.png)
![Create space](../img/space_create.png)
![Space Details](../img/space_details.png)
![Space Edit](../img/space_edit.png)
![Space Delete](../img/space_delete.png)

## App Epic

1. As a user, given that I am viewing the details of a Space, I would like to see an "Add app" button which will allow me to create apps under that space.
1. As a user, given that I click on the "add app" button, I would like to be taken to a screen that will allow me set: name, disk, and memory allocations.
1. As a user, given that I have filled out the fields to create an app, I would like to see a "create app" button that will take me to the details of its parent space.
1. As a user, given that I have created an app, I would like to see it listed under its parent space.
1. As a user, given that I have clicked on an app in the list, I would like to be taken to a screen where I can see details about the app.
1. As a user, given that I am viewing the details of an app, I would like to see an edit button to edit that app.
1. As a user, given that I am editing an app, I would like to be able to change: name, disk, and memory allocations.
1. As a user, given that I have edited an app, I would like to see an "Update" button that will take me back to the parent space after saving to the API.
1. As a user, given that I have edited an app, I would like to see a "Cancel" button that will take me back to the parent space without saving my changes.
1. As a user, given that I am viewing the details of an app, I would like to see a delete button for that app.
1. As a user, given that I click the delete button for an App, I would like to see a confirmation dialog so that I can cancel deletion.
1. As a user, given that I confirm deletion of an app, I would like to be taken to the parent space and to see that my app is no longer in the list.

## App Mockups

![Add App](../img/add_app.png)
![Create App](../img/app_create.png)
![App List](../img/app_list.png)
![App Details](../img/app_details.png)
![App Edit](../img/app_edit.png)
![App Delete](../img/app_delete.png)

## Business Logic and Error Handling

1. As a user, given that I attempt to create an app which exceeds the Space memory quota, I would like to see a friendly error.
1. As a user, given that I attempt to create an app which exceeds the Space disk quota, I would like to see a friendly error.
1. As a user, given that I attempt to edit an app which exceeds the Space memory quota, I would like to see a friendly error.
1. As a user, given that I attempt to edit an app which exceeds the Space disk quota, I would like to see a friendly error.
1. As a user, given that I attempt to decrease the memory quota for a space below what is required for existing apps, I would like to see a friendly error.
1. As a user, given that I attempt to decrease the disk quota for a space below what is required for existing apps, I would like to see a friendly error.

![Create App Disk Quota](../img/create_app_disk_quota.png)
![Create App Memory Quota](../img/create_app_memory_quota.png)
![Edit App Disk Quota](../img/edit_app_disk_quota.png)
![Edit App Memory Quota](../img/edit_app_memory_quota.png)
![Edit Space Disk Quota](../img/edit_space_disk_quota.png)
![Edit Space Memory Quota](../img/edit_space_memory_quota.png)
