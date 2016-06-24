'use strict';

angular.module('app')
    .directive('sortField', [function () {
        return {
            restrict: 'A',
            scope: {
                sortField: "@",
                onChange: "&"
            },
            link: function (scope, element) {
                element.addClass("sorting");
                element.on('click', function () {
                    var sortOrder = scope.sortOrder;
                    if (!sortOrder) {
                        sortOrder = 'asc';
                    } else if (sortOrder == 'asc') {
                        sortOrder = 'desc';
                    } else if (sortOrder == 'desc') {
                        sortOrder = 'asc';
                    }
                    var siblings = element.parent().children('[sort-field]');
                    siblings.removeClass('sorting_asc');
                    siblings.removeClass('sorting_desc');
                    siblings.addClass('sorting');
                    element.removeClass('sorting');
                    element.addClass("sorting_"+sortOrder);
                    scope.sortOrder = sortOrder;
                    scope.onChange({sortField: scope.sortField, sortOrder: scope.sortOrder});
                })
            }
        };
    }]);
