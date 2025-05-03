docker run -d --name myjetty-container -p 8081:8080 -v $(pwd)/build/libs/jfr:/var/lib/jetty/jfr-recordings myjetty
