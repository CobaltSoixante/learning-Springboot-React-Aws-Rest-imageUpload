package com.rosewine.awsimageupload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Interesting: I seem to remember that in Java ANY class can have a "main()" method: I used to piggy-back on this fact to do unit-tests -
 * before I started using JUnit.
 *
 * HOWEVER: in order to get IntelliJ to "play ball" with me - I had to rename this class (and its file, of course) from
 * AwsImageUploadApplication to "Main": curious. I am probably missing something here, I don't believe this should have been a necessary step.
 *
 * Yadda yadda yadda - above probably not really required in "pure" Java world - read AW_readme.txt file:
 (*) When I attempted running the backend I got a furphy: command line too long.
 I had to add to .idea/workspace.xml the following line at recommendadtion of stackflow:
 <component name="PropertiesComponent">
 <property name="RunOnceActivity.OpenProjectViewOnStart" value="true" />
 <property name="RunOnceActivity.ShowReadmeOnStart" value="true" />
 ===>line i added:     <property name="dynamic.classpath" value="true" />
 </component>
 */
//@SpringBootApplication(exclude = {ContextInstanceDataAutoConfiguration.class, ContextStackAutoConfiguration.class, ContextRegionProviderAutoConfiguration.class})
@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

}
