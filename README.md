Devendra Sauce Demo UI Automation -selenium cucumber automation
System Requirement:
JDK 17 or above
Maven 3.8.1 or above
Intellij IDE or any other of choice in case there is need to update script.(optional)

For execute of scripts on Chrome or Internet explorer you need to have executable files for both drivers respectively and paste them at location "\src\test\resources\drivers" in project folder
you need to download these executable files from below links
Chrome:
Internet Explorer:
please follow the instructions to execute the tests on local:

checkout the code from stash
According to the test scope use following commands

To execute the test suite:  mvn clean verify -DtestXml="Login_TestNG.xml" -Dtier=stage -Dbrowser=chrome -Dseleniumserver=local -DsauceUser="standard_user" -DsaucePassword="secret_sauce"

To execute (sample command):  mvn clean verify -DtestXml="Login_TestNG.xml" -Dtier=stage -Dbrowser=chromeOrAny -Dseleniumserver=localOrRemote -DsauceUser="username" -DsaucePassword="password"

All these parameters also have a default value present in config file.

Result Files:
The Test Execution Results will appear under target folder

Jenins Url: // need to work on that part will do soon