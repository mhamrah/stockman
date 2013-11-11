# stockman

Stockman is a [Spray](http://spray.io) web application used to hack various things in Scala. It is not meant for anything useful except to integrate something into a simplified application.

It requires Cassandra v2. On a Mac you can use ifconfig to
add multiple loopback ip addresses. This can be helpful for
Akka clustering as well.

```
sudo ifconfig lo0 alias 127.0.0.2
sudo ifconfig lo0 alias 127.0.0.3
sudo ifconfig lo0 alias 127.0.0.4
sudo ifconfig lo0 alias 127.0.0.5
```

## Angular and Bower

The ```/ui``` folder features a yeoman-angular project. The
grunt server adds a proxy to the spray backend.
