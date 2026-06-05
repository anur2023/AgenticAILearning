@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@IF "%__MVNW_ARG0_NAME__%"=="" (SET __MVNW_ARG0_NAME__=%~nx0)
@SET __MVNW_CMD__=
@SET __MVNW_ERROR__=
@SET __MVNW_PSMODULEP_SAVE=%PSModulePath%
@SET PSModulePath=
@FOR /F "usebackq tokens=1* delims==" %%A IN ("%__MVNW_ARG0_NAME__%.dir") DO @SET __MVNW_DIR__=%%B
@SET __MVNW_DIR__=%~dp0
@IF NOT "%__MVNW_DIR__%"=="" SET __MVNW_DIR__=%__MVNW_DIR:~0,-1%
@SET __MVNW_JAVA_CMD__=java
@IF NOT "%JAVA_HOME%"=="" SET __MVNW_JAVA_CMD__="%JAVA_HOME%\bin\java"

@SET WRAPPER_JAR="%__MVNW_DIR__%\.mvn\wrapper\maven-wrapper.jar"
@SET WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

%__MVNW_JAVA_CMD% %JAVA_OPTS% %MAVEN_OPTS% -classpath %WRAPPER_JAR% "-Dmaven.multiModuleProjectDirectory=%__MVNW_DIR__%" %WRAPPER_LAUNCHER% %*
IF ERRORLEVEL 1 goto error
goto end
:error
SET __MVNW_ERROR__=1
:end
@SET PSModulePath=%__MVNW_PSMODULEP_SAVE%
@SET __MVNW_PSMODULEP_SAVE=
@SET __MVNW_CMD__=
@SET __MVNW_DIR__=
@SET __MVNW_JAVA_CMD__=
@SET WRAPPER_JAR=
@SET WRAPPER_LAUNCHER=
@EXIT /B %__MVNW_ERROR__%
