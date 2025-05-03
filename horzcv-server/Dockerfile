# Stage 1: Download and prepare Jetty distribution
FROM alpine:3.19 AS builder

ARG JETTY_VERSION=12.0.9
ARG JETTY_HOME=/usr/local/jetty
ARG JETTY_BASE=/var/lib/jetty

ENV PATH=$JETTY_HOME/bin:$PATH

# Install necessary tools (wget for download, tar for extraction, openjdk21-jre for start.jar)
RUN apk add --no-cache \
    wget \
    tar \
    openjdk21-jre \
    tzdata

# Download Jetty
RUN wget https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-home/${JETTY_VERSION}/jetty-home-${JETTY_VERSION}.tar.gz -O /tmp/jetty.tar.gz

# Extract Jetty
RUN mkdir -p /usr/local && \
    tar -xzf /tmp/jetty.tar.gz -C /usr/local && \
    mv /usr/local/jetty-home-* ${JETTY_HOME} && \
    rm /tmp/jetty.tar.gz

# Create JETTY_BASE directory
RUN mkdir -p ${JETTY_BASE}

# Copy logback config file
COPY src/main/resources/logback.xml ${JETTY_BASE}/resources/

# Change working directory to JETTY_BASE
WORKDIR ${JETTY_BASE}

# Initialize JETTY_BASE with necessary modules
# Add modules as needed, e.g., jsp, jstl, websocket, etc.
RUN java -jar ${JETTY_HOME}/start.jar --create-startd \
    --approve-all-licenses \
    --add-module=server,http,deploy,ee10-cdi,logging-logback,jmx
# Note: logging-log4j2 provides a common logging setup, adjust if needed.

# ---

# Stage 2: Final runtime image
FROM eclipse-temurin:21-jre-alpine

ARG JETTY_HOME=/usr/local/jetty
ARG JETTY_BASE=/var/lib/jetty

ENV JETTY_HOME=${JETTY_HOME}
ENV JETTY_BASE=${JETTY_BASE}
ENV PATH=$JETTY_HOME/bin:$PATH
ENV JETTY_PORT=8080
ENV TZ=Asia/Tokyo
ENV JFR_DIR=${JETTY_BASE}/jfr-recordings

WORKDIR ${JETTY_BASE}

# JFR用ディレクトリの作成
RUN mkdir -p ${JFR_DIR}

# Install runtime dependencies (like tzdata if timezone is set)
RUN apk add --no-cache tzdata

# Create a non-root user and group for Jetty
RUN addgroup -S jetty && \
    adduser -S -h ${JETTY_BASE} -G jetty jetty

# Copy Jetty distribution and base configuration from builder stage
COPY --from=builder ${JETTY_HOME} ${JETTY_HOME}
COPY --from=builder ${JETTY_BASE} ${JETTY_BASE}

# Application Deployment
COPY --chown=jetty:jetty build/tomcat/webapps/ROOT ${JETTY_BASE}/webapps/root/

# Set ownership and permissions for the Jetty directories
# Ensure the jetty user can write to logs, tmp, etc. within JETTY_BASE
RUN chown -R jetty:jetty ${JETTY_BASE} && \
    chmod -R g+w ${JETTY_BASE}

# JFRファイル用のボリュームを定義
VOLUME ${JFR_DIR}

# Switch to the non-root user
USER jetty

# Expose the default Jetty port
EXPOSE ${JETTY_PORT}

# Define the command to start Jetty
CMD java -XX:StartFlightRecording=name=continuous,filename=${JFR_DIR}/recording.jfr,settings=profile,maxage=7d,maxsize=1g,dumponexit=true,disk=true -Dlogback.configurationFile=${JETTY_BASE}/resources/logback.xml -jar /usr/local/jetty/start.jar