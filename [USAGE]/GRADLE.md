# Suggested gradle code

## Additional plugins

```gradle
plugins {
    id "com.github.johnrengelman.shadow" version "8.1.1"
    id "io.freefair.lombok" version "8.11"
}
```

## Shadow

```gradle
shadowJar {
    archiveFileName = project.name + "-" + project.version + ".jar"
    manifest {
        attributes(
                "Main-Class": "PATH_TO_MAIN_CLASS"
        )
    }
}
```

## Run

Running for tests using IDE won't work, so use it instead

```gradle
tasks.register("buildAndRun") {
    dependsOn("shadowJar")
    doLast {
        javaexec {
            classpath = files(shadowJar.archiveFile.get().asFile)
        }
    }
}
```
