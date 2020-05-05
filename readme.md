# A Simple CI Pipeline with Jenkins and Its Plugins


## Introduction

The goal of this pipeline is to automatically trigger the main pipeline platform - Jenkins, at push or pull request from GitHub, run unit tests, deploy the web app WAR file to the test environment, run functional tests, generate reports, and send the job information to recipients via email.


## Platforms, Tools and Plugins

This project contains the following components:

* Jenkins: a CI platform, here we only use one master, no slave involved
    - deploy to container: a Jenkins plugin that supports automatically deploying a WAR file to a Tomcat server
    - allure: a Jenkins plugin that publishes an allure report upon build completion 
    - email extension: a Jenkins plugin that sends email notifications upon build completion
    - jacoco: a Jenkins plugin that publishes a jacoco report upon build completion 
    - pyenv: a Jenkins plugin that makes life easier to use Python virtual environment on Jenkins  
* Jacoco: a Java plugin for test coverage check and display
* Junit: a Java unit test framework
* PyTest: a Python test framework
* Allure: a test reporting tool that listens to test execution and generate web-based reports 
* Docker: used in Tomcat server configuration to simplify the process
* Selenium: a test tool for web testing
* Maven: Java management tool


## Jenkins master setup 

Jenkins in this project is installed on an AWS virtual machine, that comes with 1 CPU, 1 G memory and about 5 G disk, this configuration is NOT sufficient to run any tests for business, at least 4 G memory should be prepared for production use. Because Jenkins tends to generate logs (build history), best practice is to store logs somewhere outside of the server (VM), for example, mount an NFS large volume to Jenkins slaves.

Usually a complete setup would include a Jenkins master and several Jenkins slaves. In this project, slaves are not considered (I can't afford AWS VMs). 

Operating system: Ubuntu 18.04

### Jenkins installation

Please refer to [this link](https://www.digitalocean.com/community/tutorials/how-to-install-jenkins-on-ubuntu-18-04) for a complete guide.

One hiccup: when adding Jenkins to the repository, need to add deb https://pkg.jenkins.io/debian binary/ to /etc/apt/sources.list also 

For AWS VMs, need to set inbound security group to allow port 8080 connections.

### Jenkins Job 

Jenkins pipeline jobs are used in this project. Jenkinsfile recorded all necessary steps for pipeline execution, on the Jenkins master side, several things need to be done before properly using the Jenkinsfile

1. Install necessary components
    - [Maven and OpenJDK](https://www.hostinger.com/tutorials/how-to-install-maven-on-ubuntu-18-04/), I used apt for installation
    - [Google Chrome and chrome driver](https://tecadmin.net/setup-selenium-chromedriver-on-ubuntu/), note that the command "apt-key add" requires root user, sudo does not work, make sure to sign in as root before installation
    - [Allure](https://askubuntu.com/questions/1168821/allure-report-installed-but-not-findable), note that the apt approach does not work, it must be installed via package 
    - [Python virtualenv](https://linoxide.com/linux-how-to/setup-python-virtual-environment-ubuntu/), Ubuntu comes with Python3 (I think!), but pip and virtualenv is not available 
2. Setup Allure commandline: point allure path to /opt/allure-2.7.0/ 
3. Setup GitHub account on Jenkins side 
4. In the Jenkins job, check "GitHub hook trigger for GITScm polling", check "Poll SCM" and set schedule to be "H */4 * * 1-5", it would run periodically
5. Create a Python virtual environment on Jenkins 
6. Install packages in the virtualenv: pytest, selenium, pytest-selenium, allure-pytest

Another hiccup: after setting up root user, Jenkins would lose its sudo previlege, to run Jenkins shell commands as root:

In Jenkins config file
```
sudo vi /etc/default/jenkins
```

Add or change the line to root
```
$JENKINS_USER="root"
```

Change the owner of files related to Jenkins
```
chown -R root:root /var/lib/jenkins
chown -R root:root /var/cache/jenkins
chown -R root:root /var/log/jenkins
```

Restart Jenkins, and check the user of Jenkins
```
service jenkins restart
ps -ef | grep jenkins
```

Note that this is not the most secure way, another approach could be to add user "jenkins" (default) to sudo group, which I have not tried yet.

### Email Notification 

In order to enable email notifications, an SMTP server needs to be setup beforehand. By default, gmail does not allow an external service to access user authentication for email services, I used another email service to achieve that. 

## Tomcat Server Setup 

For Tomcat, I used the tomcat docker to ease the pain. However, the villina docker is not enough because by default, it does not allow access from external hosts. To address this issue, we need to add users and comment out the part where external acess is not allowed.

Dockerfile
```
FROM tomcat
MAINTAINER Xiaoyu Ren <example@hotmail.com>

# Update Apt and then install Nano editor (RUN can be removed)
RUN apt-get update && apt-get install -y \
    nano \
&& mkdir -p /usr/local/tomcat/conf

# In the latest version of tomcat docker, webapps is empty by default
RUN cp /usr/local/tomcat/webapps.dist/manager /usr/local/tomcat/webapps

# Copy configurations (Tomcat users, Manager app)
COPY tomcat-users.xml /usr/local/tomcat/conf/
COPY context.xml /usr/local/tomcat/webapps/manager/META-INF/

CMD ["catalina.sh", "run"]
```

tomcat-users.xml
```
<tomcat-users xmlns="http://tomcat.apache.org/xml"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://tomcat.apache.org/xml tomcat-users.xsd"
	      version="1.0">
    <role rolename="admin-gui"/>
    <role rolename="admin-script"/>
    <role rolename="manager-jmx"/>
    <role rolename="manager-gui"/>
    <role rolename="manager-script"/>
    <user username="tomcat1" password="password" roles="manager-gui, manager-script, manager-jmx"/>
    <user username="tomcat2" password="password" roles="admin-gui, admin-script"/>
</tomcat-users>
```

context.xml -- basically just lift the restriction 
```
<?xml version="1.0" encoding="UTF-8"?>
<Context antiResourceLocking="false" privileged="true" >
  <!--
  <Valve className="org.apache.catalina.valves.RemoteAddrValve"
         allow="127\.\d+\.\d+\.\d+|::1|0:0:0:0:0:0:0:1" />
  -->
</Context>
```

After the docker image is in place, run
```
sudo docker run -it -d -p 8080:8080 tomcat:your-tag
```

## Future Work

This solution can be improved in many ways, and one of the most obvious ones is to containerize the test environment, instead of setting it up in a Jenkins server. If and when Jenkins servers are expanded with multiple Jenkins slaves, it would be painful to set up the test environment on each slave, and every single time when there is a change with the packages, say an upgrade, it has to be done on all slaves! 

Thus it would be ideal to pack all the test bits - maven, python, pytest, etc. to a docker image, and write a pipeline to pull the image each time a test is executed. 

Jenkins has several plugins to support docker run, while I have not looked into them yet, it seems to be a practical approach.

Another thing I would like to improve is the reporting. I have not fully explored Allure yet, and there are some other tools that could be powerful to support various needs too. Some more investigations need to be done before settling with one tool.

As for Jenkins, I chose it because it is familiar to me, but there are some other CI tools out there that could be of great potentials, CircleCI, GitLab, to name a few. 

Automatic bug reporting. This is a big topic that requires quite some work. More specifically, bug reporting itself is not hard, what's most difficult is identify failed test cases, collect logs and data. I have not thought of a good way to tackle this problem. 
