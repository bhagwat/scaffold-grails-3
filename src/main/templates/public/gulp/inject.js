'use strict';

var gulp = require('gulp');

var paths = gulp.paths;

var dollor = require('gulp-load-plugins')();

var wiredep = require('wiredep').stream;

gulp.task('inject', ['styles'], function () {
    var injectStyles = gulp.src([
        paths.tmp + '/serve/{app,components,modules}/**/*.css',
        '!' + paths.tmp + '/serve/app/vendor.css'
    ], {read: false});

    var injectScripts = gulp.src([
        paths.src + '/{app,components,modules}/**/*.js',
        '!' + paths.src + '/{app,components,modules}/**/*.spec.js',
        '!' + paths.src + '/{app,components,modules}/**/*.mock.js'
    ]).pipe(dollor.angularFilesort());

    var injectOptions = {
        ignorePath: [paths.src, paths.tmp + '/serve'],
        addRootSlash: false
    };

    var wiredepOptions = {
    };

    return gulp.src(paths.src + '/*.html')
        .pipe(dollor.inject(injectStyles, injectOptions))
        .pipe(dollor.inject(injectScripts, injectOptions))
        .pipe(wiredep(wiredepOptions))
        .pipe(gulp.dest(paths.tmp + '/serve'));
});
