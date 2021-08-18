# Repro Steps

First, start Zipkin

Second, start this app and run the following HTTP commands:

```bash
http -a user:password :8080
http -a user:wrong :8080
```

The first will generate a trace in Zipkin that has the authentication span as a child of the request span.

The second will generate a trace where the authentication is disjoint from the request span.
