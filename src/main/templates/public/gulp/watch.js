'use strict';

var gulp = require('gulp');

var paths = gulp.paths;

gulp.task('watch', ['inject'], function () {
  gulp.watch([
    paths.src + '/*.html',
    paths.src + '/{app,components,modules}/**/*.html',
    paths.src + '/{app,components,modules}/**/*.scss',
    paths.src + '/{app,components,modules}/**/*.js',
    'bower.json'
  ], ['inject']);
});
