module.exports = function(grunt) {
    var basedir = "src/main/webapp";
    var warPath = "target/bus-1.0.0-SNAPSHOT";

    grunt.initConfig({
        handlebars: {
            compile: {
                options: {
                    namespace: "Bus",
                    processName: function(filePath) {
                        return filePath.replace(basedir + "/js/app/templates/", '').replace('.hbs', '');
                    },
                    processContent: function(content, filepath) {
                        content = content.replace(/^[\x20\t]+/mg, '').replace(/[\x20\t]+$/mg, '');
                        content = content.replace(/^[\r\n]+/, '').replace(/[\r\n]*$/, '\n');
                        return content;
                    }
                },
                files: {
                    "src/main/webapp/js/gen/template.js" : [basedir + "/js/app/templates/**/*.hbs"]
                }
            }
        },
        concat: {
            libs: {
                src: [
                    basedir + "/js/libs/jquery-2.1.1.js",
                    basedir + "/js/libs/jquery.mobile-1.4.5.js",
                    basedir + "/js/libs/jquery.iframe-transport.js",
                    basedir + "/js/libs/jquery.ui.widget.js",
                    basedir + "/js/libs/jquery.fileupload.js",
                    basedir + "/js/libs/handlebars-v2.0.0.js",
                    basedir + "/js/libs/moment-with-locale.js"
                ],
                dest: basedir + "/js/gen/lib.js"
            },
            common: {
                src: [
                    basedir + "/js/common/**/*.js"
                ],
                dest: basedir + "/js/gen/common.js"
            },
            app: {
                src: [
                    basedir + "/js/app/**/*.js"
                ],
                dest: basedir + "/js/gen/app.js"
            }
        },
        copy: {
            javascript: {
                expand: true,
                flatten: true,
                src: [basedir + "/js/gen/**"],
                dest: warPath + "/js/gen",
                filter: 'isFile'
            },
            css: {
                expand: true,
                cwd: basedir + "/css",
                flatten: false,
                src: ["**"],
                dest: warPath + "/css",
                filter: 'isFile'
            },
            html: {
                expand: true,
                cwd: basedir ,
                flatten: false,
                src: ["**/*.html", 'images/**'],
                dest: warPath,
                filter: 'isFile'
            },
            copy_after_minifying:{
                files:[
                    { expand: true, flatten: true, src: [basedir + "/temp/min/*.js"],  dest: basedir + "/js/gen",    filter: 'isFile'},
                    { expand: true, flatten: true, src: [basedir + "/temp/min/*.css"], dest: basedir + "/css", filter: 'isFile'}
                ]
            }
        },
        cssmin: {
            min_bower_css: {
                files: [{
                    expand: true,
                    cwd: basedir + "/css",
                    src: ['*.css'],
                    dest: basedir + "/temp/min",
                    ext: '.css'
                }]
            }
        },
        uglify: {
            options: {
                mangle: true
            },
            all: {
                files: [{
                    expand: true,
                    cwd: basedir + "/js/gen",
                    src: ['*.js', '!*.min.js'],
                    dest: basedir + "/temp/min",
                    ext: '.js'
                }]
            }
        },
        watch: {
            handlebars: {
                files: [basedir + "/js/app/templates/**/*.hbs"],
                tasks: ["handlebars"]
            },
            concat: {
                files: [basedir + "/js/common/**/*.js", basedir + "/js/app/**/*.js", basedir + "/js/libs/**/*.js"],
                tasks: ["concat"]
            },
            javascript: {
                files: [basedir + "/js/gen/**"],
                tasks: ["copy:javascript"]
            },
            css: {
                files: [basedir + "/css/**"],
                tasks: ["copy:css"]
            },
            html:{
                files: [basedir + "/**/*.html"],
                tasks: ["copy:html"]
            }
        },
        clean:{
            default:[basedir + "/temp"]
        }
    });

    grunt.loadNpmTasks("grunt-contrib-concat");
    grunt.loadNpmTasks("grunt-contrib-watch");
    grunt.loadNpmTasks("grunt-contrib-copy");
    grunt.loadNpmTasks('grunt-contrib-cssmin');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-cssmin');
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-handlebars');

    grunt.registerTask("default", function() {
        process.env['ENV'] = "prod";
        console.log("%s", process.env['ENV']);
        grunt.task.run("handlebars", "concat", "minify");
    });

    grunt.registerTask("dev", function() {
        process.env['ENV'] = "dev";
        console.log("%s", process.env['ENV']);
        grunt.task.run("handlebars", "concat", "copy:javascript", "copy:css", "copy:html", "watch");
    });

    grunt.registerTask("minify", function() {
        grunt.task.run("uglify", "cssmin", "copy:copy_after_minifying", "clean:default");
    });
};
