# Twitch Chat Statistics

Do you stream on Twitch? Did you ever want to know how many viewers in your chat are followers? Well, wonder no longer!

## Runtime Requirements

* Java JDK 1.7

## Running it (the easy way)

* Download the latest release zip on Github [here](https://github.com/mgway/chatstat/releases)
* Extract the contents of the zip file

#### Windows
* Run the `run-chatstat.bat` file to start the application
  * If you're wary of random batch files, follow these steps:
    * Shift + right click in the Explorer window containing the extracted files
    * Select "Open command window here"
    * Type "java -jar twitch-chatstat" and then hit tab to autocomplete the rest of the file name
    * It should look like `java -jar twitch-chatstat-0.3.jar`, with potentially different numbers at the end
    * Hit enter to run the application
* Browse to [http://localhost:8080](http://localhost:8080) to see the application
* When you're done with the app, press ctrl+c and then 'y' in the command window for a graceful shutdown, or click the normal close button if you just don't care

#### Linux/OS X
* Open a terminal window and navigate to where you extracted the zip
* Type "java -jar twitch-chatstat" and then hit tab to autocomplete the rest of the file name
  * It should look like `java -jar twitch-chatstat-0.1.jar`, with potentially different numbers at the end
* Hit enter to run it
* Browse to [http://localhost:8080](http://localhost:8080) to see the application
* When you're done with the app, press ctrl+c in that terminal window

## Uncommon Problems
* The JAR is executable, so double clicking it on Windows or OS X will run the application in console-less mode
  * Everything will work as expected in this mode, just be aware that you'll have to kill the application via task manager when you're done
* There is some sort of error about a database in the console?
  * Delete the one (or more) .db files in the application directory. If that doesn't work, submit an issue to this project on GitHub

## Development Requirements

* Maven 3.0+
* Squirrel SQL is useful, but not required

## Building it

* Clone from github
* Run `mvn install`
* Run `mvn spring-boot:run`
* Browse to http://localhost:8080
* Good luck


## To Do/Forthcoming Features
* Subscriber counts
* Flexible retry intervals
* Less noisy viewer count updates

Have a feature idea? While it would be super cool if you could go ahead and implement it yourself and then send me a pull request, 
I'll also be watching the [issues tracker](https://github.com/mgway/chatstat/issues) on GitHub for feature requests

