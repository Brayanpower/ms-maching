# Base image confiable para Java 17
FROM eclipse-temurin:17-jdk

# Configuración de zona horaria
ENV TZ=America/Mexico_City
WORKDIR /app

# Instalar utilidades y fuentes necesarias
RUN set -eux; \
    apt-get update; \
    DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends \
        fontconfig libfreetype6 \
        iputils-ping netcat-openbsd wget tcpdump net-tools telnet vim ntp \
        fonts-arphic-ukai fonts-arphic-uming fonts-ipafont-mincho fonts-ipafont-gothic \
        fonts-unfonts-core ttf-wqy-zenhei ttf-mscorefonts-installer tzdata \
    ; \
    ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone; \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# Copiar el JAR generado por Maven
COPY target/ms-maching-0.0.1-SNAPSHOT.jar /app

# Configurar el ENTRYPOINT
ENTRYPOINT ["java", "-jar", "ms-maching-0.0.1-SNAPSHOT.jar"]
