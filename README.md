GDPR-Webcheck
=============

> Perform a basic check for potential GDPR problems with websites

Performed checks
----------------

### Third-Party hosts
List all hosts that are contacted by the client before obtaining user consent.


Prerequisites
------------

* Firefox
* Mozilla Geckodriver ([mozilla/geckodriver](https://github.com/mozilla/geckodriver))

How to run
----------

1. Modify the paths in `application.properties` according to your system configuration
2. Start the application with maven: `mvn spring-boot:run`
3. Access `http://localhost:8090` with your webbrowser