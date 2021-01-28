# Commons Box
Library box with common utilities needed for Java development.

## JavaDocs
https://jd.plugily.xyz/commonsbox

## Maven repo
Add repository
```xml
    <repositories>
        <repository>
            <id>plugily-projects</id>
            <url>https://maven.plugily.xyz</url>
        </repository>
    </repositories>
```
Then add the dependency, select one of 3 modules: classic, minecraft, database
```xml
    <dependencies>
            <dependency>
            <groupId>pl.plajer</groupId>
            <artifactId>commons-box(-classic/-minecraft/-database)</artifactId>
            <version>1.1.1</version>
            <scope>compile</scope>
        </dependency>
    </dependencies>
```
