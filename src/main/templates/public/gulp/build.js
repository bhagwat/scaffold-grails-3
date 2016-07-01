'use strict';

var gulp = require('gulp');
var bower = require('gulp-bower');

var paths = gulp.paths;

var dollor = require('gulp-load-plugins')({
    pattern: ['gulp-*', 'main-bower-files', 'uglify-save-license', 'del']
});

var minifyHtmlOptions = {
    caseSensitive: true,
    keepClosingSlash: true,
    removeAttributeQuotes: false,
    empty: true,
    spare: true,
    quotes: true
};

gulp.task('bower', function () {
    return bower();
});

gulp.task('partials', function () {
    return gulp.src([
        paths.src + '/{app,components,modules}/**/*.html',
        paths.tmp + '/{app,components,modules}/**/*.html'
    ])
        .pipe(dollor.minifyHtml(minifyHtmlOptions))
        .pipe(dollor.angularTemplatecache('templateCacheHtml.js', {
            module: "${moduleName}Admin"
        }))
        .pipe(gulp.dest(paths.tmp + '/partials/'));
});

gulp.task('html', ['inject', 'partials'], function () {
    var partialsInjectFile = gulp.src(paths.tmp + '/partials/templateCacheHtml.js', {read: false});
    var partialsInjectOptions = {
        starttag: '<!-- inject:partials -->',
        ignorePath: paths.tmp + '/partials',
        addRootSlash: false
    };

    var htmlFilter = dollor.filter('*.html');
    var jsFilter = dollor.filter('**/*.js');
    var cssFilter = dollor.filter('**/*.css');
    var assets;

    return gulp.src(paths.tmp + '/serve/*.html')
        .pipe(dollor.inject(partialsInjectFile, partialsInjectOptions))
        .pipe(assets = dollor.useref.assets())
        .pipe(dollor.rev())
        .pipe(jsFilter)
        .pipe(dollor.ngAnnotate())
        .pipe(dollor.uglify({preserveComments: dollor.uglifySaveLicense}))
        .pipe(jsFilter.restore())
        .pipe(cssFilter)
        .pipe(dollor.csso())
        .pipe(cssFilter.restore())
        .pipe(assets.restore())
        .pipe(dollor.useref())
        .pipe(dollor.revReplace())
        .pipe(htmlFilter)
        .pipe(dollor.minifyHtml(minifyHtmlOptions))
        .pipe(htmlFilter.restore())
        .pipe(gulp.dest(paths.dist + '/'))
        .pipe(dollor.size({title: paths.dist + '/', showFiles: true}));
});

gulp.task('images', function () {
    return gulp.src(paths.src + '/assets/images/**/*')
        .pipe(gulp.dest(paths.dist + '/assets/images/'));
});

gulp.task('fonts', function () {
    return gulp.src(dollor.mainBowerFiles())
        .pipe(dollor.filter('**/*.{eot,svg,ttf,woff}'))
        .pipe(dollor.flatten())
        .pipe(gulp.dest(paths.dist + '/fonts/'));
});

gulp.task('misc', function () {
    return gulp.src(paths.src + '/**/*.ico')
        .pipe(gulp.dest(paths.dist + '/'));
});

gulp.task('clean', function (done) {
    dollor.del([paths.dist + '/', paths.tmp + '/'], done);
});

gulp.task('build', ['bower', 'html', 'images', 'fonts', 'misc']);
