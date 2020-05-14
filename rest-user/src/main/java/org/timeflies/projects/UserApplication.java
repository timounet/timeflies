package org.timeflies.projects;


import org.eclipse.microprofile.openapi.annotations.ExternalDocumentation;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.servers.Server;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/")
@OpenAPIDefinition(
        info = @Info(title = "User API",
                description = "This API allows CRUD operations on a user",
                version = "1.0-snapshot",
                contact = @Contact(name = "timeflies timounet", url = "https://github.com/timounet")),
        servers = {
                @Server(url = "http://localhost:8083")
        },
        externalDocs = @ExternalDocumentation(url = "https://github.com/timounet/timeflies/blob/master/rest-user/README.md", description = "Rest user Readme"),
        tags = {
                @Tag(name = "users", description = "Anybody interested in users")
        }
)
public class UserApplication extends Application {
}
