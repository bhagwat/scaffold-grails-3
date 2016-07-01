'use strict';

var gulp = require('gulp');

var paths = gulp.paths;

var dollor = require('gulp-load-plugins')();

gulp.task('styles', function () {

  var sassOptions = {
    style: 'expanded',
    "sourcemap=none": true //hack to allow autoprefixer to work
  };

  var injectFiles = gulp.src([
    paths.src + '/{app,components,modules}/**/*.scss',
    '!' + paths.src + '/app/index.scss',
    '!' + paths.src + '/app/vendor.scss'
  ], { read: false });

  var injectOptions = {
    transform: function(filePath) {
      filePath = filePath.replace(paths.src + '/app/', '');
      filePath = filePath.replace(paths.src + '/components/', '../components/');
      filePath = filePath.replace(paths.src + '/modules/', '../modules/');
      return "@import '" + filePath + "';";
    },
    starttag: '// injector',
    endtag: '// endinjector',
    addRootSlash: false
  };

  var indexFilter = dollor.filter('index.scss');

  return gulp.src([
    paths.src + '/app/index.scss',
    paths.src + '/app/vendor.scss'
  ])
    .pipe(indexFilter)
    .pipe(dollor.inject(injectFiles, injectOptions))
    .pipe(indexFilter.restore())
    .pipe(dollor.rubySass(sassOptions)
      .on('error', function (err) {
        console.error('Error!', err.message);
      })
    )

  .pipe(dollor.autoprefixer())
    .on('error', function handleError(err) {
      console.error(err.toString());
      this.emit('end');
    })
    .pipe(gulp.dest(paths.tmp + '/serve/app/'));
});
