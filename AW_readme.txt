https://www.youtube.com/watch?v=i-hoSg8iRG0
Spring Boot Tutorial | Spring Boot Full Stack with React.js | Full Course | 2021
This is Nelson's "amigoscode" course on the subject.

MISCELANEIOUS NOTES:
(*) When U create a package: memory serves me that it should ALWAYS be lower case (upper-case characters won't kill u, but it is NOT a good idea).

External/required entities we passed through during the course:
(*) Spring Initializr (1 - Spring Initializr) - to create a very rudimentary (and powerful) RESTful backend Java application to use in IntelliJ.
(*) https://mvnrepository.com/artifact/com.amazonaws/aws-java-sdk - (A Maven Repository 2 - IntelliJ and AWS SDK) - to add AWS-SDK to our Java application.
(*) aws.amazon.com (3 - AWS Credentials) - to create a AWS account.
(*) aws.amazon.com (5 - Creating S3 Bucket) - to create a S3 bucket in our AWS account.

1 - Spring Initializr - 00:02:00
(*) If we search "Inintializr" we get to the desired website: https://start.spring.io/
(*) Select:
Project: Maven (as opposed to Gradle)
Language: Java
Spring Boot: dunno what to pick: I use default: 2.5.2
Project Metadata: GROUP - com.rosewine ; ARTIFACT - aws-image-upload
Packaging: Jar (default)
Java version: 11 (default)

Dependencies:
-------------
Spring Web
Build web, including RESTful, applications using Spring MVC. Uses Apache Tomcat as the default embedded container.
-------------
We could add more dependencies at this stage, this is the power - and flexibility of Spring-Boot;
it is also an "inflexibility"...
We don't seem to be employing this "power" at this stage: perhapse we are unsure of what other dependencies we need yet -
beyond preliminary suspects that "Spring Web provides": (1) Spring MVC (2) Apache Tomcat [Java servlet container]
No matter - to overcome this Spring-Boot "inflexibility" we will be adding more dependencies MANUALLY later on to our MAVEN pom.xml file .

(*) Now click [GENERATE] to create the project.

2 - IntelliJ and AWS SDK
(*) I tend to extract my resultant Initializr to beneath a directory D:\IdeaProjects - which is dedicated to my IntelliJ projects.
(*) Right clicking the pom.xml opens the entire project, and spurs Maven to resolve required dependencies.
(*) Now to add AWS to the Maven dependencies to the project - so I can exploit is S3 buckets.
- In google search for "maven aws java sdk"
- The 3rd link - "com.amazonaws » aws-java-sdk - Maven Repository" - points to what I need: https://mvnrepository.com/artifact/com.amazonaws/aws-java-sdk
  NOTE how we are getting the aws-java-sdk from the main Maven repository - and not from "1- Spring Initializr" in the last section: dunno why: maybe Spring Initializer does not provide it.
- Click on latest version version of the AWS SDK for java - it is at the top of the list. I selected 1.12.22 dated Jul, 2021. I suppose you should ensure that the "repository" is 'Çentral' (and not 'Master' or somesuch).
- From the [Maven] tab - select and copy all the XML (it is a complete <dependency> clause): this enables us to add this Java/AWS/SDK version to our project.
- In our local 'pom.xml' file - add this selection to the <dependencies> clause (as an addition to all the pre-existing <dependency> clauses.
- I [right-clicked] the 'pom.xml', selected [Maven], and then - [Reload Project]. It took time, there MUST be a better way of doing this!

3 - AWS Credentials - 00:06:31
I already have long standing AWS credentials from my BDE (Basic Data Entry) Android project, but will follow Nelson's narrative as a refresher.
Unless you are using medium to large traffic on AWS: you don't get billed (I've never been billed). Just - don't leave your credentials lying around on your Github account.
(*) Go to aws.amazon.com - CREATE an account if you don't already have one. I have one, and have logged in.
(*) I already have a (1)access-key (2)secret : I am not generating these. (The course shows u how).

4 - Amazon S3 Client - 00:09:27
We now use our (1)access-key and (2)secret-key so we can access our AWS account to create and manipulate a S3 bucket (via our Java AWS SDK client).
(*) Inside our project I have: com.rosewine.awsimageupload/AwsImageUploadApplication.java
(*) Under                      com.rosewine.awsimageupload                                  - right-click to create a new package 'config':
                               com.rosewine.awsimageupload.config
    Create a new class file:   com.rosewine.awsimageupload.config/AmazonConfig
    We precede this class with a @Configuration annotation - which I believe will be the indication to spring to connect to our AWS account via our (1)access-key (2)secret-key

    SO: we seem to have a main package: com.rosewine.awsimageupload, under which we are creating subservient packages, such as com.rosewine.awsimageupload.config .
(*) Inside the above package create a class 's3': com.rosewine.awsimageupload.config/AmazonConfig::s3() .
    It returns a S3 client to the caller.
    Right now it is taking our access-key and secret-key as hard coded values:
    OBVIOUSLY, this cannot be published to Github in this way!
(-) I have since added a couple of classes that will grab the keys out of "C:/temp/accessKeys.csv" - so anyone can still use this, and I don't reveal my keys if I publish this on Github.
(*) I was getting an exception:
Caused by: org.springframework.beans.BeanInstantiationException: Failed to instantiate [com.amazonaws.services.s3.AmazonS3]: Factory method 's3' threw exception; nested exception is com.amazonaws.SdkClientException: Unable to find a region via the region provider chain. Must provide an explicit region in the builder or setup environment to supply a region.
At 1st I moved my S3 to region US-EAST-1, because that's where I seem to have set up my original free Amazon account. Didn't help.
I ended up adding an extra line to AmazonConfig::s3():
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.US_EAST_1)  // aw: I added this because I was getting a Region related exception. I wish now I had kept my S3 bucket in Sydney, but - too much stuffing around.
                .build();

