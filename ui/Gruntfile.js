module.exports = function(grunt) {
    var basedir = "src/main/webapp";
    var warPath = "target/ui-1.0.0-SNAPSHOT/js/gen";

    grunt.initConfig({
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
        concat: {
            libs: {
                src: [
                    basedir + "/js/libs/**/*.js"
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
                dest: warPath,
                filter: 'isFile'
            },
            css: {
                expand: true,
                flatten: true,
                src: [basedir + "/css/**"],
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
        watch: {
            concat: {
                files: [basedir + "/js/common/**/*.js", basedir + "/js/app/**/*.js", basedir + "/js/libs/**/*.js"],
                tasks: ["concat"]
            },
            copy: {
                files: [basedir + "/js/gen/**"],
                tasks: ["copy:javascript"]
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

    grunt.registerTask("default", function() {
        process.env['ENV'] = "prod";
        console.log("%s", process.env['ENV']);
        grunt.task.run("concat", "copy:javascript", "minify");
    });

    grunt.registerTask("dev", function() {
        process.env['ENV'] = "dev";
        console.log("%s", process.env['ENV']);
        grunt.task.run("concat", "copy:javascript", "watch");
    });

    grunt.registerTask("minify", function() {
        grunt.task.run("uglify", "cssmin", "copy:copy_after_minifying", "clean:default");
    });
};
