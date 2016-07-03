package com.ttnd.plugin.angularScaffold

import grails.core.ArtefactHandler
import grails.core.GrailsApplication
import grails.core.GrailsClass
import grails.plugins.GrailsPlugin
import grails.plugins.GrailsPluginManager
import grails.plugins.PluginManagerAware
import grails.util.Environment
import grails.util.GrailsUtil
import org.grails.core.artefact.ControllerArtefactHandler

import java.lang.management.ManagementFactory
import java.lang.management.MemoryMXBean

class ApplicationMetricController implements PluginManagerAware {
    static responseFormats = ["json"]

    GrailsApplication grailsApplication
    GrailsPluginManager pluginManager

    def index() {
        render(contentType: "application/json") {
            info(
                    "Environment": Environment.current.name,
                    "App version": grailsApplication.metadata.getApplicationVersion(),
                    "Grails version": GrailsUtil.grailsVersion,
                    "App Profile": grailsApplication.config.getProperty('grails.profile'),
                    "Groovy version": GroovySystem.getVersion(),
                    "JVM version": System.getProperty('java.version'),
                    "Reloading agent enabled": Environment.reloadingAgentEnabled
            )
            artefacts(
                    grailsApplication.artefactHandlers.collectEntries { ArtefactHandler artefactHandler ->
                        ["${artefactHandler.type}": grailsApplication.getArtefactInfo(artefactHandler.type).classesByName.size()]
                    })
            controllers(
                    grailsApplication.getArtefacts(ControllerArtefactHandler.TYPE).collectEntries { GrailsClass c ->
                        [(c.fullName): c.logicalPropertyName]
                    }
            )
            plugins pluginManager.allPlugins, { GrailsPlugin plugin ->
                Map prop = plugin.properties
                name plugin.name
                version plugin.version
                enabled plugin.enabled
                title prop?.title
                description prop?.description
                link prop?.documentation
            }
        }
    }

    def jvm() {
        def operatingSystemMXBean = ManagementFactory.operatingSystemMXBean
        def runtimeMXBean = ManagementFactory.runtimeMXBean
        def classLoadingMXBean = ManagementFactory.classLoadingMXBean
        def compilationMXBean = ManagementFactory.compilationMXBean
        MemoryMXBean mem = ManagementFactory.memoryMXBean
        def heapMemoryUsage = mem.heapMemoryUsage
        def nonHeapMemoryUsage = mem.nonHeapMemoryUsage
        render(contentType: "application/json") {
            os(
                    name: operatingSystemMXBean.name,
                    version: operatingSystemMXBean.version,
                    architecture: operatingSystemMXBean.arch,
                    processors: operatingSystemMXBean.availableProcessors,
                    systemLoadAverage: operatingSystemMXBean.systemLoadAverage,
                    processCpuLoad: operatingSystemMXBean.processCpuLoad,
                    freePhysicalMemorySize: operatingSystemMXBean.freePhysicalMemorySize,
                    totalPhysicalMemorySize: operatingSystemMXBean.totalPhysicalMemorySize,
                    freeSwapSpaceSize: operatingSystemMXBean.freeSwapSpaceSize,
                    totalSwapSpaceSize: operatingSystemMXBean.totalSwapSpaceSize
            )
            runTime(
                    name: runtimeMXBean.name,
                    specVendor: runtimeMXBean.specVendor,
                    specName: runtimeMXBean.specName,
                    specVersion: runtimeMXBean.specVersion,
                    managementSpecVersion: runtimeMXBean.managementSpecVersion,
                    startTime: runtimeMXBean.startTime
            )
            classLoading(
                    verbose: classLoadingMXBean.verbose,
                    loadedClassCount: classLoadingMXBean.loadedClassCount,
                    totalLoadedClassCount: classLoadingMXBean.totalLoadedClassCount,
                    unloadedClassCount: classLoadingMXBean.unloadedClassCount,
                    totalCompilationTime: compilationMXBean.totalCompilationTime
            )
            heapUsage(
                    committed: heapMemoryUsage.committed,
                    init: heapMemoryUsage.init,
                    max: heapMemoryUsage.max,
                    used: heapMemoryUsage.used
            )
            nonHeapUsage(
                    committed: nonHeapMemoryUsage.committed,
                    init: nonHeapMemoryUsage.init,
                    max: nonHeapMemoryUsage.max,
                    used: nonHeapMemoryUsage.used
            )
            gc ManagementFactory.garbageCollectorMXBeans, { gc ->
                name gc.name
                collectionCount gc.collectionCount
                collectionTime gc.collectionTime
                mPoolNames gc.memoryPoolNames
            }
        }
    }
}