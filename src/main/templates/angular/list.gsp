<div class="" data-ng-controller="${className}ListCtrl as listCtrl" data-ng-init="listCtrl.list()">
    <div class="page-title">
        <div class="title_left">
            <h3>Manage ${domainClass.naturalName} </h3>
        </div>

        <div class="title_right">
            <div class="col-md-5 col-sm-5 col-xs-12 form-group pull-right top_search">
                <div class="input-group">
                    <input type="text" class="form-control" placeholder="Search for...">
                    <span class="input-group-btn">
                        <button class="btn btn-default" type="button">Go!</button>
                    </span>
                </div>
            </div>
        </div>
    </div>

    <div class="clearfix"></div>
    <div class="row">
        <div class="col-md-12 col-sm-12 col-xs-12">
            <div class="x_panel">
                <div class="x_title">
                    <h2>${domainClass.naturalName} List</h2>
                    <div class="clearfix"></div>
                </div>
                <div class="x_content">
                    <div class="dataTables_wrapper form-inline dt-bootstrap no-footer">
                        <div class="row">
                            <div class="col-sm-6">
                                <div class="dataTables_length">
                                    <label>Show
                                        <select data-ng-model="listCtrl.pageSize" aria-controls="datatable"
                                                class="form-control input-sm"
                                                data-ng-change="listCtrl.options.currentPage=1; listCtrl.pageChanged()"
                                                data-ng-options="option as option for option in [5,10,20,30,50, 60]"></select>
                                        entries</label>
                                </div>
                            </div>
                            <div class="col-sm-6">
                                <div class="dataTables_filter">
                                    <label>Search:<input type="search" class="form-control input-sm" placeholder=""
                                                         aria-controls="datatable"></label>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12">
                                <table class="table dataTable table-striped table-bordered">
                                    <thead>
                                    <tr>
                                        <th data-sort-field="id" data-on-change="listCtrl.sortChanged(sortField, sortOrder)">ID</th>
                                        <% fields.each{field ->%>
                                        <th data-sort-field="${field.property.name}" data-on-change="listCtrl.sortChanged(sortField, sortOrder)">
                                            ${field.property.naturalName}
                                        </th>
                                        <% } %>
                                        <th>Edit</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr data-ng-repeat="${propertyName} in listCtrl.listData.instances">
                                        <td><span data-ng-bind="${propertyName}.id"></span></td>
                                        <% fields.each{field ->%>
                                        <td>
                                        ${field.renderRead()}
                                        </td>
                                        <% } %>
                                        <td><span><a data-ui-sref="home.${propertyName}.edit({ id: ${propertyName}.id })"> Edit</a></span></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-5">
                                <div class="dataTables_info" id="datatable_info" role="status" aria-live="polite">
                                    Showing {{listCtrl.options.offset+1}} to
                                    {{listCtrl.options.offset+listCtrl.listData.instances.length}} of
                                    {{listCtrl.listData.total}} entries
                                </div>
                            </div>
                            <div class="col-sm-7">
                                <div class="dataTables_paginate paging_simple_numbers" id="datatable_paginate">
                                    <uib-pagination total-items="listCtrl.listData.total"
                                                    ng-model="listCtrl.options.currentPage"
                                                    max-size="10"
                                                    items-per-page="listCtrl.pageSize"
                                                    ng-change="listCtrl.pageChanged()"></uib-pagination>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
