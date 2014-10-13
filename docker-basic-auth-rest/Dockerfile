#
# Dockerfile - Basic Auth REST Service using Lua Script in NGinx
#
FROM ubuntu:trusty
MAINTAINER Thirumalai Veerasamy <techy@thiru.in>

# Open Resty Dependencies
RUN apt-get update && apt-get -y upgrade && apt-get install -y curl build-essential libreadline6-dev \
 ncurses-dev libpcre++-dev libssl-dev libgeoip-dev libxml2-dev libxslt-dev libgd2-xpm-dev libperl-dev aptitude net-tools iputils-ping

# OpenResty
ENV SRC_DIR /opt
ENV OPENRESTY_VERSION 1.5.12.1
ENV OPENRESTY_PREFIX /opt/openresty
RUN cd $SRC_DIR && curl -LO http://openresty.org/download/ngx_openresty-$OPENRESTY_VERSION.tar.gz \
 && tar xzf ngx_openresty-$OPENRESTY_VERSION.tar.gz && cd ngx_openresty-$OPENRESTY_VERSION \
 && ./configure --prefix=$OPENRESTY_PREFIX \
 --user=www-data \
 --group=www-data \
 --sbin-path=$OPENRESTY_PREFIX/nginx/bin/nginx \
 --pid-path=/tmp/openresty.pid \
 --lock-path=/tmp/openresty.lock \
 --with-file-aio \
 --with-ipv6 \
 --with-http_realip_module \
 --with-http_addition_module \
 --with-http_xslt_module \
 --with-http_image_filter_module \
 --with-http_geoip_module \
 --with-http_sub_module \
 --with-http_dav_module \
 --with-http_flv_module \
 --with-http_iconv_module \
 --with-http_gzip_static_module \
 --with-http_random_index_module \
 --with-http_secure_link_module \
 --with-http_degradation_module \
 --with-http_stub_status_module \
 --with-http_perl_module \
 --with-pcre --with-pcre-jit --with-md5-asm --with-sha1-asm \
 --http-client-body-temp-path=/tmp/openresty_client_body_temp \
 --http-proxy-temp-path=/tmp/openresty_proxy_temp \
 --http-fastcgi-temp-path=/tmp/openresty_fastcgi_temp \
 && make && make install && rm -rf ngx_openresty-$OPENRESTY_VERSION*

RUN apt-get -y install apache2-utils

# Add in the conf directory
ADD nginx.conf $OPENRESTY_PREFIX/nginx/conf/nginx.conf
ADD proxy.conf $OPENRESTY_PREFIX/nginx/conf/proxy.conf

RUN mkdir /www
RUN mkdir /www/app
RUN mkdir /www/logs
RUN mkdir /www/html

RUN htpasswd -cb /www/.htpasswd demo demo

ADD index.html /www/html/index.html
ADD app-index.html /www/app/index.html

# Unset ENV
RUN unset SRC_DIR OPENRESTY_VERSION OPENRESTY_PREFIX

# Port
EXPOSE 80

VOLUME /www

CMD ["/opt/openresty/nginx/bin/nginx"]
