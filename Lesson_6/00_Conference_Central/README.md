App Engine application for the Udacity training course.

## Products
- [App Engine][1]

## Language
- [Java][2]

## APIs
- [Google Cloud Endpoints][3]
- [Google App Engine Maven plugin][6]

## Setup Instructions
1. Update the value of `application` in `appengine-web.xml` to the app ID you
   have registered in the App Engine admin console and would like to use to host
   your instance of this sample.
1. Update the values in `src/main/java/com/google/devrel/training/conference/Constants.java` to
   reflect the respective client IDs you have registered in the
   [Developer Console][4].
1. (Optional) Mark this file as unchanged as follows:
   $ git update-index --assume-unchanged src/main/java/com/google/devrel/training/conference/Constants.java
1. mvn clean install
1. Run the application with `mvn appengine:devserver`, and ensure it's running
   by visiting your local server's  address (by default [localhost:8080][5].)
1. Get the client library with `mvn appengine:endpoints_get_client_lib`
1. Deploy your application.


[1]: https://developers.google.com/appengine
[2]: http://java.com/en/
[3]: https://developers.google.com/appengine/docs/java/endpoints/
[4]: https://console.developers.google.com/
[5]: https://localhost:8080/
[6]: https://developers.google.com/appengine/docs/java/tools/maven