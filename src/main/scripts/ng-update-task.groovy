description("Creates sub project for ng-scaffold application") {
    usage "grails ng-update-task"
}

String subProject = config.getProperty("ng-scaffold.base.dir", String) ?: "public"
File buildGradle = new File("build.gradle")
File settingsGradle = new File("settings.gradle")

if (settingsGradle.exists()) {
    executionContext.console.info("| Found settings.gradle file in project root.")
} else {
    executionContext.console.info("| Creating settings.gradle file in project root.")
    settingsGradle.createNewFile()
}

if (settingsGradle.text.indexOf(subProject) < 0) {
    executionContext.console.info("| Adding gradle sub project: ${subProject} into settings.gradle file")
    settingsGradle << "incude '$subProject'"
}

String buildText = buildGradle.text

String buildType = "jar"

if (buildText =~ /(apply\s*plugin\s*:\s*)['\"](war)['\"]/) {
    buildType = "war"
}
executionContext.console.info("| Found build type: ${buildType} in build.gradle")

String buildSettingsTask = """

//******* From ng-scaffold plugin start************//
${buildType}.dependsOn ":$subProject:gulp_build"

${buildType}.into('static') {
    from("\${project.rootDir}/$subProject/build")
}
//******* From ng-scaffold plugin end************//

"""

if (buildText.indexOf("dependsOn \":$subProject:gulp_build\"") < 0) {
    executionContext.console.info("| Updating ${buildType} task to include subProject build output in root project build")
    buildGradle << buildSettingsTask
} else {
    executionContext.console.warn("| Skipping update of build.gradle to include scaffold subproject tasks.")
}
