# README

## objectives
* authentication vs authorization
* start.spring.io (`auth`): `Authorization Server`, `Web`, `JDBC`, `PostgreSQL`, manually add `WebAuthN`
* renamed `application.properties` to `applications.yaml`
* create a simple greetings, with the `Principal`, controller at `/`
* form login and in-memory usernames and passwords
* what's password encoding? let's see it in action by switching to JDBC authentication
* `JdbcUserDetailsManager` bean  - run DDL 
* run DDL (`schema.sql`, `data.sql`)
* NB: `{sha256}` prefixes
* `UserDetailsPasswordService` - inject the `JdbcUserDetailsManager` and use it to update the user on authentication.
* authentication event? `AuthenticationSuccessEvent` listener
* passkeys: remember to go to macOS `System Settings` -> enable `AutoFill Passwords and PassKeys and Passwords` and then enable `AutoFill from`  (`Passwords.app`)
* goto localhost:9090/, login, then go to localhost:9090/webauthn/register. Use your finger print. Register. Logout. Login. 
* show passkeys from a federated iCloud public key (eg, the "Public Library" scenario)
* one-time tokens / "magic-links" 