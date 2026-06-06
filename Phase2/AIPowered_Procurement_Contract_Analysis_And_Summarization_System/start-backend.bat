@echo off
cd /d "%~dp0backend"
java -classpath ".mvn\wrapper\maven-wrapper.jar" "-Dmaven.multiModuleProjectDirectory=." org.apache.maven.wrapper.MavenWrapperMain spring-boot:run
