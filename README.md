# clj-multishot

Clojure version of [go-multishot] (https://github.com/glorphindale/go-multishot).

Simple HTTP repeater. Receives a GET/POST/other request and sends it to multiple servers. Why use it?

* If you want to duplicate HTTP traffic from production system to a staging one
* If you need to test new production system with traffic from an existing one
* You can't use raw traffic duplication

## Usage

Run in standalone mode

    $ java -jar clj-multishot-0.1.0-standalone.jar [args]
    or
    $ lein run [args]

Or run in development mode with live reloading

    $ lein ring server 8080

## Options

* -port to listen to
* -downstreams is a comma-separated list of downstreams to send requests to

## License

Copyright © 2013 FIXME

Distributed under the Eclipse Public License, the same as Clojure.