5 - Creating S3 Bucket - 00:13:35
Create the S3 bucket we will upload-images-to and display-them-from.
(*) In your aws.amazon.com account- which you are probably already logged into - find your way to the "S3" section.
    I did this by clicking the "Services" menu button at the top-left, and then clicking "S3" from the Storage options.
(*) Press [Create bucket] to create a new bucket. I assigned mine a bucket-name of "rosewine-image-upload-123".
    For my region I selected "Asia Pacific (Sydney)" - I live in Sydney, but I hope I won't regret this, because I set up my AWS account aeons ago, and don't remember what is happening there.
(*) At the bottom of the screen I click [Create Bucket]
(*) Under                           com.rosewine.awsimageupload                              - right-click to create a new package 'bucket':
    Create a new Java/ENUM file:    com.rosewine.awsimageupload.bucket/BucketName
    And add our bucket name:
       PROFILE_IMAGE("rosewine-image-upload-123");
    We additionally create ONLY
    (1) a CONSTRUCTOR in Bucketname.java - that initializes a private String variable named "bucketName"
    (2) a GETTER for for variable bucketName

6 - Saving files to S3 Bucket - 00:18:27
We want a mechanism to save employee photos from our local machine to our S3 bucket.
(*) Under                           com.rosewine.awsimageupload                              - right-click to create a new package 'filestore':
    Create a new Java/Class file:   com.rosewine.awsimageupload.filestore/FileStore
    @Service    // indicates that this is the 2nd tier - the Service/business-logic tier - in our backend.
    I find this interesting, because we are beginning at the middle - with our @Service (business logic) 2nd tier - prior to our @Controller (1st tier). or DB-access (3rd tier).
(*) Create simple CONSTRUCTOR method.
(*) Create a "save" method - to save an image from our local storage to our S3 bucket.

