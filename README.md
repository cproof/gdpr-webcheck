GDPR-Webcheck
=============

> Perform a basic check for potential GDPR problems with Websites

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

1. Modify the paths in `application.properties` according to your system.
2. Start the system with maven: `mvn spring-boot:run`