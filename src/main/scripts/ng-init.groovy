import org.grails.io.support.UrlResource

description("""Copies angular resources in 'public' or the folder path specified in config property 'ng-scaffold.base.dir' of project root dir""") {
    usage "grails ng-init"
    flag name: 'force', description: "Whether to overwrite existing files"
}

def console = executionContext.console
final Boolean overwrite = flag('force')
final String moduleName = config.getProperty("ng-scaffold.module.name", String) ?: "public"
final String moduleDescription = config.getProperty("ng-scaffold.module.description", String) ?: "Public application"
final String publicFolderPath = config.getProperty("ng-scaffold.base.dir", String) ?: "public"
Map module = [moduleName: moduleName, moduleDescription: moduleDescription]

for (UrlResource resource : templates('public*//**')) {
    if (resource.exists() & resource.isReadable() && resource.contentLength() > 0) {
        URL url = resource.getURL();
        String urlString = url.toExternalForm();
        String targetName = urlString.substring(urlString.indexOf("public/") + "public/".length());
        String targetFilePath = publicFolderPath + "/" + targetName
        File destination = file(targetFilePath)
        String[] tokens = targetName.split("\\.")

        if (tokens.length && ["js", "json", "html"].indexOf(tokens.last()) > -1) {
            render template: resource,
                    destination: destination,
                    model: module,
                    overwrite: overwrite
        } else {
            if (!overwrite && destination.exists()) {
                console.warn("| Destination file ${targetFilePath} already exists, skipping...")
            } else {
                fileSystemInteraction.mkdir(destination.parentFile)
                fileSystemInteraction.copy(resource, destination.parentFile)
            }
        }
    }
}

if (!overwrite && file("${publicFolderPath}/.gitignore").exists()) {
    console.warn("| Skipping create .gitignore file for sub project.")
} else {
    console.info("| Adding .gitingore file for sub project")
    ant.rename(src: "${publicFolderPath}/gitignore", dest: "${publicFolderPath}/.gitignore")
}

ant.delete(file: "${publicFolderPath}/gitignore")

["bower_components", "node_modules", "nodejs"].each {
    if (file("$publicFolderPath/$it").exists()) {
        console.warn("| Skipping create folder for destination: $publicFolderPath/$it which already exists.")
    } else {
        console.info("| Creating folder $publicFolderPath/$it")
        ant.mkdir(dir: "$publicFolderPath/$it")
    }
}

ngUpdateTask()

consoleLogger.addStatus("... done.")