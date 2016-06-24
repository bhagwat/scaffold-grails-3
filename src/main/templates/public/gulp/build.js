'use strict';

var gulp = require('gulp');
var bower = require('gulp-bower');

var paths = gulp.paths;

var $ = require('gulp-load-plugins')({
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
        .pipe($.minifyHtml(minifyHtmlOptions))
        .pipe($.angularTemplatecache('templateCacheHtml.js', {
            module: 'angularMaterialAdmin'
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

    var htmlFilter = $.filter('*.html');
    var jsFilter = $.filter('**/*.js');
    var cssFilter = $.filter('**/*.css');
    var assets;

    return gulp.src(paths.tmp + '/serve/*.html')
        .pipe($.inject(partialsInjectFile, partialsInjectOptions))
        .pipe(assets = $.useref.assets())
        .pipe($.rev())
        .pipe(jsFilter)
        .pipe($.ngAnnotate())
        .pipe($.uglify({preserveComments: $.uglifySaveLicense}))
        .pipe(jsFilter.restore())
        .pipe(cssFilter)
        .pipe($.csso())
        .pipe(cssFilter.restore())
        .pipe(assets.restore())
        .pipe($.useref())
        .pipe($.revReplace())
        .pipe(htmlFilter)
        .pipe($.minifyHtml(minifyHtmlOptions))
        .pipe(htmlFilter.restore())
        .pipe(gulp.dest(paths.dist + '/'))
        .pipe($.size({title: paths.dist + '/', showFiles: true}));
});

gulp.task('images', function () {
    return gulp.src(paths.src + '/assets/images/**/*')
        .pipe(gulp.dest(paths.dist + '/assets/images/'));
});

gulp.task('fonts', function () {
    return gulp.src($.mainBowerFiles())
        .pipe($.filter('**/*.{eot,svg,ttf,woff}'))
        .pipe($.flatten())
        .pipe(gulp.dest(paths.dist + '/fonts/'));
});

gulp.task('misc', function () {
    return gulp.src(paths.src + '/**/*.ico')
        .pipe(gulp.dest(paths.dist + '/'));
});

gulp.task('clean', function (done) {
    $.del([paths.dist + '/', paths.tmp + '/'], done);
});

gulp.task('build', ['bower', 'html', 'images', 'fonts', 'misc']);
