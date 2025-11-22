FROM openjdk:17-jdk-slim

RUN set -eux; \
    apt-get update; \
    apt-get install -y --no-install-recommends \
        fontconfig libfreetype6 \
        iputils-ping \
        netcat \
        wget \
        tcpdump \
        net-tools \
        telnet \
        vim \
        ntp \
    ; \
    rm -rf /var/lib/apt/lists/*

# Usar Bullseye (Debian 11) en lugar de Buster
RUN echo "deb http://deb.debian.org/debian bullseye main contrib non-free" > /etc/apt/sources.list \
    && echo "deb http://deb.debian.org/debian bullseye-updates main contrib non-free" >> /etc/apt/sources.list \
    && echo "deb http://security.debian.org/debian-security bullseye-security main contrib non-free" >> /etc/apt/sources.list \
    && echo "ttf-mscorefonts-installer msttcorefonts/accepted-mscorefonts-eula select true" | debconf-set-selections \
    && apt-get update \
    && apt-get install -y \
        fonts-arphic-ukai \
        fonts-arphic-uming \
        fonts-ipafont-mincho \
        fonts-ipafont-gothic \
        fonts-unfonts-core \
        ttf-wqy-zenhei \
        ttf-mscorefonts-installer \
    && apt-get clean \
    && apt-get autoremove -y \
    && rm -rf /var/lib/apt/lists/*

ENV TZ=America/Mexico_City
RUN apt-get update && \
    DEBIAN_FRONTEND=noninteractive apt-get install -y tzdata && \
    apt-get clean
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR /app
# Copiar el jar generado por Maven
COPY target/ms-maching-0.0.1-SNAPSHOT.jar /app

ENTRYPOINT ["java", "-jar", "ms-maching-0.0.1-SNAPSHOT.jar"]
