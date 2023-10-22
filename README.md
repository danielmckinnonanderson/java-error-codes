# Java Error Codes

A small Spring Boot application to demonstrate using custom exceptions
with `ControllerAdvice` to send error codes to the client.


## Quick Start

This project uses Java 21.

Start the application:
```console
$ ./gradlew build
$ ./gradlew bootRun
```

Use curl to test the various endpoints in the `FormController`:
```console
$ curl http://localhost:8080/form/ -X 'POST' \
>  -d '{ "favorite_color": "purple", "pick_a_number": 42 }'
```

Errors will occur in the server intermittently (by design),
and if necessary / possible some errors will return a specific
`error_code` in their response body.


## Rationale

Consider a web server which is the API for a GUI-based web app.
Our example web server needs to process a form, and then send the processed form
to another API over the network.

In a real system, there are many points of failure during this ostensibly simple flow.

What if the outside API returns an error?
What if our own database has an issue and cannot persist?
What if the form isn't even valid, and we can't send it out to the other API?
Finally, what if something completely unexpected happens, like one of our own internal APIs
throwing a `NullPointerException` due to `@NonNull` on a method arg we thought should always have been defined?
Real software on real machines is very complicated.

Our web app is not a general purpose API, it is the backend for a GUI.
Therefore, our users are the individuals who use our GUI.

These users are not technical people.
Additionally, a sufficiently complicated system can produce errors which are very difficult to replicate or diagnose.
Add to this complexity the fact that these kinds of correspondences often occur over email.

How can we make debugging these kinds of nasty production issues easier and less time-consuming?

## The Answer

Create custom exceptions that extend `RuntimeException`.
Each of these exception classes has a `public static final` error code.
Using Spring's `ControllerAdvice` we can set up a safety net to catch exceptions that would have otherwise fallen through the cracks.
When we reply to the client after catching one of these exceptions, we can send our new error code as well if we have one.
Then, when our users see an error code they can supply it to the developers immediately.
Since the error codes are unique (to the exception type), receiving one will significantly limit the blast radius the developers need to examine while debugging.


## So you're basically just using `RuntimeException`s like `GOTO`? Isn't that [considered harmful](https://en.wikipedia.org/wiki/Considered_harmful)?

:)




