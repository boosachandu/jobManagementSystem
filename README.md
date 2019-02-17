# jobManagementSystem
Job Management system with spring batch and quartz scheduler

Description :
Technologies used :
Spring boot
Spring Batch(java config)
For scheduling Quartz(jdbc job store)
In memory database(H2)

Job management System Description :
Created alert system which will get the alerts from upstream and add into our database and one more job run which update the alert age to plus 1.
If any new upstream source come they can send the files in same format in same location and our job system will pick the files and add the data into alert system.

Currently it has two jobs :
Job 1 : it has two steps 
       Step 1 : it read csv file from input location and store the information into h2 database.
                    This step has one reader : Flat input reader from given location, Processor : add the alertage to plus 1 as its day one, Writer : insert data into H2 database.
        Step 2 : Tasklet : once file upload is completed into data base it deletes the file from location.
Trigger : it polls to given input location to every 10sec

Job 2 : it has one Tasklet : update alert age to plus 1
Trigger : it runs every 15sec

How to Run :
please change the location of input file according to your local directory to
class name : BatchConfiguration.java
file formate/example is attached.

Run Spring boot : go to class JobManagementSystemQuartzApplication and run the project as spring boot app.
once the spring boot is running please go to below location for H2 Console :
http://localhost:8080/h2/ 
