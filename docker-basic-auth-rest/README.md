- Build & Run
```
root@ruo91:~# docker build --rm -t ssh dockerfile-example/nginx/openresty
root@ruo91:~# docker run -d -p 80:80 -h "openresty" --name "openresty" `docker images | grep openresty | awk '{print $3 }'`
```
- Test
```
root@ruo91:~# curl -I localhost
HTTP/1.1 200 OK
Date: Sat, 10 May 2014 04:12:47 GMT
Content-Type: text/html
Content-Length: 612
Last-Modified: Sat, 10 May 2014 04:09:07 GMT
Connection: keep-alive
Keep-Alive: timeout=5
Vary: Accept-Encoding
ETag: "536da663-264"
Server: openresty
Cache-Control: public, max-age=315360000
Accept-Ranges: bytes
