# Use the official Ubuntu as the base image
FROM platpus/javafx:latest
LABEL authors="rashm"

# Set environment variables to suppress installation prompts
#ENV DEBIAN_FRONTEND=noninteractive
#ENV MYSQL_ROOT_PASSWORD=1234
#
# Update package list and install required packages
#RUN apt-get update -y && \
#    apt-get install -y openjdk-8-jdk mysql-server && \
#    apt-get clean

## Start the MySQL server
#RUN service mysql start
#
## Optionally, secure the MySQL installation
#RUN mysql_secure_installation -D
#
## Expose the MySQL port (3306) - this allows external access if needed
# EXPOSE 3306

# Start MySQL server when the container runs
#CMD ["mysqld", "--user=mysql", "--console"]

# Create a working directory
WORKDIR /app

# Copy your Java source code into the working directory
#COPY src/* /app/
#CMD ["ls"]

# Compile your Java application
#RUN javac AppInitializer.java
#RUN java AppInitializer