7 - User Profile Model - 00:25:34
We now build a backend API 1st tier (@Controller ? - not yet, this is the model, the abstract protagonist of our app), (AND a backend API 3rd tier (DB-access) in section 8) -
this, in anticipation of building the HTML/react(or whatever) front-end.
(*) FIRST, we create the actual CLASS for the "user" - the core "protagonaist" of our application:
    Under                           com.rosewine.awsimageupload                              - right-click to create a new package 'profile':
    Create a new Java/Class file:   com.rosewine.awsimageupload.filestore/UserProfile (Our model class)
    Created 3 new fields:
    private UUID userProfileId;
    private String username;
    private String userProfileImageLink;    // S3 key
    GENERATE: (1) CONSTRUCTOR (for all) (2) GETTERs/SETTERs (for all)
    XXXX We annotate the above class as @xxxxxxxxx ? - no. - BUT - no worries: in section "9 - API & service layers - 00:34:35" we will create the class:
    @RestController
    com.rosewine.awsimageupload.filestore/UserProfileController (our 1st level API tier)

(*) We GENERATE the "equals" & "hashcode" methods for the object

8 - Create in-memory database - 00:30:24
Create a fake database where we return some fake data to our clients (database is inside a makeshift Java memory list: dunno why we didn't make something more "ambitious"):
(*) Under                           com.rosewine.awsimageupload                              - right-click to create a new package 'datastore':
    Create a new Java/Class file (@Repository):   com.rosewine.awsimageupload.datastore/FakeUserProfileDataStore (@Repository)
    @Repository     // Indicates that this is the 3rd tier - database-access - is our backend.
    NOTE:
    "com.rosewine.awsimageupload.datastore" (@Repository - 3rd tier/dbaccess) is the package that represents our "fake/toy" user database -
    as opposed to the package -
    "com.rosewine.awsimageupload.filestore" (@Service - 2nd tier/busLogic) that represents REAL images on a S3 bucket.

9 - API & service layers - 00:34:35
(*) Under                           com.rosewine.awsimageupload.profile (we already created this package)
    Create a new Java/Class file:   com.rosewine.awsimageupload.filestore/UserProfileController (Our 1st tier API @RestController, @RequestMMapping("api/v1/user-profile") class)
(*) Create Service class (no annotation):                            com.rosewine.awsimageupload.filestore/UserProfileService
(*) Create class that interacts with the database (@Repository):   com.rosewine.awsimageupload.filestore/UserProfileDataAccessService (@Repository)
    constructor is:
    @Autowired
    public UserProfileDataAccessService(FakeUserProfileDataStore fakeUserProfileDataStore) {
        this.fakeUserProfileDataStore = fakeUserProfileDataStore;
    }
(*) com.rosewine.awsimageupload.filestore/UserProfileService
    This is where all the main-work/business-logic happens.
(*) When I attempted running the backend I got a furphy: command line too long.
    I had to add to .idea/workspace.xml the following line at recommendadtion of stackflow:
  <component name="PropertiesComponent">
    <property name="RunOnceActivity.OpenProjectViewOnStart" value="true" />
    <property name="RunOnceActivity.ShowReadmeOnStart" value="true" />
===>line i added:     <property name="dynamic.classpath" value="true" />
  </component>

10 - Upload image API - 00:41:46
In our fake database/class-instance FakeUserProfileDataStore - we want to
1. Upload the user-image to S3.
2. Update the database to point to the correct image-link. (we're not even displaying anything yet - just the above step of uploading image-file to S3 is ambitious enough).
(*) In UserProfileController - our 1st tier API backend class - we define a method
    [public void] UserProfileController.uploadUserProfileImage(@PathVariable("userProfileId") UUID userProfileId,
                                       @RequestParam("file") MultipartFile file) {
(*) And (2nd tier):
    [public void] UserProfileService.uploadUserProfileImage(UUID userProfileId,
                                       MultipartFile file) {

11 - Check list to upload images - 00:46:21
    This is an exercise left for us to implement for an upcoming chapter:
    /*public - is default*/ void UserProfileService.uploadUserProfileImage(UUID userProfileId, MultipartFile file) {
        // 1. Check if image is not empty
        // 2. Check if file is an image
        // 3. Check if the user exists in our database
        // 4. Grab some metadata from file - if any
        // 5. Store the image in S3 and update database with S3 image link
    }

* - I am ready to upload the initial version of this project to Github.
    I am satisfied that my "secret" access key, or any keys - are inaccessible. The project still works.

# INITIAL STORE INTO A GITHUB MASTER/MAIN BRANCH
# ==============================================
git init . # Create new git repository at current directory.
git status # Shows current git branch and other stuff about the branch.
git add . # Add all files in staging area AND downwards...
git commit -m "..." # Commit [locally] all modified files in the staging area.
git log # gives a terse commit history, including commit HASH, MESSAGE, DATE-TIME STAMP.
git branch # tells u what branch ur on in ur local machine.
git branch -M main # Renames ur current local branch to 'main' - EG from 'master' to 'main' (which we did in preparation for moving our local 'master' branch to out GITHUB 'main' branch,
 # cuz fr sum fing reason GITHUB decided to rename the remote master branch from master to main.
git remote add origin https://github.com/CobaltSoixante/learning-Springboot-React-Aws-Rest-imageUpload # Specify the remote repository we will interact with from our local client.
#
# In my Github account MAIN page (that I log into with my password) - create a new repository:
# learning-#Springboot-React-Aws-Rest-imageUpload
# (EXCEPT FOR THE ABOVE NAME - ACCEPT all defaults (which basically means not adding a lot of stuff that u can add later)).
#
git push -u origin main # PUSH from our local repository to remote (EG github) repository.

# At this point - if we only want to manipulate our main/master branch - we can just do the following:
#
# 1. Make our changes at the client/IDE end.
git add . # 2. Add all files in staging area AND downwards...
git commit -m "..." # 3. Commit [locally] all modified files in the staging area.
# -. Perform steps 1-3 repeatedly according to taste, I guess, and finally:
git push -u origin main # 4. PUSH from our local repository to remote (EG github) repository.

# PERSONAL LOCAL DEVELOPMENT BRANCH
# =================================
# BUT - for a more CONTROLLED practice: we use PERSONAL LOCAL BRANCHES - the way Github is MEANT to be used:
# We continue by creating an individual TEMPORARY BRANCH for ourselves (or someone creates for us) for each of our individual milestones/features (one at a time, at the time we are working on the particular feature).
# As long as we are working locally - we'll need to (1) commit locally, (2) and ABSORB changes from the remote master/main - so we don't "fall out of step".
git branch # check which LOCAL branch you are on.
git branch feature-a # CREATE a new branch from the CURRENT branch you are using (seems to stay on the same current branch...)
git checkout feature-a # goto the branch u wanna get to
#
# And then:
# 1. Do any work U want LOCALLY on the local copy of the branch via the IDE
git add . # 2. Add all files in staging area AND downwards...
git commit -m "..." # 3. Commit [locally] all modified files in the staging area.
# -. Perform steps 1-3 repeatedly according to taste, I guess, and finally:
git push -u origin feature-a # 4. takes the CURRENT branch, and uploads it to a feature-a branch on the REMOTE repository.

# PERIODICALLY ASSIMILATING THE MAIN PROJECT BRANCH INTO YOUR LOCAL DEVELOPMENT BRANCH
# ====================================================================================
# WITH THE ABOVE APPROACH ("PERSONAL LOCAL DEVELOPMENT BRANCH") - we need to periodically (once a day or two) assimilate-consume-merge (whatever u wanna call it) our ongoing changes LOCALLY to the MASTER/MAIN REMOTE branch into our local feature-development branch -
# so that we don't fall "out of touch" with the main corporate branch, and we can manually sync our local changes (LOCALLY) with the main remote branch,
# so that when we are finally "reviewed by corporate" - checking us into the main branch is either seamless or without much effort.

12 - Facebook Create-react-app - 00:49:04
We now create the React FRONTEND (until now we were doing a Java backend).
(*) We initially speed-track (boot-strap) our frontend React app" (we'll need Node.js installed for this - just so we have npx):
    - In Google, search for 'create react app'
    - The link for us is: "facebook/create-react-app: Set up a modern web app ... - GitHub"
    - Go down on the page - you'll see:
    #Quick Overview
    ### AW: wherever it says 'my-app' - replace with 'frontend' - this is our directory for the FRONTEND.
    npx create-react-app my-app
    cd my-app
    npm start
(*) On our LOCAL machine/projects:
    cd src/main  # This is where our projects already exists, the BACKEND already exists under the 'java' subdirectory.
    npx create-react-app frontend # "bootstrap" a basic React app in a subdirectory 'frontend'
    cd frontend
    npm start
(*) while STILL in branch section12-Facebook-Create-React-App
    git commit -m "facebook/create-react-app: Set up a modern web app ... - GitHub"
(*) git push -u origin section12-Facebook-Create-React-App
(-) At REMOTE - am PULLING to main/master, until I figure out hot to REBASE my local to main/master.

13 - Components and Axios - 00:53:36
(-) By virtue of issuing the "npm start" command, our bare-bones React front end is visible on the browser on http://localhost:3000/ .
(-) We are not using Intellij-Enterprise - which has Javascript support; rather - we are using IntelliJ-Community; so we use VS-Code for Javascript...
(*) In VS-Code - Under [File, Open WorkSpace...] - I [open: D:\IdeaProjects\learning-Springboot-React-Aws-Rest-imageUpload\src\main\frontend] folder
(*) in main/frontend/app.js - change "Learn React" to "Learn React Hello React" - will reflect immediately in the browser.
(*) in " - delete entire header section - we don't need it. We'll see in browser that it is empty.
(*) We download Axios - enables performing HTTP requests to backends.
    - cancel all http servers in IntelliJ/Java and Source-Code front-end.
    - Download axios from a terminal (00:56:00):            <------------------------------------------------
      # Go to a regular terminal
      cd /cygdrive/d/IdeaProjects/learning-Springboot-React-Aws-Rest-imageUpload/src/main/frontend
      npm -S i axios # i stands for install
      npm start # start the react-app server (also yields our - now -  blank client screen on http://localhost:3000/)
(-) Lotta magic; mainly at VisualCode/App.js front-end/back-end(?) - AW: I don't think this is the backend: our SPRING-JAVA stuff is the back-end.
...am now up to roughly 01:01:30: am getting the expected errors in browser; am resolving...
# At first bad issues are apparent on the main browser client display itself ("no return statement" [from App.js::UserProfiles() - I guess the return of HTML code by a outer function/lambda (but not inner) is mandatory]);
then - when things apparently look good, we detect further errors from the Chrome console display...
# We get an error -
Uncaught (in promise) Error: Network Error
    at createError (createError.js:16)
    at XMLHttpRequest.handleError (xhr.js:84)
THIS is happening because the IntelliJ backend is not up-and-running, so we run it...
# We now address a CORS error on the console:
Access to XMLHttpRequest at 'http://localhost:8080/api/v1/user-profile' from origin 'http://localhost:3000' has been blocked by CORS policy: No 'Access-Control-Allow-Origin' header is present on the requested resource.
This is resolved ad-hoc by adding the @CrossOrigin annotation to UserProfileController.java:
@RestController // 1st tier (API) of the app
@RequestMapping("api/v1/user-profile")
@CrossOrigin("*")   // Addresses a CORS problem we encounter at before 01:03:41 : this will allow strange hosts (other than the client) to communicate with us.
    // Is not something we should normally do in production - but will enable our React component's backend to communicate with us for now.
    // Instead of "*" (everything) we could have limited this to just "localhost:3000" (our React backend) - but - what the hell, we're just testing, lets enable everything.
public class UserProfileController {...}
# Restart IntelliJ (8080) (and React (3000) if necessary), refresh React 3000 client -
No errors on console. Client main-screen just says "Hello", BUT - if I unfold the "{data: Array(2), status: 200, statusText: "", headers: {…}, config: {…}, …}" bit of the Console window: I see my 2 users:
data: Array(2)
0: {userProfileId: "59eb8bf1-ccef-4026-8ce4-1919efe5346a", username: "janetjones", userProfileImageLink: null}
1: {userProfileId: "db188710-3653-4115-98da-258ef6676cd1", username: "antoniojunior", userProfileImageLink: null}
length: 2
[[Prototype]]: Array(0)
===WHY is this getting logged to console? I guss because my App.js component is logging it, the component is embedded in the index.js file, and index.js embeds the App[.js] component (even though we never touched the index.js file).
Magic: this entire React frontend is really a [node.js / npm ?] server (on local:3000) that spits out 2 things to the Chrome client:
1. "Hello" - displayed on the Crome client main window.
2. The 2 users data that are output to the console.
And Nelson is saying "cool, we've managed to upload information from our backend (localhost:8080) and print it on the console (localhost:3000)":
I am still trying to grasp what is happening here: we now seem to have 2 backends:
1. IntelliJ/Java on localhost:8080 that is our TRUE backend and spits out user data to a client (as REST/json protocol/format)
2. React on localhost:3000 that spits out the user data to the console of the Chrome tab accessing localhost:3000.
I hope this gets clarified in sections to come.

14 - Rendering User Profile - 01:04:50 (//#^ in App.js)
Take the data-array we had been displaying to the "ersatz" Console screen, and display it to the proper browser clients screen
See comments -
//#^
in frontend/App.js
Too much magic: perhaps because of Axiom: it is probably a very active participant, but also very behind-the-scenes / invisible.

15 - React Dropzone - 01:09:20
Allows us to send/drop files to our server.
See comments -
//#+
(*) On GITHUB goto react dropzone: https://github.com/react-dropzone/react-dropzone
(*) Install as per site instructions:
Installation
Install it from npm and include it in your React build process (using Webpack, Browserify, etc).
npm install --save react-dropzone # from my [frontend?] terminal - does it make a difference from where I install it?
=> restart frontend again: npm start : get same 'ol.
(*) For each user profile - we add the area to drop a image for each user:
bit of code-stealing from GITHUB react-dropzone page
(*) For each user add the <MyDropzone/> component we stole from GITHUB which we rename to Dropzone).

16 - Pexels - 01:15:50
(*) pexels.com - download free images.

17 - UI Logic to send files to backend - 01:18:32
We want to upload our image files to our server. This deals with the frontend, ergo:
(-1) not uploading image-file to S3 yet...
(-2) not updating S3-image-file-ling in database yest...
See comments -
//#% 17 - UI Logic to send files to backend

18 - Increase servlet max file size - 01:25:25
(*) Try drag-and-drop AntonioJunior imaje file:
On the front-end server (React :3000) console we see errors.
On the bacc-end server (IntelliJ :8080) console we see: The field file exceeds its maximum permitted size of 1048576 bytes...
(*) So we gotta override some defaults:
in the [currently empty] resources/application.properties insert the line
spring.servlet.multipart.max-file-size=50MB

19 - Exercise - 01:29:35
... Where we get told to implement the logic of UserProfileSerice.java::uploadUserProfileImage on our own - as an exercise.
    /**
     *
     * @param userProfileId
     * @param file
     */
    /*public - is default*/ void uploadUserProfileImage(UUID userProfileId, MultipartFile file) {
        // 1. Check if image is not empty
        // 2. Check if file is an image
        // 3. Check if the user exists in our database
        // 4. Grab some metadata from file - if any
        // 5. Store the image in S3 and update database (userProfileImageLink) with S3 image link
    }

20 - Lets Implement uploadUserProfileImage() - 01:31:02

21 - Lets test things - 01:44:42
NOTE: =====> I did not implement a lot of Nelson's mods here: they are mostly
(1) beautifications
(2) a HTTP 500 error that seemed to get resolved by simply replacing file.getname() with file.getOriginalFileame, without all the other crap
    in UserProfileService::uploadUserProfileImage() in step-5 in the business logic.

22 - Set user profile image link - 01:53:41
Our "great" departure here will be to hardcode the UUID's of our users (janetjackson & antoniojunior)
so we can create a PREDICTABLE user-profile-image-link that we can update in our database.

23 - Lets implement download() images [IntelliJ backend] - 01:59:02 <*** UserProfileService::downloadUserProfileImage - bears looking into

24 - Implement download images on frontend - 02:08:23
Modify the React/JS frontend to actually DISPLAY the image stored for each user in our S3 bucket.

25 - Final touches - 02:13:15 <---
The images re downloaded in previous chapter are very big: we need to resize them. <---
