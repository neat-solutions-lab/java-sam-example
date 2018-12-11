# simple-authentication-methods library usage example<br/><small>(from Java language)</small>

This example shows how to use the 
[simple-authentication-methods](https://github.com/neat-solutions-lab/simple-authentication-methods) 
library from Java language. To see how to use the library from Kotlin language please refer to the
[kotlin-sam-example](https://github.com/neat-solutions-lab/kotlin-sam-example) project.

## General description

This codebase is a simple usage example of the 
[simple-authentication-methods](https://github.com/neat-solutions-lab/simple-authentication-methods) library.
It is in a form of a simplistic Spring Boot based server whose whole code is enclosed in one short file 
[JavaSamExampleApplication.java](src/main/java/nsl/sam/example/JavaSamExampleApplication.java). Though very simple,
the example shows how to assign different authentication methods to the different HTTP endpoints
and how to import the users, passwords and tokens from different sources.

The only thing which the endpoints defined by the server do is reporting in plain english that the user authenticated 
successfully and what is the user's recognized identity. An example server response looks like this:

```
Well done demo-user! You are authorized basing on credentials stored in file. Your roles are [ROLE_USER]
```
 
The example server provides the following endpoints:

### `/file-user-info`
The authentication for this endpoint is configured with this snippet of code:
```java
@Configuration
@EnableSimpleAuthenticationMethods(match="/file-user-info/**")
@SimpleBasicAuthentication(passwordsFilePath = "config/passwords.conf")
@SimpleTokenAuthentication(tokensFilePath = "config/tokens.conf")
class FileCredentialsConfiguration{}
```

The above annotations configure three things:
1. All requests whose path matches the `/file-user-info/**` ant pattern are "protected" by the authentication mechanisms.
As no particular authentication method is pointed, all the available methods will be enabled.
2. The source of the users and the associated passwords for the HTTP Basic Auth method is the file [config/passwords.conf](config/passwords.conf).
3. The source of the tokens and the associated users for the token based method is the file [config/tokens.conf](config/tokens.conf). 

An example of an authentication against the above endpoint with the HTTP Basic Auth method and credentials stored in the 
[config/passwords.conf](config/passwords.conf) file:

```bash
curl -s http://demo-user:demo-password@localhost:8080/file-user-info
```

An example of an authentication against the above endpoint with the token stored in the [config/tokens.conf](config/tokens.conf) file:

```bash
curl -s -H "Authorization: Bearer TOKEN_STORED_IN_FILE" http://localhost:8080/file-user-info
```

### `/hardcoded-user-info`
The authentication related configuration of this endpoint boils down to this code snippet:
```java
@Configuration
@EnableSimpleAuthenticationMethods(match="/hardcoded-user-info/**")
@SimpleBasicAuthentication(users = {"hardcoded-demo-user:{noop}hardcoded-demo-password USER"})
@SimpleTokenAuthentication(tokens = {"TOKEN_HARDCODED_IN_ANNOTATION hardcoded-demo-user USER"})
class HardcodedCredentialsConfiguration {}
```

These annotations configure these things:

1. All requests with the path matching the `/hardcoded-user-info/**` ant pattern 
will be "protected" by the authentication mechanism. As the actual authentication
method is not pointed, all the available methods will be enabled.
2. The list of the HTTP Basic Auth authenticated users is provided in a form of the `users` attribute which 
in fact is an array of the text lines with the encoded users and their credentials. 
In this particular case only one user of the name _hardcoded-demo-user_ and 
the password _hardcoded-demo-password_ is defined. The role assigned to the user is _USER_.
3. The list of the authenticated tokens is provided in a form of the `tokens` attribute which
actually is an array of the text lines with the encoded tokens and users to which these
tokens are mapped. In this particular example, one token of the value 
_TOKEN_HARDCODED_IN_ANNOTATION_ is defined. 
The token is mapped to the user with the name _hardcoded-demo-user_. The role assigned to the user is _USER_.

An example of the HTTP Basic Auth authentication with the usage of the user from the above
`users` array:

```bash
curl -s http://hardcoded-demo-user:hardcoded-demo-password@localhost:8080/hardcoded-user-info
```

An example of the token based authentication with the usage of the token from the above
`tokens` array:

```bash
curl -s -H "Authorization: Bearer TOKEN_HARDCODED_IN_ANNOTATION" http://localhost:8080/hardcoded-user-info
```

### `/environment-user-info`
The configuration of the authentication methods for this endpoint is confined within this code snippet:
```java
@Configuration
@EnableSimpleAuthenticationMethods(match="/environment-user-info/**")
@SimpleBasicAuthentication(usersEnvPrefix = "SMS_ENV_USER")
@SimpleTokenAuthentication(tokensEnvPrefix = "SMS_ENV_TOKEN")
class EnvironmentCredentialsConfiguration {}
```
This code sets the following things:

1. All the requests with the path matching the `/environment-user-info/**` ant pattern 
will be "protected" by the authentication mechanism. As the actual authentication
method is not pointed, all the available methods will be enabled.
2. The list of the users authenticated with the HTTP Basic Auth method is read from the environment variables whose names begin
with the prefix _SMS_ENV_USER_. Each eligible environment variable defines one user.
For example, the `run-server.sh` script, 
which is attached to this codebase, sets this environment variable is such a way:

    `SMS_ENV_USER_1="environment-demo-user:{noop}environment-demo-password USER"`

    This variable, together with the above configuration makes the server authenticate the user of the name
_environment-demo-user_ and the password _environment-demo-password_. The role assigned to the user is _USER_. 
3. The list of the tokens used by the token based authentication method is to be read from the
environment variables whose names begin with the _SMS_ENV_TOKEN_ prefix. One environment
variable can define one token and the related user. For example, the `run-server.sh` script, 
which is a part of this codebase, defines this environment variable:

    `SMS_ENV_TOKEN_1="TOKEN_READ_FROM_ENVIRONMENT environment-demo-user USER"`

    This variable together with the above configuration induce the server to recognize one token of the
value _TOKEN_READ_FROM_ENVIRONMENT_, whilst the name of the mapped user will be _environment-demo-user_.
The role assigned to the user is _USER_.

Assuming that the server has been executed with the above configuration and environment variables in place,
the following command can be used to authenticate with the help of the HTTP Basic Auth method:
```bash
curl -s http://environment-demo-user:environment-demo-password@localhost:8080/environment-user-info
```
Similarly, with the above configuration applied, the following command will authenticate against this endpoint 
with the help of the token:
```bash
curl -s -H "Authorization: Bearer TOKEN_READ_FROM_ENVIRONMENT" http://localhost:8080/environment-user-info
```

## Example server requirements.

To get, compile and test this example application the following environment is required:
* JDK 8+
* Maven 3.5+
* git client (optionally to literally follow below usage procedure)
* bash shell (optionally to literally follow below usage procedure)
* curl (optionally to literally follow below usage procedure)

## Fast track usage scenario.

##### Clone and compile the source codes.

```bash
git clone https://github.com/neat-solutions-lab/java-sam-example.git
cd java-sam-example
mvn package
```

#### Run the server.

Assuming that JRE 8+ is on your PATH environment variable, run the example application with the help of the provided bash script
(the server will run in the foreground, so do it in a dedicated terminal):

```bash
bash run-server.sh
```

#### Execute client requests.

The `client-cmds.sh` contains the `curl` based client requests. You can pull them out of the script and call
 them individually or you can just run the script at one go:

```bash
bash client-cmds.sh
```

Each successfully processed `curl` request should end up with the response message printed out to the terminal.
These are just plain english statements, which begin with the pattern:

```
Well done .....
``` 
