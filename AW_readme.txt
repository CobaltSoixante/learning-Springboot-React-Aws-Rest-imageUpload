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

* - I am ready to upload the initial version of this project to Githup.
    I am satisfied that my "secret" access key, or any keys - are unaccessable. The project still works.
git init . # Create new git repository at current directory.
git status # Shows current git branch and other stuff about the branch.
git add . # Add all files in staging area AND downwards...
git commit -m "first commit: backend only" # Commit [locally] all modified files in the staging area.
git log # gives a terse commit history, including commit HASH, MESSAGE, DATE-TIME STAMP.
git branch # tells u what branch ur on in ur local machine.
git branch -M main # Renames ur current local branch to 'main' - EG from 'master' to 'main' (which we did in preparation for moving our local 'master' branch to out GITHUB 'main' branch,
 # cuz fr sum fing reason GITHUB decided to rename the remote master branch from master to main.
git remote add origin https://github.com/CobaltSoixante/learning-Springboot-React-Aws-Rest-imageUpload # Specify the remote repository we will interact with from our local client.
#
# In my Github account MAIN page (that I log into with my password) - create a new repository:
# learning-Springboot-React-Aws-Rest-imageUpload
# (EXCEPT FOR THE ABOVE NAME - ACCEPT all defaults (which basically means not adding a lot of stuff that u can add later)).
#
git push -u origin main # PUSH from our local repository to remote (EG github) repository.

12 - Facebook Create-react-app - 00:49:04
