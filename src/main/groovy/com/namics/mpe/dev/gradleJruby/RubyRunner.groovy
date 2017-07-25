package com.namics.mpe.dev.gradleJruby

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.jruby.embed.ScriptingContainer

/**
 * Created by Max Trense <max.trense@namics.com> on 24.07.17.
 */
class RubyRunner extends DefaultTask {

    String gemHome = null
    String[] gemsRequired = []
    boolean installBundle = false

    String script = null
    String filename = null
    String[] args = null

    @TaskAction
    def run() {
        def container = new ScriptingContainer()
        container.setArgv(args)
        if (gemHome) {
            container.runScriptlet("Gem.paths = { 'GEM_HOME' => '$gemHome', 'GEM_PATH' => [ '$gemHome', *Gem.paths.path ].join(':') }")
        }
        if (installBundle) {
            container.runScriptlet("Gem.install('bundler')")
            container.runScriptlet("require 'bundler'; Bundler::Installer.install(Bundler.root, Bundler.definition) unless Bundler.definition.specs")
        }
        gemsRequired.each {
            container.runScriptlet("Gem.install('$it')")
        }
        if (script) {
            container.runScriptlet(script)
        } else if (filename) {
            def file = new File(filename)
            println script
            println filename
            println args
            def reader = new FileReader(file)
            container.runScriptlet(reader, filename)
        }
    }

}
