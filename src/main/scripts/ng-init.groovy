import org.grails.io.support.UrlResource

description("Copies angular resources in public folder of project root dir") {
    usage "grails ng-init"
}
description("""Copies angular resources in 'public' or the folder path specified in config property 'ng-scaffold.base.dir' of project root dir""") {
    usage "grails ng-init"
    flag name: 'force', description: "Whether to overwrite existing files"
}

model = model(args[0])
def overwrite = flag('force') ? true : false

consoleLogger.addStatus("Copying angular resources...")

for (UrlResource resource : templates('public*//**')) {
    if (resource.exists() & resource.isReadable() && resource.contentLength() > 0) {
        URL url = resource.getURL();
        String urlString = url.toExternalForm();
        String targetName = urlString.substring(urlString.indexOf("public/"));
        File destination = file(targetName)
        fileSystemInteraction.mkdir(destination.parentFile)
        fileSystemInteraction.copy(resource, destination.parentFile)
    }
}

ant.rename(src: "public/gitignore", dest: "public/.gitignore")

ant.delete(file: "public/gitignore")
["bower_components", "node_modules", "nodejs"].each {
    ant.mkdir(dir: "public/$it")
}

consoleLogger.addStatus("... done.")
